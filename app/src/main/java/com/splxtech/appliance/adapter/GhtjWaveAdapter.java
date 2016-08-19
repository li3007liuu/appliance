package com.splxtech.appliance.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.splxtech.appliance.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li300 on 2016/8/11 0011.
 */
public class GhtjWaveAdapter extends BaseAdapter
{
    private Activity activity;
    private List<ListItem> listItems;
    private int[] bluecolor = ColorTemplate.BLUE_COLORS;

    public GhtjWaveAdapter(Activity activity)
    {
        this.activity = activity;
    }

    public void setListItems(List<ListItem> list)
    {
        this.listItems = list;
    }
    @Override
    public int getCount()
    {
        if(listItems!=null)
        {
            return listItems.size();
        }
        else
        {
            return 0;
        }
    }
    @Override
    public Object getItem(int position)
    {
        if(listItems!=null&&position<listItems.size())
        {
            return listItems.get(position);
        }
        else
        {
            return null;
        }
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        if (listItems != null && position < listItems.size()) {
            return listItems.get(position).getType();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount()
    {
        return ListItem.TYPE_COUNT;
    }



    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        int type = getItemViewType(position);
        switch (type)
        {
            case ListItem.TYPE_PIE:{
                PieChartHolder holder = null;
                if(convertView == null)
                {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_ghtj_pie,null);
                    holder = new PieChartHolder();
                    holder.textHead1 = (TextView)convertView.findViewById(R.id.text_ghtj1_head1);
                    holder.textHead2 = (TextView)convertView.findViewById(R.id.text_ghtj1_head2);
                    holder.pieChart = (PieChart)convertView.findViewById(R.id.pie_ghtj1_chart);
                    convertView.setTag(holder);
                }
                else
                {
                    holder = (PieChartHolder)convertView.getTag();
                }
                holder.textHead1.setText("各电器功耗占比");
                setPieChartDay(holder.pieChart,getPieData(12,12));
                //添加波形数据
                break;
            }

            case  ListItem.TYPE_BAR:{
                BarChartHolder holder = null;
                if(convertView == null)
                {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_ghtj_bar,null);
                    holder = new BarChartHolder();
                    holder.textHead1 = (TextView)convertView.findViewById(R.id.text_ghtj2_head1);
                    holder.textHead2 = (TextView)convertView.findViewById(R.id.text_ghtj2_head2);
                    holder.barChart = (BarChart)convertView.findViewById(R.id.bar_ghtj2_chart);
                    convertView.setTag(holder);
                }
                else
                {
                    holder = (BarChartHolder)convertView.getTag();
                }
                holder.textHead1.setText("每日总功耗");
                setBarChart(holder.barChart);
                setBarData(holder.barChart,7);
                //添加波形数据
                break;
            }

            default:
                break;
        }
        return convertView;
    }
    static class  PieChartHolder
    {
        TextView textHead1;
        TextView textHead2;
        PieChart pieChart;
    }
    static class BarChartHolder
    {
        TextView textHead1;
        TextView textHead2;
        BarChart barChart;
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
        pieChartDay.setCenterText("电器功耗\n388 kWh");//设置饼状图中间的文字
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
            yValues.add(new PieEntry(1,i));
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
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
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

    private void setBarData(BarChart mChart,int count)
    {
        float start = 0f;
        mChart.getXAxis().setAxisMinValue(start);
        mChart.getXAxis().setAxisMaxValue(start+count+1);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for(int i= (int)start;i<start+count;i++)
        {
            float tt = (float)(Math.random()*2400);
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
            set1.setColors(ColorTemplate.BLUE_COLORS);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data1 = new BarData(dataSets);
            data1.setDrawValues(false);
            data1.setBarWidth(0.9f);
            mChart.setData(data1);

        }
    }
}
