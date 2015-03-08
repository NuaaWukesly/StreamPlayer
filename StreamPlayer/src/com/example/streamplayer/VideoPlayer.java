package com.example.streamplayer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.example.dataclass.CharTools;
import com.example.dataclass.MediaList;
import com.example.dataclass.SongInfo;
import com.example.dataclass.VideoInfo;
import com.example.dataclass.myFile;
import com.example.myadapter.MyFileAdapter;
import com.example.myadapter.VideoInfoListAdapter;
import com.example.myadapter.VideoListExpandableListAdapter;
import com.example.myapplication.MyApplication;
import com.example.mydatabase.MyDataBase;
import com.example.myreceiver.myReceiver;
import com.example.myservice.DownloadService;

/**
 *该Activity用于实现视频播放部分的相关功能。有最基本的视频播放器功能，
 *如快进、快退、播放/暂停，划屏切换界面，网上视频浏览，
 *播放列表的新建、选择、添加、删除，视频的增加和删除，
 *音量的调节，主题的变换，曲库的刷新，蓝牙发送，好友推荐
 *歌曲，视频下载，定时，流量统计功能。
 *@author Streamer
 *@see {@link VideoPlayer}
 */

@SuppressWarnings("deprecation")
@SuppressLint({ "UnlocalizedSms", "ShowToast", "SetJavaScriptEnabled", "HandlerLeak" })
public class VideoPlayer extends Activity implements OnGestureListener{
	
	/*
	 * 视频播放器的标题栏上的五个按钮声明
	 */
	/**
	 * 视频播放器标题栏的全屏显示界面选择按钮
	 * @see {@link  VideoPlayer#video_titlebar_fullscreen}
	 */
	ImageButton video_titlebar_fullscreen;
	/**
	 * 视频播放器标题栏的播放器主界面选择按钮
	 * @see {@link VideoPlayer#video_titlebar_video}
	 */
	ImageButton video_titlebar_video;
	/**
	 * 视频播放器标题栏的播放器列表界面选择按钮
	 * @see {@link VideoPlayer#video_titlebar_list}
	 */
	ImageButton video_titlebar_list;
	/**
	 * 视频播放器标题栏的网页浏览界面选择按钮
	 * @see {@link VideoPlayer#video_titlebar_online}
	 */
	ImageButton video_titlebar_online;
	/**
	 * 视频播放器与音乐播放器切换按钮
	 * @see {@link VideoPlayer#video_titlebar_music}
	 */
	ImageButton video_titlebar_music;
	
	/*
	 * 视频播放器的工具栏上的五个按钮声明
	 */
	
	/**
	 * 视频播放器工具栏的播放/暂停按钮
	 * @see {@link VideoPlayer#video_toolbar_play}
	 */
	ImageButton video_toolbar_play;
	/**
	 * 视频播放器工具栏的快退选择按钮
	 * @see {@link VideoPlayer#video_toolbar_goback}
	 */
	ImageButton video_toolbar_goback;
	/**
	 * 视频播放器工具栏的快进选择按钮
	 * @see {@link VideoPlayer#video_toolbar_forward}
	 */
	ImageButton video_toolbar_forward;
	/**
	 * 视频播放器工具栏的音量调节选择按钮
	 * @see {@link VideoPlayer#video_toolbar_voice}
	 */
	ImageButton video_toolbar_voice;
	/**
	 * 视频播放器工具栏的菜单选择按钮
	 * @see {@link VideoPlayer#video_toolbar_menu}
	 */
	ImageButton video_toolbar_menu;
	
	/*
	 * 视频播放器主界面布局定义，中间部分
	 */
	/**
	 * 划屏切换组布局，用于划屏切换
	 * @see {@link VideoPlayer#video_viewflipper}
	 */
	ViewFlipper video_viewflipper;
	/**
	 * 划屏切换页面一的视频播放主界面
	 * @see {@link VideoPlayer#player}
	 */
	VideoView player;
	/**
	 * 划屏切换页面一的视频播放进度条
	 * @see {@link VideoPlayer#seekbar}
	 */
	SeekBar seekbar;											
	/**
	 * 划屏切换页面一的播放视频名称显示
	 * @see {@link VideoPlayer#videotext}
	 */
	TextView videotext;									
	/**
	 * 划屏切换页面一的当前播放位置显示
	 * @see {@link VideoPlayer#videocurrtext}
	 */
	TextView videocurrtext;									
	/**
	 * 划屏切换页面一的视频长度显示
	 * @see {@link VideoPlayer#videolentext}
	 */
	TextView videolentext;		
	/**
	 * 划屏切换页面二的播放器列表显示页面，使用可扩展布局
	 * @see {@link VideoPlayer#video_listview}
	 */
	ExpandableListView video_listview;
	/**
	 * 划屏切换页面三的网页浏览页面
	 * @see {@link VideoPlayer#video_webview}
	 */
	WebView  video_webview;
	///////////////////////////////////////////////////////////////////////
	/**
	 * 划屏切换页面四正在播放列表
	 */
	LinearLayout page4;
	View page4_currplaylist_view;
	TextView currplaylistname_textview;
	ListView currplaylist_listview;
	/**
	 * 划屏切换页面五文件浏览
	 */
	/**
	 * 划屏切换页面五,线性布局存放view
	 */
	LinearLayout page5;
	View page5_scanfile_view;
	ListView scanfile_listview;
	/**
	 * 显示选择路径
	 */
	TextView scanfile_Textview;
	Button scanfile_return_bnt;
	Button scanfile_play_bnt;
	Button scanfile_ok_bnt;
	Button scanfile_cancel_bnt;
	///////////////////////////////////////////////////////
	
	/*
	 * 视频播放器的菜单布局定义
	 */
	/**
	 * 视频播放器的菜单窗口
	 * @see {@link VideoPlayer#mymemu}
	 */
	private PopupWindow mymemu;
	/**
	 * 视频播放器的菜单窗口主页面布局
	 * @see {@link VideoPlayer#menuView}
	 */
	View  menuView;
	/**
	 * 视频播放器的菜单窗口的划屏切换布局
	 * @see {@link VideoPlayer#menu_viewflipper}
	 */
	ViewFlipper menu_viewflipper;
	/**
	 * 视频播放器的菜单窗口的标题栏的常用菜单选择按钮
	 * @see {@link VideoPlayer#menu_common_bnt}
	 */
	Button menu_common_bnt;
	/**
	 * 视频播放器的菜单窗口的标题栏的设置菜单选择按钮
	 * @see {@link VideoPlayer#menu_setting_bnt}
	 */
	Button menu_setting_bnt;
	/**
	 * 视频播放器的菜单窗口的标题栏的帮组菜单选择按钮
	 * @see {@link VideoPlayer#menu_help_bnt}
	 */
	Button menu_help_bnt;
	/**
	 * 视频播放器的的菜单窗口常用菜单下的推荐好友选项
	 * @see {@link VideoPlayer#menu_friendpush_bnt}
	 */
	ImageButton menu_friendpush_bnt;
	/**
	 * 视频播放器的菜单窗口常用菜单下的改变主题选项
	 * @see {@link VideoPlayer#menu_change_theme}
	 */
	ImageButton menu_change_theme;
	/**
	 * 视频播放器的菜单窗口常用菜单下的刷新列表选项
	 * @see {@link VideoPlayer#menu_refresh_list}
	 */
	ImageButton menu_refresh_list;
	/**
	 * 视频播放器的菜单窗口常用菜单下的退出播放器选项
	 * @see {@link VideoPlayer#menu_exit}
	 */
	ImageButton menu_exit;
	/**
	 * 视频播放器的菜单窗口设置菜单下的网络接入选项
	 * @see {@link VideoPlayer#menu_online_check}
	 */
	ImageButton menu_online_check;
	/**
	 * 视频播放器的菜单窗口设置菜单下的蓝牙发送功能选项
	 * @see {@link VideoPlayer#menu_bluetooth_share}
	 */
	ImageButton menu_bluetooth_share;
	/**
	 * 视频播放器的菜单窗口设置菜单下的下载路径选择选项
	 * @see {@link VideoPlayer#menu_downloadpath}
	 */
	ImageButton menu_downloadpath;
	/**
	 * 视频播放器的菜单窗口设置菜单下的定时设置选项
	 * @see {@link VideoPlayer#menu_timing}
	 */
	ImageButton menu_timing;
	/**
	 * 视频播放器菜单窗口帮助菜单下的流量统计选项
	 * @see {@link VideoPlayer#menu_flux_statistics}
	 */
	ImageButton menu_flux_statistics;
	/**
	 * 视频播放器菜单窗口帮助菜单下的使用说明选项
	 * @see {@link  VideoPlayer#menu_useexplain}
	 */
	ImageButton menu_useexplain;
	/**
	 * 视频播放器菜单窗口帮助菜单下的意见反馈选项
	 * @see {@link  VideoPlayer#menu_opition_return}
	 */
	ImageButton menu_opition_return;
	/**
	 * 视频播放器菜单窗口帮助菜单下的开发团队选项
	 * @see {@link  VideoPlayer#menu_dev_team}
	 */
	ImageButton menu_dev_team;
	
	////////////////////////////////////////////////////////////////////////
	/**
	 * 视频播放器列表组长按弹出窗口
	 */
	private PopupWindow group_longpressForm;
	View group_longpressView;
	private Button group_longpress_addtolist;
	private Button group_longpress_del;
	private Button group_longpress_altername;
	private Button group_longpress_cancel;
	private Button group_longpress_clear;
	/**
	 * 视频播放器列表子长按弹出窗口
	 */
	private PopupWindow child_longpressForm;
	View child_longpressView;
	private Button child_longpress_addtolist;
	private Button child_longpress_remove;
	private Button child_longpress_altername;
	private Button child_longpress_songinfo;
	private Button chile_longpress_share;
	private Button child_longpress_cancel;
	
	/**
	 * 视频播放器主题选择弹出菜单
	 */
	
	private PopupWindow bgtheme_form;
	View bgtheme_choicView;
	private Button bgtheme_bg1;
	private Button bgtheme_bg2;
	private Button bgtheme_bg3;
	private Button bgtheme_bg4;
	private Button bgtheme_bg5;
	private Button bgtheme_bg6;
	private Button bgtheme_bg7;
	private Button bgtheme_bg8;
	private Button bgtheme_bg9;
	private Button bgtheme_bg10;
	private Button bgtheme_bg11;
	private Button bgtheme_bg12;
	////////////////////////////////////////////////////////////////////////

	/*
	 * 变量设计
	 *///////////////////////////////////////////////////////////
	/*
	 * 列表型变量
	 *///////////////////////////////////////////////////////////
	/**
	 * 用于存放所有的视频列表，包括本地视频、我的下载、我的最爱等列表
	 * @see {@link  VideoPlayer#main_video_list}
	 */
	List<MediaList<VideoInfo>> main_video_list;
	/**
	 * 用于存放视频列表
	 * @see {@link  VideoPlayer#video_list}
	 */
	List<VideoInfo> video_list;
	
	/*
	 * 适配器型变量
	 */////////////////////////////////////////////////////////////
	/**
	 * 定义一个适配器，用于显示正在播放的音乐列表，适配器类型为
	 * {@link VideoInfoListAdapter}
	 * @see {@link  VideoPlayer#curr_video_adapter}
	 */
	private VideoInfoListAdapter curr_video_adapter;
	/**
	 * 定义一个适配器，用于显示所有的视频列表，该列表为可扩展列表，
	 * 适配器类型为{@link  VideoPlayer#main_video_adapter}
	 */
	private VideoListExpandableListAdapter main_video_adapter;
	
	/**
	 * 用于显示文件，存储文件结构体
	 */
	private MyFileAdapter fileAdapter;
	/**
	 * 用于保存当前显示文件列表
	 */
	List<myFile> filelist;
	
	
	/*
	 * 布尔型变量
	 */////////////////////////////////////////////////////////////
	/**
	 * 用于记录菜单窗口的显示状态，显示时为true，否则为false
	 * @see {@link  VideoPlayer#menu_isShow}
	 */
	boolean menu_isShow = false;
	/**
	 * 用于记录视频播放器工具栏的的上一曲按钮的可用状态，可用时为true，
	 * 不可用时为false
	 * @see {@link  VideoPlayer#goback_isEnable}
	 */
	boolean goback_isEnable = false;
	/**
	 * 用于记录视频播放器工具栏的播放/暂停按钮的可用状态，可用时为true
	 * 不可用时为false
	 * @see {@link  VideoPlayer#play_isEnable}
	 */
	boolean play_isEnable = false;
	/**
	 * 用于记录视频播放器工具栏的快进/下一部按钮的可用状态，可用时为true
	 * 不可用时为false
	 * @see {@link  VideoPlayer#forward_isEnable}
	 */
	boolean forward_isEnable = false;
	/**
	 * 用于记录视频播放器是否处于播放状态，是为true，否则为false
	 * @see {@link  VideoPlayer#isplay}
	 */
	boolean isplay = false;
	/**
	 * 用于记录视频播放器是否处于暂停状态，即由播放状态进入暂停，
	 * 是时为true，不是为false
	 * @see {@link  VideoPlayer#ispause}
	 */
	boolean ispause = false;

	/*
	 * 整型数组变量
	 */////////////////////////////////////////////////////////////////
	/**
	 * 用于存放播放器工具栏的播放/暂停按钮在不同状态下的背景图标的id
	 * @see {@link VideoPlayer#video_toolbar_play_state}
	 */
	int []video_toolbar_play_state = new int[]
				{R.drawable.ic_bnt_play,R.drawable.ic_bnt_pause};
	
	/*
	 * 整型常量/变量
	 *//////////////////////////////////////////////////////////////////
	/**
	 * 常量、记录播放状态下的图标数组
	 * {@link VideoPlayer#video_toolbar_play_state}中对应的下标
	 * @see {@link VideoPlayer#STATE_PLAY}
	 */
	private final static int STATE_PLAY = 1;
	/**
	 * 常量、记录暂停状态下的图标数组
	 * {@link VideoPlayer#video_toolbar_play_state}中对应的下标
	 * @see {@link VideoPlayer#STATE_PAUSE}
	 */
	private final static int STATE_PAUSE = 0;
	/**
	 * 消息对列表之播放进程改变消息编号
	 * @see {@link VideoPlayer##PROGRESS_CHANGED}
	 */
	private final static int PROGRESS_CHANGED = 0;
	/**
	 * 消息队列之播放下一首消息处理
	 * @see {@link VideoPlayer#PLAYNEXT}
	 */
	private final static int PLAYNEXT = 1;
	/**
	 * 判断定时处理消息
	 * @see {@link VideoPlayer#TIMING}
	 */
	private final static int TIMING = 2;
	/**
	 * 记录当前播放的状态，1为播放状态，0为暂停状态
	 * @see {@link VideoPlayer#CURR_STATE}
	 */
	private int CURR_STATE = STATE_PAUSE;
	/**
	 * 记录当前播放的模式。
	 * @see {@link VideoPlayer#MODE_CURR_STATE}
	 */
	/*
	 * 记录设置的时间
	 */
	private int hour1 = 25;
	private int minute1;
	/*
	 * 记录当前打开的组号
	 */
	private int groupid;
	/**
	 * 记录下载路径
	 * @see {@link VideoPlayer#downloadpath}
	 */
	@SuppressWarnings("unused")
	private String downloadpath;
	/*
	 * 视频信息结构定义
	 */
	/**
	 * 记录当前正在播放的视频的详细信息
	 * @see {@link VideoPlayer#currVideo}
	 */
	private VideoInfo currVideo;
	
	/*
	 * 其他类定义
	 */
	/**
	 * 定义一个手势识别类，用于监听用户的划屏事件
	 * @see {@link VideoPlayer#detector}
	 */
	private GestureDetector detector;
	
	/*
	 * 我的应用声明
	 */
	/**
	 * 声明我的应用{@link MyApplication}，其中存放程序运行所需要的各种信息、数据库操作、
	 * 当退出应用时保存记录和数据，
	 * @see {@link VideoPlayer#mp}
	 */
	MyApplication mp;
	
	myReceiver receiver;
	View downloadview ;
	TextView dl_filename_text ;
	TextView dl_process_text ;
	Dialog dl_dialog;
	final static int DOWNLOAD = 5;
	
	/*
	 * 我的详细队列处理
	 */
	/**
	 * 消息队列处理方法，用于音乐播放器运行时产生的的消息
	 * @see {@link VideoPlayer#myHandler}
	 */
	ProgressDialog dialogn;
	
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
	
	
	Handler myHandler = new Handler() {
	
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub		
			switch (msg.what) 
			{
				case PROGRESS_CHANGED:
				{
					/*
					 * 音乐播放器播放进度该表消息处理
					 */
					if(player.isPlaying())
					{//当初在播放状态时才可以，不然error
						int i = player.getCurrentPosition();
						
						i /= 1000;
						int minute = i / 60;
						int hour = minute / 60;
						int second = i % 60;
						minute %= 60;
						videocurrtext.setText(String.format("%02d:%02d:%02d", hour,
							minute, second));
					}
					else
					{
						seekbar.setProgress(0);
					}
					//循环发送消息，实现更新
					sendEmptyMessage(PROGRESS_CHANGED);	
					break;
				}
				case PLAYNEXT:
				{
					/*
					 * 音乐播放器播放下一首消息处理
					 */
					currVideo = getNextVideo(currVideo);
					play(currVideo.getPath());
					break;
				}
				case TIMING:
				{
					if(hour1 == Calendar.getInstance().get(Calendar.HOUR)
							&&minute1 == Calendar.getInstance().get(Calendar.MINUTE))
					{
						VideoPlayer.this.finish();
					}
					sendEmptyMessage(TIMING);
					break;
				}
				case 4:
				{
					if(player.isPlaying())
					{
						if(player.getBufferPercentage()==0)
						{
							Log.i("buff", ""+player.getBufferPercentage());
							if(!dialogn.isShowing())
							{
								dialogn.show(VideoPlayer.this, "", "加载资源...");
								player.pause();
							}
						}
						else 
						{
							if(dialogn.isShowing())
								dialogn.dismiss();
							player.start();
							
						}
					}
					//myHandler.sendEmptyMessage(4);
					break;
				}
				
				case DOWNLOAD:
				{
					dl_filename_text.setText(filename);
					dl_process_text.setText(process);
					myHandler.sendEmptyMessage(DOWNLOAD);
					break;
				}
			}
			super.handleMessage(msg);
		}
	};
	/**
	 * 获取音量管理
	 * @see {@link VideoPlayer#audioManager}
	 */
	AudioManager audioManager;
	/**
	 * 播放器的view
	 * @see {@link VideoPlayer#showThemeView}
	 */
	View showThemeView;
	
	BroadcastReceiver myreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) 
        {
        	filename = intent.getStringExtra("filename");
    		state = intent.getStringExtra("state");
    		ctrl = intent.getStringExtra("ctrl");
    		size = intent.getIntExtra("size", 0);
    		process = intent.getStringExtra("process");
         }
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*
		 * 获取主布局view
		 */
		showThemeView =getLayoutInflater().inflate(R.layout.activity_video_player,null);
		/*
		 * 将窗口设置为无标题
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*
		 * 加载布局文件
		 */
		setContentView(showThemeView);
		/*
		 * 获取我的应用程序对象
		 */
		//receiver = new myReceiver();
		registerReceiver(myreceiver, new IntentFilter("MyReceiver"));
		downloadview = getLayoutInflater().inflate(R.layout.downloadlist, null);
		dl_filename_text = (TextView)downloadview.findViewById(R.id.dl_filename);
		dl_process_text = (TextView)downloadview.findViewById(R.id.dl_process);
		
		mp = (MyApplication)getApplication();
		/*
		 * 设置主布局的背景
		 */
		showThemeView.setBackgroundResource(mp.getMusicPlayer_bgID());
		/*
		 * 获取音量管理服务
		 */
		audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
		/*
		 * 实例化视频播放器标题栏的五个控件
		 */
		video_titlebar_fullscreen = (ImageButton)findViewById(R.id.video_fullscreen);
		video_titlebar_video = (ImageButton)findViewById(R.id.video_video);
		video_titlebar_list = (ImageButton)findViewById(R.id.video_list);
		video_titlebar_online = (ImageButton)findViewById(R.id.video_online);
		video_titlebar_music = (ImageButton)findViewById(R.id.video_music);
		/*
		 * 实例化视频播放器工具栏的五个控件
		 */
		video_toolbar_play = (ImageButton)findViewById(R.id.video_toolbar_play);
		video_toolbar_goback = (ImageButton)findViewById(R.id.video_toolbar_goback);
		video_toolbar_forward = (ImageButton)findViewById(R.id.video_toolbar_forward);
		video_toolbar_voice = (ImageButton)findViewById(R.id.video_toolbar_voice);
		video_toolbar_menu = (ImageButton)findViewById(R.id.video_toolbar_menu);
		/*
		 * 实例化划屏切换布局
		 */
		video_viewflipper = (ViewFlipper)findViewById(R.id.video_viewflipper);
		/*
		 * 划屏切换页面一
		 *//////////////////////////////////////////////////////////////////////
		//播放界面
		player = (VideoView)findViewById(R.id.video_player);
		seekbar = (SeekBar)findViewById(R.id.video_seekbar);
		videotext = (TextView)findViewById(R.id.video_showvideoname);
		videocurrtext = (TextView)findViewById(R.id.video_showvideocurr);
		videolentext = (TextView)findViewById(R.id.video_showvideolen);
		/*
		 * 划屏切换页面二
		 *////////////////////////////////////////////////////////////////////////
		video_listview = (ExpandableListView)findViewById(R.id.video_Listview);
		/*
		 * 划屏切换页面三
		 *///////////////////////////////////////////////////////////////////////
		video_webview = (WebView)findViewById(R.id.video_onlineweb);
		//////////////////////////////////////////////////////////////////////////
		/*
		 * 划屏切换页面四
		 */
		page4 = (LinearLayout)findViewById(R.id.video_page4);
		/*
		 * 当前播放列表页面
		 */
		page4_currplaylist_view = getLayoutInflater().inflate(R.layout.currplaylist_layout,null);
		currplaylistname_textview = (TextView)page4_currplaylist_view.findViewById(R.id.currlistname_textview);
		currplaylist_listview = (ListView)page4_currplaylist_view.findViewById(R.id.currlistview);
		//添加到page4 
		page4.addView(page4_currplaylist_view);
		/*
		 * 划屏切换页面五
		 */
		page5 = (LinearLayout)findViewById(R.id.video_page5);
		/*
		 * 文件浏览页面
		 */
		page5_scanfile_view = getLayoutInflater().inflate(R.layout.scanfilepath,null);
		scanfile_listview = (ListView)page5_scanfile_view.findViewById(R.id.scanfile_listview);
		scanfile_Textview = (TextView)page5_scanfile_view.findViewById(R.id.scanfile_path_textview);
		scanfile_return_bnt = (Button)page5_scanfile_view.findViewById(R.id.scanfile_return_bnt);
		scanfile_play_bnt = (Button)page5_scanfile_view.findViewById(R.id.scanfile_play_bnt);
		scanfile_ok_bnt = (Button)page5_scanfile_view.findViewById(R.id.scanfile_ok_bnt);
		scanfile_cancel_bnt =(Button)page5_scanfile_view.findViewById(R.id.scanfile_cancel_bnt);
		page5.addView(page5_scanfile_view);
		scanfile_ok_bnt.setEnabled(false);
		scanfile_play_bnt.setEnabled(false);
		/////////////////////////////////////////////////////////////////////////
		/*
		 * 实例化菜单窗口布局
		 */
		menuView = this.getLayoutInflater().inflate(R.layout.video_menu_layout, null);
		menu_viewflipper = (ViewFlipper)menuView.findViewById(R.id.video_menu_viewflipper);
		menu_common_bnt = (Button)menuView.findViewById(R.id.video_menu_common);
		menu_setting_bnt = (Button)menuView.findViewById(R.id.video_menu_setting);
		menu_help_bnt = (Button)menuView.findViewById(R.id.video_menu_help);
		menu_friendpush_bnt = (ImageButton)menuView.findViewById(R.id.video_menu_frendpush);
		menu_change_theme = (ImageButton)menuView.findViewById(R.id.video_menu_theme);
		menu_refresh_list = (ImageButton)menuView.findViewById(R.id.video_menu_refresh);
		menu_exit = (ImageButton)menuView.findViewById(R.id.video_menu_exit);
		menu_online_check = (ImageButton)menuView.findViewById(R.id.video_menu_online);
		menu_bluetooth_share = (ImageButton)menuView.findViewById(R.id.video_menu_bluetooth);
		menu_downloadpath = (ImageButton)menuView.findViewById(R.id.video_menu_savepath);
		menu_timing = (ImageButton)menuView.findViewById(R.id.video_menu_timing);
		menu_flux_statistics = (ImageButton)menuView.findViewById(R.id.video_menu_flux);
		menu_useexplain = (ImageButton)menuView.findViewById(R.id.video_menu_uesexplain);
		menu_opition_return = (ImageButton)menuView.findViewById(R.id.video_menu_opinion);
		menu_dev_team = (ImageButton)menuView.findViewById(R.id.video_menu_devteam);
		///////////////////////////////////////////////////////////////////////////////////
		/*
		 * 初始化歌曲列表组长按事件弹出窗口
		 */
		group_longpressView = getLayoutInflater().inflate(R.layout.music_group_longpress_layout, null);
		group_longpress_addtolist = 
				(Button)group_longpressView.findViewById(R.id.music_list_group_addtolist);
		group_longpress_del = 
				(Button)group_longpressView.findViewById(R.id.music_list_group_del);
		group_longpress_altername = 
				(Button)group_longpressView.findViewById(R.id.music_list_group_altername);
		group_longpress_cancel = 
				(Button)group_longpressView.findViewById(R.id.music_list_group_cancelbnt);
		group_longpress_clear = 
				(Button)group_longpressView.findViewById(R.id.music_list_group_clear);
		group_longpressForm = new PopupWindow
				(group_longpressView, getWindowManager().getDefaultDisplay().getWidth()-40, 200);
		/*
		 * 初始化歌曲列表子长按键事件弹出窗口
		 */
		child_longpressView = 
				getLayoutInflater().inflate(R.layout.music_child_longpress_layout,null);
		child_longpress_addtolist = 
				(Button)child_longpressView.findViewById(R.id.music_list_child_addtolist);
		child_longpress_altername =
				(Button)child_longpressView.findViewById(R.id.music_list_child_altername);
		child_longpress_cancel = 
				(Button)child_longpressView.findViewById(R.id.music_list_child_cancelbnt);
		child_longpress_remove = 
				(Button)child_longpressView.findViewById(R.id.music_list_child_remove);
		child_longpress_altername.setEnabled(false);
		child_longpress_songinfo = 
				(Button)child_longpressView.findViewById(R.id.music_list_child_songinfo);
		chile_longpress_share =
				(Button)child_longpressView.findViewById(R.id.music_list_child_share);
		child_longpressForm = new PopupWindow
				(child_longpressView, getWindowManager().getDefaultDisplay().getWidth()-40, 200);
		/*
		 * 初始化选择主题窗口
		 */
		bgtheme_choicView = getLayoutInflater().inflate(R.layout.bgtheme_choice,null);
		bgtheme_bg1 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg1);
		bgtheme_bg2 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg2);
		bgtheme_bg3 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg3);
		bgtheme_bg4 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg4);
		bgtheme_bg5 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg5);
		bgtheme_bg6 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg6);
		bgtheme_bg7 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg7);
		bgtheme_bg8 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg8);
		bgtheme_bg9 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg9);
		bgtheme_bg10 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg10);
		bgtheme_bg11 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg11);
		bgtheme_bg12 = (Button)bgtheme_choicView.findViewById(R.id.bgtheme_bg12);
		bgtheme_form = new PopupWindow
				(bgtheme_choicView, getWindowManager().getDefaultDisplay().getWidth()-40, 200);
		///////////////////////////////////////////////////////////////////////////////////
		/*
		 * 变量的初始化
		 */
		//初始化视频播放器的播放列表,适配器和listview
		initialPlayList();
		dialogn = new ProgressDialog(VideoPlayer.this);
		mymemu = new PopupWindow(menuView, LayoutParams.FILL_PARENT, 150);
		main_video_adapter = new VideoListExpandableListAdapter(this, main_video_list);
		/*
		 * 设置一些变量的初始状态
		 */
		if(mp.getLastVideoIndex()<video_list.size()&&mp.getLastVideoIndex()>=0)
		{
			currVideo = video_list.get(mp.getLastVideoIndex());
			//play("rtsp://217.146.95.166:554/live/ch6bqvga.3gp");//currVideo.getPath()
			//pause();
			myHandler.sendEmptyMessage(PROGRESS_CHANGED);
			video_viewflipper.setDisplayedChild(0);	//初始时显示第一页
			video_titlebar_video.setBackgroundResource(R.drawable.ic_video_checked);
		}
		else
		{
			video_toolbar_play.setEnabled(false);
			seekbar.setEnabled(false);
			video_toolbar_forward.setEnabled(false);
			video_toolbar_goback.setEnabled(false);
			video_viewflipper.setDisplayedChild(3);	//初始时显示第四页
		}
		/*
		 * 加载初始资源
		 */
		mymemu.setAnimationStyle(R.style.menuAnimation);
		video_toolbar_play.setBackgroundResource(R.drawable.ic_bnt_play);
		video_listview.setIndicatorBounds(
				getWindowManager().getDefaultDisplay().getWidth()-40, 
				getWindowManager().getDefaultDisplay().getWidth()-9);
		//显示网页
		showWebView();
		//显示文件浏览
		filelist = getFile("/");
		
		/**
		 * 划屏切换页面用户触碰监听事件处理,将事件交由手势识别类处理
		 * @author Streamer
		 */
		video_viewflipper.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return detector.onTouchEvent(event);
			}
		});
		
		/**
		 * 手势识别类的实例化，用于处理用户操作屏幕事件
		 * @author Streamer
		 */
		detector = new GestureDetector(this,new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				//用户轻触屏幕后松开。
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				//用户轻触屏幕，尚末松开或拖动，注意，强调的是没有松开或者拖动状态 
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				//用户按下屏幕并拖动 
				/*
				 if(video_viewflipper.getCurrentView().getId() == R.id.video_page1)
	                {
	                	int y = (int)(e1.getY()-e2.getY());
	                	if(y>20&&isplay)
	                	{
	                		//用户往上滑
	                		Log.i("往上滑", ""+y);
	                		player.pause();
	                		int d = player.getCurrentPosition()+40*y;
	                		if(d<player.getDuration())
	                			{
	                				player.seekTo(d);
	                				seekbar.setProgress(d);
	                			}
	                		else
	                			{
	                				seekbar.setProgress(player.getDuration()-10);
	                				player.seekTo(player.getDuration()-10);
	                			}
	                		player.start();
	                		
	                	}
	                	if(y<-20&&isplay)
	                	{
	                		//用户往下滑
	                		Log.i("往上滑", ""+y);
	                		player.pause();
	                		//此时y为负数
	                		int d = player.getCurrentPosition()+40*y;
	                		if(d>10)
	                		{
	                			seekbar.setProgress(d);
	                			player.seekTo(d);
	                		}
	                		else
	                			{
	                				seekbar.setProgress(10);
	                				player.seekTo(10);
	                			}
	                		player.start();
	                	}
	                	
	                }
				 */
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				//用户长按屏幕  
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				// TODO Auto-generated method stub
				
				//用户按下屏幕，快速移动后松开（就是在屏幕上滑动）  
                //e1:第一个ACTION_DOWN事件（手指按下的那一点）  
                //e2:最后一个ACTION_MOVE事件 （手指松开的那一点）  
                //velocityX:手指在x轴移动的速度 单位：像素/秒  
                //velocityY:手指在y轴移动的速度 单位：像素/秒  
				
				/*
				 * 用户滑动屏幕是发生此事件。
				 * 需要做出的响应：当用户滑动屏幕幅度达到一定程度是，根据用户的滑动方
				 * 向切换到相应页面，并改变其他按钮的背景图片；如果菜单处于显示状态同
				 * 时将菜单窗口关闭；当音乐播放器处于播放状态时划屏失效，即不做响应。
				 */
				float x = (float) (e2.getX() - e1.getX()); 
				//注意设置判断滑动合适相应：进度条未显示时 即为 musicplayer出入未播放状态时
				//方法参照 videoplayer的相应方法
                if(x>200)
                { 
                    video_viewflipper.setInAnimation(VideoPlayer.this,R.anim.push_right_in); 
                    video_viewflipper.setOutAnimation(VideoPlayer.this,R.anim.push_right_out ); 
                    video_viewflipper.showPrevious();      
                }
                if(x<-200)
                { 
                	video_viewflipper.setInAnimation(VideoPlayer.this,R.anim.push_left_in ); 
                    video_viewflipper.setOutAnimation(VideoPlayer.this,R.anim.push_left_out); 
                    video_viewflipper.showNext();
                }
                change_titlebar_bnt_bg();
                if(mymemu.isShowing())
                {
                	mymemu.dismiss();
        			video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
        			menu_isShow = false;
                }
				return false;
			}//End
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				//用户轻触屏幕，单击
				if(mymemu.isShowing())
				{
					mymemu.dismiss();
					video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				}
				if(group_longpressForm.isShowing())
					group_longpressForm.dismiss();
				if(child_longpressForm.isShowing())
					child_longpressForm.dismiss();
				if(bgtheme_form.isShowing())
					bgtheme_form.dismiss();
				if(dialogn.isShowing())
					dialogn.dismiss();
				return false;
			}
		});//End
	
		/**
		 * 视频列表集合的展开列表中的视频点击事件处理
		 * 播放点击的视频，将页面切换到播放页面，改变
		 * 响应控件图标
		 * @author Streamer
		 */
		
		video_listview.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Log.i("播放曲目路径", video_list.get(childPosition).getPath());
				currVideo = main_video_list.get(groupPosition).getList().get(childPosition);
				play(currVideo.getPath());
				video_viewflipper.setDisplayedChild(0);
				change_titlebar_bnt_bg();
				CURR_STATE = STATE_PLAY;
				video_toolbar_play.setBackgroundResource(video_toolbar_play_state[CURR_STATE]);
				return false;
			}
		});
		
		/**
		 * 所有媒体列表的组项目点击事件
		 * @author Streamer
		 */
		video_listview.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：展开该音乐列表
				 */
				
				if(groupPosition==main_video_list.size()-1)
				{
					/*
					 * 点击的是新建列表
					 */
					final EditText editnewlist = new EditText(VideoPlayer.this);
					editnewlist.setHeight(70);
					editnewlist.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
					editnewlist.setGravity(Gravity.LEFT);
					Dialog dialog = new AlertDialog.Builder(VideoPlayer.this)
							.setTitle("新建列表名")
							.setView(editnewlist)
							.setNegativeButton("取消", 
									new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).setPositiveButton("新建",  new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									MediaList<VideoInfo> newlist = new MediaList<VideoInfo>
									(editnewlist.getText().toString(), 0, new ArrayList<VideoInfo>());
									if(mp.newVideoListByListName(newlist))
									{
										showInfo("列表建立成功！");
										initialPlayList();
									}
									else
									{
										showInfo("列表建立失败！");
									}
									dialog.dismiss();
								}
							}).create();
					dialog.show();
					
					return true;
				}
				return false;				//返回true时下级菜单不显示
			}
		});	//End
		
		/**
		 * 之展开一个组，关闭其他展开的组
		 * @author Streamer
		 */
		video_listview.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				/*
				 * 获取展开的组号
				 */
				groupid = groupPosition;
				for(int i=0;i<main_video_list.size();i++)
				{
					if(i!=groupPosition)
					{
						video_listview.collapseGroup(i);
					}
				}
			}
		});//End
		
		/**
		 * 当前播放列表项目点击事件
		 * @author Streamer
		 */
		currplaylist_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 //TODO Auto-generated method stub
				/*
				 * 需要作出的响应：播放选择的曲目，并作图标转换，状态记录
				 */
				if(arg2<video_list.size())
				{
					
					currVideo = video_list.get(arg2);
					play(currVideo.getPath());
					CURR_STATE = STATE_PLAY;
					video_viewflipper.setDisplayedChild(0);
					change_titlebar_bnt_bg();
					video_toolbar_play.setBackgroundResource(video_toolbar_play_state[CURR_STATE]);
				}
				else
				{
					showInfo("播放失败！");
				}
			}
		});//End
		
		/**
		 * 监听可扩展列表的长按事件,通过判断view的id可以判断是长按组还是长按子
		 * @author Streamer
		 */
		video_listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int index = arg2;
				if(arg1.getId() == R.id.music_listgroup)
				{
					//长按组
					if(arg2!=main_video_list.size()-1)
					{
						/*
						 * 初始化一些功能的可用状态
						 */
						if(arg2<=5)
						{
							/*
							 * 系统预设列表不可变更
							 */
							group_longpress_altername.setEnabled(false);
							group_longpress_del.setEnabled(false);
						}
						else
						{
							group_longpress_altername.setEnabled(true);
							group_longpress_del.setEnabled(true);
						}
						if(arg2 == 3)
						{
							/*
							 * 默认列表不可清除
							 */
							group_longpress_clear.setEnabled(false);
						}
						else
						{
							group_longpress_clear.setEnabled(true);
						}
						group_longpressForm.showAsDropDown(arg1, 20, 10);
					}//End
					/**
					 * 长按媒体列表集合事件，播放该列表
					 * @author Streamer
					 */
					group_longpress_addtolist.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							video_list = main_video_list.get(index).getList();
							mp.getAllVideoList().get(3).getList().clear();
							mp.getAllVideoList().get(3).getList().addAll(video_list);
							initialPlayList();
							group_longpressForm.dismiss();
						}
					});//End
					/**
					 * 长按媒体列表集合事件，更改列表名称
					 * @author Streamer
					 */
					group_longpress_altername.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							final EditText editnewlist = new EditText(VideoPlayer.this);
							editnewlist.setHeight(70);
							editnewlist.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
							editnewlist.setGravity(Gravity.LEFT);
							Dialog dialog = new AlertDialog.Builder(VideoPlayer.this)
							.setTitle("新列表名")
							.setView(editnewlist)
							.setNegativeButton("取消", 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									})
							.setPositiveButton("更改", 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											mp.getAllVideoList()
											.get(index)
											.setListName(editnewlist.getText().toString());
											initialPlayList();
											dialog.dismiss();
										}
									}).create();
							dialog.show();
						}
					});//End
					/**
					 * 长按媒体列表集合事件，删除列表
					 * @author Streamer
					 */
					group_longpress_del.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mp.getAllVideoList().remove(index);
							group_longpressForm.dismiss();
							initialPlayList();
						}
					});//End
					/**
					 * 长按媒体列表集合事件，清除该列表
					 * @author Streamer
					 */
					group_longpress_clear.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mp.getAllVideoList().get(index).getList().clear();
							initialPlayList();
							group_longpressForm.dismiss();
						}
					});//End
					/**
					 * 长按媒体列表集合事件，取消操作
					 * @author Streamer
					 */
					group_longpress_cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							group_longpressForm.dismiss();
						}
					});//End
					//Log.i("组名", ((TextView)arg1.findViewById(R.id.music_mainlist_exlist_textview)).getText().toString());
				}//End if
				/*
				 * 如果长按的是子元素
				 */
				else if(arg1.getId() == R.id.music_listchild)
				{
					//长按子元素
					child_longpressForm.showAsDropDown(arg1, 20, 10);
					/**
					 * 长按列表集合中的子项的添加到列表功能
					 * @author Streamer
					 */
					child_longpress_addtolist.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String [] listsname = new String[main_video_list.size()-1];
							/*
							 * 获取所有列表名称
							 */
							for(int i=0;i<main_video_list.size()-1;i++)
							{
								listsname[i] = main_video_list.get(i).getListName();
							}
							Dialog dialog = new AlertDialog.Builder(VideoPlayer.this)
							.setTitle("添加到")
							.setItems(listsname, 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											main_video_list.get(which).getList().add
											((VideoInfo)video_listview.getItemAtPosition(index));
										}
									})
							.setNegativeButton("取消", 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									}).create();
							dialog.show();
							child_longpressForm.dismiss();
						}
					});//End
					
					/**
					 * 长按列表集合中的子项的更改名字功能
					 * @author Streamer
					 */
					child_longpress_altername.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							child_longpressForm.dismiss();
							
							final EditText editnewlist = new EditText(VideoPlayer.this);
							editnewlist.setHeight(70);
							editnewlist.setText(((VideoInfo)video_listview.getItemAtPosition(index)).getTitle());
							editnewlist.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
							editnewlist.setGravity(Gravity.LEFT);
							Dialog dialog = new AlertDialog.Builder(VideoPlayer.this)
							.setTitle("新视频名")
							.setView(editnewlist)
							.setNegativeButton("取消", 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									})
							.setPositiveButton("更改",  
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											
											final int i = mp.getAllVideoList()
													.get(groupid)
													.getList()
													.indexOf((SongInfo)video_listview
															.getItemAtPosition(index));
											final File file = new File(mp.getAllVideoList()
													.get(groupid)
													.getList()
													.get(i)
													.getPath());
											String modname = editnewlist.getText().toString();  
											final String pFile = file.getParentFile().getPath()  
				                                    + "/";  
				                            final String newPath = pFile + modname+".mp4";  
				                            // 判断文件是否存在   
				                            if (new File(newPath).exists()) 
				                            {  
				                                // 排除修改文件时没修改直接送出的情况   
				                                if (!modname.equals(file.getName())) 
				                                {  
				                                    // 跳出警告 文件名重复，并确认时候修改   
				                                    new AlertDialog.Builder(  
				                                           VideoPlayer.this)  
				                                            .setTitle("警告！")  
				                                            .setMessage("文件已存在，是否要覆盖？")  
				                                            .setPositiveButton("确定",
				                                            		new DialogInterface.OnClickListener() {  
				  
				                                                        @Override  
				                                                        public void onClick(  
				                                                                DialogInterface dialog,  
				                                                                int which)
				                                                        {  
				                                                            // TODO   
				                                                            // Auto-generated   
				                                                            // method stub   
				                                                            // 单击确定，覆盖原来的文件   
				                                                            file.renameTo(new File(  
				                                                                    newPath));
				                                                            /*
				                                                             * 更改该媒体数据
				                                                             */
				                                                            mp.getAllVideoList().get(groupid).getList().get(i).
				            												setDisplayName(editnewlist.getText().toString());
				            												mp.getAllVideoList().get(groupid).getList().get(i).
				            												setTitle(editnewlist.getText().toString());
				            												initialPlayList();
				                                                            
				                                                        }
				                                                    })
				                                            .setNegativeButton(  
				                                                            "取消",  
				                                                            new DialogInterface.OnClickListener() {  
				          
				                                                                @Override  
				                                                                public void onClick(  
				                                                                        DialogInterface dialog,  
				                                                                        int which) {  
				                                                                    // TODO   
				                                                                    // Auto-generated   
				                                                                    // method stub   
				                                                                }  
				                                                            }).show();//最里层的对话框
				                                }//if
				                            }//if
											dialog.dismiss();
										}//点击确定事件处理结束
									}).create();//最外层
							dialog.show();//最外层
						}
					});//End
					/**
					 * 取消操作
					 * @author Streamer
					 */
					child_longpress_cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							child_longpressForm.dismiss();
						}
					});//End
					/**
					 * 长按列表集合中的子项的移除列表功能
					 * @author Streamer
					 */
					child_longpress_remove.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							/*
							 * 移除选中媒体
							 */
							mp.getAllVideoList()
							.get(groupid)
							.getList()
							.remove(main_video_list.get(groupid).
									getList()
									.indexOf((VideoInfo)video_listview
											.getItemAtPosition(index)));
							initialPlayList();
							child_longpressForm.dismiss();	
						}
					});//End
					/**
					 * 长按列表集合中的子项显示媒体信息功能
					 * @author Streamer
					 */
					child_longpress_songinfo.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							child_longpressForm.dismiss();
							final VideoInfo s = (VideoInfo)video_listview.getItemAtPosition(index);
							Dialog dialog = new AlertDialog.Builder(VideoPlayer.this)
							.setTitle("媒体信息")
							.setItems(new String[]
											{
											 "文件名："+s.getTitle(),
											 "专辑名："+s.getAlbum(),
											 "类   型："+s.getMimeType(),
											 "艺术家："+s.getArtist(),
											 "时   长："+s.getDuration()+"ms",
											 "大   小："+s.getSize()+"byte",
											 "路	径："+s.getPath()
											 }, null)
							.setPositiveButton("确定", 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									})
							.create();
							dialog.show();
						}
					});//End
					/**
					 * 长按列表集合中的子项的分享功能
					 * @author Streamer
					 */
					chile_longpress_share.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							child_longpressForm.dismiss();
							/*
							 * 显示系统消息
							 */
							showSysInfo("该功能在暂不可用！");
						}
					});
					//Log.i("子名",((TextView)arg1.findViewById(R.id.songinfo_textview01)).getText().toString());
				}//End
				/*
				 * 返回true，不给系统传递按键信息，防止与click冲突
				 */
				return true;
			}
		});//End
		
		/**
		 * 全屏播放
		 * @author Streamer
		 */
		video_titlebar_fullscreen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if((!currVideo.getPath().isEmpty())&&isplay)
				{
					player.pause();
					Intent intent = new Intent(VideoPlayer.this, FullscreenActivity.class);
					intent.putExtra("path", currVideo.getPath());
					intent.putExtra("currtime", player.getCurrentPosition());
					startActivity(intent);
				}
			}
		});//End
		
		/**
		 * 主页面显示
		 * @author Streamer
		 */
		video_titlebar_video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(video_viewflipper.getCurrentView().getId() == R.id.video_page1)
				{
					//如果不是正确的页面
				}
				else
				{
					video_viewflipper.setDisplayedChild(0);
				}
		        change_titlebar_bnt_bg();
			}
		});//End
		
		/**
		 * 显示列表按钮点击事件
		 * @author Streamer
		 */
		video_titlebar_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(video_viewflipper.getCurrentView().getId() == R.id.video_Listview)
				{
					/*
					 * 如果是主列表，则显示长在播放列表，否则显示列表集合
					 */
					video_viewflipper.setDisplayedChild(3);
				}
				else
				{
					video_viewflipper.setDisplayedChild(1);
				}
		        change_titlebar_bnt_bg();
			}
		});//End
	
		/**
		 * 选择网络浏览页面事件
		 * @author Streamer
		 */
		video_titlebar_online.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(video_viewflipper.getCurrentView().getId() == R.id.video_onlineweb)
				{
					//如果不是正确的页面
				}
				else
				{
					video_viewflipper.setDisplayedChild(2);
				}
				//change titlebar every button image
		        change_titlebar_bnt_bg();
			}
		});//End

		/**
		 * 进入音乐播放器按钮点击事件
		 * @author Streamer
		 */
		video_titlebar_music.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent musicIntent = new Intent();
				musicIntent.setClass(VideoPlayer.this,MusicPlayer.class);
				VideoPlayer.this.finish();
				VideoPlayer.this.startActivity(musicIntent);	
			}
		});//End
	
		/**
		 * 点击播放按钮事件
		 * @author Streamer
		 */
		video_toolbar_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：如果处于播放状态，则暂停，否则播放，并记录播放器的播放状态
				 *
				 * 
				 */
				if(CURR_STATE==STATE_PAUSE)
				{
					if(ispause)		//是处于暂停状态
					{
						pause();
						CURR_STATE = STATE_PLAY;
					}
				}
				else 
				{
					pause();
					CURR_STATE = STATE_PAUSE;
				}
				//改变该按钮的背景
				seekbar.setEnabled(true);
				video_toolbar_play.setBackgroundResource
				(video_toolbar_play_state[CURR_STATE]);
			}
		});//End
		
		/**
		 * 快退按钮点击事件，快退
		 * @author Streamer
		 */
		video_toolbar_goback.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(isplay)
				{
					int curr = player.getCurrentPosition();
					if(curr-5000>0)
					{
						player.seekTo(curr-5000);
						seekbar.setProgress(curr-5000);
					}
					else
					{
						player.seekTo(0);
						seekbar.setProgress(0);
					}
				}
				/*
				 * 事件不返回，防止与点击事件冲突
				 */
				return true;
			}
		});//End
		
		/**
		 * 快退按钮长按事件，播放上一媒体
		 * @author Streamer
		 */
		video_toolbar_goback.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				
				if(!getPreVideo(currVideo).getPath().isEmpty())
				{
					currVideo = getPreVideo(currVideo);
					play(currVideo.getPath());
				}
				return false;
			}
		});//End
		
		/**
		 * 前进按钮点击事件，快进
		 * @author Streamer
		 */
		video_toolbar_forward.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(isplay)
				{
					int curr = player.getCurrentPosition();
					if(curr+5000>player.getDuration())
					{
						player.seekTo(player.getDuration());
						seekbar.setProgress(player.getDuration());
					}
					else
					{
						player.seekTo(curr+5000);
						seekbar.setProgress(curr+5000);
					}
				}
				return false;
			}
		});//End
		
		/**
		 * 长按前进按钮事件，播放下一媒体
		 * @author Streamer
		 */
		video_toolbar_forward.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(!getNextVideo(currVideo).getPath().isEmpty())
				{
					currVideo = getNextVideo(currVideo);
					play(currVideo.getPath());
				}
				return false;
			}
		});//End
		
		/**
		 * 声音调节按钮点击事件
		 * @author Streamer 
		 */
		video_toolbar_voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 事件处理：当媒体音量可调的时候弹出音乐音量调节窗口，或关闭窗口
				 * 以及记录音量调节窗口的状态
				 */
				 audioManager.adjustStreamVolume(
				           AudioManager.STREAM_MUSIC,
				           AudioManager.ADJUST_SAME,
				           AudioManager.FLAG_PLAY_SOUND |AudioManager.FLAG_SHOW_UI);
			}
		});//End
		
		/**
		 * 选择菜单按钮点击事件
		 * @author Streamer
		 */
		video_toolbar_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(menu_isShow)
				{
					mymemu.dismiss();
					video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
					menu_isShow = false;
				}
				else
				{
					
					mymemu.showAtLocation(video_toolbar_menu, Gravity.BOTTOM, 0, 80);
					video_toolbar_menu.setBackgroundResource(R.drawable.ic_menu_checked);
					menu_viewflipper.setDisplayedChild(0);
					change_menu_bg();
					menu_isShow = true;
				}
			}
		});//End

		/**
		 * 选择常见菜单
		 * @author Streamer
		 */
		menu_common_bnt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				menu_viewflipper.setDisplayedChild(0);
				change_menu_bg();
				return false;
			}
		});//End
		
		/**
		 * 选择设置菜单
		 * @author Streamer
		 */
		menu_setting_bnt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				menu_viewflipper.setDisplayedChild(1);
				change_menu_bg();
				return false;
			}
		});//End
		
		/**
		 * 选择帮组菜单
		 * @author Streamer
		 */
		menu_help_bnt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				menu_viewflipper.setDisplayedChild(2);
				change_menu_bg();
				return false;
			}
		});//End
		
		/**
		 * 选择推荐朋友
		 * @author Streamer
		 */
		menu_friendpush_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * 显示系统信息
				 */
				showSysInfo("该功能暂不可用！");
			}
		});//End
		
		/**
		 * 选择改变主题
		 * @author Streamer
		 */
		menu_change_theme.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mymemu.dismiss();
				bgtheme_form.showAtLocation(bgtheme_choicView, Gravity.CENTER, 0, 0);
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
			}
		});
		/**
		 * 以下为改变播放器背景
		 * @author Streamer
		 */
		bgtheme_bg1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.bg);
				showThemeView.setBackgroundResource(R.drawable.bg);
			}
		});
		
		bgtheme_bg2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.bg1);
				showThemeView.setBackgroundResource(R.drawable.bg1);
			}
		});
		bgtheme_bg3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.bg2);
				showThemeView.setBackgroundResource(R.drawable.bg2);
			}
		});
		bgtheme_bg4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.bg3);
				showThemeView.setBackgroundResource(R.drawable.bg3);
			}
		});
		bgtheme_bg5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.bg4);
				showThemeView.setBackgroundResource(R.drawable.bg4);
			}
		});
		bgtheme_bg6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.ic_bg1);
				showThemeView.setBackgroundResource(R.drawable.ic_bg1);
			}
		});
		bgtheme_bg7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.ic_bg2);
				showThemeView.setBackgroundResource(R.drawable.ic_bg2);
			}
		});
		bgtheme_bg8.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.ic_bg3);
				showThemeView.setBackgroundResource(R.drawable.ic_bg3);
			}
		});
		bgtheme_bg9.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.ic_bg4);
				showThemeView.setBackgroundResource(R.drawable.ic_bg4);
			}
		});
		bgtheme_bg10.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.ic_bg5);
				showThemeView.setBackgroundResource(R.drawable.ic_bg5);
			}
		});
		bgtheme_bg11.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.ic_bg6);
				showThemeView.setBackgroundResource(R.drawable.ic_bg6);
			}
		});
		bgtheme_bg12.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgtheme_form.dismiss();
				mp.setMusicPlayer_bgID(R.drawable.ic_bg7);
				showThemeView.setBackgroundResource(R.drawable.ic_bg7);
			}
		});//End
		
		/**
		 * 刷新本地音乐列表
		 * @author Streamer
		 */
		menu_refresh_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/*
				 * 需要作出的响应：刷新曲库
				 */
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * 刷新应用的数据
				 */
				mp.refreshLocalVideoInfo(VideoPlayer.this);
				mp.getAllVideoList().get(0).getList().clear();
				mp.getAllVideoList().get(0).getList().addAll(mp.getLocalVideoInfo());
				initialPlayList();
			}
		});//End

		/**
		 * 选择退出程序
		 * @author Streamer
		 */
		menu_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 显示退出提示窗口
				 */
				showExitInfo();
			}
		});//End

		/**
		 * 检查网络功能
		 * @author Streamer
		 */
		menu_online_check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：网络检查
				 */
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				
				/*
				 * 显示系统消息
				 */
				showSysInfo("该功能暂不可用！");
			}
		});//End
		
		/**
		 * 蓝牙发送功能
		 * @author Streamer
		 */
		menu_bluetooth_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * 显示系统消息
				 */
				showSysInfo("该功能暂不可用！");
			}
		});//End
		
		/**
		 * 选择文件下载保存路径
		 * @author Streamer
		 */
		menu_downloadpath.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：下载路径选择
				 */
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * 设置文件浏览的确定键可用
				 */
				scanfile_ok_bnt.setEnabled(true);
				/*
				 * 进入文件浏览界面
				 */
				video_viewflipper.setDisplayedChild(4);
			}
		});//End

		/**
		 * 文件浏览，点击文件事件
		 * @author Streamer
		 */
		scanfile_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(filelist.size()>arg2&&arg2>=0)
				{
					File file = new File(filelist.get(arg2).getFilePath());
					if(file.canRead())
				    {
						if (file.isDirectory())
						{
							/* 如果是文件夹就再进去读取 */
							filelist = getFile(filelist.get(arg2).getFilePath());
							scanfile_ok_bnt.setEnabled(true);
						}
						else
						{
							/* 如果是文件*/ 
							scanfile_ok_bnt.setEnabled(false);
							scanfile_Textview.setText(filelist.get(arg2).getFilePath());
							if(file.getPath().endsWith(".mp4")
									||file.getPath().endsWith(".MP4"))
							{
								scanfile_play_bnt.setEnabled(true);
							}
							else
							{
								scanfile_play_bnt.setEnabled(false);
							}
						}
				    }
				    else
				    {
				      /* 弹出AlertDialog显示权限不足 */
				    	showInfo("没有权限");
				    }
				  }
			}
		});//End
		
		/**
		 * 文件浏览，返回键点击事件
		 * @author Streamer
		 */
		scanfile_return_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!scanfile_Textview.getText().toString().equals("/"))
				{
					/*
					 * 如果还有上级文件夹，返回上一级
					 */
					filelist = getFile(new File(scanfile_Textview.getText().toString()).getParent());
					scanfile_ok_bnt.setEnabled(false);
				}
				else
				{
					/*
					 * 如果已无上级文件夹，返回主界面
					 */
					video_viewflipper.setDisplayedChild(0);
					change_titlebar_bnt_bg();
				}
			}
		});//End
		
		/**
		 * 文件浏览，播放键点击事件
		 * @author Streamer
		 */
		scanfile_play_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					File file = new File(scanfile_Textview.getText().toString());
					/*
					 * 构建一个新的媒体结构体
					 */
					currVideo = new VideoInfo();
					currVideo.setArtist("<Unkown>");
					currVideo.setDisplayName(file.getName());
					currVideo.setPath(file.getPath());
					currVideo.setTitle(file.getName());
					play(scanfile_Textview.getText().toString());
					isplay = true;
					video_viewflipper.setDisplayedChild(0);
					change_titlebar_bnt_bg();
					/*
					 * 添加到当前播放列表
					 */
					video_list.add(currVideo);
					curr_video_adapter.setList(video_list);
					currplaylist_listview.postInvalidate();
					CURR_STATE = STATE_PLAY;
					video_toolbar_play.setEnabled(true);
					video_toolbar_play.setBackgroundResource(video_toolbar_play_state[CURR_STATE]);
			}
		});//End
		
		/**
		 * 文件浏览，选择路径点击确定事件
		 * @author Streamer
		 */
		scanfile_ok_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog dialog = new AlertDialog.Builder(VideoPlayer.this).setTitle("下载路径如下：")
						.setMessage(scanfile_Textview.getText().toString()).
						setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								downloadpath = scanfile_Textview.getText().toString();
								dialog.dismiss();
								/*
								 * 回到主界面 
								 */
								video_viewflipper.setDisplayedChild(0);
							}
						}).create();
				dialog.show();
				scanfile_ok_bnt.setEnabled(false);
				change_titlebar_bnt_bg();
			}
		});//End
		
		/**
		 * 文件浏览的取消按钮点击事件
		 * @author Streamer
		 */
		scanfile_cancel_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				video_viewflipper.setDisplayedChild(0);
				change_titlebar_bnt_bg();
			}
		});//End
		
		/**
		 * 定时功能
		 * @author Streamer
		 */
		menu_timing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：定时设置
				 */
				mymemu.dismiss();
				/*
				 * 定义时间选择窗口
				 */
				final TimePicker timePicker = new TimePicker(VideoPlayer.this);
				timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR));
				timePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
				timePicker.setIs24HourView(true);
				Dialog dialog = new AlertDialog.Builder(VideoPlayer.this).
						setTitle("定时退出").setView(timePicker)
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).setPositiveButton("确定",  new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								hour1 = timePicker.getCurrentHour();
								minute1 = timePicker.getCurrentMinute();
								myHandler.sendEmptyMessage(TIMING);
								showInfo("应用将在"+hour1+":"+minute1+"关闭！");
								dialog.dismiss();
							}
						}).create();
				dialog.show();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
			}
		});//End
		
		/**
		 * 显示统计流量信息
		 * @author Streamer
		 */
		menu_flux_statistics.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：统计流量
				 */
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				//显示系统消息
				showSysInfo("没有流量统计信息！");
			}
		});//End
		
		/**
		 * 显示使用说明窗口
		 * @author Streamer
		 */
		menu_useexplain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				//显示系统消息
				//showSysInfo("该功能暂不可用！");
				
				dl_dialog  = new AlertDialog.Builder(VideoPlayer.this)
				.setView(downloadview)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				})
				.create();
				dl_dialog.show();
			}
		});
		
		/**
		 * 弹出意见反馈窗口
		 * @author Streamer
		 */
		menu_opition_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：弹出回馈窗口，Message发送窗口
				 */
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				final EditText editopinion = new EditText(VideoPlayer.this);
				editopinion.setHeight(200);
				editopinion.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
				editopinion.setGravity(Gravity.LEFT);
				Dialog dialog = new AlertDialog.Builder(VideoPlayer.this).
						setTitle("意见反馈").setView(editopinion)
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).setPositiveButton("发送",  new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								SmsManager.getDefault().sendTextMessage
								("18061608810", null, "反馈意见："+editopinion.getText().toString(), null, null);
								showInfo("意见反馈成功！您的鼓励，我们的动力。");
								dialog.dismiss();
							}
						}).create();
				dialog.show();
			}
		});//End return opinion
		
		/**
		 * 点击显示开发团队信息事件
		 * @author Streamer
		 */
		menu_dev_team.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：显示开发团队信息
				 */
				mymemu.dismiss();
				
				final TimePicker timePicker = new TimePicker(VideoPlayer.this);
				timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR));
				timePicker .setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
				
				Dialog dialog = new AlertDialog.Builder(VideoPlayer.this).
						setTitle("Streamer团队信息").setItems(new String[]
								{
								  "李 蝉 秀    （队长）"
								 ,"陈华红子   （搜集）"
								 ,"王 晓 亮    （支持）"
								 ,"李 亚 学    （保障）"
								 ,"吴 香 礼    （实施）"
								 ,"Dev by Streamer."}, null)
						.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).create();
				dialog.show();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
			}
		});
		/**
		 * 音乐播放器准备就绪事件
		 * @author Streamer
		 */
		player.setOnPreparedListener(new OnPreparedListener() {

			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：显示歌曲播放信息，如歌曲名，时常
				 */
				videotext.setText("当前播放 :"+currVideo.getDisplayName()
						.substring(0, currVideo.getDisplayName().indexOf('.')));
				if(!currVideo.getPath().startsWith("rtsp://")&&!currVideo.getPath().startsWith("http://"))
				{
					Log.i("player", "准备就绪");
					int i = player.getDuration();
					Log.d("onCompletion", "" + i);
					seekbar.setMax(i);
					i /= 1000;
					int minute = i / 60;
					int hour = minute / 60;
					int second = i % 60;
					minute %= 60;
					videolentext.setText(String.format("%02d:%02d:%02d", hour,
							minute, second));
					myHandler.sendEmptyMessage(PROGRESS_CHANGED);
				}
				else
				{
					myHandler.sendEmptyMessage(4);
				}
				if(isplay)
				{
						player.start();
				}
						
			}
		});
		
		/**
		 * 音乐播放完毕事件
		 * @author Streamer
		 */
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stu
				/*
				 * 需要作出的响应：播放下一媒体
				 */
				mp.reset();
				player.invalidate();
				//发送播放下一媒体消息
				myHandler.sendEmptyMessageDelayed(PLAYNEXT, 100);
				//Log.i("player","接下来播放"+currVideo.getDisplayName());
			}
		});
		
		/**
		 * 播放器发生错误时间
		 * @author Streamer
		 */
		player.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				/*
				 * 重设播放器，并自动播放下一媒体
				 */
				mp.reset();
				myHandler.sendEmptyMessageDelayed(PLAYNEXT, 100);
				/*Returns
				 *True if the method handled the error, 
				 *false if it didn't. Returning false, or not having an 
				 *OnErrorListener at all, will cause the OnCompletionListener to be called. 
				 */
				return false;				//返回false不做处理
			}
		});
			
		/**
		 * 进度条改变事件
		 * @author Streamer
		 */
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekbar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				/*
				 * 用户改变进度时
				 * 需要作出的响应：定位播放
				 */
				if (fromUser) {
					if(isplay)
					{
						/*
						 * 当用户滑动进度条时，只有当前处在播放状态才会响应
						 */
						player.seekTo(progress);
					}
				}
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				//用户开始触碰
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				//用户结束触碰
			}
		});	
		
	}//End Create()
	
	/**
	 * 播放指定路径的媒体，并改变响应的状态
	 * @author Streamer
	 * @param MediaPath	媒体路径
	 * @return 播放成功返回true，否则返回false
	 * @see {@link VideoPlayer#play(String)}
	 */
	private boolean	play(String MediaPath)
    {
    	
		File file = new File(MediaPath);
    	if(file.exists())
    	{
    		/*
    		 * 文件存在时
    		 */
    		player.setVideoURI(Uri.parse(MediaPath));
    		//player.setVideoPath(MediaPath);
    		isplay = true;
    		ispause = false;
    		video_toolbar_play.setEnabled(true);
    		seekbar.setEnabled(true);	
    		Log.i("MediaPath", MediaPath);//进度条可用
    		if(mp.getAllVideoList().get(4).getList().lastIndexOf(currVideo)==-1)
    		{
    			mp.getAllVideoList().get(4).getList().add(currVideo);
    			initialPlayList();
    		}
    		return true;
    	}
    	else if(MediaPath.startsWith("http://")||MediaPath.startsWith("rtsp://"))
    	{
    		player.setVideoURI(Uri.parse(MediaPath));
    		isplay = true;
    		ispause = false;
    		video_toolbar_play.setEnabled(true);
    		seekbar.setEnabled(true);	
    		Log.i("MediaPath", MediaPath);//进度条可用
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    		
    }//End play()

	/**
	 * 定义一个函数，判断播放器如果处于暂停状态则重新播放，
	 * 否则如果处于播放状态，则暂停。
	 * @author Streamer
	 * @see {@link VideoPlayer#pause()}
	 */
	private void pause()
	{
		if(ispause)
		{
			player.start();
			seekbar.setProgress(player.getCurrentPosition());
			video_toolbar_forward.setEnabled(true);
			video_toolbar_goback.setEnabled(true);
			ispause = false;
			isplay = true;
		}
		else if(isplay)
		{
			player.pause();
			video_toolbar_forward.setEnabled(false);
			video_toolbar_goback.setEnabled(false);
			ispause = true;
			isplay = false;
		}
	}
	
	/**
	 * 根据当前的播放曲目以及当前的播放列表获取下一曲目。
	 * 需要注意，该方法用于自动获取下一视频
	 * @author Streamer
	 * @param currVideo  当前视频
	 * @return 下一视频
	 * @see {@link VideoPlayer#getNextVideo(VideoInfo)}
	 */
	private VideoInfo getNextVideo(VideoInfo currVideo)
	{
		VideoInfo nextVideo = new VideoInfo();
		int index = video_list.indexOf(currVideo)+1;
		if(index<video_list.size()&&index>=0)
			nextVideo = video_list.get(index);
		return nextVideo;
	}
	
	/**
	 * 根据当前的播放视频以及当前的播放列表获取前一视频。
	 * @author Streamer
	 * @param currVideo  当前视频
	 * @return 下一视频
	 * @see {@link VideoPlayer#getPreVideo(VideoInfo)}
	 */
	private VideoInfo getPreVideo(VideoInfo currVideoInfo)
	{
		VideoInfo preVideo = new VideoInfo();
		int index = video_list.indexOf(currVideo)-1;
		if(index>=0&&index<video_list.size())
			preVideo = video_list.get(index);
		return preVideo;
	}
	
	/**
	 * 根据当前状态改变标题栏的各个图标的背景
	 * @author Streamer
	 * @see {@link VideoPlayer#change_titlebar_bnt_bg()}
	 */
	private boolean change_titlebar_bnt_bg()
	{
		if(video_viewflipper.getCurrentView().getId()==R.id.video_page1)
        {
        	video_titlebar_video.setBackgroundResource(R.drawable.ic_video_checked);
        }
        else
        {
        	video_titlebar_video.setBackgroundResource(R.drawable.ic_video);
        }
        if(video_viewflipper.getCurrentView().getId()==R.id.video_Listview)
        {
        	video_titlebar_list.setBackgroundResource(R.drawable.ic_list_checked);
        }
        else
        {
        	video_titlebar_list.setBackgroundResource(R.drawable.ic_list);
        }
        if(video_viewflipper.getCurrentView().getId()==R.id.video_onlineweb)
        {
        	video_titlebar_online.setBackgroundResource(R.drawable.ic_online_checked);
        }
        else
        {
        	video_titlebar_online.setBackgroundResource(R.drawable.ic_online);
        }
        
        return true;
	}
	/**
	 * 根据当前状态，改变菜单窗口中的背景
	 * @author Streamer
	 * @return true
	 * @see {@link VideoPlayer#change_menu_bg()}
	 */
	private boolean change_menu_bg()
	{
		switch (menu_viewflipper.getCurrentView().getId()) {
		case R.id.video_menu_page1:
		{
			menu_common_bnt.setBackgroundResource(R.color.menu_bgcolor);
			menu_setting_bnt.setBackgroundResource(R.color.music_titlebar_bgcolor);
			menu_help_bnt.setBackgroundResource(R.color.music_titlebar_bgcolor);
			break;
		}
		case R.id.video_menu_page2:
		{
			menu_common_bnt.setBackgroundResource(R.color.music_titlebar_bgcolor);
			menu_setting_bnt.setBackgroundResource(R.color.menu_bgcolor);
			menu_help_bnt.setBackgroundResource(R.color.music_titlebar_bgcolor);
			break;
		}
		case R.id.video_menu_page3:
		{
			menu_common_bnt.setBackgroundResource(R.color.music_titlebar_bgcolor);
			menu_setting_bnt.setBackgroundResource(R.color.music_titlebar_bgcolor);
			menu_help_bnt.setBackgroundResource(R.color.menu_bgcolor);
			break;
		}
		default:
			break;
		}
		mymemu.update();
		return true;
	}

	/**
	 * 显示退出提示窗口
	 * @author Streamer
	 */
	private void showExitInfo()
	{
		Dialog exitdialog = new AlertDialog.Builder(VideoPlayer.this)
		.setIcon(R.drawable.ic_launcher)
		.setTitle("系统消息")
		.setMessage("你将要退出播放器？")
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				/*
				 * 退出程序
				 */
				finish();
			}
		})
		.create();
		exitdialog.show();
	}
	
	/**
	 * 显示短消息
	 * @author Streamer
	 * @param info 显示的消息
	 */
	private void showInfo(String info)
	{
		Toast.makeText(getApplicationContext(), info
				, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 显示系统消息函数
	 * @author Streamer
	 * @param info 信息
	 * @see {@link VideoPlayer#showSysInfo(String)}
	 */
	private void showSysInfo(String info)
	{
		new AlertDialog.Builder(VideoPlayer.this).
				setTitle("系统信息").setMessage(info)
				.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create().show();
	}
	
	/**
	 * 显示网页，及做网页的相关处理
	 * @author Streamer
	 * @param 
	 * @see {@link showWebView()}
	 */
	private void showWebView()
	{
		//video_webview.setWebViewClient(new DownLoadWebViewClient(VideoPlayer.this));
		video_webview.setWebViewClient(new WebViewClient()
		{

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				if (url.substring(url.length()-4).equals(".mp3")||url.substring(url.length()-4).equals(".mp4"))//&&url.substring(7,10).equals("221")
	            {
	                 play(url);
	                CharTools chartools = new CharTools();
	 				Log.i("info", "open an url");
	 	            String urlStr = "";                                                                        //存放解码后的url
	 	            //如果是utf8编码
	 	            if (chartools.isUtf8Url(url))
	 	            {
	 	                    urlStr = chartools.Utf8URLdecode(url);
	 	            }
	 	            else 
	 	            {		//如果不是utf8编码
	 	                    urlStr = URLDecoder.decode(url);
	 	            }
	                 String ss[] = urlStr.split("/");
	                 String fileName = ss[ss.length-1];                                //得到音乐文件的全名（包括后缀）
	                 Log.i("info", "musicfile: " + fileName);
	                 //将下载链接和文件名传递给下载模块
	                 Intent intent = new Intent(getApplicationContext(),DownloadService.class);
	                 intent.putExtra("url", urlStr);
	                 intent.putExtra("fileName", fileName);
	                 Log.i(urlStr, fileName);
	                 myHandler.sendEmptyMessage(DOWNLOAD);
	                VideoPlayer.this.startService(intent);
	                 video_viewflipper.setDisplayedChild(0);
	            }
				return super.shouldOverrideUrlLoading(view, url);
			}
			 
		});
		WebSettings s = video_webview.getSettings();
		s.setSaveFormData(false);
		s.setSavePassword(false);
		s.setUseWideViewPort(true);
		s.setJavaScriptEnabled(true);
		s.setLightTouchEnabled(true);
		video_webview.setWebChromeClient(new WebChromeClient()
		{

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				//Activity和Webview根据加载程度决定进度条的进度大小
		        //当加载到100%的时候 进度条自动消失
		        VideoPlayer.this.setProgress(newProgress * 100);
				super.onProgressChanged(view, newProgress);
			}
			
		});
		video_webview.loadUrl("http://192.168.42.62/Streamer/");//http://m.kugou.com
	}
	
	/**
	 * 显示文件列表
	 * @author Streamer
	 * @param path 显示的路径
	 * @see {@link MediaPlayer#getFile()}
	 */
	public List<myFile> getFile(String path)
	{
		//设置路径
		scanfile_Textview.setText(path);
		File file = new File(path);
		List<myFile> list = new ArrayList<myFile>(); 
		if(file.exists())
		{
			File [] files =file.listFiles();
			String filename;
			String filepath;
			int id;
			if(files.length != 0)
			{
				for(int i=0 ;i<files.length;i++)
				{
					filepath = files[i].getPath();
					filename = files[i].getName();
					if(new File(filepath).isDirectory())
					{
						//是文件夹
						id = R.drawable.ic_fold;
					}
					else if(filepath.endsWith("MP3")||filepath.endsWith("mp3"))
					{
						//音乐文件
						id = R.drawable.ic_mp3;
					}
					else if(filepath.endsWith("3gp")||filepath.endsWith("3GP")
							||filepath.endsWith("MP4")||filepath.endsWith("mp4"))
					{
						id = R.drawable.ic_mp4;
					}
					else
					{
						//文件
						id  = R.drawable.ic_copyfile;
					}
					list.add(new myFile(filename, id, filepath));	
				}
			}
			fileAdapter = new MyFileAdapter(VideoPlayer.this, list);
			scanfile_listview.setAdapter(fileAdapter);
		}
		return list;
	}
	/**
	 * 初始化音乐播放器的各个列表，适配器及对应的listview
	 * @author Streamer
	 */
	private void initialPlayList()
	{
		/*
		 * 初始化音乐播放器的各个音乐列表
		 */
		main_video_list  = mp.getAllVideoList();
		/*
		 * 获取当前的播放列表，以后加载数据从数据库读取，保证系统的记忆性
		 * 读取的是默认列表，如果默认列表为空，则加载本地音乐列表
		 */
		video_list = main_video_list.get(3).getList();	
		if(video_list.isEmpty())
		{
			/*
			 * 如果默认列表为空，重新建立默认列表
			 */
			video_list = main_video_list.get(0).getList();
			main_video_list.get(3).getList().addAll(video_list);
		}
		/*
		 * 初始化适配器
		 */
		curr_video_adapter = new VideoInfoListAdapter(this,video_list);
		currplaylist_listview.setAdapter(curr_video_adapter);
		main_video_adapter = new VideoListExpandableListAdapter(this,main_video_list);
		//给音乐的所有列表显示添加适配器
		video_listview.setAdapter(main_video_adapter);		
	}
	
	/**
	 * 重写菜单显示，显示自己的菜单
	 * @author Streamer
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(menu_isShow)
		{
			mymemu.dismiss();
			video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
			menu_isShow = false;
		}
		else
		{
			video_toolbar_menu.setBackgroundResource(R.drawable.ic_menu_checked);
			mymemu.showAtLocation(video_toolbar_menu, Gravity.BOTTOM, 0, 80);
			menu_viewflipper.setDisplayedChild(0);
			change_menu_bg();
			menu_isShow =true;
		}
		return false;  //true turn sys menu else not return
	}

	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		this.detector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(isplay&&!currVideo.getPath().isEmpty())
		{
			play(currVideo.getPath());
			player.seekTo(mp.getVideoCurrTime());
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(player.isPlaying())
		{
			mp.setVideoCurrTime(player.getCurrentPosition());
			player.pause();
		}
		super.onPause();
	}

	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if(player.isPlaying())
			player.pause();
		video_webview.stopLoading();
		showInfo("正在保存数据...");
		MyDataBase mydatabase;
		mydatabase = new MyDataBase();
		mp.setLastVideoIndex(video_list.lastIndexOf(currVideo));
		main_video_list.get(3).getList().clear();
		/*
		 * 将当前播放列表放入默认列表
		 */
		main_video_list.get(3).getList().addAll(video_list);
		mp.resetAllVideoList(main_video_list);
		mydatabase.savaVideoData(mp.getAllVideoList());
		mp.SavaData();
		showInfo("视频播放器退出成功，欢迎再次使用！");
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	/**
	 * 监听按键事件
	 * @author Streamer
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
			{
				/*
				 * 点击后退事件
				 */
				if(video_viewflipper.getCurrentView().getId()==R.id.video_online)
				{
					//显示的是网页时
					if(video_webview.canGoBack())
					{
						//显示上一页面
						video_webview.goBack();
					}
					else
					{
						video_viewflipper.showPrevious();
						change_titlebar_bnt_bg();
					}
					return true;
				}
				if(video_viewflipper.getCurrentView().getId()!=R.id.video_page1)
				{
					video_viewflipper.showPrevious();
					change_titlebar_bnt_bg();
					return true;
				}
				else
				{
					showExitInfo();
				}	
			}	
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
