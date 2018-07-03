package com.yuechuankeji.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.network.ChannelAttributeKey;
import com.yuechuankeji.network.ChannelManager;
import com.yuechuankeji.protocol.BasicProto;
import com.yuechuankeji.protocol.IdToProto;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

public class Codec {
	Logger log = LoggerFactory.getLogger(Codec.class);

	public static Codec inst = new Codec();
	public static boolean doCheckSum = false;
	public static boolean doEncode = true;

	/**
	 * 初步解码，读取协议内容，并完成解密过程，返回未加密的协议数组
	 * 
	 * @param buf
	 *            netty提供的缓冲区封装类，用于读取内容
	 * @return 协议数组，如果加密出现问题（例如账号id不正确），则返回null
	 */
	public byte[] decryptData(ByteBuf buf, ChannelHandlerContext ctx) {

		int accountId = buf.readIntLE();
		int ctxId = buf.readIntLE();
		int serialNo = buf.readIntLE();

		byte[] res = new byte[buf.readableBytes()];
		buf.readBytes(res);

		int key = res[0] & 0xff;
		key = key | key << 8 | key << 16 | key << 24;

		accountId ^= key;
		ctxId ^= key;
		serialNo ^= key;

		Attribute<Integer> attr = ctx.channel().attr(ChannelAttributeKey.ACCOUNT_ID);
		if (attr.get() == null) {
			attr.set(accountId);
		}
		encodeData(ctxId, serialNo, KeyFile.k_http, res, 0);
		return res;

	}

	/** 在加密之后的协议头部添加校验信息 */
	public void encryptData(byte[] data, ByteBuf buf, Channel channel) throws Exception {

		int channelId = ChannelManager.getInst().getChannelId(channel);
		int serialNo = getSerialNo(channel);
		int accountId = channel.attr(ChannelAttributeKey.ACCOUNT_ID).get();

		encodeData(channelId, serialNo, KeyFile.k_http, data, 0);

		int k1 = data[0] & 0xff;
		int key = k1 | k1 << 8 | k1 << 16 | k1 << 24;

		buf.writeIntLE(accountId ^ key);
		buf.writeIntLE(channelId ^ key);
		buf.writeIntLE(serialNo ^ key);
		buf.writeBytes(data);

		return;

	}

	/**
	 * 对传入的协议内容数组进行编辑，加入长度、id、校验和
	 * 
	 * @param id
	 *            协议id
	 * @param bytes
	 *            协议内容数组
	 * @return 编辑之后的byte[]
	 */
	public byte[] buildProto(short id, byte[] bytes) throws Exception {
		int len = bytes == null ? 8 : bytes.length + 8;
		ByteBuffer buffer = ByteBuffer.allocate(len);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		// 加入协议长度
		buffer.putShort((short) len);
		// 加入协议id
		buffer.putShort(id);
		// 校验和预占位
		buffer.putInt(0);
		if (bytes != null) {
			// 放入protobuf生成的byte数组
			buffer.put(bytes);
		}
		byte[] res = buffer.array();
		if (doCheckSum && doEncode) {
			fillCheckSum(res);
		}
		return res;
	}

	/** 根据byte数组生成校验和 */
	public void fillCheckSum(byte[] bytes) throws Exception {
		int sum = 0;
		for (int i = 8; i < bytes.length; i++) {
			sum += bytes[i] & 0x000000ff;
		}

		sum = GetCheckSum9(sum);

		bytes[4] = (byte) ((sum >> 0) & 0xff);
		bytes[5] = (byte) ((sum >> 8) & 0xff);
		bytes[6] = (byte) ((sum >> 16) & 0xff);
		bytes[7] = (byte) ((sum >> 24) & 0xff);
	}

	/**
	 * 对传入的byte[]进行加密
	 * 
	 * @param sessionId
	 *            客户端连接的id
	 * @param serialNo
	 *            协议序列号，每发一次+1
	 * @param key3
	 *            协议加密用数字，KeyFile中获取
	 * @param data
	 *            需要加密的byte[]数据
	 * @param startIndex
	 *            开始加密的位置，无特殊需求传0
	 * @return 返回经过加密后的数据
	 */
	public byte[] encodeData(int channelId, int serialNo, int key3, byte[] data, int startIndex) {
		if (!doEncode) {
			// 不加密，直接返回
			return data;
		}

		int index = 0;

		int k1 = (int) ((channelId * serialNo + key3) * key3);
		byte k2 = (byte) (((k1 & 0xFF) + ((k1 >> 8) & 0xFF) + ((k1 >> 16) & 0xFF) + ((k1 >> 24) & 0xFF)) & 0xFF);
		byte key;
		for (int i = startIndex, max = data.length; i < max; i++) {
			key = (byte) (k2 ^ KeyFile.k_keys[index++]);
			data[i] ^= key;

			if (index >= KeyFile.k_keys.length)
				index = 0;
		}
		return data;
	}

	/** 校验和检查，当检查开启时，会判断校验和是否正确 */
	public boolean checkSum(byte[] data, int checkSum) {
		if (!doCheckSum || !doEncode) {
			// 不检查或者不加密，都跳过验证，直接返回true
			return true;
		}
		int sum = 0;
		for (int i = 8; i < data.length; i++) {
			sum += data[i] & 0x000000ff;
		}
		int sum9 = GetCheckSum9(sum);

		return sum9 == checkSum;
	}

	/** 十进制数转换九进制，用于生成和检查校验和 */
	public int GetCheckSum9(int sum) {
		long sum2 = 0;
		int power = 1;
		while (sum > 0) {
			sum2 = (sum2 + ((sum % 9) * power));
			sum = (sum - (sum % 9)) / 9;
			power *= 10;
		}
		return (int) sum2;
	}

	/** 获取玩家连接的协议序列号 */
	public int getSerialNo(Channel channel) {
		synchronized (channel) {
			Attribute<Integer> attr = channel.attr(ChannelAttributeKey.SERIALNO);
			int serialno = attr.get();
			attr.set(serialno + 1);
			return serialno;
		}
	}

	/** String 转byte[] */
	public byte[] buildString(String str) {
		byte[] strbytes = null;
		byte[] res = null;
		ByteBuf buf = null;
		try {
			strbytes = str.getBytes("utf-8");
			buf = Unpooled.buffer(strbytes.length + 2);
			buf.writeShortLE((short) strbytes.length);
			buf.writeBytes(strbytes);
			res = buf.array();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (buf != null)
				buf.release();
		}
		return res;
	}

	/** 从ByteBuf中读取String */
	public String readString(ByteBuf buf) {
		short len = buf.readShortLE();
		byte[] strbytes = new byte[len];
		buf.readBytes(strbytes);
		String res = null;
		try {
			res = new String(strbytes, "utf-8");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return res;
	}

	/***/
	public BasicProto parseProto(short protoId, ByteBuf buf, Channel channel) {
		BasicProto res = IdToProto.get(protoId);
		if (res == null) {
			return null;
		}
		res.decode(buf);
		return res;
	}
}
