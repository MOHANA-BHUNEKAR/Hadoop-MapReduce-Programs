package com.cloudwick.hadoop.ChainingJobs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;   

//THis is not a good way to implement. Manually created two jobs. Right method implemented in DriverManagerChainMapper.java
public class DriverManager extends Configured implements Tool {

	public int run(String[] args) throws Exception {

		if (args.length != 5) {
			System.out
					.printf("Usage: %s [generic options] <input dir> <intermediate output dir> <output dir> <location filter> <salary filter>\n",
							getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.out);
			return -1;
		}
		
		/* JOB A -- start */
		Configuration confA = new Configuration();
		confA.set("intermediateOutput", args[1]);
		confA.set("location", args[3]);
		Job jobA = new Job(confA, "filter location job");
		jobA.setJarByClass(DriverManager.class);
		jobA.setJobName(this.getClass().getName());

		// no reducers are required for this job
		jobA.setNumReduceTasks(0);

		jobA.setMapperClass(JobAMapper.class);
		jobA.setMapOutputKeyClass(IntWritable.class);
		jobA.setMapOutputValueClass(Text.class);

		jobA.setOutputKeyClass(IntWritable.class);
		jobA.setOutputValueClass(Text.class);

		FileInputFormat.setInputPaths(jobA, new Path(args[0]));

		FileOutputFormat.setOutputPath(jobA, new Path(args[1]));

		jobA.waitForCompletion(true);

		/* JOB A -- end */

		/* JOB B -- start */

		Configuration confB = new Configuration();
		confB.set("salary", args[4]);
		Job jobB = new Job(confB, "filter salary job");
		jobB.setJarByClass(DriverManager.class);
		jobB.setJobName(this.getClass().getName());
		jobB.setMapperClass(JObBMapper.class);
		jobB.setNumReduceTasks(0);

		jobB.setMapOutputKeyClass(IntWritable.class);
		jobB.setMapOutputValueClass(Text.class);

		jobB.setOutputKeyClass(IntWritable.class);
		jobB.setOutputValueClass(Text.class);

		FileInputFormat.setInputPaths(jobB, new Path(args[1]));
		FileOutputFormat.setOutputPath(jobB, new Path(args[2]));

		return jobB.waitForCompletion(true) ? 0 : 1;

		/* JOB B -- end */

		// jobA.setInputFormatClass(TextInputFormat.class);
		// jobA.setOutputFormatClass(TextOutputFormat.class);
		// TextOutputFormat.setOutputPath(jobA, new Path(args[1]));
		// FileInputFormat.setInputPaths(jobA, new Path(args[0]));
		// TextInputFormat.addInputPath(jobB, new Path(args[1]));

	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new DriverManager(), args);
		System.exit(exitCode);
	}

}
