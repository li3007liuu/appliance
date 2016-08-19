package com.splxtech.appliance.view;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.widget.TextView;

import com.splxtech.appliance.R;
import com.splxtech.appliance.model.MessageStart;
import com.splxtech.appliance.service.DbdaoService;
import com.splxtech.appliance.service.NetService;


public class StartActivity extends Activity {

    private TextView textStartVersion;
    private TextView textStartCompany;

    private MessageStart messageStart;

    private Runnable delaystart_run;
    private final Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setupView();
        //启动网络服务
        Intent intent =new Intent(this, NetService.class);
        startService(intent);
        //启动后台数据库操作服务
        intent = new Intent(this, DbdaoService.class);
        startService(intent);

        delaystart_run = new Runnable() {
            @Override
            public void run() {
                Intent startIntent = new Intent(StartActivity.this,MainActivity.class);
                StartActivity.this.startActivity(startIntent);
                StartActivity.this.finish();
            }
        };
        new Handler().postDelayed(delaystart_run,3000);

    }

    private void setupView() {
        messageStart = new MessageStart();
        messageStart.setMessagestart(this);
        textStartVersion = (TextView) findViewById(R.id.text_start_ver);
        textStartVersion.setText(messageStart.getverison());
        textStartCompany = (TextView) findViewById(R.id.text_start_company);
        textStartCompany.setText(messageStart.getCompany());
    }
}
