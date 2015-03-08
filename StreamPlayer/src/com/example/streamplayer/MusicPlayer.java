package com.example.streamplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
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

import com.example.dataclass.LyricView;
import com.example.dataclass.MediaList;
import com.example.dataclass.SongInfo;
import com.example.dataclass.SongLyric;
import com.example.dataclass.myFile;
import com.example.myadapter.MusicListExpandableListAdapter;
import com.example.myadapter.MyFileAdapter;
import com.example.myadapter.SongInfoListAdapter;
import com.example.myapplication.MyApplication;
import com.example.mydatabase.MyDataBase;


/**
 *该Activity用于实现音乐播放部分的相关功能。有最基本的音乐播放器功能，
 *如播放模式、上一曲、下一曲、播放/暂停，划屏切换界面，网上音乐浏览，
 *播放列表的新建、选择、添加、删除，歌曲的增加和删除，
 *歌词的同步显示，音量的调节，主题的变换，曲库的刷新，蓝牙发送
 *歌曲，歌曲下载，定时，流量统计功能。
 *@author Streamer
 *@see {@link MusicPlayer}
 */

@SuppressWarnings("deprecation")
@SuppressLint({ "NewApi", "HandlerLeak", "UnlocalizedSms", "SetJavaScriptEnabled" })
public class MusicPlayer extends Activity implements OnGestureListener {
	
	/*
	 * 音乐播放器的标题栏上的五个按钮声明
	 */
	/**
	 * 音乐播放器标题栏的音乐主界面选择按钮
	 * @see {@link MusicPlayer#music_titlebar_music}
	 */
	ImageButton music_titlebar_music;
	/**
	 * 音乐播放器标题栏的播放器列表界面选择按钮
	 * @see {@link MusicPlayer#music_titlebar_list}
	 */
	ImageButton music_titlebar_list;
	/**
	 * 音乐播放器标题栏的网页浏览界面选择按钮
	 * @see {@link MusicPlayer#music_titlebar_online}
	 */
	ImageButton music_titlebar_online;
	/**
	 * 音乐播放器标题栏的声音调节窗口选择按钮
	 * @see {@link MusicPlayer#music_titlebar_voice}
	 */
	ImageButton music_titlebar_voice;
	/**
	 * 音乐播放器与视频播放器切换按钮
	 * @see {@link MusicPlayer#music_titlebar_video}
	 */
	ImageButton music_titlebar_video;
	
	/*
	 * 音乐播放器的工具栏上的五个按钮声明
	 */
	/**
	 * 音乐播放器工具栏的播放模式选择按钮
	 * @see {@link MusicPlayer#music_toolbar_mode}
	 */
	ImageButton music_toolbar_mode;
	/**
	 * 音乐播放器工具栏的上一曲选择按钮
	 * @see {@link MusicPlayer#music_toolbar_pre}
	 */
	ImageButton music_toolbar_pre;
	/**
	 * 音乐播放器工具栏的播放/暂停按钮
	 * @see {@link MusicPlayer#music_toolbar_play}
	 */
	ImageButton music_toolbar_play;
	/**
	 * 音乐播放器工具栏的下一曲选择按钮
	 * @see {@link MusicPlayer #music_toolbar_next}
	 */
	ImageButton music_toolbar_next;
	/**
	 * 音乐播放器工具栏的菜单选择按钮
	 * @see {@link MusicPlayer#music_toolbar_menu}
	 */
	ImageButton music_toolbar_menu;
	
	/*
	 * 音乐播放器主界面布局定义，中间部分
	 */
	/**
	 * 划屏切换组布局，用于划屏切换
	 * @see {@link MusicPlayer#music_viewflipper}
	 */
	ViewFlipper music_viewflipper;
	/**
	 * 划屏切换页面一的歌词显示页面,需更正！
	 * @see {@link MusicPlayer#curr_music_listview}
	 */
	//用linearLayout存放歌词控件
	LinearLayout layout_lrc;
	//歌词对象
	private SongLyric lrc;
	//歌词控件
	private LyricView vwLrc;
	/*
	 * 注意，真正的使用的时候将MediaPlayer 的getCurrentPosition传给歌词控件的setTime中，
	 * 刷新的时候只用该方法即可
	 */
	
	/**
	 * 划屏切换页面一的歌曲播放进度条
	 * @see {@link MusicPlayer#seekbar1}
	 */
	SeekBar seekbar1;											
	/**
	 * 划屏切换页面的播放曲目显示
	 * @see {@link MusicPlayer#songtext}
	 */
	TextView songtext;									
	/**
	 * 划屏切换页面一的当前播放位置显示
	 * @see {@link MusicPlayer#songcurrtext}
	 */
	TextView songcurrtext;									
	/**
	 * 划屏切换页面一的歌曲长度显示
	 * @see {@link MusicPlayer#songlentext}
	 */
	TextView songlentext;									
	/**
	 * 划屏切换页面一的歌曲播放，使用videoview
	 * @see {@link MusicPlayer#player}
	 */
	VideoView player;
	
	/**
	 * 划屏切换页面二的播放器列表显示页面，使用可扩展布局
	 * @see {@link MusicPlayer#main_music_list}
	 */
	ExpandableListView main_music_listview;
	/**
	 * 划屏切换页面三的网页浏览页面
	 * @see {@link MusicPlayer#onlinepage_music_webview}
	 */
	WebView  onlinepage_music_webview;
	/**
	 * 划屏切换页面四,线性布局存放其他view
	 */
	LinearLayout page4;
	/**
	 * 划屏切换页面四正在播放列表
	 */
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
	/*
	 * 音乐播放器的菜单布局定义
	 */
	/**
	 * 音乐播放器的菜单窗口
	 * @see {@link MusicPlayer#mymemu}
	 */
	private PopupWindow mymemu;
	
	/**
	 * 音乐播放器的菜单窗口主页面布局
	 * @see {@link MusicPlayer#menuView}
	 */
	View  menuView;
	/**
	 * 音乐播放器的菜单窗口的划屏切换布局
	 * @see {@link MusicPlayer#menu_viewflipper}
	 */
	ViewFlipper menu_viewflipper;
	/**
	 * 音乐播放器的菜单窗口的标题栏的常用菜单选择按钮
	 * @see {@link MusicPlayer#menu_common_bnt}
	 */
	Button menu_common_bnt;
	/**
	 * 音乐播放器的菜单窗口的标题栏的设置菜单选择按钮
	 * @see {@link MusicPlayer#menu_setting_bnt}
	 */
	Button menu_setting_bnt;
	/**
	 * 音乐播放器的菜单窗口的标题栏的帮组菜单选择按钮
	 * @see {@link MusicPlayer#menu_help_bnt}
	 */
	Button menu_help_bnt;
	/**
	 * 音乐播放器的的菜单窗口常用菜单下的显示歌词选项
	 * @see {@link MusicPlayer#menu_showLRC_bnt}
	 */
	ImageButton menu_LRCCode_bnt;
	/**
	 * 音乐播放器的菜单窗口常用菜单下的改变主题选项
	 * @see {@link MusicPlayer#menu_change_theme}
	 */
	ImageButton menu_change_theme;
	/**
	 * 音乐播放器的菜单窗口常用菜单下的刷新列表选项
	 * @see {@link MusicPlayer#menu_refresh_list}
	 */
	ImageButton menu_refresh_list;
	/**
	 * 音乐播放器的菜单窗口常用菜单下的退出播放器选项
	 * @see {@link MusicPlayer#menu_exit}
	 */
	ImageButton menu_exit;
	/**
	 * 音乐播放器的菜单窗口设置菜单下的网络接入选项
	 * @see {@link MusicPlayer#menu_online_check}
	 */
	ImageButton menu_online_check;
	/**
	 * 音乐播放器的菜单窗口设置菜单下的蓝牙发送功能选项
	 * @see {@link MusicPlayer#menu_bluetooth_share}
	 */
	ImageButton menu_bluetooth_share;
	/**
	 * 音乐播放器的菜单窗口设置菜单下的下载路径选择选项
	 * @see {@link MusicPlayer#menu_downloadpath}
	 */
	ImageButton menu_downloadpath;
	/**
	 * 音乐播放器的菜单窗口设置菜单下的定时设置选项
	 * @see {@link MusicPlayer#menu_timing}
	 */
	ImageButton menu_timing;
	/**
	 * 音乐播放器菜单窗口帮助菜单下的流量统计选项
	 * @see {@link MusicPlayer#menu_flux_statistics}
	 */
	ImageButton menu_flux_statistics;
	/**
	 * 音乐播放器菜单窗口帮助菜单下的下载列表选项
	 * @see {@link MusicPlayer#menu_download_list}
	 */
	ImageButton menu_download_list;
	/**
	 * 音乐播放器菜单窗口帮助菜单下的意见反馈选项
	 * @see {@link MusicPlayer#menu_opition_return}
	 */
	ImageButton menu_opition_return;
	/**
	 * 音乐播放器菜单窗口帮助菜单下的开发团队选项
	 */
	ImageButton menu_dev_team;
	
	/**
	 * 音乐播放器歌词编码选择窗口
	 */
	private PopupWindow LrcCodeform;
	View LrcCodeView;
	private Button GBK_bnt;
	private Button UTF8_bnt;
	private Button UTF16_bnt;
	private Button Unicode_bnt;
	private Button LrcCode_cancelbnt;
	
	/**
	 * 音乐播放器列表组长按弹出窗口
	 */
	private PopupWindow group_longpressForm;
	View group_longpressView;
	private Button group_longpress_addtolist;
	private Button group_longpress_del;
	private Button group_longpress_altername;
	private Button group_longpress_cancel;
	private Button group_longpress_clear;
	/**
	 * 音乐播放器列表子长按弹出窗口
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
	 * 音乐播放器主题选择弹出菜单
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
	
	/*
	 * 变量设计
	 *///////////////////////////////////////////////////////////
	/*
	 * 列表型变量
	 *///////////////////////////////////////////////////////////
	
	/**
	 * 用于存放所有的音乐列表，包括本地音乐、我的下载、我的最爱等列表
	 * @see {@link MusicPlayer#main_music_list}
	 */
	List<MediaList<SongInfo>> main_music_list;

	/**
	 * 用于存放当前正在播放的音乐列表
	 * @see {@link MusicPlayer#currPlayList}
	 */
	List<SongInfo> currPlayList;
	
	/*
	 * 适配器型变量
	 */////////////////////////////////////////////////////////////
	/**
	 * 定义一个适配器，用于显示正在播放的音乐列表，适配器类型为
	 * {@link SongInfoListAdapter}
	 * @see {@link MusicPlayer#curr_music_adapter}
	 */
	private SongInfoListAdapter curr_music_adapter;
	/**
	 * 定义一个适配器，用于显示所有的音乐列表，该列表为可扩展列表，
	 * 适配器类型为{@link MusicListExpandableListAdapter}
	 */
	private MusicListExpandableListAdapter main_music_adapter;
	
	/**
	 * 用于显示文件
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
	 * @see {@link MusicPlayer#menu_isShow}
	 */
	boolean menu_isShow = false;
	/**
	 * 用于记录音乐播放器工具栏的的上一曲按钮的可用状态，可用时为true，
	 * 不可用时为false
	 * @see {@link MusicPlayer#pre_isEnable}
	 */
	boolean pre_isEnable = false;
	/**
	 * 用于记录音乐播放器工具栏的播放/暂停按钮的可用状态，可用时为true
	 * 不可用时为false
	 * @see {@link MusicPlayer#play_isEnable}
	 */
	boolean play_isEnable = false;
	/**
	 * 用于记录音乐播放器工具栏的下一首按钮的可用状态，可用时为true
	 * 不可用时为false
	 * @see {@link MusicPlayer#next_isEnable}
	 */
	boolean next_isEnable = false;
	/**
	 * 用于记录音乐播放器是否处于播放状态，是为true，否则为false
	 * @see {@link MusicPlayer#isplay}
	 */
	private  boolean isplay = false;
	/**
	 * 用于记录音乐播放器是否处于暂停状态，即由播放状态进入暂停，
	 * 是时为true，不是为false
	 */
	private boolean ispause = false;
	
	/*
	 * 整型数组变量
	 */////////////////////////////////////////////////////////////////
	/**
	 * 用于存放音乐播放器工具栏的播放/暂停按钮在不同状态下的背景图标的id
	 * @see {@link MusicPlayer#music_toolbar_mode_state}
	 */
	int []music_toolbar_play_state = new int[]
				{R.drawable.ic_bnt_play,R.drawable.ic_bnt_pause};
	/**
	 * 用于存放音乐播放器工具栏的模式选择按钮在不同状态下的图标的id
	 * @see {@link MusicPlayer#music_toolbar_mode_state}
	 */
	int []music_toolbar_mode_state = new int[]
				{R.drawable.ic_mode_order,R.drawable.ic_mode_random,
			R.drawable.ic_mode_loopall,R.drawable.ic_mode_loop};
	
	/*
	 * 整型常量/变量
	 *//////////////////////////////////////////////////////////////////
	/**
	 * 常量、记录播放状态下的图标数组
	 * {@link MusicPlayer#music_toolbar_play_state}中对应的下标
	 * @see {@link MusicPlayer#STATE_PLAY}
	 */
	private final static int STATE_PLAY = 1;
	/**
	 * 常量、记录暂停状态下的图标数组
	 * {@link MusicPlayer#music_toolbar_play_state}中对应的下标
	 * @see {@link MusicPlayer#STATE_PAUSE}
	 */
	private final static int STATE_PAUSE = 0;
	/**
	 * 常量，记录顺序播放模式下的图标数组
	 * {@link MusicPlayer#music_toolbar_mode_state}对应的下标
	 * @see {@link MusicPlayer#MODE_ORDER}
	 */
	private final static int MODE_ORDER = 0;
	/**
	 * 常量，记录随机播放模式下的图标数组
	 * {@link MusicPlayer#music_toolbar_mode_state}对应的下标
	 * @see {@link MusicPlayer#MODE_RANDOM}
	 */
	private final static int MODE_RANDOM = 1;
	/**
	 * 常量，记录列表循环播放模式下的图标数组
	 * {@link MusicPlayer#music_toolbar_mode_state}对应的下标
	 * @see {@link MusicPlayer#MODE_LOOPALL}
	 */
	private final static int MODE_LOOPALL = 2;
	/**
	 * 常量，记录单曲循环播放模式下的图标数组
	 * {@link MusicPlayer#music_toolbar_mode_state}对应的下标
	 * @see {@link MusicPlayer#MODE_LOOP}
	 */
	private final static int MODE_LOOP = 3;
	/**
	 * 消息对列表之播放进程改变消息编号
	 * @see {@link MusicPlayer#PROGRESS_CHANGED}
	 */
	private final static int PROGRESS_CHANGED = 0;
	/**
	 * 消息队列之播放下一首消息处理
	 */
	private final static int PLAYNEXT = 1;
	/**
	 * 判断时间消息
	 */
	private final static int TIMING = 2;
	/**
	 * 记录当前播放的状态，1为播放状态，0为暂停状态
	 * @see {@link MusicPlayer#CURR_STATE}
	 */
	private int CURR_STATE = STATE_PAUSE;
	/**
	 * 记录当前播放的模式。
	 * @see {@link MusicPlayer#MODE_CURR_STATE}
	 */
	private  int MODE_CURR_STATE;
	/**
	 * 记录音乐播放的位置
	 */
	private int currtime=0;
	/**
	 * 记录设置的时间
	 */
	private int hour1 = 25;
	private int minute1;
	/*
	 * 记录当前打开的组号
	 */
	private int groupid;
	/*
	 * 记录下载路径
	 */
	@SuppressWarnings("unused")
	private String downloadpath;
	/*
	 * 歌曲信息结构定义
	 */
	/**
	 * 记录当前正在播放的歌曲的详细信息
	 * @see {@link MusicPlayer#currSong}
	 */
	private SongInfo currSong;
	
	/*
	 * 其他类定义
	 */
	/**
	 * 定义一个手势识别类，用于监听用户的划屏事件
	 * @see {@link MusicPlayer#detector}
	 */
	private GestureDetector detector;
	
	/*
	 * 我的应用声明
	 */
	/**
	 * 声明我的应用{@link MyApplication}，其中存放程序运行所需要的各种信息、数据库操作、
	 * 当退出应用时保存记录和数据，
	 * @see {@link MusicPlayer#mp}
	 */
	MyApplication mp;
	
	/*
	 * 我的详细队列处理
	 */
	/**
	 * 消息队列处理方法，用于音乐播放器运行时产生的的消息
	 * @see {@link MusicPlayer#myHandler}
	 */
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
						songcurrtext.setText(String.format("%02d:%02d:%02d", hour,
							minute, second));
						//更新歌词
						vwLrc.setTime(player.getCurrentPosition());
						vwLrc.postInvalidate();
					}
					else
					{
						seekbar1.setProgress(0);
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
					currSong = getNextSong(currSong);
					play(currSong.getmFilePath());
					break;
				}
				case TIMING:
				{
					/*
					 * 定时判断，是否到时间，实则关闭应用
					 */
					if(hour1 == Calendar.getInstance().get(Calendar.HOUR)
							&&minute1 == Calendar.getInstance().get(Calendar.MINUTE))
					{
						MusicPlayer.this.finish();
					}
					sendEmptyMessage(TIMING);
					break;
				}
			}
			super.handleMessage(msg);
		}
	};
	
	/**
	 * 获取音量管理
	 * @see {@link MusicPlayer#audioManager}
	 */
	AudioManager audioManager;
	/*
	 * 播放器的view
	 */
	View showThemeView;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * 获取主布局view
		 */
		showThemeView =getLayoutInflater().inflate(R.layout.activity_music_player,null);
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
		 * 实例化音乐播放器标题栏的五个控件
		 */
		music_titlebar_music = (ImageButton)findViewById(R.id.music_music);
		music_titlebar_list = (ImageButton)findViewById(R.id.music_list);
		music_titlebar_online = (ImageButton)findViewById(R.id.music_online);
		music_titlebar_voice = (ImageButton)findViewById(R.id.music_voice);
		music_titlebar_video = (ImageButton)findViewById(R.id.music_video);
		/*
		 * 实例化音乐播放器工具栏的五个控件
		 */
		music_toolbar_mode = (ImageButton)findViewById(R.id.music_toolbar_mode);
		music_toolbar_pre = (ImageButton)findViewById(R.id.music_toolbar_pre);
		music_toolbar_play = (ImageButton)findViewById(R.id.music_toolbar_play);
		music_toolbar_next =(ImageButton)findViewById(R.id.music_toolbar_next);
		music_toolbar_menu = (ImageButton)findViewById(R.id.music_toolbar_menu);
		/*
		 * 实例化划屏切换布局
		 */
		music_viewflipper = (ViewFlipper)findViewById(R.id.music_viewflipper);
		/*
		 * 划屏切换页面一
		 *//////////////////////////////////////////////////////////////////////
		//实例化歌词对象
		lrc = new SongLyric("");
		layout_lrc = (LinearLayout)findViewById(R.id.Linear_songLRC);
		vwLrc = new LyricView(layout_lrc.getContext());
		layout_lrc.addView(vwLrc);
		//为歌词控件初始化，设置歌词
		player = (VideoView)findViewById(R.id.music_videoview);
		seekbar1 = (SeekBar)findViewById(R.id.music_seekbar);
		songtext = (TextView)findViewById(R.id.music_showsongname);
		songcurrtext = (TextView)findViewById(R.id.music_showsongcurr);
		songlentext = (TextView)findViewById(R.id.music_showsonglen);
		/*
		 * 划屏切换页面二
		 *//////////////////////////////////////////////////////////////////////////////////
		main_music_listview = (ExpandableListView)findViewById(R.id.musicpage_mainListview);
		/*
		 * 划屏切换页面三
		 *////////////////////////////////////////////////////////////////////////////
		onlinepage_music_webview = (WebView)findViewById(R.id.musicpage_onlineweb);
		/*
		 * 划屏切换页面四
		 */
		page4 = (LinearLayout)findViewById(R.id.music_page4);
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
		page5 = (LinearLayout)findViewById(R.id.music_page5);
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
		/*
		 * 实例化菜单窗口布局
		 */
		
		menuView = this.getLayoutInflater().inflate(R.layout.music_menu_layout, null);
		menu_viewflipper = (ViewFlipper)menuView.findViewById(R.id.music_menu_viewflipper);
		menu_common_bnt = (Button)menuView.findViewById(R.id.music_menu_common);
		menu_setting_bnt = (Button)menuView.findViewById(R.id.music_menu_setting);
		menu_help_bnt = (Button)menuView.findViewById(R.id.music_menu_help);
		menu_LRCCode_bnt = (ImageButton)menuView.findViewById(R.id.music_menu_musicLrc);
		menu_change_theme = (ImageButton)menuView.findViewById(R.id.music_menu_theme);
		menu_refresh_list = (ImageButton)menuView.findViewById(R.id.music_menu_refresh);
		menu_exit = (ImageButton)menuView.findViewById(R.id.music_menu_exit);
		menu_online_check = (ImageButton)menuView.findViewById(R.id.music_menu_online);
		menu_bluetooth_share = (ImageButton)menuView.findViewById(R.id.music_menu_bluetooth);
		menu_downloadpath = (ImageButton)menuView.findViewById(R.id.music_menu_savepath);
		menu_timing = (ImageButton)menuView.findViewById(R.id.music_menu_timing);
		menu_flux_statistics = (ImageButton)menuView.findViewById(R.id.music_menu_flux);
		menu_download_list = (ImageButton)menuView.findViewById(R.id.music_menu_downloadlist);
		menu_opition_return = (ImageButton)menuView.findViewById(R.id.music_menu_opinion);
		menu_dev_team = (ImageButton)menuView.findViewById(R.id.music_menu_devteam);
		
		/*
		 * 初始化歌词编码选择窗口
		 */
		LrcCodeView = this.getLayoutInflater().inflate(R.layout.lrccode_layout, null);
		GBK_bnt = (Button)LrcCodeView.findViewById(R.id.GBK_bnt);
		UTF8_bnt = (Button)LrcCodeView.findViewById(R.id.UTF8_bnt);
		UTF16_bnt = (Button)LrcCodeView.findViewById(R.id.UTF16_bnt);
		Unicode_bnt = (Button)LrcCodeView.findViewById(R.id.Unicode_bnt);
		LrcCode_cancelbnt = (Button)LrcCodeView.findViewById(R.id.LrcCode_cancelbnt);
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
		/*
		 * 变量的初始化
		 */
		//初始化音乐播放器的播放列表,适配器和listview
		initialPlayList();
		
		mymemu = new PopupWindow(menuView, LayoutParams.FILL_PARENT, 150);
		LrcCodeform = new PopupWindow
		(LrcCodeView, getWindowManager().getDefaultDisplay().getWidth()-40, 200);
		/*
		 * 设置一些变量的初始状态
		 */
		mymemu.setAnimationStyle(R.style.menuAnimation);	//菜单窗口动作风格
		//Log.i("Index", "msg"+mp.getLastSongIndex());
		/*
		 * 初始化播放模式以及音乐播放器
		 */
		MODE_CURR_STATE = mp.getMusicPlayerMode();
		if(mp.getLastSongIndex()<currPlayList.size()&&mp.getLastSongIndex()>=0)
		{
			currSong = currPlayList.get(mp.getLastSongIndex());
			play(currSong.getmFilePath());
			pause();
			myHandler.sendEmptyMessage(PROGRESS_CHANGED);
			music_viewflipper.setDisplayedChild(0);	//初始时显示第一页
		}
		else
		{
			music_toolbar_next.setEnabled(false);	//下一曲按钮不可用
			music_toolbar_pre.setEnabled(false);		//上一曲按钮不可用
			music_toolbar_play.setEnabled(false);	//播放键不可用，有改动
			seekbar1.setEnabled(false);
			music_viewflipper.setDisplayedChild(3);	//初始时显示第四页
		}
		/*
		 * 加载初始资源
		 */
		music_toolbar_mode.setBackgroundResource(music_toolbar_mode_state[MODE_CURR_STATE]);
		music_toolbar_play.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
		music_titlebar_music.setBackgroundResource(R.drawable.ic_music_checked);
		main_music_listview.setIndicatorBounds(
				getWindowManager().getDefaultDisplay().getWidth()-40, 
				getWindowManager().getDefaultDisplay().getWidth()-9);
		//显示网页
		showWebView();
		//显示文件浏览
		filelist = getFile("/");
		//Log.i("scanfile", "到此");
		/**
		 * 划屏切换页面用户触碰监听事件处理,将事件交由手势识别类处理
		 * @author Streamer
		 */
		music_viewflipper.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//交由手势识别类
				return detector.onTouchEvent(event);
			}
		});//End
		
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
				
				 if(music_viewflipper.getCurrentView().getId() == R.id.music_page1)
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
	                				seekbar1.setProgress(d);
	                			}
	                		else
	                			{
	                				seekbar1.setProgress(player.getDuration()-10);
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
	                			seekbar1.setProgress(d);
	                			player.seekTo(d);
	                		}
	                		else
	                			{
	                				seekbar1.setProgress(10);
	                				player.seekTo(10);
	                			}
	                		player.start();
	                	}
	                	
	                }
				 
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
                    music_viewflipper.setInAnimation(MusicPlayer.this,R.anim.push_right_in); 
                    music_viewflipper.setOutAnimation(MusicPlayer.this,R.anim.push_right_out ); 
                    music_viewflipper.showPrevious();      
                }
                if(x<-200)
                { 
                	music_viewflipper.setInAnimation(MusicPlayer.this,R.anim.push_left_in ); 
                    music_viewflipper.setOutAnimation(MusicPlayer.this,R.anim.push_left_out); 
                    music_viewflipper.showNext();
                }
              //change titlebar every button image
                change_titlebar_bnt_bg();
                if(mymemu.isShowing())
                {
                	mymemu.dismiss();
        			music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
        			menu_isShow = false;
                }
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				//用户轻触屏幕，单击
				if(mymemu.isShowing())
				{
					mymemu.dismiss();
					music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				}
				if(LrcCodeform.isShowing())
					LrcCodeform.dismiss();
				if(group_longpressForm.isShowing())
					group_longpressForm.dismiss();
				if(child_longpressForm.isShowing())
					child_longpressForm.dismiss();
				if(bgtheme_form.isShowing())
					bgtheme_form.dismiss();
				return false;
			}
		});
		
		/**
		 * 音乐播放器标题栏的音乐播放主界面选择按钮点击事件处理
		 * @author Streamer
		 */
		music_titlebar_music.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 事件处理：将显示界面切换到音乐播放的主界面，并改变所
				 * 有的音乐播放器标题栏的选项按钮的背景图片
				 */
				if(music_viewflipper.getCurrentView().getId() == R.id.music_page1)
				{
					//如果是正确的页面，不作处理
				}
				else
				{
					//如果不是处在正确的页面，切换页面
					music_viewflipper.setDisplayedChild(0);
				}
				//改变按钮背景图片
		        change_titlebar_bnt_bg();
			}
		});//End
		
		/**
		 * 音乐播放器标题栏的音乐播放器所有列表显示界面选择按钮点击事件，即
		 * 界面二选择按钮点击事件
		 * @author Streamer
		 */
		music_titlebar_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 事件处理：将显示界面切换到音乐播放的所有音乐列表显示界面，
				 * 并改变所有的音乐播放器标题栏的选项按钮的背景图片
				 */
				if(music_viewflipper.getCurrentView().getId() == R.id.musicpage_mainListview)
				{
					//如果是正确的页面，不作处理
					music_viewflipper.setDisplayedChild(3);
				}
				else
				{
					//如果不是处在正确的页面，切换页面
					music_viewflipper.setDisplayedChild(1);
				}
				//改变按钮背景图片
		        change_titlebar_bnt_bg();
			}
		});//End
		
		/**
		 * 音乐播放器标题栏的音乐播放器网络浏览显示界面选择按钮即界面三选择按钮点击事件
		 * @author Streamer
		 */
		music_titlebar_online.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 事件处理：将显示界面切换到音乐播放的网络浏览显示界面，
				 * 并改变所有的音乐播放器标题栏的选项按钮的背景图片
				 */
				if(music_viewflipper.getCurrentView().getId() == R.id.musicpage_onlineweb)
				{
					//如果是正确的页面，不作处理
				}
				else
				{
					//如果不是处在正确的页面，切换页面
					music_viewflipper.setDisplayedChild(2);
				}
				//改变按钮背景图片
		        change_titlebar_bnt_bg();
			}
		});//End
		
		/**
		 * 音乐播放器标题栏的音乐播放器声音添加选项按钮点击事件
		 * @author Streamer
		 */
		music_titlebar_voice.setOnClickListener(new OnClickListener() {
			
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
		 * 音乐播放器标题栏的音乐播放器切换到视频播放器的选择按钮点击事件
		 * @author Streamer
		 */
		music_titlebar_video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 事件处理：将音乐播放器的为完成任务保存，如下载，并将切换到视频播放器
				 */
				//Log.i(this.toString(), "进入了");
				Intent videoIntent = new Intent();
				videoIntent.setClass(MusicPlayer.this,VideoPlayer.class);
				//关闭音乐播放器
				MusicPlayer.this.finish();
				//启动视频播放器
				MusicPlayer.this.startActivity(videoIntent);
			}
		});//End

		/**
		 * 音乐播放器工具栏的播放模式选择按钮点击事件
		 * @author Streamer
		 */
		music_toolbar_mode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要做出的响应：依次改变的前的播放模式并显示对应的模式图标；
				 * 改变一些控件的可用状态
				 */
				MODE_CURR_STATE++;
				if(MODE_CURR_STATE == 4)
					MODE_CURR_STATE = MODE_ORDER;
				music_toolbar_mode.setBackgroundResource(music_toolbar_mode_state[MODE_CURR_STATE]);
				if(MODE_CURR_STATE == MODE_RANDOM)
				{
						music_toolbar_next.setEnabled(true);
						music_toolbar_pre.setEnabled(true);
				}
				if(MODE_CURR_STATE == MODE_LOOPALL)
					music_toolbar_next.setEnabled(true);
			}
		});//End
		
		/**
		 * 音乐播放器工具栏的上一首选择按钮点击事件
		 * @author Streamer
		 */
		music_toolbar_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：根据当前的模式获取下一首播放曲目，并播放
				 */
				//获取前一首曲目信息并替换当前曲目
				currSong = getPreSong(currSong);	
				play(currSong.getmFilePath());
				CURR_STATE = STATE_PLAY;
				//改变该按钮的背景
				music_toolbar_play.setBackgroundResource
				(music_toolbar_play_state[CURR_STATE]);
			}
		});//End

		/**
		 * 音乐播放器工具栏的播放/暂停选择按钮点击事件
		 * @author Streamer
		 */
		music_toolbar_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：如果处于播放状态，则暂停，否则播放，并记录播放器的播放状态
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
				seekbar1.setEnabled(true);
				music_toolbar_play.setBackgroundResource
				(music_toolbar_play_state[CURR_STATE]);
			}
		});//End

		/**
		 * 音乐播放器工具栏的下一曲选项按钮点击事件
		 * @author Streamer
		 */
		music_toolbar_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：根据当前的模式获取下一首播放曲目，并播放
				 */
				//获取写一首曲目信息并替换当前曲目
				int state= MODE_CURR_STATE;
				if(MODE_CURR_STATE == MODE_LOOP)
				{
					//当处于循环状态时，getNextSong将获取原来的歌曲，及获取不成功
					MODE_CURR_STATE = MODE_ORDER;
				}
				currSong = getNextSong(currSong);	
				MODE_CURR_STATE = state;
				play(currSong.getmFilePath());
				CURR_STATE = STATE_PLAY;
				//改变该按钮的背景
				music_toolbar_play.setBackgroundResource
				(music_toolbar_play_state[CURR_STATE]);
			}
		});//End

		/**
		 * 音乐播放器工具栏的菜单显示选择按钮点击事件
		 * @author Streamer
		 */
		music_toolbar_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：如果菜单处于显示状态则将其关闭，否则打开，并记录菜单的显示状态
				 */
				if(menu_isShow)
				{
					//菜单处于显示状态,关闭菜单窗口
					mymemu.dismiss();
					//改变按钮背景
					music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
					//记录状态
					menu_isShow = false;
				}
				else
				{
					//菜单处于关闭状态，显示菜单
					mymemu.showAtLocation(music_toolbar_menu, Gravity.BOTTOM, 0, 80);
					music_toolbar_menu.setBackgroundResource(R.drawable.ic_menu_checked);
					//打开菜单默认出入菜单页面一
					menu_viewflipper.setDisplayedChild(0);
					//改变菜单页面标题栏的背景状态
					change_menu_bg();
					//记录状态
					menu_isShow = true;
				}
			}
		});//End

		/**
		 * 菜单窗口的常用菜单选项按钮触碰事件
		 * @author Streamer
		 */
		menu_common_bnt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				/*
				 * 需要做出的响应：显示常用菜单页面，并改变其他标题栏按钮背景
				 */
				menu_viewflipper.setDisplayedChild(0);
				change_menu_bg();
				return false;
			}
		});//End
		
		/**
		 * 菜单窗口的设置菜单选项按钮触碰事件
		 * @author Streamer
		 */
		menu_setting_bnt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				/*
				 * 需要做出的响应：显示设置菜单页面，并改变其他标题栏按钮背景
				 */
				menu_viewflipper.setDisplayedChild(1);
				change_menu_bg();
				return false;
			}
		});//End

		/**
		 * 菜单窗口的帮助菜单选项按钮触碰事件
		 * @author Streamer
		 */
		menu_help_bnt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				/*
				 * 需要做出的响应：显示帮助菜单页面，并改变其他标题栏按钮背景
				 */
				menu_viewflipper.setDisplayedChild(2);
				change_menu_bg();
				return false;
			}
		});//End

		/**
		 * 菜单窗口的常用菜单下的显示歌词选项按钮点击事件
		 * @author Streamer
		 */
		menu_LRCCode_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：选择歌词编码类型
				 */
				mymemu.dismiss();
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				if(LrcCodeform.isShowing())
				{
					LrcCodeform.dismiss();
				}
				else
				{
					LrcCodeform.showAtLocation(LrcCodeView, Gravity.CENTER, 0, 0);
				}
				Log.i("menu_showLRC_bnt", "修正歌词");
			}
		});//End
		
		GBK_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lrc.setLrc(currSong.getmFilePath().substring(0, currSong.getmFilePath().indexOf('.'))+".lrc","GBK");
				lrc.setMaxTime(player.getDuration());
				vwLrc.setLyric(lrc);
				vwLrc.reset();
				vwLrc.setTime(0);
				vwLrc.postInvalidate();
				LrcCodeform.dismiss();
			}
		});//End
		
		UTF8_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lrc.setLrc(currSong.getmFilePath().substring(0, currSong.getmFilePath().indexOf('.'))+".lrc","UTF-8");
				lrc.setMaxTime(player.getDuration());
				vwLrc.setLyric(lrc);
				vwLrc.reset();
				vwLrc.setTime(0);
				vwLrc.postInvalidate();
				LrcCodeform.dismiss();
			}
		});//End
		
		UTF16_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lrc.setLrc(currSong.getmFilePath().substring(0, currSong.getmFilePath().indexOf('.'))+".lrc","UTF-16");
				lrc.setMaxTime(player.getDuration());
				vwLrc.setLyric(lrc);
				vwLrc.reset();
				vwLrc.setTime(0);
				vwLrc.postInvalidate();
				LrcCodeform.dismiss();
			}
		});//End
		
		Unicode_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lrc.setLrc(currSong.getmFilePath().substring(0, currSong.getmFilePath().indexOf('.'))+".lrc","Unicode");
				lrc.setMaxTime(player.getDuration());
				vwLrc.setLyric(lrc);
				vwLrc.reset();
				vwLrc.setTime(0);
				vwLrc.postInvalidate();
				LrcCodeform.dismiss();
			}
		});//End
		
		LrcCode_cancelbnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 关闭编码选择窗口
				 */
				LrcCodeform.dismiss();
			}
		});//End
		
		/**
		 * 菜单窗口的常用菜单下的更换主题选项按钮点击事件
		 * @author Streamer
		 */
		menu_change_theme.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：更换主题
				 */
				mymemu.dismiss();
				bgtheme_form.showAtLocation(bgtheme_choicView, Gravity.CENTER, 0, 0);
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				Log.i("menu_change_theme", "更换主题");
			}
		});//End
		
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
				//setContentView(showThemeView);
			}
		});
		
		/**
		 * 菜单窗口的常用菜单下的刷新曲库选项按钮点击事件
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
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * 刷新应用的数据
				 */
				mp.refreshLocalMusicInfo(MusicPlayer.this);
				mp.getAllMusicList().get(0).getList().clear();
				mp.getAllMusicList().get(0).getList().addAll(mp.getLocalMusicInfo());
				initialPlayList();
				Log.i("menu_refresh_list", "刷新曲库");
			}
		});//End
		
		/**
		 * 菜单窗口的常用菜单下的退出程序选项按钮点击事件
		 * @author Streamer
		 */
		menu_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：退出播放器，保存数据
				 */
				//Log.i("menu_exit", "退出播放器");
				showExitInfo();
			}
		});//End
		
		/**
		 * 菜单窗口的设置菜单下的网络检查选项按钮点击事件
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
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * 显示系统消息
				 */
				showSysInfo("该功能暂不可用！");
				//Log.i("menu_online_check", "网络检查");
			}
		});//End
		
		/**
		 * 菜单窗口的设置菜单下的蓝牙分享选项按钮点击事件
		 * @author Streamer
		 */
		menu_bluetooth_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：蓝牙分享
				 */
				mymemu.dismiss();
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * 显示系统消息
				 */
				showSysInfo("该功能暂不可用！");
				Log.i("menu_bluetooth_share", "蓝牙分享");
			}
		});//End
		
		/**
		 * 菜单窗口的设置菜单下的下载路径选项按钮点击事件
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
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * 设置文件浏览的确定键可用
				 */
				scanfile_ok_bnt.setEnabled(true);
				music_viewflipper.setDisplayedChild(4);
				//Log.i("menu_downloadpath", "下载路径选择");
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
							if(file.getPath().endsWith(".mp3")
									||file.getPath().endsWith(".MP3")
									||file.getPath().endsWith(".avi")
									||file.getPath().endsWith(".AVI"))
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
				    	showInfo("没有权限！");
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
					filelist = getFile(new File(scanfile_Textview.getText().toString()).getParent());
					scanfile_ok_bnt.setEnabled(false);
				}
				else
				{
					music_viewflipper.setDisplayedChild(0);
					change_titlebar_bnt_bg();
				}
			}
		});
		
		/**
		 * 文件浏览，播放键点击事件
		 * @author Streamer
		 */
		scanfile_play_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					File file = new File(scanfile_Textview.getText().toString());
					currSong = new SongInfo();
					currSong.setmSinger("<Unkown>");
					currSong.setmFileTitle(file.getName());
					currSong.setmFilePath(file.getPath());
					currSong.setmFileName(file.getName());
					play(scanfile_Textview.getText().toString());
					isplay = true;
					music_viewflipper.setDisplayedChild(0);
					change_titlebar_bnt_bg();
					currPlayList.add(currSong);
					curr_music_adapter.setList(currPlayList);
					currplaylist_listview.postInvalidate();
					CURR_STATE = STATE_PLAY;
					music_toolbar_play.setEnabled(true);
					music_toolbar_play.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
			}
		});
		
		/**
		 * 文件浏览，选择路径点击确定事件
		 * @author Streamer
		 */
		scanfile_ok_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).setTitle("下载路径如下：")
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
								music_viewflipper.setDisplayedChild(0);
							}
						}).create();
				dialog.show();
				scanfile_ok_bnt.setEnabled(false);
				change_titlebar_bnt_bg();
			}
		});
		
		/**
		 * 文件浏览的取消按钮点击事件
		 * @author Streamer
		 */
		scanfile_cancel_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				music_viewflipper.setDisplayedChild(0);
				change_titlebar_bnt_bg();
			}
		});//End
		
		/**
		 * 菜单窗口的设置菜单下的定时设置选项按钮点击事件
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
				final TimePicker timePicker = new TimePicker(MusicPlayer.this);
				timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR));
				timePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
				timePicker.setIs24HourView(true);
				Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
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
								showInfo("播放器将于"+hour1+":"+minute1+"退出。");
								dialog.dismiss();
							}
						}).create();
				dialog.show();
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				Log.i("menu_timing", "定时设置");
			}
		});
		
		/**
		 * 菜单窗口的帮助菜单下的流量统计选项按钮点击事件
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
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				showSysInfo("没有流量统计信息");
				Log.i("menu_flux_statistics", "统计流量");
			}
		});
		
		/**
		 * 菜单窗口的帮助菜单下的下载列表选项按钮点击事件
		 * @author Streamer
		 */
		menu_download_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：显示下载列表
				 */
				mymemu.dismiss();
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				showSysInfo("没有下载信息");
				//Log.i("menu_download_list", "显示下载列表");
			}
		});//End
		
		/**
		 * 菜单窗口的常用帮助下的意见反馈选项按钮点击事件
		 * @author Streamer
		 */
		menu_opition_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：弹出回馈窗口，E_mail发送窗口
				 */
				mymemu.dismiss();
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				
				final EditText editopinion = new EditText(MusicPlayer.this);
				editopinion.setHeight(200);
				editopinion.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
				editopinion.setGravity(Gravity.LEFT);
				Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
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
				Log.i("menu_opinion_return", "用户意见回馈");
			}
		});//End
	
		/**
		 * 菜单窗口的帮助菜单下的开发团队选项按钮点击事件
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
				
				final TimePicker timePicker = new TimePicker(MusicPlayer.this);
				timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR));
				timePicker .setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
				
				Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
						setTitle("Streamer 团队信息")
						.setItems(
								new String[]
								{
								  "李 婵 秀    （队长）"
								 ,"陈华红子   （搜集）"
								 ,"王 晓 亮    （支持）"
								 ,"李 亚 学    （保障）"
								 ,"吴 香 礼    （实施）"
								 ,"Dev By Streamer."}, null)
						.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).create();
				dialog.show();
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				Log.i("menu_dev_team", "显示开发团队信息");
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
				if(arg2<currPlayList.size())
				{
					Log.i("播放曲目路径", currPlayList.get(arg2).getmFilePath());
					currSong = currPlayList.get(arg2);
					play(currSong.getmFilePath());
					CURR_STATE = STATE_PLAY;
					music_viewflipper.setDisplayedChild(0);
					change_titlebar_bnt_bg();
					music_toolbar_play.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
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
		main_music_listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int index = arg2;
				if(arg1.getId() == R.id.music_listgroup)
				{
					//长按组
					if(arg2!=main_music_list.size()-1)
					{
						/*
						 * 初始化一些功能的可用状态
						 */
						if(arg2<=4)
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
						if(arg2 == 2)
						{
							group_longpress_clear.setEnabled(false);
						}
						else
						{
							group_longpress_clear.setEnabled(true);
						}
						group_longpressForm.showAsDropDown(arg1, 20, 10);
					}
					/**
					 * 长按媒体列表集合事件，播放该列表
					 * @author Streamer
					 */
					group_longpress_addtolist.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							currPlayList = main_music_list.get(index).getList();
							main_music_list.get(2).getList().clear();
							main_music_list.get(2).addAll(currPlayList);
							initialPlayList();
							group_longpressForm.dismiss();
						}
					});
					/**
					 * 长按媒体列表集合事件，更改列表名称
					 * @author Streamer
					 */
					group_longpress_altername.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							final EditText editnewlist = new EditText(MusicPlayer.this);
							editnewlist.setHeight(70);
							editnewlist.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
							editnewlist.setGravity(Gravity.LEFT);
							Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
									setTitle("新列表名").setView(editnewlist)
									.setNegativeButton("取消", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									}).setPositiveButton("更改",  new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											mp.getAllMusicList().get(index).setListName(editnewlist.getText().toString());
											initialPlayList();
											dialog.dismiss();
										}
									}).create();
							dialog.show();
						}
					});
					/**
					 * 长按媒体列表集合事件，删除列表
					 * @author Streamer
					 */
					group_longpress_del.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mp.getAllMusicList().remove(index);
							group_longpressForm.dismiss();
							initialPlayList();
						}
					});
					/**
					 * 长按媒体列表集合事件，清除该列表
					 * @author Streamer
					 */
					group_longpress_clear.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mp.getAllMusicList().get(index).getList().clear();
							initialPlayList();
							Log.i("listclear", "带刺");
							group_longpressForm.dismiss();
						}
					});
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
					});
					Log.i("组名", ((TextView)arg1.findViewById(R.id.music_mainlist_exlist_textview)).getText().toString());
				}
				else if(arg1.getId() == R.id.music_listchild)
				{
					//长按子
					child_longpressForm.showAsDropDown(arg1, 20, 10);
					/**
					 * 长按列表集合中的子项的添加到列表功能
					 * @author Streamer
					 */
					child_longpress_addtolist.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String [] listsname = new String[main_music_list.size()-1];
							for(int i=0;i<main_music_list.size()-1;i++)
							{
								listsname[i] = main_music_list.get(i).getListName();
							}
							Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).setTitle("添加到")
									.setItems(listsname, new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											
											main_music_list.get(which).getList().add
											((SongInfo)main_music_listview.getItemAtPosition(index));
										
										}
									})
									.setNegativeButton("取消", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									})
									.create();
							dialog.show();
							child_longpressForm.dismiss();
						}
					});
					/**
					 * 长按列表集合中的子项的更改名字功能
					 * @author Streamer
					 */
					child_longpress_altername.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							child_longpressForm.dismiss();
							
							final EditText editnewlist = new EditText(MusicPlayer.this);
							editnewlist.setHeight(70);
							editnewlist.setText(((SongInfo)main_music_listview.getItemAtPosition(index)).getmFileTitle());
							editnewlist.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
							editnewlist.setGravity(Gravity.LEFT);
							Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
									setTitle("新音乐名").setView(editnewlist)
									.setNegativeButton("取消", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									})
									.setPositiveButton("更改",  new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											
											final int i = mp.getAllMusicList().get(groupid).getList()
													.indexOf((SongInfo)main_music_listview.getItemAtPosition(index));
											final File file = new File(mp.getAllMusicList().get(groupid).getList().get(i).getmFilePath());
											String modname = editnewlist.getText().toString();  
											final String pFile = file.getParentFile().getPath()  
				                                    + "/";  
				                            final String newPath = pFile + modname+".mp3";  
				                            // 判断文件是否存在   
				                            if (new File(newPath).exists()) 
				                            {  
				                                // 排除修改文件时没修改直接送出的情况   
				                                if (!modname.equals(file.getName())) {  
				                                    // 跳出警告 文件名重复，并确认时候修改   
				                                    new AlertDialog.Builder(  
				                                            MusicPlayer.this)  
				                                            .setTitle("警告！")  
				                                            .setMessage("文件已存在，是否要覆盖？")  
				                                            .setPositiveButton(  
				                                                    "确定",  
				                                                    new DialogInterface.OnClickListener() {  
				  
				                                                        @Override  
				                                                        public void onClick(  
				                                                                DialogInterface dialog,  
				                                                                int which)
				                                                        {  
				                                                            // TODO   
				                                                            // Auto-generated   
				                                                            // method stub   
				                                                            // 单机确定，覆盖原来的文件   
				                                                            file.renameTo(new File(  
				                                                                    newPath));
				                                                            mp.getAllMusicList().get(groupid).getList().get(i).
				            												setmFileName(editnewlist.getText().toString());
				            												mp.getAllMusicList().get(groupid).getList().get(i).
				            												setmFileTitle(editnewlist.getText().toString());
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
				                                                            }).show();
				                                }
				                            }
											dialog.dismiss();
										}
									}).create();
							dialog.show();
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
							mp.getAllMusicList().get(groupid).getList().remove(main_music_list.get(groupid).
									getList().indexOf((SongInfo)main_music_listview.getItemAtPosition(index)));
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
							final SongInfo s = (SongInfo)main_music_listview.getItemAtPosition(index);
							Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
									setTitle("歌曲信息").setItems(new String[]
											{
											 "文件名："+s.getmFileTitle(),
											 "专辑名："+s.getmAlbum(),
											 "类   型："+s.getmFileType(),
											 "歌   手："+s.getmSinger(),
											 "时   长："+s.getmDuration()+"ms",
											 "大   小："+s.getmFileSize()+"byte",
											 "年	份："+s.getmYear()+"年",
											 "路	径："+s.getmFilePath()
											 }, null)
									.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									}).create();
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
							showSysInfo("该功能暂不可用！");
							child_longpressForm.dismiss();
						}
					});
					//Log.i("子名",((TextView)arg1.findViewById(R.id.songinfo_textview01)).getText().toString());
				}
				/*
				 * 返回true，不给系统传递按键信息，防止与click冲突
				 */
				return true;
			}
		});//End
		
		/**
		 * 之展开一个组，关闭其他展开的组
		 * @author Streamer
		 */
		main_music_listview.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				groupid = groupPosition;
				for(int i=0;i<main_music_list.size();i++)
				{
					if(i!=groupPosition)
					{
						main_music_listview.collapseGroup(i);
					}
				}
			}
		});//End
		/**
		 * 音乐列表集合的展开列表中的视频点击事件处理
		 * 播放点击的音乐频，将页面切换到播放页面，改变
		 * 响应控件图标
		 * @author Streamer
		 */
		main_music_listview.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Log.i("播放曲目路径", main_music_list.get(groupPosition).getList().get(childPosition).getmFilePath());
				/*
				 * 获取点击曲目
				 */
				currSong = main_music_list.get(groupPosition).getList().get(childPosition);
				play(currSong.getmFilePath());
				music_viewflipper.setDisplayedChild(0);
				change_titlebar_bnt_bg();
				CURR_STATE = STATE_PLAY;
				music_toolbar_play.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);	
				
				return false;
			}
			
		});
		
		/**
		 * 所有音乐列表的组项目点击事件
		 * @author Streamer
		 */
		main_music_listview.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：展开该音乐列表
				 */
				
				if(groupPosition==main_music_list.size()-1)
				{
					/*
					 * 点击的是新建列表
					 */
					final EditText editnewlist = new EditText(MusicPlayer.this);
					editnewlist.setHeight(70);
					editnewlist.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
					editnewlist.setGravity(Gravity.LEFT);
					Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
							setTitle("新建列表名").setView(editnewlist)
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).setPositiveButton("新建",  new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									MediaList<SongInfo> newlist = new MediaList<SongInfo>
									(editnewlist.getText().toString(), 0, new ArrayList<SongInfo>());
									if(mp.newMusicListByListName(newlist))
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
				
				//Log.i("你点击了",main_music_list.get(groupPosition).getListName());
				return false;				//返回true时下级菜单不显示
			}
		});		
		
		/**
		 * 音乐播放器准备就绪事件
		 */
		player.setOnPreparedListener(new OnPreparedListener() {

			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：显示歌曲播放信息，如歌曲名，时常
				 */
				Log.i("player", "准备就绪");
				lrc.setLrc(currSong.getmFilePath().substring(0, currSong.getmFilePath().indexOf('.'))+".lrc","GBK");
				lrc.setMaxTime(player.getDuration());
				vwLrc.setLyric(lrc);
				vwLrc.reset();
				vwLrc.setTime(0);
				vwLrc.postInvalidate();
				int i = player.getDuration();
				Log.d("onCompletion", "" + i);
				seekbar1.setMax(i);
				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				songlentext.setText(String.format("%02d:%02d:%02d", hour,
						minute, second));
				songtext.setText("当前播放 :"+currSong.getmFileTitle().substring(0, currSong.getmFileTitle().indexOf('.')));
				if(isplay)
				{
						player.start();
				}
				myHandler.sendEmptyMessage(PROGRESS_CHANGED);		
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
				 * 需要作出的响应：播放下一首音乐
				 */
				mp.reset();
				player.invalidate();
				Log.i("player",currSong.getmFileTitle()+"播放完毕" );
				//发送播放下一首消息
				myHandler.sendEmptyMessageDelayed(PLAYNEXT, 100);
				Log.i("player","接下来播放"+currSong.getmFileTitle() );
			}
		});
		
		player.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
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
		seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
		
	}

	/**
	 * 播放指定路径的媒体，并改变响应的状态
	 * @author Streamer
	 * @param MediaPath	媒体路径
	 * @return 播放成功返回true，否则返回false
	 * @see {@link MusicPlayer#play(String)}
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
    		isplay = true;
    		ispause = false;
    		
    		int index = currPlayList.indexOf(currSong);
    		if(index<currPlayList.size()-1||MODE_CURR_STATE == MODE_RANDOM)
    		{
    				music_toolbar_next.setEnabled(true);			//可用
    		}
    		else 
    		{
    			music_toolbar_next.setEnabled(false);
    		}
    		if(index>0||MODE_CURR_STATE == MODE_RANDOM)
    		{
    			music_toolbar_pre.setEnabled(true);				//可用
    		}
    		else
    		{
    			music_toolbar_pre.setEnabled(false);
    		}
    		music_toolbar_play.setEnabled(true);
    		seekbar1.setEnabled(true);	
    		Log.i("MediaPath", MediaPath);//进度条可用
    		if(mp.getAllMusicList().get(3).getList().lastIndexOf(currSong)==-1)
    		{
    			/*
    			 * 将歌曲加入最近播放
    			 */
    			mp.getAllMusicList().get(3).getList().add(currSong);
    			initialPlayList();
    		}
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    		
    }

	/**
	 * 定义一个函数，判断播放器如果处于暂停状态则重新播放，
	 * 否则如果处于播放状态，则暂停。
	 * @author Streamer
	 * @see {@link MusicPlayer#pause()}
	 */
	private void pause()
	{
		if(ispause)
		{
			player.start();
			seekbar1.setProgress(player.getCurrentPosition());
			ispause = false;
			isplay = true;
		}
		else if(isplay)
		{
			player.pause();
			ispause = true;
			isplay = false;
		}
	}
	
	/**
	 * 根据当前的播放曲目以及当前的播放列表获取下一曲目。
	 * 需要注意，该方法用于自动获取下一首，与单曲循环与否
	 * @author Streamer
	 * @param currSong  当前曲目
	 * @return 下一曲目
	 * @see {@link MusicPlayer#getNextSong(SongInfo)}
	 */
	private SongInfo getNextSong(SongInfo currSong)
	{
		
		SongInfo nextSong = currSong;
		if(MODE_CURR_STATE == MODE_LOOPALL)
		{
			//列表循环
			if(!currPlayList.isEmpty())
			{
				int index1 = currPlayList.indexOf(currSong)+1;
				if(index1<currPlayList.size()&&index1>=0)
				{
					nextSong = currPlayList.get(index1);
				}
				else if(index1==currPlayList.size())
				{
					nextSong = currPlayList.get(0);
				}
			}
		}
		else if(MODE_CURR_STATE == MODE_ORDER )
		{
			//顺序播放或单曲循环
			int index1 = currPlayList.indexOf(currSong)+1;
			if(index1<currPlayList.size()&&index1>0)
			{
				nextSong = currPlayList.get(index1);
			}
			else
			{
				nextSong = new SongInfo();
			}
		}
		else if(MODE_CURR_STATE == MODE_RANDOM)
		{
			//随机播放
			//返回指定范围的随机数(m-n之间)的公式[2]：Math.random()*(n-m)+m；
			int index1 = (int) (Math.random()*currPlayList.size());
			if(index1>0&&index1<currPlayList.size())
				nextSong = currPlayList.get(index1);
		}
		else if(MODE_CURR_STATE == MODE_LOOP) 
		{
			//单曲循环
			nextSong = currSong;
		}
		
		return nextSong;
		
	}
	
	/**
	 * 根据当前的播放曲目以及当前的播放列表，根据播放模式获取前一曲目。
	 * @author Streamer
	 * @param currSong  当前曲目
	 * @return 下一曲目
	 * @see {@link MusicPlayer#getPreSong(SongInfo)}
	 */
	private SongInfo getPreSong(SongInfo currSongInfo)
	{
		SongInfo preSong = new SongInfo();
		if(MODE_CURR_STATE == MODE_LOOPALL)
		{
			//列表循环
			int index = currPlayList.indexOf(currSong);
			if(index ==  0)
				preSong = currPlayList.get(currPlayList.size()-1);
			else if(index>0&&index<currPlayList.size())
				preSong = currPlayList.get(index-1);
		}
		else if(MODE_CURR_STATE == MODE_ORDER)
		{
			//顺序播放或单曲循环
			
			int index = currPlayList.indexOf(currSong)-1;
			if(index>=0&&index<currPlayList.size())
				preSong = currPlayList.get(index);
		}
		else if(MODE_CURR_STATE == MODE_RANDOM)
		{
			//随机播放
			//返回指定范围的随机数(m-n之间)的公式[2]：Math.random()*(n-m)+m；
			int index = (int) (Math.random()*currPlayList.size());
			if(index>0&&index<currPlayList.size())
				preSong = currPlayList.get(index);
		}
		return preSong;
	}
	
	/**
	 * 根据播放器当前状态更新标题栏的各个图标
	 * @author Streamer
	 * @see {@link MusicPlayer#change_titlebar_bnt_bg()}
	 */
	private boolean change_titlebar_bnt_bg()
	{
		if(music_viewflipper.getCurrentView().getId()==R.id.music_page1)
        {
        	music_titlebar_music.setBackgroundResource(R.drawable.ic_music_checked);
        }
        else
        {
        	music_titlebar_music.setBackgroundResource(R.drawable.ic_music);
        }
        if(music_viewflipper.getCurrentView().getId()==R.id.musicpage_mainListview)
        {
        	music_titlebar_list.setBackgroundResource(R.drawable.ic_list_checked);
        }
        else
        {
        	music_titlebar_list.setBackgroundResource(R.drawable.ic_list);
        }
        if(music_viewflipper.getCurrentView().getId()==R.id.musicpage_onlineweb)
        {
        	music_titlebar_online.setBackgroundResource(R.drawable.ic_online_checked);
        }
        else
        {
        	music_titlebar_online.setBackgroundResource(R.drawable.ic_online);
        }
        
        return true;
	}//End

	/**
	 * 显示文件列表
	 * @author Streamer
	 * @param path 显示的路径
	 * @see {@link VideoPlayer#getFile()}
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
			fileAdapter = new MyFileAdapter(MusicPlayer.this, list);
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
		main_music_list  = mp.getAllMusicList();
		/*
		 * 获取当前的播放列表，以后加载数据从数据库读取，保证系统的记忆性
		 * 读取的是默认列表，如果默认列表为空，则加载本地音乐列表
		 */
		currPlayList = main_music_list.get(2).getList();	
		if(currPlayList.isEmpty())
		{
			/*
			 * 如果默认列表为空，重新建立默认列表
			 */
			currPlayList = main_music_list.get(0).getList();
			main_music_list.get(2).getList().addAll(currPlayList);
		}
		Log.i("test", "长度"+currPlayList.size());
		/*
		 * 初始化适配器
		 */
		curr_music_adapter = new SongInfoListAdapter(this,currPlayList);
		currplaylist_listview.setAdapter(curr_music_adapter);
		main_music_adapter = new MusicListExpandableListAdapter(this,main_music_list);
		//给音乐的所有列表显示添加适配器
		main_music_listview.setAdapter(main_music_adapter);		
	}
	
	/**
	 * 显示退出提示窗口
	 * @author Streamer
	 */
	private void showExitInfo()
	{
		Dialog exitdialog = new AlertDialog.Builder(MusicPlayer.this)
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
	 * @see {@link MusicPlayer#showSysInfo(String)}
	 */
	private void showSysInfo(String info)
	{
		new AlertDialog.Builder(MusicPlayer.this).
				setTitle("系统信息").setMessage(info)
				.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create().show();
	}
	
	
	private void showWebView()
	{
		//onlinepage_music_webview.setWebViewClient(new DownLoadWebViewClient(MusicPlayer.this));
		onlinepage_music_webview.setWebViewClient(new WebViewClient()
		{

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				if (url.substring(url.length()-4).equals(".mp3"))//&&url.substring(7,10).equals("221")
	            {
	                 play(url);
	                 music_viewflipper.setDisplayedChild(0);
	            }
				return super.shouldOverrideUrlLoading(view, url);
			}
			 
		});
		WebSettings s = onlinepage_music_webview.getSettings();
		s.setSaveFormData(false);
		s.setSavePassword(false);
		s.setUseWideViewPort(true);
		s.setJavaScriptEnabled(true);
		s.setLightTouchEnabled(true);
		onlinepage_music_webview.setWebChromeClient(new WebChromeClient()
		{

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				//Activity和Webview根据加载程度决定进度条的进度大小
		           //当加载到100%的时候 进度条自动消失
		            MusicPlayer.this.setProgress(newProgress * 100);
				super.onProgressChanged(view, newProgress);
			}
			
		});
		onlinepage_music_webview.loadUrl("http://m.kugou.com");//http://m.hao123.com
	}
	
	/**
	 * 根据菜单选择的当前状态更新其标题栏的背景
	 * @author Streamer
	 * @return true
	 * @see {@link MusicPlayer#change_menu_bg()}
	 */
	private boolean change_menu_bg()
	{
		switch (menu_viewflipper.getCurrentView().getId()) {
		case R.id.menu_page1:
		{
			menu_common_bnt.setBackgroundResource(R.color.menu_bgcolor);
			menu_setting_bnt.setBackgroundResource(R.color.music_titlebar_bgcolor);
			menu_help_bnt.setBackgroundResource(R.color.music_titlebar_bgcolor);
			break;
		}
		case R.id.menu_page2:
		{
			menu_common_bnt.setBackgroundResource(R.color.music_titlebar_bgcolor);
			menu_setting_bnt.setBackgroundResource(R.color.menu_bgcolor);
			menu_help_bnt.setBackgroundResource(R.color.music_titlebar_bgcolor);
			break;
		}
		case R.id.menu_page3:
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//when pree the menukey into this
		if(menu_isShow)
		{
			mymemu.dismiss();
			music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
			menu_isShow = false;
		}
		else
		{
			music_toolbar_menu.setBackgroundResource(R.drawable.ic_menu_checked);
			mymemu.showAtLocation(music_toolbar_menu, Gravity.BOTTOM, 0, 80);
			menu_viewflipper.setDisplayedChild(0);
			change_menu_bg();
			menu_isShow =true;
		}
		return false;  //true turn sys menu else not return
	}

	@Override
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
	}

	/**
	 * 当播放器进入后台时
	 * @author Streamer
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		/*
		 * 保存音乐播放器的播放状态，当播放器退到后台时
		 */
		if(isplay)
		{
			/*
			 * 如果处于播放状态
			 */
			currtime = player.getCurrentPosition();
		}
		super.onPause();
	}//End

	/**
	 * 当应用从后台从新回到前台时
	 * @author Streamer
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(isplay)
		{
			/*
			 * 如果，退到后台时时是播放状态，从新播放
			 */
			player.seekTo(currtime);
			player.start();
		}
		super.onResume();
	}//End

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if(player.isPlaying())
			player.pause();
		onlinepage_music_webview.stopLoading();
		onlinepage_music_webview.clearAnimation();
		showInfo("正在保存数据...");
		MyDataBase mydatabase;
		mydatabase = new MyDataBase();
		mp.setLastSongIndex(currPlayList.lastIndexOf(currSong));
		//Log.i("Index", "msg"+currPlayList.lastIndexOf(currSong));
		main_music_list.get(2).getList().clear();
		/*
		 * 将当前播放列表放入默认列表
		 */
		main_music_list.get(2).getList().addAll(currPlayList);
		mp.resetAllMusicList(main_music_list);
		mydatabase.savaMusicData(mp.getAllMusicList());
		mp.setMusicPlayerMode(MODE_CURR_STATE);
		mp.SavaData();
		showInfo("音乐播放器退出成功，欢迎再次使用！");
		super.finish();
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
				if(music_viewflipper.getCurrentView().getId()==R.id.musicpage_onlineweb)
				{
					//显示的是网页时
					if(onlinepage_music_webview.canGoBack())
					{
						//如果网页可以后退，则显示上一网页
						onlinepage_music_webview.goBack();
					}
					else
					{
						/*
						 * 显示前一页面
						 */
						music_viewflipper.showPrevious();
						change_titlebar_bnt_bg();
					}
					return true;
				}
				if(music_viewflipper.getCurrentView().getId()!=R.id.music_page1)
				{
					/*
					 * 如果当前不是处在第一页面，显示上一页
					 */
					music_viewflipper.showPrevious();
					change_titlebar_bnt_bg();
					return true;
				}
				else
				{
					/*
					 * 如果当前处在第一页面，弹出退出提示窗口
					 */
					showExitInfo();
				}	
			}	
		}
		return super.onKeyDown(keyCode, event);
	}//End

}
