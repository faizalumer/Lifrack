package com.theAlternate.lifrack;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.theAlternate.lifrack.Dao.HabitOperationsManagerImpl;
import com.theAlternate.lifrack.Dao.HitDaoImpl;
import com.theAlternate.lifrack.Dao.HitSessionDaoImpl;
import com.theAlternate.lifrack.Dao.TargetDaoImpl;
import com.theAlternate.lifrack.LocalDB.View_HabitsCompleteInfo;
import com.theAlternate.lifrack.TimerLayout.OnTimerActionListener;

public class Fragment_HabitList extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private ListView mListView;
	private HabitListAdapter adpt_habits;
	private static final int HABITLIST_LOADER_ID = 0;
	private static final String LOG_TAG = "Fragment_HabitList";
	private ActionMode mActionMode;
	ChronometerHolder chronometerHolder = new ChronometerHolder(); 
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_habitlist, container,false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.fragment_habitlist, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onOptionsItemSelected");}
		
		switch(item.getItemId()){
		case R.id.action_Add:
			//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"Action_Add seleted");}
			Intent intent = new Intent(getActivity(),Activity_AddHabit.class);
			startActivity(intent);
			break;
			
			default:
				break;
			
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		getView().findViewById(android.R.id.empty).setVisibility(View.INVISIBLE);
		getListView().setEmptyView(null);
		
		//set the adaptor for list view
		adpt_habits = new HabitListAdapter(getActivity());
		mListView = getListView();
		this.setListAdapter(adpt_habits);
		
		//start CAB on long click
		mListView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onLongClick");}
				if(mActionMode != null){
					uncheckAll();
					mActionMode.finish();	
				}
				mListView.setItemChecked(pos, true);
				mActionMode = ((ActionBarActivity) getActivity()).startSupportActionMode(new ContextualActionBarCallBack_Habit());
				return true;
			}
			
		});
		
		//open detail tabs on click
		mListView.setOnItemClickListener(new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> adapterView, View arg1, int pos, long id){
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onItemClick");}
				
				Intent intent = new Intent(getActivity(),Activity_HabitDetails.class);
				intent.putExtra(Activity_HabitDetails.KEY_HABITID, id);
				intent.putExtra(Activity_HabitDetails.KEY_HABITNAME
						, ((Cursor)adapterView.getAdapter().getItem(pos)).getString(Projection.INDEX_COLUMN_NAME));
				startActivity(intent);
				
			}
			
		});
		
	}
	
	private void uncheckAll() {
		SparseBooleanArray selectedItems = mListView.getCheckedItemPositions();
		for (int i = 0; i < selectedItems.size(); i++) {
			mListView.setItemChecked(selectedItems.keyAt(i), false);
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onResume()");}
		
		//initialize the loader
		getLoaderManager().initLoader(HABITLIST_LOADER_ID, null, this);
		
		//when returning back to this fragment, the selected item remains highlighted
		//so remove selection
		if(mActionMode == null)
		uncheckAll();
		
		//start any chronometers that were paused
		chronometerHolder.startAll();
		
		//test
		/*if(BuildConfig.DEBUG){
			Log.d("TEST","getCurrentTimestampFromSqlite - " + com.theAlternate.lifrack.test.test.getCurrentTimestampFromSqlite(getActivity()));
			Log.d("TEST","getNowFromSqlite - " + com.theAlternate.lifrack.test.test.getNowFromSqlite(getActivity()));
			Log.d("TEST","getDayFromCurrentTimestampSqlite - " + com.theAlternate.lifrack.test.test.getDayFromCurrentTimestampSqlite(getActivity()));
			Log.d("TEST","getDayFromNowSqlite - " + com.theAlternate.lifrack.test.test.getDayFromNowSqlite(getActivity()));
			Log.d("TEST","getLocalTimeFromNowSqlite - " + com.theAlternate.lifrack.test.test.getLocalTimeFromNowSqlite(getActivity()));
			Log.d("TEST","getLocalTimeFromCurrentTimeStampSqlite - " + com.theAlternate.lifrack.test.test.getLocalTimeFromCurrentTimeStampSqlite(getActivity()));
			Log.d("TEST","getDayFromLocalTimeNowSqlite - " + com.theAlternate.lifrack.test.test.getDayFromLocalTimeNowSqlite(getActivity()));
			Log.d("TEST","getHourFromLocalTimeNowSqlite - " + com.theAlternate.lifrack.test.test.getHourFromLocalTimeNowSqlite(getActivity()));
			Log.d("TEST","java date() - " + Utilities.getCurrentTimeFormatted());
			Log.d("TEST","java date() - " + Utilities.getCurrentTimeFormattedUTC());
		}*/
		 
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onPause.chronometerList.size()=" + chronometerHolder.getCount());}
		
		//stop any chronometers that might be running
		chronometerHolder.stopAll();
		
	}
	
	//class to store the projection specification of the loader
	private static final class Projection{
		
		private static final String[] columns = new String[]{
			View_HabitsCompleteInfo.COLUMN_ID
			, View_HabitsCompleteInfo.COLUMN_NAME
			, View_HabitsCompleteInfo.COLUMN_CREATED_TIME
			, View_HabitsCompleteInfo.COLUMN_TARGET_DESCRIPTION
			, View_HabitsCompleteInfo.COLUMN_TARGET_CREATED_TIME
			, View_HabitsCompleteInfo.COLUMN_DAILY_FREQUENCY
			, View_HabitsCompleteInfo.COLUMN_MONDAY
			, View_HabitsCompleteInfo.COLUMN_TUESDAY
			, View_HabitsCompleteInfo.COLUMN_WEDNESDAY
			, View_HabitsCompleteInfo.COLUMN_THURSDAY
			, View_HabitsCompleteInfo.COLUMN_FRIDAY
			, View_HabitsCompleteInfo.COLUMN_SATURDAY
			, View_HabitsCompleteInfo.COLUMN_SUNDAY
			, View_HabitsCompleteInfo.COLUMN_WEEK_FREQUENCY
			, View_HabitsCompleteInfo.COLUMN_SCHEDULE_CREATED_TIME
			, View_HabitsCompleteInfo.COLUMN_TOTAL_HIT_COUNT
			, View_HabitsCompleteInfo.COLUMN_THIS_YEAR_HIT_COUNT
			, View_HabitsCompleteInfo.COLUMN_THIS_MONTH_HIT_COUNT
			, View_HabitsCompleteInfo.COLUMN_TODAY_HIT_COUNT
			, View_HabitsCompleteInfo.COLUMN_LAST_HIT_TIME
			, View_HabitsCompleteInfo.COLUMN_THIS_WEEK_HIT_COUNT
			, View_HabitsCompleteInfo.COLUMN_TARGET_HIT_COUNT
			, View_HabitsCompleteInfo.COLUMN_TARGET_HIT_DAY_COUNT
			, View_HabitsCompleteInfo.COLUMN_HIT_SESSION_START_TIME
			, View_HabitsCompleteInfo.COLUMN_TARGET_ID
			, View_HabitsCompleteInfo.COLUMN_SCHEDULE_ID
			, View_HabitsCompleteInfo.COLUMN_HIT_SESSION_ID
		};
		
		private static final int INDEX_COLUMN_ID = 0;
		private static final int INDEX_COLUMN_NAME = 1;
		private static final int INDEX_COLUMN_CREATED_TIME = 2;
		private static final int INDEX_COLUMN_TARGET_DESCRIPTION = 3;
		private static final int INDEX_COLUMN_TARGET_CREATED_TIME = 4;
		private static final int INDEX_COLUMN_DAILY_FREQUENCY = 5;
		private static final int INDEX_COLUMN_MONDAY = 6;
		private static final int INDEX_COLUMN_TUESDAY = 7;
		private static final int INDEX_COLUMN_WEDNESDAY = 8;
		private static final int INDEX_COLUMN_THURSDAY = 9;
		private static final int INDEX_COLUMN_FRIDAY = 10;
		private static final int INDEX_COLUMN_SATURDAY = 11;
		private static final int INDEX_COLUMN_SUNDAY = 12;
		private static final int INDEX_COLUMN_WEEK_FREQUENCY = 13;
		private static final int INDEX_COLUMN_SCHEDULE_CREATED_TIME = 14;
		private static final int INDEX_COLUMN_TOTAL_HIT_COUNT = 15;
		private static final int INDEX_COLUMN_THIS_YEAR_HIT_COUNT = 16;
		private static final int INDEX_COLUMN_THIS_MONTH_HIT_COUNT = 17;
		private static final int INDEX_COLUMN_TODAY_HIT_COUNT = 18;
		private static final int INDEX_COLUMN_LAST_HIT = 19;
		private static final int INDEX_COLUMN_THIS_WEEK_HIT_COUNT = 20;
		private static final int INDEX_COLUMN_TARGET_HIT_COUNT = 21;
		private static final int INDEX_COLUMN_TARGET_HIT_DAY_COUNT = 22;
		private static final int INDEX_COLUMN_HIT_SESSION_START_TIME = 23;
		private static final int INDEX_COLUMN_TARGET_ID = 24;
		private static final int INDEX_COLUMN_SCHEDULE_ID = 25;
		private static final int INDEX_COLUMN_HIT_SESSION_ID = 26;
		
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
		
		CursorLoader cursorLoader = new CursorLoader(getActivity(),MyContentProvider.URI_HABITS,Projection.columns,selection,selectionArgs,sortOrder);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onLoadFinished. cursor count=" + data.getCount());}
		adpt_habits.swapCursor(data);
		getView().findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
		getListView().setEmptyView(getView().findViewById(android.R.id.empty));
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adpt_habits.swapCursor(null);
	}
	
	
	//it would be more modularized if this inner class can be moved to it's own file 
	private class HabitListAdapter extends CursorAdapter{

		private LayoutInflater layoutInflater;
		
		public HabitListAdapter(Context context) {
			super(context, null, 0);
			layoutInflater = LayoutInflater.from(context);
		}

		private class ViewHolder{
			TextView tv_habitName;
			TextView tv_nextScheduledDate;
			TextView tv_lastHit;
			ImageButton btn_hit;
			TextView tv_todayHitCount;
			TextView tv_thisWeekHitCount;
			TextView tv_thisMonthHitCount;
			TextView tv_thisYearHitCount;
			TargetLayout tgt_target;
			ImageButton btn_startHitSession;
			TextView tv_totalHitCount;
			ViewAnimator va_hitSession;
			TimerLayout timerSession;
			TextView tv_habitCreationDate;
			
		}
		
		@Override
		public View newView(Context arg0, Cursor cursor, ViewGroup container) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"newView");}
			final View itemLayout = layoutInflater.inflate(R.layout.item_habit, container, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.tv_habitName = (TextView) itemLayout.findViewById(R.id.txt_habitname);
			viewHolder.tv_nextScheduledDate = (TextView) itemLayout.findViewById(R.id.txt_nextScheduledDate);
			
			viewHolder.btn_hit = (ImageButton) itemLayout.findViewById(R.id.btn_hit);
			viewHolder.tv_lastHit = (TextView) itemLayout.findViewById(R.id.txt_lasthit);
			viewHolder.tv_todayHitCount = (TextView) itemLayout.findViewById(R.id.txt_todayhitcount);
			viewHolder.tv_thisWeekHitCount = (TextView) itemLayout.findViewById(R.id.txt_thisweekhitcount);
			viewHolder.tv_thisMonthHitCount = (TextView) itemLayout.findViewById(R.id.txt_thismonthhitcount);
			viewHolder.tv_thisYearHitCount = (TextView) itemLayout.findViewById(R.id.txt_thisyearhitcount);
			viewHolder.tgt_target = (TargetLayout) itemLayout.findViewById(R.id.tgt_target);
			viewHolder.btn_startHitSession = (ImageButton) itemLayout.findViewById(R.id.btn_starthitsession);
			viewHolder.tv_totalHitCount = (TextView) itemLayout.findViewById(R.id.txt_totalhitcount);
			viewHolder.va_hitSession = (ViewAnimator) itemLayout.findViewById(R.id.va_hitsession);
			viewHolder.timerSession = (TimerLayout) itemLayout.findViewById(R.id.timer_hitsession);
			viewHolder.tv_habitCreationDate = (TextView) itemLayout.findViewById(R.id.txt_habitcreationdate);
			
			itemLayout.setTag(viewHolder);
			
			return itemLayout;
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"bindView");}
			
			//get the view holder for later use
			final ViewHolder viewHolder = (ViewHolder) view.getTag();
			
			//get values from cursor
			String habitName = cursor.getString(Projection.INDEX_COLUMN_NAME);
			String creationDate = cursor.getString(Projection.INDEX_COLUMN_CREATED_TIME);
			String lastHitTime = cursor.getString(Projection.INDEX_COLUMN_LAST_HIT);
			int totalHitCount = cursor.getInt(Projection.INDEX_COLUMN_TOTAL_HIT_COUNT);
			int todayHitCount = cursor.getInt(Projection.INDEX_COLUMN_TODAY_HIT_COUNT);
			int thisMonthHitCount = cursor.getInt(Projection.INDEX_COLUMN_THIS_MONTH_HIT_COUNT);
			int thisYearHitCount = cursor.getInt(Projection.INDEX_COLUMN_THIS_YEAR_HIT_COUNT);
			int thisWeekHitCount = cursor.getInt(Projection.INDEX_COLUMN_THIS_WEEK_HIT_COUNT);
			final long habitId = cursor.getLong(Projection.INDEX_COLUMN_ID);
			final Long targetId = cursor.isNull(Projection.INDEX_COLUMN_TARGET_ID) ? null : cursor.getLong(Projection.INDEX_COLUMN_TARGET_ID);
			final String targetDescription = cursor.getString(Projection.INDEX_COLUMN_TARGET_DESCRIPTION);
			String targetCreatedTime = cursor.getString(Projection.INDEX_COLUMN_TARGET_CREATED_TIME);
			Integer targetHitCount = cursor.getInt(Projection.INDEX_COLUMN_TARGET_HIT_COUNT);
			Integer targetHitDayCount = cursor.getInt(Projection.INDEX_COLUMN_TARGET_HIT_DAY_COUNT);
			String hitSessionStartTime = cursor.getString(Projection.INDEX_COLUMN_HIT_SESSION_START_TIME);
			final Long hitSessionId = cursor.isNull(Projection.INDEX_COLUMN_HIT_SESSION_ID) ? null : cursor.getLong(Projection.INDEX_COLUMN_HIT_SESSION_ID);
			
			Date dt_hitSessionStartTime;
			final HitSession hitSession;
			if (hitSessionId != null) {
				dt_hitSessionStartTime = null;
				try {
					dt_hitSessionStartTime = Utilities.getSimpleDateFormatUTC().parse(hitSessionStartTime);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				hitSession = new HitSession(hitSessionId,null,habitId,dt_hitSessionStartTime,null);
			}
			else{
				dt_hitSessionStartTime = null;
				hitSession = null;
			}
			
			 
			boolean isScheduleEnabled = (!cursor.isNull(Projection.INDEX_COLUMN_SCHEDULE_ID));
			if(isScheduleEnabled){
				int dailyFrequency = cursor.getInt(Projection.INDEX_COLUMN_DAILY_FREQUENCY);
				boolean monday = (cursor.getInt(Projection.INDEX_COLUMN_MONDAY) == 1)?true:false;
				boolean tuesday = (cursor.getInt(Projection.INDEX_COLUMN_TUESDAY) == 1)?true:false;
				boolean wednesday = (cursor.getInt(Projection.INDEX_COLUMN_WEDNESDAY) == 1)?true:false;
				boolean thursday = (cursor.getInt(Projection.INDEX_COLUMN_THURSDAY) == 1)?true:false;
				boolean friday = (cursor.getInt(Projection.INDEX_COLUMN_FRIDAY) == 1)?true:false;
				boolean saturday = (cursor.getInt(Projection.INDEX_COLUMN_SATURDAY) == 1)?true:false;
				boolean sunday = (cursor.getInt(Projection.INDEX_COLUMN_SUNDAY) == 1)?true:false;
				int weekFrequency = cursor.getInt(Projection.INDEX_COLUMN_WEEK_FREQUENCY);
				String scheduleCreatedTime = cursor.getString(Projection.INDEX_COLUMN_SCHEDULE_CREATED_TIME);
				long scheduleId = cursor.getLong(Projection.INDEX_COLUMN_SCHEDULE_ID);
				
				Date dt_scheduleCreatedTime = null;
						
				try {
					dt_scheduleCreatedTime = Utilities.getSimpleDateFormatUTC().parse(scheduleCreatedTime);
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Schedule schedule = new Schedule(scheduleId,habitId,dailyFrequency, monday, tuesday, wednesday, thursday, friday, saturday, sunday, weekFrequency, dt_scheduleCreatedTime, null);
				
				//get the next scheduled date
				Calendar nextScheduledDate = null;
				String str_nextScheduledDate = "";
				try {
					nextScheduledDate = schedule.getNextScheduledDate(null);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				/*if next scheduled date is today and 
				 * today's hit count has already reached the required quota,
				 * get the next scheduled date after today
				*/
				if(Utilities.isDatesEqual(nextScheduledDate, Calendar.getInstance()) && todayHitCount >= dailyFrequency){
					try {
						Calendar tomorrow = Calendar.getInstance();
						tomorrow.add(Calendar.DAY_OF_YEAR, 1);
						nextScheduledDate = schedule.getNextScheduledDate(tomorrow);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				str_nextScheduledDate =  "next:" + Utilities.getDurationFromNowIgnoringTime(nextScheduledDate);
				
				//set the views with values
				viewHolder.tv_nextScheduledDate.setText(str_nextScheduledDate);
			}
			
			//calculate target created time
			Date dt_targetCreatedTime = null;
			if (!(targetCreatedTime == null || targetCreatedTime.equals(null))) {
				try {
					dt_targetCreatedTime = Utilities.getSimpleDateFormatUTC().parse(targetCreatedTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			//set target creation date
			try {
				Date dt_creationDate = Utilities.getSimpleDateFormatUTC().parse(creationDate);
				Spannable spn_creationDate = new SpannableString("created " + Utilities.getDisplayFormattedDate(dt_creationDate));
				spn_creationDate.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 8, spn_creationDate.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				viewHolder.tv_habitCreationDate.setText(spn_creationDate);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			//set all the other views with values
			viewHolder.tv_habitName.setText(habitName);
			viewHolder.tv_totalHitCount.setText(String.valueOf(totalHitCount));
			viewHolder.tv_lastHit.setText(Utilities.getDurationFromNow(lastHitTime));
			viewHolder.tv_todayHitCount.setText(String.valueOf(todayHitCount));
			viewHolder.tv_thisMonthHitCount.setText(String.valueOf(thisMonthHitCount));
			viewHolder.tv_thisYearHitCount.setText(String.valueOf(thisYearHitCount));
			viewHolder.tv_thisWeekHitCount.setText(String.valueOf(thisWeekHitCount));
			
			//initialize the target layout only if there is an open target
			final Target openTarget;
			if(targetId != null){
				viewHolder.tgt_target.setValues(targetDescription, new HitEffort(targetHitCount,targetHitDayCount), dt_targetCreatedTime);
				openTarget = new Target(targetId, habitId,targetDescription,dt_targetCreatedTime,null);
			}
			else{
				viewHolder.tgt_target.clearValues();
				openTarget = null;
			}
			
			//initialize the session timer
			viewHolder.va_hitSession.setInAnimation(null);
			viewHolder.va_hitSession.setOutAnimation(null);
			if(hitSessionId !=null){
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"hitSessionStartTime="+hitSessionStartTime);}
				viewHolder.va_hitSession.setDisplayedChild(1);
			}
			else viewHolder.va_hitSession.setDisplayedChild(0);
			Chronometer tmpChronometer = viewHolder.timerSession.init(dt_hitSessionStartTime);
			if(tmpChronometer != null){
				chronometerHolder.add(hitSessionId, tmpChronometer);
			}
			
			viewHolder.tgt_target.setOnActionListener(new TargetLayout.OnActionListener(){

				private void notifyURI(){
					getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
				}
				
				@Override
				public void onAchieve() {
					openTarget.achieve();
					SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
					new TargetDaoImpl(db).update(openTarget);
					notifyURI();
					Toast.makeText(getActivity(), "Congragulations on achieving your target", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onAbandon() {
					SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
					new TargetDaoImpl(db).delete(openTarget.getId());
					notifyURI();
					Toast.makeText(getActivity(), "Target abandoned", Toast.LENGTH_LONG).show();
					
				}

				@Override
				public void onSet(String description) {
					Target newTarget = new Target(habitId,description,new Date(),null);
					SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
					new TargetDaoImpl(db).insert(newTarget);
					notifyURI();
					Toast.makeText(getActivity(), "New target created", Toast.LENGTH_LONG).show();
					
				}
				
			});
			
			//register a hit in local DB on hit button click
			viewHolder.btn_hit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					Hit hit = new Hit(habitId, new Date());
					SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
					new HitDaoImpl(db).insert(hit);
					//new Habit(habitId).hit();
					getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
				}
				
			});
			
			//start hit session on click
			viewHolder.btn_startHitSession.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view){
					HitSession hitSession = new HitSession(habitId, new Date());
					SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
					new HitSessionDaoImpl(db).insert(hitSession);
					
					final Animation inAnim = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_in_left);
					final Animation outAnim = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_out_right);
					viewHolder.va_hitSession.setInAnimation(inAnim);
					viewHolder.va_hitSession.setOutAnimation(outAnim);
					
					Chronometer tmpChronometer = viewHolder.timerSession.init(hitSession.getStartTime());
					if(tmpChronometer != null){
						chronometerHolder.add(hitSessionId, tmpChronometer);
					}
					
					viewHolder.va_hitSession.showNext();
					Toast.makeText(getActivity(), "Session started", Toast.LENGTH_LONG).show();
					getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
				}
			});
			
			viewHolder.timerSession.setOnTimerActionListener(new OnTimerActionListener(){

				@Override
				public void onEnd() {
					//end the session
					
					SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
					db.beginTransaction();
					boolean isSuccess = true;
					try{
						Hit hit = new Hit(habitId,new Date());
						long hitId = new HitDaoImpl(db).insert(hit);
						hitSession.endSession(hitId, hit.getHitTime());
						new HitSessionDaoImpl(db).update(hitSession);
						db.setTransactionSuccessful();
					}
					catch(Exception e){
						isSuccess = false;
						e.printStackTrace();
					}
					finally{
						db.endTransaction();
					}
					
					if(isSuccess){
						final Animation inAnim = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_in_left);
						final Animation outAnim = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_out_right);
						viewHolder.va_hitSession.setInAnimation(inAnim);
						viewHolder.va_hitSession.setOutAnimation(outAnim);
						
						Toast.makeText(getActivity(), "Session ended", Toast.LENGTH_LONG).show();
						viewHolder.va_hitSession.showNext();
						chronometerHolder.remove(hitSessionId);
						getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
					}
					
				}

				@Override
				public void onCancel() {
					//cancel the session
					SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
					new HitSessionDaoImpl(db).delete(hitSessionId);
					final Animation inAnim = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_in_left);
					final Animation outAnim = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_out_right);
					viewHolder.va_hitSession.setInAnimation(inAnim);
					viewHolder.va_hitSession.setOutAnimation(outAnim);
					
					Toast.makeText(getActivity(), "Session cancelled", Toast.LENGTH_LONG).show();
					viewHolder.va_hitSession.showNext();
					chronometerHolder.remove(hitSessionId);
					getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
					
				}
				
			});
			
		}

		
		
		
	}
	
	class ContextualActionBarCallBack_Habit implements ActionMode.Callback{

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked");}
			boolean isSuccess = false;
			SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
			
			switch(menuItem.getItemId()){
			case R.id.action_delete_habit :
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : delete");}
				//assuming only 1 selection allowed. So getCheckedItemIds()[0] should be enough
				isSuccess = new HabitOperationsManagerImpl(db).deleteHabit(mListView.getCheckedItemIds()[0]);
				if(isSuccess){
					getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
					Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_LONG).show();
					Reminder.setNextReminderAlarm();
				}
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : delete : done");}
				break;
				
			case R.id.action_archive_habit :
				//assuming only 1 selection allowed. So getCheckedItemPosition() should be enough
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : archive");}
				Cursor cursor = ((Cursor) mListView.getItemAtPosition(mListView.getCheckedItemPosition()));
				if(!cursor.isNull(Projection.INDEX_COLUMN_TARGET_ID)){
					Toast.makeText(getActivity(), "Delete/Achieve the target first", Toast.LENGTH_LONG).show();
				}
				else if(!cursor.isNull(Projection.INDEX_COLUMN_HIT_SESSION_ID)){
					Toast.makeText(getActivity(), "End/Cancel the session first", Toast.LENGTH_LONG).show();
				}
				else{
					isSuccess = new HabitOperationsManagerImpl(db).archiveHabit(mListView.getCheckedItemIds()[0]);
					if(isSuccess){
						getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
						getActivity().getContentResolver().notifyChange(MyContentProvider.URI_ARCHIVED_HABITS, null);
						Toast.makeText(getActivity(), "Archived", Toast.LENGTH_LONG).show();
						Reminder.setNextReminderAlarm();
					}
				}
				
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : archive : done");}
				break;
				
			case R.id.action_edit_habit :
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : edit");}
				//assuming only 1 selection allowed. So getCheckedItemIds()[0] should be enough
				Intent intent = new Intent(getActivity(),Activity_EditHabit.class);
				intent.putExtra(Activity_EditHabit.KEY_HABITID, mListView.getCheckedItemIds()[0]);
				startActivity(intent);
				
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : edit : done");}
				break;
				
			default :
				break;
			}
			
			mode.finish();
			return true;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onCreateActionMode");}
			mode.getMenuInflater().inflate(R.menu.habit_contextual_menu, menu);
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode actionMode) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onDestroyActionMode");}
			mListView.setItemChecked(mListView.getCheckedItemPosition(), false);
			mActionMode = null; 
			
		}

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onPrepareActionMode");}
			return false;
		}
	}


	
	
	
}