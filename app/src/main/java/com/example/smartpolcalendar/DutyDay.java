package com.example.smartpolcalendar;

public class DutyDay {
    public int dayNumber;
    public String dutyType;
    public String holidayName; // 공휴일 이름 (null 가능)
    public boolean isToday;    // 오늘 날짜 여부
    public int dayOfWeek;      // 1(일) ~ 7(토)

    // 전체 필드를 받는 생성자
    public DutyDay(int dayNumber, String dutyType, String holidayName, boolean isToday, int dayOfWeek) {
        this.dayNumber = dayNumber;
        this.dutyType = dutyType;
        this.holidayName = holidayName;
        this.isToday = isToday;
        this.dayOfWeek = dayOfWeek;
    }

    // 기존 방식: 최소 정보만 (빈 셀 등)
    public DutyDay(int dayNumber, String dutyType) {
        this(dayNumber, dutyType, null, false, 0);
    }

    // 기존 방식: 공휴일만 포함
    public DutyDay(int dayNumber, String dutyType, String holidayName) {
        this(dayNumber, dutyType, holidayName, false, 0);
    }

    // 기존 방식: 공휴일 + 오늘
    public DutyDay(int dayNumber, String dutyType, String holidayName, boolean isToday) {
        this(dayNumber, dutyType, holidayName, isToday, 0);
    }
}