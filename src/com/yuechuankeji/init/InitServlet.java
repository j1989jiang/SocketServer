package com.yuechuankeji.init;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.dbwork.DBSaver;
import com.yuechuankeji.dbwork.HibernateUtil;
import com.yuechuankeji.dbwork.Redis;
import com.yuechuankeji.event.EventMgr;
import com.yuechuankeji.job.SchedulerMgr;
import com.yuechuankeji.network.BootStrapMgr;
import com.yuechuankeji.network.ChannelManager;





public class InitServlet implements Servlet {
	public static Logger log = LoggerFactory.getLogger(InitServlet.class);
	
	@Override
	public void init(ServletConfig arg0) throws ServletException {
		BaseService.getInst().init();
		GameService.getInst().init();
		NetworkService.getInst().init();
		log.info("==========服务器启动成功========");
	}
	
	@Override
	public void destroy() {
		// TODO 自动生成的方法存根
		close();
	}
	
	
	
	public static void close(){
		closeNet();
		log.info("================game server begin to shutdown================");
		EventMgr.shutdown();
		SchedulerMgr.inst.stop();
		ServerConfig.shutdown();
		Redis.destroy(); 
		DBSaver.inst.shutdown();
		HibernateUtil.getSessionFactory().close();
		log.info("================game server shutdown ok================");
	}

	public static void closeNet() {
		// 通知登陆服：关服
//		SocketMgr.getInst().acceptor.unbind();
//		SessionManager.getInst().closeAll();
//		SocketMgr.getInst().acceptor.dispose();
		ChannelManager.getInst().closeAllChannel();
		BootStrapMgr.getInst().shutDown();
//		EndServ ser = new EndServ();
//		ser.start();
	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO 自动生成的方法存根
		return null;
	}
	
	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		
	}
}
