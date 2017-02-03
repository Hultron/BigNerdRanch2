package com.hultron.bignerdranch2;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends PickerDialogFragment {
  private TimePicker mTimePicker;

  public static TimePickerFragment newInstance(Date date) {
    Bundle args = getArgs(date);
    TimePickerFragment fragment = new TimePickerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override protected View initLayout() {
    View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

    mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
    mTimePicker.setIs24HourView(false);
    setTime(mCalendar.get(Calendar.HOUR_OF_DAY),mCalendar.get(Calendar.MINUTE));
    return v;
  }

  @Override protected Date getDate() {
    //TimePicker只用来设置时间，日期保持不变
    int year = mCalendar.get(Calendar.YEAR);
    int month = mCalendar.get(Calendar.MONTH);
    int day = mCalendar.get(Calendar.DAY_OF_MONTH);


    return new GregorianCalendar(year,month,day,getHour(),getMinute()).getTime();
  }

  /*setTime用于给TimePicker设置时间*/
  private void setTime(int hour, int minute) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      mTimePicker.setHour(hour);
      mTimePicker.setMinute(minute);
    }
    else {
      mTimePicker.setCurrentHour(hour);
      mTimePicker.setCurrentMinute(minute);
    }
  }

  /*getHour和getMinute用于从TimePicker获取时间*/
  private int getHour() {
    int hour;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
     hour = mTimePicker.getHour();
    }
    else {
      hour = mTimePicker.getCurrentHour();
    }
    return hour;
  }

  private int getMinute() {
    int minute;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      minute = mTimePicker.getMinute();
    }
    else {
      minute = mTimePicker.getCurrentMinute();
    }
    return minute;
  }
}
