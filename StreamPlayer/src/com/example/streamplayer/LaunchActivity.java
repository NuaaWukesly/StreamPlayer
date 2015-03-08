package com.example.streamplayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;

import com.example.myapplication.MyApplication;

public class LaunchActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_launch);
		
		new Handler().postDelayed(new Runnable()
		{ 
			public void run() 
			{ 
				/* Create an Intent that will start the Main WordPress Activity. */
				Intent mainIntent = new Intent(LaunchActivity.this,MusicPlayer.class); 
				LaunchActivity.this.startActivity(mainIntent); 
				LaunchActivity.this.finish();
			}
		}, 3000); 
	}
}
