package com.taobao.mc.search.job;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.lib.MultipleInputs;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 
 * <pre>
 * desc: 得到线索图片信息，包括tag
 * created: Mar 7, 2013 4:06:17 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
@SuppressWarnings("deprecation")
public class FetchFullIndexDataJob extends Configured implements Tool {
	private static final String TI = "TI";
	private static final String FI = "FI";
	private static final String SEPARATOR_DOUBLE_STAR = "%!%";
	private static final String SEPARATOR_TAB = "\t";
	private static final String SEPARATOR_SPACE = " ";
	private static final String TAG_TI = TI + SEPARATOR_DOUBLE_STAR;
	private static final String TAG_FI = FI + SEPARATOR_DOUBLE_STAR;

	public int run(String[] args) throws Exception {
		JobConf c = new JobConf(getConf(), FetchFullIndexDataJob.class);
		c.setJobName("FetchFullIndexData");

		c.setOutputKeyClass(Text.class);
		c.setOutputValueClass(Text.class);

		// c.setMapperClass(TagPicMiscListMapper.class);
		// c.setMapperClass(FileTagMapper.class);
		c.setReducerClass(GetFileFullInfoReduce.class);

		// c.setInputFormat(SequenceFileAsTextInputFormat.class);
		// c.setOutputFormat(TextOutputFormat.class);

		c.setMaxMapTaskFailuresPercent(5);
		c.setNumReduceTasks(100);		

		Calendar today = Calendar.getInstance();
		String inputPath1 = MessageFormat.format(
				"/group/shop-tadget/file_tag_mysql_virtual_db/{0,date,yyyyMMdd}/out/*",
				today.getTime());

		today.add(Calendar.DAY_OF_MONTH, -1);
		String inputPath2 = MessageFormat.format(
				"/group/taobao/taobao/hive/s_file/pt={0,date,yyyyMMdd000000}/*",
				today.getTime());

		// String inputPath1 = "/group/shop-tadget/file_tag_mysql_virtual_db/20130321/out/*";
		// String inputPath2 = "/group/taobao/taobao/hive/s_file/pt={0,date,yyyyMMdd}/*";

		// for (int i = 0; i < args.length; i++) {
		// if ("-input".endsWith(args[i])) {
		// inputPath = args[i + 1];
		// }
		// }

		String outputPath = MessageFormat.format(
				"/group/shop-tadget/file_info_mysql_virtual_db/{0,date,yyyyMMdd}/out/",
				new Date());

		for (int i = 0; i < args.length; i++) {
			if ("-output".endsWith(args[i])) {
				outputPath = args[i + 1];
			}
		}

		MultipleInputs.addInputPath(c, new Path(inputPath1), TextInputFormat.class,
				TagListMapper.class);
		MultipleInputs.addInputPath(c, new Path(inputPath2), TextInputFormat.class, FileInfoMapper.class);

		// SequenceFileInputFormat.setInputPaths(c, new Path(inputPath1));
		FileOutputFormat.setOutputPath(c, new Path(outputPath));
		JobClient.runJob(c);
		
		return 0;
	}

	public static class TagListMapper extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, Text> {

		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output,
				Reporter reporter) throws IOException {
			String val = new String(value.getBytes(), 0, value.getLength(), "UTF-8");
			reporter.incrCounter("TagListMapper", "record", 1);

			output.collect(new Text(key.toString()), new Text(TAG_TI + val));
		}
	}

	public static class FileInfoMapper extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, Text> {

		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output,
				Reporter reporter) throws IOException {
			String val = new String(value.getBytes(), 0, value.getLength(), "UTF-8");
			String[] fields = val.split(SEPARATOR_TAB);
			if (fields != null && fields.length == 21) {
				reporter.incrCounter("FileInfoMapper", "record", 1);
				// long parseLong = 0L;
				// parseLong = Long.parseLong(fields[4]);
				output.collect(new Text(fields[0]), new Text(TAG_FI + val));
			}
		}
	}

	public static class GetFileFullInfoReduce extends MapReduceBase implements
			Reducer<Text, Text, Text, Text> {
		private String tag, fileInfo;

		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<Text, Text> output,
				Reporter reporter) throws IOException {

			List<String> fileIdTotag = new ArrayList<String>();
			List<String> fileIdToFileId = new ArrayList<String>();
			while (values.hasNext())
			{
				String currValue = values.next().toString();
				String valueSplitted[] = currValue.split(SEPARATOR_DOUBLE_STAR);
				if (valueSplitted[0].equals(TI))
				{
					tag = valueSplitted[1].trim();
					if (tag != null) {
						fileIdTotag.add(tag);
					}
				}
				else if (valueSplitted[0].equals(FI))
				{
					fileInfo = valueSplitted[1].trim();
					if (fileInfo != null) {
						fileIdToFileId.add(fileInfo);
					}
				}
			}

			// 合并两个list
			for (String fileInfo : fileIdToFileId) {
				reporter.incrCounter("GetFileFullInfoReduce", "record", 1);
				int i = 0;
				for (String tag : fileIdTotag) {
					if (i == 0) {
						fileInfo = fileInfo + SEPARATOR_TAB + tag;
					} else {
						fileInfo = fileInfo + SEPARATOR_SPACE + tag;
					}
				}
				output.collect(key, new Text(fileInfo));
			}
		}
	}

	public static void main(String[] args) throws Exception {
		StringBuilder logContext = new StringBuilder();

		FetchTagsJob job = new FetchTagsJob();
		try {
			int rs = ToolRunner.run(job, args);
			if (rs == 0) {
				System.err.print("finish success 1");

				FetchFullIndexDataJob job2 = new FetchFullIndexDataJob();
				rs = ToolRunner.run(job2, args);
				if (rs == 0) {
					System.err.print("finish success 2");
				} else {
					System.err.print("finish failed 2");
					logContext.append("FetchFullIndexDataJob").append(" failure ...\n");
				}

			} else {
				System.err.print("finish failed 1");
				logContext.append("FetchTagsJob").append(" failure ...\n");
			}
		} catch (Exception e) {
			logContext.append(e.getMessage()).append("\n");
		}

		String fileName = MessageFormat.format("./wrong-indexs-{0,date,yyyyMMdd}", new Date());
		File logFile = new File(fileName);

		FileWriter fileWriter = null;
		try {
			if (logFile.exists())
				logFile.delete();

			logFile.createNewFile();
			fileWriter = new FileWriter(logFile, true);
			fileWriter.write(logContext.toString());
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		System.exit(0);
	}
}