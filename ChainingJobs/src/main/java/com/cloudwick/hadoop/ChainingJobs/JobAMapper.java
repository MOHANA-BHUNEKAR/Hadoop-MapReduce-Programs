package com.cloudwick.hadoop.ChainingJobs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class JobAMapper extends Mapper<LongWritable,Text,IntWritable,Text>{
	String locationFilter = null;
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//super.map(key, value, context);
		String line = value.toString();
		String[] arrayRecord = line.split(",");
		Configuration conf = context.getConfiguration();
		locationFilter = conf.get("location").toLowerCase().trim();
		if(arrayRecord!=null && arrayRecord.length == 4 && arrayRecord[2].toLowerCase().trim().equals(locationFilter))
			context.write(new IntWritable(Integer.parseInt(arrayRecord[0])),new Text(arrayRecord[0] + "," +arrayRecord[1] + "," + arrayRecord[2]+ "," + arrayRecord[3]));
		
	}

}
