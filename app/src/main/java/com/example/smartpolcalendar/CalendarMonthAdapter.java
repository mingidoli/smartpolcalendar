package com.example.smartpolcalendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CalendarMonthAdapter extends FragmentStateAdapter {

    private final String[] weekdayPattern;
    private final String[] weekendPattern;
    private final String[] cyclePattern;
    private final String baseDate;
    private final String patternType;

    public CalendarMonthAdapter(@NonNull FragmentActivity fa, String patternType,
                                String[] weekday, String[] weekend, String[] cycle, String baseDate) {
        super(fa);
        this.patternType = patternType;
        this.weekdayPattern = weekday;
        this.weekendPattern = weekend;
        this.cyclePattern = cycle;
        this.baseDate = baseDate;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int year = 2025 + (4 + position) / 12;
        int month = (4 + position) % 12;
        return CalendarMonthFragment.newInstance(year, month, patternType,
                weekdayPattern, weekendPattern, cyclePattern, baseDate);
    }

    @Override
    public int getItemCount() {
        return 24;
    }
}