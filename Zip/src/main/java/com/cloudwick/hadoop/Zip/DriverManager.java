package com.cloudwick.hadoop.Zip;
 
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path; 
import org.apache.hadoop.io.Text;  
import org.apache.hadoop.mapreduce.Job;   
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
 
 
public class DriverManager extends Configured implements Tool { 
	 public int run(String[] args) throws Exception {

	        if (args.length != 2) {
	            System.out.printf(
	                    "Usage: %s [generic options] <input dir> <output dir> <filter files pattern> \n", getClass()
	                    .getSimpleName());
	            ToolRunner.printGenericCommandUsage(System.out);
	            return -1;
	        }
	         
	        Job job = new Job(getConf());
	        job.setJarByClass(DriverManager.class);
	        job.setJobName(this.getClass().getName());
	        job.setNumReduceTasks(0);

	        //ZipFileInputFormat.setInputPathFilter(job, RegexFilter.class);
	        ZipFileInputFormat.setInputPaths(job,  new Path(args[0]));
	        FileOutputFormat.setOutputPath(job, new Path(args[1]));

	        job.setMapperClass(ZipMapper.class); 
	        
	        job.setInputFormatClass(ZipFileInputFormat.class);
	        job.setOutputFormatClass(TextOutputFormat.class);

	        job.setMapOutputKeyClass(Text.class);
	        job.setMapOutputValueClass(Text.class);

	        job.setOutputKeyClass(Text.class);
	        job.setOutputValueClass(Text.class);

	        if (job.waitForCompletion(true)) { 
	            return 0;
	        }
	        return 1;
	    }

	    public static void main(String[] args) throws Exception {
	        int exitCode = ToolRunner.run(new DriverManager(), args);
	        System.exit(exitCode);
	    }

}