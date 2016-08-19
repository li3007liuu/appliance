package com.splxtech.appliance.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.splxtech.appliance.Data;
import com.splxtech.appliance.R;
import com.splxtech.appliance.service.NetService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by li300 on 2016/7/23 0023.
 */
public class DlcsActivity extends Activity implements View.OnClickListener
{
    private NetService.MyBinder myBinder;
    private Intent intent;

    private DlcsReceiver dlcsReceiver = null;

    private TextView textDlcsDyyxz;
    private TextView textDlcsDlyxz;
    private TextView textDlcsPl;
    private TextView textDlcsXwj;
    private TextView textDlcsYggl;
    private TextView textDlcsWggl;
    private TextView textDlcsGlys;
    private Button   buttonDlcsBack;

    private final Timer timer = new Timer();
    private TimerTask timerTask= new TimerTask() {
        @Override
        public void run() {
            if(myBinder.GetNetState()==true) {
                myBinder.GetDlcsService();
            }
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlcs);
        textDlcsDyyxz = (TextView)findViewById(R.id.text_dlcs_dyyxz);
        textDlcsDlyxz = (TextView)findViewById(R.id.text_dlcs_dlyxz);
        textDlcsPl = (TextView)findViewById(R.id.text_dlcs_pl);
        textDlcsXwj = (TextView)findViewById(R.id.text_dlcs_xwj);
        textDlcsYggl = (TextView)findViewById(R.id.text_dlcs_yggl);
        textDlcsWggl = (TextView)findViewById(R.id.text_dlcs_wggl);
        textDlcsGlys = (TextView)findViewById(R.id.text_dlcs_glys);
        buttonDlcsBack = (Button)findViewById(R.id.button_dlcs_back);
        buttonDlcsBack.setOnClickListener(this);
        buttonDlcsBack.setText("< 电力查询");
        buttonDlcsBack.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonDlcsBack.setTextColor(Color.rgb(220,221,222));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonDlcsBack.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        intent = new Intent(this,NetService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        //注册广播接收器
        dlcsReceiver = new DlcsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.splxtech.power.service.NetService");
        DlcsActivity.this.registerReceiver(dlcsReceiver,intentFilter);
        //开启定时器定时发送数据
        timer.schedule(timerTask,1000,200);

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        timer.cancel();
        DlcsActivity.this.unregisterReceiver(dlcsReceiver);
        unbindService(serviceConnection);
    }
    @Override
    public void onClick(View view)
    {
        finish();
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (NetService.MyBinder)service;
            if(myBinder.GetNetState()==true)
            {
                myBinder.GetDlcsService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public class DlcsReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context,Intent intent)
        {
            Bundle bundle = intent.getExtras();
            int portnum = bundle.getInt("PORTNUM");
            int recenum = bundle.getInt("RECEDATANUM");
            int[] receData = bundle.getIntArray("RECEDATA");
            float dyyxz = ((float)receData[0])/100;
            float dlyxz = ((float)receData[1])/1000;
            float pl = int_pl(receData[2]);
            float xwj = ((float)receData[3])/100;
            float yggl = ((float)receData[4])/1000;
            float wggl = ((float)receData[5])/1000;
            float glys = ((float)receData[6])/10000;

            if(portnum==NetService.DLCSPORTNUM)
            {
                textDlcsDyyxz.setText(dyyxz+"");
                textDlcsDlyxz.setText(dlyxz+"");
                textDlcsPl.setText(pl+"");
                textDlcsXwj.setText(xwj+"");
                textDlcsYggl.setText(yggl+"");
                textDlcsWggl.setText(wggl+"");
                textDlcsGlys.setText(glys+"");
            }
        }
    }

    public float int_pl(int temp)
    {
        //short plshort = (short)temp;
        //低位
        //byte plbyte1 = (byte)(plshort&0xff);
        //高位
        //byte plbyte2 = (byte)((plshort>>>8)&0xff);
        float plfloat = 65535+temp;//plbyte2*256+plbyte1;
        return plfloat/1000;
    }
}
