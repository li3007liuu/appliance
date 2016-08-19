package com.splxtech.appliance.database;

import android.app.Application;
import android.content.Context;

import com.splxtech.dao.DaoMaster;
import com.splxtech.dao.DaoSession;

/**
 * Created by li300 on 2016/8/13 0013.
 */
public class BaseApplication extends Application
{
    private static BaseApplication mInstance;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    @Override
    public void onCreate()
    {
        super.onCreate();
        if(mInstance==null)
            mInstance = this;
    }
    //取得DaoMaster
    public static DaoMaster getDaoMaster(Context context)
    {
        if(daoMaster == null)
        {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,"appliancedb",null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context)
    {
        if(daoSession == null)
        {
            if(daoMaster == null)
            {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return  daoSession;
    }
}
