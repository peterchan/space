package com.taobao.mc.search.job;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.mc.commoms.utils.lang.time.TimeUtils;
import com.taobao.mc.commoms.utils.timer.McJobScheduleException;
import com.taobao.mc.search.model.FileInfo;
import com.taobao.mc.search.utils.CsvReader;

/**
 * <pre>
 * desc: 
 * created: 2013-2-26 上午10:49:35
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class BuildIndexFromFileTask {
	private static final Logger logger = LoggerFactory.getLogger(BuildIndexFromFileTask.class);

	private static int BATCH_ROW = 10000;
	private static final BuildIndexThreadPool pool = new BuildIndexThreadPool();

	/**
	 * 从文件中构建索引
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @param totalRow
	 *            需要解析的文件的记录行，如果-1则为全部
	 * @param type
	 *            0: 在云端文件 1: 云梯上的文件
	 * @return 返回任务提交是否成功，不表示任务执行完成
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public boolean buildIndex(String filePath, int totalRow, int type) throws IOException, SolrServerException {
		if (totalRow == -1) {
			totalRow = Integer.MAX_VALUE;
		}

		try {
			pool.init();
			pool.start();
		} catch (IllegalArgumentException e) {
			logger.error("Start thread pool exception", e);
			return false;
		} catch (McJobScheduleException e) {
			logger.error("Start thread pool exception", e);
			return false;
		}

		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		}

		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();
			switch (type) {
				case 0:
					for (File readyFile : listFiles) {
						if (readerFileInfo(readyFile.getPath(), totalRow, BATCH_ROW) == 0) {
							return false;
						}
					}
				case 1:
				default:
					for (File readyFile : listFiles) {
						if (readerFileInfoYunti(readyFile.getPath(), totalRow, BATCH_ROW) == 0) {
							return false;
						}
					}
			}
		} else {
			switch (type) {
				case 0:
					if (readerFileInfo(filePath, totalRow, BATCH_ROW) == 0) {
						return false;
					}
				case 1:
				default:
					if (readerFileInfoYunti(filePath, totalRow, BATCH_ROW) == 0) {
						return false;
					}
			}
		}

		return true;
	}

	/**
	 * 读取并提交索引构建,文件格式是在云端
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * 
	 * @param totalRow
	 *            总共需要读取文件的多少行
	 * @param indexBatchRow
	 *            每读取多少行进行索引构建
	 * @return 返回真正的文件行数
	 * @throws IOException
	 */
	private int readerFileInfo(String filePath, int totalRow, int indexBatchRow) throws IOException {
		CsvReader reader = new CsvReader(filePath, ',', Charset.forName("GBK"));

		int realRow = 0;

		String[] columnNames = null;

		List<FileInfo> infos = new ArrayList<FileInfo>(indexBatchRow);

		int totalCount = totalRow <= 0 ? Integer.MAX_VALUE : totalRow;
		while (totalCount >= 0 && reader.readRecord()) {
			// System.out.println(totalCount);
			totalCount--;
			realRow++;

			String[] values = reader.getValues();
			if (values == null || values.length == 0) {
				continue;
			}

			if (realRow == 1) {
				columnNames = new String[values.length];
				for (int j = 0; j < values.length; j++) {
					columnNames[j] = values[j];
				}
				continue;

			} else {
				if (columnNames == null || columnNames.length != values.length) {
					continue;
				}
			}

			FileInfo info = new FileInfo();
			infos.add(info);

			for (int j = 0; j < values.length; j++) {
				String value = values[j];

				String columnName = columnNames[j];
				if (StringUtils.equalsIgnoreCase(columnName, "id")) {
					info.setId(value);
					info.setFid(Long.parseLong(value));

					// // 生成随机的特征词汇
					// int count = RandomUtils.nextInt(8);
					// if (count < 3) {
					// count = 3;
					// }
					//
					// StringBuilder sb = new StringBuilder();
					// while (count > 0) {
					// count--;
					// sb.append(RandomUtils.nextInt(100)).append(" ");
					// }
					//
					// // System.out.println(value + " : " + sb.toString());
					//
					// info.setFeature(sb.toString());

				} else if (StringUtils.equalsIgnoreCase(columnName, "url")) {
					info.setUrl(value);
				} else if (StringUtils.equalsIgnoreCase(columnName, "status")) {
					info.setStatus(Integer.parseInt(value));
				} else if (StringUtils.equalsIgnoreCase(columnName, "biz_id")) {
					info.setBizId(Long.parseLong(value));
				} else if (StringUtils.equalsIgnoreCase(columnName, "user_id")) {
					info.setUserId(Long.parseLong(value));
				} else if (StringUtils.equalsIgnoreCase(columnName, "name")) {
					info.setName(value);
				} else if (StringUtils.equalsIgnoreCase(columnName, "dir_id")) {
					info.setDirId(Long.parseLong(value));
				} else if (StringUtils.equalsIgnoreCase(columnName, "size")) {
					info.setSize(Long.parseLong(value));
				} else if (StringUtils.equalsIgnoreCase(columnName, "type")) {
					info.setType(Integer.parseInt(value));
				} else if (StringUtils.equalsIgnoreCase(columnName, "gmt_create")) {
					info.setGmtCreate(TimeUtils.parseDate(value));
				} else if (StringUtils.equalsIgnoreCase(columnName, "file_modified")) {
					info.setFileModified(TimeUtils.parseDate(value));
				} else if (StringUtils.equalsIgnoreCase(columnName, "gmt_modified")) {
					info.setGmtModified(TimeUtils.parseDate(value));
				}
			}

			if (infos.size() == indexBatchRow) {
				try {
					pool.addTask(infos);
				} catch (IllegalArgumentException e) {
					logger.error("Start thread pool exception", e);
				} catch (McJobScheduleException e) {
					logger.error("Start thread pool exception", e);
				}
				infos = new ArrayList<FileInfo>(indexBatchRow);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}

		}

		if (infos.size() > 0) {
			try {
				pool.addTask(infos);
			} catch (IllegalArgumentException e) {
				logger.error("Start thread pool exception", e);
			} catch (McJobScheduleException e) {
				logger.error("Start thread pool exception", e);
			}
			infos = null;
		}
		return realRow;
	}

	/**
	 * <pre>
	 * 读取并提交索引构建,文件格式是云梯的文件
	 * 
	 * 文件格式：
	 * id biz_id user_id name dir_id size type url status mark file_modified deleted gmt_create gmt_modified md5 
	 * extra_str1 extra_str2 extra_str3 extra_num1 extra_num2 extra_num3 
	 * tag
	 * </pre>
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * 
	 * @param totalRow
	 *            总共需要读取文件的多少行
	 * @param indexBatchRow
	 *            每读取多少行进行索引构建
	 * @return 返回真正的文件行数
	 * @throws IOException
	 */
	private int readerFileInfoYunti(String filePath, int totalRow, int indexBatchRow) throws IOException {
		CsvReader reader = new CsvReader(filePath, '\t', Charset.forName("UTF-8"));
		reader.setSafetySwitch(false);

		int realRow = 0;

		List<FileInfo> infos = new ArrayList<FileInfo>(indexBatchRow);

		int totalCount = totalRow <= 0 ? Integer.MAX_VALUE : totalRow;
		while (totalCount >= 0 && reader.readRecord()) {
			totalCount--;
			realRow++;

			String[] values = reader.getValues();
			if (values == null || values.length == 0 || values.length < 21) {
				continue;
			}

			if (StringUtils.isBlank(values[12]) || !StringUtils.equals(values[12], "0")) {
				continue;
			}

			FileInfo info = new FileInfo();
			infos.add(info);
			try {
				info.setId(values[1]);
				info.setFid(Long.parseLong(values[1]));
				info.setBizId(Long.parseLong(values[2]));
				info.setUserId(Long.parseLong(values[3]));
				info.setName(values[4]);
				info.setDirId(Long.parseLong(values[5]));
				info.setSize(Long.parseLong(values[6]));
				info.setType(Integer.parseInt(values[7]));
				info.setUrl(values[8]);
				info.setStatus(Integer.parseInt(values[9]));
				info.setFileModified(TimeUtils.parseDate(values[11]));
				info.setGmtCreate(TimeUtils.parseDate(values[13]));
				info.setGmtModified(TimeUtils.parseDate(values[14]));
				info.setMd5(values[15]);
				if (values.length >= 23) {
					info.setTag(values[22]);
				}
			} catch (Throwable e) {
				logger.error("Setting file info exception.", e);
				continue;
			}

			if (infos.size() == indexBatchRow) {

				try {
					pool.addTask(infos);
				} catch (IllegalArgumentException e) {
					logger.error("Start thread pool exception", e);
				} catch (McJobScheduleException e) {
					logger.error("Start thread pool exception", e);
				}
				infos = new ArrayList<FileInfo>(indexBatchRow);

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
			}
		}

		if (infos.size() > 0) {
			try {
				pool.addTask(infos);
			} catch (IllegalArgumentException e) {
				logger.error("Start thread pool exception", e);
			} catch (McJobScheduleException e) {
				logger.error("Start thread pool exception", e);
			}

			System.out.println(infos.size());
		}
		return realRow;
	}

	public boolean stop() {
		try {
			pool.stop();
		} catch (McJobScheduleException e) {
			logger.error("Start thread pool exception", e);
			return false;
		}

		return true;
	}

	public static void main(String[] args) throws IOException, SolrServerException {
		String path = "/media/xiangfeng/SF/out";
		int totalRow = -1;
		if (args != null && args.length > 0) {
			path = args[0];
		}

		if (args != null && args.length > 1) {
			totalRow = Integer.parseInt(args[1]);
		}

		System.out.println(path);
		System.out.println(totalRow);

		BuildIndexFromFileTask task = new BuildIndexFromFileTask();
		task.buildIndex(path, totalRow, 1);
	}
}
