package com.dlu.ghh.filter;

import java.io.IOException;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dlu.ghh.bean.User;
import com.dlu.ghh.utils.SessionUtils;

@Component
@WebFilter(urlPatterns={"/*"})
public class LoggerMDCFilter extends OncePerRequestFilter implements Filter{

	@Override
	public boolean isLoggable(LogRecord record) {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(request.getSession(false) != null && SessionUtils.sessionValidate(request.getSession())) {
			User user = (User) request.getSession().getAttribute("user");
			MDC.put("userId", user.getId()); 
			MDC.put("username", user.getUsername()); 
			MDC.put("password", user.getPassword()); 
		}
		filterChain.doFilter(request, response);
	}
	
}
