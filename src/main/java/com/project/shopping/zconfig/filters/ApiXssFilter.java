package com.project.shopping.zconfig.filters;

import java.io.IOException;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeFilter;
import com.project.shopping.utils.XssServeltRequestWrapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * 
 * @author
 * @date 2024.05.12
 */
public class ApiXssFilter implements Filter {
	private XssEscapeFilter xssEscapeFilter = XssEscapeFilter.getInstance();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new XssServeltRequestWrapper(request, xssEscapeFilter), response);

	}

	@Override
	public void destroy() {}
}
