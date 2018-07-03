package com.yuechuankeji.util;


import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class PriorityFutureTask<V> extends FutureTask<V>{

	public PriorityFutureTask(Callable<V> callable) {
		super(callable);
	}

}