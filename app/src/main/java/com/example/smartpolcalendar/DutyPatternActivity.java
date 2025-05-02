package com.example.smartpolcalendar; // 패키지명은 프로젝트에 맞게 수정하세요

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DutyPatternActivity extends AppCompatActivity {

    Spinner spMon, spTue, spWed, spThu, spFri, spSat, spSun;
    Button btnSavePattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_pattern);

        // Spinner 연결
        spMon = findViewById(R.id.spMon);
        spTue = findViewById(R.id.spTue);
        spWed = findViewById(R.id.spWed);
        spThu = findViewById(R.id.spThu);
        spFri = findViewById(R.id.spFri);
        spSat = findViewById(R.id.spSat);
        spSun = findViewById(R.id.spSun);
        btnSavePattern = findViewById(R.id.btnSavePattern);

        // Spinner 어댑터 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.duty_options, // res/values/strings.xml 에 정의되어 있어야 함
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 스피너에 어댑터 연결
        spMon.setAdapter(adapter);
        spTue.setAdapter(adapter);
        spWed.setAdapter(adapter);
        spThu.setAdapter(adapter);
        spFri.setAdapter(adapter);
        spSat.setAdapter(adapter);
        spSun.setAdapter(adapter);

        // 저장 버튼 클릭
        btnSavePattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] weekdayPattern = {
                        spMon.getSelectedItem().toString(),
                        spTue.getSelectedItem().toString(),
                        spWed.getSelectedItem().toString(),
                        spThu.getSelectedItem().toString(),
                        spFri.getSelectedItem().toString()
                };
                String[] weekendPattern = {
                        spSat.getSelectedItem().toString(),
                        spSun.getSelectedItem().toString()
                };

                // 결과 Intent에 패턴 담기
                Intent intent = new Intent();
                intent.putExtra("weekdayPattern", weekdayPattern);
                intent.putExtra("weekendPattern", weekendPattern);
                setResult(RESULT_OK, intent);

                Toast.makeText(DutyPatternActivity.this, "패턴이 저장되었습니다!", Toast.LENGTH_SHORT).show();

                finish(); // MainActivity로 복귀
            }
        });
    }
}