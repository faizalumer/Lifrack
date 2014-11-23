package com.theAlternate.lifrack;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_About extends ActionBarActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setTitle("About");
		
		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		//set the app version text
		((TextView)findViewById(R.id.txt_appversion)).setText(packageInfo.versionName);
		
		//set the play store button
		((TextView)findViewById(R.id.txt_viewplaystore)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				String packageName = getPackageName();
				try{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
				}catch(android.content.ActivityNotFoundException e){
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/details?id=" + packageName)));
				}
			}
		});
		
		//set the donate button
		((LinearLayout)findViewById(R.id.ll_donate)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.wikimediafoundation.org/wiki/Ways_to_Give")));
			}
		});
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater.inflate()
	}*/
}