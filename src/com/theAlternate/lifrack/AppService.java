package com.theAlternate.lifrack;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.theAlternate.lifrack.Dao.HabitDaoImpl;
import com.theAlternate.lifrack.Dao.HitDaoImpl;
import com.theAlternate.lifrack.Dao.IReminderDao;
import com.theAlternate.lifrack.Dao.ReminderDaoImpl;
import com.theAlternate.lifrack.Dao.ScheduleDaoImpl;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.LongSparseArray;
import android.util.Log;
import android.util.SparseLongArray;

public class AppService extends Service {
	
	private static final String LOG_TAG = "AppService";
	
	public static final String ACTION_SET_NEXT_REMINDER_ALARM = "ACTION_SET_NEXT_REMINDER_ALARM";
	public static final String ACTION_REMIND = "ACTION_REMIND";
	public static final String ACTION_REMINDED = "ACTION_REMINDED";
	public static final String KEY_REMINDER_ID = "REMINDER_ID";
	public static final int REQUEST_CODE_REMINDER = 1;
	
	@Override
	public void onCreate(){
		if(BuildConfig.DEBUG) {Log.d(LOG_TAG,"onCreate");}
	}
	
	private void setNextReminderAlarm(){
		if(BuildConfig.DEBUG) {Log.d(LOG_TAG,"START : setNextReminderAlarm()");}
		
		//get schedules and reminders
		SQLiteDatabase db = LocalDBHelper.getInstance().getReadableDatabase();
		List<Reminder> reminderList = new ReminderDaoImpl(db).getAllReminders();
		List<Schedule> scheduleList = new ScheduleDaoImpl(db).getAllActiveSchedules();
		
		//quit if there are no active schedules or reminders
		if(reminderList == null || scheduleList == null) return;
		
		Reminder reminder = null;
		Schedule schedule = null;
		Calendar nextScheduledDate = null;
		Calendar nextToNextScheduledDate = null;
		Calendar alarm = null;
		Calendar earliestAlarm = null;
		ArrayList<Long> reminderIds = new ArrayList<Long>();
		
		int reminderCount = reminderList.size();
		int scheduleCount = scheduleList.size();
		
		//get today and tomorrow
		Calendar now = Calendar.getInstance();
		Calendar tomorrow = (Calendar) now.clone(); 
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);

		/////////////////////
		for (int i = 0; i < scheduleCount; i++) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"i="+i);}
			
			//get the schedule
			schedule = scheduleList.get(i);
			
			//get the next schedule date
			try {
				nextScheduledDate = schedule.getNextScheduledDate(null);
				nextToNextScheduledDate = schedule.getNextScheduledDate(tomorrow);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			for (int j = 0; j < reminderCount; j++) {
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"j="+j);}
				
				//get the reminder
				reminder = reminderList.get(j);
				
				if(reminder.getHabitId() == schedule.getHabitId()){
					//merge the next schedule date and reminder time.
					//if today is the next schedule date and the reminder time has already passed,
					//get the next scheduled date after today.
					if (Utilities.isDatesEqual(nextScheduledDate, now) && reminder.hasPassedAsOfNow()) {
						alarm = reminder.getWithDate(nextToNextScheduledDate);
					}
					else alarm = reminder.getWithDate(nextScheduledDate); 
					
					//set as earliest and add ID to list if this is first iteration
					if(reminderIds.size() == 0){
						earliestAlarm = alarm;
						reminderIds.add(reminder.getId());
					}
					
					//if this alarm is the same as the earliest,
					//add ID to list.
					else if(alarm.equals(earliestAlarm)){
						reminderIds.add(reminder.getId());
					}
					//if this alarm is earlier than the earliest till now,
					//clear the list, add ID to the list and set this as the earliest.
					else if(alarm.before(earliestAlarm)){
						reminderIds.clear();
						earliestAlarm = alarm;
						reminderIds.add(reminder.getId());
					}
				
				}
			}//end of inner loop iteration
		}//end of outer loop iteration
		
		//set the alarm
		int count = reminderIds.size();
		if(count > 0){
			//convert arrayList to array
			long[] reminderIdsArr = new long[reminderIds.size()];
			for(int i=0;i<reminderIds.size();i++){
				reminderIdsArr[i] = reminderIds.get(i);
			}
			
			Intent intent1 = new Intent(getApplicationContext(),this.getClass());
			intent1.setAction(ACTION_REMIND);
			intent1.putExtra(KEY_REMINDER_ID, reminderIdsArr);
			PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), REQUEST_CODE_REMINDER, intent1
					, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC, earliestAlarm.getTimeInMillis(), pendingIntent);
		}
		
		if(BuildConfig.DEBUG) {Log.d(LOG_TAG,"END : setNextReminderAlarm()=" + Utilities.getSimpleDateFormat().format(earliestAlarm.getTime()));}
	}
	
	private class Async_setNextReminderAlarm extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			setNextReminderAlarm();
			return null;
		}
		
	}
	
	public void showNotification(long[] reminderIds){	
	
		SQLiteDatabase db = LocalDBHelper.getInstance().getReadableDatabase();
		IReminderDao reminderDao = new ReminderDaoImpl(db);
		for (int i = 0; i < reminderIds.length; i++) {
			Reminder reminder = reminderDao.get(reminderIds[i]);
			if (reminder != null) {
				int notificationId = (int) reminder.getId();
				String habitName = new HabitDaoImpl(db).get(reminder.getHabitId()).getName();
				Hit lastHit = new HitDaoImpl(db).getLastByHabitId(reminder.getHabitId());
				String contentText = null;
				if(lastHit != null) contentText = "Last hit " + lastHit.getDurationFromNow();
				
				Intent contentIntent = new Intent(this, MainActivity.class);
				contentIntent.setAction(ACTION_REMINDED);
				contentIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				contentIntent.putExtra(MainActivity.KEY_NOTIFICATION_ID, notificationId);
				PendingIntent contentPendingIntent = PendingIntent.getActivity(this, notificationId, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyApplication.getContext())
					.setContentTitle(habitName)
					.setContentText(contentText)
					.setSmallIcon(R.drawable.ic_stat_notify)
					.setContentIntent(contentPendingIntent);
	
				NotificationManager notificationManager = (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(notificationId, notificationBuilder.build());
				if (BuildConfig.DEBUG) {Log.d(LOG_TAG, "Issued notificationId="+ notificationId);
				}
			}
		}
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		if(BuildConfig.DEBUG) {Log.d(LOG_TAG,"onStartCommand");}
		
		String action = intent.getAction();
		if(action.equals(ACTION_SET_NEXT_REMINDER_ALARM)){
			if(BuildConfig.DEBUG) {Log.d(LOG_TAG,"START : ACTION_SET_NEXT_REMINDER_ALARM");}
			//setNextReminderAlarm();
			new Async_setNextReminderAlarm().execute();
			if(BuildConfig.DEBUG) {Log.d(LOG_TAG,"END : ACTION_SET_NEXT_REMINDER_ALARM");}
		}
		else if(action.equals(ACTION_REMIND)){
			if(BuildConfig.DEBUG) {Log.d(LOG_TAG,"START : ACTION_REMIND");}
			
			// show notification
			long[] reminderIds = intent.getExtras().getLongArray(KEY_REMINDER_ID);
			showNotification(reminderIds);

			//set next reminder alarm
			new Async_setNextReminderAlarm().execute();
			//setNextReminderAlarm();
			
			if(BuildConfig.DEBUG) {Log.d(LOG_TAG,"END : ACTION_REMIND");}
		}
		
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy(){
		if(BuildConfig.DEBUG) {Log.d(LOG_TAG,"onDestroy");}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}