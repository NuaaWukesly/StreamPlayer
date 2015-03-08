package com.example.myreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class myReceiver extends BroadcastReceiver{

	/*
	 * ���ص��ļ���
	 */
	private String filename="";
	/**
	 * ���ص�״̬
	 * start,stop,pause
	 */
	private String state="start";
	/*
	 * ���صĿ��ƣ����ڷ�����
	 */
	private String ctrl="";
	/*
	 * ���ص��ļ���С
	 */
	private int size= 0;
	/*
	 * ���صĽ���
	 */
	private String process  = "0.0%";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//���ڽ��չ㲥
		this.filename = intent.getStringExtra("filename");
		this.state = intent.getStringExtra("state");
		this.ctrl = intent.getStringExtra("ctrl");
		this.size = intent.getIntExtra("size", 0);
		this.process = intent.getStringExtra("process");
		Log.i("re", "shoudao"+"$$$$$"+this.filename);
		Log.e("po", this.process);
	}
	
	public String getFilename()
	{
		return this.filename;
	}
	public String getCtrl()
	{
		return this.ctrl;
	}
	
	public String getState()
	{
		return this.state;
	}

	public int getSize()
	{
		return this.size;
	}
	public String getPocess()
	{
		return this.process;
	}
}
