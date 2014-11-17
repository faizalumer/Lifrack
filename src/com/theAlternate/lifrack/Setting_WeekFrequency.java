package com.theAlternate.lifrack;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class Setting_WeekFrequency extends IntegerSettingLayout{
	private final static String LOG_TAG = "Setting_WeekFrequency";

	public Setting_WeekFrequency(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTitle("Week frequency");
		setDialogTitle("Repeat every ? weeks");
		setValue(null);
		
	}

	@Override
	String getStatus() {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getStatus()");}
		Integer value = getValue();
		String result = null;
		
		if(value == null) result = "Not selected yet";
		else if(value == 1) result = "Repeat every week";
		else result = "Repeat every " + value.toString() + " weeks";
			
		return result;
	}
	
} 