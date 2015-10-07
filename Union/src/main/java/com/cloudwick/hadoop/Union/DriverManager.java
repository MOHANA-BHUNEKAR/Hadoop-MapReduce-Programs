package com.cloudwick.hadoop.Union;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
 

public class DriverManager extends Configured implements Tool{
	public int run(String[] args) throws Exception {

        if (args.length != 3) {
            System.out.printf(
                    "Usage: %s [generic options] <input dir A> <input dir B> <output dir>\n", getClass()
                    .getSimpleName());
            ToolRunner.printGenericCommandUsage(System.out);
            return -1;
        }
         
        Job job = new Job(getConf());
        job.setJarByClass(DriverManager.class);
        job.setJobName(this.getClass().getName());

        Path pathA=new Path(args[0]);
        Path pathB=new Path(args[1]);
        
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        
        job.setMapperClass(UnionMapper.class); 
        job.setReducerClass(UnionReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        //MultipleInputs allows to have multiple input paths with a different InputFormat
        MultipleInputs.addInputPath(job, pathA, TextInputFormat.class);
        MultipleInputs.addInputPath(job, pathB, TextInputFormat.class);

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
