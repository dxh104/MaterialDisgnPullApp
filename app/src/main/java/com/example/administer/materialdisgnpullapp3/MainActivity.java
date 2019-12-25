package com.example.administer.materialdisgnpullapp3;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administer.materialdisgnpullapp3.adapter.TextRecycleViewAdapter;
import com.example.administer.materialdisgnpullapp3.entity.TextData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XHD on 2019/11/20
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {//沉浸状态栏
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getSupportActionBar().hide();
        initView();
    }

    private void initView() {
        rv = (RecyclerView) findViewById(R.id.rv);
        List<TextData> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new TextData("" + i));
        }
        TextRecycleViewAdapter textRecycleViewAdapter = new TextRecycleViewAdapter(list);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(textRecycleViewAdapter);
    }
}
