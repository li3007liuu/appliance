package com.splxtech.appliance.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.splxtech.appliance.Data;
import com.splxtech.appliance.R;
import com.splxtech.appliance.adapter.ApplianceAdapter;
import com.splxtech.appliance.chart.DqsjdissActivity;


import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import greendao.Appliance;
import greendao.WasteTypeBean;


/**
 * Created by li300 on 2016/7/20 0020.
 */
public class DqjcFragment extends Fragment {


   // private TextView textView;

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 0x120)
            {
                Bundle bundle= (Bundle) msg.obj;
                int recenum = bundle.getInt("RECEDATANUM");
                int[] receData = bundle.getIntArray("RECEDATA");
                String ss = recenum+""+"\t";
                //清空在线列表
                applianceList.removeAll(applianceList);
                for (int i=0;i<dataapplianceList.size();i++)
                {
                    dataapplianceList.get(i).setOnline(false);
                }
                for(int i=0;i<recenum;i++)
                {
                    ss = ss+i+":"+receData[i]+"\t";
                    for(int j= 0;j<dataapplianceList.size();j++)
                    {
                        if((j+1)==(receData[i]))
                        {
                            //设置在线状态
                            dataapplianceList.get(j).setOnline(true);
                        }
                    }
                }
                //textView.setText(ss);
                for(int i=0;i<dataapplianceList.size();i++)
                {
                    if(dataapplianceList.get(i).getOnline()==true)
                    {
                        applianceList.add(dataapplianceList.get(i));
                    }
                }
                for(int i=0;i<dataapplianceList.size();i++)
                {
                    if(dataapplianceList.get(i).getOnline()==false)
                    {
                        applianceList.add(dataapplianceList.get(i));
                    }
                }
                applianceAdapter.notifyDataSetChanged();

            }
            else if(msg.what==0x121)
            {
                //仅仅更新后台参数，在线状态更新去刷新参数
                Bundle bundle= (Bundle) msg.obj;
                int recenum = bundle.getInt("RECEDATANUM");
                int[] receData = bundle.getIntArray("RECEDATA");
                //String displaydata="";
                //for(int i=0;i<recenum;i++)
                //{
                //    displaydata = displaydata + i +":\t" + receData[i] +"\t";
                //}
                //textView.setText(displaydata);
                if(recenum==(dataapplianceList.size()*2))//校验参数是否正确
                {

                    for(int i=0;i<dataapplianceList.size();i++)
                    {
                        dataapplianceList.get(i).setTime(receData[i]);
                        dataapplianceList.get(i).setWaste(receData[i+dataapplianceList.size()]);
                        //将总接收时间以年月日形式存入数据库中
                        //long typeid =dataapplianceList.get(i).getId();
                        //List<WasteTypeBean> aa= Data.dbService.getWastesByIdDay(typeid,Data.getYear(),Data.getMonth(),Data.getDay());
                        //long wasteid;
                        //if(aa==null)
                        //{
                        //    wasteid = typeid*1024 +
                        //}
                        //Data.dbService.saveWastes(new WasteTypeBean(Data.dbService.getWasteMaxId(),Data.getYear(),Data.getMonth(),Data.getDay(),receData[i+dataapplianceList.size()],0,0,0,0,0,0,0,0,0,0,0,0,0,0,typeid));
                    }
                }
            }
            super.handleMessage(msg);
        }
    };



    //private TextView textView;
    private ListView listView;
    private List<Appliance> applianceList = new ArrayList<Appliance>();
    private List<Appliance> dataapplianceList = new ArrayList<Appliance>();
    private List<Appliance> nolineapplianceList = new ArrayList<Appliance>();
    private ApplianceAdapter applianceAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_dqjc,container,false);
        listView = (ListView)view.findViewById(R.id.list_dqjc_dqlb);
        //textView = (TextView)view.findViewById(R.id.text_dqjc_test);
        return view;
    }
    @Override

    public void onStart()
    {
        super.onStart();
        dataapplianceList = Data.getApplianceList();
        //先移除再添加，不然会出现故障
        applianceList.removeAll(applianceList);
        applianceList.addAll(dataapplianceList);
        applianceAdapter = new ApplianceAdapter(this.getActivity(),R.layout.item_dqjc,applianceList);
        listView.setAdapter(applianceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Data.selectappliance = applianceList.get(i);
                Intent intent = new Intent(getActivity(), DqsjdissActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

}
