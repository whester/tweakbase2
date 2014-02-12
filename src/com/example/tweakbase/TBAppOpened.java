package com.example.tweakbase;

import java.util.Calendar;

public class TBAppOpened {
	private double latitude;
	private double longitude;
	private int dayOfWeek;
	private int intervalId;
	private String name;
	private String packagename;
	
	public TBAppOpened (double lat, double lon, int dow, String name, String packagename){
		this.latitude = lat;
		this.longitude = lon;
		this.dayOfWeek = dow; 
		this.name = name;
		this.packagename = packagename;
		this.intervalId = generateIntervalId();
	}
	
	public TBAppOpened(){
		
	}
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double lat) {
		this.latitude = lat;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double lon) {
		this.longitude = lon;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getIntervalId() {
		return intervalId;
	}

	public void setIntervalId(int intervalId) {
		this.intervalId = intervalId;
	}
	
	public int generateIntervalId() {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long passed = now - c.getTimeInMillis();
		long secondsPassed = passed / 1000;
		long minutesPassed = secondsPassed / 60;
		return (int) minutesPassed / 5;
	}
	
	public void setAppName(String name){
		this.name = name;
	}

	public String getAppName() {
		return name;
	}
	
	public void setAppPackageName(String packagename){
		this.packagename = packagename;
	}

	public String getAppPackageName() {
		return packagename;
	}

}
