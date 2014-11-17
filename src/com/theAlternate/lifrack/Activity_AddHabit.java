package com.theAlternate.lifrack;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class Activity_AddHabit extends ActionBarActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addhabit);
		setTitle("Add new tracker");
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater.inflate()
	}*/
}