package com.theAlternate.lifrack;

import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import android.util.Log;
import android.widget.Chronometer;

public class ChronometerHolder {
	private WeakHashMap<Long, Chronometer> chronometerMap;
	private static final String LOG_TAG = "ChronometerHolder";

	public ChronometerHolder() {
		chronometerMap = new WeakHashMap<Long, Chronometer>();
	}

	public void add(Long id, Chronometer chronometer){
		chronometerMap.put(id, chronometer);
	}
	
	public void remove(Long id){
		chronometerMap.remove(id);
	}
	
	public int getCount(){
		return chronometerMap.size();
	}
	
	public void startAll() {
		// start any chronometers that were paused
		if (chronometerMap.size() > 0) {
			Set<Entry<Long, Chronometer>> set = chronometerMap.entrySet();
			Iterator<Entry<Long, Chronometer>> iterator = set.iterator();
			Entry<Long, Chronometer> entry;
			while (iterator.hasNext()) {
				entry = (Entry<Long, Chronometer>) iterator.next();
				entry.getValue().start();
				if (BuildConfig.DEBUG) {
					Log.d(LOG_TAG, "onResume.started id=" + String.valueOf(entry.getKey()));
				}
			}
		}
	}

	public void stopAll() {
		// stop any chronometers that might be running
		if (chronometerMap.size() > 0) {
			Set<Entry<Long, Chronometer>> set = chronometerMap.entrySet();
			Iterator<Entry<Long, Chronometer>> iterator = set.iterator();
			Entry<Long, Chronometer> entry;
			while (iterator.hasNext()) {
				entry = (Entry<Long, Chronometer>) iterator.next();
				entry.getValue().stop();
				if (BuildConfig.DEBUG) {
					Log.d(LOG_TAG, "onPause.stopped id=" + String.valueOf(entry.getKey()));
							
				}
			}
		}
	}

}