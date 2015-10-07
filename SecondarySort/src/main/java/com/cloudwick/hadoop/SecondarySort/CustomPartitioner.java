package com.cloudwick.hadoop.SecondarySort;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class CustomPartitioner extends Partitioner<CompositeKeyWritable, Text>{

	@Override
	public int getPartition(CompositeKeyWritable key, Text value, int numPartitions) {
		// TODO Auto-generated method stub
		//return 0;
		return (key.getDeptId().hashCode() % numPartitions);
	}

}
