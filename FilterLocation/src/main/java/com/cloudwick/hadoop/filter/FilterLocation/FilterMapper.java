package com.cloudwick.hadoop.filter.FilterLocation;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FilterMapper extends Mapper<LongWritable,Text,IntWritable,Text>{
 
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//super.map(key, value, context);
		String line = value.toString();
		String[] arrayRecord = line.split(",");
		if(arrayRecord!=null && arrayRecord.length == 3 && arrayRecord[1].toString().trim().equals("CA"))
			context.write(new IntWritable(Integer.parseInt(arrayRecord[0])),new Text(arrayRecord[1] + "," + arrayRecord[2]));
	}
 

}
