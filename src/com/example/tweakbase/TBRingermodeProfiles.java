package com.example.tweakbase;

public class TBRingermodeProfiles {
	private double latitude;
	private double longitude;
	private int dayOfWeek;
	private int intervalStartId;
	private int intervalEndId;
	private int type;
	private boolean active; 
	
	public TBRingermodeProfiles (double lat, double lon, int dow, int type, int intervalStartId, int intervalEndId, boolean act){
		this.latitude = lat;
		this.longitude = lon;
		this.dayOfWeek = dow;
		this.type = type; 
		this.intervalStartId = intervalStartId;
		this.intervalEndId = intervalEndId;
		this.active = act;
	}
	
	public TBRingermodeProfiles(){
		
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
	
	public int getIntervalStartId() {
		return intervalStartId;
	}

	public int getIntervalEndId() {
		return intervalEndId;
	}

	public void setIntervalStartId(int intervalStartId) {
		this.intervalStartId = intervalStartId;
	}

	public void setIntervalEndId(int intervalEndId) {
		this.intervalEndId = intervalEndId;
	}

	public int getType(){
		return type;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public boolean getActive(){
		return active;
	}
	
	public void setActive(boolean active){
		this.active = active;
	}
}
