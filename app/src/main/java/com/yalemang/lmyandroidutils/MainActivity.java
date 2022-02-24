package com.yalemang.lmyandroidutils;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.yalemang.library.android.ViewUtils;
import com.yalemang.lmyandroidutils.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        int hierarchy = ViewUtils.getViewHierarchy(tv).getXmlViewHierarchy();
        Log.d("Ellen2018","tv视图的层级为:"+hierarchy);
        ViewUtils.viewTree(tv);
    }
}