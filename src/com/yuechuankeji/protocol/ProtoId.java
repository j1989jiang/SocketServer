package com.yuechuankeji.protocol;

public class ProtoId {
	public static final short TEST_PROTOCOL = 1;
	//心跳协议号
	public static final short KEEP_ALIVE = 100;
	public static final short LOGIN = 5001;	//登录
	public static final short REGRIST = 5002;	//注册
	
	public static final short GET_PLAYER_INFO = 5004 ; 	//获取角色信息
	public static final short CREATE_PLAYER = 5005 ;	//创建角色
	public static final short ENTER_GAME = 5006 ;	//进入游戏
}
