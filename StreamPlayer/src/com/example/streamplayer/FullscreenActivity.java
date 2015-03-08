package com.example.streamplayer;

import com.example.myapplication.MyApplication;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

public class FullscreenActivity extends Activity {

	private String path;
	private VideoView player;
	private MediaController controller;
	private int currtime = 0;
	private MyApplication mp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fullscreen);
		//水平方向
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		mp = (MyApplication)getApplication();
		player = (VideoView)findViewById(R.id.fullscreenplayer);
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		currtime = intent.getIntExtra("currtime", 0);
		player.setVideoURI(Uri.parse(path));
		player.seekTo(currtime);
		controller = new MediaController(this);
		player.setMediaController(controller);
		
		
		player.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				player.start();
			}
		});
		player.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				mp.reset();
				player.setVideoURI(Uri.parse(path));
				player.seekTo(currtime);
				return true;
			}
		});
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mp.setVideoCurrTime(player.getCurrentPosition());
		Log.i("full", "到此"+mp.getVideoCurrTime());
		mp.setVideoState(player.isPlaying());
		Log.i("state",""+player.isPlaying());
		if(player.isPlaying())
			player.pause();
		else
		{
			player.start();
			player.pause();
		}
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_fullscreen, menu);
		return true;
	}

}
