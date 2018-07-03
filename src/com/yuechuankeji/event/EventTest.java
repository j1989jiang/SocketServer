package com.yuechuankeji.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventTest extends EventProc {
	public static Logger log = LoggerFactory.getLogger(EventTest.class);
	
	public static EventTest inst ;
	
	public static EventTest getInst(){
		if(inst == null){
			inst = new EventTest();
			inst.init();
		}
		return inst ;
	}
	
	public void init(){
		
	}
	@Override
	public void proc(Event param) {
		log.info("检测到时间ID为{}的事件触发" , EventID.Test);
	}

	/**
	 * 事件注册，表示当触发某个ID的事件时，会在线程中调用这个类的proc方法*/
	public void doReg() {
		EventMgr.regist(EventID.Test, this);

	}

}
