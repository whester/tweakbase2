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

	public boolean equals(Object tbr) {
		if (tbr instanceof TBRingermodePair) {
			TBRingermodePair t = (TBRingermodePair) tbr;
			return this.startIntervalId == t.getStartIntervalId() && this.endIntervalId == t.getEndIntervalId() && 
					this.type == t.getType() && this.dayOfWeek == t.getDayOfWeek();
		} else {
			return false;
		}
	}
	
	public long getMillisOfNextOccurance() {
		Calendar cNow = Calendar.getInstance();
		long now = cNow.getTimeInMillis();
		cNow.set(Calendar.HOUR_OF_DAY, 0);
		cNow.set(Calendar.MINUTE, 0);
		cNow.set(Calendar.SECOND, 0);
		cNow.set(Calendar.MILLISECOND, 0);
//		if (dayOfWeek - Calendar.getInstance().get(Calendar.DAY_OF_WEEK) > 0) {
//			nextOccurance += (dayOfWeek - Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) * 1000 * 60 * 60 * 24;
//		} else if (dayOfWeek - Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 0) {
//			// do nothing
//		} else {
//			nextOccurance += (7 - ( Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - dayOfWeek)) * 1000 * 60 * 60 * 24;
//		}
//		nextOccurance += (startIntervalId * 1000 * 60 * 5) - millisSinceMidnight;
//		if (nextOccurance <= 0) {
//			nextOccurance += 1000 * 60 * 60 * 24 * 7;	// A week from now
//		}
//		return System.currentTimeMillis() + nextOccurance + 1000 * 60 * 60 * 6;	// 6 hour offset FOR central time
		
		Calendar cNext = Calendar.getInstance();
		cNext.set(Calendar.HOUR_OF_DAY, startIntervalId/12);
		cNext.set(Calendar.MINUTE, (startIntervalId%12) * 5);
		cNext.set(Calendar.SECOND, 0);
		cNext.set(Calendar.MILLISECOND, 0);
		cNext.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		
		if (cNext.getTimeInMillis() - now < 0) {
			return cNext.getTimeInMillis() + 1000 * 60 * 60 * 24 * 7;
		} else {
			return cNext.getTimeInMillis();
		}
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
//		int endHour = (endIntervalId/12)%12;
//		int endMinute = (endIntervalId * 5) % 60;
//		String m = endIntervalId >= 144 ? "pm" : "am";
//		if (endMinute >= 10) {
//			return endHour + ":" + endMinute + m;
//		} else {
//			return endHour + ":0" + endMinute + m;
//		}
		
		int endHour = (endIntervalId/12)%12;
		int endMinute = (endIntervalId * 5) % 60;
		String endHourString;
		String endMinuteString;
		String m = endIntervalId >= 144 ? "pm" : "am";
		if (endMinute < 10) {
			endMinuteString = "0" + endMinute;
		} else {
			endMinuteString = Integer.toString(endMinute);
		}
		if (endHour == 0) {
			endHourString = "12";
		} else {
			endHourString = Integer.toString(endHour);
		}
		return endHourString + ":" + endMinuteString + m;
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
