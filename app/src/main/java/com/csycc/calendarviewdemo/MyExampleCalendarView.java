package com.csycc.calendarviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.csycc.calendarview.CalendarView;
import android.widget.TextView;

/**
 * Created by zg on 16/9/5.
 */
public class MyExampleCalendarView extends CalendarView {
    public MyExampleCalendarView(Context context) {
        super(context);
    }

    public MyExampleCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyExampleCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public View createItemView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.view_calendar_item, parent, false);
    }

    @Override
    public void setDate(View itemVew, int year, int month, int day, boolean isSelectMonth) {
        TextView textView = ((TextView) itemVew);
        textView.setText("" + day);

        if (isSelectMonth) {
            textView.setTextColor(0xff000000);
        } else {
            textView.setTextColor(0xff999999);
        }
    }

    private String[] weekArr;

    @Override
    public View createTitleView(ViewGroup parent, int week) {
        if (null == weekArr) {
            weekArr = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        }

        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_calendar_item, parent, false);
        textView.setText("星期" + weekArr[week]);
        return textView;
    }
}
