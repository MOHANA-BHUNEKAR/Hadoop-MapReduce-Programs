package com.cloudwick.hadoop.CustomCounters;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
 

public class DriverManager extends Configured implements Tool{
	public enum LOG_COUNTERS {
		  INFO,
		  DEBUG,
		  EXCEPTION
		 }
	
	public int run(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.printf(
                    "Usage: %s [generic options] <input dir> <output dir>\n", getClass()
                    .getSimpleName());
            ToolRunner.printGenericCommandUsage(System.out);
            return -1;
        }

          
        Job job = new Job(getConf());
        job.setJarByClass(DriverManager.class);
        job.setJobName(this.getClass().getName());
        job.setNumReduceTasks(0);
        

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(CountersMapper.class); 

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        if (job.waitForCompletion(true)) {
        	Counters counters = job.getCounters();
        	


      	  System.out.printf("INFO: %d, DEBUG: %d, EXCEPTION: %d\n",
      	      counters.findCounter(LOG_COUNTERS.INFO).getValue(),
      	      counters.findCounter(LOG_COUNTERS.DEBUG).getValue(),
      	  counters.findCounter(LOG_COUNTERS.EXCEPTION).getValue());
      	 
        	
            return 0;
        }
        return 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new DriverManager(), args);
        
        System.exit(exitCode);
    }
}

