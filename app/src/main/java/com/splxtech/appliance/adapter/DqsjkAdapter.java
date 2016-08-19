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
public class DqsjkAdapter extends ArrayAdapter<Appliance>
{
    private int resourceId;
    public DqsjkAdapter(Context context, int textViewResourceId, List<Appliance>objects)
    {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
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
            viewHolder.applianceImage = (ImageView)view.findViewById(R.id.image_dqsjk_dqimage);
            viewHolder.applianceId = (TextView)view.findViewById(R.id.text_dqsjk_id);
            viewHolder.applianceName = (TextView)view.findViewById(R.id.text_dqsjk_Name);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.applianceImage.setImageResource(appliance.getImageId1());
        viewHolder.applianceName.setText(appliance.getName());
        viewHolder.applianceId.setText(position+1+"");//(appliance.getId()+"");

        return view;

    }
    class ViewHolder
    {
        ImageView applianceImage;
        TextView  applianceName;
        TextView applianceId;
    }
}
