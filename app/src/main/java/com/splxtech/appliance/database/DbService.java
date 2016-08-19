package com.splxtech.appliance.database;


import android.content.Context;
import android.text.TextUtils;


import com.splxtech.dao.ApplianceDao;
import com.splxtech.dao.DaoSession;
import com.splxtech.dao.WasteTypeBeanDao;

import java.util.List;

import greendao.Appliance;
import greendao.WasteTypeBean;


/**
 * Created by li300 on 2016/8/13 0013.
 */
public class DbService
{
    private static final String TAG = DbService.class.getSimpleName();
    private static DbService instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private ApplianceDao applianceDao;
    private WasteTypeBeanDao wasteTypeDao;

    private DbService()
    {

    }
    //采用单例模式
    public static DbService getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new DbService();
            if(appContext == null)
            {
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = BaseApplication.getDaoSession(context);
            instance.applianceDao = instance.mDaoSession.getApplianceDao();
            instance.wasteTypeDao = instance.mDaoSession.getWasteTypeBeanDao();
        }
        return instance;
    }
    //根据电器ID，取出电器数据
    public Appliance loadAppliance(long id)
    {
        if(!TextUtils.isEmpty(id+""))
        {
            return applianceDao.load(id);
        }
        return null;
    }
    // 取出所有电器数据
    public List<Appliance>loadAllAppliance()
    {
        return applianceDao.loadAll();
    }
    //生成按id倒叙排序的列表
    public List<Appliance>loadAllApplianceByOrder()
    {
        return applianceDao.queryBuilder().orderDesc(ApplianceDao.Properties.AppId).list();
    }
    //根据查询条件，返回数据列表 where 条件 params 参数 return 数据列表
    public List<Appliance>queryAppliance(String where,String... params)
    {
        return applianceDao.queryRaw(where,params);
    }
    //根据用户信息 插入或修改信息
    // param 电器信息
    //@return 插入或修改的用户id
    public long saveAppliance(Appliance appliance)
    {
        return applianceDao.insertOrReplace(appliance);
    }
    //批量插入或者修改用户信息
    public void saveApplianceLists(final List<Appliance> list)
    {
        if(list == null||list.isEmpty()) {
            return;
        }
        applianceDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<list.size();i++)
                {
                    Appliance appliance = list.get(i);
                    applianceDao.insertOrReplace(appliance);
                }
            }
        });
    }
    //删除所有数据,同时删除下一级数据库的数据
    public void deleteAllAppliance()
    {
        applianceDao.deleteAll();
        wasteTypeDao.deleteAll();
    }
    //根据id 删除数据
    //param id 用户id
    public void deleteAppliance(long id)
    {
        //删除对应的id下的功耗统计数据
        applianceDao.deleteByKey(id);
        wasteTypeDao.queryBuilder().where(WasteTypeBeanDao.Properties.TypeId.eq(id)).buildDelete();
    }
    //根据用户类删除信息
    public void deleteAppliance(Appliance appliance)
    {
        applianceDao.delete(appliance);
    }

    //根据电器id，取出该类别下的所有信息
    public List<WasteTypeBean> getWastesByTypeId(long typeid)
    {
        return wasteTypeDao.load(typeid).getWastes();
    }
    //删除全部数据
    public void deleteAllWaste()
    {
        wasteTypeDao.deleteAll();
    }
    public void saveAllWaste(final List<WasteTypeBean> list)
    {
        if(list == null||list.isEmpty()) {
            return;
        }
        wasteTypeDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<list.size();i++)
                {
                    WasteTypeBean wasteTypeBean = list.get(i);
                    wasteTypeDao.insertOrReplace(wasteTypeBean);
                }
            }
        });
    }
    //添加或修改
    public long saveWastes(WasteTypeBean temp)
    {
        return wasteTypeDao.insertOrReplace(temp);
    }
    //返回所有新闻,用于测试类别删除的同步性
    public List<WasteTypeBean> getAllWastes()
    {
        return wasteTypeDao.loadAll();
    }
    //取出某电器当月用的功耗
    public List<WasteTypeBean> getWastesByIdMouth(long typeid,int year,int mouth)
    {
        return wasteTypeDao.queryBuilder().where(WasteTypeBeanDao.Properties.TypeId.eq(typeid),WasteTypeBeanDao.Properties.DataYear.eq(year),WasteTypeBeanDao.Properties.DataMouth.eq(mouth)).list();
    }
    public  List<WasteTypeBean> getWastesByIdDay(long typeid,int year,int mouth,int day)
    {
        return wasteTypeDao.queryBuilder().where(WasteTypeBeanDao.Properties.TypeId.eq(typeid),WasteTypeBeanDao.Properties.DataYear.eq(year),WasteTypeBeanDao.Properties.DataMouth.eq(mouth),WasteTypeBeanDao.Properties.DataDay.eq(day)).list();
    }
    public long getWasteMaxId()
    {
        return wasteTypeDao.count();
    }
}
