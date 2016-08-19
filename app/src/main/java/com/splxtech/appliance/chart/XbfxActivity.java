package com.splxtech.appliance.chart;

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
import android.widget.RadioButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.splxtech.appliance.R;
import com.splxtech.appliance.service.NetService;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by li300 on 2016/7/24 0024.
 */
public class XbfxActivity extends Activity implements View.OnClickListener
{
    //服务绑定
    private NetService.MyBinder myBinder;
    private Intent intent;

    //消息广播相关变量
    private XbfxReceiver xbfxReceiver = null;

    //自定义变量
    private boolean disShift;

    //布局控件
    private Button buttonXbfxBack;
    private BarChart dlChart,dyChart;
    private RadioButton radioQbxb;
    private RadioButton radioJcxb;

    // 定时器
    private final Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (myBinder.GetNetState() == true) {
                //获得电压电流波形
                Log.i("XbfxTest","Timer send data!");
                    myBinder.GetDlxbService();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xbfx);
        dlChart = (BarChart)findViewById(R.id.bar_xbfx_dlxb);
        dyChart = (BarChart)findViewById(R.id.bar_xbfx_dyxb);
        setBarChart(dlChart);
        setBarChart(dyChart);

        buttonXbfxBack = (Button)findViewById(R.id.button_xbfx_back);
        buttonXbfxBack.setOnClickListener(this);
        buttonXbfxBack.setText("< 电力查询");
        buttonXbfxBack.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonXbfxBack.setTextColor(Color.rgb(220,221,222));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonXbfxBack.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        radioQbxb = (RadioButton)findViewById(R.id.radio_xbfx_qbxb);
        radioQbxb.setOnClickListener(this);
        radioJcxb = (RadioButton)findViewById(R.id.radio_xbfx_jcxb);
        radioJcxb.setOnClickListener(this);
        //显示全部波形
        disShift = false;
        //服务绑定
        intent = new Intent(this,NetService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);

        //注册广播接收器
        xbfxReceiver = new XbfxReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.splxtech.power.service.NetService");
        this.registerReceiver(xbfxReceiver,intentFilter);
        //开启定时器定时发送数据
        timer.schedule(timerTask,1000,300);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        timer.cancel();
        this.unregisterReceiver(xbfxReceiver);
        unbindService(serviceConnection);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_xbfx_back :
                finish();
                break;
            case R.id.radio_xbfx_qbxb:
                disShift = false;
                break;
            case R.id.radio_xbfx_jcxb:
                disShift = true;
                break;
        }
    }
    private void setBarChart(BarChart mChart)
    {
        //添加边框
        mChart.setDrawBorders(true);
        //不画阴影
        mChart.setDrawBarShadow(false);
        //mChart.setDrawValue(false);
        //设置值在柱状图上顶部显示
        mChart.setDrawValueAboveBar(false);

        //不描述
        mChart.setDescription("");
        //描述不显示
        mChart.setDescriptionColor(Color.WHITE);
        //最大显示数为42
        mChart.setMaxVisibleValueCount(42);
        //不能斜向缩放
        mChart.setPinchZoom(false);
        //水平方向进行缩放，垂直不进行缩放
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(false);
        mChart.setDrawGridBackground(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(8);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(6,false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value==100)
                {
                    return 100+"";
                }
                else if(value==80)
                {
                    return 10+"";
                }
                else if(value==60)
                {
                    return 1+"";
                }
                else if(value==40)
                {
                    return 0.1+"";
                }
                else if(value==20)
                {
                    return 0.01+"";
                }
                else if(value==0)
                {
                    return 0.001+"";
                }
                else
                {
                    return "";
                }
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        //关闭右侧坐标轴显示
        mChart.getAxisRight().setEnabled(false);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        BarData data1 = new BarData();
        data1.setValueTextSize(10f);
        data1.setBarWidth(0.9f);
        mChart.setData(data1);

    }
//flag == false 为电流  flag == true 为电压  count ==20 显示数组前20个值 count=40 显示数组前40个值
    private void setData(BarChart mChart,int count,float[] data,boolean flag)
    {
        float start = 0f;
        mChart.getXAxis().setAxisMinValue(start);
        mChart.getXAxis().setAxisMaxValue(start+count+1);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for(int i= (int)start;i<start+count;i++)
        {
            yVals1.add(new BarEntry(i + 1f, data[i]));
            /*if(flag==false)
            {
                yVals1.add(new BarEntry(i + 1f, data[i]));
            }*/
            /*else
            {
                yVals1.add(new BarEntry(i + 1f, data[i+count]));
            }*/
        }
        BarDataSet set1;
        if(mChart.getData()!=null&&
                mChart.getData().getDataSetCount()>0)
        {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        }
        else
        {
            if(flag==false) {
                set1 = new BarDataSet(yVals1, "电压谐波");
                set1.setColors(ColorTemplate.DLXB_COLORS);
            }
            else
            {
                set1 = new BarDataSet(yVals1, "电流谐波");
                set1.setColors(ColorTemplate.DYXB_COLORS);
            }
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data1 = new BarData(dataSets);
            data1.setDrawValues(false);
            data1.setBarWidth(0.9f);
            mChart.setData(data1);

        }
    }
    //服务绑定相关函数
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (NetService.MyBinder) service;
            if (myBinder.GetNetState() == true)
            {
                Log.i("XbfxTest","Binding send data!");
                //获得电压电流波形
                myBinder.GetDlxbService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
    };
    //广播消息接收器相关函数
    public class XbfxReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context,Intent intent)
        {
            Bundle bundle = intent.getExtras();
            int portnum = bundle.getInt("PORTNUM");
            int recenum = bundle.getInt("RECEDATANUM");
            int[] receData = bundle.getIntArray("RECEDATA");
            if(portnum == NetService.DLXBPORTNUM)
            {
                Log.i("XbfxTest","start draw bxt!");
                float[] fdata1;
                float[] fdata2;
                if(disShift==false)
                {
                    fdata1 = new float[40];
                    fdata2 = new float[40];
                    double temp1 = Math.log10((float)(receData[0]));
                    double temp2 = Math.log10((float)(receData[40]));
                    for (int i = 0; i < 80; i++) {
                        if (i == 40 || i == 0)
                        {
                            fdata1[0] = 100;
                            fdata2[0] = 100;
                        }
                        else if (i < 40)
                        {
                            fdata1[i] = (float) (Math.log10((float) (receData[i])) / temp1 * 100);
                        }
                        else
                        {
                            fdata2[i-40] = (float) (Math.log10((float) (receData[i])) / temp2 * 100);
                        }
                    }
                    dlChart.clear();
                    setData(dlChart, 40, fdata1, false);
                    dyChart.clear();
                    setData(dyChart, 40, fdata2, true);
                }
                else
                {
                    fdata1 = new float[20];
                    fdata2 = new float[20];
                    double temp1 = Math.log10((float)(receData[0]));
                    double temp2 = Math.log10((float)(receData[40]));
                    for (int i = 0; i < 80; i++)
                    {
                        if (i == 40 || i == 0)
                        {
                            fdata1[0] = 100;
                            fdata2[0] = 100;
                        }
                        else if (i < 40)
                        {
                            if(i%2==0)
                            {
                                fdata1[i/2] = (float) (Math.log10((float) (receData[i])) / temp1 * 100);
                            }
                        }
                        else
                        {
                            if(i%2==0)
                            {
                                fdata2[(i-40)/2] = (float) (Math.log10((float) (receData[i])) / temp2 * 100);
                            }
                        }
                    }
                    dlChart.clear();
                    setData(dlChart, 20, fdata1, false);
                    dyChart.clear();
                    setData(dyChart, 20, fdata2, true);

                }
                Log.i("XbfxTest","finish draw bxt!");
            }
        }
    }
}
