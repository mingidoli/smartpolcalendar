package com.example.smartpolcalendar;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.*;

public class DutyCompareResultActivity extends AppCompatActivity {

    private TableLayout tableResult;
    private TextView textCommonDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_compare_result);

        tableResult = findViewById(R.id.tableResult);
        textCommonDays = findViewById(R.id.textCommonDays);

        ArrayList<DutyGroup> dutyGroups = (ArrayList<DutyGroup>) getIntent().getSerializableExtra("dutyGroups");
        if (dutyGroups == null || dutyGroups.isEmpty()) return;

        // 기준일자 (첫 번째 조 기준)
        String baseDateStr = dutyGroups.get(0).getBaseDate();
        Calendar baseCal = Calendar.getInstance();
        try {
            baseCal.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(baseDateStr));
        } catch (Exception e) {
            Toast.makeText(this, "기준일 형식 오류", Toast.LENGTH_SHORT).show();
            return;
        }

        int year1 = baseCal.get(Calendar.YEAR);
        int month1 = baseCal.get(Calendar.MONTH); // 0-indexed
        int maxDay1 = getMaxDayOfMonth(year1, month1);

        int year2 = (month1 == 11) ? year1 + 1 : year1;
        int month2 = (month1 + 1) % 12;
        int maxDay2 = getMaxDayOfMonth(year2, month2);

        List<List<String>> matrixMonth1 = new ArrayList<>();
        List<List<String>> matrixMonth2 = new ArrayList<>();
        List<String> groupNames = new ArrayList<>();

        for (DutyGroup group : dutyGroups) {
            groupNames.add(group.getGroupName());
            matrixMonth1.add(generateDutiesForMonth(group, year1, month1, maxDay1));
            matrixMonth2.add(generateDutiesForMonth(group, year2, month2, maxDay2));
        }

        addMonthTable("[" + (month1 + 1) + "월]", year1, month1, maxDay1, groupNames, matrixMonth1);
        addMonthTable("[" + (month2 + 1) + "월]", year2, month2, maxDay2, groupNames, matrixMonth2);

        // ✅ 두 달 공통 비번일 모두 계산
        List<Integer> commonDays1 = getCommonOffDays(matrixMonth1, maxDay1);
        List<Integer> commonDays2 = getCommonOffDays(matrixMonth2, maxDay2);

        // ✅ 하단 출력
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(month1 + 1).append("월] 공통 비번일: ")
                .append(commonDays1.isEmpty() ? "없음" : formatDayList(commonDays1))
                .append("\n[").append(month2 + 1).append("월] 공통 비번일: ")
                .append(commonDays2.isEmpty() ? "없음" : formatDayList(commonDays2));

        textCommonDays.setText(sb.toString());
    }

    private void addMonthTable(String title, int year, int month, int maxDay,
                               List<String> groupNames, List<List<String>> matrix) {
        // 월 제목
        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextSize(16);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setPadding(0, 24, 0, 8);
        tableResult.addView(titleView);

        // 요일 행
        TableRow dayOfWeekRow = new TableRow(this);
        dayOfWeekRow.addView(makeCell("요일", true, 0xFF000000));
        for (int d = 1; d <= maxDay; d++) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, d);
            String weekdayStr = new SimpleDateFormat("E", Locale.KOREA).format(cal.getTime());
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int color = (dayOfWeek == Calendar.SATURDAY) ? 0xFF0000FF :
                    (dayOfWeek == Calendar.SUNDAY) ? 0xFFFF0000 : 0xFF000000;
            dayOfWeekRow.addView(makeCell(weekdayStr, true, color));
        }
        tableResult.addView(dayOfWeekRow);

        // 날짜 헤더
        TableRow header = new TableRow(this);
        header.addView(makeCell("조/일", true, 0xFF000000));
        for (int i = 1; i <= maxDay; i++) {
            header.addView(makeCell(String.valueOf(i), true, 0xFF444444));
        }
        tableResult.addView(header);

        // 근무 패턴 행
        for (int i = 0; i < matrix.size(); i++) {
            TableRow row = new TableRow(this);
            row.addView(makeCell(groupNames.get(i), true, 0xFF0066CC));
            for (String duty : matrix.get(i)) {
                row.addView(makeCell(duty, false, 0xFF333333));
            }
            tableResult.addView(row);
        }
    }

    private List<String> generateDutiesForMonth(DutyGroup group, int year, int month, int maxDay) {
        List<String> duties = new ArrayList<>();
        String[] pattern = group.getPattern();

        Calendar base = Calendar.getInstance();
        try {
            base.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(group.getBaseDate()));
        } catch (Exception e) {
            base.set(year, month, 1);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        int diff = daysBetween(base, cal);

        for (int i = 0; i < maxDay; i++) {
            int index = ((diff + i) % pattern.length + pattern.length) % pattern.length;
            duties.add(pattern[index]);
        }

        return duties;
    }

    private List<Integer> getCommonOffDays(List<List<String>> matrix, int maxDay) {
        List<Integer> result = new ArrayList<>();
        for (int d = 0; d < maxDay; d++) {
            boolean allOff = true;
            for (List<String> group : matrix) {
                String duty = group.get(d);
                if (!duty.equals("비") && !duty.equals("휴")) {
                    allOff = false;
                    break;
                }
            }
            if (allOff) result.add(d + 1);
        }
        return result;
    }

    private String formatDayList(List<Integer> days) {
        return days.toString().replaceAll("[\\[\\]]", "");
    }

    private int getMaxDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private int daysBetween(Calendar start, Calendar end) {
        long diff = end.getTimeInMillis() - start.getTimeInMillis();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    private TextView makeCell(String text, boolean bold, int textColor) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(10, 6, 10, 6);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(13);
        tv.setTextColor(textColor);
        if (bold) tv.setTypeface(null, Typeface.BOLD);
        return tv;
    }
}