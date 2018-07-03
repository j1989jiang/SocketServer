package com.yuechuankeji.cache;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections.map.LRUMap;


/**常用的数据以 map<id,object> 的形式放在内存中
 * 避免反复从数据库中查询以降低数据库io压力*/
public class Cache {
	public static ConcurrentHashMap<String, Object> lockMap = new ConcurrentHashMap<>();
	public static Map<Class, Map> cacheMap = Collections.synchronizedMap(new LRUMap(5000));
	
	public static void init(){
		
	}
	
}
