package com.yuechuankeji.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class SecurityFilter implements Filter{
	
	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		FilterMgr.inst.doFilter4Security(req, resp, chain);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}


}
