package com.splxtech.appliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.splxtech.appliance.R;


import java.util.List;

import greendao.Appliance;

/**
 * Created by li300 on 2016/7/27 0027.
 */
public class ApplianceAdapter   extends ArrayAdapter<Appliance>
{
    private int resourceId;
    public ApplianceAdapter(Context context, int textViewResourceId, List<Appliance> objects)
    {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Appliance appliance = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.applianceImage=(ImageView)view.findViewById(R.id.image_dqjc_dqimage);
            viewHolder.applianceName=(TextView)view.findViewById(R.id.text_dqjc_dqname);
            viewHolder.applianceTime=(TextView)view.findViewById(R.id.text_dqjc_dqjrsj);
            viewHolder.applianceWaste=(TextView)view.findViewById(R.id.text_dqjc_dqdrgh);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.applianceImage.setImageResource(appliance.getImageId());
        viewHolder.applianceName.setText(appliance.getId()+":"+appliance.getName());
        viewHolder.applianceTime.setText(disTime(appliance.getTime()));//(appliance.getTime()+"");
        viewHolder.applianceWaste.setText(disTime(appliance.getWaste()));//(appliance.getWaste()+"");
        return view;
    }

    class ViewHolder
    {
        ImageView applianceImage;
        TextView  applianceName;
        TextView  applianceTime;
        TextView  applianceWaste;
    }
    private String disTime(int ii)
    {
        String stime;
        int iii;
        if(ii>0)
        {
            stime = ii/3600+":"+(ii%3600)/60+":"+(ii%60);
        }
        else
        {
            iii= 65535 + ii;
            stime = ii/3600+":"+(ii%3600)/60+":"+(ii%60);
        }
        return stime;
    }
}
