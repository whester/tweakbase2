package com.example.tweakbase;

public class LocationObject implements Comparable {
	
	private double lon;
	private double lat;
	private String name;
	private int id;
	private int count;
	
	public LocationObject(double lon, double lat, String name, int id, int count){
		this.lon = lon;
		this.lat = lat;
		this.name = name;
		this.id = id;
		this.count = count;
	}
	
	public int compareTo(Object location){
		return this.count - ((LocationObject)location).getCount();
	}
	
	
	public LocationObject(){
		
	}
	
	public void setLon(double newLon){
		lon = newLon;
	}
	
	public void setLat(double newLat){
		lat = newLat;
	}
	
	public void setName(String newName){
		 name = newName;
	}
	
	public void setID(int idNum){
		id = idNum; 
	}
	
	public void setCount (int newCount){
		count = newCount;
	}
	
	public double getLon(){
		return lon;
	}
	
	public double getLat(){
		return lat;
	}
	
	public String getName(){
		return name;
	}
	
	public int getID(){
		return id;
	}
	
	public int getCount(){
		return count;
	}

	

	

}
