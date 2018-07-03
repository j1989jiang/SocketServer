package com.yuechuankeji.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TPE extends ThreadPoolExecutor{
	public static Logger log = LoggerFactory.getLogger(TPE.class.getSimpleName());
	public TPE(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		if(t != null){
			log.error("A执行出错",t);
		}else if (r instanceof Future) {
			try{
				Future<?> f = (Future<?>) r;
				f.get();
			}catch(ExecutionException e){
				log.error("C出错", e.getCause());
			}catch(Exception e){
				log.error("B执行出错",e);
			}
		}else{
			log.warn("what's this ? {}",r.getClass().getName());
		}
	}
	
	//加入优先队列，可以提前处理
	public <T> Future<T> submitPQ(Callable<T> task) {
		if (task == null) throw new NullPointerException();
		RunnableFuture<T> ftask = new PriorityFutureTask<T>(task);
		execute(ftask);
		return ftask;
	}
}