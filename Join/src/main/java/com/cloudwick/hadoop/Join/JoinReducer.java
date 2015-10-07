package com.cloudwick.hadoop.Join;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class JoinReducer extends Reducer<IntWritable, Text, IntWritable, Text>{
	@Override
	protected void reduce(
			IntWritable key,
			Iterable<Text> values,
			Reducer<IntWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//super.reduce(arg0, arg1, arg2);
		  
		List<String> listRecords = new ArrayList<String>();
		String deptName = null ;
		
		//loop through values - value with , is a record from employee and without , is from department (works only is department has 2 columns (id,name) 
		for (Text value : values) {
			if(value.toString().contains(",")) {
				listRecords.add(value.toString());
			}
			else {
				deptName = value.toString();
			}
		}
		for (String record : listRecords) {
			String [] empRecord = record.split(",");
			context.write(new IntWritable(Integer.parseInt(empRecord[0])), new Text(empRecord[1] + "," + deptName));
		}
		
	}
}
