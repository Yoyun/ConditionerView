package com.yoyun.conditionerexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yoyun.conditionerview.ConditionerView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ConditionerView condView1;
    ConditionerView condView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        condView1 = (ConditionerView) findViewById(R.id.condView1);
        condView1.setOnProgressChangedListener(new ConditionerView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(ConditionerView view, float progress) {
                Log.d("condView1", "progress：" + progress);
            }
        });
        condView2 = (ConditionerView) findViewById(R.id.condView2);
        condView2.setOnProgressChangedListener(new ConditionerView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(ConditionerView view, float progress) {
                Log.d("condView2", "progress：" + progress);
            }
        });
    }
}
