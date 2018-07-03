package com.yuechuankeji.http;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.init.ServerConfig;



public class StartServ implements Runnable{

	public static Logger log = LoggerFactory.getLogger(StartServ.class);
	public static String host;
	public static int port;
	public StartServ(){
		host = ServerConfig.loginServerIp ;
		port = ServerConfig.loginServerPort ;
	}
	@Override
	public void run() {
		notifyStartServer();
	} 

	public void start(){
		new Thread(this, "startServ").start();
	}
	public void notifyStartServer(){
			int serverId = ServerConfig.serverId; 
			String msg = "{" + "\"msg\" : \"SUCCESS\",\"serverId\":" + serverId +  "}" ;
			String page =  ServerConfig.loginServerAddr + "gameSerInfo.jsp";
			MyClient hc = new MyClient(host, port);
			String respMesg = hc.sendRequest(page, msg);
			if (respMesg != null){
				log.info("服务器：{},状态：{}，登陆服收到消息，并返回", serverId, msg);
				// 设置服务器名称
				setServerName(respMesg);
			}
			else{
				log.error("服务器：{},状态：{}，登陆服没有收到消息", serverId, msg);
			}
		}

	public void setServerName(String responseMess){
		if(responseMess != null && !responseMess.equals("")){
			JSONObject jo = JSONObject.fromObject(responseMess);
			if(jo != null){
				String serName = (String)(jo.get("serverName"));
				if(serName != null && !serName.equals("")){
					ServerConfig.serverName = serName;
					log.info("设置GameServer.serverName的值为:{}" , serName);
				}
			}
		}
	}
}
