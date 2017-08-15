package com.cnbs.utilslibrary.calendarUtils.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;

import com.cnbs.utilslibrary.calendarUtils.adapter.CalendarAdapter;
import com.cnbs.utilslibrary.calendarUtils.adapter.MonthCalendarAdapter;
import com.cnbs.utilslibrary.calendarUtils.listener.OnClickMonthCalendarListener;
import com.cnbs.utilslibrary.calendarUtils.listener.OnClickMonthViewListener;
import com.cnbs.utilslibrary.calendarUtils.listener.OnMonthCalendarPageChangeListener;
import com.cnbs.utilslibrary.calendarUtils.utils.Utils;
import com.cnbs.utilslibrary.calendarUtils.view.CalendarView;
import com.cnbs.utilslibrary.calendarUtils.view.MonthView;

import org.joda.time.DateTime;
import org.joda.time.Months;

import java.util.List;

/**
 * Created by necer on 2017/6/12.
 * 月视图日历
 */

public class MonthCalendar extends CalendarViewPager implements OnClickMonthViewListener {


    private OnClickMonthCalendarListener onClickMonthCalendarListener;
    private OnMonthCalendarPageChangeListener onMonthCalendarPageChangeListener;


    public MonthCalendar(Context context) {
        this(context, null);
    }

    public MonthCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected CalendarAdapter getCalendarAdapter(List<String> pointList) {
        mPageSize = Months.monthsBetween(startDateTime, endDateTime).getMonths() + 1;
        mCurrPage = Months.monthsBetween(startDateTime, DateTime.now()).getMonths();
        return new MonthCalendarAdapter(getContext(), mPageSize, mCurrPage, new DateTime(), this, pointList);
    }


    @Override
    protected void initCurrentCalendarView() {
        currentView = calendarAdapter.getCalendarViews().get(getCurrentItem());
        if (onMonthCalendarPageChangeListener != null && currentView != null) {
            DateTime selectDateTime = currentView.getSelectDateTime();
            DateTime initialDateTime = currentView.getInitialDateTime();
            onMonthCalendarPageChangeListener.onMonthCalendarPageSelected(selectDateTime == null ? initialDateTime : selectDateTime);
        }
    }

    @Override
    public void onClickCurrentMonth(DateTime dateTime) {
        doClickEvent(dateTime, getCurrentItem());
    }

    @Override
    public void onClickLastMonth(DateTime dateTime) {
        int currentItem = getCurrentItem() - 1;
        doClickEvent(dateTime, currentItem);
    }

    @Override
    public void onClickNextMonth(DateTime dateTime) {
        int currentItem = getCurrentItem() + 1;
        doClickEvent(dateTime, currentItem);
    }

    @Override
    public void setDate(int year, int month, int day, boolean smoothScroll) {
        DateTime dateTime = new DateTime(year, month, day, 0, 0, 0);
        int i = jumpDate(dateTime, smoothScroll);
        MonthView monthView = (MonthView) calendarAdapter.getCalendarViews().get(i);
        if (monthView == null) {
            return;
        }
        monthView.setSelectDateTime(dateTime);
    }

    @Override
    public int jumpDate(DateTime dateTime, boolean smoothScroll) {
        SparseArray<CalendarView> calendarViews = calendarAdapter.getCalendarViews();
        if (calendarViews.size() == 0) {
            return getCurrentItem();
        }
        DateTime initialDateTime = calendarViews.get(getCurrentItem()).getInitialDateTime();
        int months = Utils.getIntervalMonths(initialDateTime, dateTime);

        int i = getCurrentItem() + months;
        setCurrentItem(i, smoothScroll);
        return i;
    }


    public void setOnClickMonthCalendarListener(OnClickMonthCalendarListener onClickMonthCalendarListener) {
        this.onClickMonthCalendarListener = onClickMonthCalendarListener;
    }

    public void setOnMonthCalendarPageChangeListener(OnMonthCalendarPageChangeListener onMonthCalendarPageChangeListener) {
        this.onMonthCalendarPageChangeListener = onMonthCalendarPageChangeListener;
    }


    private void doClickEvent(DateTime dateTime, int currentItem) {
        MonthCalendar.this.setCurrentItem(currentItem);
        MonthView monthView = (MonthView) calendarAdapter.getCalendarViews().get(currentItem);
        if (monthView == null) {
            return;
        }
        monthView.setSelectDateTime(dateTime);
        //清除其他选中
        if (!isMultiple) {
            clearSelect(monthView);
        }
        if (onClickMonthCalendarListener != null) {
            onClickMonthCalendarListener.onClickMonthCalendar(dateTime);
        }

    }
}

