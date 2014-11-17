package com.theAlternate.lifrack;

import java.text.ParseException;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TargetLayout extends FrameLayout {

	private static final String LOG_TAG = "TargetLayout";
	private ImageButton btn_setTarget;
	private LinearLayout ll_target;
	private TextView tv_target;
	private TextView tv_targetEffort;
	private ImageButton btn_achieveTarget;
	private ImageButton btn_abandonTarget;
	private TextView tv_targetCreated;
	
	private String description;
	
	private final Context context;
	
	public OnActionListener onActionListener;

	public TargetLayout(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.context = context;

		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "started constructor");
		}

		// inflate the layout file
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_target, this, true);
		btn_setTarget = (ImageButton) view.findViewById(R.id.btn_set_target);
		ll_target = (LinearLayout) view.findViewById(R.id.ll_target);
		tv_target = (TextView) view.findViewById(R.id.txt_target);
		tv_targetEffort = (TextView) view.findViewById(R.id.txt_target_effort);
		btn_achieveTarget = (ImageButton) view.findViewById(R.id.btn_achieve_target);
		btn_abandonTarget = (ImageButton) view.findViewById(R.id.btn_abandon_target);
		tv_targetCreated = (TextView) view.findViewById(R.id.txt_target_created);
	
		//onclicklisteners
		btn_setTarget.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (BuildConfig.DEBUG) {Log.d(LOG_TAG, "onClick : set");}
				// initialize value to show in the dialog
				AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
				final EditText edt_input = new EditText(getContext());
				alert.setView(edt_input);
				alert.setTitle("Set a target");
				
				alert.setPositiveButton("save",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if(BuildConfig.DEBUG){Log.d(LOG_TAG,"edit : save");}
						String input = edt_input.getText().toString();
						if(!input.equals("")){
							setDescription(input);
							onActionListener.onSet(input);
							}
						else Toast.makeText(getContext(), "Cannot create blank target", Toast.LENGTH_LONG).show();
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
		});
		
		btn_achieveTarget.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (BuildConfig.DEBUG) {Log.d(LOG_TAG, "onClick : onAchieve()");}
				onActionListener.onAchieve();
				clearValues();
			}
		});
		
		btn_abandonTarget.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (BuildConfig.DEBUG) {Log.d(LOG_TAG, "onClick : onAbandon()");}
				onActionListener.onAbandon();
				clearValues();
			}
		});
		
		clearValues();
	
		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "end constructor");
		}
	
	}

	public interface OnActionListener {
		public void onAchieve();
		public void onAbandon();
		public void onSet(String description);
	}

	public void setOnActionListener(OnActionListener onActionListener) {
		this.onActionListener = onActionListener;
	}

	private void setDescription(String description) {
		this.description = description;
		tv_target.setText(this.description);
	}
	
	//method to show the provided data
	public void setValues(String description, HitEffort hitEffort, Date createdTime ){
		setDescription(description);
		tv_targetEffort.setText(hitEffort.toString());
		
		//set target creation date
		Spannable spn = new SpannableString("target set " + Utilities.getDisplayFormattedDate(createdTime));
		spn.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 11, spn.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tv_targetCreated.setText(spn);
		
		ll_target.setVisibility(View.VISIBLE);
		btn_setTarget.setVisibility(View.GONE);
	}
	
	//method to clear all fields and show button for setting new target
	public void clearValues(){
		setDescription(null);
		ll_target.setVisibility(View.GONE);
		btn_setTarget.setVisibility(View.VISIBLE);
	}
	
}
