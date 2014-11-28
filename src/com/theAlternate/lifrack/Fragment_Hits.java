package com.theAlternate.lifrack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
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
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.theAlternate.lifrack.Dao.IHitDao;
import com.theAlternate.lifrack.Dao.HitDaoImpl;
import com.theAlternate.lifrack.Dao.IHitSessionDao;
import com.theAlternate.lifrack.Dao.HitSessionDaoImpl;
import com.theAlternate.lifrack.LocalDB.View_ArchivedHits;
import com.theAlternate.lifrack.LocalDB.View_Hits;
import com.theAlternate.lifrack.R.color;

public class Fragment_Hits extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
	HitLogAdapter mAdapter;
	ListView mListView;
	private static final int HITLOG_LOADER_ID = 0;
	private static final String LOG_TAG = "Fragment_Hits";
	private ActionMode mActionMode;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_hits, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		//inititalize list view
		mAdapter = new HitLogAdapter(getActivity());
		mListView = getListView();
		this.setListAdapter(mAdapter);
		
		//initially hide the empty view,
		//otherwise it will be shown until loader has finished
		getView().findViewById(android.R.id.empty).setVisibility(View.INVISIBLE);
		mListView.setEmptyView(null);
		
		//start CAB on long click only if it is not in archived mode
		if(!isArchived()){
			
			
			mListView.setOnItemLongClickListener(new OnItemLongClickListener(){
	
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
					if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onLongClick");}
					//if action mode is already started
					if(mActionMode != null){
						//if the only selected item was long-clicked, then end the action mode
						if(mListView.getCheckedItemCount() == 1){
							SparseBooleanArray checkedItemPositions = mListView.getCheckedItemPositions();
							for(int i=0;i<checkedItemPositions.size();i++){
								if(checkedItemPositions.keyAt(i) == pos && checkedItemPositions.valueAt(i)){
									mActionMode.finish();
									return true;
								}
							}
							return false;
						}
						return false;
						
					}
					//if action mode is NOT already started
					else{
						mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
						mListView.setItemChecked(pos, true);
						mActionMode = ((ActionBarActivity) getActivity()).startSupportActionMode(new ContextualActionBarCallBack());
						return true;
					}
				}
				
			});
		}
		
	}
	
	private void uncheckAll() {
		SparseBooleanArray selectedItems = mListView.getCheckedItemPositions();
		if(selectedItems != null){
			for (int i = 0; i < selectedItems.size(); i++) {
				mListView.setItemChecked(selectedItems.keyAt(i), false);
			}
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		getLoaderManager().initLoader(HITLOG_LOADER_ID, null, this);
		
		//when returning back to this fragment, the selected item remains highlighted
		//so remove selection
		if(mActionMode == null)
		uncheckAll();
	}
	//need local time here
	private static final class Projection{
		private static final String[] live = new String[] {
				View_Hits.COLUMN_ID
				, View_Hits.COLUMN_HIT_TIME
				, View_Hits.COLUMN_SESSION_ID
				, View_Hits.COLUMN_SESSION_START_TIME
		};
		
		private static final String[] archived = new String[]{
			View_ArchivedHits.COLUMN_ID
			, View_ArchivedHits.COLUMN_HIT_TIME
			, View_ArchivedHits.COLUMN_SESSION_ID
			, View_ArchivedHits.COLUMN_SESSION_START_TIME
			};
		
		private static final int INDEX_COLUMN_ID = 0;
		private static final int INDEX_COLUMN_HIT_TIME = 1;
		private static final int INDEX_COLUMN_SESSION_ID = 2;
		private static final int INDEX_COLUMN_SESSION_START_TIME = 3;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String selection = null;
		String[] selectionArgs = new String[] {String.valueOf(getActivity().getIntent().getExtras().getLong(Activity_HabitDetails.KEY_HABITID))};
		String sortOrder = null;
		CursorLoader cursorLoader = null;
		
		if(isArchived()){
			selection = View_ArchivedHits.COLUMN_HABIT_ID + "=?";
			sortOrder = View_ArchivedHits.COLUMN_HIT_ID + " desc";
			cursorLoader = new CursorLoader(getActivity(), MyContentProvider.URI_ARCHIVED_HABIT_HITS, Projection.archived, selection, selectionArgs, sortOrder);
		}
		else{
			selection = View_Hits.COLUMN_HABIT_ID + "=?";
			sortOrder = View_Hits.COLUMN_HIT_ID + " desc";
			cursorLoader = new CursorLoader(getActivity(), MyContentProvider.URI_HABIT_HITS, Projection.live, selection, selectionArgs, sortOrder);
		}
		
		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		mAdapter.swapCursor(cursor);
		getView().findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
		mListView.setEmptyView(getView().findViewById(android.R.id.empty));
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}
	
	private class HitLogAdapter extends CursorAdapter{
		private LayoutInflater layoutInflater;
		
		public HitLogAdapter(Context context){
			super(context,null,0);
			layoutInflater = LayoutInflater.from(context);
		}
		
		private class ViewHolder{
			TextView tv_year;
			TextView tv_month;
			TextView tv_week;
			TextView tv_day;
			TextView tv_time;
		}

		@Override
		public void bindView(View view, Context arg1, Cursor cursor) {
			
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			
			//get the hit time
			String hitTime = cursor.getString(Projection.INDEX_COLUMN_HIT_TIME);
			
			Date dt_hitTime = null;
			try {
				dt_hitTime = Utilities.getSimpleDateFormatUTC().parse(hitTime);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			//get the session start time
			String sessionStartTime = cursor.getString(Projection.INDEX_COLUMN_SESSION_START_TIME);
			
			Date dt_sessionStartTime = null;
			String sessionDuration = null;
			if(sessionStartTime != null){
				try {
					dt_sessionStartTime = Utilities.getSimpleDateFormatUTC().parse(sessionStartTime);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				//calculate session duration
				sessionDuration = Utilities.getSessionDuration(dt_sessionStartTime, dt_hitTime);
				
			}
			
			//set the year
			viewHolder.tv_year.setText(new SimpleDateFormat("yyyy",Locale.ENGLISH).format(dt_hitTime));
			
			//set the month
			viewHolder.tv_month.setText(new SimpleDateFormat("MMM",Locale.ENGLISH).format(dt_hitTime));
			
			//set the week
			Calendar cal_hitTime = Calendar.getInstance();
			cal_hitTime.setFirstDayOfWeek(Calendar.MONDAY);
			cal_hitTime.setTime(dt_hitTime);
			Spannable spannable = new SpannableString("wk" + String.valueOf(cal_hitTime.get(Calendar.WEEK_OF_MONTH)));
			spannable.setSpan(new RelativeSizeSpan(.75f),0,2,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			spannable.setSpan(new ForegroundColorSpan(color.grey),0,2,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			viewHolder.tv_week.setText(spannable);
			
			//set the day
			spannable = new SpannableString(new SimpleDateFormat("EEE",Locale.ENGLISH).format(dt_hitTime) 
					+ "\n(" + String.valueOf(cal_hitTime.get(Calendar.DAY_OF_MONTH)) + ")");
			spannable.setSpan(new RelativeSizeSpan(.75f),4,spannable.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			spannable.setSpan(new ForegroundColorSpan(color.grey),4,spannable.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			viewHolder.tv_day.setText(spannable);
			
			//set the time
			spannable = new SpannableString(new SimpleDateFormat("HH:mm",Locale.ENGLISH).format(dt_hitTime)
					+ (sessionDuration == null ? "" : "\n("+ sessionDuration +")"));
			spannable.setSpan(new RelativeSizeSpan(.75f),5,spannable.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			spannable.setSpan(new ForegroundColorSpan(color.grey),5,spannable.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			viewHolder.tv_time.setText(spannable);
			
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup container) {
			final View itemLayout = layoutInflater.inflate(R.layout.item_hit, container, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.tv_year = (TextView) itemLayout.findViewById(R.id.txt_hittime_year);
			viewHolder.tv_month = (TextView) itemLayout.findViewById(R.id.txt_hittime_month);
			viewHolder.tv_week = (TextView) itemLayout.findViewById(R.id.txt_hittime_week);
			viewHolder.tv_day = (TextView) itemLayout.findViewById(R.id.txt_hittime_day);
			viewHolder.tv_time = (TextView) itemLayout.findViewById(R.id.txt_hittime_time);
			//viewHolder.tv_test = (TextView) itemLayout.findViewById(R.id.txt_hittime_test);
			itemLayout.setTag(viewHolder);
			return itemLayout;
		}
		
	}
	
	private boolean isArchived(){
		return getActivity().getIntent().getExtras().getBoolean(Activity_HabitDetails.KEY_ARCHIVED); //returns false if it does not exist
	}
	
	
	private class ContextualActionBarCallBack implements ActionMode.Callback{

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked");}
			boolean isSuccess = false;
			
			switch(menuItem.getItemId()){
			case R.id.action_delete_hit :
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : delete");}
				SparseBooleanArray checkedPositions = mListView.getCheckedItemPositions();
				int count = checkedPositions.size();
				Cursor cursor;
				SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
				IHitSessionDao hitSessionDao = new HitSessionDaoImpl(db);
				IHitDao hitDao = new HitDaoImpl(db);
				db.beginTransaction();
				try{
					//delete the sessions
					for(int i=0;i<count;i++){
						Log.d(LOG_TAG,"i=" + String.valueOf(i) + ",key=" + checkedPositions.keyAt(i) + ",value=" + (checkedPositions.valueAt(i)?"true":"false"));
						if(checkedPositions.valueAt(i)){
							cursor = (Cursor) mListView.getItemAtPosition(checkedPositions.keyAt(i));
							if(!cursor.isNull(Projection.INDEX_COLUMN_SESSION_ID)){
								hitSessionDao.delete(cursor.getLong(Projection.INDEX_COLUMN_SESSION_ID));
							}
						}
						
					}
					
					//delete the hits
					isSuccess = hitDao.delete(mListView.getCheckedItemIds());
					
					db.setTransactionSuccessful();
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					db.endTransaction();
				}
				
				if(isSuccess){
					getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
					getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABIT_HITS, null);
					Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_LONG).show();
				}
				
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onActionItemClicked : delete : done");}
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
			mode.getMenuInflater().inflate(R.menu.hit_contextual_menu, menu);
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode actionMode) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onDestroyActionMode");}
			uncheckAll();
			mActionMode = null;
			mListView.post(new Runnable(){
				@Override
				public void run(){
					mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
				}
			});
			
			
		}

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onPrepareActionMode");}
			return false;
		}
	}
	
}