package com.taobao.mc.search.job;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.mc.commoms.utils.timer.McJobSchedule;
import com.taobao.mc.commoms.utils.timer.McJobScheduleException;
import com.taobao.mc.commoms.utils.timer.domain.McJob;
import com.taobao.mc.commoms.utils.timer.domain.McJobGroup;
import com.taobao.mc.commoms.utils.timer.ext.McJobDefinition;
import com.taobao.mc.search.constants.CoreNames;
import com.taobao.mc.search.model.FileInfo;
import com.taobao.mc.search.param.WriteIndexResult;
import com.taobao.mc.search.service.FileIndexService;
import com.taobao.mc.search.service.impl.DefaultFileIndexService;

/**
 * <pre>
 * desc: 
 * created: Mar 6, 2013 10:07:02 AM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class BuildIndexThreadPool {
	private static final Logger logger = LoggerFactory.getLogger(BuildIndexThreadPool.class);

	private static final McJobSchedule jobSchedule = new McJobSchedule();
	private static final FileIndexService service = new DefaultFileIndexService();

	//
	private static final String GROUP_NAME = "oneceJobGroup";
	private static final String JOB_NAME = "oneceJob";

	private static long timeConst = 0;
	private static long indexedCount = 0;
	private static Set<String> ids = new HashSet<String>();

	static {
		McJobGroup oneceJobGroup = new McJobGroup();
		oneceJobGroup.setGroupId(GROUP_NAME);
		oneceJobGroup.setRepeatInterval(200);
		oneceJobGroup.setRepeatCount(0);
		List<McJobGroup> groups = new ArrayList<McJobGroup>(1);
		groups.add(oneceJobGroup);
		jobSchedule.setGroups(groups);
	}

	/**
	 * 默认初始化方法，线程池容量为3
	 * 
	 * @throws McJobScheduleException
	 */
	public void init() throws McJobScheduleException {
		init(5);
	}

	/**
	 * 初始化线程池
	 * 
	 * @throws McJobScheduleException
	 */
	public void init(int maxThread) throws McJobScheduleException {
		jobSchedule.setMaxThreads(maxThread);
		jobSchedule.init();

	}

	/**
	 * 开始线程池
	 * 
	 * @throws McJobScheduleException
	 */
	public void start() throws McJobScheduleException {
		jobSchedule.start();
	}

	/**
	 * 结束线程池
	 * 
	 * @throws McJobScheduleException
	 */
	public void stop() throws McJobScheduleException {
		jobSchedule.stop();
	}

	/**
	 * 添加一个任务去执行索引构建
	 * 
	 * @param fileInfos
	 * @throws IllegalArgumentException
	 * @throws McJobScheduleException
	 */
	public void addTask(List<FileInfo> fileInfos) throws IllegalArgumentException, McJobScheduleException {
		McJobDefinition definition = new McJobDefinition();
		definition.setName(JOB_NAME + System.currentTimeMillis());
		definition.setGroupId(GROUP_NAME);
		BuildIndexJob buildIndexJob = new BuildIndexJob();
		buildIndexJob.setDefinition(definition);
		buildIndexJob.setFileInfos(fileInfos);
		System.out.println("get: " + fileInfos.size());
		jobSchedule.registerJob(buildIndexJob);
	}

	class BuildIndexJob extends McJob {
		private List<FileInfo> fileInfos;

		public void setFileInfos(List<FileInfo> fileInfos) {
			this.fileInfos = fileInfos;
		}

		@Override
		public boolean execute() throws Exception {
			long c1 = System.currentTimeMillis();

			for (FileInfo info : fileInfos) {
				if (!ids.add(info.getId())) {
					System.out.println("duplicate id : " + info.getId());
				}
			}

			WriteIndexResult writeFileInfo = service.writeIndex(fileInfos, CoreNames.FILE_INDEX);
			timeConst = timeConst + System.currentTimeMillis() - c1;
			logger.debug(ToStringBuilder.reflectionToString(writeFileInfo));
			indexedCount = indexedCount + fileInfos.size();
			System.out.println("timeConst::" + timeConst + " indexedCount: " + indexedCount + " id size:" + ids.size());

			return writeFileInfo.isSuccess();
		}
	}
}
