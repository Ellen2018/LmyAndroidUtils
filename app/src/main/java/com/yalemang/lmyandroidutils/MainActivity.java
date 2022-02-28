package com.yalemang.lmyandroidutils;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yalemang.library.android.viewutils.ViewUtils;

import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        findViewById(R.id.tv2).setOnClickListener(v -> {
            ViewUtils.activityViewTree(this);
        });
    }
}