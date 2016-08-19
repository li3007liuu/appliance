package com.splxtech.appliance.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.splxtech.appliance.Data;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by li300 on 2016/7/18 0018.
 * 基本处理流程是：UI请求数据   生成请求数据 发送请求  等待接收  接收完成后校验  通知UI更新数据
 */
public class NetService extends Service
{
    private int count;
    private boolean net_state;
    private ClientThread clientThread;
    private byte[] dataHeader = new byte[]{0,9,0,5,0,15,1};
    private byte[] reqDlcs = new byte[]{0,9,0,5,0,15,1,0,12,0,0,0,0,13,10};//电力参数
    private byte[] reqDlbx = new byte[]{0,9,0,5,0,15,1,0,13,2,0,0,1,13,10};//读取512个电流电压波形点
    private byte[] reqDlxb = new byte[]{0,9,0,5,0,15,1,0,14,0,80,0,1,13,10};//读取40个点的电压、电流谐波
    private byte[] reqDqljsb = new byte[]{0,9,0,5,0,15,1,0,11,0,0,0,1,13,10};//获取当前连接的设备号
    private byte[] reqLsljsb = new byte[]{0,9,0,5,0,15,1,0,11,0,0,0,2,13,10};//获取历史连接的设备号
    private byte[] reqDqdrgh = new byte[]{0,9,0,5,0,15,1,0,10,0,0,0,1,13,10};//获取某设备当日日功耗
    private byte[] reqDqdygh = new byte[]{0,9,0,5,0,15,1,0,10,0,0,0,2,13,10};//获取某设备当月日功耗
    private byte[] reqDqdngh=new byte[]{0,9,0,5,0,15,1,0,10,0,0,0,3,13,10};//获取某设备当年月功耗
    private byte[] reqAdddq = new byte[]{0,9,0,5,0,15,1,0,16,0,2,0,0,13,10};//新增电器
    private byte[] reqRemovedq = new byte[]{0,9,0,5,0,15,1,0,17,0,2,0,0,13,10};//删除电器
    private byte[] reqDrjrsc  = new byte[]{0,9,0,5,0,15,1,0,18,0,0,0,0,13,10};//请求接入时长
    private byte[] reqXblb = new byte[]{0,9,0,5,0,15,1,0,15,0,0,0,1,13,10};

    //接收到的数据存入该数组中去
    private int[] receData=new int[2048];
    private byte[] recebyte = new byte[4096];



    //通信端口号，标记请求的端口，在等待消息队列里通过该端口号进行判断；
    //该端口号在请求数据时附值，在获取完值之后清除，以保证通信的正常；

    public static int DLCSPORTNUM = 1;
    public static int DLBXPORTNUM = 2;
    public static int DLXBPORTNUM = 4;
    public static int DQLJSBPORTNUM = 6;
    public static int LSLJSBPORTNUM = 7;
    public static int DQDRGHPORTNUM = 8;
    public static int DQDYGHPORTNUM = 9;
    public static int DQDNGHPORTNUM = 10;
    public static int ADDDQPORTNUM = 11;
    public static int REMOVEDQPORTNUM = 12;
    public static int DRLJSCPORTNUM = 13;
    public static int XBLBPORTNUM = 14;
    public static int DEBUGTESTNUM = 18;

    private  Intent intent = new Intent();

    private int commPort;
    private Handler  receHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what==clientThread.OPENFIALE)
            {
                net_state = false;
                super.handleMessage(msg);
            }
            else if(msg.what==clientThread.RECEDATA)
            {
                count--;
                recebyte = (byte[]) msg.obj;
                //实际接收到的byte个数
                int bytenum = byte2short(recebyte[4094],recebyte[4095]);
                int datanum = byte2short(recebyte[9],recebyte[10]);
                int cmd = byte2short(recebyte[7],recebyte[8]);
                if(bytenum==5)
                {
                    //表示心跳包存在
                }
                else if(commPort==DEBUGTESTNUM)
                {
                    intent.putExtra("PORTNUM",commPort);
                    intent.putExtra("RECEDATA", receData);
                    intent.putExtra("RECEDATANUM", datanum);
                    intent.putExtra("RECEBYTE",recebyte);
                    intent.putExtra("RECEBYTENUM",bytenum);
                    intent.setAction("com.splxtech.power.service.NetService");
                    sendBroadcast(intent);
                }
                //表示正常数据
                else if(bytenum>=15)
                {
                    //数据头部校验
                    //将实际接收到值与设定值进行进行匹配

                    if (HeadEcc())
                    {
                        if(bytenum==15)
                        {
                            datanum = 0;
                            //receData = null;
                        }
                        else {
                            WriteData(recebyte, datanum);
                        }
                        //发送广播携带3个参数，端口号，接收到的数据，接收到的数据个数
                        if (cmd == 13&&commPort==DLBXPORTNUM)
                        {
                            intent.putExtra("PORTNUM", commPort);
                            intent.putExtra("RECEDATA", receData);
                            intent.putExtra("RECEDATANUM", datanum);
                            intent.putExtra("RECEBYTE",recebyte);
                            intent.putExtra("RECEBYTENUM",bytenum);
                            intent.setAction("com.splxtech.power.service.NetService");
                            sendBroadcast(intent);
                        }
                        else if(cmd==12&&commPort==DLCSPORTNUM)
                        {
                            intent.putExtra("PORTNUM", commPort);
                            intent.putExtra("RECEDATA", receData);
                            intent.putExtra("RECEDATANUM", datanum);
                            intent.putExtra("RECEBYTE",recebyte);
                            intent.putExtra("RECEBYTENUM",bytenum);
                            intent.setAction("com.splxtech.power.service.NetService");
                            sendBroadcast(intent);
                        }
                        else if(cmd==14&&commPort==DLXBPORTNUM)
                        {
                            intent.putExtra("PORTNUM", commPort);
                            intent.putExtra("RECEDATA", receData);
                            intent.putExtra("RECEDATANUM", datanum);
                            intent.putExtra("RECEBYTE",recebyte);
                            intent.putExtra("RECEBYTENUM",bytenum);
                            intent.setAction("com.splxtech.power.service.NetService");
                            sendBroadcast(intent);
                        }
                        else if(cmd==11&&commPort==DQLJSBPORTNUM)
                        {
                            intent.putExtra("PORTNUM", commPort);
                            intent.putExtra("RECEDATA", receData);
                            intent.putExtra("RECEDATANUM", datanum);
                            intent.putExtra("RECEBYTE",recebyte);
                            intent.putExtra("RECEBYTENUM",bytenum);
                            intent.setAction("com.splxtech.power.service.NetService");
                            sendBroadcast(intent);
                        }
                        else if(cmd==16&&commPort==ADDDQPORTNUM)
                        {
                            intent.putExtra("PORTNUM", commPort);
                            intent.putExtra("RECEDATA", receData);
                            intent.putExtra("RECEDATANUM", datanum);
                            intent.putExtra("RECEBYTE",recebyte);
                            intent.putExtra("RECEBYTENUM",bytenum);
                            intent.setAction("com.splxtech.power.service.NetService");
                            sendBroadcast(intent);
                        }
                        else if(cmd==17&&commPort==REMOVEDQPORTNUM)
                        {
                            intent.putExtra("PORTNUM", commPort);
                            intent.putExtra("RECEDATA", receData);
                            intent.putExtra("RECEDATANUM", datanum);
                            intent.putExtra("RECEBYTE",recebyte);
                            intent.putExtra("RECEBYTENUM",bytenum);
                            intent.setAction("com.splxtech.power.service.NetService");
                            sendBroadcast(intent);
                        }
                        else if(cmd==18&&commPort==DRLJSCPORTNUM)
                        {
                            intent.putExtra("PORTNUM", commPort);
                            intent.putExtra("RECEDATA", receData);
                            intent.putExtra("RECEDATANUM", datanum);
                            intent.putExtra("RECEBYTE",recebyte);
                            intent.putExtra("RECEBYTENUM",bytenum);
                            intent.setAction("com.splxtech.power.service.NetService");
                            sendBroadcast(intent);
                        }
                        else if(cmd==15&&commPort==XBLBPORTNUM)
                        {
                            intent.putExtra("PORTNUM", commPort);
                            intent.putExtra("RECEDATA", receData);
                            intent.putExtra("RECEDATANUM", datanum);
                            intent.putExtra("RECEBYTE",recebyte);
                            intent.putExtra("RECEBYTENUM",bytenum);
                            intent.setAction("com.splxtech.power.service.NetService");
                            sendBroadcast(intent);
                        }

                    }
                }
                super.handleMessage(msg);
            }
        }
    };

    //参数头校验
    private boolean HeadEcc()
    {
        dataHeader[4]=recebyte[4094];
        dataHeader[5]=recebyte[4095];
        int j=0;
        for(int i=0;i<7;i++)
        {
            if(recebyte[i]==dataHeader[i])
            {
                j++;
            }

        }
        if(j==7)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private short byte2short(byte datah,byte datal)
    {
        short s0 = (short)(datah&0xff);
        short s1 = (short)(datal&0xff);
        s0 <<=8;
        return (short)(s0|s1);
    }

    //更新为有符号数
    private void WriteData(byte [] inputByte,int num)
    {

        int j;
        for(int i=0;i<num;i++)
        {
            j=i*2+13;
            receData[i] =(int)byte2short(inputByte[j],inputByte[j+1]); //temp1*256+temp2;
        }
    }
    private MyBinder mBinder= new MyBinder();
    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    private final Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(count>60)
            {
                mBinder.ConnectService();
                //clientThread = new ClientThread(receHandler,Data.ServiceIp,Data.ServicePort);
                //new Thread(clientThread).start();
            }
            count=0;
        }
    };

    @Override
    public void onCreate()
    {
        super.onCreate();
        net_state = false;
        SharedPreferences preferences = getSharedPreferences("netconfig", Context.MODE_PRIVATE);
        Data.ServiceIp=preferences.getString("HOST_IP", null);
        Data.ServicePort=preferences.getInt("HOST_PORT", 0);
        clientThread = new ClientThread(receHandler,Data.ServiceIp,Data.ServicePort);
        new Thread(clientThread).start();
        net_state = true;
        commPort = 0;
        count=0;
        timer.schedule(timerTask,1000,60000);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        int a = super.onStartCommand(intent,flags,startId);
        return a;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    public class MyBinder extends Binder {
        //与服务器建立连接
        public void ConnectService()
        {
            clientThread = new ClientThread(receHandler,Data.ServiceIp,Data.ServicePort);
            new Thread(clientThread).start();
            net_state = true;
        }
        //获取网络状态
        public boolean GetNetState()
        {
            return net_state;
        }
        public void GetDlcsService()
        {
            count++;
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqDlcs;
                clientThread.revHandler.sendMessage(message);
                commPort = DLCSPORTNUM;
            }
            catch (Exception e)
            {

            }
        }
        public void GetDlbxService()
        {
            count++;
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqDlbx;
                clientThread.revHandler.sendMessage(message);
                commPort = DLBXPORTNUM;
            }
            catch (Exception e)
            {

            }
        }
        public void GetDlxbService()
        {
            count++;
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqDlxb;
                clientThread.revHandler.sendMessage(message);
                commPort = DLXBPORTNUM;
            }
            catch (Exception e)
            {

            }
        }
        public void GetDqljsbService()
        {
            count++;
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqDqljsb;
                clientThread.revHandler.sendMessage(message);
                commPort = DQLJSBPORTNUM;
            }
            catch (Exception e)
            {

            }
        }
        public void GetLsljsbService()
        {
            count++;
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqLsljsb;
                clientThread.revHandler.sendMessage(message);
                commPort = LSLJSBPORTNUM;
            }
            catch (Exception e)
            {

            }
        }
        public void GetDqdrghService()
        {
            count++;
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqDqdrgh;
                clientThread.revHandler.sendMessage(message);
                commPort = DQDRGHPORTNUM;
            }
            catch (Exception e)
            {

            }
        }
        public void GetDqdyghService()
        {
            count++;
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqDqdygh;
                clientThread.revHandler.sendMessage(message);
                commPort = DQDYGHPORTNUM;
            }
            catch (Exception e)
            {

            }
        }
        public void GetDqdnghService()
        {
            count++;
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqDqdngh;
                clientThread.revHandler.sendMessage(message);
                commPort = DQDNGHPORTNUM;
            }
            catch (Exception e)
            {

            }
        }
        public void SendAddDqService(int num)
        {
            count++;
            reqAdddq[11]=(byte)(num/256);
            reqAdddq[12]=(byte)(num&0xff);
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqAdddq;
                clientThread.revHandler.sendMessage(message);
                commPort = ADDDQPORTNUM ;
            }
            catch (Exception e)
            {

            }

        }
        public void SendRemoveDqSerivice(int num)
        {
            count++;
            reqRemovedq[11]=(byte)(num/256);
            reqRemovedq[12]=(byte)(num&0xff);
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqRemovedq;
                clientThread.revHandler.sendMessage(message);
                commPort = REMOVEDQPORTNUM;
            }
            catch (Exception e)
            {

            }
        }
        public void GetDqLjscSerivice()
        {
            count++;
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqDrjrsc;
                clientThread.revHandler.sendMessage(message);
                commPort = DRLJSCPORTNUM;
            }
            catch (Exception e)
            {

            }
        }
        public void GetXblbSerivice()
        {
            count++;
            short ii = 160;
            reqXblb[9]= (byte)(ii>>8);
            reqXblb[10]=(byte)(ii&0xff);
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = reqXblb;
                clientThread.revHandler.sendMessage(message);
                commPort = XBLBPORTNUM;
            }
            catch (Exception e)
            {

            }

        }
        public void SendDebugCmd(byte[] cmd)
        {
            count++;
            try{
                Message message = new Message();
                message.what = clientThread.SENDDATA;
                message.obj = cmd;
                clientThread.revHandler.sendMessage(message);
                commPort = DEBUGTESTNUM;
            }
            catch (Exception e)
            {

            }
        }
    }
}
