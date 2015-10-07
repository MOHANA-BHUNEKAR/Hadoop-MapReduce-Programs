package com.cloudwick.hadoop.Join;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class EmpMapper extends Mapper<LongWritable, Text, IntWritable, Text>{
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//super.map(key, value, context);
		
		//ex record: 1001,sai,101 (empid,empName,deptId)
		String[] empValues = value.toString().split(","); 
		context.write(new IntWritable(Integer.parseInt(empValues[2])), new Text(empValues[0]+","+empValues[1]));
	}
}
