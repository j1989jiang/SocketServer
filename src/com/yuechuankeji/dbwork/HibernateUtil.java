package com.yuechuankeji.dbwork;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.bean.DBhash;
import com.yuechuankeji.cache.Cache;

import net.sf.json.JSONObject;

/**数据存储工具类，hibernate框架的包装*/
public class HibernateUtil {
	
	public static Logger log = LoggerFactory.getLogger(HibernateUtil.class);
	public static Map<Class<?>, String> beanKeyMap = new HashMap<Class<?>, String>();
	public static final SessionFactory sessionFactory = buildSessionFactory();

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static SessionFactory buildSessionFactory() {
    	log.info("开始构建hibernate");
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	Configuration configuration = new Configuration().configure();
        	ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        	SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        	log.info("结束构建hibernate");
        	return sessionFactory;
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            log.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    
    public static void insert(Object o){
    	if(o instanceof DBhash){
    		DBSaver.inst.insert((DBhash)o);
    	}else{
    		insertToDB(o);
    	}
    }
    
    
    protected static void insertToDB(Object o){
    	Session session = sessionFactory.getCurrentSession();
    	session.beginTransaction();
    	try {
   			session.save(o);
        	session.getTransaction().commit();
		} catch (Throwable e) {
			Object info = o == null ? "null" : o.getClass().getSimpleName()+JSONObject.fromObject(o).toString();
			log.error("insert fail"+info, e);
			session.getTransaction().rollback();
		}
    }
    
    
    public static void update(Object o){
    	if(o instanceof DBhash){
    		DBSaver.inst.update((DBhash)o);
    	}else{
    		updateToDB(o);
    	}
    }
    
    protected static void updateToDB(Object o){
    	Session session = sessionFactory.getCurrentSession();
    	session.beginTransaction();
    	try {
   			session.update(o);
        	session.getTransaction().commit();
		} catch (Throwable e) {
			Object info = o == null ? "null" : o.getClass().getSimpleName()+JSONObject.fromObject(o).toString();
			log.error("update fail"+info, e);
			session.getTransaction().rollback();
		}
    }
    
    
    public static void delete(Object o){
    	if(o instanceof DBhash){
    		DBSaver.inst.delete((DBhash)o);
    	}else{
    		updateToDB(o);
    	}
    }
    
    protected static void deleteFromDB(Object o){
    	Session session = sessionFactory.getCurrentSession();
    	session.beginTransaction();
    	try {
   			session.delete(o);
        	session.getTransaction().commit();
		} catch (Throwable e) {
			Object info = o == null ? "null" : o.getClass().getSimpleName()+JSONObject.fromObject(o).toString();
			log.error("delete fail"+info, e);
			session.getTransaction().rollback();
		}
    }
    
    /**
     * 通过主键id获取制定数据
     * */
    public static <T> T find(Class<T> t,long id){
    	Map cacheMap = Cache.cacheMap.get(t);
    	T ret = null ;
    	if(cacheMap != null ){
    		ret = (T)cacheMap.get(id);
    		if( ret != null ){
    			return ret ;
    		}else if(cacheMap.containsKey(id)){
    				return null ;
    		}
    	}
    	ret = findFromDB(t, id);
    	if(cacheMap != null ){
    		cacheMap.putIfAbsent(id, ret);
    	}
    	return ret;
    	
    }
    
    public static <T> T findFromDB(Class<T> t,long id){
    	String keyField = getKeyField(t);
    	
    	if(keyField == null){
    		throw new RuntimeException("类型"+t+"没有标注主键");
    	}
    	T ret = find(t, "where "+keyField+"="+id);
    	return ret;
    }
    /**
     * @param where 举例："where id > 100"
     * */
    public static <T> T find(Class<T> t,String where){
    	Session session = sessionFactory.getCurrentSession();
    	Transaction tr = session.beginTransaction();
    	T ret = null;
    	try{
	    	String hql = "from "+t.getSimpleName()+" "+ where;
	    	Query query = session.createQuery(hql);
	    	ret = (T) query.uniqueResult();
	    	tr.commit();
    	}catch(Exception e){
    		tr.rollback();
    		log.error("list fail for {} {}", t, where);
    		log.error("list fail", e);
    	}
    	return ret;
    }
    
    /**
     * @param t
     * @param where 例子： where id>100
     */
    public static <T>  List<T> list(Class<T> t, String where){
    	Session session = sessionFactory.getCurrentSession();
    	Transaction tr = session.beginTransaction();
    	List<T> list = Collections.EMPTY_LIST;
    	try{
	    	String hql = "from "+t.getSimpleName()+" "+ where;
	    	Query query = session.createQuery(hql);
	    	
	    	list = query.list();
	    	tr.commit();
    	}catch(Exception e){
    		tr.rollback();
    		log.error("list fail for {} {}", t, where);
    		log.error("list fail", e);
    	}
    	return list;
    }
    
    
    /**
     * @param t
     * @param where 例子： where id>100
     * @param start 从第几条开始取，下标0开始
     * @param num 结果显示条数 
     */
    public static <T>  List<T> list(Class<T> t, String where,int start,int num){
    	Session session = sessionFactory.getCurrentSession();
    	Transaction tr = session.beginTransaction();
    	List<T> list = Collections.EMPTY_LIST;
    	try{
	    	String hql = "from "+t.getSimpleName()+" "+ where;
	    	Query query = session.createQuery(hql);
	    	query.setFirstResult(start);
	    	query.setMaxResults(num);
	    	list = query.list();
	    	tr.commit();
    	}catch(Exception e){
    		tr.rollback();
    		log.error("list fail for {} {}", t, where);
    		log.error("list fail", e);
    	}
    	return list;
    }
    
    
    /**
     * 可以一次查询主键容器中的所有数据条目并返回
     * @param keyValues 主键容器*/
    public static<T> List<T> list( Class<T> t, Collection keyValues){
    	String keyName = getKeyField(t);
    	if(keyName == null || "".equals(keyName)){
    		throw new RuntimeException("类型"+t+"没有标注主键");
    	}
		List<T> res  = Collections.emptyList() ;
		Session session = sessionFactory.getCurrentSession();
		Transaction tr = session.beginTransaction();
		try{
			Criteria cr = session.createCriteria(t);
			cr=cr.add(Restrictions.in(keyName, keyValues));
			res = cr.list();
			tr.commit();
		}catch(Exception e){
			tr.rollback();
			log.error("查询失败 class:{} keyName:{}",t.getSimpleName(), keyName);
		}
		return res ;
	}
    
    
    public static String getKeyField(Class<?> c){
    	synchronized (beanKeyMap) {
	    	String key = beanKeyMap.get(c);
	    	if(key != null){
	    		return key;
	    	}
	    	Field[] fs = c.getDeclaredFields();
	    	for(Field f : fs){
	    		if(f.isAnnotationPresent(javax.persistence.Id.class)){
	    			key = f.getName();
	    			beanKeyMap.put(c, key);
	    			break;
	    		}
	    	}
	    	return key;
    	}
    }
}
