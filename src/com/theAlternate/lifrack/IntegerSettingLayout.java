package com.theAlternate.lifrack;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public abstract class IntegerSettingLayout extends LinearLayout implements
		OnClickListener {

	private static final String LOG_TAG = "IntegerSettingLayout";
	private TextView tv_title;
	private TextView tv_status;
	private Integer mValue;
	private Context mContext;
	private String dialogTitle="" ;

	@Override
	public void onClick(View view) {
		if(BuildConfig.DEBUG) {Log.d(LOG_TAG,"onClick");}
		final Dialog dialog = new Dialog(mContext);
		//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setTitle(dialogTitle);
		dialog.setContentView(R.layout.dialog_numberpicker);
		Button btn_set = (Button) dialog.findViewById(R.id.btn_set);
		final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.np_numberpicker);
		np.setMinValue(1);
		np.setMaxValue(50);
		np.setWrapSelectorWheel(true);
		np.setValue(getValue()==null ? np.getMinValue() : getValue());
		btn_set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setValue(np.getValue());
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	abstract String getStatus();

	public IntegerSettingLayout(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "started constructor");
		}
		
		mContext = context;
		
		//inflate
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.setting, this, true);
		setOrientation(LinearLayout.VERTICAL);
		
		tv_title = (TextView) findViewById(R.id.title);
		tv_status = (TextView) findViewById(R.id.status);
		
		setOnClickListener(this);
		
		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "end constructor");
		}
	}

	protected void setTitle(String title) {
		tv_title.setText(title);
	}

	public void setValue(Integer value) {
		this.mValue = value;
		tv_status.setText(getStatus());
	}

	public Integer getValue() {
		return mValue;
	}
	
	protected void setDialogTitle(String dialogTitle){
		this.dialogTitle = dialogTitle;
	}

}
