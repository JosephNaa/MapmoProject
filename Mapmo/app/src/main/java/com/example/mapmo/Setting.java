package com.example.mapmo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class Setting extends AppCompatActivity {
    private Switch serviceSwitch;
    private RadioButton rad_50, rad_100, rad_300, rad_500,rad_1000 ;
    private RadioGroup radiogroup;

    public int radius=0;
    DBHandler dbHandler = DBHandler.open(this);

    public int id;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        serviceSwitch = (Switch) findViewById(R.id.serviceSwitch);


        if (ExampleService.serviceStart == true)
        {
            serviceSwitch.setChecked(true);
        }
        else
        {
            serviceSwitch.setChecked(false);
        }
        serviceSwitch.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String input = "MAPMO가 실행중입니다";
                    Intent serviceIntent = new Intent(Setting.this,ExampleService.class);
                    serviceIntent.putExtra("inputExtra",input);
                    startService(serviceIntent);
                }
                else{
                    ExampleService.serviceStart = false;
                    Intent serviceIntent = new Intent(Setting.this,ExampleService.class);
                    stopService(serviceIntent);
                }
            }
        }));
        rad_50 = (RadioButton)findViewById(R.id.radio50);
        rad_100 = (RadioButton)findViewById(R.id.radio100);
        rad_300 = (RadioButton)findViewById(R.id.radio300);
        rad_500 = (RadioButton)findViewById(R.id.radio500);
        rad_1000 = (RadioButton)findViewById(R.id.radio1000);

        //라디오 그룹 설정
        radiogroup = (RadioGroup) findViewById(R.id.radioGroup);

        Cursor cursorRadius = dbHandler.select_radius();
        cursorRadius.moveToLast();
        id = cursorRadius.getInt(0);
        radius = cursorRadius.getInt(1);
        if (radius == 50)
        {
            rad_50.setChecked(true);
        }
        else if (radius == 100)
        {
            rad_100.setChecked(true);
        }
        else if (radius == 300)
        {
            rad_300.setChecked(true);
        }
        else if (radius == 500)
        {
            rad_500.setChecked(true);
        }
        else if (radius == 1000)
        {
            rad_1000.setChecked(true);
        }

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio50:

                        dbHandler.update_radius(id,50);
                        Log.i("dsdsds","sdsdsd");
                        //Toast.makeText(Setting.this, "50미터로 설정", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio100:
                        dbHandler.update_radius(id,100);
                        //Toast.makeText(Setting.this, "100미터로 설정", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio300:
                        dbHandler.update_radius(id,300);
                        //Toast.makeText(Setting.this, "300미터로 설정", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio500:
                        dbHandler.update_radius(id,500);
                        //Toast.makeText(Setting.this, "500미터로 설정", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio1000:
                        dbHandler.update_radius(id,1000);
                        //Toast.makeText(Setting.this, "1000미터로 설정", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });




        dbHandler.close();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }

}