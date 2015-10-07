package com.cloudwick.hadoop.Zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.log4j.Logger;

public class ZipFileRecordReader extends RecordReader<Text, BytesWritable> {
	private Logger logger = Logger.getLogger(ZipFileRecordReader.class);

	private FSDataInputStream fsin;
	private ZipInputStream zip;
	private Text currentKey;
	private BytesWritable currentValue;
	private boolean isFinished = false;
	private Configuration conf;

	@Override
	public void initialize(InputSplit inputSplit,
			TaskAttemptContext taskAttemptContext) throws IOException,
			InterruptedException {
		conf = taskAttemptContext.getConfiguration();
		Path path = ((FileSplit) inputSplit).getPath();
		FileSystem fs = path.getFileSystem(conf);
		fsin = fs.open(path);
		zip = new ZipInputStream(fsin);
	}

	/* No filter by filename inside zip files--start */
//	@Override
//	public boolean nextKeyValue() throws IOException, InterruptedException
//	{
//		ZipEntry entry = zip.getNextEntry();
//		if ( entry == null )
//		{
//			isFinished = true;
//			return false;
//		}
//		// Set the key 
//		currentKey = new Text( entry.getName() );
//		
//		// Set the value
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		byte[] temp = new byte[8192];
//		while ( true )
//		{
//			int bytesRead = zip.read(temp, 0, 8192);
//			if ( bytesRead > 0 )
//				bos.write(temp, 0, bytesRead);
//			else
//				break;
//		}
//		zip.closeEntry();
//		currentValue = new BytesWritable( bos.toByteArray() );
//		return true;
//	}
	/* No filter by filename inside zip files--end */
	
	/* Filter files by filename inside zip files--start */
	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException { 
		return getNextKey();
		
	}
	
	public boolean getNextKey() throws IOException{
		ZipEntry entry = zip.getNextEntry();
		if (entry == null) {
			isFinished = true;
			return false;
		}
		else
			return patternEntryCheck(entry);
	}
	
	public boolean patternEntryCheck(ZipEntry entry) throws IOException{ 
		 String fileName = entry.getName(); 
		 
		 //file.pattern passed as -Dfile.pattern=pattern
		 String file_pattern = conf.get("file.pattern");
		 Pattern pattern = null;
		 Matcher matcher = null;
		 
		 if (file_pattern != null) {
			 pattern = Pattern.compile(file_pattern.toLowerCase().trim());
			 matcher = pattern.matcher(fileName.toLowerCase());
		 }
		
		
		if (matcher.find()) { 
			// Set the key
			currentKey = new Text(entry.getName());

			// Set the value
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] temp = new byte[8192];
			while (true) {
				int bytesRead = zip.read(temp, 0, 8192);
				if (bytesRead > 0)
					bos.write(temp, 0, bytesRead);
				else
					break;
			}
			zip.closeEntry();
			currentValue = new BytesWritable(bos.toByteArray());
			return true;
		} else {
			//if file not matched with pattern skip the file and move to next file
			return getNextKey();
		}
	}
	/* Filter files by filename inside zip --end */

	@Override
	public float getProgress() throws IOException, InterruptedException {
		return isFinished ? 1 : 0;
	}

	@Override
	public Text getCurrentKey() throws IOException, InterruptedException {
		return currentKey;
	}

	@Override
	public BytesWritable getCurrentValue() throws IOException,
			InterruptedException {
		return currentValue;
	}

	@Override
	public void close() throws IOException {
		try {
			zip.close();
		} catch (Exception e) {
		}
		try {
			fsin.close();
		} catch (Exception e) {
		}
	}

}
