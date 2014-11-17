package com.theAlternate.lifrack;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

public class MyTimePickerDialog extends TimePickerDialog{

	public MyTimePickerDialog(Context context,
			OnTimeSetListener onTimeSetListener, int hour, int minute, boolean b) {
		super(context,onTimeSetListener,hour,minute,b);
	}

	@Override
	public void onStop(){
		
	}
	
}