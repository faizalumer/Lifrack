package com.theAlternate.lifrack;

import java.util.Calendar;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ReminderLayout extends LinearLayout {

	private static final String LOG_TAG = "ReminderLayout";
	private TextView tv_reminder;
	private Button btn_cancelReminder;
	private Reminder mReminder;
	private Context context;
	
	public OnActionListener onActionListener;

	public ReminderLayout(Context context) {

		//super(context, attrs);
		super(context);

		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "started constructor");
		}
		
		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) setBackgroundResource(R.drawable.selector_backgroundcolor);
		else setBackground(getResources().getDrawable(R.drawable.selector_backgroundcolor)); 
		
		// inflate the layout file
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_reminder, this, true);
		
		tv_reminder = (TextView) view.findViewById(R.id.txt_reminder);
		btn_cancelReminder = (Button) view.findViewById(R.id.btn_cancelreminder);
		
		this.context = context;

		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "end constructor");
		}
	
		//onclicklisteners
		tv_reminder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (BuildConfig.DEBUG) {Log.d(LOG_TAG, "onClick : edit");}
				edit();
			}
		});
		
		//onclicklisteners
		btn_cancelReminder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (BuildConfig.DEBUG) {Log.d(LOG_TAG, "onClick : cancel");}
				onActionListener.onCancel();
			}
		});
	
	}

	public interface OnActionListener {
		public void onTimeSet();
		public void onCancel();
	}

	public void setOnActionListener(OnActionListener onActionListener) {
		this.onActionListener = onActionListener;
	}

	//called whenever the time needs to be edited
	public void edit() {
		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "edit()");
		}
		
		// initialize value to show in the dialog
		int hour;
		int minute;
		if(mReminder == null){
			Calendar currentTime = Calendar.getInstance();
			hour = currentTime.get(Calendar.HOUR_OF_DAY);
			minute = currentTime.get(Calendar.MINUTE);
		}
		else{
			hour = mReminder.getHour();
			minute = mReminder.getMinute();
		}

		// initialize the dialog
		MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(context,
				new MyTimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker timePicker,
							int selectedHour, int selectedMinute) {
						if(mReminder == null) setReminder(new Reminder(selectedHour, selectedMinute));
						else {
							mReminder.setTime(selectedHour, selectedMinute);
							tv_reminder.setText(mReminder.getFormattedTime());
						}//updateTime(selectedHour, selectedMinute);
						
						onActionListener.onTimeSet();
					}
				}, hour, minute, true);
		
		//set the appropriate title
		if(mReminder==null) timePickerDialog.setTitle("Set Reminder");
		else timePickerDialog.setTitle("Edit Reminder");
		
		timePickerDialog.show();

	}

	public void setReminder(Reminder reminder) {
		this.mReminder = reminder;
		tv_reminder.setText(reminder.getFormattedTime());
	}
	
	public Reminder getReminder() {
		return mReminder;
	}

}
