package com.example.demoRedis.domain;

import java.io.Serializable;
import java.util.Arrays;

import junit.framework.Assert;

import org.apache.tomcat.util.buf.StringUtils;

public class CustomCacheKey implements Serializable {

	 public static final CustomCacheKey EMPTY = new CustomCacheKey();

	 private final Object[] params;
	 private final int hashCode;

	 public CustomCacheKey(Object...elements) {
	
	  this.params = new Object[elements.length];
	  System.arraycopy(elements, 0, this.params, 0, elements.length);
	  this.hashCode = Arrays.deepHashCode(this.params);
	 }

	 @Override
	 public boolean equals(Object obj) {
	  return (this == obj || (obj instanceof CustomCacheKey &&
	   Arrays.deepEquals(this.params, ((CustomCacheKey) obj).params)));
	 }

	 @Override
	 public final int hashCode() {
	  return this.hashCode;
	 }

	
	}
