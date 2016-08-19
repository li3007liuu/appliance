package com.splxtech.appliance.model;

/**
 * Created by li300 on 2016/8/2 0002.
 */
public class SelectButton
{
    private int imageId;
    private String name;

    public SelectButton(String name,int imageid)
    {
        this.name = name;
        this.imageId = imageid;
    }
    public int getImageId()
    {
        return imageId;
    }
    public String getName()
    {
        return name;
    }
}

