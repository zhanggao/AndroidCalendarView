# AndroidCalendarView
可高度定制样式的安卓日历控件。

    // 需要重写三个方法
    // 实例化日历的每个itemView，自定义样式
    @Override
    public View createItemView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.view_calendar_item, parent, false);
    }
    
    // 
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
    // 日历顶部 星期几 的view
    @Override
    public View createTitleView(ViewGroup parent, int week) {
        if (null == weekArr) {
            weekArr = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        }

        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_calendar_item, parent, false);
        textView.setText("星期" + weekArr[week]);
        return textView;
    }
