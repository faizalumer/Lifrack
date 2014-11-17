package com.theAlternate.lifrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootUpReceiver extends BroadcastReceiver{

	private static final String LOG_TAG = "BootUpReceiver";
	
	@Override
	public void onReceive(Context context, Intent arg1) {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onReceive");}
		Intent intent = new Intent(context,AppService.class);
		intent.setAction(AppService.ACTION_SET_NEXT_REMINDER_ALARM);
		context.startService(intent);
	}
	
}