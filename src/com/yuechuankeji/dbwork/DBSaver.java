package com.yuechuankeji.dbwork;

import java.util.concurrent.TimeUnit;
import com.yuechuankeji.bean.DBhash;
import com.yuechuankeji.init.ServerConfig;
import com.yuechuankeji.util.PriorityQ;
import com.yuechuankeji.util.TPE;

/**数据延迟存储线程，将数据存储请求放在线程池中执行，充分利用cpu空闲时间，缓解io接口压力*/
public class DBSaver {
	public static TPE[] es;
	public static DBSaver inst;
	
	public static DBSaver getInst(){
		if(inst == null ){
			inst = new DBSaver();
			inst.init();
		}
		return inst;
	}
	
	public void init(){
		es = new TPE[ServerConfig.DBSaverSize];
		int len = es.length;
		for(int i=0;i<len; i++){
			es[i] = new TPE(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new PriorityQ<Runnable>());
		}
	}
	
	public int hash(DBhash b) {
		long hash = b.hash();
		hash/=1000;
		long grid = hash % es.length;
		int v = (int) grid;
		return v;
	}
	
	public void shutdown(){
		int len = es.length;
		for(int i=0;i<len; i++){
			es[i].shutdown();
		}
	}
	
	public void insert(DBhash b) {
		int v = hash(b);
		es[v].submit(()->
			HibernateUtil.insertToDB(b)
		);		
	}
	
	public void update(DBhash b){
		int v = hash(b);
		es[v].submit(()->
			HibernateUtil.updateToDB(b)
		);	
	}
	
	
	public void delete(DBhash b) {
		int v = hash(b);
		es[v].submit(()->
			HibernateUtil.deleteFromDB(b)
		);		
	}
	
}
