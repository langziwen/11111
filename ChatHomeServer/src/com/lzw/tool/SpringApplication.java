package com.lzw.tool;

import org.springframework.context.ApplicationContext;

public class SpringApplication {
	
	private static ApplicationContext ctx;
	
	public static void setApplicationContext(ApplicationContext temp){
		ctx = temp;
	}
	
	public static ApplicationContext getApplicationContext(){
		return ctx;
	}
}
