package com.theAlternate.lifrack;

import java.text.ParseException;
import java.util.Date;

import com.theAlternate.lifrack.Dao.HabitOperationsManagerImpl;
import com.theAlternate.lifrack.Dao.TargetDaoImpl;
import com.theAlternate.lifrack.LocalDB.View_ArchivedTargetsInfo;
import com.theAlternate.lifrack.LocalDB.View_TargetsInfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_TargetsInfo extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
	TargetSummaryAdapter mAdapter;
	ListView mListView;
	private static final int TARGETSUMMARY_LOADER_ID = 0;
	private static final String LOG_TAG = "Fragment_TargetsInfo";
	private boolean isArchived = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_targetsinfo, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		//inititalize list view
		mAdapter = new TargetSummaryAdapter(getActivity());
		mListView = getListView();
		this.setListAdapter(mAdapter);
		
	}
	
	@Override
	public void onResume(){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onResume()");}
		super.onResume();
		isArchived = getActivity().getIntent().getExtras().getBoolean(Activity_HabitDetails.KEY_ARCHIVED); //returns false if it does not exist
		getLoaderManager().initLoader(TARGETSUMMARY_LOADER_ID, null, this);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onPause()");}
	}
	
	private static final class Projection{
		private static final String[] live = new String[]{
			View_TargetsInfo.COLUMN_TARGET_ID
			, View_TargetsInfo.COLUMN_DESCRIPTION
			, View_TargetsInfo.COLUMN_CREATED_TIME
			, View_TargetsInfo.COLUMN_ACHIEVED_TIME
			, View_TargetsInfo.COLUMN_HIT_COUNT
			, View_TargetsInfo.COLUMN_HIT_DAY_COUNT
			, View_TargetsInfo.COLUMN_HIT_COUNT_FROM_HABIT_CREATION
			, View_TargetsInfo.COLUMN_HIT_DAY_COUNT_FROM_HABIT_CREATION
			, View_TargetsInfo.COLUMN_HABIT_CREATED_TIME
		};
		
		private static final String[] archived = new String[]{
			View_ArchivedTargetsInfo.COLUMN_TARGET_ID
			, View_ArchivedTargetsInfo.COLUMN_DESCRIPTION
			, View_ArchivedTargetsInfo.COLUMN_CREATED_TIME
			, View_ArchivedTargetsInfo.COLUMN_ACHIEVED_TIME
			, View_ArchivedTargetsInfo.COLUMN_HIT_COUNT
			, View_ArchivedTargetsInfo.COLUMN_HIT_DAY_COUNT
			, View_ArchivedTargetsInfo.COLUMN_HIT_COUNT_FROM_HABIT_CREATION
			, View_ArchivedTargetsInfo.COLUMN_HIT_DAY_COUNT_FROM_HABIT_CREATION
			, View_ArchivedTargetsInfo.COLUMN_HABIT_CREATED_TIME
		};
		
		private static final int INDEX_COLUMN_TARGET_ID = 0;
		private static final int INDEX_COLUMN_DESCRIPTION = 1;
		private static final int INDEX_COLUMN_CREATED_TIME = 2;
		private static final int INDEX_COLUMN_ACHIEVED_TIME = 3;
		private static final int INDEX_COLUMN_HIT_COUNT = 4;
		private static final int INDEX_COLUMN_HIT_DAY_COUNT = 5;
		private static final int INDEX_COLUMN_HIT_COUNT_FROM_HABIT_CREATION = 6;
		private static final int INDEX_COLUMN_HIT_DAY_COUNT_FROM_HABIT_CREATION = 7;
		private static final int INDEX_COLUMN_HABIT_CREATED_TIME = 8;
		
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onCreateLoader()");}
		String selection = null;
		String[] selectionArgs = new String[] {String.valueOf(getActivity().getIntent().getExtras().getLong(Activity_HabitDetails.KEY_HABITID))};
		String sortOrder = null;
		CursorLoader cursorLoader = null;
		
		if(isArchived){
			selection = View_ArchivedTargetsInfo.COLUMN_HABIT_ID + "=?";
			sortOrder = View_ArchivedTargetsInfo.COLUMN_CREATED_TIME + " desc";
			cursorLoader = new CursorLoader(getActivity(), MyContentProvider.URI_ARCHIVED_HABIT_TARGETS, Projection.archived, selection, selectionArgs, sortOrder);
			
		}
		else{
			selection = View_TargetsInfo.COLUMN_HABIT_ID + "=?";
			sortOrder = View_TargetsInfo.COLUMN_CREATED_TIME + " desc";
			cursorLoader = new CursorLoader(getActivity(), MyContentProvider.URI_HABIT_TARGETS, Projection.live, selection, selectionArgs, sortOrder);
		}
		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onLoadFinished()");}
		mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}
	
	private class TargetSummaryAdapter extends CursorAdapter{
		private LayoutInflater layoutInflater;
		final int menuId_achieve = 1;
		final int menuId_unachieve = 2;
		
		public TargetSummaryAdapter(Context context){
			super(context,null,0);
			layoutInflater = LayoutInflater.from(context);
		}
		
		private void notifyURI(){
			getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABIT_TARGETS, null);
			getActivity().getContentResolver().notifyChange(MyContentProvider.URI_HABITS, null);
		}
		
		public void showEditDescriptionDialog(final Target target){
			// initialize value to show in the dialog
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			final EditText edt_input = new EditText(getActivity());
			alert.setView(edt_input);
			alert.setTitle("Edit description");
			edt_input.setText(target.getDescription());
			
			alert.setPositiveButton("save",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					if(BuildConfig.DEBUG){Log.d(LOG_TAG,"edit : save");}
					String input = edt_input.getText().toString();
					if(!input.equals("")){
						if(!target.getDescription().equals(input)){
							target.setDescription(input);
							SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
							new TargetDaoImpl(db).update(target);
							notifyURI();
							Toast.makeText(getActivity(), "Description edited", Toast.LENGTH_LONG).show();
						}
						else Toast.makeText(getActivity(), "No changes to save", Toast.LENGTH_LONG).show();
					}
					else Toast.makeText(getActivity(),"Target name cannot be blank", Toast.LENGTH_LONG).show();
				}
			});
			alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(BuildConfig.DEBUG){Log.d(LOG_TAG,"edit : cancel");}
					
				}
			});
			alert.show();
		}
		
		
		private class PopupClickListener implements OnMenuItemClickListener{

			Target target;
			
			public PopupClickListener(Target target){
				this.target = target;
			}
			
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"menuItem.getId()=" + String.valueOf(menuItem.getItemId()));}
				boolean isSuccess = false;
				SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
				
				switch(menuItem.getItemId()){
				case R.id.action_edit_target :
					if(BuildConfig.DEBUG){Log.d(LOG_TAG,"edit" + String.valueOf(target.getId()));}
					showEditDescriptionDialog(target);
				
					break;
					
				case R.id.action_delete_target :
					if(BuildConfig.DEBUG){Log.d(LOG_TAG,"delete" + String.valueOf(target.getId()));}
					new TargetDaoImpl(db).delete(target.getId());
					notifyURI();
					Toast.makeText(getActivity(), "Target deleted", Toast.LENGTH_LONG).show();
					break;
					
				case menuId_achieve :
					isSuccess = new HabitOperationsManagerImpl(db).achieveTarget(target.getId());
					if(isSuccess){
						notifyURI();
						Toast.makeText(getActivity(), "Congragulations on achieveing your target", Toast.LENGTH_LONG).show();
					}
					
					break;
					
				case menuId_unachieve :
					isSuccess = new HabitOperationsManagerImpl(db).unachieveTarget(target.getId());
					if(isSuccess){
						notifyURI();
						Toast.makeText(getActivity(), "Target Un-achieved", Toast.LENGTH_LONG).show();
					}
					
					break;
					
				default :
					break;
				}
				
				return false;
			}
			
			
		}
		
		private class ViewHolder{
			TextView tv_description;
			TextView tv_targetSetTime;
			TextView tv_targetAchievedTime;
			TextView tv_targetDuration;
			TextView tv_targetEffort;
			TextView tv_targetCumulativeEffort;
			TextView tv_targetCumulativeDuration;
			ImageButton ibt_options;
		}
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup container) {
			final View itemLayout = layoutInflater.inflate(R.layout.item_targetsinfo, container, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.tv_description = (TextView) itemLayout.findViewById(R.id.txt_description);
			viewHolder.tv_targetSetTime = (TextView) itemLayout.findViewById(R.id.txt_targetsettime);
			viewHolder.tv_targetAchievedTime = (TextView) itemLayout.findViewById(R.id.txt_targetachievedtime);
			viewHolder.tv_targetDuration = (TextView) itemLayout.findViewById(R.id.txt_targetduration);
			viewHolder.tv_targetEffort = (TextView) itemLayout.findViewById(R.id.txt_targeteffort);
			viewHolder.tv_targetCumulativeEffort = (TextView) itemLayout.findViewById(R.id.txt_targetcumulativeeffort);
			viewHolder.tv_targetCumulativeDuration = (TextView) itemLayout.findViewById(R.id.txt_targetcumulativeduration);
			viewHolder.ibt_options = (ImageButton) itemLayout.findViewById(R.id.ibt_options);
			itemLayout.setTag(viewHolder);
			return itemLayout;
		}

		@Override
		public void bindView(View view, Context arg1, Cursor cursor) {
			
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			
			//get the values from cursor
			final long id = cursor.getLong(Projection.INDEX_COLUMN_TARGET_ID);
			final String description = cursor.getString(Projection.INDEX_COLUMN_DESCRIPTION);
			final String createdTime = cursor.getString(Projection.INDEX_COLUMN_CREATED_TIME);
			final String achievedTime = cursor.getString(Projection.INDEX_COLUMN_ACHIEVED_TIME);
			int hitCount = cursor.getInt(Projection.INDEX_COLUMN_HIT_COUNT);
			int hitDayCount = cursor.getInt(Projection.INDEX_COLUMN_HIT_DAY_COUNT);
			String habitCreatedTime = cursor.getString(Projection.INDEX_COLUMN_HABIT_CREATED_TIME);
			int hitCountFromHabitCreation = cursor.getInt(Projection.INDEX_COLUMN_HIT_COUNT_FROM_HABIT_CREATION);
			int hitDayCountFromHabitCreation = cursor.getInt(Projection.INDEX_COLUMN_HIT_DAY_COUNT_FROM_HABIT_CREATION);
			final boolean isAchieved = !cursor.isNull(Projection.INDEX_COLUMN_ACHIEVED_TIME);
			final boolean isLatest = cursor.isFirst(); 
			Date dt_habitCreatedTime = null;
			try {
				dt_habitCreatedTime = Utilities.getSimpleDateFormatUTC().parse(habitCreatedTime);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			Date dt_createdTime = null;
			try {
				dt_createdTime = Utilities.getSimpleDateFormatUTC().parse(createdTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Date dt_achievedTime = null;
			if(isAchieved){
				try {
					dt_achievedTime = Utilities.getSimpleDateFormatUTC().parse(achievedTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			final Target target = new Target(id,getActivity().getIntent().getExtras().getLong(Activity_HabitDetails.KEY_HABITID),description,dt_createdTime,dt_achievedTime);
			
			
			//set the description
			viewHolder.tv_description.setText(description);
			
			//set the created time
			viewHolder.tv_targetSetTime.setText(Utilities.getDisplayFormattedDate(dt_createdTime));
			
			//set the achieved time
			String diff=null;
			String diffSinceHabitCreation=null;
			//if NOT achieved
			if(!isAchieved){
				viewHolder.tv_targetAchievedTime.setText("-");
				
				Date now = new Date();
				diff = Utilities.getDifferenceIgnoringTime(dt_createdTime,now);
				diffSinceHabitCreation = Utilities.getDifferenceIgnoringTime(dt_habitCreatedTime,now);
				
			}
			//if achieved
			else{
				viewHolder.tv_targetAchievedTime.setText(Utilities.getDisplayFormattedDate(dt_achievedTime));
				
				diff = Utilities.getDifferenceIgnoringTime(dt_createdTime,dt_achievedTime);
				diffSinceHabitCreation = Utilities.getDifferenceIgnoringTime(dt_habitCreatedTime,dt_achievedTime);
			}
			
			
			/*set the effort views*/
			viewHolder.tv_targetDuration.setText(diff);
			viewHolder.tv_targetEffort.setText(new HitEffort(hitCount,hitDayCount).toString());
			
			/*set the cumulative effort views*/
			viewHolder.tv_targetCumulativeDuration.setText(diffSinceHabitCreation);
			viewHolder.tv_targetCumulativeEffort.setText(new HitEffort(hitCountFromHabitCreation,hitDayCountFromHabitCreation).toString());
			
			//set the onclicklistener for options button
			if(!isArchived){
				viewHolder.ibt_options.setVisibility(View.VISIBLE);
				viewHolder.ibt_options.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View view){
						PopupMenu popupMenu = new PopupMenu(getActivity(),view);
						popupMenu.getMenuInflater().inflate(R.menu.target_contextual_menu, popupMenu.getMenu());
						
						//add extra menu items
						if(!isAchieved) popupMenu.getMenu().add(Menu.NONE,menuId_achieve,Menu.NONE,"Achieve target");
						else if(isLatest) popupMenu.getMenu().add(Menu.NONE,menuId_unachieve,Menu.NONE,"Un-achieve target");
						
						//set menu click listener
						popupMenu.setOnMenuItemClickListener(new PopupClickListener(target));
						
						//show menu
						popupMenu.show();
					}
				});
				
			}
			else viewHolder.ibt_options.setVisibility(View.INVISIBLE);
			
		}

		
		
	}
	
}