package com.splxtech.appliance.model;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by li300 on 2016/7/13 0013.
 */
public class MessageStart {
    private String version;
    private String company;

    public MessageStart()
    {

    }
    public void setMessagestart(Activity context)
    {
        company = "@2016 splxtech.com,all rights reserved";
        PackageManager pm = context.getPackageManager();
        try{
            PackageInfo pi = pm.getPackageInfo("com.example.flux.android",0);
            version = "v"+pi.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {

        }
    }
    public String getverison()
    {
        return version;
    }
    public String getCompany()
    {
        return company;
    }

}

