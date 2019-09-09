package com.neil.trafficrestrictionwidget;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private SharedPreferences sharedPreferences;

    private static final int STYLE_MODE_LIGHT=0;
    private static final int STYLE_MODE_DARK=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("style",0);
        int mode = sharedPreferences.getInt("mode", 0);
        if(mode==STYLE_MODE_LIGHT){
            RadioButton radioButton=findViewById(R.id.light_style_btn);
            radioButton.setChecked(true);
        }else if(mode==STYLE_MODE_DARK){
            RadioButton radioButton=findViewById(R.id.dark_style_btn);
            radioButton.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.light_style_btn:
                saveStyleMode(STYLE_MODE_LIGHT);
                break;
            case R.id.dark_style_btn:
                saveStyleMode(STYLE_MODE_DARK);
                break;
        }
        AppWidgetUtil.sendBroadcast(this,"com.neil.traffic.changeStyleMode",TrafficRestrictionAppWidget.class);
    }

    private void saveStyleMode(int mode){
        sharedPreferences.edit().putInt("mode",mode).apply();
    }
}
