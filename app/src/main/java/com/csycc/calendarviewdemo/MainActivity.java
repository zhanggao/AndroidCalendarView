package com.csycc.calendarviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.csycc.calendarview.CalendarView;

import java.util.Calendar;

public class MainActivity extends Activity {

    private TextView prevMonthText, dateDetailText, nextMonthText;
    private MyExampleCalendarView myCalendarView;

    private int year, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prevMonthText = (TextView) findViewById(R.id.prev_month);
        dateDetailText = (TextView) findViewById(R.id.date_detail);
        nextMonthText = (TextView) findViewById(R.id.next_month);
        myCalendarView = (MyExampleCalendarView) findViewById(R.id.calender_view);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        refreshView();

        prevMonthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month--;
                if (month < 0) {
                    month = 11;
                    year--;
                }
                refreshView();
            }
        });

        nextMonthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month++;
                if (month > 11) {
                    month = 0;
                    year++;
                }
                refreshView();
            }
        });

        myCalendarView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onClick(int year, int month, int day) {
                // 月份是 0 -- 11
                Toast.makeText(MainActivity.this, year + "-" + (month + 1) + "-" + day, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshView() {
        // 月份是 0 -- 11
        dateDetailText.setText(year + "-" + (month + 1));
        myCalendarView.setDate(year, month);
    }

}
