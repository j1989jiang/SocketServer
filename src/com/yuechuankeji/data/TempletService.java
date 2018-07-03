package com.yuechuankeji.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TempletService {
	
	public static Logger log = LoggerFactory.getLogger(TempletService.class);
	
	public static TempletService templetService = new TempletService();
	
	public static Map<String, List<?>> templetMap = new HashMap<String, List<?>>();
	
	
	public static TempletService getInstance() {
		return templetService;
	}
	
	public void registerObject(Object o, Map<String, List<?>> dataMap) {
		add(o.getClass().getSimpleName(), o, dataMap);
	}
	
	@SuppressWarnings("rawtypes")
	public void add(String key, Object data, Map<String, List<?>> dataMap) {
		List list = dataMap.get(key);
		if (list == null) {
			list = new ArrayList();
			dataMap.put(key, list);
		}
		list.add(data);
	}
	
	public void afterLoad(){
		//TODO 对于某些常用的复杂配置，可以在加载完成之后创建单独的staticMap
	}
	
}
