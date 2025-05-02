package com.example.smartpolcalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PATTERN = 1001;

    private TextView textMonthYear;
    private Button btnSetPattern;
    private ViewPager2 viewPagerCalendar;

    private SharedPreferences prefs;
    private static final String PREF_NAME = "DutyPrefs";

    private String[] weekdayPattern = {"주", "주", "주", "주", "야"};
    private String[] weekendPattern = {"휴", "휴"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View 연결
        viewPagerCalendar = findViewById(R.id.viewPagerCalendar);
        textMonthYear = findViewById(R.id.textMonthYear);
        btnSetPattern = findViewById(R.id.btnSetPattern);

        // SharedPreferences 초기화 및 패턴 불러오기
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        loadSavedPatterns();

        // ViewPager 어댑터 설정
        CalendarMonthAdapter adapter = new CalendarMonthAdapter(this, weekdayPattern, weekendPattern);
        viewPagerCalendar.setAdapter(adapter);

        // 월 표시
        viewPagerCalendar.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int year = 2025 + (4 + position) / 12;
                int month = (4 + position) % 12 + 1;
                textMonthYear.setText(year + "년 " + month + "월");
            }
        });

        // 초기 월 표시 (첫 페이지는 2025년 5월)
        textMonthYear.setText("2025년 5월");

        // 패턴 설정 버튼 클릭
        btnSetPattern.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DutyPatternActivity.class);
            startActivityForResult(intent, REQUEST_PATTERN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PATTERN && resultCode == RESULT_OK && data != null) {
            weekdayPattern = data.getStringArrayExtra("weekdayPattern");
            weekendPattern = data.getStringArrayExtra("weekendPattern");

            // SharedPreferences 저장
            SharedPreferences.Editor editor = prefs.edit();
            for (int i = 0; i < weekdayPattern.length; i++) {
                editor.putString("weekday_" + i, weekdayPattern[i]);
            }
            for (int i = 0; i < weekendPattern.length; i++) {
                editor.putString("weekend_" + i, weekendPattern[i]);
            }
            editor.apply();

            // ViewPager 갱신 (재설정 필요)
            CalendarMonthAdapter adapter = new CalendarMonthAdapter(this, weekdayPattern, weekendPattern);
            viewPagerCalendar.setAdapter(adapter);
            viewPagerCalendar.setCurrentItem(0, false); // 다시 5월부터 보기
        }
    }

    private void loadSavedPatterns() {
        weekdayPattern = new String[5];
        weekendPattern = new String[2];

        for (int i = 0; i < 5; i++) {
            weekdayPattern[i] = prefs.getString("weekday_" + i, "주");
        }
        for (int i = 0; i < 2; i++) {
            weekendPattern[i] = prefs.getString("weekend_" + i, "휴");
        }

        if (weekdayPattern.length < 5 || weekendPattern.length < 2) {
            weekdayPattern = new String[]{"주", "주", "주", "주", "야"};
            weekendPattern = new String[]{"휴", "휴"};
        }
    }
}