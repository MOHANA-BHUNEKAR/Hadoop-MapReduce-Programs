package com.cloudwick.hadoop.sftp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; 
 

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class ZipFileInputFormat extends FileInputFormat<Text, BytesWritable> {
	
	private static final String path_sftp_properties = "config.properties";
	
	@Override
	/**
	 * We tell hadoop not to split the zip file.
	 */
	protected boolean isSplitable(org.apache.hadoop.mapreduce.JobContext ctx,
			Path filename) {
		return false;
	}

	@Override
	public RecordReader<Text, BytesWritable> createRecordReader(
			InputSplit split, TaskAttemptContext context) throws IOException,
			InterruptedException {
		return new ZipFileRecordReader();
	}

	
	/*returns list of splits (each split referring to one zip file). 
	 * Since the config file has source-directory, all we need is the zip file name to fetch the data.
	 * We can create append location or any other information appended to the string and fetch the relevant information later.
	 */
	@Override
	public List<InputSplit> getSplits(JobContext arg0) throws IOException {
		// TODO Auto-generated method stub
		// return super.getSplits(arg0);
		List<InputSplit> splits = new ArrayList<InputSplit>();

		try {
			Properties prop = new Properties();
			prop.load(this.getClass().getClassLoader().getResourceAsStream(
					path_sftp_properties));
			
			System.out.println(prop.getProperty("source-user"));
			System.out.println(prop.getProperty("source-host"));
			System.out.println(prop.getProperty("source-password"));
			System.out.println(prop.getProperty("source-directory"));
			
			SFTPClient sftpClient = new SFTPClient(prop); 
			
			List<String> fileNames = sftpClient.getZipFileNames(prop);
			for (String fileName : fileNames) {
				String sourceName = fileName;
				splits.add(new SFTPZipsListInputSplit(sourceName));
			}
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return splits;
	}
}