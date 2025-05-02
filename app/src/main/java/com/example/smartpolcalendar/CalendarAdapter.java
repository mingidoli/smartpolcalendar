package com.example.smartpolcalendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DutyDay dutyDay = dutyList.get(position);

        if (dutyDay.dayNumber == 0) {
            holder.textDay.setText("");
            holder.textHoliday.setText("");
            holder.textDuty.setText("");
        } else {
            holder.textDay.setText(String.valueOf(dutyDay.dayNumber));

            if (dutyDay.holidayName != null) {
                holder.textHoliday.setText(dutyDay.holidayName);   // 빨간 글씨
                holder.textDuty.setText(dutyDay.dutyType);         // 검정 글씨
            } else {
                holder.textHoliday.setText("");                    // 공휴일 아님
                holder.textDuty.setText(dutyDay.dutyType);         // 검정 글씨
            }
        }
    }

    @Override
    public int getItemCount() {
        return dutyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDay;
        TextView textHoliday; // ← 반드시 선언
        TextView textDuty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDay = itemView.findViewById(R.id.textDay);
            textDuty = itemView.findViewById(R.id.textDuty);
            textHoliday = itemView.findViewById(R.id.textHoliday);

        }
    }
}