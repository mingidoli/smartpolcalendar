package com.example.smartpolcalendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import java.util.Calendar;


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private final List<DutyDay> dutyList;

    public CalendarAdapter(List<DutyDay> dutyList) {
        this.dutyList = dutyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_day, parent, false);

        int totalHeight = parent.getMeasuredHeight(); // RecyclerView 높이
        int rowCount = 6; // 최대 6주
        int cellHeight = totalHeight / rowCount;

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                cellHeight
        );
        view.setLayoutParams(params);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DutyDay dutyDay = dutyList.get(position);

        if (dutyDay.dayNumber == 0) {
            // 빈 칸 처리
            holder.textDay.setText("");
            holder.textHoliday.setText("");
            holder.textDuty.setText("");

            holder.itemView.setBackgroundColor(Color.WHITE);

            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if (params != null) {
                params.height = 0;
                holder.itemView.setLayoutParams(params);
            }

            return;
        }

        holder.itemView.setPadding(0, 0, 0, 0);

        // 날짜 표시
        holder.textDay.setText(String.valueOf(dutyDay.dayNumber));

        // 기본 색 설정
        holder.textDay.setTextColor(Color.BLACK);
        holder.textDuty.setTextColor(Color.DKGRAY);
        holder.textHoliday.setText("");

        // 공휴일 처리
        if (dutyDay.holidayName != null) {
            holder.textHoliday.setText(dutyDay.holidayName);
            holder.textDay.setTextColor(Color.RED);
            holder.textHoliday.setTextColor(Color.RED);
            holder.textDuty.setText(dutyDay.dutyType);
            holder.textDuty.setTextColor(Color.BLACK);
        } else {
            // 공휴일이 아닌 경우: 요일에 따라 색상 분기
            if (dutyDay.dayOfWeek == Calendar.SATURDAY) {
                holder.textDay.setTextColor(Color.parseColor("#1976D2")); // 파란색
            } else if (dutyDay.dayOfWeek == Calendar.SUNDAY) {
                holder.textDay.setTextColor(Color.RED); // 일요일도 빨간색
            } else {
                holder.textDay.setTextColor(Color.BLACK);
            }
            holder.textDuty.setText(dutyDay.dutyType);
        }

        // 오늘 날짜 배경 강조
        if (dutyDay.isToday) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E0F2FF")); // 연파랑
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return dutyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDay;
        TextView textHoliday;
        TextView textDuty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDay = itemView.findViewById(R.id.textDay);
            textHoliday = itemView.findViewById(R.id.textHoliday);
            textDuty = itemView.findViewById(R.id.textDuty);
        }
    }
}