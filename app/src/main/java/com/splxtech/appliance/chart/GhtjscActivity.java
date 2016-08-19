package com.splxtech.appliance.chart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.splxtech.appliance.Data;
import com.splxtech.appliance.R;
import com.splxtech.appliance.database.DataInit;
import com.splxtech.appliance.scrollview.GhtjScrollView;

import java.util.ArrayList;
import java.util.List;

import greendao.WasteTypeBean;

/**
 * Created by li300 on 2016/8/12 0012.
 */
public class GhtjscActivity extends Activity implements GhtjScrollView.OnScrollListener,View.OnClickListener
{
    private GhtjScrollView ghtjScrollView;
    private LineChart ssLayout;
    private WindowManager windowManager;
    private TextView textView;
    private TextView textDygh;
    private Button buttonBack;
    private LineChart lineChart;
    private BarChart barChart;
    private PieChart pieChart;

    //电器当月每日功耗
    private int[] appliancewaste;
    //每种电器当月总功耗
    private int[] mouthwaste;
    //总功耗
    private int powerwaste;
    //每月总功耗统计
    private int[] yearwaste = new int[12];
    private List<WasteTypeBean> list = new ArrayList<WasteTypeBean>();
    private void InitGhtjData()
    {
        DataInit dataInit = new DataInit();
        //删除统计数据
        Data.dbService.deleteAllWaste();
        //初始化统计数据
        Data.dbService.saveAllWaste(dataInit.getWasteTypeBeanList(Data.getApplianceList().size()));
        powerwaste =0;
        //获取当月每日功耗
        mouthwaste = new int[Data.getApplianceList().size()];
        for(int i=0;i<Data.getApplianceList().size();i++)
        {
            list = Data.dbService.getWastesByIdMouth(Data.getApplianceList().get(i).getAppId(),Data.getYear(),Data.getMonth());
            appliancewaste = new int[list.size()];
            for(int j=0;j<list.size();j++)
            {
                mouthwaste[i] = mouthwaste[i]+list.get(j).getPowerWaste();
                powerwaste = powerwaste + list.get(j).getPowerWaste();
                appliancewaste[j] = list.get(j).getPowerWaste()+appliancewaste[j];
            }
            for(int k=1;k<(Data.getMonth()+1);k++)
            {
                list = Data.dbService.getWastesByIdMouth(Data.getApplianceList().get(i).getAppId(),Data.getYear(),k);
                if(list==null)
                {
                    yearwaste[k-1] = 0;
                }
                else
                {
                    for(int h=0;h<list.size();h++)
                    {
                        yearwaste[k-1]=list.get(h).getPowerWaste()+yearwaste[k-1];
                    }
                }
            }
        }
        //12个月份全部显示

    }

    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };
    private int[] bluecolor = ColorTemplate.BLUE_COLORS;
    //悬浮框View
    private static View suspendView;
    //悬浮框的参数
    private static WindowManager.LayoutParams suspendLayoutParams;
    //
    private int ssLayoutHeight;
    private int ghtjScrollViewTop;
    private int ssLayoutTop;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghtjsc);
        InitGhtjData();
        ghtjScrollView = (GhtjScrollView)findViewById(R.id.ghtjscrollview);
        ssLayout = (LineChart)findViewById(R.id.line_ghtjsc_chart);
        textView = (TextView)findViewById(R.id.text_ghtjsc_dis);
        textDygh = (TextView)findViewById(R.id.text_ghtjsc_dyghz);
        textDygh.setText(powerwaste/1000+"");
        buttonBack = (Button)findViewById(R.id.button_ghtjsc_back);
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
        ghtjScrollView.setOnScrollListener(this);
        windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        lineChart = (LineChart)findViewById(R.id.line_ghtjsc_chart);
        setmLineChart(lineChart);
        setLineData(lineChart,yearwaste);
        barChart = (BarChart)findViewById(R.id.bar_ghtjsc_chart);
        setBarChart(barChart);
        setBarData(barChart,appliancewaste);
        pieChart = (PieChart)findViewById(R.id.pie_ghtjsc_chart);
        setPieChartDay(pieChart,getPieData(mouthwaste));

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
        {
            ssLayoutHeight = ssLayout.getHeight();
            ssLayoutTop = ssLayout.getTop();
            ghtjScrollViewTop = ghtjScrollView.getTop();
        }
    }

    @Override
    public void onScroll(int scrollY)
    {
        if(scrollY>=ssLayoutTop)
        {
            if(suspendView == null)
            {
                //showSuspend();

            }
            //textView.setText("diss floating!");
        }
        else if(scrollY <= ssLayoutTop + ssLayoutHeight)
        {
            if(suspendView!=null)
            {
                //removeSuspend();

            }
            //textView.setText("remove floating!");
        }
    }

    @Override
    public void onClick(View view)
    {
        finish();
    }
    private void showSuspend()
    {
        if(suspendView == null)
        {
            suspendView = LayoutInflater.from(this).inflate(R.layout.floating_layout, null);
            if(suspendLayoutParams == null){
                suspendLayoutParams = new WindowManager.LayoutParams();
                suspendLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                suspendLayoutParams.format = PixelFormat.RGBA_8888;
                suspendLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                suspendLayoutParams.gravity = Gravity.TOP;
                suspendLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                suspendLayoutParams.height = ssLayoutHeight;
                suspendLayoutParams.x = 0;
                suspendLayoutParams.y = ghtjScrollViewTop;
            }
        }

        windowManager.addView(suspendView, suspendLayoutParams);
    }
    private void removeSuspend(){
        if(suspendView != null){
            windowManager.removeView(suspendView);
            suspendView = null;
        }
    }
    @Override
    public void onPause()
    {
        //移除悬浮框 防止出现其他界面仍有悬浮框的情况
        //removeSuspend();
        super.onPause();
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
    private void setLineData(LineChart mLineChart,int[] dataIn)
    {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for(int i=0;i<dataIn.length;i++)
        {
            float val = (float)(dataIn[i]);
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

            set1.setColor(Color.WHITE);
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
    private void setPieChartDay(PieChart pieChartDay ,PieData pieData)
    {
        pieChartDay.setDescriptionColor(Color.WHITE);
        pieChartDay.setHoleRadius(60f);
        pieChartDay.setTransparentCircleRadius(64f);
        //中间添加文字
        pieChartDay.setDrawCenterText(true);
        pieChartDay.setDrawHoleEnabled(true);
        pieChartDay.setRotationAngle(0);//初始旋转角度
        pieChartDay.setRotationEnabled(false);//不能手动旋转
        pieChartDay.setUsePercentValues(false);//显示成百分比
        pieChartDay.setCenterText("");//设置饼状图中间的文字
        pieChartDay.setCenterTextSize(26f);

        pieChartDay.setData(pieData);
        Legend legend = pieChartDay.getLegend();
        legend.setEnabled(false);
        pieChartDay.animateXY(1000,1000);

        pieChartDay.setEntryLabelColor(Color.WHITE);
        pieChartDay.setEntryLabelTextSize(14f);
    }
    //计数是 12 范围是12
    private PieData getPieData(int[] temp)
    {
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
        for(int i=0;i<temp.length;i++)
        {
            yValues.add(new PieEntry(temp[i],Data.getApplianceList().get(i).getName()));
        }
        PieDataSet pieDataSet = new PieDataSet(yValues,"");
        pieDataSet.setSliceSpace(2f);
        /*ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int i=0;i<temp.length;i++)
        {
            colors.add(bluecolor[i%10]);
        }*/
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setDrawValues(false);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5*(metrics.densityDpi/160f);
        pieDataSet.setSelectionShift(px);

        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }
    private void setBarChart(BarChart mChart)
    {
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
        mChart.setMaxVisibleValueCount(12);
        //不能斜向缩放
        mChart.setPinchZoom(false);
        //水平方向进行缩放，垂直不进行缩放
        mChart.setScaleXEnabled(false);
        mChart.setScaleYEnabled(false);
        mChart.setDrawGridBackground(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)(value)+"";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(8);
        xAxis.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(6,false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextSize(12f);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f);
        //关闭右侧坐标轴显示
        mChart.getAxisRight().setEnabled(false);
        Legend l = mChart.getLegend();
        l.setEnabled(false);
        /*
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);*/

        BarData data1 = new BarData();
        data1.setValueTextSize(10f);
        data1.setBarWidth(0.9f);
        mChart.setData(data1);

    }

    private void setBarData(BarChart mChart,int[] dataIn)
    {
        float start = 0f;
        mChart.getXAxis().setAxisMinValue(start);
        mChart.getXAxis().setAxisMaxValue(start+dataIn.length+1);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for(int i= (int)start;i<start+dataIn.length;i++)
        {
            float tt = (float)(dataIn[i]);
            yVals1.add(new BarEntry(i + 1f, tt));
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
            set1 = new BarDataSet(yVals1, "");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data1 = new BarData(dataSets);
            data1.setDrawValues(false);
            data1.setBarWidth(0.9f);
            mChart.setData(data1);

        }
    }
}
