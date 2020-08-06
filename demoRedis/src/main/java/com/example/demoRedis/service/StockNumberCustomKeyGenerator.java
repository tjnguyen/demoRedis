package com.example.demoRedis.service;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

import com.example.demoRedis.domain.StockNumberInfo;

public class StockNumberCustomKeyGenerator implements KeyGenerator {

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
	      StockNumberInfo dppInfo = (StockNumberInfo)params[0]; 
		  return dppInfo.getStockNumber() ;
		  	  
		  
	  }
	  
	}
