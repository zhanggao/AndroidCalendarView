package com.csycc.calendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Created by zg on 16/9/2.
 */
public abstract class CalendarView extends ViewGroup {

    private final String TAG = "CalendarView---";

    private CalendarItemHolder[] itemViewArr;
    private int year = 2016;
    private int month = 8;
    private OnItemClickListener onItemClickListener;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

        refreshDate();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    public abstract View createItemView(ViewGroup parent);

    public abstract void setDate(View itemVew, int year, int month, int day, boolean isSelectMonth);

    /**
     * @param parent
     * @param week   0 -- 6 0表示星期日
     * @return
     */
    public abstract View createTitleView(ViewGroup parent, int week);

    /**
     * @param year
     * @param month 0 -- 11
     */
    public void setDate(int year, int month) {
        this.year = year;
        this.month = month;
        refreshDate();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void init() {
        for (int i = 0; i < 7; i++) {
            addView(createTitleView(this, i));
        }

        itemViewArr = new CalendarItemHolder[6 * 7];
        for (int i = 0; i < itemViewArr.length; i++) {
            itemViewArr[i] = new CalendarItemHolder(createItemView(this));
            addView(itemViewArr[i].getItemView());
            final int finalI = i;
            itemViewArr[i].getItemView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        CalendarItemHolder holder = itemViewArr[finalI];
                        onItemClickListener.onClick(holder.getYear(), holder.getMonth(), holder.getDay());
                    }
                }
            });
        }
    }

    private void refreshDate() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, 1);
        int thisWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.roll(Calendar.DATE, -1);
        int thisMonthDays = calendar.get(Calendar.DATE);

        calendar.set(year, month - 1, 1);
        calendar.roll(Calendar.DATE, -1);
        int lastMonthDays = calendar.get(Calendar.DATE);

        Log.e(TAG, "this " + thisMonthDays + " last " + lastMonthDays + " week " + thisWeek);

        int itemYear, itemDay, itemMonth;
        for (int i = 0; i < itemViewArr.length; i++) {
            itemYear = year;
            if (i < thisWeek) {
                itemDay = lastMonthDays + i + 1 - thisWeek;
                itemMonth = month - 1;
                if (itemMonth < 0) {
                    itemMonth = 11;
                    itemYear = year - 1;
                }
            } else if (i < thisWeek + thisMonthDays) {
                itemDay = i + 1 - thisWeek;
                itemMonth = month;
            } else {
                itemDay = i + 1 - thisWeek - thisMonthDays;
                itemMonth = month + 1;
                if (itemMonth > 11) {
                    itemMonth = 0;
                    itemYear = year + 1;
                }
            }
            itemViewArr[i].setDate(itemYear, itemMonth, itemDay);
            setDate(itemViewArr[i].getItemView(), itemYear, itemMonth, itemDay, itemMonth == month);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        for (int i = 0, len = getChildCount(); i < len; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }

        int maxWidth = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            Log.e(TAG, "W EXACTLY");
            maxWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            Log.e(TAG, "W AT_MOST");
            View child;
            int maxWidthTitle = 0;
            for (int i = 0; i < 7; i++) {
                child = getChildAt(i);
                maxWidthTitle += child.getMeasuredWidth();
            }
            int maxWidthContent = 0;
            for (int i = 7; i < 14; i++) {
                child = getChildAt(i);
                maxWidthContent += child.getMeasuredWidth();
            }
            maxWidth = Math.max(maxWidthTitle, maxWidthContent);

            maxWidth = maxWidth < widthSize ? maxWidth : widthSize;

        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            Log.e(TAG, "W UNSPECIFIED");
            maxWidth = widthSize;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int maxHeight = 0;
        if (heightMode == MeasureSpec.EXACTLY) {
            Log.e(TAG, "H EXACTLY");
            maxHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            Log.e(TAG, "H AT_MOST");
            View child;
            child = getChildAt(0);
            maxHeight = child.getMeasuredHeight();
            child = getChildAt(7);
            maxHeight = maxHeight + child.getMeasuredHeight() * 6;

            maxHeight = maxHeight < heightSize ? maxHeight : heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            Log.e(TAG, "H UNSPECIFIED");
            maxHeight = heightSize;
        }

        Log.e(TAG, "maxW " + maxWidth + " maxH " + maxHeight);

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int childCount = getChildCount();

        final int childWidth = (right - left - getPaddingLeft() - getPaddingRight()) / 7;
        final int paddingLeft = getPaddingLeft();

        View child;
        int height = getPaddingTop();
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);

            int curWidth = child.getMeasuredWidth();
            int curHeight = child.getMeasuredHeight();

            if (i % 7 == 0 && i >= 7) {
                height = height + curHeight;
            }
            int childLeft = paddingLeft + childWidth * (i % 7);

//            Log.e(TAG, "i " + i + " curWidth " + curWidth + " curHeight " + curHeight);
//            Log.e(TAG, "cL " + childLeft + " cH " + height);

            child.getLayoutParams().width = childWidth;
            child.layout(childLeft, height, childLeft + childWidth, height + curHeight);
        }
    }

    public interface OnItemClickListener {
        /**
         * @param year
         * @param month 0 -- 11
         * @param day   0 -- 31
         */
        void onClick(int year, int month, int day);
    }

    private static class CalendarItemHolder {

        private View itemView;
        private int year;
        private int month;
        private int day;

        public CalendarItemHolder(View itemView) {
            this.itemView = itemView;
        }

        public View getItemView() {
            return itemView;
        }

        public void setDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public int getDay() {
            return day;
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }
    }
}
