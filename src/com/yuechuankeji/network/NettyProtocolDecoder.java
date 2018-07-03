package com.yuechuankeji.network;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.protocol.ProtoMsg;
import com.yuechuankeji.util.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**协议内容解密解码器，接受协议解密之后的byte[]，转化为ProtoMsg*/
public class NettyProtocolDecoder extends MessageToMessageDecoder<byte[]>{
	public static Logger log = LoggerFactory.getLogger(NettyProtocolDecoder.class);
	/**重写抽象类的解密方法，会在解密之后调用
	 * */
	@Override
	protected void decode(ChannelHandlerContext ctx, byte[] in, List<Object> out) throws Exception {
		
		if(in.length < 8 ){
			log.error("channel {} decode error : not enough bytes");
			return;
		}
		int len = (in[0] & 0x000000ff) | ((in[1] & 0x000000ff) << 8); 
		
		short protoId = (short)((in[2] & 0x000000ff) | ((in[3] & 0x000000ff) << 8));
		
		int checkSum = (in[4] & 0x000000ff) | ((in[5] & 0x000000ff) << 8) | ((in[6] & 0x000000ff) << 16) | ((in[7] & 0x000000ff) << 24); 
		boolean checkPass = Codec.inst.checkSum(in, checkSum);
		if(checkPass){
			ProtoMsg pms = new ProtoMsg();
			pms.id = protoId;
			ByteBuf proto = Unpooled.wrappedBuffer(in , 8 , len-8);
			
			pms.builder = Codec.inst.parseProto(protoId, proto, ctx.channel());
			out.add(pms);
			proto.release();
			return;
		}else{
			//校验和校验失败
			ByteBuf buf = ctx.alloc().buffer();
			buf.writeBytes("ClientError : checksum error".getBytes());
			ctx.writeAndFlush(buf);
			buf.release();
			log.error("channel {} decode error : checksum error");
			return;
		}
	}
	
}
