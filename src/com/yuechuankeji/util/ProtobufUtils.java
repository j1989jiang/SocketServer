package com.yuechuankeji.util;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLite.Builder;

import io.netty.channel.ChannelHandlerContext;

public class ProtobufUtils {
	
	public static Logger log = LoggerFactory.getLogger(ProtobufUtils.class.getSimpleName());
	
	public static MessageLite.Builder parseFrom(int len, IoBuffer buf) {
		short uid = buf.getShort();
		byte[] data = new byte[len - 4];
		buf.get(data);

		Builder ret = parseProto(uid, data, (IoSession)null);

		return ret;
	}

	public static Builder parseProto(short protoId, byte[] data,IoSession session) {
		return parseProto(protoId, data,0,data.length,session);
	}

	public static Builder parseProto(short protoId, byte[] data, int offset,
			int len,IoSession session) {
//		MessageLite tmpLite = IdToProto.get(protoId);
		Builder ret = null;
//		if(tmpLite == null){
//			log.error("没有找到协议类型 {}", protoId);
//		}else{
//			try {
//				MessageLite.Builder builder = tmpLite.newBuilderForType();
//				ret = builder.mergeFrom(data, offset, len);
//			} catch (Exception e) {
//				log.error("解析协议出错", e);
//				e.printStackTrace();
//				//TODO 破解协议加入黑名单
//			}
//		}
		return ret;
	}
	
	
	public static Builder parseProto(short protoId, byte[] data,ChannelHandlerContext session) {
		return parseProto(protoId, data,0,data.length,session);
	}
	/**
	 * 根据传入的内容解析协议
	 * @param protoId 要解析协议id
	 * @param data 包含协议内容的数组
	 * @param offset 解析时跳过的数组长度
	 * @param len 协议内容的数组长度
	 * @param ctx 暂时无用字段，添加对玩家处理（例如：封号）的代码时会用到
	 * */
	public static Builder parseProto(short protoId, byte[] data, int offset,
			int len,ChannelHandlerContext ctx) {
//		MessageLite tmpLite = IdToProto.get(protoId);
		Builder ret = null;
//		if(tmpLite == null){
//			log.error("没有找到协议类型 {}", protoId);
//		}else{
//			try {
//				MessageLite.Builder builder = tmpLite.newBuilderForType();
//				ret = builder.mergeFrom(data, offset, len);
//			} catch (Exception e) {
//				log.error("解析协议出错", e);
//				e.printStackTrace();
//				//TODO 破解协议加入黑名单
//			}
//		}
		return ret;
	}

	public static MessageLite.Builder parseFrom(int len, byte[] data) {
		IoBuffer buf = IoBuffer.allocate(1024).setAutoExpand(true);
		buf.put(data);
		buf.flip();

		return parseFrom(data.length, buf);
	}


}
