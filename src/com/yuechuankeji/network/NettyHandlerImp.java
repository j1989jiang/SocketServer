package com.yuechuankeji.network;

import java.io.IOException;

import org.apache.mina.core.write.WriteToClosedSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.protocol.ProtoMsg;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

public class NettyHandlerImp {
	
	public static Logger log = LoggerFactory.getLogger(NettyHandlerImp.class);
	public static NettyHandlerImp inst = new NettyHandlerImp();
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof ProtoMsg){
			ProtoMsg pms = (ProtoMsg)msg;
			Dispatcher.inst.route(pms.id, pms.builder, ctx.channel());
		}else{
			log.error("未经解析的协议内容{}",msg);
		}
	}
	
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		if (cause instanceof IOException) {
			String msg = cause.getMessage();
			if(msg==null)msg = "it'sNull";
			if(cause instanceof WriteToClosedSessionException){
				log.warn("IO异常Z {} WriteToClosed", ctx);
			}else if(msg.contains("连接超时")){
				log.warn("IO异常A {} {}", ctx,msg);
			}else if(msg.contains("Connection reset by peer")){
				log.warn("IO异常B {} {}", ctx,msg);
			}else if(msg.contains("远程主机强迫关闭了一个现有的连接")){
				//客户端session主动关闭的时候不再刷错误日志
			}else  {
				log.warn("IO异常 {} {}", ctx, cause);
			}
			//TODO session出现异常，玩家掉线，需要处理后续问题
			return;
		}
		log.error("exceptionCaught sid {}",ctx.channel().id().asShortText());
		log.error("异常出现：", cause);
	}
	
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ChannelManager.inst.addChannel(ctx.channel());
	}
	
	
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ChannelManager.inst.removeChannel(ctx.channel());
	}
	
	public void channelIdle(ChannelHandlerContext ctx)throws Exception{
		//TODO 玩家连接空闲处理，用于实现断线重连功能
		
	}
}
