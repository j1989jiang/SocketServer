package com.yuechuankeji.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yuechuankeji.init.ServerConfig;


public class EndServ implements Runnable{

	public static Logger log = LoggerFactory.getLogger(EndServ.class);
	public static String host;
	public static int port;
	public EndServ(){
		host =  ServerConfig.loginServerIp ;
		port = ServerConfig.loginServerPort ;
	}
	@Override
	public void run() {
		notifyStartServer();
	} 

	public void start(){
		new Thread(this, "endServ").start();
	}
	public void notifyStartServer(){
			int serverId = ServerConfig.serverId; 
			String msg = "{" + "\"msg\" : \"END_SERVER\",\"serverId\":" + serverId +  "}" ;
			String page =  "/qxrouter/gameSerInfo.jsp";
			MyClient hc = new MyClient(host, port);
			String res = hc.sendRequest(page, msg);
			if (res != null){
				log.info("服务器：{},状态：{}，登陆服收到消息，并返回", serverId, msg);
			}
			else{
				log.error("服务器：{},状态：{}，登陆服没有收到消息", serverId, msg);
			}
		}

}