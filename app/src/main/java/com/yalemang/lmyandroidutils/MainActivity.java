package com.yalemang.lmyandroidutils;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yalemang.library.android.viewutils.ViewTreeDialog;
import com.yalemang.library.android.viewutils.ViewUtils;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView tv;
    private Button tv2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);
        tv2.setOnClickListener(v -> {
            ViewUtils.viewTree(tv2);
        });
    }
}