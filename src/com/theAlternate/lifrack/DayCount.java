package com.theAlternate.lifrack;

import android.os.Parcel;
import android.os.Parcelable;

public class DayCount implements Parcelable{
	private int day,count;

	public int getDay() {
		return day;
	}
	
	public int getCount() {
		return count;
	}

	public DayCount(int day, int count) {
		this.day = day;
		this.count = count;
	}

	public DayCount(Parcel source) {
		this.day = source.readInt();
		this.count = source.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(day);
		dest.writeInt(count);
	}
	
	public static final Parcelable.Creator<DayCount> CREATOR = new Parcelable.Creator<DayCount>() {

		@Override
		public DayCount createFromParcel(Parcel source) {
			return new DayCount(source);
		}

		@Override
		public DayCount[] newArray(int size) {
			return new DayCount[size];
		}
		
		
	};
}