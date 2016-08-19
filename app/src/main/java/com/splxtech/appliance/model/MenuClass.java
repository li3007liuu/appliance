package com.splxtech.appliance.model;

/**
 * Created by li300 on 2016/7/28 0028.
 */
public class MenuClass
{
    private String name;
    private int imageId;
    public MenuClass(String name,int imageId)
    {
        this.name = name;
        this.imageId = imageId;
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
