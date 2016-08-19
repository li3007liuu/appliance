package com.splxtech.appliance;


import com.splxtech.appliance.database.DbService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import greendao.Appliance;
import greendao.WasteTypeBean;

/**
 * 定义全局变量，以便在其他地方activity引用
 * Created by li300 on 2016/7/29 0029.
 */
public class Data
{
    private static List<Appliance> applianceList = new ArrayList<Appliance>();
    private static List<WasteTypeBean> wasteTypeBeanList = new ArrayList<WasteTypeBean>();
    public static List<Appliance> getApplianceList()
    {
        return applianceList;
    }
    public static void setApplianceList(List<Appliance> list)
    {
        applianceList = list;
    }
    public static int getYear()
    {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }
    public static int getMonth()
    {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH)+1;
    }
    public static int getDay()
    {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }
    public static int getHours()
    {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.HOUR_OF_DAY);
    }
    // 1 == 周日  2==周一 3 == 周二 4==周三
    public static int getDayOfWeek()
    {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK);
    }
    //全局变量 操作数据库,在Db服务里面进行对象设定
    public static DbService dbService;
    public static List<WasteTypeBean> getWasteTypeBeanList()
    {
        return wasteTypeBeanList;
    }
    public static void setWasteTypeBeanList(List<WasteTypeBean> list)
    {
        wasteTypeBeanList = list;
    }
    public static Appliance selectappliance;
    public static boolean netstate;//网络正常则为真 网络错误则为假
    public static String ServiceIp;
    public static int ServicePort;
}
