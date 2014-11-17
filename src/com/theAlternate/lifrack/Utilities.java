package com.theAlternate.lifrack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public final class Utilities {
	private static final String LOG_TAG = "Utilities";
	
	public Utilities(){} //empty constructor to prevent instantiation
	
	/**
	 * Method to detect presence of duplicates in a generic list. 
	 * Depends on the equals method of the concrete type. make sure to implement it as required.
	 */
	public static <T> boolean hasDuplicates(List<T> list){
		if(list == null) return false;
		
		int count = list.size();
		T t1,t2;
		
		for(int i=0;i<count;i++){
			t1 = list.get(i);
			for(int j=i+1;j<count;j++){
				t2 = list.get(j);
				if(t2.equals(t1)){
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Method to get the properly formatted current time in local timezone.
	 * This is the format used by SQLite for storing all time values in localDB as text 
	 */
	public static String getCurrentTimeFormatted() {
		String date = getSimpleDateFormat().format(new Date()); 
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,date);}
		return date;
	}
	
	/**
	 * Method to get the properly formatted current time in UTC
	 * This is the format used by SQLite for storing all time values in localDB as text 
	 */
	public static String getCurrentTimeFormattedUTC() {
		String date = getSimpleDateFormatUTC().format(new Date()); 
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,date);}
		return date;
	}
	
	/**
	 * Method to get the format used by SQLite for storing all time values in localDB as text.
	 * The timezone used is default, which is the local timezone
	 */
	public static SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH); //caps in the format make a difference. be careful when changing.
	}
	
	/**
	 * Method to get the format used by SQLite for storing all time values in localDB as text.
	 * in UTC timezone
	 */
	public static SimpleDateFormat getSimpleDateFormatUTC() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return simpleDateFormat;
	}
	
	/**
	 * Method to get the display format of time values.
	 */
	@Deprecated
	public static SimpleDateFormat getDisplaySimpleDateFormat() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy",Locale.ENGLISH);
		return simpleDateFormat;
	}
	
	/**
	 * Method to format a date for display
	 */
	public static String getDisplayFormattedDate(Date dt) {
		if(dt == null) throw new IllegalArgumentException();
		
		SimpleDateFormat simpleDateFormat;
		String result = "";

		int daysDiff = getDayDifference(dt, new Date());
		if(daysDiff == 0){
			result = "today";
		} 
		else if(daysDiff == 1) result = "yesterday";
		else if(daysDiff < 7){
			simpleDateFormat = new SimpleDateFormat("EEEE",Locale.ENGLISH);
			result = simpleDateFormat.format(dt);
		}
		else{
			simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy",Locale.ENGLISH);
			result = simpleDateFormat.format(dt);
		}
		return result;
	}
	
	
	
	
	
	/*Accepts a number and 
	 * returns a string with prefix 0 if it is 1 digit
	 */
	public static String padSingleDigit(int c){
		if(c >= 10){
			return String.valueOf(c);
		}
		else return "0" + String.valueOf(c);
	}
	
	public static String getDurationFromNow(String time){
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"time="+time);}
		if(time == null) return "";
		
		final long ONE_MINUTE = 1 * 60 * 1000; // 1m * 60s * 1000ms
		final long ONE_HOUR = 1 * 60 * 60 * 1000; // 1hr * 60m * 60s * 1000ms
		final long ONE_DAY = 1 * 24 * 60 * 60 * 1000; // 1day * 24hrs * 60m * 60s * 1000ms
		//final long ONE_WEEK = 1 * 7 * 24 * 60 * 60 * 1000; // 1week * 7days * 24hrs * 60m * 60s * 1000ms
		
		SimpleDateFormat df = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		Date dtTime;
		try {
			dtTime = df.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return "error";
		}
		Date dtNow = new Date();
		
		long diffTotal = dtNow.getTime() - dtTime.getTime();
		long diffMinutes = diffTotal / ( 1000 * 60 );
		long diffHours = diffTotal / ( 1000 * 60 * 60);
		long diffDays = diffTotal / ( 1000 * 60 * 60 * 24);
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"diffTotal="+diffTotal+",diffMinutes="+diffMinutes+",diffHours="+diffHours+",diffDays="+diffDays);}
		StringBuilder result = new StringBuilder();
		long temp;
		
		if(diffTotal <=  ONE_MINUTE){
			//return "just now";
			result.append("just now");
		}
		else if(diffTotal <=  ONE_HOUR){
			if(diffMinutes == 1) result.append("1 min");
			else result.append(diffMinutes + " mins");
			
			result.append(" ago");
		}
		else if(diffTotal <=  ONE_DAY){
			if(diffHours == 1) result.append("1 hr");
			else result.append(diffHours + " hrs");

			temp = (diffMinutes - (diffHours * 60));
			if(temp == 1) result.append(",1 min");
			else if(temp > 1) result.append("," + temp + " mins");
			result.append(" ago");
		}
		else {
			if(diffDays == 1) result.append("1 day");
			else result.append(diffDays + " days");
				
			temp = diffHours - diffDays*24;
			if(temp == 1) result.append(",1 hr");
			else if(temp > 1) result.append("," + temp + " hrs");
			/*temp = diffMinutes - diffHours*60;
			if(temp > 0) result.append("," + temp + " mins");*/
			result.append(" ago");
		}
		
		return result.toString();
	}
	
	public static String getDurationFromNow(Date time){
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"time="+time);}
		if(time == null) return "";
		
		final long ONE_MINUTE = 1 * 60 * 1000; // 1m * 60s * 1000ms
		final long ONE_HOUR = 1 * 60 * 60 * 1000; // 1hr * 60m * 60s * 1000ms
		final long ONE_DAY = 1 * 24 * 60 * 60 * 1000; // 1day * 24hrs * 60m * 60s * 1000ms
		//final long ONE_WEEK = 1 * 7 * 24 * 60 * 60 * 1000; // 1week * 7days * 24hrs * 60m * 60s * 1000ms
		
		/*SimpleDateFormat df = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		Date dtTime;
		try {
			dtTime = df.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return "error";
		}*/
		Date dtNow = new Date();
		
		long diffTotal = dtNow.getTime() - time.getTime();
		long diffMinutes = diffTotal / ( 1000 * 60 );
		long diffHours = diffTotal / ( 1000 * 60 * 60);
		long diffDays = diffTotal / ( 1000 * 60 * 60 * 24);
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"diffTotal="+diffTotal+",diffMinutes="+diffMinutes+",diffHours="+diffHours+",diffDays="+diffDays);}
		StringBuilder result = new StringBuilder();
		long temp;
		
		if(diffTotal <=  ONE_MINUTE){
			//return "just now";
			result.append("just now");
		}
		else if(diffTotal <=  ONE_HOUR){
			if(diffMinutes == 1) result.append("1 min");
			else result.append(diffMinutes + " mins");
			
			result.append(" ago");
		}
		else if(diffTotal <=  ONE_DAY){
			if(diffHours == 1) result.append("1 hr");
			else result.append(diffHours + " hrs");

			temp = (diffMinutes - (diffHours * 60));
			if(temp == 1) result.append(",1 min");
			else if(temp > 1) result.append("," + temp + " mins");
			result.append(" ago");
		}
		else {
			if(diffDays == 1) result.append("a day");
			else result.append(diffDays + " days");
				
			temp = diffHours - diffDays*24;
			if(temp == 1) result.append(",1 hr");
			else if(temp > 1) result.append("," + temp + " hrs");
			/*temp = diffMinutes - diffHours*60;
			if(temp > 0) result.append("," + temp + " mins");*/
			result.append(" ago");
		}
		
		return result.toString();
	}
	
	/*
	 * function to calculate duration between two times.
	 * if any of the parameters is null, it is set to the current time.
	 * The other parameter should be in local timezone, otherwise the reuslt will be wrong.
	 */
	public static String getDuration(String fromTime, String toTime){
		Date dt_fromTime = null;
		Date dt_toTime = null;

		/*initialize comparison times.
		 * if any one parameter is null, set it to the current time
		 */
		if(fromTime == null && toTime == null) return null;
		
		if(fromTime == null){ 
			dt_fromTime = new Date();
			try {
				dt_toTime = getSimpleDateFormat().parse(toTime);
			} catch (ParseException e) {
				e.printStackTrace();
				return "error";
			}
		}
		
		else if(toTime == null){
			dt_toTime = new Date();
			try {
				dt_fromTime = getSimpleDateFormat().parse(fromTime);
			} catch (ParseException e) {
				e.printStackTrace();
				return "error";
			}
		}
		
		else{
			try {
				dt_fromTime = getSimpleDateFormat().parse(fromTime);
				dt_toTime = getSimpleDateFormat().parse(toTime);
			} catch (ParseException e) {
				e.printStackTrace();
				return "error";
			}
		}
		
		final long ONE_MINUTE = 1 * 60 * 1000; // 1m * 60s * 1000ms
		final long ONE_HOUR = 1 * 60 * 60 * 1000; // 1hr * 60m * 60s * 1000ms
		final long ONE_DAY = 1 * 24 * 60 * 60 * 1000; // 1day * 24hrs * 60m * 60s * 1000ms
		//final long ONE_WEEK = 1 * 7 * 24 * 60 * 60 * 1000; // 1week * 7days * 24hrs * 60m * 60s * 1000ms
		
		long diffTotal = dt_toTime.getTime() - dt_fromTime.getTime();
		long diffSecs = diffTotal / 1000;
		long diffMinutes = diffTotal / ( 1000 * 60 );
		long diffHours = diffTotal / ( 1000 * 60 * 60);
		long diffDays = diffTotal / ( 1000 * 60 * 60 * 24);
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"diffTotal="+diffTotal+",diffMinutes="+diffMinutes+",diffHours="+diffHours+",diffDays="+diffDays);}
		StringBuilder result = new StringBuilder();
		long temp;
		
		if(diffTotal <=  ONE_MINUTE){
			//return "just now";
			result.append("less than a minute");
		}
		else if(diffTotal <=  ONE_HOUR){
			if(diffMinutes == 1) result.append("1 min");
			else result.append(diffMinutes + " mins");
			
			temp = (diffSecs - (diffMinutes * 60));
			if(temp == 1) result.append(",1 sec");
			else if(temp > 1) result.append("," + temp + " secs");
			
		}
		else if(diffTotal <=  ONE_DAY){
			if(diffHours == 1) result.append("1 hr");
			else result.append(diffHours + " hrs");

			temp = (diffMinutes - (diffHours * 60));
			if(temp == 1) result.append(",1 min");
			else if(temp > 1) result.append("," + temp + " mins");
		}
		else {
			if(diffDays == 1) result.append("a day");
			else result.append(diffDays + " days");
				
			temp = diffHours - diffDays*24;
			if(temp == 1) result.append(",1 hr");
			else if(temp > 1) result.append("," + temp + " hrs");
			
			temp = (diffMinutes - (diffHours * 60));
			if(temp == 1) result.append(",1 min");
			else if(temp > 1) result.append("," + temp + " mins");
		}
		
		return result.toString();
	}
	
	/*
	 * function to calculate duration between two UTC times.
	 * if any of the parameters is null, it is set to the current time.
	 */
	public static String getSessionDuration(Date dt_fromTime, Date dt_toTime){

		/*initialize comparison times.
		 * if any one parameter is null, set it to the current time
		 */
		if(dt_fromTime == null && dt_toTime == null) return null;
		
		if(dt_fromTime == null){ 
			dt_fromTime = new Date();
		}
		
		else if(dt_toTime == null){
			dt_toTime = new Date();
		}
		
		final long ONE_MINUTE = 1 * 60 * 1000; // 1m * 60s * 1000ms
		final long ONE_HOUR = 1 * 60 * 60 * 1000; // 1hr * 60m * 60s * 1000ms
		final long ONE_DAY = 1 * 24 * 60 * 60 * 1000; // 1day * 24hrs * 60m * 60s * 1000ms
		//final long ONE_WEEK = 1 * 7 * 24 * 60 * 60 * 1000; // 1week * 7days * 24hrs * 60m * 60s * 1000ms
		
		long diffTotal = dt_toTime.getTime() - dt_fromTime.getTime();
		long diffSecs = diffTotal / 1000;
		long diffMinutes = diffTotal / ( 1000 * 60 );
		long diffHours = diffTotal / ( 1000 * 60 * 60);
		long diffDays = diffTotal / ( 1000 * 60 * 60 * 24);
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"diffTotal="+diffTotal+",diffMinutes="+diffMinutes+",diffHours="+diffHours+",diffDays="+diffDays);}
		StringBuilder result = new StringBuilder();
		long temp;
		
		if(diffTotal <  ONE_MINUTE){
			if(diffSecs == 1) result.append("1 sec");
			else result.append(diffSecs + " secs");
		}
		else if(diffTotal <=  ONE_HOUR){
			if(diffMinutes == 1) result.append("1 min");
			else result.append(diffMinutes + " mins");
			
			temp = (diffSecs - (diffMinutes * 60));
			if(temp == 1) result.append(",1 sec");
			else if(temp > 1) result.append("," + temp + " secs");
			
		}
		else{
			if(diffHours == 1) result.append("1 hr");
			else result.append(diffHours + " hrs");

			temp = (diffMinutes - (diffHours * 60));
			if(temp == 1) result.append(",1 min");
			else if(temp > 1) result.append("," + temp + " mins");
		}
		/*else {
			if(diffDays == 1) result.append("a day");
			else result.append(diffDays + " days");
				
			temp = diffHours - diffDays*24;
			if(temp == 1) result.append(",1 hr");
			else if(temp > 1) result.append("," + temp + " hrs");
			
			temp = (diffMinutes - (diffHours * 60));
			if(temp == 1) result.append(",1 min");
			else if(temp > 1) result.append("," + temp + " mins");
		}*/
		
		return result.toString();
	}
	
	public static String getDayForDisplay(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM yyyy",Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date dt = null;
		try{dt = Utilities.getSimpleDateFormatUTC().parse(time);}
		catch(Exception e){e.printStackTrace();}
		return sdf.format(dt);
	}
	
	/*function to get the time part for display on screen
	 * For example "2014-09-25 13:28:05" will return 13:28
	 * 
	 * */
	public static String getTimeForDisplay(String time){
		String result = null;
		result = time.substring(11, 16);
		
		return result;
	}
	
	public static String getDurationFromNowIgnoringTime(Calendar cal){
		Calendar now = Calendar.getInstance();
		if(cal == null) return null;
		
		StringBuilder result = new StringBuilder();
		
		int yearDiff = cal.get(Calendar.YEAR) - now.get(Calendar.YEAR);
		int diffInDays=0;
		
		if(yearDiff == 0){
			diffInDays = cal.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
		}
		else{
			//create a calendar with the last day of this year
			Calendar endOfThisYear = Calendar.getInstance();
			endOfThisYear.set(Calendar.MONTH,Calendar.DECEMBER);
			endOfThisYear.set(Calendar.DAY_OF_MONTH,31);
			
			//get days remaining this year from now
			diffInDays = endOfThisYear.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
			
			//get the days from 1st of next year to the input cal, and add to total
			diffInDays = diffInDays + cal.get(Calendar.DAY_OF_YEAR); 
		}
		
		//construct the result string
		if(diffInDays == 0) result.append("today");
		else if(diffInDays == 1) result.append("tomorrow");
		else{
			int weeks = diffInDays / 7; //(int) Math.floor(diffInDays / 7);
			int days = diffInDays % 7;
			if(weeks == 0){
				if(days == 1) result.append("1 day");
				else result.append(days + " days");
			} 
			else{ 
				if(weeks == 1) result.append("1 week");
				else result.append(weeks + " weeks");
				
				if(days == 0) {}
				else if(days == 1) result.append(",1 day");
				else result.append("," + days + " days");
			}
			result.append(" to go");
		
		}
		
		return result.toString();
	}
	
	/*
	 * Method to return the duration of today since the provided UTC date, ignoring the time part.
	 * The default timezone is applied to the argument, so it will give different results
	 * when run in different timezones.
	 * */
	public static String getDurationTillToday(Date dt){
		if(dt == null) return null;
		
		Calendar now = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		
		//if the provided date is greater than now, return null 
		int cmp = cal.compareTo(now);
		if(cmp == 1) return null;
		
		StringBuilder result = new StringBuilder();
		
		int yearDiff = cal.get(Calendar.YEAR) - now.get(Calendar.YEAR);
		int diffInDays=0;
		
		if(yearDiff == 0){
			diffInDays =  now.get(Calendar.DAY_OF_YEAR)- cal.get(Calendar.DAY_OF_YEAR);
		}
		else{
			//create a calendar with the last day of the year
			Calendar endOfYear = (Calendar) cal.clone();
			endOfYear.set(Calendar.MONTH,Calendar.DECEMBER);
			endOfYear.set(Calendar.DAY_OF_MONTH,31);
			
			//get days remaining in cal's year
			diffInDays = endOfYear.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR);
			
			//get the days from 1st of cal's year to now, and add to total
			diffInDays = diffInDays + now.get(Calendar.DAY_OF_YEAR)
					+ (yearDiff - 1)*365;
		}
		
		//construct the result string
		if(diffInDays == 0) result.append("today");
		else if(diffInDays == 1) result.append("yesterday");
		else{
			int weeks = diffInDays / 7; //(int) Math.floor(diffInDays / 7);
			int days = diffInDays % 7;
			if(weeks == 0){
				result.append(days + " days");
			} 
			else{ 
				if(weeks == 1) result.append("1 week");
				else result.append(weeks + " weeks");
				
				if(days == 0) {}
				else if(days == 1) result.append(",1 day");
				else result.append("," + days + " days");
			}
			result.append(" ago");
		
		}
		
		return result.toString();
	}
	
	/*
	 * Method to return the difference between two UTC dates, ignoring the time part.
	 * The arguments can be in any order, it will be ordered automatically.
	 * The default timezone is applied to the arguments, so it will give different results
	 * when run in different timezones.
	 * */
	
	public static String getDifferenceIgnoringTime(Date dt1, Date dt2){
		//return immediately if any value is null or they are equal
		if(dt1 == null || dt2 == null) return null;
		if(dt1.equals(dt2)) return "0 days";
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(dt1);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(dt2);
		
		//find out the order of the input values
		Calendar calEarlier = null;
		Calendar calLater = null;
		if(cal2.after(cal1)){
			calEarlier = cal1;
			calLater = cal2;
		}
		else{
			calEarlier = cal2;
			calLater = cal1;
		}
		
		StringBuilder result = new StringBuilder();
		
		int yearDiff = calLater.get(Calendar.YEAR) - calEarlier.get(Calendar.YEAR);
		int diffInDays=0;
		
		if(yearDiff == 0){
			diffInDays = calLater.get(Calendar.DAY_OF_YEAR) - calEarlier.get(Calendar.DAY_OF_YEAR) + 1;
		}
		else{
			//create a calendar with the last day of the earlier year
			Calendar endOfEarlierYear = Calendar.getInstance();
			endOfEarlierYear.set(Calendar.MONTH,Calendar.DECEMBER);
			endOfEarlierYear.set(Calendar.DAY_OF_MONTH,31);
			endOfEarlierYear.set(Calendar.YEAR,calEarlier.get(Calendar.YEAR));
			
			//get days remaining in earlier year
			diffInDays = endOfEarlierYear.get(Calendar.DAY_OF_YEAR) - calEarlier.get(Calendar.DAY_OF_YEAR) + 1;
			
			//get the total difference in days
			diffInDays = diffInDays + calLater.get(Calendar.DAY_OF_YEAR) 
					+ (yearDiff - 1)*365; 
		}
		
		//construct the result string
		if(diffInDays == 1) result.append("1 day");
		else{
			int weeks = (diffInDays) / 7; //(int) Math.floor(diffInDays / 7);
			int days = (diffInDays) % 7;
			if(weeks == 0){
				if(days == 1) result.append("1 day");
				else result.append(days + " days");
			} 
			else{ 
				if(weeks == 1) result.append("1 week");
				else result.append(weeks + " weeks");
				
				if(days == 0) {}
				else if(days == 1) result.append(",1 day");
				else result.append("," + days + " days");
			}
		}
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"cal1="+getSimpleDateFormat().format(cal1.getTime())+",cal2="+getSimpleDateFormat().format(cal2.getTime())+",result="+result.toString());}
		return result.toString();
	}
	
	/*
	 * Method to return the difference in number of days between two UTC dates, ignoring the time part.
	 * The arguments can be in any order, it will be ordered automatically.
	 * The default timezone is applied to the arguments, so it will give different results
	 * when run in different timezones.
	 * */
	
	public static int getDayDifference(Date dt1, Date dt2){
		//return immediately if any value is null or they are equal
		if(dt1 == null || dt2 == null) throw new IllegalArgumentException();
		if(dt1.equals(dt2)) return 0;
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(dt1);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(dt2);
		
		//find out the order of the input values
		Calendar calEarlier = null;
		Calendar calLater = null;
		if(cal2.after(cal1)){
			calEarlier = cal1;
			calLater = cal2;
		}
		else{
			calEarlier = cal2;
			calLater = cal1;
		}
		
		int yearDiff = calLater.get(Calendar.YEAR) - calEarlier.get(Calendar.YEAR);
		int diffInDays=0;
		
		if(yearDiff == 0){
			diffInDays = calLater.get(Calendar.DAY_OF_YEAR) - calEarlier.get(Calendar.DAY_OF_YEAR);
		}
		else{
			//create a calendar with the last day of the earlier year
			Calendar endOfEarlierYear = Calendar.getInstance();
			endOfEarlierYear.set(Calendar.MONTH,Calendar.DECEMBER);
			endOfEarlierYear.set(Calendar.DAY_OF_MONTH,31);
			endOfEarlierYear.set(Calendar.YEAR,calEarlier.get(Calendar.YEAR));
			
			//get days remaining in earlier year
			diffInDays = endOfEarlierYear.get(Calendar.DAY_OF_YEAR) - calEarlier.get(Calendar.DAY_OF_YEAR);
			
			//get the total difference in days
			diffInDays = diffInDays + calLater.get(Calendar.DAY_OF_YEAR) 
					+ (yearDiff - 1)*365; 
		}
		
		return diffInDays;
	}

	public static boolean isDatesEqual(Calendar cal1, Calendar cal2){
		if(cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR))
				return false;
		if(cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH))
			return false;
		if(cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH))
			return false;
		return true;
	}
	
	public static boolean isDateMoreThan(Calendar cal1, Calendar cal2){
		if(cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR))
				return false;
		else if(cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR))
			return true;
		if(cal1.get(Calendar.MONTH) < cal2.get(Calendar.MONTH))
			return false;
		else if(cal1.get(Calendar.MONTH) > cal2.get(Calendar.MONTH))
			return true;
		if(cal1.get(Calendar.DAY_OF_MONTH) <= cal2.get(Calendar.DAY_OF_MONTH))
			return false;
		else return true;
	}
	
	public static boolean isDateMoreThanOrEqual(Calendar cal1, Calendar cal2){
		if(cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR))
				return false;
		else if(cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR))
			return true;
		if(cal1.get(Calendar.MONTH) < cal2.get(Calendar.MONTH))
			return false;
		else if(cal1.get(Calendar.MONTH) > cal2.get(Calendar.MONTH))
			return true;
		if(cal1.get(Calendar.DAY_OF_MONTH) < cal2.get(Calendar.DAY_OF_MONTH))
			return false;
		else return true; 
		
		
	}
	
	/*public static String getWeekNumber(String time){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"time="+time);}
		if(time == null) return "";
		
		final long ONE_MINUTE = 1 * 60 * 1000; // 1m * 60s * 1000ms
		final long ONE_HOUR = 1 * 60 * 60 * 1000; // 1hr * 60m * 60s * 1000ms
		final long ONE_DAY = 1 * 24 * 60 * 60 * 1000; // 1day * 24hrs * 60m * 60s * 1000ms
		//final long ONE_WEEK = 1 * 7 * 24 * 60 * 60 * 1000; // 1week * 7days * 24hrs * 60m * 60s * 1000ms
		
		SimpleDateFormat df = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		Date dtTime;
		try {
			dtTime = df.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return "error";
		}
		Date dtNow = new Date();
		
		long diffTotal = dtNow.getTime() - dtTime.getTime();
		long diffMinutes = diffTotal / ( 1000 * 60 );
		long diffHours = diffTotal / ( 1000 * 60 * 60);
		long diffDays = diffTotal / ( 1000 * 60 * 60 * 24);
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"diffTotal="+diffTotal+",diffMinutes="+diffMinutes+",diffHours="+diffHours+",diffDays="+diffDays);}
		if(diffTotal <=  ONE_MINUTE){
			return "Just Now";
		}
		else if(diffTotal <=  ONE_HOUR){
			if(diffMinutes == 1) return "a min ago";
			else return diffMinutes + " mins ago";
		}
		else if(diffTotal <=  ONE_DAY){
			if(diffHours == 1) return "an hour ago";
			return diffHours + " hours ago";
		}
		else {
			if(diffDays == 1) return "a day ago";
			else return diffDays + " days ago"; 
		}
	}*/

}
