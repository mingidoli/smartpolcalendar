package com.example.smartpolcalendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class CalendarMonthAdapter extends FragmentStateAdapter {

    private final int startYear = 2025;
    private final int startMonth = 4; // 5월 (0부터 시작)
    private final int monthCount = 12;

    private final String[] weekdayPattern;
    private final String[] weekendPattern;

    public CalendarMonthAdapter(@NonNull FragmentActivity fragmentActivity,
                                String[] weekdayPattern, String[] weekendPattern) {
        super(fragmentActivity);
        this.weekdayPattern = weekdayPattern;
        this.weekendPattern = weekendPattern;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int year = startYear + (startMonth + position) / 12;
        int month = (startMonth + position) % 12;
        return CalendarMonthFragment.newInstance(year, month, weekdayPattern, weekendPattern);
    }

    @Override
    public int getItemCount() {
        return monthCount; // 예: 12개월 슬라이드
    }
}