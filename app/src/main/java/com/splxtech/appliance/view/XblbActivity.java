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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import com.splxtech.appliance.R;
import com.splxtech.appliance.adapter.XbcsAdapter;
import com.splxtech.appliance.model.XbcsClass;
import com.splxtech.appliance.service.NetService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by li300 on 2016/7/30 0030.
 */
public class XblbActivity extends Activity implements View.OnClickListener {

    //服务绑定
    private NetService.MyBinder myBinder;
    private Intent intent;

    //消息广播相关变量
    private XblbReceiver xblbReceiver = null;

    //控件变量
    private Button buttonXblbBack;
    private ListView listView;
    private RadioButton radioDllb;
    private RadioButton radioDylb;

    private boolean disctl;
    private List<XbcsClass> xbcsClassList = new ArrayList<XbcsClass>();
    private XbcsAdapter xbcsAdapter;

    // 定时器
    private final Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (myBinder.GetNetState() == true) {
                //获得电压电流波形
                myBinder.GetXblbSerivice();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xblb);
        //初始化进去显示电压谐波列表
        disctl = true;
        radioDllb = (RadioButton) findViewById(R.id.radio_xblb_dlxb);
        radioDllb.setOnClickListener(this);
        radioDylb = (RadioButton)findViewById(R.id.radio_xblb_dyxb);
        radioDylb.setOnClickListener(this);
        buttonXblbBack = (Button)findViewById(R.id.button_xblb_back);
        buttonXblbBack.setText("< 电力查询");
        buttonXblbBack.setOnClickListener(this);
        buttonXblbBack.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonXblbBack.setTextColor(Color.rgb(220,221,222));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonXblbBack.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        initXblblist();
        xbcsAdapter = new XbcsAdapter(this,R.layout.item_xblb_xbdis,xbcsClassList);
        listView = (ListView)findViewById(R.id.list_xblb_dis);
        listView.setAdapter(xbcsAdapter);

        //服务绑定
        intent = new Intent(this,NetService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);

        //注册广播接收器
        xblbReceiver = new XblbReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.splxtech.power.service.NetService");
        this.registerReceiver(xblbReceiver,intentFilter);
        //开启定时器定时发送数据
        timer.schedule(timerTask,1000,250);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        this.unregisterReceiver(xblbReceiver);
        unbindService(serviceConnection);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_xblb_back:
                finish();
                break;
            case R.id.radio_xblb_dlxb:
                disctl = false;
                break;
            case R.id.radio_xblb_dyxb:
                disctl = true;
                break;

        }

    }

    private void initXblblist()
    {
        for(int i=0;i<40;i++)
        {
            xbcsClassList.add(new XbcsClass(i+1,0,0,0));
        }
    }

    //服务绑定相关函数
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (NetService.MyBinder) service;
            if (myBinder.GetNetState() == true) {
                //获得电压电流波形
                myBinder.GetXblbSerivice();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    //广播消息接收器相关函数
    public class XblbReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle bundle = intent.getExtras();
            int portnum = bundle.getInt("PORTNUM");
            int recenum = bundle.getInt("RECEDATANUM");
            int[] receData = bundle.getIntArray("RECEDATA");
            if(portnum == NetService.XBLBPORTNUM&&recenum==160)
            {
                int first;
                if(disctl==false)
                {
                    first=80;
                }
                else
                {
                    first=0;
                }
                float fdata;
                float dlfz;
                float xwj;
                double temp1=Math.log10((float)(receData[first]));
                for(int i=0;i<40;i++)
                {
                    dlfz = ((float) receData[first+i])/100;
                    fdata = (float) (Math.log10((float) (receData[i+first])) / temp1 * 100);
                    xwj = (float) receData[first+40];
                    xbcsClassList.get(i).setXbfz(dlfz);
                    xbcsClassList.get(i).setXbbfb(fdata);
                    xbcsClassList.get(i).setXbxwj(xwj);
                }
                xbcsAdapter.notifyDataSetChanged();
            }
        }
    }
}
