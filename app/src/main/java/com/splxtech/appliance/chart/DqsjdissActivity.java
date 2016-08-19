package com.splxtech.appliance.chart;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.splxtech.appliance.Data;
import com.splxtech.appliance.R;
import com.splxtech.appliance.adapter.DqsjdissBottomAdapter;
import com.splxtech.appliance.database.DataInit;
import com.splxtech.appliance.database.DbService;
import com.splxtech.appliance.model.Dqsjdiss_ghtj;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import greendao.WasteTypeBean;

/**
 * Created by li300 on 2016/8/8 0008.
 */
public class DqsjdissActivity extends Activity implements View.OnClickListener
{
    List<WasteTypeBean> list = new ArrayList<WasteTypeBean>();
    //控键变量
    private Button buttonDqsjdissBack;
    private GridView gridView;
    private TextView textDqsjdissTitle;
    private RadioButton radioDqsjdissDay;
    private RadioButton radioDqsjdissWeek;
    private RadioButton radioDqsjdissMouth;
    private RadioButton radioDqsjdissYear;
    private List<Dqsjdiss_ghtj> dqsjdiss_ghtjList = new ArrayList<Dqsjdiss_ghtj>();
    private DqsjdissBottomAdapter dqsjdissBottomAdapter;
    private PieChart pieChartDay;
    private BubbleChart bubbleChartWeek;
    private BarChart barChartYear;
    private LineChart lineChartMouth;
    private int[] bluecolor = ColorTemplate.BLUE_COLORS;

    private int[] daysDiss = new int[12];
    private int daysJrcs,daysJrsc,daysPjjrsc,daysGh,daysDf;

    private int[] weeksDiss = new int[7];
    private int weeksJrcs,weeksJrsc,weeksPjjrsc,weeksGh,weeksDf;

    private int[] mouthsDiss; //= new int[31];
    private int mouthsJrcs,mouthsJrsc,mouthsPjjrsc,mouthsGh,mouthsDf;

    private int[] yearsDiss;// = new int[12];
    private int yearsJrcs,yearsJrsc,yearsPjjrsc,yearsGh,yearsDf;

    //
    protected String[] mHours = new String[]
            {
              "0:00"   ,"2:00"  ,"4:00" ,"6:00" ,"8:00","10:00","12:00",
                    "14:00" ,"16:00" ,"18:00","20:00","22:00"
            };
    protected String[] mMonths = new String[] {
            "","Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private void InitWeaste()
    {

        long typeId = Data.selectappliance.getAppId();
        Log.i("dqsjdiss","typeId"+typeId);
        DataInit dataInit = new DataInit();
        //删除统计数据
        Data.dbService.deleteAllWaste();
        //初始化统计数据
        Data.dbService.saveAllWaste(dataInit.getWasteTypeBeanList(Data.getApplianceList().size()));
        //获取当日数据
        list=Data.dbService.getWastesByIdDay(typeId,Data.getYear(),Data.getMonth(),17);
        Log.i("dqsjdiss","list"+list);
        if(list!=null)
        {
            Log.i("dqsjdiss","list.size"+list.size());
            if(list.size()>0) {
                daysDiss[0] = list.get(0).getTimeWaste1();
                daysDiss[1] = list.get(0).getTimeWaste2();
                daysDiss[2] = list.get(0).getTimeWaste3();
                daysDiss[3] = list.get(0).getTimeWaste4();
                daysDiss[4] = list.get(0).getTimeWaste4();
                daysDiss[5] = list.get(0).getTimeWaste6();
                daysDiss[6] = list.get(0).getTimeWaste7();
                daysDiss[7] = list.get(0).getTimeWaste8();
                daysDiss[8] = list.get(0).getTimeWaste9();
                daysDiss[9] = list.get(0).getTimeWaste10();
                daysDiss[10] = list.get(0).getTimeWaste11();
                daysDiss[11] = list.get(0).getTimeWaste12();
                daysGh = list.get(0).getPowerWaste();
                daysJrcs = list.get(0).getUseNum();
                daysJrsc = list.get(0).getUseTime();
            }
            daysPjjrsc = daysJrsc/daysJrcs;
            daysDf=daysGh/2000;
        }
        else
        {
            for(int i=0;i<12;i++)
            {
                daysDiss[i]=0;
            }
        }
        for(int i=0;i<7;i++)
        {
            list = Data.dbService.getWastesByIdDay(typeId,Data.getYear(),Data.getMonth(),(Data.getDay()-i-1));
            if(list!=null)
            {
                weeksGh = list.get(0).getPowerWaste()+weeksGh;
                weeksJrcs = list.get(0).getUseNum()+weeksJrcs;
                weeksJrsc = list.get(0).getUseTime()+weeksJrsc;

            }
        }
        weeksPjjrsc = weeksJrsc / weeksJrcs;
        weeksDf = weeksGh / 2000;
        list = Data.dbService.getWastesByIdMouth(typeId,Data.getYear(),Data.getMonth());
        mouthsDiss = new int[list.size()];
        for(int i=0;i<list.size();i++)
        {
            mouthsDiss[i]=list.get(i).getPowerWaste();
            mouthsGh = list.get(i).getPowerWaste()+mouthsGh;
            mouthsJrcs = list.get(i).getUseNum()+mouthsJrcs;
            mouthsJrsc = list.get(i).getUseTime()+mouthsJrsc;
        }
        mouthsPjjrsc = mouthsJrsc/mouthsJrcs;
        mouthsDf = mouthsGh/2000;
        yearsDiss = new int[12];
        for(int i=1;i<(Data.getMonth()+1);i++)
        {
            list = Data.dbService.getWastesByIdMouth(typeId,Data.getYear(),i);
            if(list==null)
            {
                yearsDiss[i-1] = 0;
            }
            else
            {
                for(int j=0;j<list.size();j++)
                {
                    yearsDiss[i-1]=list.get(i).getPowerWaste()+yearsDiss[i];
                    yearsGh = list.get(i).getPowerWaste()+yearsGh;
                    yearsJrcs = list.get(i).getUseNum() + yearsJrcs;
                    yearsJrsc = list.get(i).getUseTime() + yearsJrsc;
                }
            }
        }
        yearsPjjrsc = yearsJrsc/yearsJrcs;
        yearsDf = yearsGh/2000;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dqsjdis);
        textDqsjdissTitle = (TextView)findViewById(R.id.text_dqsjdiss_title);
        textDqsjdissTitle.setText(Data.selectappliance.getName());
        buttonDqsjdissBack = (Button)findViewById(R.id.button_dqsjdis_back);
        buttonDqsjdissBack.setOnClickListener(this);
        buttonDqsjdissBack.setText("< 电器监测");
        buttonDqsjdissBack.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonDqsjdissBack.setTextColor(Color.rgb(220,221,222));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonDqsjdissBack.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        radioDqsjdissDay = (RadioButton)findViewById(R.id.radio_dqsjdis_day);
        radioDqsjdissDay.setOnClickListener(this);
        radioDqsjdissWeek = (RadioButton)findViewById(R.id.radio_dqsjdis_week);
        radioDqsjdissWeek.setOnClickListener(this);
        radioDqsjdissMouth = (RadioButton)findViewById(R.id.radio_dqsjdis_mouth);
        radioDqsjdissMouth.setOnClickListener(this);
        radioDqsjdissYear = (RadioButton)findViewById(R.id.radio_dqsjdis_year);
        radioDqsjdissYear.setOnClickListener(this);
        InitWeaste();
        pieChartDay = (PieChart)findViewById(R.id.pie_dqsjdis_daydis);
        PieData pieData = getPieData(12,12);
        setPieChartDay(pieData);
        InitDqsjdisslist();
        bubbleChartWeek = (BubbleChart)findViewById(R.id.bubble_dqsjdis_weekdis);
        setBubbleChartWeek();
        setBubbleData();
        lineChartMouth = (LineChart)findViewById(R.id.line_dqsjdis_mouth);
        setmLineChart(lineChartMouth);
        setLineData(lineChartMouth,mouthsDiss);
        barChartYear = (BarChart)findViewById(R.id.bar_dqsjdis_year);
        setBarChart(barChartYear);
        setBarData(barChartYear,yearsDiss);
        barChartYear.setVisibility(View.INVISIBLE);
        lineChartMouth.setVisibility(View.INVISIBLE);
        bubbleChartWeek.setVisibility(View.INVISIBLE);
        gridView = (GridView)findViewById(R.id.grid_dqsjdis_tj);
        dqsjdissBottomAdapter = new DqsjdissBottomAdapter(this,R.layout.item_dqsjdis_dqtj,dqsjdiss_ghtjList);
        gridView.setAdapter(dqsjdissBottomAdapter);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_dqsjdis_back:
                finish();
                break;
            case R.id.radio_dqsjdis_day:
                dqsjdiss_ghtjList.get(0).setContext(daysJrcs+"x");
                dqsjdiss_ghtjList.get(1).setContext("0W");
                dqsjdiss_ghtjList.get(2).setContext(daysPjjrsc/3600+"h"+((daysPjjrsc%3600)/60)+"min"+daysPjjrsc%60+"s");
                dqsjdiss_ghtjList.get(3).setContext(daysGh+"Wh");
                dqsjdiss_ghtjList.get(4).setContext(daysJrsc/3600+"h"+((daysJrsc%3600)/60)+"min"+daysJrsc%60+"s");
                dqsjdiss_ghtjList.get(5).setContext("¥"+daysDf);
                dqsjdissBottomAdapter.notifyDataSetChanged();
                pieChartDay.setVisibility(View.VISIBLE);
                bubbleChartWeek.setVisibility(View.INVISIBLE);
                barChartYear.setVisibility(View.INVISIBLE);
                lineChartMouth.setVisibility(View.INVISIBLE);
                break;
            case R.id.radio_dqsjdis_week:
                dqsjdiss_ghtjList.get(0).setContext(weeksJrcs+"x");
                dqsjdiss_ghtjList.get(1).setContext("0W");
                dqsjdiss_ghtjList.get(2).setContext(weeksPjjrsc/3600+"h"+((weeksPjjrsc%3600)/60)+"min"+weeksPjjrsc%60+"s");
                dqsjdiss_ghtjList.get(3).setContext(weeksGh+"Wh");
                dqsjdiss_ghtjList.get(4).setContext(weeksJrsc/3600+"h"+((daysJrsc%3600)/60)+"min"+weeksJrsc%60+"s");
                dqsjdiss_ghtjList.get(5).setContext("¥"+weeksDf);
                dqsjdissBottomAdapter.notifyDataSetChanged();
                pieChartDay.setVisibility(View.INVISIBLE);
                bubbleChartWeek.setVisibility(View.VISIBLE);
                barChartYear.setVisibility(View.INVISIBLE);
                lineChartMouth.setVisibility(View.INVISIBLE);
                break;
            case R.id.radio_dqsjdis_mouth:
                dqsjdiss_ghtjList.get(0).setContext(mouthsJrcs+"x");
                dqsjdiss_ghtjList.get(1).setContext("0W");
                dqsjdiss_ghtjList.get(2).setContext(mouthsPjjrsc/3600+"h"+((mouthsPjjrsc%3600)/60)+"min"+mouthsPjjrsc%60+"s");
                dqsjdiss_ghtjList.get(3).setContext(mouthsGh/1000+"kWh");
                dqsjdiss_ghtjList.get(4).setContext(mouthsJrsc/3600+"h"+((mouthsJrsc%3600)/60)+"min"+mouthsJrsc%60+"s");
                dqsjdiss_ghtjList.get(5).setContext("¥"+mouthsDf);
                dqsjdissBottomAdapter.notifyDataSetChanged();
                pieChartDay.setVisibility(View.INVISIBLE);
                bubbleChartWeek.setVisibility(View.INVISIBLE);
                barChartYear.setVisibility(View.INVISIBLE);
                lineChartMouth.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_dqsjdis_year:
                dqsjdiss_ghtjList.get(0).setContext(yearsJrcs+"x");
                dqsjdiss_ghtjList.get(1).setContext("0W");
                dqsjdiss_ghtjList.get(2).setContext(yearsPjjrsc/3600+"h"+((yearsPjjrsc%3600)/60)+"min"+yearsPjjrsc%60+"s");
                dqsjdiss_ghtjList.get(3).setContext(yearsGh/1000+"kWh");
                dqsjdiss_ghtjList.get(4).setContext(yearsJrsc/3600+"h"+((yearsJrsc%3600)/60)+"min"+yearsJrsc%60+"s");
                dqsjdiss_ghtjList.get(5).setContext("¥"+yearsDf);
                dqsjdissBottomAdapter.notifyDataSetChanged();
                pieChartDay.setVisibility(View.INVISIBLE);
                bubbleChartWeek.setVisibility(View.INVISIBLE);
                barChartYear.setVisibility(View.VISIBLE);
                lineChartMouth.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void InitDqsjdisslist()
    {
        dqsjdiss_ghtjList.add(new Dqsjdiss_ghtj("接入频率",daysJrcs+"x"));
        dqsjdiss_ghtjList.add(new Dqsjdiss_ghtj("电力","0W"));
        dqsjdiss_ghtjList.add(new Dqsjdiss_ghtj("平均接入时间",daysPjjrsc/3600+"h"+((daysPjjrsc%3600)/60)+"min"+daysPjjrsc%60+"s"));
        dqsjdiss_ghtjList.add(new Dqsjdiss_ghtj("耗电量",daysGh+"Wh"));
        dqsjdiss_ghtjList.add(new Dqsjdiss_ghtj("总接入时长",daysJrsc/3600+"h"+((daysJrsc%3600)/60)+"min"+daysJrsc%60+"s"));
        dqsjdiss_ghtjList.add(new Dqsjdiss_ghtj("电费","¥"+daysDf));
    }

    private void setPieChartDay(PieData pieData)
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
        pieChartDay.setCenterText(Data.selectappliance.getName()+"\n"+daysGh+"Wh");//设置饼状图中间的文字
        pieChartDay.setCenterTextSize(26f);

        pieChartDay.setData(pieData);
        Legend legend = pieChartDay.getLegend();
        legend.setEnabled(false);
        pieChartDay.animateXY(1000,1000);

        pieChartDay.setEntryLabelColor(Color.WHITE);
        pieChartDay.setEntryLabelTextSize(14f);
    }
    //计数是 12 范围是12
    private PieData getPieData(int count,float range)
    {
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
        for(int i=0;i<count;i++)
        {
            yValues.add(new PieEntry(1,mHours[i]));
        }
        PieDataSet pieDataSet = new PieDataSet(yValues,"");
        pieDataSet.setSliceSpace(2f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int i=0;i<count;i++)
        {
            colors.add(bluecolor[i%10]);
        }
        pieDataSet.setColors(colors);
        pieDataSet.setDrawValues(false);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5*(metrics.densityDpi/160f);
        pieDataSet.setSelectionShift(px);

        PieData pieData = new PieData(pieDataSet);
        return pieData;

    }

    private void setBubbleChartWeek()
    {
        bubbleChartWeek.setDescription("");
        bubbleChartWeek.setDrawGridBackground(false);
        bubbleChartWeek.setTouchEnabled(false);

        Legend legend = bubbleChartWeek.getLegend();
        legend.setEnabled(false);
        YAxis y1 = bubbleChartWeek.getAxisLeft();
        y1.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                return ((int)value)+":00";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        y1.setGridLineWidth(0f);
        y1.setAxisMaxValue(24);
        y1.setAxisMinValue(0);
        y1.setLabelCount(6);
        y1.setDrawGridLines(false);
        y1.setTextSize(12f);
        y1.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        bubbleChartWeek.getAxisRight().setEnabled(false);

        XAxis x1=bubbleChartWeek.getXAxis();
        x1.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value>=6)
                {
                    return "六";
                }
                else if(value>=5)
                {
                    return "五";
                }
                else if(value>=4)
                {
                    return "四";
                }
                else if(value>=3)
                {
                    return "三";
                }
                else if(value>=2)
                {
                    return "二";
                }
                else if(value>=1)
                {
                    return "一";
                }
                else
                {
                    return "日";
                }
            }
            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        x1.setPosition(XAxis.XAxisPosition.BOTTOM);
        x1.setAxisMaxValue(6);
        x1.setAxisMinValue(0);
        x1.setDrawGridLines(false);
        x1.setTextSize(10f);
        x1.setLabelCount(6);
    }

    private void setBubbleData()
    {
        ArrayList<BubbleEntry> yVals1 = new ArrayList<BubbleEntry>();
        for(int i=0;i<7;i++)
        {
            float val = (float)(Math.random()*24);
            float size = (float)(Math.random()*200);
            yVals1.add(new BubbleEntry(i,val,size));
        }
        BubbleDataSet set1 = new BubbleDataSet(yVals1,"");
        set1.setColors(bluecolor);
        set1.setDrawValues(false);
        BubbleData bubbleData = new BubbleData(set1);
        bubbleData.setDrawValues(false);
        bubbleData.setHighlightCircleWidth(1.5f);

        bubbleChartWeek.setData(bubbleData);
        bubbleChartWeek.invalidate();
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
                return mMonths[((int)(value))%13];
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

    private void setBarData(BarChart mChart,int[] mouths)
    {
        float start = 0f;
        mChart.getXAxis().setAxisMinValue(start);
        mChart.getXAxis().setAxisMaxValue(start+mouths.length+1);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for(int i= (int)start;i<start+mouths.length;i++)
        {
            float tt = (float)(mouths[i]);
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
            set1.setColors(Color.BLUE);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data1 = new BarData(dataSets);
            data1.setDrawValues(false);
            data1.setBarWidth(0.9f);
            mChart.setData(data1);

        }
    }

    private void setmLineChart(LineChart mLineChart)
    {
                /**/
        //允许触摸使能
        mLineChart.setTouchEnabled(false);
        mLineChart.setDragDecelerationEnabled(true);
        //波形绘制的速度
        mLineChart.setDragDecelerationFrictionCoef(0.9f);
        //允许拖拽
        mLineChart.setDragEnabled(false);
        mLineChart.setDescriptionColor(Color.WHITE);
        //波形图仅允许垂直缩放
        // mLineChart.setScaleEnabled(true);
        mLineChart.setScaleYEnabled(false);
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
        l.setEnabled(false);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return ((int)value)+"";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setTextSize(12f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setEnabled(false);

        /*
        MarkerView mv = new MyMarkerView(this,R.layout.content_marker_view);
        mLineChart.setMarkerView(mv);*/

    }
    private void setLineData(LineChart mLineChart,int[] weaste)
    {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for(int i=0;i<weaste.length;i++)
        {
            float val = (float)(weaste[i]);
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
                //设置对应坐标轴为左边
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.BLUE);
                //不画圈
            set1.setLineWidth(2f);
            //set1.setCircleRadius(3f);
            //set1.setFillAlpha(65);
            //set1.setFillColor(Color.WHITE);//(ColorTemplate.getHoloBlue());
            //set1.setHighLightColor(Color.rgb(244, 117, 117));
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
}
