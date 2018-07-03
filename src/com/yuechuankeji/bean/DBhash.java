package com.yuechuankeji.bean;

/**所有在Cache中缓存的数据必须支持此接口
 * 所有支持此接口的数据必须在Cache中缓存*/
public interface DBhash {
	/**返回数数据持有者唯一标识，用以确保数据存储顺序正确*/
	long hash();
}
