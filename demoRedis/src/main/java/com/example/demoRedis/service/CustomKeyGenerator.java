package com.example.demoRedis.service;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;

import com.example.demoRedis.domain.CustomCacheKey;

public class CustomKeyGenerator implements KeyGenerator {

	 @Override
	 public Object generate(Object target, Method method, Object...params) {
		 System.out.println("target " + target.toString() + "method " + method.getName());
	  return generateKey(params);
	 }

	 /**
	  * Generate a key based on the specified parameters.
	  */
	 public static String generateKey(Object...params) {
	  /*if (params.length == 0) {
	   return SimpleKey.EMPTY;
	  }*/
	      String result = null;
		  int len = params.length;
		  result = (String)params[0];
		  if(len > 1 ) {
		     for(int i = 1; i<len; i++) {
			    System.out.println("param " + params[i]);
			    result = result.concat("-");
			    result = result.concat((String)params[i]);
		     }
		  }
		  
		  
		  return result;
		  	  
		  
	  }
	  
	}
