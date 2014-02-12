package com.example.tweakbase;

public class TBAccelerometer {
	private double latitude;
	private double longitude;
	private int dayOfWeek;
	private double xcoordinate;
	private double ycoordinate;
	private double zcoordinate;
	
	public TBAccelerometer(double lat, double lon, int dow, double x, double y, double z){
		this.latitude = lat;
		this.longitude = lon;
		this.dayOfWeek = dow;
		this.xcoordinate = x;
		this.ycoordinate = y;
		this.zcoordinate = z;
	}
	
	public TBAccelerometer(){
		
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
	
	public double getXcoordinate(){
		return xcoordinate;
	}
	
	public void setXcoordinate(double x){
		this.xcoordinate = x;
	}
	
	public double getYcoordinate(){
		return ycoordinate;
	}
	
	public void setYcoordinate(double y){
		this.ycoordinate = y;
	}
	
	public double getZcoordinate(){
		return zcoordinate;
	}
	
	public void setZcoordinate(double z){
		this.zcoordinate = z;
	}

}
