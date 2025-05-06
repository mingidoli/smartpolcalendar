package com.example.smartpolcalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PATTERN = 1001;

    private TextView textMonthYear;
    private Button btnSetPattern, btnCompareDuty;
    private ViewPager2 viewPagerCalendar;
    private SharedPreferences prefs;

    private String[] weekdayPattern;
    private String[] weekendPattern;
    private String[] cyclePattern;
    private String baseDate;
    private String patternType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textMonthYear = findViewById(R.id.textMonthYear);
        btnSetPattern = findViewById(R.id.btnSetPattern);
        btnCompareDuty = findViewById(R.id.btnCompareDuty); // 새로운 버튼
        viewPagerCalendar = findViewById(R.id.viewPagerCalendar);
        prefs = getSharedPreferences("DutyPrefs", MODE_PRIVATE);

        loadSavedPatterns();

        CalendarMonthAdapter adapter = new CalendarMonthAdapter(
                this, patternType, weekdayPattern, weekendPattern, cyclePattern, baseDate);
        viewPagerCalendar.setAdapter(adapter);

        viewPagerCalendar.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                int year = 2025 + (4 + position) / 12;
                int month = (4 + position) % 12 + 1;
                textMonthYear.setText(year + "년 " + month + "월");
            }
        });

        textMonthYear.setText("2025년 5월");

        btnSetPattern.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DutyPatternActivity.class);
            startActivityForResult(intent, REQUEST_PATTERN);
        });

        btnCompareDuty.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DutyCompareInputActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PATTERN && resultCode == RESULT_OK && data != null) {
            SharedPreferences.Editor editor = prefs.edit();
            patternType = data.getStringExtra("patternType");

            if ("weekday".equals(patternType)) {
                weekdayPattern = data.getStringArrayExtra("weekdayPattern");
                weekendPattern = data.getStringArrayExtra("weekendPattern");
                cyclePattern = null;
                baseDate = null;

                editor.putString("patternType", "weekday");
                for (int i = 0; i < 5; i++) editor.putString("weekday_" + i, weekdayPattern[i]);
                for (int i = 0; i < 2; i++) editor.putString("weekend_" + i, weekendPattern[i]);

            } else {
                String cycle = data.getStringExtra("cyclePattern");
                baseDate = data.getStringExtra("baseDate");
                cyclePattern = cycle.split(",");

                weekdayPattern = null;
                weekendPattern = null;

                editor.putString("patternType", "cycle");
                editor.putString("baseDate", baseDate);
                editor.putInt("cycleLength", cyclePattern.length);
                for (int i = 0; i < cyclePattern.length; i++) {
                    editor.putString("cycle_" + i, cyclePattern[i].trim());
                }
            }

            editor.apply();

            CalendarMonthAdapter adapter = new CalendarMonthAdapter(
                    this, patternType, weekdayPattern, weekendPattern, cyclePattern, baseDate);
            viewPagerCalendar.setAdapter(adapter);
            viewPagerCalendar.setCurrentItem(0, false);
        }
    }

    private void loadSavedPatterns() {
        patternType = prefs.getString("patternType", "weekday");

        if ("weekday".equals(patternType)) {
            weekdayPattern = new String[5];
            weekendPattern = new String[2];
            for (int i = 0; i < 5; i++) weekdayPattern[i] = prefs.getString("weekday_" + i, "주");
            for (int i = 0; i < 2; i++) weekendPattern[i] = prefs.getString("weekend_" + i, "휴");
        } else {
            baseDate = prefs.getString("baseDate", null);
            int len = prefs.getInt("cycleLength", 0);
            cyclePattern = new String[len];
            for (int i = 0; i < len; i++) {
                cyclePattern[i] = prefs.getString("cycle_" + i, "주");
            }
        }
    }
}