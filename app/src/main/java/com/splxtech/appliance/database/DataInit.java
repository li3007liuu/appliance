package com.splxtech.appliance.database;

import java.util.ArrayList;
import java.util.List;

import greendao.WasteTypeBean;

/**
 * Created by li300 on 2016/8/17 0017.
 */
public class DataInit
{
    private List<WasteTypeBean> wasteTypeBeanList;
    //电视机模式一为周一至周五
    private int[] dsjmode1={0,0,0,0,0,0,0,5,10,10,5,0};
    private int[] dsjmodel2={0,0,0,0,5,3,6,3,6,10,10,3};
    //微波炉 早上使用一下，傍晚或晚上使用一下
    private int[] wblmodel={0,0,0,1,0,0,0,0,2,3,0,0};
    //热水器 间歇性启动热水保温，热水保温
    private int[] rsqmodel={1,1,1,1,1,1,1,1,3,4,1,1};
    //电饭煲
    private int[] dfbmodel1={0,0,0,0,0,0,0,0,3,0,0,0};
    private int[] dfbmodel2={0,0,0,0,0,3,0,0,3,0,0,0};
    //挂式空调 夜晚用电
    private int[] gsktmodel={5,5,5,3,0,0,0,0,0,0,3,5};
    private int[] days={3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
    private int[] mouths = {7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8};
    private int firstday = 6;
    private int[] type={1,2,3,4,5,6};
    //电视机70 电饭煲500 微波炉 1000 挂式空调1000 热水器 1000 柜式空调 2000
    private int[] wwww={100,1000,1000,500,1000,2000};
    private long id;
    public DataInit()
    {
        wasteTypeBeanList = new ArrayList<WasteTypeBean>();
        id=0;
    }
    public List<WasteTypeBean> getWasteTypeBeanList(int size)
    {
        int[] times={0,0,0,0,0,0,0,0,0,0,0,0};
        int powerweast;
        int jrcs=0;
        int jrsc=0;
        WasteTypeBean wasteTypeBean;
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<46;j++)
            {
                powerweast = 0;
                for(int k=0;k<12;k++)
                {
                    if(i%6==0)
                    {
                        //周六周日
                        if(j%7<2)
                        {
                            jrcs = 5 + (int)(Math.random()*6);
                            jrsc = 18000 + (int)(Math.random()*3000);
                            times[k]=(int)((dsjmodel2[k]*Math.random())*wwww[0]/10);
                        }
                        else
                        {
                            jrcs = 2 + (int)(Math.random()*2);
                            jrsc = 10800 + (int)(Math.random()*2000);
                            times[k]=(int)((dsjmode1[k]*Math.random())*wwww[0]/10);
                        }
                    }
                    else if(i%6==1)
                    {
                        jrcs =  2 + (int)(Math.random()*2);
                        jrsc = 1200 + (int)(Math.random()*300);
                        times[k]=(int)((wblmodel[k]*Math.random())*wwww[1]/10);
                    }
                    else if(i%6==2)
                    {
                        jrcs =  2 + (int)(Math.random()*2);
                        jrsc = 18000 + (int)(Math.random()*3000);
                        times[k]=(int)((rsqmodel[k]*Math.random())*wwww[2]/10);
                    }
                    else if(i%6==3)
                    {
                        jrcs =  2 + (int)(Math.random()*2);
                        jrsc = 1200 + (int)(Math.random()*300);
                        if(j%7<2)
                        {
                            times[k]=(int)((dfbmodel2[k]*Math.random())*wwww[3]/10);
                        }
                        else
                        {
                            times[k]=(int)((dfbmodel1[k]*Math.random())*wwww[3]/10);
                        }
                    }
                    else if(i%6==4)
                    {
                        jrcs =  2 + (int)(Math.random()*2);
                        jrsc = 18000 + (int)(Math.random()*3000);
                        times[k]=(int)((gsktmodel[k]*Math.random())*wwww[4]/10);
                    }
                    else
                    {

                        //周六周日
                        if(j%7<2)
                        {
                            jrcs = 5 + (int)(Math.random()*6);
                            jrsc = 18000 + (int)(Math.random()*3000);
                            times[k]=(int)((dsjmodel2[k]*Math.random())*wwww[5]/10);
                        }
                        else
                        {
                            jrcs = 2 + (int)(Math.random()*2);
                            jrsc = 10800 + (int)(Math.random()*2000);
                            times[k]=(int)((dsjmode1[k]*Math.random())*wwww[5]/10);
                        }
                    }
                    powerweast = powerweast + times[k];
                }
                wasteTypeBean = new WasteTypeBean(id,2016,mouths[j],days[j],jrcs,jrsc,powerweast,times[0],
                        times[1],times[2],times[3],times[4],times[5],times[6],times[7],times[8],times[9],times[10],
                        times[11],(long)(i+1));
                wasteTypeBeanList.add(wasteTypeBean);
                id++;

            }
        }
        return wasteTypeBeanList;
    }
}
