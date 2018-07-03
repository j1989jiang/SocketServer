package com.yuechuankeji.init;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**服务器配置文件加载类*/
public class Config {
	
	public static Logger log  = LoggerFactory.getLogger(Config.class);
	
	public static final String SERVER_CONFIG_PATH = "/server.properties";	//服务器配置文件路径
	
	public  Properties data = new Properties();
	public  List<String> crontabConfs = new ArrayList<String>();
	
	public  String get(String key) {
		String ret = data.getProperty(key);
		if(ret!=null){
			ret = ret.trim();
		}
		return ret;
	}
	
	public String get(String key, String safeRet){
		String v = get(key);
		if(v == null){
			return safeRet;
		}else{
			return v.trim();
		}
	}
	
	public int get(String key, int safeRet){
		String v = get(key);
		if(v == null){
			return safeRet;
		}else{
			return Integer.parseInt(v.trim());
		}
	}
	
	public  void loadServerConfig() {
		InputStream stream = Config.class.getResourceAsStream(SERVER_CONFIG_PATH);
		try {
			if (stream != null) {
				data.load(stream);
				printConf();
			}else{
				log.error("没有找到配置文件:"+SERVER_CONFIG_PATH);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("load config error!");
		}finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public  void printConf(){
		log.warn("===================Server Conf==========================");
		Set<Entry<Object, Object>> entrySet = data.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			log.warn("server conf:" + entry.getKey() + "->" + entry.getValue());
		}
		
		for (String crontab : crontabConfs) {
			log.warn("crontab conf:" + crontab);
		}
	}
}
