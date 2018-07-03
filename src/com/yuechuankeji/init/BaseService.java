package com.yuechuankeji.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.cache.Cache;
import com.yuechuankeji.dbwork.DBSaver;
import com.yuechuankeji.dbwork.HibernateUtil;
import com.yuechuankeji.dbwork.Redis;

/**
 * 服务器基础类，用于读取服务器配置、加载hibernate、连接redis
 * 属于最基本的内容，首先调用*/
public class BaseService {
	
	public static Logger log =LoggerFactory.getLogger(BaseService.class);
	public static BaseService inst ;
	
	public static BaseService getInst(){
		if(inst == null){
			inst = new BaseService();
		}
		return inst;
	}
	public void init(){
		
		//加载服务器配置文件，连接登录服获取服务器ID
		ServerConfig.init();
		
		
		//触发Hibernate的构建
		log.info("启动数据存储服务");
		HibernateUtil.getSessionFactory();
		//初始化缓存系统
		Cache.init();
		log.info("缓存初始化完成");
		//初始化延迟存储线程
		DBSaver.getInst();
		log.info("存储线程池初始化完成");
		log.info("数据存储服务器启动成功");
		
		
		//启动redis连接
		log.info("启动redis连接");
		Redis.getInstance();
		
		
		//加载游戏配置文件
		log.info("加载游戏配置文件");
//		loadData();
		
	}
}
