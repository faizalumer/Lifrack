package com.theAlternate.lifrack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
//import android.app.TimePickerDialog;

public class Fragment_AddHabit extends Fragment{
	private static final String LOG_TAG = "Fragment_AddHabit";
	private static final String KEY_SCHEDULE_ENABLED = "com.theAlternate.lifrack.scheduleEnabled";
	private static final String KEY_WEEK_FREQUENCY = "com.theAlternate.lifrack.weekFrequency";
	private static final String KEY_DAILY_FREQUENCY = "com.theAlternate.lifrack.dailyFrequency";
	private static final String KEY_REMINDERS = "com.theAlternate.lifrack.reminders";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onCreate()");}
		setHasOptionsMenu(true);
		
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onSaveInstanceState()");}
		CheckedTextView chk_schedule = (CheckedTextView) getView().findViewById(R.id.chk_schedule);
		outState.putBoolean(KEY_SCHEDULE_ENABLED, chk_schedule.isChecked());
		
		//daily frequency
		Integer dailyFrequency = ((Setting_DailyFrequency) getView().findViewById(R.id.stg_dailyfrequency)).getValue();
		if(dailyFrequency != null) outState.putInt(KEY_DAILY_FREQUENCY, dailyFrequency);
		
		//week frequency
		Integer weekFrequency = ((Setting_WeekFrequency) getView().findViewById(R.id.stg_weekfrequency)).getValue();
		if(weekFrequency != null) outState.putInt(KEY_WEEK_FREQUENCY, weekFrequency);
		
		//Get the reminders
		ArrayList<Reminder> reminderList = getReminders();
		if(reminderList != null) outState.putParcelableArrayList(KEY_REMINDERS, reminderList);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_edithabit, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActivityCreated()");}
		
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
			
			
		}
		
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
				reminderContainer.removeView(reminderLayout);
				
			}
		});
		
		/*if a reminder parameter was not passed, open the dialog to allow user input,
		 * otherwise set the value automatically and add to the grid
		 * */
		if(reminder == null) reminderLayout.edit();
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
	
	private void saveData(){
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
		Date now = new Date();
		boolean isScheduleEnabled = ((CheckedTextView) getView().findViewById(R.id.chk_schedule)).isChecked();
		Schedule schedule = null;
		List<Reminder> reminderList = null;
		
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
		
		if(isScheduleEnabled){
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
			
			//schedule = new Schedule(dailyFrequency, monday, tuesday, wednesday, thursday, friday, saturday, sunday, weekFrequency);
		}
		
		//add to local DB
		boolean isSuccess = true;
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		db.beginTransaction();
		try{
			long habitId = new HabitDaoImpl().insert(new Habit(habitName,now));
			
			if(isScheduleEnabled){
				new ScheduleDaoImpl()
				.insert(new Schedule(habitId,dailyFrequency, monday, tuesday, wednesday, thursday, friday, saturday, sunday, weekFrequency,now));
			
				if(reminderList != null){
					ReminderDao reminderDao = new ReminderDaoImpl();
					Reminder newReminder,reminder;
					for(int i=0;i<reminderList.size();i++){
						reminder = reminderList.get(i);
						newReminder = new Reminder(habitId,reminder.getHour(), reminder.getHour());
						reminderDao.insert(newReminder);
					}
				}
				
				
			}
			
			db.setTransactionSuccessful();
		} catch(Exception e){
			isSuccess = false;
			e.printStackTrace();
		}finally {
			db.endTransaction();
		}
	
		if(isSuccess){
			getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
			Toast.makeText(getActivity(), "Tracker created", Toast.LENGTH_LONG).show();
			if(reminderList != null && reminderList.size() > 0) Reminder.setNextReminderAlarm();
			
			//exit screen
			getActivity().onBackPressed();
		}
		
	}
}
