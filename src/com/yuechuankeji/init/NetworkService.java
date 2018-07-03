package com.yuechuankeji.init;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.dbwork.Redis;
import com.yuechuankeji.http.StartServ;
import com.yuechuankeji.network.BootStrapMgr;
import com.yuechuankeji.network.ChannelManager;


/**用于启动通信端口，要在最后执行*/
public class NetworkService {
	
	public static Logger log = LoggerFactory.getLogger(NetworkService.class);
	public static NetworkService inst ;

	public static NetworkService getInst() {
		if(inst == null){
			inst = new NetworkService();
		}
		return inst;
	}
	
	public void init(){
		
		//启动守护线程
		ThreadViewer.start();
		//启动session管理器
		ChannelManager.getInst();
		//启动通信端口，开始允许玩家连接
		try {
			BootStrapMgr.getInst().start();
			log.info("启动接监听端口成功，通知登录服务器……");
			new StartServ().start();
		} catch (Exception e) {
			log.error("启动端口失败", e);
			e.printStackTrace();
			System.exit(0);
		}
		//redis连接测试
		final Redis r = Redis.getInstance();
		new Thread(new Runnable() {
			@Override
			public void run() {
				r.test();
			}
		}).start();
		
	}
	
}
