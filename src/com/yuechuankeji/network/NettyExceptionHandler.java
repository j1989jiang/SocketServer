package com.yuechuankeji.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.protocol.ProtoMsg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**因为netty的机制，在写操作的时候不能使用exceptionCaught方法来捕获异常
 * 所以添加这个handler,在执行写操作的时候添加监听器，用于追踪异常信息*/
public class NettyExceptionHandler extends ChannelOutboundHandlerAdapter{
	public static Logger log = LoggerFactory.getLogger(NettyExceptionHandler.class);
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if(!(msg instanceof ProtoMsg || msg instanceof ByteBuf)){
			log.error("wrong type of message was writen");
			return;
		}
		ctx.writeAndFlush(msg, promise).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
}
