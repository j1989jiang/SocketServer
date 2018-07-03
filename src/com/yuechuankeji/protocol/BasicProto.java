package com.yuechuankeji.protocol;

import io.netty.buffer.ByteBuf;

public interface BasicProto {
	public void encode(ByteBuf buf);

	public void decode(ByteBuf buf);
}
