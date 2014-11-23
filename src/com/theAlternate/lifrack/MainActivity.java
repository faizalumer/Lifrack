package com.theAlternate.lifrack;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	private static final String LOG_TAG = "MainActivity";
	public static final String KEY_NOTIFICATION_ID = "notificationId";
	
	private FragmentCollectionPagerAdapter mFragmentCollectionPagerAdapter;
	private ViewPager mViewPager; 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_habitdetails, menu);
		return super.onCreateOptionsMenu(menu);
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_About:
			Intent intent = new Intent(this,Activity_About.class);
			startActivity(intent);
			break;

		default:
			
			break;
		}
		return super.onOptionsItemSelected(item);	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onCreate()");}
		setContentView(R.layout.activity_main);
		dismissNotification(getIntent());
		
		//display tabs in action bar
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//initialize view pager
		mFragmentCollectionPagerAdapter = new FragmentCollectionPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pgr_habitlist);
		mViewPager.setAdapter(mFragmentCollectionPagerAdapter);
		
		//setup page change listener
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position){
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		//setup tab listener
		TabListener tabListener = new TabListener(){

			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction arg1) {
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		actionBar.addTab(actionBar.newTab()
				.setText("Live")
				.setTabListener(tabListener)
				);
		
		actionBar.addTab(actionBar.newTab()
				.setText("Archived")
				.setTabListener(tabListener)
				);
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onResume()");}
		
	}
	
	@Override
	public void onNewIntent(Intent newIntent){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onNewIntent()");}
		dismissNotification(newIntent);
	}
	
	//dismiss notification if opened from a reminder notification
	public void dismissNotification(Intent intent){
		if(intent.getAction().equals(AppService.ACTION_REMINDED)){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				int notificationId = bundle.getInt(KEY_NOTIFICATION_ID);
				if(notificationId != 0){
					if(BuildConfig.DEBUG){Log.d(LOG_TAG,"cancelling notification id=" + notificationId);}
					NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					notificationManager.cancel(notificationId);
				}
			}
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
	
	/*private void startFragment(){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"startFragment");}
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment_HabitList fragment_HabitList = new Fragment_HabitList();
		fragmentTransaction.add(R.id.rl_activity_main_container, fragment_HabitList);
		fragmentTransaction.commit();
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"startFragment : done");}
	}*/
	
public class FragmentCollectionPagerAdapter extends FragmentStatePagerAdapter{

	public FragmentCollectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getItem=" + position);}
		Fragment fragment = null;
		Bundle args = new Bundle();
		if(position == 0){
			fragment = new Fragment_HabitList();
		}
		if(position == 1){
			fragment = new Fragment_ArchivedHabitList();
		}
		return fragment;
	}

	@Override
	public int getCount() {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getCount");}
		return 2;
	}
	
}

}
