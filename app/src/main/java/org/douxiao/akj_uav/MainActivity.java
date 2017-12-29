package org.douxiao.akj_uav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    ImageButton start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) { //隐去标题栏
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        findViewById(R.id.Sys_setting).setOnClickListener(this); //进入setting界面
        findViewById(R.id.start).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Sys_setting:
                Intent intent = new Intent(MainActivity.this, UAV_Setting.class);
                startActivity(intent);
                break;
            case R.id.start:
                Intent intent1 = new Intent(MainActivity.this, speechControl.class);
                startActivity(intent1);
                finish();
                System.exit(0);
                break;
            default:
                break;
        }

    }

}
