package com.yuechuankeji.event;


public abstract class EventProc {
	public boolean disable;
	public EventProc(){
		doReg();
	}
	public abstract void proc(Event param);
	public abstract void doReg();
}