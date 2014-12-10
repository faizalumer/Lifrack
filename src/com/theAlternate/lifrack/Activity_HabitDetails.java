package com.theAlternate.lifrack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.util.Log;
import android.view.Menu;

public class Activity_HabitDetails extends ActionBarActivity{
	//public static final String ARG_OBJECT = "object"; //need to change
	public static final String LOG_TAG = "Activity_HabitDetails";
	public static final String KEY_HABITID = "com.theAlternate.lifrack.habitID";
	public static final String KEY_HABITNAME = "com.theAlternate.lifrack.habitName";
	public static final String KEY_ARCHIVED = "com.theAlternate.lifrack.archived";
	
	private FragmentCollectionPagerAdapter mFragmentCollectionPagerAdapter;
	private ViewPager mViewPager;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_habitdetails);
		
		//set the title
		boolean isArchived = getIntent().getExtras().getBoolean(KEY_ARCHIVED);
		setTitle(getIntent().getExtras().getCharSequence(KEY_HABITNAME) + (isArchived?"(Archived)":""));
		
		//display tabs in action bar
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//initialize view pager
		mFragmentCollectionPagerAdapter = new FragmentCollectionPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mFragmentCollectionPagerAdapter);
		
		//setup page change listener
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position){
				actionBar.setSelectedNavigationItem(position);
			} 
		});
		
		//setup tab listener
		TabListener tabListener = new TabListener() {
			
			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onTabSelected.tab.getPosition()=" + tab.getPosition() );}
				mViewPager.setCurrentItem(tab.getPosition());
			}
			
			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		
		actionBar.addTab(actionBar.newTab()
				.setText("Calendar")
				.setTabListener(tabListener));
		
		actionBar.addTab(actionBar.newTab()
				.setText("Hits")
				.setTabListener(tabListener));
		
		actionBar.addTab(actionBar.newTab()
				.setText("Targets")
				.setTabListener(tabListener));
		
		/*actionBar.addTab(actionBar.newTab()
				.setText("Sessions")
				.setTabListener(tabListener));*/
		
		
	}
	
	public class FragmentCollectionPagerAdapter extends FragmentStatePagerAdapter{
		public FragmentCollectionPagerAdapter(FragmentManager fm){
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getItem=" + arg0);}
			Fragment fragment = null;
			Bundle args = new Bundle();
			if(arg0 == 0){
				fragment = new Fragment_HitsCalendar(); //need to change
			}
			else if(arg0 == 1){
				fragment = new Fragment_Hits();
			}
			else if(arg0 == 2){
				fragment = new Fragment_TargetsInfo();
			}
			
			fragment.setArguments(args);
			return fragment;
			
		}

		@Override
		public int getCount() {
			//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getCount");}
			return 3; //need to change
		}
		
		/*@Override
		public CharSequence getPageTitle(int position){
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getPageTitle");}
			return "OBJECT" + (position + 1); //need to change
		}*/
	}
}