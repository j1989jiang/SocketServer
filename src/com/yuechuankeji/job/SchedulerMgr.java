package com.yuechuankeji.job;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**定时任务框架quartz,用于触发各种任务
 * 在doSchedule()方法中添加需要执行的定时任务 */
public class SchedulerMgr {
	public static Scheduler scheduler;
	public static SchedulerMgr inst;
	public static Logger log = LoggerFactory.getLogger(SchedulerMgr.class);

	public static SchedulerMgr getInst(){
		if(inst == null ){
			inst = new SchedulerMgr();
			inst.init();
		}
		return inst;
	}
	public void init(){
		SchedulerFactory  sfa = new StdSchedulerFactory();
		try {
			scheduler = sfa.getScheduler();
			scheduler.start();
			log.info("获取scheduler成功");
		}catch (SchedulerException e) {
			log.error("获取scheduler失败");
			e.printStackTrace();
		}
		doSchedule();
	}
	
	
	public void addScheduler(Class<? extends Job> jobClass, String time){
		JobDetail job = JobBuilder.newJob(jobClass).build();
		CronTrigger trigger =
				TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule(time)) 
				.build(); 
		try {
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			log.error("添加job：{}到定时任务列表中失败", jobClass);
		}
	}
	
	/**
	 * 时间格式：秒 分 时 日 月 周 年
	 * 注意	：周和日必须有一个且只能有一个是?
	 * 举个例子：0 0 2 * * ? * 代表每年每月每天的2点0分0秒触发 问号表示不关心具体是周几
	 * */
	public void doSchedule(){
		//TODO,所有定时任务添加工作
		addScheduler(JobTest.class, "0/10 50-59 * * * ?");
	}
	
	/**
	 * 关服时调用，关闭定时服务
	 */
	public void stop(){
		try {
			scheduler.shutdown(true);
		} catch (SchedulerException e){
			e.printStackTrace();
		}
	}
}
