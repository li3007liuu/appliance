package com.splxtech.appliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.splxtech.appliance.R;
import com.splxtech.appliance.model.XbcsClass;


import java.util.List;

/**
 * Created by li300 on 2016/8/4 0004.
 */
public class XbcsAdapter extends ArrayAdapter<XbcsClass>
{
    private int resourceId;
    public XbcsAdapter(Context context, int textViewResourceId, List<XbcsClass>objects)
    {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        XbcsClass xbcsClass = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.textxbcsNum = (TextView)view.findViewById(R.id.text_xbdis_num);
            viewHolder.textxbcsFz = (TextView)view.findViewById(R.id.text_xbdis_fz);
            viewHolder.textxbcsBfb = (TextView)view.findViewById(R.id.text_xbdis_bfb);
            viewHolder.textxbcsXwj = (TextView)view.findViewById(R.id.text_xbdis_xwj);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.textxbcsNum.setText(xbcsClass.getId()+"");
        viewHolder.textxbcsFz.setText(xbcsClass.getSxbfz());
        viewHolder.textxbcsBfb.setText(xbcsClass.getSxbbfb());
        viewHolder.textxbcsXwj.setText(xbcsClass.getSxbxwj());

        return view;
    }

    class ViewHolder
    {
        TextView textxbcsNum;
        TextView textxbcsFz;
        TextView textxbcsBfb;
        TextView textxbcsXwj;
    }
}
