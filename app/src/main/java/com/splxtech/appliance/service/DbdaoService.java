package com.splxtech.appliance.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.splxtech.appliance.Data;
import com.splxtech.appliance.database.DataInit;
import com.splxtech.appliance.database.DbService;

/**
 * Created by li300 on 2016/8/15 0015.
 */
public class DbdaoService extends Service
{
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        Data.dbService = DbService.getInstance(this);
        Data.setApplianceList(Data.dbService.loadAllAppliance());
        //DataInit dataInit = new DataInit();
        //删除统计数据
        //Data.dbService.deleteAllWaste();
        //初始化统计数据
        //Data.dbService.saveAllWaste(dataInit.getWasteTypeBeanList(Data.getApplianceList().size()));
    }
}
