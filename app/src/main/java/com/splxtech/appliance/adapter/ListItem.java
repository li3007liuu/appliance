package com.splxtech.appliance.adapter;

/**
 * Created by li300 on 2016/8/11 0011.
 */
public class ListItem
{
    public static final int TYPE_PIE = 0;
    public static final int TYPE_BAR = 1;
    public static final int TYPE_COUNT = 3;

    private String name;
    private int type;
    public ListItem(int type,String name)
    {
        this.type = type;
        this.name = name;
    }
    public int getType()
    {
        return type;
    }
    public String getName()
    {
        return name;
    }
}
