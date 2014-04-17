package com.example.tweakbase;

import android.media.AudioManager;

public class TBRingermodeProfiles {
	private double latitude;
	private double longitude;
	private int id;
	private int dayOfWeek;
	private int intervalStartId;
	private int intervalEndId;
	private int type;
	private int active; 
	
	public TBRingermodeProfiles (double lat, double lon, int dow, int type, int intervalStartId, int intervalEndId, int act){
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
	
	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id = id;
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
	
	public int getActive(){
		return active;
	}
	
	public void setActive(int active){
		this.active = active;
	}
	
	public boolean equals(Object other) {
		TBRingermodeProfiles o = (TBRingermodeProfiles) other;
		if (o.getDayOfWeek() == this.dayOfWeek && o.getIntervalEndId() == this.intervalEndId && o.getIntervalStartId() == this.intervalStartId && o.getType() == this.type) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString(){
		TBRingermodeProfiles o = (TBRingermodeProfiles) this;
		String s = o.getDayOfWeekString() + " from " + o.getSartTimeString() + " to " + o.getEndTimeString() + " mode changes to " + o.getRingermodeTypeString() + ".";
		return s;
	}
	
	public String getSartTimeString() {
		int startHour = (intervalStartId/12)%12;
		int startMinute = (intervalStartId * 5) % 60;
		String startHourString;
		String startMinuteString;
		String m = intervalStartId >= 144 ? "pm" : "am";
		if (startMinute < 10) {
			startMinuteString = "0" + startMinute;
		} else {
			startMinuteString = Integer.toString(startMinute);
		}
		if (startHour == 0) {
			startHourString = "12";
		} else {
			startHourString = Integer.toString(startHour);
		}
		return startHourString + ":" + startMinuteString + m;
	}
	
	public String getEndTimeString() {
		int endHour = (intervalEndId/12)%12;
		int endMinute = (intervalEndId * 5) % 60;
		String m = intervalEndId >= 144 ? "pm" : "am";
		if (endMinute >= 10) {
			return endHour + ":" + endMinute + m;
		} else {
			return endHour + ":0" + endMinute + m;
		}
	}
	
	public String getDayOfWeekString() {
		switch (dayOfWeek) {
		case 1: return "Sunday";
		case 2: return "Monday";
		case 3: return "Tuesday";
		case 4: return "Wednesday";
		case 5: return "Thursday";
		case 6: return "Friday";
		case 7: return "Saturday";
		default: return "Invalid date";
		}
	}
	
	public String getRingermodeTypeString() {
		if (type == AudioManager.RINGER_MODE_SILENT) {
			return "silence";
		}else if(type == AudioManager.RINGER_MODE_VIBRATE){
			return "vibrate";
		}else if(type == AudioManager.RINGER_MODE_NORMAL){
			return "ring";
		}else{
			return "well this is awkward";
		}
	}
}
