package com.yuechuankeji.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.http.MyClient;

import net.sf.json.JSONObject;

public class ServerConfig {
	
	public static Logger log = LoggerFactory.getLogger(ServerConfig.class);
	
	public static int serverId ; //服务器唯一ID
	public static int port ;	//服务器通讯框架监听端口
	public static String serverName ;	//服务器在登录服设置的服务器名称，每次启动时获取，SDK接入时可能会用
	
	public static String cacheServer ;	//memcached 服务器的地址、端口号
	public static String redisServer ;	//redis服务器的地址、端口号
	
	public static String loginServerIp;	//登录服务器地址
	public static int loginServerPort;	//登录服务器端口
	public static String loginServerAddr ;	//登录服务器的验证链接
	
	public volatile static boolean shutdown = false ;	//关服标记
	
	public static int keepAliveTimeOut ;	//心跳间隔
	
	public static int maxOnlineNum ; //最大在线人数，未使用字段
	
	public static boolean doencrypt = true ;	//服务器协议加密开关
	
	public static int DBSaverSize ;	//服务器延迟存储线程池大小
	
	public static int EventPoolSize ; //事件处理器线程池大小
	
	public static String configPath = "/server.properties";	//服务器配置文件路径
	
	public static Config cfg ;
	public static void init(){
		
		log.info("开始加载服务器配置……");
		cfg = new Config();
		cfg.loadServerConfig();
		serverId = cfg.get("serverId", 0);
		port = cfg.get("serverPort", 8586);
		cacheServer = cfg.get("cacheServer");
		redisServer = cfg.get("redisServer");
		loginServerIp = cfg.get("loginServer");
		loginServerPort = cfg.get("loginServerPort",8090);
		loginServerAddr = cfg.get("loginServerAddr");
		keepAliveTimeOut = cfg.get("keepAliveTimeOut", 60*5);	//默认心跳间隔5分钟
		String doencryptStr = cfg.get("doencrypt");
		if("off".equals(doencryptStr)||"false".equals(doencryptStr)){
			doencrypt = false ;
		}
		DBSaverSize = cfg.get("DBSaverSize", 60);
		EventPoolSize = cfg.get("EventPoolSize", 10);
		
//		loadServerId();
		log.info("服务器配置加载成功！");
	}
	
	
	public static void loadServerId(){
		
		log.info("请求serverId......");
		String msg = "{" + "\"msg\" : \"SERVERID\" }";
		MyClient hc = new MyClient(loginServerIp, loginServerPort);
		//gameSerManager.jsp 游戏服请求服务器ID页面
		String  respMesg = 
				hc.sendRequest(loginServerAddr+"gameSerManager.jsp", msg);
		
		setServerId(respMesg);
		
	}
	
	
	public static void setServerId(String responseMess){
		log.info("ret msg is :{}",responseMess);
		if(responseMess != null && !responseMess.equals("")){
			JSONObject jo = JSONObject.fromObject(responseMess);
			if(jo != null){
				String serId = jo.optString("serverId",null);
				if(serId != null && !serId.equals("")){
					cfg.data.put("serverId", serId);
					int returnSerId = Integer.parseInt(serId);
					if(serverId != returnSerId ){
						log.error("配置serverId与loginServer不一致");
						serverId = returnSerId;
						System.exit(0);
					}
					log.info("设置serverId的值为:" + serId);
				}
			}
		}
	}


	public static void shutdown() {
		// TODO Auto-generated method stub
		shutdown = true ;
	}
	
	
}
