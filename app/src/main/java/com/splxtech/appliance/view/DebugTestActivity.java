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
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.splxtech.appliance.Edit.CustomTextWatcher;
import com.splxtech.appliance.R;
import com.splxtech.appliance.service.NetService;

/**
 * Created by li300 on 2016/8/10 0010.
 */
public class DebugTestActivity extends Activity implements View.OnClickListener {

    private NetService.MyBinder myBinder;
    private Intent intent;

    private DebugTestReceiver debugTestReceiver=null;

    private Button buttonBack;
    private Button buttonSend;
    private TextView textReceiver;
    private EditText editSend;
    private String textDis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_test);
        editSend = (EditText) findViewById(R.id.edit_tszs_senddata);
        editSend.addTextChangedListener(new CustomTextWatcher(editSend));
        textReceiver = (TextView) findViewById(R.id.text_tszs_recedis);
        textReceiver.setMovementMethod(ScrollingMovementMethod.getInstance());
        buttonBack = (Button) findViewById(R.id.button_tszs_back);
        buttonBack.setText("< 个人中心");
        buttonBack.setOnClickListener(this);
        buttonBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonBack.setTextColor(Color.rgb(220, 221, 222));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    buttonBack.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        buttonSend = (Button) findViewById(R.id.button_tszs_send);
        buttonSend.setOnClickListener(this);

        intent = new Intent(this,NetService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        //注册广播接收器
        debugTestReceiver = new DebugTestReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.splxtech.power.service.NetService");
        DebugTestActivity.this.registerReceiver(debugTestReceiver,intentFilter);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_tszs_back:
                finish();
                break;
            case R.id.button_tszs_send:
                char[] cont = editSend.getText().toString().toCharArray();
                byte[] aa=textTobyte(cont,editSend.length());
                //发送debug命令
                myBinder.SendDebugCmd(aa);
                /*String sss="";
                for(int i=0;i<aa.length;i++)
                {
                    sss=sss+aa[i]+"\t";
                }
                textReceiver.setText(editSend.getText() + "\n" + editSend.length()+"\n"+aa.length+"\n"+sss);*/
                break;
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        DebugTestActivity.this.unregisterReceiver(debugTestReceiver);
        unbindService(serviceConnection);
    }

    private byte[] textTobyte(char[] chars, int num)
    {
        byte[] temp1;
        if(num%3==2)
        {
            temp1=new byte[num/3+1];
            for(int i=0;i<(num/3+1);i++)
            {
                temp1[i]=char2ToInt(chars[i*3],chars[i*3+1]);
            }
        }
        else
        {
            temp1=new byte[num/3];
            for(int i=0;i<(num/3);i++)
            {
                temp1[i]=char2ToInt(chars[i*3],chars[i*3+1]);
            }
        }
        return temp1;
    }
    private byte char2ToInt(char char1,char char2)
    {
        int i1,i2,i3;
        if (char1 >= 48 && char1 <= 57)
        {
                    /* 0-9*/
            i1 = char1-48;
        }
        else if (char1 >= 65 && char1 <= 70)
        {
                    /* A-F*/
            i1 = char1-65+10;
        }
        else if (char1 >= 97 && char1 <= 102)
        {
            i1 = char1-97+10;
        }
        else
        {
            i1=0;
        }
        if (char2 >= 48 && char2 <= 57)
        {
                    /* 0-9*/
            i2 = char2-48;
        }
        else if (char2 >= 65 && char2 <= 70)
        {
                    /* A-F*/
            i2 = char2-65+10;
        }
        else if (char2 >= 97 && char2 <= 102)
        {
            i2 = char2-97+10;
        }
        else
        {
            i2=0;
        }
        i3 = i1*16+i2;
        return (byte)(i3&0xff);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (NetService.MyBinder)service;
            if(myBinder.GetNetState()==true)
            {
                //进入该界面，进入 新通道
                myBinder.SendDebugCmd(new byte[]{0,1});
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public class DebugTestReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle bundle = intent.getExtras();
            int portnum = bundle.getInt("PORTNUM");
            int bytenum = bundle.getInt("RECEBYTENUM");
            byte[] byteData = bundle.getByteArray("RECEBYTE");
            byte[] tempdata = new byte[bytenum];
            //textDis = textDis + "\n";
            if(portnum==NetService.DEBUGTESTNUM)
            {
                for(int i=0;i<bytenum;i++)
                {
                    tempdata[i] = byteData[i];
                    //textDis = textDis + byteData[i] + "\t"  ;
                }
                String ss = new String(tempdata);
                //textDis = textDis + ss;
                //textReceiver.setText(textDis);
                refreshLogView(ss+"\n");

            }
        }
    }
    void refreshLogView(String msg)
    {
        textReceiver.append(msg);
        int offset = textReceiver.getLineCount()*textReceiver.getLineHeight();
        if(offset>textReceiver.getHeight())
        {
            textReceiver.scrollTo(0,offset-textReceiver.getHeight());
        }
    }
}