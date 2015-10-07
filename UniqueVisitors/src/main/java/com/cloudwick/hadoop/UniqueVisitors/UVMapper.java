package com.cloudwick.hadoop.UniqueVisitors;
 
import java.io.IOException;
 
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class UVMapper extends Mapper<LongWritable, Text, Text, Text>{ 

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//super.map(key, value, context);
		
		//ex record sai-www.google.com 
		String line = value.toString();
		String[] arrayLog = line.split("-"); 
		
		//make www.google.com as key and value as user
		context.write(new Text(arrayLog[1]), new Text(arrayLog[0]));
	}
}
