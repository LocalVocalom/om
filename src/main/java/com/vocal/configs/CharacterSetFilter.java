package com.vocal.configs;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;

import com.vocal.utils.Constants;

//@Component
//@Order(1)
public class CharacterSetFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CharacterSetFilter.class);
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LOGGER.info("Character Encoding used={}" + request.getCharacterEncoding());
		request.setCharacterEncoding(Constants.UTF8_ENCODING);
		//response.setContentType("text/html; charset=UTF-8");
		
		response.setContentType("application/json; charset=UTF-8");
		response.setCharacterEncoding(Constants.UTF8_ENCODING);
		chain.doFilter(request, response);
		
	}

}
