package com.example.smartpolcalendar;

public class DutyDay {
    public int dayNumber;
    public String dutyType;
    public String holidayName; // ← 공휴일명 (없으면 null)


    public DutyDay(int dayNumber, String dutyType) {
        this.dayNumber = dayNumber;
        this.dutyType = dutyType;
        this.holidayName = null;
    }
    public DutyDay(int dayNumber, String dutyType, String holidayName) {
        this.dayNumber = dayNumber;
        this.dutyType = dutyType;
        this.holidayName = holidayName;
    }
}