package com.splxtech.appliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.splxtech.appliance.R;
import com.splxtech.appliance.model.Dqsjdiss_ghtj;

import java.util.List;

/**
 * Created by li300 on 2016/8/8 0008.
 */
public class DqsjdissBottomAdapter extends ArrayAdapter<Dqsjdiss_ghtj>
{
    private int resourceId;
    public DqsjdissBottomAdapter(Context context, int textViewResourceId, List<Dqsjdiss_ghtj> objects)
    {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        Dqsjdiss_ghtj dqsjdiss_ghtj = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView)view.findViewById(R.id.text_dqsjdis_itemtitle);
            viewHolder.context = (TextView)view.findViewById(R.id.text_dqsjdis_itemcontent);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.title.setText(dqsjdiss_ghtj.getTitle());
        viewHolder.context.setText(dqsjdiss_ghtj.getContext());

        return view;
    }

    class ViewHolder
    {
        TextView title;
        TextView context;
    }
}
