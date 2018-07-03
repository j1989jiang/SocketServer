package com.yuechuankeji.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.util.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/** 协议加密编码器，调用加密算法对byte[]进行加密 */
public class NettyEncryptEncoder extends MessageToByteEncoder<byte[]> {

	public static Logger log = LoggerFactory.getLogger(NettyEncryptEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] data, ByteBuf out) throws Exception {
		
		Codec.inst.encryptData(data, out, ctx.channel());
		
	}
}
