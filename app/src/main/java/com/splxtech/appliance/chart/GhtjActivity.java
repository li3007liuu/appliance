package com.splxtech.appliance.chart;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.splxtech.appliance.R;
import com.splxtech.appliance.adapter.GhtjWaveAdapter;
import com.splxtech.appliance.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li300 on 2016/8/10 0010.
 */
public class GhtjActivity extends Activity implements View.OnClickListener
{
    private Button buttonBack;
    private Button buttonTest;
    private TextView textView1,textView2;
    private RelativeLayout relativeLayout;
    private LineChart lineChart;
    private ListView listView;
    private List<ListItem> listItemList = new ArrayList<ListItem>();
    private GhtjWaveAdapter ghtjWaveAdapter;
    private boolean scaler_flag;
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghtj);
        buttonBack = (Button)findViewById(R.id.button_ghtj_back);
        buttonBack.setText("< 功耗分析");
        buttonBack.setOnClickListener(this);
        buttonBack.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonBack.setTextColor(Color.rgb(220,221,222));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonBack.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        buttonTest = (Button)findViewById(R.id.button_ghtj_test);
        buttonTest.setOnClickListener(this);
        relativeLayout = (RelativeLayout)findViewById(R.id.relative_ghtj) ;
        textView1 = (TextView)findViewById(R.id.text_ghtj_tt);
        textView2 = (TextView)findViewById(R.id.text_ghtj_dygh);
        lineChart = (LineChart)findViewById(R.id.linechart_ghtj_chart);
        setmLineChart(lineChart);
        setLineData(lineChart);
        listView = (ListView)findViewById(R.id.list_ghtj_chart);
        ghtjWaveAdapter = new GhtjWaveAdapter(this);
        listView.setAdapter(ghtjWaveAdapter);
        listItemList.add(new ListItem(ListItem.TYPE_BAR,"电器每日功耗"));
        listItemList.add(new ListItem(ListItem.TYPE_PIE,"电器每日功耗"));
        ghtjWaveAdapter.setListItems(listItemList);
        ghtjWaveAdapter.notifyDataSetChanged();
        scaler_flag = false;
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_ghtj_back:
                finish();
                break;
            case R.id.button_ghtj_test:
                if(scaler_flag==false)
                {
                    scaler_flag = true;
                    textView1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.ghtj_text));
                    textView2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.ghtj_text));
                    relativeLayout.startAnimation(AnimationUtils.loadAnimation(this,R.anim.ghtj_relative));
                    lineChart.startAnimation(AnimationUtils.loadAnimation(this,R.anim.ghtj_view));
                }
                else
                {
                    scaler_flag = false;
                    textView1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.ghtj_text2));
                    textView2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.ghtj_text2));
                    relativeLayout.startAnimation(AnimationUtils.loadAnimation(this,R.anim.ghtj_relative2));
                    lineChart.startAnimation(AnimationUtils.loadAnimation(this,R.anim.ghtj_view2));
                }
                break;
        }

    }

    private void setmLineChart(LineChart mLineChart)
    {
        mLineChart.setTouchEnabled(false);
        mLineChart.setDragDecelerationEnabled(true);
        mLineChart.setDragDecelerationFrictionCoef(0.9f);
        //允许拖拽
        mLineChart.setDragEnabled(false);
        mLineChart.setDescriptionColor(Color.alpha(0));
        //波形图仅允许垂直缩放
        // mLineChart.setScaleEnabled(true);
        mLineChart.setScaleYEnabled(false);
        mLineChart.setScaleXEnabled(false);

        mLineChart.setDrawGridBackground(false);
        mLineChart.setHighlightPerDragEnabled(false);
        //捏拉缩放 防止出现缩放x轴的情况
        mLineChart.setPinchZoom(false);
        //设置背景颜色
        mLineChart.setBackgroundColor(Color.alpha(0));
        LineData data = new LineData();
        mLineChart.setData(data);


        Legend l = mLineChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mMonths[((int)(value))%12];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);


        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setEnabled(false);

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setEnabled(false);

    }
    private void setLineData(LineChart mLineChart)
    {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for(int i=0;i<12;i++)
        {
            float val = (float)(Math.random()*240);
            yVals1.add(new Entry(i,val));
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

            set1 = new LineDataSet(yVals1, "");

            set1.setColor(Color.rgb(115,178,219));
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextColor(Color.WHITE);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            //设置对应坐标轴为左边
            set1.setFillColor(Color.WHITE);
            //Drawable drawable = getDrawable(R.drawable.colorw);
            //set1.setFillDrawable(drawable);
            set1.setDrawValues(true);
            set1.setValueTextSize(10f);
           //
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            // set data
            mLineChart.setData(data);
            mLineChart.invalidate();
        }
    }


}
