package com.splxtech.appliance.service;

import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;


/**
 * Created by li300 on 2016/7/21 0021.
 * 消息  0x101  网络打开失败 由客户端线程发向服务
 * 消息  0x102  给服务器发送数据 服务发向客户端
 * 消息  0x103  将接收到的数据发送给服务  客户端发送给服务
 */
public class ClientThread implements Runnable
{
    public final static int OPENFIALE = 0x101;
    public final static int SENDDATA  = 0x102;
    public final static int RECEDATA  = 0x103;
    private Socket socket;
    private String hostIP;
    private int hostPort;

    //定义服务向线程发送消息的Handler对象
    Handler sendHandler;//线程向服务发送消息的Handle
    Handler revHandler;
    byte[] content = new byte[4096];
    BufferedReader bufferedReader=null;
    OutputStream outputStream=null;
    InputStream inputStream = null;

    public ClientThread(Handler handler,String ip,int port)
    {
        this.sendHandler = handler;
        this.hostIP = ip;
        this.hostPort = port;
    }

    @Override
    public void run()
    {
        socket = new Socket();
        //连接服务器
        try{
            socket.connect(new InetSocketAddress(hostIP,hostPort),5000);
            //bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();

            new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        int len = -1;
                        while((len=inputStream.read(content))!=-1)
                        {
                            content[4094]=(byte)(len/256);
                            content[4095]=(byte)(len&0xff);
                            Message message = new Message();
                            message.what = RECEDATA;
                            message.obj = content;
                            sendHandler.sendMessage(message);
                            Log.i("ClientTest","Recerved,count:"+len+"!");
                        }
                    }
                    catch (IOException io)
                    {
                        io.printStackTrace();
                    }
                }
            }.start();
            //为当前线程初始化Looper
            Looper.prepare();
            //创建revHandler对象
            revHandler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    if(msg.what==SENDDATA) {
                        if (socket.isClosed() == false)
                        {
                            try
                            {
                                outputStream.write((byte[]) msg.obj);
                            }
                            catch (Exception e)
                            {
                                try
                                {
                                    socket.close();
                                }
                                catch (Exception b)
                                {
                                    b.printStackTrace();
                                }
                                e.printStackTrace();

                            }
                        }
                    }
                }
            };
            Looper.loop();
        }
        catch (SocketTimeoutException e)
        {
            Message message = new Message();
            message.what = OPENFIALE;
            message.obj = "网络连接超时！";
            sendHandler.sendMessage(message);
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
    }
}
