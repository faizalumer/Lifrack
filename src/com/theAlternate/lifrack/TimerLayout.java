package com.theAlternate.lifrack;

import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.Space;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TimerLayout extends LinearLayout {

	private static final String LOG_TAG = "TimerLayout";
	//Date startTime;
	ImageButton btn_endTimer;
	ImageButton btn_cancelTimer;
	Chronometer cmt_timer;
	OnTimerActionListener onTimerActionListener;
	Runnable updateTimerThread;
	Handler handler;
	Space space;

	public void noOp(){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"noOp");}
	}
	
	public TimerLayout(Context context, AttributeSet attrs) {
		super(context,attrs);
		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "started constructor");
		}
		
		setOrientation(LinearLayout.VERTICAL);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.timer, this, true);
		cmt_timer = (Chronometer) getChildAt(0);
		btn_endTimer = (ImageButton) ((ViewGroup) getChildAt(1)).getChildAt(0);
		btn_cancelTimer = (ImageButton) ((ViewGroup) getChildAt(1)).getChildAt(2);
		space = (Space) ((ViewGroup) getChildAt(1)).getChildAt(1);
		
		//The space widget visibility is set to INVISIBLE by the framework
		//this causes it to not receive input events.
		//So making it visible to consume the onTouch event.
		space.setVisibility(View.VISIBLE);
		space.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent){
				return true;
			}
		});
		
		btn_endTimer.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				if (BuildConfig.DEBUG) {
					Log.d(LOG_TAG, "onEnd");}
				cmt_timer.stop();
				//stop();
				onTimerActionListener.onEnd();
			}
		});
		
		btn_cancelTimer.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View view){
				if (BuildConfig.DEBUG) {
					Log.d(LOG_TAG, "onCancel");}
				cmt_timer.stop();
				//stop();
				onTimerActionListener.onCancel();
			}
		});
		
		cmt_timer.setOnChronometerTickListener(new OnChronometerTickListener() {
			
			@Override
			public void onChronometerTick(Chronometer arg0) {
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onChronometerTick.objectid=" + System.identityHashCode(TimerLayout.this));}
				
			}
		});
		
		/*updateTimerThread = new Runnable(){

			@Override
			public void run() {
				
				//calculate total time
				long timeInMilliSeconds = (new Date().getTime()) - startTime.getTime();
				int secs = (int) (timeInMilliSeconds / 1000) % 60 ;
				Log.d(LOG_TAG,"updateTimerThread.startTime.secs="+String.valueOf(secs));
				int mins = (int) ((timeInMilliSeconds / (1000*60)) % 60);
				int hours   = (int) ((timeInMilliSeconds / (1000*60*60)) % 24);
				//hours = hours % 24;
				cmt_timer.setText(String.format("%02d", hours)
						+ ":" + String.format("%02d", mins)
						+ ":" + String.format("%02d", secs)
						);
				
				handler.postDelayed(this, 1000);
			}
			
		};*/
		
		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "end constructor");
		}
	}

/*	public void init(Date startTime) {

		stop();
		if (startTime != null) {
			this.startTime = startTime;
			handler.postDelayed(updateTimerThread, 0);
		}

	}*/
	
	public Chronometer init(Date startTime){
		Chronometer obj = null;
		cmt_timer.stop();
		if(startTime!=null){
			Date now = new Date();
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"init.diff=" + ((now.getTime() - startTime.getTime())/1000L) + "s");}
			//this.startTime = startTime;
			long elapsedTime = now.getTime() - startTime.getTime();
			cmt_timer.setBase(SystemClock.elapsedRealtime() - elapsedTime);
			cmt_timer.start();
			obj = cmt_timer;
		}
		
		return obj;
		
	}
	
	/*@Override
	public void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		Log.d(LOG_TAG,"onDetachedFromWindow.id=" + System.identityHashCode(TimerLayout.this));
	}*/
	
	
	
	/*public void setStartTime(Date startTime){
		this.startTime = startTime;
	}*/
	
	/*public void updateTimer(){
		//don't do anything if startTime is not set
		if(startTime == null) return;
		
		//calculate timer value
		long timeInMilliSeconds = (new Date().getTime()) - startTime.getTime();
		int secs = (int) (timeInMilliSeconds / 1000) % 60 ;
		Log.d(LOG_TAG,"updateTimerThread.startTime.secs="+String.valueOf(secs));
		int mins = (int) ((timeInMilliSeconds / (1000*60)) % 60);
		int hours   = (int) ((timeInMilliSeconds / (1000*60*60)) % 24);
		
		//set the value into the view
		cmt_timer.setText(String.format("%02d", hours)
				+ ":" + String.format("%02d", mins)
				+ ":" + String.format("%02d", secs)
				);
	}*/
	
	/*private void stop(){
		handler.removeCallbacks(updateTimerThread);
	}*/
	
	
	
	public interface OnTimerActionListener {
		public void onCancel();
		public void onEnd();
	}
	
	public void setOnTimerActionListener(OnTimerActionListener onTimerActionListener){
		this.onTimerActionListener = onTimerActionListener;
	}

}
