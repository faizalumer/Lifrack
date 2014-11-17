package com.theAlternate.lifrack;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class Setting_DailyFrequency extends IntegerSettingLayout{

	private final static String LOG_TAG = "Setting_DailyFrequency";
	
	public Setting_DailyFrequency(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTitle("Daily frequency");
		setDialogTitle("Repeat ? times a day");
		setValue(null);
	}

	@Override
	String getStatus() {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getStatus()");}
		
		Integer value = getValue();
		String result = null;
		
		if(value == null) result = "Not selected yet";
		else if(value == 1) result = "Repeat once in a day";
		else if(value == 2) result = "Repeat twice in a day";
		else if(value == 3) result = "Repeat thrice in a day";
		else result = "Repeat " + value.toString() + " times in a day";
			
		return result;
	}
	
} 