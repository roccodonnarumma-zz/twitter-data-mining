package com.project.batch.driver;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import com.project.batch.map.MovieTweetMapper;
import com.project.batch.model.Tweet;
import com.project.batch.reduce.MovieTweetReducer;

public class MovieTweetDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length != 1) {
            System.err.println("Usage: MovieTweetDriver <in>");
            System.exit(2);
        }

        String input = otherArgs[0];

        Path output = new Path("/tmp");
        while (true) {
            output.getFileSystem(conf).delete(output, true);

            Job job = Job.getInstance(conf);
            job.setJarByClass(MovieTweetDriver.class);
            job.setJobName("Movie Tweet Batch");
            job.setMapperClass(MovieTweetMapper.class);
            job.setReducerClass(MovieTweetReducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Tweet.class);
            job.setOutputKeyClass(NullWritable.class);
            job.setOutputValueClass(NullWritable.class);
            FileInputFormat.addInputPath(job, new Path(input));
            FileOutputFormat.setOutputPath(job, output);
            job.waitForCompletion(true);
            Thread.sleep(120000);
        }
    }
}
