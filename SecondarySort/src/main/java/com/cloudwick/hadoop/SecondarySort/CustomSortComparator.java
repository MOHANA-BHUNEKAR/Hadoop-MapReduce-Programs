package com.cloudwick.hadoop.SecondarySort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class CustomSortComparator extends WritableComparator{

	protected CustomSortComparator() {
		super(CompositeKeyWritable.class, true);
	}
	
	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		CompositeKeyWritable key1 = (CompositeKeyWritable) w1;
		CompositeKeyWritable key2 = (CompositeKeyWritable) w2;
 
		int cmpResult = key1.getDeptId().compareTo(key2.getDeptId());
		if (cmpResult == 0)// same deptNo
		{
			//such that dept value comes 1st in the output
			return key1.getFlag() - key2.getFlag(); 
		}
		return cmpResult;
	}
 

}
