package com.yuechuankeji.network;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.util.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**协议加密解码器，读取ByteBuf的内容转化为byte[]输出给下一个解码器*/
public class NettyDecryptDecoder extends ByteToMessageDecoder{
	public static Logger log = LoggerFactory.getLogger(NettyDecryptDecoder.class);
	/**重写抽象类的解密方法，会在收到协议时自动调用
	 * */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		if(buf.readableBytes() < 20){
			
			ByteBuf ret = ctx.alloc().buffer();
			ret.writeBytes("ClientError : decode error".getBytes());
			ctx.writeAndFlush(ret);
			log.error("channel {} decode error : not enough bytes",ctx.channel().id().asShortText());
			return;
		}
		
		byte[] request = Codec.inst.decryptData(buf, ctx);
		//解密出错，通知客户端decode error
		if(request == null){
			ByteBuf ret = ctx.alloc().buffer();
			ret.writeBytes("ClientError : decode error".getBytes());
			ctx.writeAndFlush(ret);
			log.error("channel {} decode error : message wrong",ctx.channel().id().asShortText());
			return;
		}
		//解密成功，加入下一步的解密处理
		out.add(request);
	}
	
}
