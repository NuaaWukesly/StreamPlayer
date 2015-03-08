package com.example.myreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class myReceiver extends BroadcastReceiver{

	/*
	 * 下载的文件名
	 */
	private String filename="";
	/**
	 * 下载的状态
	 * start,stop,pause
	 */
	private String state="start";
	/*
	 * 下载的控制，用于服务中
	 */
	private String ctrl="";
	/*
	 * 下载的文件大小
	 */
	private int size= 0;
	/*
	 * 下载的进度
	 */
	private String process  = "0.0%";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//用于接收广播
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
