package com.yuechuankeji.util;

import com.yuechuankeji.protocol.BasicProto;

import io.netty.channel.Channel;


/**功能管理类统一接口，便于功能初始化和针对协议调用*/
public interface FunctionManager {
	/**功能管理类初始化*/
	public void init();
	/**功能管理类逻辑处理入口*/
	public void handler(short id ,BasicProto proto , Channel channel);
}
