package com.splxtech.appliance.model;
import java.text.DecimalFormat;

/**
 * Created by li300 on 2016/8/4 0004.
 */
public class XbcsClass
{
    private int id;
    private float xbfz;
    private float xbbfb;
    private float xbxwj;

    public XbcsClass(int sid,float sxbfz,float sxbbfb,float sxbxwj)
    {
        id = sid;
        xbfz = sxbbfb;
        xbbfb = sxbbfb;
        xbxwj = sxbxwj;
    }
    public void setXbfz(float fxbfz)
    {
        xbfz = fxbfz;
    }
    public void setXbbfb(float fxbbfb)
    {
        xbbfb = fxbbfb;
    }
    public void setXbxwj(float fxbxwj)
    {
        xbxwj = fxbxwj;
    }
    public int getId()
    {
        return  id;
    }
    public float getXbfz()
    {
        return xbfz;
    }
    public String getSxbfz()
    {
        DecimalFormat decimalFormat=new DecimalFormat(".0");
        return decimalFormat.format(xbfz);
    }
    public float getXbbfb()
    {
        return  xbbfb;
    }
    public String getSxbbfb()
    {
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        return decimalFormat.format(xbbfb);
    }
    public float getXbxwj()
    {
        return  xbxwj;
    }
    public String getSxbxwj()
    {
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        return decimalFormat.format(xbxwj);
    }
}
