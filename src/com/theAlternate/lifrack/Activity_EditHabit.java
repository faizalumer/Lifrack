package com.theAlternate.lifrack;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class Activity_EditHabit extends ActionBarActivity{
	public static final String KEY_HABITID = "habitId";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edithabit);
		setTitle("Edit tracker");
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater.inflate()
	}*/
}