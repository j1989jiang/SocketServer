package com.yuechuankeji.job;

import java.util.Calendar;

import org.quartz.*;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.event.EventID;
import com.yuechuankeji.event.EventMgr;

/**quartz测试类，一个定时任务的示例*/
public class JobTest implements Job{
	public static Logger log = LoggerFactory.getLogger(JobTest.class);
	
	/**当到达任务执行时间时，quartz自动调用此方法*/
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		Calendar c =  Calendar.getInstance();
		EventMgr.addEvent(110, EventID.Test, null);
		log.info("jobTest{}",c.getTime());
		
	}

}
