package com.example.demoRedis.service;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

import com.example.demoRedis.domain.Segment;
import com.example.demoRedis.domain.StockNumberInfo;

public class SegmentCustomKeyGenerator implements KeyGenerator {

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
	      Segment segment = (Segment)params[0]; 
	      System.out.println("segment key " + segment.getSegmentNumber());
		  return segment.getSegmentNumber() ;
		  	  
		  
	  }
	  
	}
