<%@page import="javax.persistence.criteria.CriteriaBuilder.In"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.apache.commons.httpclient.params.HttpMethodParams"%>
<%@page import="org.apache.commons.httpclient.HttpMethod"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.nio.CharBuffer"%>
<%@page import="java.nio.ByteBuffer"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStream"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
Logger log = LoggerFactory.getLogger(this.getClass());

session.setMaxInactiveInterval(20);

long begin = System.currentTimeMillis();

char[] c1 = null ;
try{
	String method = request.getMethod();
	if("post".equalsIgnoreCase(method)){
		StringBuilder sb = new StringBuilder();
		String lenStr = request.getHeader("content-length") ;
		int len = 1 ;
		if(lenStr != null && !"".equals(lenStr)){
			len = Integer.parseInt(lenStr.trim());
		}
		InputStream in = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		OutputStream output = response.getOutputStream();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		String line = null ;
		byte[] temp = new byte[len] ;
		while(in.read(temp) != -1){
			byteOut.write(temp);
		}
		byte[] req = byteOut.toByteArray();
		String url = new String(req);
		for(int i = 0 ; i < req.length ; i++){
			sb.append(req[i] + " ");
		}
		String byteStr = sb.toString();
		sb.setLength(0);
		log.info("\r\nbodystr: "+ url);
		log.info("\r\nbodybyte: "+ byteStr);
		byte[] bytes = new byte[]{-1,-3,45,66,-128,127,0};
		response.setHeader("content-length", bytes.length + "");
		output.write(bytes);
		output.flush();
		output.close();
		byteOut.close();
		response.flushBuffer();  
		out.clear();
		out = pageContext.pushBody();
		long end = System.currentTimeMillis();
		log.info("耗时：{}",end-begin);
	}
}catch(Exception e){
	e.printStackTrace();
}

%>
