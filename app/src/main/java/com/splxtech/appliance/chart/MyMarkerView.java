package com.splxtech.appliance.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.splxtech.appliance.R;

import java.text.DecimalFormat;

/**
 * Created by li300 on 2016/8/3 0003.
 */
public class MyMarkerView extends MarkerView
{
    private TextView textView;
    public MyMarkerView(Context context,int layoutResource)
    {
        super(context,layoutResource);
        textView = (TextView)findViewById(R.id.tv_content_marker_view);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        DecimalFormat decimalFormat=new DecimalFormat(".0");
        String disx = decimalFormat.format(e.getX());
        String disy = decimalFormat.format(e.getY());
        textView.setText("x:" + disx + "\ny:"+disy);
    }

    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }
}
