package com.cloudwick.hadoop.SecondarySort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class CustomGroupComparator extends WritableComparator{
	  protected CustomGroupComparator() {
			super(CompositeKeyWritable.class, true);
		}
	  
	  @Override
		public int compare(WritableComparable w1, WritableComparable w2) {
			CompositeKeyWritable key1 = (CompositeKeyWritable) w1;
			CompositeKeyWritable key2 = (CompositeKeyWritable) w2;
			return key1.getDeptId().compareTo(key2.getDeptId());
		}
}
