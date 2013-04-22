package com.taobao.mc.search.job;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileAsTextInputFormat;
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
public class FetchTagsJob extends Configured implements Tool {
	private static final String TP = "TP";
	private static final String FT = "FT";
	private static final String SEPARATOR_DOUBLE_STAR = "%!%";
	private static final String TAG_TP = TP + SEPARATOR_DOUBLE_STAR;
	private static final String TAG_FT = FT + SEPARATOR_DOUBLE_STAR;

	public int run(String[] args) throws Exception {
		JobConf c = new JobConf(getConf(), FetchTagsJob.class);
		c.setJobName("FetchTagsJob");

		c.setOutputKeyClass(Text.class);
		c.setOutputValueClass(Text.class);

		// c.setMapperClass(TagPicMiscListMapper.class);
		// c.setMapperClass(FileTagMapper.class);
		c.setReducerClass(GetTagReduce.class);

		// c.setInputFormat(SequenceFileAsTextInputFormat.class);
		// c.setOutputFormat(TextOutputFormat.class);

		c.setMaxMapTaskFailuresPercent(5);

		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_MONTH, -1);
		String inputPath1 = MessageFormat.format("/group/taobao/taobao/dw/stb/{0,date,yyyyMMdd}/tag_pic_misc/*",
				today.getTime());
		String inputPath2 = MessageFormat.format(
				"/group/taobao/taobao/dw/stb/{0,date,yyyyMMdd}/file_tag_mysql_virtual_db/*",
				today.getTime());

		// String inputPath1 = "/group/taobao/taobao/dw/stb/20130319/tag_pic_misc/*";
		// String inputPath2 = "/group/taobao/taobao/dw/stb/20130319/file_tag_mysql_virtual_db/*";

		// for (int i = 0; i < args.length; i++) {
		// if ("-input".endsWith(args[i])) {
		// inputPath = args[i + 1];
		// }
		// }

		String outputPath = MessageFormat.format("/group/shop-tadget/file_tag_mysql_virtual_db/{0,date,yyyyMMdd}/out/",
				new Date());

		for (int i = 0; i < args.length; i++) {
			if ("-output".endsWith(args[i])) {
				outputPath = args[i + 1];
			}
		}

		MultipleInputs.addInputPath(c, new Path(inputPath1), SequenceFileAsTextInputFormat.class,
				TagPicMiscListMapper.class);
		MultipleInputs.addInputPath(c, new Path(inputPath2), SequenceFileAsTextInputFormat.class, FileTagMapper.class);

		// SequenceFileInputFormat.setInputPaths(c, new Path(inputPath1));
		FileOutputFormat.setOutputPath(c, new Path(outputPath));
		JobClient.runJob(c);
		return 0;
	}

	public static class TagPicMiscListMapper extends MapReduceBase implements
			Mapper<Text, Text, Text, Text> {

		public void map(Text key, Text value, OutputCollector<Text, Text> output,
				Reporter reporter) throws IOException {
			String val = new String(value.getBytes(), 0, value.getLength(), "UTF-8");
			String[] fields = val.split("");
			if (fields != null && fields.length == 5) {
				reporter.incrCounter("TagPicMiscListMapper", "record", 1);

				// long parseLong = 0L;
				// parseLong = Long.parseLong(fields[0]);

				output.collect(new Text(fields[0]), new Text(TAG_TP + fields[1]));
			} else {
				reporter.incrCounter("TagPicMiscListMapper", "record", 1);
				output.collect(new Text(key), new Text(TAG_TP + value));
			}
		}
	}

	public static class FileTagMapper extends MapReduceBase implements
			Mapper<Text, Text, Text, Text> {

		public void map(Text key, Text value, OutputCollector<Text, Text> output,
				Reporter reporter) throws IOException {
			String val = new String(value.getBytes(), 0, value.getLength(), "UTF-8");
			String[] fields = val.split("");
			if (fields != null && fields.length == 7) {
				reporter.incrCounter("FileTagMapper", "record", 1);
				// long parseLong = 0L;
				// parseLong = Long.parseLong(fields[4]);
				output.collect(new Text(fields[4]), new Text(TAG_FT + fields[3]));
			}
		}
	}

	public static class GetTagReduce extends MapReduceBase implements
			Reducer<Text, Text, Text, Text> {
		private String tag, fileid;

		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<Text, Text> output,
				Reporter reporter) throws IOException {

			List<String> tagIdTotag = new ArrayList<String>();
			List<String> tagIdToFileId = new ArrayList<String>();
			while (values.hasNext())
			{
				String currValue = values.next().toString();
				String valueSplitted[] = currValue.split(SEPARATOR_DOUBLE_STAR);
				if (valueSplitted[0].equals(TP))
				{
					tag = valueSplitted[1].trim();
					if (tag != null) {
						tagIdTotag.add(tag);
					}
				}
				else if (valueSplitted[0].equals(FT))
				{
					fileid = valueSplitted[1].trim();
					if (fileid != null) {
						tagIdToFileId.add(fileid);
					}
				}
			}

			// 合并两个map
			for (String fileId : tagIdToFileId) {
				reporter.incrCounter("GetTagReduce", "record", 1);
				for (String tag : tagIdTotag) {
					output.collect(new Text(fileId), new Text(tag));
				}
			}

		}
	}

	public static void main(String[] args) throws Exception {
		StringBuilder logContext = new StringBuilder();
		FetchTagsJob job = new FetchTagsJob();
		// /try {
		int rs = ToolRunner.run(job, args);
		if (rs == 0) {
			System.err.print("finish success");
		} else {
			System.err.print("finish failed");
			// logContext.append("GetItemPicCountJob").append(" failure ...\n");
		}
		// } catch (Exception e) {
		// logContext.append(e.getMessage()).append("\n");
		// }

		// String fileName = MessageFormat.format("./wrong-pics-{0,date,yyyyMMdd}", new Date());com.t
		// File logFile = new File(fileName);
		//
		// FileWriter fileWriter = null;
		// try {
		// if (logFile.exists())
		// logFile.delete();
		//
		// logFile.createNewFile();
		// fileWriter = new FileWriter(logFile, true);
		// fileWriter.write(logContext.toString());
		// fileWriter.close();
		// } catch (Exception e) {
		// e.printStackTrace(System.out);
		// }
		// System.exit(0);
	}
}