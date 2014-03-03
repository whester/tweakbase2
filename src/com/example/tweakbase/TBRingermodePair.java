package com.example.tweakbase;

import java.io.Serializable;
import java.util.Calendar;

public class TBRingermodePair implements Serializable {
	private static final long serialVersionUID = 1L;	// Auto-generated
	private int startIntervalId;
	private int endIntervalId;
	private int type;
	private int dayOfWeek;
	private boolean isOvernight;
	
	// Went from t1 to t2
	public TBRingermodePair(TBRingermode t1, TBRingermode t2) {
		startIntervalId = t1.getIntervalId();
		endIntervalId = t2.getIntervalId();
		type = t2.getType();
		dayOfWeek = t1.getDayOfWeek();
		isOvernight = t1.getDayOfWeek() == t2.getDayOfWeek();
	}

	public int getStartIntervalId() {
		return startIntervalId;
	}

	public int getEndIntervalId() {
		return endIntervalId;
	}

	public int getType() {
		return type;
	}
	
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public boolean isOvernight() {
		return isOvernight;
	}

	public boolean equals(TBRingermodePair t) {
		return this.startIntervalId == t.getStartIntervalId() && this.endIntervalId == t.getEndIntervalId() && 
				this.type == t.getType() && this.dayOfWeek == t.getDayOfWeek();
	}
	
	public long getMillisOfNextOccurance() {
		long nextOccurance = 0;
		if (dayOfWeek - Calendar.getInstance().get(Calendar.DAY_OF_WEEK) > 0) {
			nextOccurance += (dayOfWeek - Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) * 1000 * 60 * 60 * 24;
		} else {
			nextOccurance += (7 - ( Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - dayOfWeek)) * 1000 * 60 * 60 * 24;
		}
		nextOccurance += (startIntervalId * 1000 * 60 * 5) - (System.currentTimeMillis()%(1000*60*60*24));
		if (nextOccurance <= 0) {
			nextOccurance += 1000 * 60 * 60 * 24 * 7;	// A week from now
		}
		return System.currentTimeMillis() + nextOccurance + 1000 * 60 * 60 * 6;	// 6 hour offset FOR central time
	}
	
	public String getSartTimeString() {
		int startHour = (startIntervalId/12)%12;
		int startMinute = (startIntervalId * 5) % 60;
		String startHourString;
		String startMinuteString;
		String m = startIntervalId >= 144 ? "pm" : "am";
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
		int endHour = (endIntervalId/12)%12;
		int endMinute = (endIntervalId * 5) % 60;
		String m = endIntervalId >= 144 ? "pm" : "am";
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
		if (type == 0) {
			return "silence your phone";
		} else {
			return "set your phone to vibrate";
		}
	}
}
