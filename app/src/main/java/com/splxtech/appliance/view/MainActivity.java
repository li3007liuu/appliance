package com.splxtech.appliance.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.splxtech.appliance.Data;
import com.splxtech.appliance.R;

import com.splxtech.appliance.database.DataInit;
import com.splxtech.appliance.service.DbdaoService;
import com.splxtech.appliance.service.NetService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import greendao.Appliance;


public class MainActivity extends Activity implements View.OnClickListener{

    //控键专用
    private RadioButton   radio_Main_Dqjc;
    private RadioButton   radio_Main_Ghfx;
    private RadioButton   radio_Main_Dlcx;
    private RadioButton   radio_Main_Grzx;
    private TextView      text_Main_title;
    private Button        button_Main_Dqsjk;

    //fragment
    private DqjcFragment  fragment_Main_Dqjc;
    private GhfxFragment  fragment_Main_Ghfx;
    private DlfxFragment  fragment_Main_Dlcx;
    private GrzxFragment  fragment_Main_Grzx;
    private int selected;
    boolean sendflag;
    //数据读取专用
    //数据库
    private List<Appliance> applianceList2 = new ArrayList<Appliance>();


    //服务绑定
    private NetService.MyBinder myBinder;
    private Intent intent;
    private boolean bindfinish;

    //消息广播相关变量
    private DqjcReceiver dqjcReceiver = null;

    //定时器相关
    private final Timer timer = new Timer();
    private boolean sendctl = false;
    private TimerTask timerTask= new TimerTask() {
        @Override
        public void run() {

            if((myBinder.GetNetState()==true&&sendflag==true)&&selected==0)
            {
                if(sendctl==false)
                {
                    myBinder.GetDqljsbService();
                    sendctl = true;
                }
                else
                {
                    myBinder.GetDqLjscSerivice();
                    sendctl = false;
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        //新建数据库
        //dbService = DbService.getInstance(this);
        //dbHelper = new ApplianceListDb(this,"ApplianceStore.db",null,1);
        //applianceList2=Data.getApplianceList();
        //DataInit dataInit = new DataInit();
        //删除统计数据
        //Data.dbService.deleteAllWaste();
        //初始化统计数据
        //Data.dbService.saveAllWaste(dataInit.getWasteTypeBeanList(applianceList2.size()));
        //Data.setApplianceList(applianceList2);
        select(0);
        selected=0;
        //与服务进行绑定
        bindfinish = false;
        intent = new Intent(this,NetService.class);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
        //新建广播接收器
        dqjcReceiver = new DqjcReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.splxtech.power.service.NetService");
        registerReceiver(dqjcReceiver,intentFilter);
        //开启定时器定时发送数据
        timer.schedule(timerTask,1000,250);

    }
    @SuppressLint("SetJavaScriptEnable")
    private void setView()
    {
        text_Main_title = (TextView)findViewById(R.id.text_main_title);
        radio_Main_Dlcx = (RadioButton)findViewById(R.id.radio_main_dlcx);
        radio_Main_Dlcx.setOnClickListener(this);
        radio_Main_Dqjc = (RadioButton)findViewById(R.id.radio_main_dqjk);
        radio_Main_Dqjc.setOnClickListener(this);
        radio_Main_Ghfx = (RadioButton)findViewById(R.id.radio_main_ghfx);
        radio_Main_Ghfx.setOnClickListener(this);
        radio_Main_Grzx = (RadioButton)findViewById(R.id.radio_main_grzx);
        radio_Main_Grzx.setOnClickListener(this);
        button_Main_Dqsjk = (Button)findViewById(R.id.button_main_dqsjk);
        button_Main_Dqsjk.setOnClickListener(this);
        button_Main_Dqsjk.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    button_Main_Dqsjk.setTextColor(Color.rgb(220,221,222));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    button_Main_Dqsjk.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        imageselect(0);
    }

    private void imageselect(int i)
    {
        switch (i)
        {
            case 0:
                //text_Main_title.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                text_Main_title.setText("电器监测");
                button_Main_Dqsjk.setText("电器数据库 >");
                button_Main_Dqsjk.setEnabled(true);
                radio_Main_Dqjc.setBackgroundResource(R.drawable.dqjc1);
                radio_Main_Ghfx.setBackgroundResource(R.drawable.ghfx0);
                radio_Main_Dlcx.setBackgroundResource(R.drawable.dlcx0);
                radio_Main_Grzx.setBackgroundResource(R.drawable.grzx0);
                break;
            case 1:
                text_Main_title.setGravity(Gravity.CENTER);
                text_Main_title.setText("功耗分析");
                button_Main_Dqsjk.setText("");
                button_Main_Dqsjk.setEnabled(false);
                radio_Main_Dqjc.setBackgroundResource(R.drawable.dqjc0);
                radio_Main_Ghfx.setBackgroundResource(R.drawable.ghfx1);
                radio_Main_Dlcx.setBackgroundResource(R.drawable.dlcx0);
                radio_Main_Grzx.setBackgroundResource(R.drawable.grzx0);
                break;
            case 2:
                text_Main_title.setGravity(Gravity.CENTER);
                text_Main_title.setText("电力查询");
                button_Main_Dqsjk.setText("");
                button_Main_Dqsjk.setEnabled(false);
                radio_Main_Dqjc.setBackgroundResource(R.drawable.dqjc0);
                radio_Main_Ghfx.setBackgroundResource(R.drawable.ghfx0);
                radio_Main_Dlcx.setBackgroundResource(R.drawable.dlcx1);
                radio_Main_Grzx.setBackgroundResource(R.drawable.grzx0);
                break;
            case 3:
                text_Main_title.setGravity(Gravity.CENTER);
                text_Main_title.setText("个人中心");
                button_Main_Dqsjk.setText("");
                button_Main_Dqsjk.setEnabled(false);
                radio_Main_Dqjc.setBackgroundResource(R.drawable.dqjc0);
                radio_Main_Ghfx.setBackgroundResource(R.drawable.ghfx0);
                radio_Main_Dlcx.setBackgroundResource(R.drawable.dlcx0);
                radio_Main_Grzx.setBackgroundResource(R.drawable.grzx1);
                break;
        }
    }

    private void select(int i)
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft=fragmentManager.beginTransaction();//开启一个事务
        //隐藏碎片
        if(fragment_Main_Dqjc!=null)
        {
            ft.hide(fragment_Main_Dqjc);
        }
        if(fragment_Main_Ghfx!=null)
        {
            ft.hide(fragment_Main_Ghfx);
        }
        if(fragment_Main_Dlcx!=null)
        {
            ft.hide(fragment_Main_Dlcx);
        }
        if(fragment_Main_Grzx!=null)
        {
            ft.hide(fragment_Main_Grzx);
        }
        switch(i)
        {
            case 0:
                if(fragment_Main_Dqjc==null)
                {
                    fragment_Main_Dqjc = new DqjcFragment();
                    ft.add(R.id.frame_main_container,fragment_Main_Dqjc);
                }
                else
                {
                    ft.show(fragment_Main_Dqjc);
                }
                break;
            case 1:
                if(fragment_Main_Ghfx==null)
                {
                    fragment_Main_Ghfx = new GhfxFragment();
                    ft.add(R.id.frame_main_container,fragment_Main_Ghfx);
                }
                else
                {
                    ft.show(fragment_Main_Ghfx);
                }
                break;
            case 2:
                if(fragment_Main_Dlcx==null)
                {
                    fragment_Main_Dlcx = new DlfxFragment();
                    ft.add(R.id.frame_main_container,fragment_Main_Dlcx);
                }
                else
                {
                    ft.show(fragment_Main_Dlcx);
                }
                break;
            case 3:
                if(fragment_Main_Grzx==null)
                {
                    fragment_Main_Grzx = new GrzxFragment();
                    ft.add(R.id.frame_main_container,fragment_Main_Grzx);
                }
                else
                {
                    ft.show(fragment_Main_Grzx);
                }
                break;
        }
        ft.commit();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        sendflag=true;
    }
    @Override
    public void onPause()
    {

        sendflag=false;
        super.onPause();
    }

    @Override
    public void onStop()
    {
        applianceList2 = Data.getApplianceList();
        Data.dbService.deleteAllAppliance();
        Data.dbService.saveApplianceLists(applianceList2);
        super.onStop();

    }
    @Override
    public void onDestroy()
    {
        //主程序退出时关闭网络服务
        unregisterReceiver(dqjcReceiver);
        unbindService(serviceConnection);
        timer.cancel();
        Intent intent = new Intent(this, NetService.class);
        stopService(intent);
        intent = new Intent(this, DbdaoService.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.radio_main_dqjk:
                selected = 0;
                imageselect(0);
                select(0);
                break;
            case R.id.radio_main_ghfx:
                selected = 1;
                imageselect(1);
                select(1);
                break;
            case R.id.radio_main_dlcx:
                selected = 2;
                imageselect(2);
                select(2);
                break;
            case R.id.radio_main_grzx:
                selected = 3;
                imageselect(3);
                select(3);
                break;
            case R.id.button_main_dqsjk:
                Intent intent = new Intent(this,DqsjkListActivity.class);
                this.startActivity(intent);
                break;
        }
    }
    //服务绑定相关函数
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (NetService.MyBinder) service;
            bindfinish=true;
            if(myBinder.GetNetState()==true)
            {
                myBinder.GetDqljsbService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    //广播消息接收器相关函数
    public class DqjcReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int portnum = bundle.getInt("PORTNUM");
            int recenum = bundle.getInt("RECEDATANUM");
            int[] receData = bundle.getIntArray("RECEDATA");
            if(portnum==NetService.DQLJSBPORTNUM) {
                try {
                    Message message = new Message();
                    message.what = 0x120;
                    message.obj = bundle;
                    fragment_Main_Dqjc.mHandler.sendMessage(message);
                } catch (Exception e) {

                }
            }
            else if(portnum==NetService.DRLJSCPORTNUM)
            {
                try {
                    Message message = new Message();
                    message.what = 0x121;
                    message.obj = bundle;
                    fragment_Main_Dqjc.mHandler.sendMessage(message);
                } catch (Exception e) {

                }
            }
        }
    }
}
