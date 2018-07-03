package com.yuechuankeji.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdToProto {
	public static Logger log = LoggerFactory.getLogger(IdToProto.class);
	public static BasicProto get(short protoId){
		switch(protoId){
		
		default:
			log.error("unknown protoId{}" , protoId );
			return null;
		}
	}
}
