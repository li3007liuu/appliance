package com.splxtech.appliance.model;

/**
 * Created by li300 on 2016/8/8 0008.
 */
public class Dqsjdiss_ghtj
{
    private String title;
    private String context;
    public Dqsjdiss_ghtj(String tt,String cc)
    {
        title = tt;
        context = cc;
    }
    public String getTitle()
    {
        return title;
    }
    public String getContext()
    {
        return context;
    }
    public void setContext(String ss)
    {
        context = ss;
    }
}
