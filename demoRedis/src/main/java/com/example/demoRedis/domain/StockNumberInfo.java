package com.example.demoRedis.domain;

import java.io.Serializable;
import java.util.List;

public class StockNumberInfo implements Serializable{
	
	private String stockNumber;
	private int volume;
	private List<Segment> segments; 
	private String topPath;
	
	
	
	public String getStockNumber() {
		return stockNumber;
	}
	public void setStockNumber(String stockNumber) {
		this.stockNumber = stockNumber;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public List<Segment> getSegments() {
		return segments;
	}
	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}
	public String getTopPath() {
		return topPath;
	}
	public void setTopPath(String topPath) {
		this.topPath = topPath;
	}
	
	
	

}
