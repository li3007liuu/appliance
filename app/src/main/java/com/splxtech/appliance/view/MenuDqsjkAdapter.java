package com.splxtech.appliance.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.splxtech.appliance.R;
import com.splxtech.appliance.model.MenuClass;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

/**
 * Created by li300 on 2016/7/28 0028.
 */
public class MenuDqsjkAdapter extends ArrayAdapter<MenuClass>
{
    private int resourceId;
    private Context context;
    private List<MenuClass> menuClassList;
    public MenuDqsjkAdapter(Context context, int textViewResourceId, List<MenuClass> objects)
    {
        super(context,textViewResourceId,objects);
        this.context = context;
        this.menuClassList = objects;
        resourceId = textViewResourceId;
    }
    @Override
    public int getCount()
    {
        return menuClassList.size();
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MenuClass menuClass = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null)
        {
            view = LayoutInflater.from(context).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.menuImage = (ImageView)view.findViewById(R.id.image_dqsjk_add);
            viewHolder.menuText = (TextView)view.findViewById(R.id.text_dqsjk_add);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.menuImage.setImageResource(menuClass.getImageId());
        viewHolder.menuText.setText(menuClass.getName());
        return view;
    }


    class ViewHolder
    {
        ImageView menuImage;
        TextView menuText;
    }
}
