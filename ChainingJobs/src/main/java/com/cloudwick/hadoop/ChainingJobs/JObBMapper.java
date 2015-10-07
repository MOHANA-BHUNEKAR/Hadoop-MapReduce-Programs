package com.cloudwick.hadoop.ChainingJobs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable; 
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class JObBMapper extends Mapper<IntWritable,Text,IntWritable,Text>{
	int salaryFilter ;
	protected void map(IntWritable key, Text value, org.apache.hadoop.mapreduce.Mapper<IntWritable,Text,IntWritable,Text>.Context context) throws IOException ,InterruptedException {
		String line = value.toString();
		String[] arrayRecord = line.split(",");
		Configuration conf = context.getConfiguration();
		salaryFilter =  Integer.parseInt(conf.get("salary")); 
		if(arrayRecord!=null  && Integer.parseInt(arrayRecord[3]) > salaryFilter)
			context.write(new IntWritable(Integer.parseInt(arrayRecord[0].trim())),new Text(arrayRecord[1] + "," + arrayRecord[2]+ "," + arrayRecord[3]));
	}; 
}