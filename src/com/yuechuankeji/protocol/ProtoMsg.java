package com.yuechuankeji.protocol;


public class ProtoMsg {
	public short id ;
	public BasicProto builder;
	
	public ProtoMsg(){
	}
	
	public ProtoMsg(short id ){
		this.id = id ;
	}
	public ProtoMsg(short id , BasicProto builder ){
		this.id = id ;
		this.builder = builder;
	}
}
