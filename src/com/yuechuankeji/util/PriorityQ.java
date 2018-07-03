package com.yuechuankeji.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class PriorityQ<V> extends LinkedBlockingQueue<V>{
	
	private static final long serialVersionUID = -4670632698923751428L;
	public LinkedBlockingQueue<V> pQ = new LinkedBlockingQueue<>();
	
	@Override
	public boolean isEmpty() {
		return super.isEmpty() && pQ.isEmpty();
	}
	
	@Override
	public boolean offer(V e) {
		if(e instanceof PriorityFutureTask){
			return pQ.offer(e);
		}
		return super.offer(e);
	}
	
	@Override
	public V poll(long timeout, TimeUnit unit) throws InterruptedException {
		if(pQ.isEmpty()){
			return super.poll(timeout, unit);
		}else{
			return pQ.poll(timeout, unit);
		}
	}
	
	@Override
	public V take() throws InterruptedException {
		if(pQ.isEmpty()){
			return super.take();
		}else{
			return pQ.take();
		}
	}
}