package com.yuechuankeji.network;

import io.netty.util.AttributeKey;

public class ChannelAttributeKey {
	public static final AttributeKey<Integer> ACCOUNT_ID = AttributeKey.newInstance("ACCOUNT_ID");
	public static final AttributeKey<Integer> PLAYER_ID = AttributeKey.newInstance("PLAYER_ID");
	public static final AttributeKey<Integer> SERIALNO = AttributeKey.newInstance("SERIALNO");
	public static final AttributeKey<String> ACCOUNT_NAME = AttributeKey.newInstance("ACCOUNT_NAME");
}
