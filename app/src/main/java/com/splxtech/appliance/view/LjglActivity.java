package com.splxtech.appliance.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.splxtech.appliance.Data;
import com.splxtech.appliance.R;
import com.splxtech.appliance.service.NetService;


/**
 * Created by li300 on 2016/7/21 0021.
 */
public class LjglActivity extends Activity implements View.OnClickListener
{
    private String serviceIP;
    private int servicePort;
    private NetService.MyBinder mbinder;
    private Intent intent;

    private boolean BinderState;
    private EditText editLjglIp;
    private EditText editLjglPort;
    private Button   buttonLjglConnect;
    private Button   buttonLjglBack;
    private CoordinatorLayout container;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mbinder = (NetService.MyBinder)service;
            mbinder.ConnectService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ljgl);
        editLjglIp = (EditText)findViewById(R.id.edit_ljgl_ip);
        editLjglPort = (EditText)findViewById(R.id.edit_ljgl_port);
        container = (CoordinatorLayout)findViewById(R.id.ljgl_container);
        buttonLjglConnect = (Button)findViewById(R.id.button_ljgl_connect);
        buttonLjglConnect.setOnClickListener(this);
        buttonLjglBack = (Button)findViewById(R.id.button_ljgl_back);
        buttonLjglBack.setText("< 个人中心");
        buttonLjglBack.setOnClickListener(this);
        buttonLjglBack.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonLjglBack.setTextColor(Color.rgb(220,221,222));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonLjglBack.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        /*SharedPreferences preferences = getSharedPreferences("netconfig", Context.MODE_PRIVATE);
        serviceIP=preferences.getString("HOST_IP", null);
        servicePort=preferences.getInt("HOST_PORT", 0);*/
        editLjglIp.setText(Data.ServiceIp);
        editLjglPort.setText(Integer.toString(Data.ServicePort));
        BinderState = false;
        intent = new Intent(this,NetService.class);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(BinderState==true) {
            unbindService(connection);
        }
    }
    @Override
    public void onClick(View view)
    {
        if(view.getId()==R.id.button_ljgl_connect)
        {
            serviceIP = editLjglIp.getText().toString();
            servicePort = Integer.parseInt(editLjglPort.getText().toString());
            SharedPreferences sharedPreferences = getSharedPreferences("netconfig", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            Data.ServiceIp = serviceIP;
            Data.ServicePort = servicePort;
            edit.putString("HOST_IP", serviceIP);
            edit.putInt("HOST_PORT", servicePort);
            edit.commit();
            bindService(intent, connection, BIND_AUTO_CREATE);
            Snackbar.make(container,"连接成功",Snackbar.LENGTH_SHORT).show();
            BinderState = true;
        }
        else if(view.getId()==R.id.button_ljgl_back)
        {
            finish();
        }
    }
}
