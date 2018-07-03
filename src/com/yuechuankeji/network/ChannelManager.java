package com.yuechuankeji.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

public class ChannelManager {
	
	public static Logger log = LoggerFactory.getLogger(ChannelManager.class);
	public static ChannelManager inst ;
	/**
	 * 用于管理所有在线玩家，按照id-channel对应*/
	public static Map<Integer, Channel> channelMap;
	public static Map<Integer, String> reconnectMap;
	
	@SuppressWarnings("unchecked")
	public ChannelManager() {
		channelMap = new HashMap<Integer, Channel>();
		reconnectMap = Collections.synchronizedMap(new LRUMap(1000));
		log.info("CtxManager已启动");
	}
	
	public static ChannelManager getInst(){
		if(inst == null ){
			inst = new ChannelManager();
		}
		return inst ;
	}
	
	/**把channel加入channelMap*/
	public void addChannel(Channel channel){
		if(channel == null ){
			return;
		}
		synchronized (channelMap) {
			
			int id = getChannelId(channel);//用十六进制读取
			channelMap.put(id, channel);
		}
		channel.attr(ChannelAttributeKey.PLAYER_ID).set(0);
		channel.attr(ChannelAttributeKey.SERIALNO).set(0);
	}
	
	public int getChannelId(Channel channel){
		if(channel == null){
			return 0 ;
		}
		return (int) Long.parseLong(channel.id().asShortText() , 16);
	}
	
	public void removeChannel(Channel channel){
		if(channel == null ){
			return;
		}
		synchronized (channelMap) {
			int id = getChannelId(channel);//用十六进制读取
			channelMap.remove(id);
		}
	}
	
	public void closeAllChannel(){
		synchronized (channelMap) {
			channelMap.values().stream().forEach(ctx->ctx.close());
			log.info("关闭所有玩家连接完成");
		}
	}
	
	public List<Channel> getAllChannel(){
		synchronized (channelMap) {
			List<Channel> res = new ArrayList<>(channelMap.values());
			return res;
		}
	}
	
}
