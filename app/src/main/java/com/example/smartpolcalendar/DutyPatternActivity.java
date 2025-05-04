package com.example.smartpolcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DutyPatternActivity extends AppCompatActivity {

    private RadioGroup radioPatternType;
    private LinearLayout layoutWeekday, layoutCycle;
    private Spinner spMon, spTue, spWed, spThu, spFri, spSat, spSun;
    private EditText etCycleInput;
    private Button btnSavePattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_pattern);

        radioPatternType = findViewById(R.id.radioPatternType);
        layoutWeekday = findViewById(R.id.layoutWeekday);
        layoutCycle = findViewById(R.id.layoutCycle);
        etCycleInput = findViewById(R.id.etCycleInput);
        btnSavePattern = findViewById(R.id.btnSavePattern);

        spMon = findViewById(R.id.spMon);
        spTue = findViewById(R.id.spTue);
        spWed = findViewById(R.id.spWed);
        spThu = findViewById(R.id.spThu);
        spFri = findViewById(R.id.spFri);
        spSat = findViewById(R.id.spSat);
        spSun = findViewById(R.id.spSun);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.duty_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spMon.setAdapter(adapter); spTue.setAdapter(adapter); spWed.setAdapter(adapter);
        spThu.setAdapter(adapter); spFri.setAdapter(adapter); spSat.setAdapter(adapter); spSun.setAdapter(adapter);

        radioPatternType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioWeekday) {
                layoutWeekday.setVisibility(View.VISIBLE);
                layoutCycle.setVisibility(View.GONE);
            } else {
                layoutWeekday.setVisibility(View.GONE);
                layoutCycle.setVisibility(View.VISIBLE);
            }
        });

        btnSavePattern.setOnClickListener(v -> {
            Intent intent = new Intent();
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(new Date());

            if (radioPatternType.getCheckedRadioButtonId() == R.id.radioWeekday) {
                String[] weekday = {
                        spMon.getSelectedItem().toString(),
                        spTue.getSelectedItem().toString(),
                        spWed.getSelectedItem().toString(),
                        spThu.getSelectedItem().toString(),
                        spFri.getSelectedItem().toString()
                };
                String[] weekend = {
                        spSat.getSelectedItem().toString(),
                        spSun.getSelectedItem().toString()
                };
                intent.putExtra("patternType", "weekday");
                intent.putExtra("weekdayPattern", weekday);
                intent.putExtra("weekendPattern", weekend);
            } else {
                String cycle = etCycleInput.getText().toString().trim();
                intent.putExtra("patternType", "cycle");
                intent.putExtra("cyclePattern", cycle);
                intent.putExtra("baseDate", today); // 기준일 저장
            }

            setResult(RESULT_OK, intent);
            finish();
        });
    }
}