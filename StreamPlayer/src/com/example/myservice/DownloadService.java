package com.example.myservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class DownloadService extends Service implements Runnable {//实现Runable接口
    
    private String URL_str;                                              	//网络歌曲的路径
    private File download_file;                                        	//下载的文件
    private int total_read = 0;                                        		//已经下载文件的长度（以字节为单位）
    private int readLength = 0;                                       		//一次性下载的长度（以字节为单位）
    private int file_length = 0;                                        //音乐文件的长度（以字节为单位）
    private boolean flag = false;                                        	//是否停止下载，停止下载为true
    private Thread downThread;                                       	 //下载线程
    private String fileName;                                       		 //下载的文件名
    private Intent myintent;
    
        @Override
        public IBinder onBind(Intent intent) {
                return null;
        }

        @Override
        public void onCreate() {
                downThread = new Thread(this);                        //初始化下载线程
                downThread.start();
                myintent = new Intent("MyReceiver");
        }

        @Override
        public void onStart(Intent intent, int startId) {
             URL_str = intent.getExtras().getString("url");                //获取下载链接的url
             fileName = intent.getExtras().getString("fileName");		//获取下载的文件名
             Log.i(URL_str, fileName);
        }
        
        @Override
        public void onDestroy() {
                flag = true;                                                                //停止下载
        }
        
        //实现Run方法，进行歌曲的下载
        @Override
        public void run() 
        {
        	//文件输出流
            FileOutputStream fos = null;
            //文件输出流
            FileInputStream fis = null;                    
            InputStream is = null;                                 //网络文件输入流
            URL url = null;
            try 
            {
            	//网络歌曲的url
            	url = new URL(URL_str);                                
            	HttpURLConnection httpConnection = null;
            	//打开网络连接
            	httpConnection = (HttpURLConnection)url.openConnection();
            	is = httpConnection.getInputStream();
            	//在sdcard根目录建立一个与要下载的文件的名字相同的文件
            	download_file = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
            	fos = new FileOutputStream(download_file, true);                //初始化文件输出流
            	fis = new FileInputStream(download_file);                           //初始化文件输入流
            	total_read = fis.available();                                                	//初始化“已下载部分”的长度，此处应为0
            	file_length = httpConnection.getContentLength();              //要下载的文件的总长度
            	if (is == null) 
            	{ 
            		 //如果下载失败则打印日志，并返回
            		Log.i("info", "donload failed...");
            		return;
                 }
            	byte buf[] = new byte[1024];//定义下载缓冲区
            	readLength = 0;//一次性下载的长度
            	Log.i("info", "download start...");
                //向前台发送开始下载广播
            	myintent.putExtra("state", "start");
            	myintent.putExtra("ctrl", "start");
            	myintent.putExtra("filename",fileName);
            	myintent.putExtra("process", "0.0%");
            	myintent.putExtra("size", file_length);
            	sendBroadcast(myintent);
            	//如果读取网络文件的数据流成功，且用户没有选择停止下载，则开始下载文件
            	while (readLength != -1  && !flag) 
            	{
            		if((readLength = is.read(buf))>0)
            		{
            			fos.write(buf, 0, readLength);
            			total_read += readLength;                        //已下载文件的长度增加
            		}
            		if (total_read == file_length) 
            		{
            			//当已下载的长度等于网络文件的长度，则下载完成
            			flag = false;
            			Log.i("info", "download complete...");
            			//向前台发送下载完成广播
            			myintent.putExtra("state", "stop");
            			myintent.putExtra("ctrl", "start");
            			myintent.putExtra("filename",fileName);
            			myintent.putExtra("process","100%");
            			myintent.putExtra("size", file_length);
                    	sendBroadcast(myintent);
            			//关闭输入输出流
            			fos.close();
            			is.close();
            			fis.close();
            		}
            		Thread.sleep(1000);//当前现在休眠50毫秒
            		myintent.putExtra("state", "start");
            		myintent.putExtra("ctrl", "start");
            		myintent.putExtra("filename",fileName);
            		myintent.putExtra("process", ((total_read+0.0)/file_length*10+"").substring(0, 5)+"%");
            		myintent.putExtra("size", file_length);
                	sendBroadcast(myintent);
            		Log.i("info", "download process : "//打印下载进度
            				+ ((total_read+0.0)/file_length*10+"").substring(0, 5)+"%");
            		}
            	} catch (Exception e) {
            		
            		myintent.putExtra("state", "stop");
            		myintent.putExtra("ctrl", "start");
            		myintent.putExtra("filename",fileName);
            		myintent.putExtra("process", ((total_read+0.0)/file_length*10+"").substring(0, 5)+"%");
            		myintent.putExtra("size", file_length);
                	sendBroadcast(myintent);
            		e.printStackTrace();
                }
        }
}
