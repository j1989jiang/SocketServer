package com.yuechuankeji.init;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.data.DataLoader;
import com.yuechuankeji.event.EventMgr;
import com.yuechuankeji.job.SchedulerMgr;
import com.yuechuankeji.network.Dispatcher;

/**
 * 用于加载游戏配置文件，启动游戏各个功能类*/
public class GameService {
	
	public static Logger log = LoggerFactory.getLogger(GameService.class);
	
	public static GameService inst ;
	
	public static GameService getInst(){
		if(inst == null ){
			inst = new GameService();
		}
		return inst;
	}
	
	public void init(){


		//初始化定时任务
		SchedulerMgr.getInst();
		log.info("定时器启动");
		
		//初始化事件管理器
		EventMgr.getInst();
		log.info("事件管理器线程池启动");
		
		//初始化协议分发器
		Dispatcher.getInst();
		log.info("协议分发器启动");
		//TODO 加载游戏配置启动功能
		
	}
	
	public void loadData(){
		DataLoader loader = new DataLoader("com.chuanqukeji.data", File.separator+"dataConfig.xml");
		loader.load();
	}
	
}
