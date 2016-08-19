package com.splxtech.appliance.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.splxtech.appliance.Data;
import com.splxtech.appliance.R;
import com.splxtech.appliance.adapter.DqsjkAdapter;
import com.splxtech.appliance.service.NetService;
import com.splxtech.appliance.swipemenulistview.SwipeMenu;
import com.splxtech.appliance.swipemenulistview.SwipeMenuCreator;
import com.splxtech.appliance.swipemenulistview.SwipeMenuItem;
import com.splxtech.appliance.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import greendao.Appliance;


/**
 * Created by li300 on 2016/7/27 0027.
 */
public class DqsjkListActivity extends Activity
{
    //服务绑定
    private NetService.MyBinder myBinder;
    private Intent intent;
    //消息广播相关变量
    private DqsjkReceiver dqsjkReceiver = null;
    //对话框
    private ProgressDialog dialog;
    private int state;//按下新增电器之后给下位机发送命令，该值为0,上位机接收到下位机命令之后，新增电器该值为端口号
    private boolean handleflag=true;
    private View dilagview;
    private Handler prodialogtoui;//线程向服务发送消息的Handle
    private int deledDqId;//删除的电器ID;
    private CoordinatorLayout container;
    //控件变量相关
    private List<Appliance> applianceList = new ArrayList<Appliance>();
    private DqsjkAdapter dqsjkAdapter;
    private Button buttonDqsjkBack;
    private Button buttonDqsjkAdd;
    private int imageselect;
    private FloatingActionButton floatDqsjkdelectAll;
    //活动变量
    private Activity activity = this;

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dqsjk);
        container = (CoordinatorLayout) findViewById(R.id.dqsjk_container);
        buttonDqsjkBack=(Button)findViewById(R.id.button_dqsjk_back);
        buttonDqsjkBack.setText("< 电器监测");
        buttonDqsjkBack.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonDqsjkBack.setTextColor(Color.rgb(220,221,222));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonDqsjkBack.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        floatDqsjkdelectAll = (FloatingActionButton)findViewById(R.id.float_dqsjk_deleteall);
        floatDqsjkdelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deleet all
                if(myBinder.GetNetState()==true) {
                    boolean IsOnline=false;
                    //检查是否有在线状态电器
                    for(int i=0;i<applianceList.size();i++)
                    {
                        if(applianceList.get(i).getOnline()==true)
                        {
                            IsOnline = true;
                        }
                    }
                    if(IsOnline==false)
                    {
                        state = 0;
                        myBinder.SendRemoveDqSerivice(0);
                        handleflag = true;//启动等待任务
                        //发送数据
                        progress_dialog("删除电器", "全部电器正在删除中", "删除失败！");
                        prodialogtoui = new Handler()
                        {
                            @Override
                            public void handleMessage(Message msg)
                            {
                                if(msg.what == 0x112)
                                {
                                    applianceList.removeAll(applianceList);
                                    dqsjkAdapter.notifyDataSetChanged();
                                }
                                super.handleMessage(msg);
                            }
                        };

                    }
                    else
                    {
                        Snackbar.make(container,"在线不能删除电器！",Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(DqsjkListActivity.this,"在线不能删除电器！",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Snackbar.make(container,"设备没有连接网络，请确保设备连接正常！",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        buttonDqsjkBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonDqsjkAdd = (Button)findViewById(R.id.button_dqsjk_add);
        buttonDqsjkAdd.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonDqsjkAdd.setBackgroundResource(R.drawable.icon_dilag_add3);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonDqsjkAdd.setBackgroundResource(R.drawable.icon_dilag_add2);
                }
                return false;
            }
        });
        buttonDqsjkAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dilagview = v;
                if(myBinder.GetNetState()==true)
                {
                    state=0;
                    myBinder.SendAddDqService(1);//(applianceList.size()+1);
                    handleflag = true;//启动等待任务
                    prodialogtoui = new Handler()
                    {
                        @Override
                        public void handleMessage(Message msg)
                        {
                            if(msg.what == 0x110)
                            {
                                dialog_addappliance(dilagview,"新增电器",null);
                            }
                            else if(msg.what==0x113)
                            {
                                Snackbar.make(container,"添加失败！",Snackbar.LENGTH_SHORT).show();
                            }
                            super.handleMessage(msg);
                        }
                    };
                    //发送数据
                    progress_dialog("新增电器","请打开需要添加的电器，等待……","电器添加失败，请重新添加！");

                }
                else
                {
                    Log.i("Dqsjk","设备没有连接网络，请确保设备连接正常！");
                    //提示设备没连接
                    Snackbar.make(container,"设备没有连接网络，请确保设备连接正常！",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        //initAppliance();
        applianceList = Data.getApplianceList();
        dqsjkAdapter = new DqsjkAdapter(this,R.layout.item_dqsjk,applianceList);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem updataItem = new SwipeMenuItem(getApplicationContext());
                updataItem.setWidth(dp2px(90));
                updataItem.setBackground(new ColorDrawable(Color.GRAY));
                updataItem.setTitle("更新");
                updataItem.setTitleSize(20);
                updataItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(updataItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setWidth(dp2px(90));
                deleteItem.setBackground(new ColorDrawable(Color.RED));
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(20);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        final SwipeMenuListView listView = (SwipeMenuListView)findViewById(R.id.list_dqsjk_dq);
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index)
                {
                    case 0:
                        break;
                    case 1:

                        if(myBinder.GetNetState()==true)
                        {
                            state = 0;
                            deledDqId = position;
                            if(applianceList.get(position).getOnline()==false) {
                                myBinder.SendRemoveDqSerivice(position + 1);
                                handleflag = true;//启动等待任务
                                //发送数据
                                progress_dialog("删除电器", "选中电器正在删除中", "删除失败！");
                                prodialogtoui = new Handler()
                                {
                                    @Override
                                    public void handleMessage(Message msg)
                                    {
                                        if(msg.what == 0x112)
                                        {
                                            applianceList.remove(deledDqId);
                                            dqsjkAdapter.notifyDataSetChanged();
                                        }
                                        else if(msg.what==0x113)
                                        {
                                            Snackbar.make(container,"删除失败",Snackbar.LENGTH_SHORT).show();
                                        }
                                        super.handleMessage(msg);
                                    }
                                };

                            }
                            else
                            {
                                Snackbar.make(container,"在线不能删除电器！",Snackbar.LENGTH_SHORT).show();
                                //Toast.makeText(DqsjkListActivity.this,"在线不能删除电器！",Toast.LENGTH_LONG).show();

                            }
                        }
                        else
                        {
                            Log.i("Dqsjk","设备没有连接网络，请确保设备连接正常！");
                            //提示设备没连接
                            Snackbar.make(container,"设备没有连接网络，请确保设备连接正常！",Snackbar.LENGTH_SHORT).show();
                        }

                        break;
                }
                return  false;

            }
        });
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position)
            {

            }
            @Override
            public void onSwipeEnd(int position)
            {

            }
        });
        listView.setAdapter(dqsjkAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Appliance appliance = applianceList.get(i);
                dialog_addappliance(view,"编辑电器",appliance);
            }
        });
        //服务绑定
        intent = new Intent(this,NetService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        //注册广播接收器
        dqsjkReceiver = new DqsjkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.splxtech.power.service.NetService");
        this.registerReceiver(dqsjkReceiver,intentFilter);
    }

    @Override
    public void onPause()
    {
        //全局变量更新
        Data.setApplianceList(applianceList);
        //保存数据
        super.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(dqsjkReceiver);
        unbindService(serviceConnection);
    }
    public int dp2px(float dipValue)
    {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int)(dipValue*scale+0.5f);
    }

    public void dialog_addappliance(View source, final String title, final Appliance appliance)
    {

        LinearLayout dialog_view = (LinearLayout)getLayoutInflater().inflate(R.layout.dialog_addappliance,null);
        final int [] imageIds = new int[]
                {
                        R.drawable.bjbdn1   ,R.drawable.bx1     ,R.drawable.cfj1    ,R.drawable.dd1     ,R.drawable.dfg1    ,R.drawable.dfs1    ,R.drawable.dsj1    ,R.drawable.gskt1   ,
                        R.drawable.kt1      ,R.drawable.rsq1    ,R.drawable.tsdn1   ,R.drawable.wbl1    ,R.drawable.xcq1    ,R.drawable.yx1     ,R.drawable.kx1     ,R.drawable.xyj1
                };
        final int [] imageIds2 = new int[]
                {
                        R.drawable.bjbdn0   ,R.drawable.bx0     ,R.drawable.cfj0    ,R.drawable.dd0     ,R.drawable.dfg0    ,R.drawable.dfs0    ,R.drawable.dsj0    ,R.drawable.gskt0   ,
                        R.drawable.kt0      ,R.drawable.rsq0    ,R.drawable.tsdn0   ,R.drawable.wbl0    ,R.drawable.xcq0    ,R.drawable.yx0     ,R.drawable.kx0     ,R.drawable.xyj0
                };

        final ImageView switcher;
        final EditText editTextName;
        final TextView textViewId;
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for(int i =0;i<imageIds.length;i++)
        {
            Map<String,Object>  listitem=new HashMap<String,Object>();
            listitem.put("image",imageIds[i]);
            mapList.add(listitem);
        }
        switcher = (ImageView)dialog_view.findViewById(R.id.image_adddialog_sicon);

        editTextName = (EditText)dialog_view.findViewById(R.id.edit_adddq_Name);
        textViewId = (TextView)dialog_view.findViewById(R.id.text_adddq_id);
        if(title=="新增电器")
        {
            editTextName.setText("未命名"+applianceList.size());
            textViewId.setText(applianceList.size()+1+"");
            switcher.setImageResource(R.drawable.bjbdn1);
            imageselect = 0;
        }
        else if(title=="编辑电器")
        {
            editTextName.setText(appliance.getName());
            textViewId.setText(appliance.getId()+"");

            for(int i=0;i<imageIds.length;i++) {
                if (imageIds[i] == appliance.getImageId1())
                {
                    imageselect = i;
                }
            }
            switcher.setImageResource(imageIds[imageselect]);//(imageIds[imageselect]);
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(this,mapList,R.layout.cell,new String[]{"image"},new int[]{R.id.image_cell});
        GridView gridView = (GridView) dialog_view.findViewById(R.id.grid_adddq_image);
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("error:","switcher:"+switcher+"position"+position);
                switcher.setImageResource(imageIds[position]);
                imageselect = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("error:","switcher:"+switcher+"position"+position);
                switcher.setImageResource(imageIds[position]);
                imageselect = position;
            }
        });
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(dialog_view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(title=="新增电器") {
                            //Long id, String name, Integer imageId1, Integer imageId2, Integer appId, Integer time, Integer waste, Boolean online
                            applianceList.add(new Appliance((long)(applianceList.size()+1),editTextName.getText().toString(), imageIds[imageselect], imageIds2[imageselect],applianceList.size()+1,0,0,false));
                        }
                        else if(title=="编辑电器")
                        {
                            applianceList.get(appliance.getAppId()-1).setName(editTextName.getText().toString());
                            applianceList.get(appliance.getAppId()-1).setImageId1(imageIds[imageselect]);
                            applianceList.get(appliance.getAppId()-1).setImageId2(imageIds2[imageselect]);
                        }
                        dqsjkAdapter.notifyDataSetChanged();
                    }
                })
                .create()
                .show();
    }
    //服务绑定相关函数
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (NetService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    //广播消息接收器相关函数
    public class DqsjkReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle bundle = intent.getExtras();
            int portnum = bundle.getInt("PORTNUM");
            int recenum = bundle.getInt("RECEDATANUM");
            int[] receData = bundle.getIntArray("RECEDATA");
            if(portnum==NetService.ADDDQPORTNUM)
            {
                if(recenum==2&&receData[0]==1)
                {
                    state = portnum;
                }
            }
            else if(portnum==NetService.REMOVEDQPORTNUM)
            {
                if(recenum==2&&receData[0]==1)
                {
                    state = portnum;
                }
            }

        }
    }

    public void progress_dialog(final String title , String message, final String failed)
    {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.setMax(100);
        dialog.show();
        dialog.setProgress(5);
        new Thread() {
            public void run() {
                while (handleflag) {
                    try {
                        // 400毫秒让进度条刷新
                        Thread.sleep(400);
                        //获取当前进度
                        int progress = dialog.getProgress();
                        //进度增加
                        progress++;  //也可以设置dialog.incrementProgressBy(5);
                        //重新设置
                        dialog.setProgress(progress);
                        //判断是否达到最大值
                        if (state==NetService.ADDDQPORTNUM)
                        {
                            //消失
                            dialog.dismiss();
                            Looper.prepare();
                            Message message = new Message();
                            message.what = 0x110;
                            message.obj = 1;
                            prodialogtoui.sendMessage(message);
                            Looper.loop();
                            //线程标识符
                            handleflag=false;
                        }
                        else if(state==NetService.REMOVEDQPORTNUM)
                        {
                            //消失
                            dialog.dismiss();
                            Looper.prepare();
                            Message message = new Message();
                            message.what = 0x112;
                            message.obj = 1;
                            prodialogtoui.sendMessage(message);
                            Looper.loop();

                            //线程标识符
                            handleflag=false;
                        }
                        else if(dialog.getProgress() >= 100)
                        {
                            //消失
                            dialog.dismiss();
                            //发送取消添加命令
                            myBinder.SendAddDqService(0);
                            handleflag=false;
                            Looper.prepare();
                            Message message = new Message();
                            message.what = 0x113;
                            message.obj = 1;
                            prodialogtoui.sendMessage(message);
                            Looper.loop();
                            //发送操作失败

                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
        }.start();
    }
}
