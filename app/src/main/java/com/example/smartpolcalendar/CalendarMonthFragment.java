package com.example.smartpolcalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CalendarMonthFragment extends Fragment {

    private int year;
    private int month; // 0부터 시작 (ex: 4 = 5월)
    private String[] weekdayPattern;
    private String[] weekendPattern;

    public static CalendarMonthFragment newInstance(int year, int month, String[] weekday, String[] weekend) {
        CalendarMonthFragment fragment = new CalendarMonthFragment();
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putStringArray("weekdayPattern", weekday);
        args.putStringArray("weekendPattern", weekend);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_month, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerMonth);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));

        if (getArguments() != null) {
            year = getArguments().getInt("year");
            month = getArguments().getInt("month");
            weekdayPattern = getArguments().getStringArray("weekdayPattern");
            weekendPattern = getArguments().getStringArray("weekendPattern");
        }

        List<DutyDay> dutyList = generateMonthlyDuties(year, month, weekdayPattern, weekendPattern);
        recyclerView.setAdapter(new CalendarAdapter(dutyList));

        return view;
    }

    private List<DutyDay> generateMonthlyDuties(int year, int month, String[] weekdayPattern, String[] weekendPattern) {
        List<DutyDay> list = new ArrayList<>();
        HashMap<String, String> holidays = HolidayUtils.getHolidays();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);

        int startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 1=일~7=토
        int emptyCells = startDayOfWeek - 1;

        // 1. 빈 칸 추가
        for (int i = 0; i < emptyCells; i++) {
            list.add(new DutyDay(0, "")); // 빈 셀
        }

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 2. 실제 날짜 생성
        for (int day = 1; day <= maxDay; day++) {
            cal.set(year, month, day);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            String duty;
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                duty = weekendPattern[dayOfWeek == Calendar.SATURDAY ? 0 : 1];
            } else {
                int index = (day - 1) % weekdayPattern.length;
                duty = weekdayPattern[index];
            }

            String dateKey = sdf.format(cal.getTime());
            String holidayName = holidays.get(dateKey); // 공휴일 이름이 있으면 표시

            list.add(new DutyDay(day, duty, holidayName));
        }

        return list;
    }
}