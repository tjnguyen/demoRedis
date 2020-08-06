package com.example.demoRedis.domain;

import java.io.Serializable;

public class Segment implements Serializable{
	
	private String segmentNumber;
	private String status;
	private String path;
	
	public Segment(String segmentNumber,String status, String path) {
		this.path = path;
		this.segmentNumber = segmentNumber;
		this.status = status;
	}
	
	public String getSegmentNumber() {
		return segmentNumber;
	}
	public void setSegmentNumber(String segmentNumber) {
		this.segmentNumber = segmentNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	

}
