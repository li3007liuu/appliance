package com.splxtech.appliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.splxtech.appliance.R;
import com.splxtech.appliance.model.SelectButton;

import java.util.List;

/**
 * Created by li300 on 2016/8/2 0002.
 */
public class SelectBtnAdapter extends ArrayAdapter<SelectButton>
{
    private int resourceId;
    public SelectBtnAdapter(Context context ,int textViewResourceId,List<SelectButton> objects)
    {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        SelectButton selectButton = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.selectbtnImage = (ImageView)view.findViewById(R.id.item_main_btlogo);
            viewHolder.selectbtnName = (TextView)view.findViewById(R.id.item_main_title);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.selectbtnImage.setImageResource(selectButton.getImageId());
        viewHolder.selectbtnName.setText(selectButton.getName());

        return view;

    }

    class ViewHolder
    {
        ImageView selectbtnImage;
        TextView selectbtnName;
    }
}
