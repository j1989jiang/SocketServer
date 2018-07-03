package com.yuechuankeji.network;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.protocol.BasicProto;
import com.yuechuankeji.util.FunctionManager;

import io.netty.channel.Channel;


/**功能分发类，根据协议id调用正确的功能管理类对协议进行处理*/
public class Dispatcher {
	
	public static Logger log = LoggerFactory.getLogger(Dispatcher.class);
	
	public static Map<Short, FunctionManager> map ;
	public static Dispatcher inst ;
	
	public static Dispatcher getInst(){
		if(inst == null ){
			inst = new Dispatcher();
			inst.init();
		}
		return inst;
	}
	
	
	public void init() {
		map = new HashMap<Short, FunctionManager>();
	}
	
	
	public void route(short id ,BasicProto proto , Channel channel){
		try {
			map.get(id).handler(id, proto, channel);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static void regist(short protoId , FunctionManager mgr){
		synchronized (map) {
			FunctionManager ret =  map.putIfAbsent(protoId, mgr) ;
			if( ret != null){
				log.error("重复注册的协议号：{} 类名：{} , {} ", protoId , mgr.getClass() , ret.getClass());
			}
			
		}
	}
}
