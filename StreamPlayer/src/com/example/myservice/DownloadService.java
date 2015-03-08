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

public class DownloadService extends Service implements Runnable {//ʵ��Runable�ӿ�
    
    private String URL_str;                                              	//���������·��
    private File download_file;                                        	//���ص��ļ�
    private int total_read = 0;                                        		//�Ѿ������ļ��ĳ��ȣ����ֽ�Ϊ��λ��
    private int readLength = 0;                                       		//һ�������صĳ��ȣ����ֽ�Ϊ��λ��
    private int file_length = 0;                                        //�����ļ��ĳ��ȣ����ֽ�Ϊ��λ��
    private boolean flag = false;                                        	//�Ƿ�ֹͣ���أ�ֹͣ����Ϊtrue
    private Thread downThread;                                       	 //�����߳�
    private String fileName;                                       		 //���ص��ļ���
    private Intent myintent;
    
        @Override
        public IBinder onBind(Intent intent) {
                return null;
        }

        @Override
        public void onCreate() {
                downThread = new Thread(this);                        //��ʼ�������߳�
                downThread.start();
                myintent = new Intent("MyReceiver");
        }

        @Override
        public void onStart(Intent intent, int startId) {
             URL_str = intent.getExtras().getString("url");                //��ȡ�������ӵ�url
             fileName = intent.getExtras().getString("fileName");		//��ȡ���ص��ļ���
             Log.i(URL_str, fileName);
        }
        
        @Override
        public void onDestroy() {
                flag = true;                                                                //ֹͣ����
        }
        
        //ʵ��Run���������и���������
        @Override
        public void run() 
        {
        	//�ļ������
            FileOutputStream fos = null;
            //�ļ������
            FileInputStream fis = null;                    
            InputStream is = null;                                 //�����ļ�������
            URL url = null;
            try 
            {
            	//���������url
            	url = new URL(URL_str);                                
            	HttpURLConnection httpConnection = null;
            	//����������
            	httpConnection = (HttpURLConnection)url.openConnection();
            	is = httpConnection.getInputStream();
            	//��sdcard��Ŀ¼����һ����Ҫ���ص��ļ���������ͬ���ļ�
            	download_file = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
            	fos = new FileOutputStream(download_file, true);                //��ʼ���ļ������
            	fis = new FileInputStream(download_file);                           //��ʼ���ļ�������
            	total_read = fis.available();                                                	//��ʼ���������ز��֡��ĳ��ȣ��˴�ӦΪ0
            	file_length = httpConnection.getContentLength();              //Ҫ���ص��ļ����ܳ���
            	if (is == null) 
            	{ 
            		 //�������ʧ�����ӡ��־��������
            		Log.i("info", "donload failed...");
            		return;
                 }
            	byte buf[] = new byte[1024];//�������ػ�����
            	readLength = 0;//һ�������صĳ���
            	Log.i("info", "download start...");
                //��ǰ̨���Ϳ�ʼ���ع㲥
            	myintent.putExtra("state", "start");
            	myintent.putExtra("ctrl", "start");
            	myintent.putExtra("filename",fileName);
            	myintent.putExtra("process", "0.0%");
            	myintent.putExtra("size", file_length);
            	sendBroadcast(myintent);
            	//�����ȡ�����ļ����������ɹ������û�û��ѡ��ֹͣ���أ���ʼ�����ļ�
            	while (readLength != -1  && !flag) 
            	{
            		if((readLength = is.read(buf))>0)
            		{
            			fos.write(buf, 0, readLength);
            			total_read += readLength;                        //�������ļ��ĳ�������
            		}
            		if (total_read == file_length) 
            		{
            			//�������صĳ��ȵ��������ļ��ĳ��ȣ����������
            			flag = false;
            			Log.i("info", "download complete...");
            			//��ǰ̨����������ɹ㲥
            			myintent.putExtra("state", "stop");
            			myintent.putExtra("ctrl", "start");
            			myintent.putExtra("filename",fileName);
            			myintent.putExtra("process","100%");
            			myintent.putExtra("size", file_length);
                    	sendBroadcast(myintent);
            			//�ر����������
            			fos.close();
            			is.close();
            			fis.close();
            		}
            		Thread.sleep(1000);//��ǰ��������50����
            		myintent.putExtra("state", "start");
            		myintent.putExtra("ctrl", "start");
            		myintent.putExtra("filename",fileName);
            		myintent.putExtra("process", ((total_read+0.0)/file_length*10+"").substring(0, 5)+"%");
            		myintent.putExtra("size", file_length);
                	sendBroadcast(myintent);
            		Log.i("info", "download process : "//��ӡ���ؽ���
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
