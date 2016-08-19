package com.splxtech.appliance.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;


/**
 * Created by li300 on 2016/7/15 0015.
 */
public class WifiAdmin {
    //定义 wifimanage 对象
    private WifiManager wifiManager;
    //定义 wifiinfo对象
    private WifiInfo wifiInfo;
    //设置 活动
    private static Context context;


    /*
    *   构造方法
    * */
    public WifiAdmin()
    {

    }

    public void setWifiAdmin(Context context)
    {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }
    //获取ssid
    public String getSSID()
    {
        return (wifiInfo==null)?"NULL":wifiInfo.getSSID();
    }

    //路由器的mac地址为 bssid
    public String getBSSID()
    {
        return (wifiInfo==null)?null:wifiInfo.getBSSID();
    }

    //获取mac地址
    public String getMacAddress()
    {
        return (wifiInfo==null)?"NULL":wifiInfo.getMacAddress();
    }
    //获取ip地址
    public int getIPAddress()
    {
        return (wifiInfo==null)?0:wifiInfo.getIpAddress();
    }
    //获得连接的ID
    public int getNetworkId()
    {
        return (wifiInfo==null)?0:wifiInfo.getNetworkId();
    }

    // 检测 wifi是否连接
    public static boolean IsWifi()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null)
            return false;
        int netType = networkInfo.getType();
        int netSubType = networkInfo.getSubtype();
        if(ConnectivityManager.TYPE_WIFI==netType)
            return networkInfo.isConnectedOrConnecting();
        return false;

    }

    // 检测移动数据是否连接
    public static boolean IsMobileConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(mMobileNetworkInfo!=null)
            return mMobileNetworkInfo.isConnected();
        return false;
    }

    //检测移动数据是否有可移动的网络数据
    public static boolean IsMobileAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(mMobileNetworkInfo!=null)
            return mMobileNetworkInfo.isAvailable();
        return false;
    }

    //检查网络是否可用
    public static boolean IsNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo()!=null
                &&connectivityManager.getActiveNetworkInfo().isConnected()
                &&connectivityManager.getActiveNetworkInfo().getState()==NetworkInfo.State.CONNECTED)
        {
            return connectivityManager.getActiveNetworkInfo().isAvailable();
        }
        return false;

    }

    //获取网络IP
    public String getLocalIp()
    {
        //成功获取外网ip,并显示了地区.
        String IP = "";
        try
        {
            String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setUseCaches(false);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream in = connection.getInputStream();

// 将流转化为字符串
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));

                String tmpString = "";
                StringBuilder retJSON = new StringBuilder();
                while ((tmpString = reader.readLine()) != null)
                {
                    retJSON.append(tmpString + "\n");
                }

                JSONObject jsonObject = new JSONObject(retJSON.toString());
                String code = jsonObject.getString("code");
                if (code.equals("0"))
                {
                    JSONObject data = jsonObject.getJSONObject("data");
                    IP = data.getString("ip"); //+ "(" + data.getString("country")
                    //+ data.getString("area") + "区"
                    //+ data.getString("region") + data.getString("city")
                    //+ data.getString("isp") + ")";

                    Log.e("提示", "您的IP地址是：" + IP);
                }
                else
                {
                    IP = "";
                    Log.e("提示", "IP接口异常，无法获取IP地址！");
                }
            }
            else
            {
                IP = "";
                Log.e("提示", "网络连接异常，无法获取IP地址！");
            }
        }
        catch (Exception e)
        {
            IP = "";
            Log.e("提示", "获取IP地址时出现异常，异常信息是：" + e.toString());
        }
        return IP;

    }

    String FormatIp(int i)
    {
        return (i&0xff)+"."+((i>>8)&0xff)+"."+((i>>16)&0xff)+"."+((i>>24)&0xff);
    }
}
