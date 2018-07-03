package com.yuechuankeji.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**消息处理器，C->S通信时ChannelPipeline的最后一环，逻辑层入口*/
public class NettyHandler extends ChannelInboundHandlerAdapter{
	/**收到的协议经过一系列的decoder之后，最终会通过这个方法进入逻辑层进行处理*/
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyHandlerImp.inst.channelRead(ctx, msg);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		NettyHandlerImp.inst.channelActive(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		NettyHandlerImp.inst.channelInactive(ctx);
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent){
			NettyHandlerImp.inst.channelIdle(ctx);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		NettyHandlerImp.inst.exceptionCaught(ctx, cause);
	}
}
