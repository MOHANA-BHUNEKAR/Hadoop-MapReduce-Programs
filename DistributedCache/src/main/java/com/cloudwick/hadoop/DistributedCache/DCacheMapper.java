package com.cloudwick.hadoop.DistributedCache;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DCacheMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
	private static HashMap<Integer, String> deptHashMap = new HashMap<Integer, String>();
	private BufferedReader bufferedReader;
	private String deptName = ""; 

 

	@Override
	protected void setup(
			Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		// super.setup(context);
		Path[] cacheFilesLocal = DistributedCache.getLocalCacheFiles(context
				.getConfiguration());

		for (Path eachPath : cacheFilesLocal) {
			if (eachPath.getName().toString().trim().equals("dept.txt")) { 
				loadDepartmentsHashMap(eachPath, context);
			}
		}
	}

	private void loadDepartmentsHashMap(Path filePath, Context context)
			throws IOException {

		String strLineRead = "";

		try {
			bufferedReader = new BufferedReader(new FileReader(filePath.toString()));

			// Read each line, split and load to HashMap
			while ((strLineRead = bufferedReader.readLine()) != null) {
				String deptFieldArray[] = strLineRead.split(",");
				deptHashMap.put(Integer.parseInt(deptFieldArray[0].trim()),
						deptFieldArray[1].trim());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
	}

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		// super.map(key, value, context); 

		String arrEmpValues[] = value.toString().split(",");

		try {
			deptName = deptHashMap.get(Integer
					.parseInt(arrEmpValues[2].trim()));
		} finally {
			deptName = ((deptName.equals(null) || deptName.equals("")) ? "NOT-FOUND"
					: deptName);
		}

		context.write(new IntWritable(Integer.parseInt(arrEmpValues[0])),
				new Text(arrEmpValues[1] + "," + deptName));
		deptName = "";
	}
}
