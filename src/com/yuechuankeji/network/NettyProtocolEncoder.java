package com.yuechuankeji.network;

import java.util.List;

import com.yuechuankeji.protocol.ProtoMsg;
import com.yuechuankeji.util.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**协议内容编码器，实现ProtoMsg到byte数组的转换*/
public class NettyProtocolEncoder extends MessageToMessageEncoder<ProtoMsg>{

	@Override
	protected void encode(ChannelHandlerContext ctx, ProtoMsg pms, List<Object> out) throws Exception {
		short id = pms.id;
		byte[] bytes = null ;
		ByteBuf buf = null ;
		//获取协议内容序列化之后的byte[]数组
		if(pms.builder != null ){
			try {
				buf = PooledByteBufAllocator.DEFAULT.buffer();
				pms.builder.encode(buf);
				bytes = new byte[buf.readableBytes()];
				buf.readBytes(bytes);
				
			} finally {
				if(buf != null){
					buf.release() ;
				}
			}
		}
		//添加协议长度、协议id、校验和
		bytes = Codec.inst.buildProto(id, bytes);
		out.add(bytes);
	}
	
}
