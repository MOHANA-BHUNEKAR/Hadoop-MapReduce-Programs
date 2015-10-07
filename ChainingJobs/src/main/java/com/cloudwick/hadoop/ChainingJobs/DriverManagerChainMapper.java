package com.cloudwick.hadoop.ChainingJobs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class DriverManagerChainMapper extends Configured implements Tool {

	public int run(String[] args) throws Exception {

		if (args.length != 4) {
			System.out
					.printf("Usage: %s [generic options] <input dir> <output dir> <location filter> <salary filter>\n",
							getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.out);
			return -1;
		}
		Configuration conf = new Configuration();
		conf.set("location", args[2]);
		conf.set("salary", args[3]);
		Job job = new Job(conf);
		job.setJarByClass(DriverManager.class);
		job.setJobName(this.getClass().getName());
		job.setNumReduceTasks(0);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		//The ChainMapper class allows to use multiple Mapper classes within a single Map task.
		//The Mapper classes are invoked in a chained (or piped) fashion, the output of the first becomes the input of the second, 
		//and so on until the last Mapper, the output of the last Mapper will be written to the task's output.
		//The key functionality of this feature is that the Mappers in the chain do not need to be aware that they are executed in a chain.
		Configuration confA = new Configuration(false);
		ChainMapper.addMapper(job, JobAMapper.class, LongWritable.class,
				Text.class, IntWritable.class, Text.class, confA);
		
		//Make sure output of MapperA similart to input for Mapper B
		Configuration confB = new Configuration(false);
		ChainMapper.addMapper(job, JObBMapper.class, IntWritable.class,
				Text.class, IntWritable.class, Text.class, confB);
 

		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);

        if (job.waitForCompletion(true)) {
            return 0;
        }
        return 1; 

	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new DriverManagerChainMapper(), args);
		System.exit(exitCode);
	}

}
