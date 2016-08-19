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
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.splxtech.appliance.R;
import com.splxtech.appliance.service.NetService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by li300 on 2016/7/24 0024.
 * 包含：服务绑定 广播消息接收  chart设置等
 *
 *
 */
public class BxtActivity extends Activity implements View.OnClickListener {

    //服务绑定参数
    private NetService.MyBinder myBinder;
    private Intent intent;

    //波形点数
    private int BxtNum = 128;

    //广播消息参数
    private BxtReceiver bxtReceiver = null;


    //View界面数据
    private Button buttonBxtBack;
    private LineChart mLineChart;
    private LineChart lineChart2;

    //定时器数据
    private final Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (myBinder.GetNetState() == true)
            {
                //请求电压电流波形
                    myBinder.GetDlbxService();
                Log.i("Bxttest","Timer send data!");
            }
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_chart_test);

        mLineChart = (LineChart) findViewById(R.id.spread_line_chart);
        setmLineChart(mLineChart,false);
         lineChart2 = (LineChart) findViewById(R.id.spread_line_chart2);
        setmLineChart(lineChart2,true);

        buttonBxtBack = (Button)findViewById(R.id.button_bxt_back);
        buttonBxtBack.setText("< 电力查询");
        buttonBxtBack.setOnClickListener(this);
        buttonBxtBack.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonBxtBack.setTextColor(Color.rgb(220,221,222));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonBxtBack.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        //新建服务绑定
        intent = new Intent(this,NetService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);

        //注册广播接收器
        bxtReceiver = new BxtReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.splxtech.power.service.NetService");
        this.registerReceiver(bxtReceiver,intentFilter);
        //开启定时器定时发送数据 定时200ms请求数据一次
        timer.schedule(timerTask,1000,300);
    }
    @Override
    public void onDestroy()
    {
        timer.cancel();
        this.unregisterReceiver(bxtReceiver);
        unbindService(serviceConnection);
        super.onDestroy();
    }
    @Override
    public void onClick(View view) {
        finish();
    }
    private void setmLineChart(LineChart mLineChart,boolean flag)
    {
                /**/
        //允许触摸使能
        mLineChart.setTouchEnabled(true);
        mLineChart.setDragDecelerationEnabled(true);
        //波形绘制的速度
        mLineChart.setDragDecelerationFrictionCoef(0.9f);
        //允许拖拽
        mLineChart.setDragEnabled(true);
        mLineChart.setDescriptionColor(Color.WHITE);
        //波形图仅允许垂直缩放
        // mLineChart.setScaleEnabled(true);
        mLineChart.setScaleYEnabled(flag);
        mLineChart.setScaleXEnabled(false);

        mLineChart.setDrawGridBackground(false);
        mLineChart.setHighlightPerDragEnabled(false);
        //捏拉缩放 防止出现缩放x轴的情况
        mLineChart.setPinchZoom(false);
        //设置背景颜色
        mLineChart.setBackgroundColor(Color.WHITE);
        LineData data = new LineData();
        mLineChart.setData(data);
        //setData();
        //设置波形从左往右慢慢显示
        //mLineChart.animateX(10);
        Legend l = mLineChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(15f);
        l.setTextColor(Color.BLACK);
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);


        YAxis leftAxis = mLineChart.getAxisLeft();
        if(flag==false) {
            leftAxis.setValueFormatter(new AxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    DecimalFormat decimalFormat = new DecimalFormat(".0");
                    return decimalFormat.format(value);
                }

                @Override
                public int getDecimalDigits() {
                    return 0;
                }
            });
            leftAxis.setTextColor(Color.rgb(3, 154, 244));
            leftAxis.setAxisMaxValue(15000);
            leftAxis.setAxisMinValue(-15000);
        }
        else
        {
            leftAxis.setValueFormatter(new AxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    DecimalFormat decimalFormat=new DecimalFormat(".0");
                    return decimalFormat.format(value);
                }

                @Override
                public int getDecimalDigits() {
                    return 0;
                }
            });
            leftAxis.setTextColor(Color.rgb(244, 117, 117));
            //不设置最大值最小值就会自适应坐标轴
            //leftAxis.setAxisMaxValue(3000);
            //leftAxis.setAxisMinValue(-3000);
        }
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setEnabled(false);

        /*
        MarkerView mv = new MyMarkerView(this,R.layout.content_marker_view);
        mLineChart.setMarkerView(mv);*/

    }

    private void setData(int[] disdata,LineChart mLineChart,boolean flag)
    {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for(int i=0;i<BxtNum;i++)
        {
            if(flag==false)
                yVals1.add(new Entry(i,disdata[i]));
            else
                yVals1.add(new Entry(i,disdata[i+BxtNum]));
        }
        LineDataSet set1;

        if(mLineChart.getData()!=null&&
                mLineChart.getData().getDataSetCount() > 0)
        {
            set1 = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            //更新图表
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        }
        else
        {
            if(flag==false)
            {
                set1 = new LineDataSet(yVals1, "电压波形图");
                //设置对应坐标轴为左边
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(Color.rgb(3, 154, 244));
                //不画圈
            }
            else
            {
                set1 = new LineDataSet(yVals1, "电流波形图");
                //设置对应坐标轴为左边
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(Color.rgb(252,104,15));
                //不画圈
            }
            set1.setLineWidth(2f);
            //set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(Color.WHITE);//(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircles(false);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            data.setDrawValues(false);
            // set data
            mLineChart.setData(data);
            mLineChart.invalidate();
        }
    }

    //服务绑定
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (NetService.MyBinder) service;
            if (myBinder.GetNetState() == true)
            {
                //请求电压电流波形
                myBinder.GetDlbxService();
                Log.i("Bxttest","binder finish send data!");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
    };
    //接收消息广播
    public class BxtReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context,Intent intent)
        {
            Bundle bundle = intent.getExtras();
            int portnum = bundle.getInt("PORTNUM");
            int recenum = bundle.getInt("RECEDATANUM");
            int[] receData = bundle.getIntArray("RECEDATA");
            if(portnum==NetService.DLBXPORTNUM)
            {
                Log.i("Bxttest","data received ，start draw!");
                mLineChart.clear();
                setData(receData,mLineChart,false);
                lineChart2.clear();
                setData(receData,lineChart2,true);
                Log.i("Bxttest","draw finished!");
            }
        }
    }
}
