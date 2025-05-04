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
import java.util.*;

public class CalendarMonthFragment extends Fragment {

    private int year;
    private int month;
    private String[] weekdayPattern;
    private String[] weekendPattern;
    private String[] cyclePattern;
    private String baseDate;
    private String patternType;

    public static CalendarMonthFragment newInstance(int year, int month,
                                                    String patternType,
                                                    String[] weekday,
                                                    String[] weekend,
                                                    String[] cycle,
                                                    String baseDate) {
        CalendarMonthFragment fragment = new CalendarMonthFragment();
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putStringArray("weekdayPattern", weekday);
        args.putStringArray("weekendPattern", weekend);
        args.putStringArray("cyclePattern", cycle);
        args.putString("baseDate", baseDate);
        args.putString("patternType", patternType);
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
            cyclePattern = getArguments().getStringArray("cyclePattern");
            baseDate = getArguments().getString("baseDate");
            patternType = getArguments().getString("patternType");
        }

        List<DutyDay> dutyList = generateMonthlyDuties(year, month);
        recyclerView.setAdapter(new CalendarAdapter(dutyList));
        return view;
    }

    private List<DutyDay> generateMonthlyDuties(int year, int month) {
        List<DutyDay> list = new ArrayList<>();
        HashMap<String, String> holidays = HolidayUtils.getHolidays();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);

        int emptyCells = cal.get(Calendar.DAY_OF_WEEK) - 1;
        for (int i = 0; i < emptyCells; i++) list.add(new DutyDay(0, ""));

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar today = Calendar.getInstance();

        for (int day = 1; day <= maxDay; day++) {
            cal.set(year, month, day);
            int dow = cal.get(Calendar.DAY_OF_WEEK);
            String dateKey = sdf.format(cal.getTime());
            String holiday = holidays.get(dateKey);
            boolean isToday = year == today.get(Calendar.YEAR)
                    && month == today.get(Calendar.MONTH)
                    && day == today.get(Calendar.DAY_OF_MONTH);

            String duty;

            if ("weekday".equals(patternType)) {
                if (dow == Calendar.SATURDAY || dow == Calendar.SUNDAY) {
                    duty = weekendPattern[dow == Calendar.SATURDAY ? 0 : 1];
                } else {
                    int index = (day - 1) % weekdayPattern.length;
                    duty = weekdayPattern[index];
                }
            } else {
                // 기준일 차이 계산
                try {
                    Calendar base = Calendar.getInstance();
                    base.setTime(sdf.parse(baseDate));
                    long diff = (cal.getTimeInMillis() - base.getTimeInMillis()) / (1000 * 60 * 60 * 24);
                    int idx = (int) (diff % cyclePattern.length);
                    if (idx < 0) idx += cyclePattern.length;
                    duty = cyclePattern[idx];
                } catch (Exception e) {
                    duty = "";
                }
            }

            DutyDay dutyDay = new DutyDay(day, duty, holiday, isToday);
            dutyDay.dayOfWeek = dow;
            list.add(dutyDay);
        }

        return list;
    }
}