package com.example.tweakbase;

import android.R.string;

public class TBLocationCluster {
	
	private int intervalID;
	private int dayOfWeek;
	private String appName;
	private double lat;
	private double lon;
	private int clusterNum;
	
	public TBLocationCluster(TBAppOpened app){
		intervalID = app.getIntervalId();
		dayOfWeek = app.getDayOfWeek();
		appName = app.getAppName();
		lat = app.getLatitude();
		lon = app.getLongitude();
	}
	
	public double getLat(){
		return lat;
	}
	
	public double getLon(){
		return lon;
	}
	
	public void setClusterNum (int number){
		clusterNum = number;
	}
	
	public int getClusterNum(){
		return clusterNum;
	}
}
