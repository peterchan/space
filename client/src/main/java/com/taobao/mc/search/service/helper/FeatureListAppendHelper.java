package com.taobao.mc.search.service.helper;

import java.util.List;
import java.util.Map;

import com.taobao.mc.search.model.FeatureList;
import com.taobao.mc.search.utils.CommonTools;

/**
 * <pre>
 * desc: 
 * created: Mar 7, 2013 9:58:39 AM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class FeatureListAppendHelper implements AppendHelper<FeatureList> {
	@Override
	public FeatureList append(FeatureList f, Map map) {
		Object object = map.get("fid");
		if (object != null && object instanceof List) {
			List oldList = (((List) object));
			String[] oldFid = new String[oldList.size()];
			oldList.toArray(oldFid);
			String[] newFid = f.getFid();
			f.setFid(CommonTools.concat(oldFid, newFid));
		}
		return f;
	}
}
