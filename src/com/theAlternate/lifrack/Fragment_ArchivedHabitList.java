package com.theAlternate.lifrack;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_ArchivedHabitList extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private ListView mListView;
	private HabitListAdapter adpt_habits;
	private static final int HABITLIST_LOADER_ID = 0;
	private static final String LOG_TAG = "Fragment_ArchivedHabitList";
	private ActionMode mActionMode;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_archivedhabitlist, container,false);
	}
	
	/*@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.fragment_archivedhabitlist, menu);
	}*/
	
	//this does not work
	/*@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onCreateContextMenu before super");}
		super.onCreateContextMenu(menu, v, menuInfo);
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onCreateContextMenu after super");}
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.target_contextual_menu, menu);
	}*/
	
	/*@Override
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
	}*/
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
				
		//set the adaptor for list view
		adpt_habits = new HabitListAdapter(getActivity());
		//mListView = (ListView) getView().findViewById(R.id.listview_archivedhabits);
		//mListView.setAdapter(adpt_habits);
		mListView = getListView();
		this.setListAdapter(adpt_habits);
		
		/*//show archived habits button
		int countArchived = Habit.getArchivedCount(getActivity());
		if(countArchived > 0){
			Button btn_ShowArchived = new Button(getActivity());
			btn_ShowArchived.setText("Show Archived");
			mListView.addFooterView(btn_ShowArchived);
		}*/
		
		//start CAB on long click
		mListView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onLongClick");}
				if(mActionMode != null){
					uncheckAllHabits();
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
				intent.putExtra(Activity_HabitDetails.KEY_ARCHIVED, true);
				intent.putExtra(Activity_HabitDetails.KEY_HABITNAME
						, ((Cursor)adapterView.getAdapter().getItem(pos)).getString(Projection.INDEX_COLUMN_NAME));
				startActivity(intent);
				
			}
			
		});
		
		
	}
	
	private void uncheckAllHabits() {
		//Log.d("faizal","uncheckAllMessages - START");
		SparseBooleanArray selectedItems = mListView.getCheckedItemPositions();
		for (int i = 0; i < selectedItems.size(); i++) {
			//Log.d("faizal", "uncheckAllMessages - i=" + i);
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
		uncheckAllHabits();
		
		//start any chronometers that were paused
		//chronometerHolder.startAll();
		
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
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onPause.chronometerList.size()=" + chronometerList.size());}
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onPause.chronometerList.size()=" + chronometerMap.size());}
		//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onPause.chronometerList.size()=" + chronometerHolder.getCount());}
		
		//stop any chronometers that might be running
		//chronometerHolder.stopAll();
		
	}
	
	/*@Override
	public void onDestroy(){
		super.onDestroy();
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onDestroy");}
	}*/
	
	/*private void onClickAddButton(){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getUserVisibleHint()="+getUserVisibleHint());}L
		Intent intent = new Intent(getActivity(),Activity_AddHabit.class);
		startActivity(intent);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment_AddHabit fragment_addHabit = new Fragment_AddHabit();
		fragmentTransaction.add(R.id.rl_activity_main_container, fragment_addHabit);
		fragmentTransaction.commit();
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onClickAddButton : Activity_AddHabit started");}
	}*/
	
	//class to store the projection specification of the loader
	private static final class Projection{
		
		private static final String[] columns = new String[]{
			View_ArchivedHabits_Summary.COLUMN_ID
			,View_ArchivedHabits_Summary.COLUMN_NAME
			,View_ArchivedHabits_Summary.COLUMN_CREATED_TIME
			,View_ArchivedHabits_Summary.COLUMN_ARCHIVED_TIME
			,View_ArchivedHabits_Summary.COLUMN_TARGET_COUNT
			,View_ArchivedHabits_Summary.COLUMN_HIT_COUNT
			,View_ArchivedHabits_Summary.COLUMN_HIT_DAY_COUNT
		};
		
		private static final int INDEX_COLUMN_ID = 0;
		private static final int INDEX_COLUMN_NAME = 1;
		private static final int INDEX_COLUMN_CREATED_TIME = 2;
		private static final int INDEX_COLUMN_ARCHIVED_TIME = 3;
		private static final int INDEX_COLUMN_TARGET_COUNT = 4;
		private static final int INDEX_COLUMN_HIT_COUNT = 5;
		private static final int INDEX_COLUMN_HIT_DAY_COUNT = 6;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
		
		CursorLoader cursorLoader = new CursorLoader(getActivity(),MyContentProvider.URI_ARCHIVED_HABITS,Projection.columns,selection,selectionArgs,sortOrder);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onLoadFinished. cursor count=" + data.getCount());}
		adpt_habits.swapCursor(data);
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
			TextView tv_hits;
			TextView tv_targets;
			TextView tv_habitDuration;
		}
		
		@Override
		public View newView(Context arg0, Cursor cursor, ViewGroup container) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"newView");}
			final View itemLayout = layoutInflater.inflate(R.layout.item_archivedhabit, container, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.tv_habitName = (TextView) itemLayout.findViewById(R.id.txt_habitname);
			viewHolder.tv_hits = (TextView) itemLayout.findViewById(R.id.txt_hits);
			viewHolder.tv_targets = (TextView) itemLayout.findViewById(R.id.txt_targets);
			viewHolder.tv_habitDuration = (TextView) itemLayout.findViewById(R.id.txt_habitduration);
			
			itemLayout.setTag(viewHolder);
			
			return itemLayout;
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"bindView");}

			//get values from cursor
			String habitName = cursor.getString(Projection.INDEX_COLUMN_NAME);
			String habitCreatedTime = cursor.getString(Projection.INDEX_COLUMN_CREATED_TIME);
			String habitArchivedTime = cursor.getString(Projection.INDEX_COLUMN_ARCHIVED_TIME);
			int targetCount = cursor.getInt(Projection.INDEX_COLUMN_TARGET_COUNT);
			int hitCount = cursor.getInt(Projection.INDEX_COLUMN_HIT_COUNT);
			int hitDayCount = cursor.getInt(Projection.INDEX_COLUMN_HIT_DAY_COUNT);
			
			//calculate active duration
			Date dt_habitCreatedTime = null;
			try {
				dt_habitCreatedTime = Utilities.getSimpleDateFormatUTC().parse(habitCreatedTime);
			} catch (ParseException e1) {
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"Error parsing date-"+habitCreatedTime);};
				e1.printStackTrace();
			}
			
			Date dt_habitArchivedTime = null;
			try {
				dt_habitArchivedTime = Utilities.getSimpleDateFormatUTC().parse(habitArchivedTime);
			} catch (ParseException e1) {
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"Error parsing date-"+habitArchivedTime);};
				e1.printStackTrace();
			}
			
			String diff = Utilities.getDifferenceIgnoringTime(dt_habitCreatedTime,dt_habitArchivedTime);
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"habitCreatedTime_local="+habitCreatedTime+",habitArchivedTime_local="+habitArchivedTime+",diff="+diff);}
			
			//set the views with values
			final ViewHolder viewHolder = (ViewHolder) view.getTag();
			viewHolder.tv_habitName.setText(habitName);
			viewHolder.tv_hits.setText(String.valueOf(hitCount) + " hits on " + String.valueOf(hitDayCount) + " days");
			viewHolder.tv_targets.setText(String.valueOf(targetCount));
			viewHolder.tv_habitDuration.setText(diff);
			
		}

		
		
		
	}
	
	/*private Runnable updateTimerThread = new Runnable(){
		public void run(){
			
		}
	};*/
	
	class ContextualActionBarCallBack_Habit implements ActionMode.Callback{
		//private Context mContext;
		//private ListView mListView;
		//private static final String LOG_TAG = "ContextualActionBarCallBack_Habit";
		
		/*public ContextualActionBarCallBack_Habit(Context context, ListView listView) {
			mContext = context;
			mListView = listView;
		}*/

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked");}
			boolean isSuccess;
			
			switch(menuItem.getItemId()){
			case R.id.action_delete_habit :
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : delete");}
				//assuming only 1 selection allowed. So getCheckedItemIds()[0] should be enough
				isSuccess = new HabitOperationsManagerImpl().deleteArchivedHabit(mListView.getCheckedItemIds()[0]);
				//Reminder.setNextReminderAlarm();
				//isSuccess = new ArchivedHabit(mListView.getCheckedItemIds()[0]).delete();
				if(isSuccess) getActivity().getContentResolver().notifyChange(MyContentProvider.URI_ARCHIVED_HABITS, null);
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : delete : done");}
				break;
				
			case R.id.action_unarchive_habit :
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : archive");}
				//assuming only 1 selection allowed. So getCheckedItemIds()[0] should be enough
				isSuccess = new HabitOperationsManagerImpl().unarchiveHabit(mListView.getCheckedItemIds()[0]);
				if(isSuccess){
					getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
					getActivity().getContentResolver().notifyChange(MyContentProvider.URI_ARCHIVED_HABITS, null);
					Toast.makeText(getActivity(), "Un-archived", Toast.LENGTH_LONG).show();
				}
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : archive : done");}
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
			mode.getMenuInflater().inflate(R.menu.archivedhabit_contextual_menu, menu);
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