package com.example.smartpolcalendar;

import java.util.HashMap;

public class HolidayUtils {
    public static HashMap<String, String> getHolidays() {
        HashMap<String, String> holidays = new HashMap<>();

        // ✅ 2025년 공휴일
        holidays.put("2025-01-01", "신정");
        holidays.put("2025-02-27", "설날 연휴");
        holidays.put("2025-02-28", "설날");
        holidays.put("2025-03-01", "삼일절");
        holidays.put("2025-05-01", "근로자의 날"); // 법정공휴일 아님
        holidays.put("2025-05-05", "어린이날");
        holidays.put("2025-05-06", "대체공휴일");
        holidays.put("2025-06-06", "현충일");
        holidays.put("2025-08-15", "광복절");
        holidays.put("2025-09-06", "추석 연휴");
        holidays.put("2025-09-07", "추석");
        holidays.put("2025-09-08", "추석 연휴");
        holidays.put("2025-10-03", "개천절");
        holidays.put("2025-10-09", "한글날");
        holidays.put("2025-12-25", "성탄절");

        // ✅ 2026년 공휴일
        holidays.put("2026-01-01", "신정");
        holidays.put("2026-02-16", "설날 연휴");
        holidays.put("2026-02-17", "설날");
        holidays.put("2026-02-18", "설날 연휴");
        holidays.put("2026-03-01", "삼일절");
        holidays.put("2026-05-01", "근로자의 날"); // 법정공휴일 아님
        holidays.put("2026-05-05", "어린이날");
        holidays.put("2026-05-25", "석가탄신일");
        holidays.put("2026-06-06", "현충일");
        holidays.put("2026-08-15", "광복절");
        holidays.put("2026-09-24", "추석 연휴");
        holidays.put("2026-09-25", "추석");
        holidays.put("2026-09-26", "추석 연휴");
        holidays.put("2026-10-03", "개천절");
        holidays.put("2026-10-09", "한글날");
        holidays.put("2026-12-25", "성탄절");

        return holidays;
    }
}