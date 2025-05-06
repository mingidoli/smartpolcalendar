package com.example.smartpolcalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.*;

public class DutyCompareInputActivity extends AppCompatActivity {

    private LinearLayout layoutGroupList;
    private Button btnAddGroup, btnCompare, btnSave, btnLoad;
    private final List<View> groupViews = new ArrayList<>();
    private final List<DutyGroup> dutyGroups = new ArrayList<>();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_compare_input);

        layoutGroupList = findViewById(R.id.layoutGroupList);
        btnAddGroup = findViewById(R.id.btnAddGroup);
        btnCompare = findViewById(R.id.btnCompare);
        btnSave = findViewById(R.id.btnSaveGroups);
        btnLoad = findViewById(R.id.btnLoadGroups);
        prefs = getSharedPreferences("ComparePrefs", Context.MODE_PRIVATE);

        // 첫 조 초기화
        addGroupView("A조");

        btnAddGroup.setOnClickListener(v -> {
            String nextGroup = String.valueOf((char) ('A' + groupViews.size())) + "조";
            addGroupView(nextGroup);
        });

        btnSave.setOnClickListener(v -> {
            saveGroupsToPrefs();
            Toast.makeText(this, "근무조 저장 완료", Toast.LENGTH_SHORT).show();
        });

        btnLoad.setOnClickListener(v -> {
            loadGroupsFromPrefs();
            Toast.makeText(this, "저장된 근무조 불러옴", Toast.LENGTH_SHORT).show();
        });

        btnCompare.setOnClickListener(v -> {
            dutyGroups.clear();

            for (View groupView : groupViews) {
                EditText etName = groupView.findViewById(R.id.etGroupName);
                EditText etPattern = groupView.findViewById(R.id.etPattern);
                EditText etBaseDate = groupView.findViewById(R.id.etBaseDate);

                String name = etName.getText().toString().trim();
                String patternRaw = etPattern.getText().toString().trim();
                String baseDate = etBaseDate.getText().toString().trim();

                if (name.isEmpty() || patternRaw.isEmpty() || baseDate.isEmpty()) {
                    Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] pattern = patternRaw.split(",");
                dutyGroups.add(new DutyGroup(name, pattern, baseDate));
            }

            Intent intent = new Intent(this, DutyCompareResultActivity.class);
            intent.putExtra("dutyGroups", new ArrayList<>(dutyGroups));
            startActivity(intent);
        });
    }

    private void addGroupView(String groupName) {
        View view = getLayoutInflater().inflate(R.layout.item_group_input, null);
        EditText etName = view.findViewById(R.id.etGroupName);
        EditText etBaseDate = view.findViewById(R.id.etBaseDate);

        etName.setText(groupName);
        etBaseDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(new Date()));

        layoutGroupList.addView(view);
        groupViews.add(view);
    }

    private void saveGroupsToPrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("groupCount", groupViews.size());

        for (int i = 0; i < groupViews.size(); i++) {
            View view = groupViews.get(i);
            EditText etName = view.findViewById(R.id.etGroupName);
            EditText etPattern = view.findViewById(R.id.etPattern);
            EditText etBaseDate = view.findViewById(R.id.etBaseDate);

            editor.putString("name_" + i, etName.getText().toString().trim());
            editor.putString("pattern_" + i, etPattern.getText().toString().trim());
            editor.putString("baseDate_" + i, etBaseDate.getText().toString().trim());
        }

        editor.apply();
    }

    private void loadGroupsFromPrefs() {
        layoutGroupList.removeAllViews();
        groupViews.clear();

        int count = prefs.getInt("groupCount", 0);
        for (int i = 0; i < count; i++) {
            View view = getLayoutInflater().inflate(R.layout.item_group_input, null);

            EditText etName = view.findViewById(R.id.etGroupName);
            EditText etPattern = view.findViewById(R.id.etPattern);
            EditText etBaseDate = view.findViewById(R.id.etBaseDate);

            etName.setText(prefs.getString("name_" + i, ""));
            etPattern.setText(prefs.getString("pattern_" + i, ""));
            etBaseDate.setText(prefs.getString("baseDate_" + i, ""));

            layoutGroupList.addView(view);
            groupViews.add(view);
        }
    }
}