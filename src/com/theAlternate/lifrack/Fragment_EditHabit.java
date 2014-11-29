package com.theAlternate.lifrack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.theAlternate.lifrack.Dao.HabitDaoImpl;
import com.theAlternate.lifrack.Dao.IReminderDao;
import com.theAlternate.lifrack.Dao.ReminderDaoImpl;
import com.theAlternate.lifrack.Dao.IScheduleDao;
import com.theAlternate.lifrack.Dao.ScheduleDaoImpl;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Fragment_EditHabit extends Fragment{
	private static final String LOG_TAG = "Fragment_EditHabit";
	private static final String KEY_SCHEDULE_ENABLED = "com.theAlternate.lifrack.scheduleEnabled";
	private static final String KEY_WEEK_FREQUENCY = "com.theAlternate.lifrack.weekFrequency";
	private static final String KEY_DAILY_FREQUENCY = "com.theAlternate.lifrack.dailyFrequency";
	private static final String KEY_REMINDERS = "com.theAlternate.lifrack.reminders";
	private static final String KEY_DELETED_REMINDERS = "com.theAlternate.lifrack.deletedReminders";
	
	ArrayList<Long> deletedReminderList = new ArrayList<Long>();
	long habitId;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onCreate()");}
		setHasOptionsMenu(true);
		
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onSaveInstanceState()");}
		//schedule enabled
		CheckedTextView chk_schedule = (CheckedTextView) getView().findViewById(R.id.chk_schedule);
		outState.putBoolean(KEY_SCHEDULE_ENABLED, chk_schedule.isChecked());
		
		//daily frequency
		Integer dailyFrequency = ((Setting_DailyFrequency) getView().findViewById(R.id.stg_dailyfrequency)).getValue();
		if(dailyFrequency != null) outState.putInt(KEY_DAILY_FREQUENCY, dailyFrequency);
		
		//week frequency
		Integer weekFrequency = ((Setting_WeekFrequency) getView().findViewById(R.id.stg_weekfrequency)).getValue();
		if(weekFrequency != null) outState.putInt(KEY_WEEK_FREQUENCY, weekFrequency);
		
		//reminders
		ArrayList<Reminder> reminderList = getReminders();
		if(reminderList != null) outState.putParcelableArrayList(KEY_REMINDERS, reminderList);
		
		//deleted reminders
		long[] deletedReminderArr = new long[deletedReminderList.size()];
		for(int i=0;i<deletedReminderList.size();i++){
			deletedReminderArr[i] = deletedReminderList.get(i);
		}
		outState.putLongArray(KEY_DELETED_REMINDERS, deletedReminderArr);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_edithabit, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActivityCreated()");}
		
		//get the hait id from the intent
		habitId = getActivity().getIntent().getExtras().getLong(Activity_EditHabit.KEY_HABITID);
		
		// set up the save button click listener
		Button btn_save = (Button) getView().findViewById(R.id.btn_save_habit);
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				saveData();

			}

		});

		// set up the Add-Reminder button click listener
		ImageButton btn_addReminder = (ImageButton) getView().findViewById(R.id.btn_addremainder);
		btn_addReminder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (BuildConfig.DEBUG) {
					Log.d(LOG_TAG, "add remainder button clicked");
				}
				createNewReminder(null);
			}

		});
		
		//set up schedule checkbox
		CheckedTextView chk_schedule = (CheckedTextView) getView().findViewById(R.id.chk_schedule);
		 
		
		chk_schedule.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				((CheckedTextView)view).toggle();
				if(((CheckedTextView)view).isChecked()){
					if(BuildConfig.DEBUG){Log.d(LOG_TAG,"checked");}
					setEnabledAll(getView().findViewById(R.id.container_schedule),true);
					//getView().findViewById(R.id.container_schedule).setEnabled(true);
				}
				else{
					if(BuildConfig.DEBUG){Log.d(LOG_TAG,"unchecked");}
					setEnabledAll(getView().findViewById(R.id.container_schedule),false);
				}
				
				
			}
		});
		
		//disable by default
		setEnabledAll(getView().findViewById(R.id.container_schedule),false);
		
		//restore state after orientation change
		if(savedInstanceState != null){
			chk_schedule.setChecked(savedInstanceState.getBoolean(KEY_SCHEDULE_ENABLED));
			if(chk_schedule.isChecked()) setEnabledAll(getView().findViewById(R.id.container_schedule),true);
			
			int temp = savedInstanceState.getInt(KEY_DAILY_FREQUENCY); //returns 0 if it does not exist
			if(temp != 0)((Setting_DailyFrequency) getView().findViewById(R.id.stg_dailyfrequency)).setValue(temp);
			
			temp = savedInstanceState.getInt(KEY_WEEK_FREQUENCY); //returns 0 if it does not exist
			if(temp != 0) ((Setting_WeekFrequency) getView().findViewById(R.id.stg_weekfrequency)).setValue(temp);
			
			ArrayList<Reminder> reminderList = savedInstanceState.getParcelableArrayList(KEY_REMINDERS);
			if(reminderList != null && reminderList.size() > 0){
				for(int i=0;i<reminderList.size();i++){
					createNewReminder(reminderList.get(i));
				}
				
			}
			
			long[] deletedReminderArr = savedInstanceState.getLongArray(KEY_DELETED_REMINDERS);
			for(int i=0; i<deletedReminderArr.length; i++) deletedReminderList.add(deletedReminderArr[i]);
			//deletedReminderList = savedInstanceState.getParcelableArrayList(KEY_DELETED_REMINDERS);
		}
		else LoadDataFromDB();
		
	}
	
	private ArrayList<Reminder> getReminders(){
		ArrayList<Reminder> reminderList = new ArrayList<Reminder>();
		GridLayout gridLayout = (GridLayout) getView().findViewById(R.id.grdl_reminders);
		int count = gridLayout.getChildCount();
		Reminder reminder;
		for(int i=0;i<count;i++){
			reminder = ((ReminderLayout) gridLayout.getChildAt(i)).getReminder();
			reminderList.add(reminder);
		}
		if(count == 0) return null;
		else return reminderList;
	} 
	
	private void createNewReminder(Reminder reminder){
		final ReminderLayout reminderLayout = new ReminderLayout(getActivity());
		final GridLayout reminderContainer = (GridLayout) getView().findViewById(R.id.grdl_reminders);
		reminderLayout.setOnActionListener(new ReminderLayout.OnActionListener() {
			
			@Override
			public void onTimeSet() {
				//add the view to the grid only if it is a newly created one
				if(reminderLayout.getParent() == null)
				reminderContainer.addView(reminderLayout);
				
				//set attributes
				MarginLayoutParams layoutParams = (MarginLayoutParams) reminderLayout.getLayoutParams();
				layoutParams.setMargins(5, 5, 5, 5);
				reminderLayout.setLayoutParams(layoutParams);
				reminderLayout.requestLayout();
				
			}
			
			@Override
			public void onCancel() {
				//add to deleted list if it exists in DB
				Reminder reminder = reminderLayout.getReminder();
				if(reminder.hasValidId()) deletedReminderList.add(reminder.getId());
				
				//remove view from grid
				reminderContainer.removeView(reminderLayout);
				
			}
		});
		
		/*if a reminder parameter was not passed, open the dialog to allow user input,
		 * otherwise set the value automatically and add to the grid
		 * */
		if(reminder == null){ 
			//reminderLayout.setReminder(new Reminder(habitId));
			reminderLayout.edit();
		
		}
		else{
			reminderLayout.setReminder(reminder);
			reminderContainer.addView(reminderLayout);
			
			//set attributes
			MarginLayoutParams layoutParams = (MarginLayoutParams) reminderLayout.getLayoutParams();
			layoutParams.setMargins(5, 5, 5, 5);
			reminderLayout.setLayoutParams(layoutParams);
			reminderLayout.requestLayout();
		}
		
		
	}
	
	private void setEnabledAll(View view, boolean enabled){
		view.setEnabled(enabled);
		//view.setFocusable(enabled);
		if(view instanceof ViewGroup){
			ViewGroup viewGroup = (ViewGroup) view;
			for(int i=0; i<viewGroup.getChildCount(); i++){
				View child = viewGroup.getChildAt(i);
				if(child instanceof ViewGroup){
					setEnabledAll(child,enabled);
				}
				else{
					child.setEnabled(enabled);
				}
			}
		}
	}
	
	private void saveData(){
		//long habitId = getActivity().getIntent().getExtras().getLong(Activity_EditHabit.KEY_HABITID);
		
		//get the entered values
		String habitName = ((EditText) getView().findViewById(R.id.edt_habitname)).getText().toString();
		
		//Alert if habit name is blank
		if(habitName.isEmpty() || habitName == ""){
			/*ScrollView scrollView = (ScrollView) getView().findViewById(R.id.scrvw_addhabit);
			scrollView.smoothScrollTo(0, scrollView.getTop());*/
			Toast.makeText(getActivity(), "Tracker name is blank. Please enter one.", Toast.LENGTH_LONG).show();
			return;
		}
		
		//get the schedule
		boolean isScheduleEnabled = ((CheckedTextView) getView().findViewById(R.id.chk_schedule)).isChecked();
		Schedule schedule = null;
		ArrayList<Reminder> reminderList = null;
		if(isScheduleEnabled){
			Integer dailyFrequency = ((Setting_DailyFrequency) getView().findViewById(R.id.stg_dailyfrequency)).getValue();
			Integer weekFrequency = ((Setting_WeekFrequency) getView().findViewById(R.id.stg_weekfrequency)).getValue();
			
			boolean monday = ((ToggleButton) getView().findViewById(R.id.tgbtn_monday)).isChecked();
			boolean tuesday = ((ToggleButton) getView().findViewById(R.id.tgbtn_tuesday)).isChecked();
			boolean wednesday = ((ToggleButton) getView().findViewById(R.id.tgbtn_wednesday)).isChecked();
			boolean thursday = ((ToggleButton) getView().findViewById(R.id.tgbtn_thursday)).isChecked();
			boolean friday = ((ToggleButton) getView().findViewById(R.id.tgbtn_friday)).isChecked();
			boolean saturday = ((ToggleButton) getView().findViewById(R.id.tgbtn_saturday)).isChecked();
			boolean sunday = ((ToggleButton) getView().findViewById(R.id.tgbtn_sunday)).isChecked();
			
			//Get the reminders
			reminderList = getReminders();
			
			//Alert if no weekdays are selected
			if(!(monday || tuesday || wednesday || thursday || friday || saturday ||sunday)){
				Toast.makeText(getActivity(), "No days have been selected. Please select atleast one.", Toast.LENGTH_LONG).show();
				return;
			}
			
			//Alert if week frequency not set
			if(weekFrequency == null){
				Toast.makeText(getActivity(), "Week frequency has not been set", Toast.LENGTH_LONG).show();
				return;
			}
			
			//Alert if daily frequency not set
			if(dailyFrequency == null){
				Toast.makeText(getActivity(), "Daily frequency has not been set", Toast.LENGTH_LONG).show();
				return;
			}
			
			//alert if there are duplicate reminders
			if(Utilities.hasDuplicates(reminderList)){
				Toast.makeText(getActivity(), "Remove duplicate reminders", Toast.LENGTH_LONG).show();
				return;
			}
			
			schedule = new Schedule(habitId,dailyFrequency, monday, tuesday, wednesday, thursday, friday, saturday, sunday, weekFrequency, new Date());
		}
		
		//get original data from DB
		SQLiteDatabase db = LocalDBHelper.getInstance().getReadableDatabase();
		Schedule originalSchedule = new ScheduleDaoImpl(db).getLatestScheduleByHabitId(habitId);
		Habit originalHabit = new HabitDaoImpl(db).get(habitId);
		List<Reminder> originalReminderList = new ReminderDaoImpl(db).getAllRemindersByHabitId(habitId);
		
		//start transaction
		boolean isChanged = false;
		boolean isSuccess = true;
		boolean reminderServiceNeedsUpdate = false;
		
		db = LocalDBHelper.getInstance().getWritableDatabase();
		db.beginTransaction();
		try{
			//update name
			if(!originalHabit.getName().equals(habitName)){
				originalHabit.setName(habitName);
				new HabitDaoImpl(db).update(originalHabit);
				isChanged = true;
			}
			
			IScheduleDao scheduleDao = new ScheduleDaoImpl(db);
			//invalidate the schedule if it has been disabled
			if((originalSchedule == null || !originalSchedule.isInvalidated()) && !isScheduleEnabled){
				originalSchedule.invalidate();
				scheduleDao.update(originalSchedule);
				isChanged = true;
				reminderServiceNeedsUpdate = true;
			}
			
			//update the schedule and reminders only if the schedule is enabled
			//otherwise just ignore all changes
			else if(isScheduleEnabled){
				IReminderDao reminderDao = new ReminderDaoImpl(db);
				
				//delete reminders
				for(Long reminderId : this.deletedReminderList){
					reminderDao.delete(reminderId);
					isChanged = true;
					reminderServiceNeedsUpdate = true;
				}
				
				//insert/update reminders
				if(reminderList != null){
					for(Reminder reminder : reminderList){
						//insert
						if(!reminder.hasValidId()){
							reminderDao.insert(new Reminder(habitId,reminder.getHour(),reminder.getMinute()));
							isChanged = true;
							reminderServiceNeedsUpdate = true;
						}
						//update
						else{
							int count = originalReminderList.size();
							Reminder originalReminder;
							for(int i=0;i<count;i++){
								originalReminder = originalReminderList.get(i);
								if(originalReminder.getId() == reminder.getId())
								{
									//update only if the data has changed
									if(!originalReminder.equals(reminder)){
										reminderDao.update(reminder);
										isChanged = true;
										reminderServiceNeedsUpdate = true;
									}
									break;
								}
							}	
						}
						
					}
				}
				
				//simply insert new schedule if there was no earlier schedule
				//or if the earlier one was already disabled
				if(originalSchedule == null || originalSchedule.isInvalidated()){
					scheduleDao.insert(schedule);
					isChanged = true;
					reminderServiceNeedsUpdate = true;
				}
				//otherwise invalidate earlier schedule and insert new one
				else if(!originalSchedule.equals(schedule)){
					originalSchedule.invalidate();
					scheduleDao.update(originalSchedule);
					scheduleDao.insert(schedule);
					isChanged = true;
					reminderServiceNeedsUpdate = true;
				}
				
			}
			
			db.setTransactionSuccessful();
		}catch(Exception e){
			e.printStackTrace();
			isSuccess = false;
		}
		finally{
			db.endTransaction();
		}
		
		if(isChanged && isSuccess){
			Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
			getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
			if(reminderServiceNeedsUpdate) Reminder.setNextReminderAlarm();
			
			//exit screen
			getActivity().onBackPressed();
		}
		else if(isChanged){
			Toast.makeText(getActivity(), "Update failed", Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(getActivity(), "There are no changes to save", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void LoadDataFromDB(){
		//get the content from the local DB
		//int habitId = (int) getActivity().getIntent().getExtras().getLong(Activity_EditHabit.KEY_HABITID);
		
		//////////////////
		//HabitEditableContent originalContent = new Habit(habitId).getEditableContent();
		SQLiteDatabase db = LocalDBHelper.getInstance().getReadableDatabase();	
		Schedule schedule = new ScheduleDaoImpl(db).getLatestScheduleByHabitId(habitId);
			Habit habit = new HabitDaoImpl(db).get(habitId);
			List<Reminder> reminderList = new ReminderDaoImpl(db).getAllRemindersByHabitId(habitId);
			//String habitName = getName();dfdfb
			//Reminder[] reminderArr = getReminders();
			
			//HabitEditableContent result = new HabitEditableContent(habitName,targetDescription,habitSchedule,reminders);
			//HabitEditableContent result = new HabitEditableContent(habitName,schedule,reminderArr);
			
			//return result;
			
		//}
		/////////////////////
		
		/*set the existing values into the views*/
		//set the name
		EditText edt_habitname = (EditText) getView().findViewById(R.id.edt_habitname);
		edt_habitname.setText(habit.getName());

		//set the schedule and reminders
		if(schedule != null){
			((Setting_WeekFrequency) getView().findViewById(R.id.stg_weekfrequency)).setValue(schedule.getWeekFrequency());
			((Setting_DailyFrequency) getView().findViewById(R.id.stg_dailyfrequency)).setValue(schedule.getDailyFrequency());
			
			ToggleButton tgbtn_monday = (ToggleButton) getView().findViewById(R.id.tgbtn_monday);
			tgbtn_monday.setChecked(schedule.getMonday());
			
			ToggleButton tgbtn_tuesday = (ToggleButton) getView().findViewById(R.id.tgbtn_tuesday);
			tgbtn_tuesday.setChecked(schedule.getTuesday());
			
			ToggleButton tgbtn_wednesday = (ToggleButton) getView().findViewById(R.id.tgbtn_wednesday);
			tgbtn_wednesday.setChecked(schedule.getWednesday());
			
			ToggleButton tgbtn_thursday = (ToggleButton) getView().findViewById(R.id.tgbtn_thursday);
			tgbtn_thursday.setChecked(schedule.getThursday());
			
			ToggleButton tgbtn_friday = (ToggleButton) getView().findViewById(R.id.tgbtn_friday);
			tgbtn_friday.setChecked(schedule.getFriday());
			
			ToggleButton tgbtn_saturday = (ToggleButton) getView().findViewById(R.id.tgbtn_saturday);
			tgbtn_saturday.setChecked(schedule.getSaturday());
			
			ToggleButton tgbtn_sunday = (ToggleButton) getView().findViewById(R.id.tgbtn_sunday);
			tgbtn_sunday.setChecked(schedule.getSunday());
		}
		
		//create reminder layouts
		if(reminderList != null){
			int count = reminderList.size();
			for(int i=0;i<count;i++){
				createNewReminder(reminderList.get(i));
			}
		}
		
		//set the scheudle status
		CheckedTextView chk_schedule = ((CheckedTextView)getView().findViewById(R.id.chk_schedule));
		ViewGroup container_schedule = ((ViewGroup)getView().findViewById(R.id.container_schedule));
		if(schedule != null && !schedule.isInvalidated()){
			chk_schedule.setChecked(true);
			setEnabledAll(container_schedule,true);
		}
		else{
			chk_schedule.setChecked(false);
			setEnabledAll(container_schedule,false);
		}
		
		
	}
}