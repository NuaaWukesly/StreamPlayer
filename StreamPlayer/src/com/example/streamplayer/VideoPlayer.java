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
 *��Activity����ʵ����Ƶ���Ų��ֵ���ع��ܡ������������Ƶ���������ܣ�
 *���������ˡ�����/��ͣ�������л����棬������Ƶ�����
 *�����б���½���ѡ����ӡ�ɾ������Ƶ�����Ӻ�ɾ����
 *�����ĵ��ڣ�����ı任�������ˢ�£��������ͣ������Ƽ�
 *��������Ƶ���أ���ʱ������ͳ�ƹ��ܡ�
 *@author Streamer
 *@see {@link VideoPlayer}
 */

@SuppressWarnings("deprecation")
@SuppressLint({ "UnlocalizedSms", "ShowToast", "SetJavaScriptEnabled", "HandlerLeak" })
public class VideoPlayer extends Activity implements OnGestureListener{
	
	/*
	 * ��Ƶ�������ı������ϵ������ť����
	 */
	/**
	 * ��Ƶ��������������ȫ����ʾ����ѡ��ť
	 * @see {@link  VideoPlayer#video_titlebar_fullscreen}
	 */
	ImageButton video_titlebar_fullscreen;
	/**
	 * ��Ƶ�������������Ĳ�����������ѡ��ť
	 * @see {@link VideoPlayer#video_titlebar_video}
	 */
	ImageButton video_titlebar_video;
	/**
	 * ��Ƶ�������������Ĳ������б����ѡ��ť
	 * @see {@link VideoPlayer#video_titlebar_list}
	 */
	ImageButton video_titlebar_list;
	/**
	 * ��Ƶ����������������ҳ�������ѡ��ť
	 * @see {@link VideoPlayer#video_titlebar_online}
	 */
	ImageButton video_titlebar_online;
	/**
	 * ��Ƶ�����������ֲ������л���ť
	 * @see {@link VideoPlayer#video_titlebar_music}
	 */
	ImageButton video_titlebar_music;
	
	/*
	 * ��Ƶ�������Ĺ������ϵ������ť����
	 */
	
	/**
	 * ��Ƶ�������������Ĳ���/��ͣ��ť
	 * @see {@link VideoPlayer#video_toolbar_play}
	 */
	ImageButton video_toolbar_play;
	/**
	 * ��Ƶ�������������Ŀ���ѡ��ť
	 * @see {@link VideoPlayer#video_toolbar_goback}
	 */
	ImageButton video_toolbar_goback;
	/**
	 * ��Ƶ�������������Ŀ��ѡ��ť
	 * @see {@link VideoPlayer#video_toolbar_forward}
	 */
	ImageButton video_toolbar_forward;
	/**
	 * ��Ƶ����������������������ѡ��ť
	 * @see {@link VideoPlayer#video_toolbar_voice}
	 */
	ImageButton video_toolbar_voice;
	/**
	 * ��Ƶ�������������Ĳ˵�ѡ��ť
	 * @see {@link VideoPlayer#video_toolbar_menu}
	 */
	ImageButton video_toolbar_menu;
	
	/*
	 * ��Ƶ�����������沼�ֶ��壬�м䲿��
	 */
	/**
	 * �����л��鲼�֣����ڻ����л�
	 * @see {@link VideoPlayer#video_viewflipper}
	 */
	ViewFlipper video_viewflipper;
	/**
	 * �����л�ҳ��һ����Ƶ����������
	 * @see {@link VideoPlayer#player}
	 */
	VideoView player;
	/**
	 * �����л�ҳ��һ����Ƶ���Ž�����
	 * @see {@link VideoPlayer#seekbar}
	 */
	SeekBar seekbar;											
	/**
	 * �����л�ҳ��һ�Ĳ�����Ƶ������ʾ
	 * @see {@link VideoPlayer#videotext}
	 */
	TextView videotext;									
	/**
	 * �����л�ҳ��һ�ĵ�ǰ����λ����ʾ
	 * @see {@link VideoPlayer#videocurrtext}
	 */
	TextView videocurrtext;									
	/**
	 * �����л�ҳ��һ����Ƶ������ʾ
	 * @see {@link VideoPlayer#videolentext}
	 */
	TextView videolentext;		
	/**
	 * �����л�ҳ����Ĳ������б���ʾҳ�棬ʹ�ÿ���չ����
	 * @see {@link VideoPlayer#video_listview}
	 */
	ExpandableListView video_listview;
	/**
	 * �����л�ҳ��������ҳ���ҳ��
	 * @see {@link VideoPlayer#video_webview}
	 */
	WebView  video_webview;
	///////////////////////////////////////////////////////////////////////
	/**
	 * �����л�ҳ�������ڲ����б�
	 */
	LinearLayout page4;
	View page4_currplaylist_view;
	TextView currplaylistname_textview;
	ListView currplaylist_listview;
	/**
	 * �����л�ҳ�����ļ����
	 */
	/**
	 * �����л�ҳ����,���Բ��ִ��view
	 */
	LinearLayout page5;
	View page5_scanfile_view;
	ListView scanfile_listview;
	/**
	 * ��ʾѡ��·��
	 */
	TextView scanfile_Textview;
	Button scanfile_return_bnt;
	Button scanfile_play_bnt;
	Button scanfile_ok_bnt;
	Button scanfile_cancel_bnt;
	///////////////////////////////////////////////////////
	
	/*
	 * ��Ƶ�������Ĳ˵����ֶ���
	 */
	/**
	 * ��Ƶ�������Ĳ˵�����
	 * @see {@link VideoPlayer#mymemu}
	 */
	private PopupWindow mymemu;
	/**
	 * ��Ƶ�������Ĳ˵�������ҳ�沼��
	 * @see {@link VideoPlayer#menuView}
	 */
	View  menuView;
	/**
	 * ��Ƶ�������Ĳ˵����ڵĻ����л�����
	 * @see {@link VideoPlayer#menu_viewflipper}
	 */
	ViewFlipper menu_viewflipper;
	/**
	 * ��Ƶ�������Ĳ˵����ڵı������ĳ��ò˵�ѡ��ť
	 * @see {@link VideoPlayer#menu_common_bnt}
	 */
	Button menu_common_bnt;
	/**
	 * ��Ƶ�������Ĳ˵����ڵı����������ò˵�ѡ��ť
	 * @see {@link VideoPlayer#menu_setting_bnt}
	 */
	Button menu_setting_bnt;
	/**
	 * ��Ƶ�������Ĳ˵����ڵı������İ���˵�ѡ��ť
	 * @see {@link VideoPlayer#menu_help_bnt}
	 */
	Button menu_help_bnt;
	/**
	 * ��Ƶ�������ĵĲ˵����ڳ��ò˵��µ��Ƽ�����ѡ��
	 * @see {@link VideoPlayer#menu_friendpush_bnt}
	 */
	ImageButton menu_friendpush_bnt;
	/**
	 * ��Ƶ�������Ĳ˵����ڳ��ò˵��µĸı�����ѡ��
	 * @see {@link VideoPlayer#menu_change_theme}
	 */
	ImageButton menu_change_theme;
	/**
	 * ��Ƶ�������Ĳ˵����ڳ��ò˵��µ�ˢ���б�ѡ��
	 * @see {@link VideoPlayer#menu_refresh_list}
	 */
	ImageButton menu_refresh_list;
	/**
	 * ��Ƶ�������Ĳ˵����ڳ��ò˵��µ��˳�������ѡ��
	 * @see {@link VideoPlayer#menu_exit}
	 */
	ImageButton menu_exit;
	/**
	 * ��Ƶ�������Ĳ˵��������ò˵��µ��������ѡ��
	 * @see {@link VideoPlayer#menu_online_check}
	 */
	ImageButton menu_online_check;
	/**
	 * ��Ƶ�������Ĳ˵��������ò˵��µ��������͹���ѡ��
	 * @see {@link VideoPlayer#menu_bluetooth_share}
	 */
	ImageButton menu_bluetooth_share;
	/**
	 * ��Ƶ�������Ĳ˵��������ò˵��µ�����·��ѡ��ѡ��
	 * @see {@link VideoPlayer#menu_downloadpath}
	 */
	ImageButton menu_downloadpath;
	/**
	 * ��Ƶ�������Ĳ˵��������ò˵��µĶ�ʱ����ѡ��
	 * @see {@link VideoPlayer#menu_timing}
	 */
	ImageButton menu_timing;
	/**
	 * ��Ƶ�������˵����ڰ����˵��µ�����ͳ��ѡ��
	 * @see {@link VideoPlayer#menu_flux_statistics}
	 */
	ImageButton menu_flux_statistics;
	/**
	 * ��Ƶ�������˵����ڰ����˵��µ�ʹ��˵��ѡ��
	 * @see {@link  VideoPlayer#menu_useexplain}
	 */
	ImageButton menu_useexplain;
	/**
	 * ��Ƶ�������˵����ڰ����˵��µ��������ѡ��
	 * @see {@link  VideoPlayer#menu_opition_return}
	 */
	ImageButton menu_opition_return;
	/**
	 * ��Ƶ�������˵����ڰ����˵��µĿ����Ŷ�ѡ��
	 * @see {@link  VideoPlayer#menu_dev_team}
	 */
	ImageButton menu_dev_team;
	
	////////////////////////////////////////////////////////////////////////
	/**
	 * ��Ƶ�������б��鳤����������
	 */
	private PopupWindow group_longpressForm;
	View group_longpressView;
	private Button group_longpress_addtolist;
	private Button group_longpress_del;
	private Button group_longpress_altername;
	private Button group_longpress_cancel;
	private Button group_longpress_clear;
	/**
	 * ��Ƶ�������б��ӳ�����������
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
	 * ��Ƶ����������ѡ�񵯳��˵�
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
	 * �������
	 *///////////////////////////////////////////////////////////
	/*
	 * �б��ͱ���
	 *///////////////////////////////////////////////////////////
	/**
	 * ���ڴ�����е���Ƶ�б�����������Ƶ���ҵ����ء��ҵ�����б�
	 * @see {@link  VideoPlayer#main_video_list}
	 */
	List<MediaList<VideoInfo>> main_video_list;
	/**
	 * ���ڴ����Ƶ�б�
	 * @see {@link  VideoPlayer#video_list}
	 */
	List<VideoInfo> video_list;
	
	/*
	 * �������ͱ���
	 */////////////////////////////////////////////////////////////
	/**
	 * ����һ����������������ʾ���ڲ��ŵ������б�����������Ϊ
	 * {@link VideoInfoListAdapter}
	 * @see {@link  VideoPlayer#curr_video_adapter}
	 */
	private VideoInfoListAdapter curr_video_adapter;
	/**
	 * ����һ����������������ʾ���е���Ƶ�б����б�Ϊ����չ�б�
	 * ����������Ϊ{@link  VideoPlayer#main_video_adapter}
	 */
	private VideoListExpandableListAdapter main_video_adapter;
	
	/**
	 * ������ʾ�ļ����洢�ļ��ṹ��
	 */
	private MyFileAdapter fileAdapter;
	/**
	 * ���ڱ��浱ǰ��ʾ�ļ��б�
	 */
	List<myFile> filelist;
	
	
	/*
	 * �����ͱ���
	 */////////////////////////////////////////////////////////////
	/**
	 * ���ڼ�¼�˵����ڵ���ʾ״̬����ʾʱΪtrue������Ϊfalse
	 * @see {@link  VideoPlayer#menu_isShow}
	 */
	boolean menu_isShow = false;
	/**
	 * ���ڼ�¼��Ƶ�������������ĵ���һ����ť�Ŀ���״̬������ʱΪtrue��
	 * ������ʱΪfalse
	 * @see {@link  VideoPlayer#goback_isEnable}
	 */
	boolean goback_isEnable = false;
	/**
	 * ���ڼ�¼��Ƶ�������������Ĳ���/��ͣ��ť�Ŀ���״̬������ʱΪtrue
	 * ������ʱΪfalse
	 * @see {@link  VideoPlayer#play_isEnable}
	 */
	boolean play_isEnable = false;
	/**
	 * ���ڼ�¼��Ƶ�������������Ŀ��/��һ����ť�Ŀ���״̬������ʱΪtrue
	 * ������ʱΪfalse
	 * @see {@link  VideoPlayer#forward_isEnable}
	 */
	boolean forward_isEnable = false;
	/**
	 * ���ڼ�¼��Ƶ�������Ƿ��ڲ���״̬����Ϊtrue������Ϊfalse
	 * @see {@link  VideoPlayer#isplay}
	 */
	boolean isplay = false;
	/**
	 * ���ڼ�¼��Ƶ�������Ƿ�����ͣ״̬�����ɲ���״̬������ͣ��
	 * ��ʱΪtrue������Ϊfalse
	 * @see {@link  VideoPlayer#ispause}
	 */
	boolean ispause = false;

	/*
	 * �����������
	 */////////////////////////////////////////////////////////////////
	/**
	 * ���ڴ�Ų������������Ĳ���/��ͣ��ť�ڲ�ͬ״̬�µı���ͼ���id
	 * @see {@link VideoPlayer#video_toolbar_play_state}
	 */
	int []video_toolbar_play_state = new int[]
				{R.drawable.ic_bnt_play,R.drawable.ic_bnt_pause};
	
	/*
	 * ���ͳ���/����
	 *//////////////////////////////////////////////////////////////////
	/**
	 * ��������¼����״̬�µ�ͼ������
	 * {@link VideoPlayer#video_toolbar_play_state}�ж�Ӧ���±�
	 * @see {@link VideoPlayer#STATE_PLAY}
	 */
	private final static int STATE_PLAY = 1;
	/**
	 * ��������¼��ͣ״̬�µ�ͼ������
	 * {@link VideoPlayer#video_toolbar_play_state}�ж�Ӧ���±�
	 * @see {@link VideoPlayer#STATE_PAUSE}
	 */
	private final static int STATE_PAUSE = 0;
	/**
	 * ��Ϣ���б�֮���Ž��̸ı���Ϣ���
	 * @see {@link VideoPlayer##PROGRESS_CHANGED}
	 */
	private final static int PROGRESS_CHANGED = 0;
	/**
	 * ��Ϣ����֮������һ����Ϣ����
	 * @see {@link VideoPlayer#PLAYNEXT}
	 */
	private final static int PLAYNEXT = 1;
	/**
	 * �ж϶�ʱ������Ϣ
	 * @see {@link VideoPlayer#TIMING}
	 */
	private final static int TIMING = 2;
	/**
	 * ��¼��ǰ���ŵ�״̬��1Ϊ����״̬��0Ϊ��ͣ״̬
	 * @see {@link VideoPlayer#CURR_STATE}
	 */
	private int CURR_STATE = STATE_PAUSE;
	/**
	 * ��¼��ǰ���ŵ�ģʽ��
	 * @see {@link VideoPlayer#MODE_CURR_STATE}
	 */
	/*
	 * ��¼���õ�ʱ��
	 */
	private int hour1 = 25;
	private int minute1;
	/*
	 * ��¼��ǰ�򿪵����
	 */
	private int groupid;
	/**
	 * ��¼����·��
	 * @see {@link VideoPlayer#downloadpath}
	 */
	@SuppressWarnings("unused")
	private String downloadpath;
	/*
	 * ��Ƶ��Ϣ�ṹ����
	 */
	/**
	 * ��¼��ǰ���ڲ��ŵ���Ƶ����ϸ��Ϣ
	 * @see {@link VideoPlayer#currVideo}
	 */
	private VideoInfo currVideo;
	
	/*
	 * �����ඨ��
	 */
	/**
	 * ����һ������ʶ���࣬���ڼ����û��Ļ����¼�
	 * @see {@link VideoPlayer#detector}
	 */
	private GestureDetector detector;
	
	/*
	 * �ҵ�Ӧ������
	 */
	/**
	 * �����ҵ�Ӧ��{@link MyApplication}�����д�ų�����������Ҫ�ĸ�����Ϣ�����ݿ������
	 * ���˳�Ӧ��ʱ�����¼�����ݣ�
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
	 * �ҵ���ϸ���д���
	 */
	/**
	 * ��Ϣ���д��������������ֲ���������ʱ�����ĵ���Ϣ
	 * @see {@link VideoPlayer#myHandler}
	 */
	ProgressDialog dialogn;
	
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
	
	
	Handler myHandler = new Handler() {
	
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub		
			switch (msg.what) 
			{
				case PROGRESS_CHANGED:
				{
					/*
					 * ���ֲ��������Ž��ȸñ���Ϣ����
					 */
					if(player.isPlaying())
					{//�����ڲ���״̬ʱ�ſ��ԣ���Ȼerror
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
					//ѭ��������Ϣ��ʵ�ָ���
					sendEmptyMessage(PROGRESS_CHANGED);	
					break;
				}
				case PLAYNEXT:
				{
					/*
					 * ���ֲ�����������һ����Ϣ����
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
								dialogn.show(VideoPlayer.this, "", "������Դ...");
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
	 * ��ȡ��������
	 * @see {@link VideoPlayer#audioManager}
	 */
	AudioManager audioManager;
	/**
	 * ��������view
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
		 * ��ȡ������view
		 */
		showThemeView =getLayoutInflater().inflate(R.layout.activity_video_player,null);
		/*
		 * ����������Ϊ�ޱ���
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*
		 * ���ز����ļ�
		 */
		setContentView(showThemeView);
		/*
		 * ��ȡ�ҵ�Ӧ�ó������
		 */
		//receiver = new myReceiver();
		registerReceiver(myreceiver, new IntentFilter("MyReceiver"));
		downloadview = getLayoutInflater().inflate(R.layout.downloadlist, null);
		dl_filename_text = (TextView)downloadview.findViewById(R.id.dl_filename);
		dl_process_text = (TextView)downloadview.findViewById(R.id.dl_process);
		
		mp = (MyApplication)getApplication();
		/*
		 * ���������ֵı���
		 */
		showThemeView.setBackgroundResource(mp.getMusicPlayer_bgID());
		/*
		 * ��ȡ�����������
		 */
		audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
		/*
		 * ʵ������Ƶ������������������ؼ�
		 */
		video_titlebar_fullscreen = (ImageButton)findViewById(R.id.video_fullscreen);
		video_titlebar_video = (ImageButton)findViewById(R.id.video_video);
		video_titlebar_list = (ImageButton)findViewById(R.id.video_list);
		video_titlebar_online = (ImageButton)findViewById(R.id.video_online);
		video_titlebar_music = (ImageButton)findViewById(R.id.video_music);
		/*
		 * ʵ������Ƶ������������������ؼ�
		 */
		video_toolbar_play = (ImageButton)findViewById(R.id.video_toolbar_play);
		video_toolbar_goback = (ImageButton)findViewById(R.id.video_toolbar_goback);
		video_toolbar_forward = (ImageButton)findViewById(R.id.video_toolbar_forward);
		video_toolbar_voice = (ImageButton)findViewById(R.id.video_toolbar_voice);
		video_toolbar_menu = (ImageButton)findViewById(R.id.video_toolbar_menu);
		/*
		 * ʵ���������л�����
		 */
		video_viewflipper = (ViewFlipper)findViewById(R.id.video_viewflipper);
		/*
		 * �����л�ҳ��һ
		 *//////////////////////////////////////////////////////////////////////
		//���Ž���
		player = (VideoView)findViewById(R.id.video_player);
		seekbar = (SeekBar)findViewById(R.id.video_seekbar);
		videotext = (TextView)findViewById(R.id.video_showvideoname);
		videocurrtext = (TextView)findViewById(R.id.video_showvideocurr);
		videolentext = (TextView)findViewById(R.id.video_showvideolen);
		/*
		 * �����л�ҳ���
		 *////////////////////////////////////////////////////////////////////////
		video_listview = (ExpandableListView)findViewById(R.id.video_Listview);
		/*
		 * �����л�ҳ����
		 *///////////////////////////////////////////////////////////////////////
		video_webview = (WebView)findViewById(R.id.video_onlineweb);
		//////////////////////////////////////////////////////////////////////////
		/*
		 * �����л�ҳ����
		 */
		page4 = (LinearLayout)findViewById(R.id.video_page4);
		/*
		 * ��ǰ�����б�ҳ��
		 */
		page4_currplaylist_view = getLayoutInflater().inflate(R.layout.currplaylist_layout,null);
		currplaylistname_textview = (TextView)page4_currplaylist_view.findViewById(R.id.currlistname_textview);
		currplaylist_listview = (ListView)page4_currplaylist_view.findViewById(R.id.currlistview);
		//��ӵ�page4 
		page4.addView(page4_currplaylist_view);
		/*
		 * �����л�ҳ����
		 */
		page5 = (LinearLayout)findViewById(R.id.video_page5);
		/*
		 * �ļ����ҳ��
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
		 * ʵ�����˵����ڲ���
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
		 * ��ʼ�������б��鳤���¼���������
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
		 * ��ʼ�������б��ӳ������¼���������
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
		 * ��ʼ��ѡ�����ⴰ��
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
		 * �����ĳ�ʼ��
		 */
		//��ʼ����Ƶ�������Ĳ����б�,��������listview
		initialPlayList();
		dialogn = new ProgressDialog(VideoPlayer.this);
		mymemu = new PopupWindow(menuView, LayoutParams.FILL_PARENT, 150);
		main_video_adapter = new VideoListExpandableListAdapter(this, main_video_list);
		/*
		 * ����һЩ�����ĳ�ʼ״̬
		 */
		if(mp.getLastVideoIndex()<video_list.size()&&mp.getLastVideoIndex()>=0)
		{
			currVideo = video_list.get(mp.getLastVideoIndex());
			//play("rtsp://217.146.95.166:554/live/ch6bqvga.3gp");//currVideo.getPath()
			//pause();
			myHandler.sendEmptyMessage(PROGRESS_CHANGED);
			video_viewflipper.setDisplayedChild(0);	//��ʼʱ��ʾ��һҳ
			video_titlebar_video.setBackgroundResource(R.drawable.ic_video_checked);
		}
		else
		{
			video_toolbar_play.setEnabled(false);
			seekbar.setEnabled(false);
			video_toolbar_forward.setEnabled(false);
			video_toolbar_goback.setEnabled(false);
			video_viewflipper.setDisplayedChild(3);	//��ʼʱ��ʾ����ҳ
		}
		/*
		 * ���س�ʼ��Դ
		 */
		mymemu.setAnimationStyle(R.style.menuAnimation);
		video_toolbar_play.setBackgroundResource(R.drawable.ic_bnt_play);
		video_listview.setIndicatorBounds(
				getWindowManager().getDefaultDisplay().getWidth()-40, 
				getWindowManager().getDefaultDisplay().getWidth()-9);
		//��ʾ��ҳ
		showWebView();
		//��ʾ�ļ����
		filelist = getFile("/");
		
		/**
		 * �����л�ҳ���û����������¼�����,���¼���������ʶ���ദ��
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
		 * ����ʶ�����ʵ���������ڴ����û�������Ļ�¼�
		 * @author Streamer
		 */
		detector = new GestureDetector(this,new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				//�û��ᴥ��Ļ���ɿ���
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				//�û��ᴥ��Ļ����ĩ�ɿ����϶���ע�⣬ǿ������û���ɿ������϶�״̬ 
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				//�û�������Ļ���϶� 
				/*
				 if(video_viewflipper.getCurrentView().getId() == R.id.video_page1)
	                {
	                	int y = (int)(e1.getY()-e2.getY());
	                	if(y>20&&isplay)
	                	{
	                		//�û����ϻ�
	                		Log.i("���ϻ�", ""+y);
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
	                		//�û����»�
	                		Log.i("���ϻ�", ""+y);
	                		player.pause();
	                		//��ʱyΪ����
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
				//�û�������Ļ  
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				// TODO Auto-generated method stub
				
				//�û�������Ļ�������ƶ����ɿ�����������Ļ�ϻ�����  
                //e1:��һ��ACTION_DOWN�¼�����ָ���µ���һ�㣩  
                //e2:���һ��ACTION_MOVE�¼� ����ָ�ɿ�����һ�㣩  
                //velocityX:��ָ��x���ƶ����ٶ� ��λ������/��  
                //velocityY:��ָ��y���ƶ����ٶ� ��λ������/��  
				
				/*
				 * �û�������Ļ�Ƿ������¼���
				 * ��Ҫ��������Ӧ�����û�������Ļ���ȴﵽһ���̶��ǣ������û��Ļ�����
				 * ���л�����Ӧҳ�棬���ı�������ť�ı���ͼƬ������˵�������ʾ״̬ͬ
				 * ʱ���˵����ڹرգ������ֲ��������ڲ���״̬ʱ����ʧЧ����������Ӧ��
				 */
				float x = (float) (e2.getX() - e1.getX()); 
				//ע�������жϻ���������Ӧ��������δ��ʾʱ ��Ϊ musicplayer����δ����״̬ʱ
				//�������� videoplayer����Ӧ����
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
				//�û��ᴥ��Ļ������
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
		 * ��Ƶ�б��ϵ�չ���б��е���Ƶ����¼�����
		 * ���ŵ������Ƶ����ҳ���л�������ҳ�棬�ı�
		 * ��Ӧ�ؼ�ͼ��
		 * @author Streamer
		 */
		
		video_listview.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Log.i("������Ŀ·��", video_list.get(childPosition).getPath());
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
		 * ����ý���б������Ŀ����¼�
		 * @author Streamer
		 */
		video_listview.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ��չ���������б�
				 */
				
				if(groupPosition==main_video_list.size()-1)
				{
					/*
					 * ��������½��б�
					 */
					final EditText editnewlist = new EditText(VideoPlayer.this);
					editnewlist.setHeight(70);
					editnewlist.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
					editnewlist.setGravity(Gravity.LEFT);
					Dialog dialog = new AlertDialog.Builder(VideoPlayer.this)
							.setTitle("�½��б���")
							.setView(editnewlist)
							.setNegativeButton("ȡ��", 
									new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).setPositiveButton("�½�",  new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									MediaList<VideoInfo> newlist = new MediaList<VideoInfo>
									(editnewlist.getText().toString(), 0, new ArrayList<VideoInfo>());
									if(mp.newVideoListByListName(newlist))
									{
										showInfo("�б����ɹ���");
										initialPlayList();
									}
									else
									{
										showInfo("�б���ʧ�ܣ�");
									}
									dialog.dismiss();
								}
							}).create();
					dialog.show();
					
					return true;
				}
				return false;				//����trueʱ�¼��˵�����ʾ
			}
		});	//End
		
		/**
		 * ֮չ��һ���飬�ر�����չ������
		 * @author Streamer
		 */
		video_listview.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				/*
				 * ��ȡչ�������
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
		 * ��ǰ�����б���Ŀ����¼�
		 * @author Streamer
		 */
		currplaylist_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 //TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ������ѡ�����Ŀ������ͼ��ת����״̬��¼
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
					showInfo("����ʧ�ܣ�");
				}
			}
		});//End
		
		/**
		 * ��������չ�б�ĳ����¼�,ͨ���ж�view��id�����ж��ǳ����黹�ǳ�����
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
					//������
					if(arg2!=main_video_list.size()-1)
					{
						/*
						 * ��ʼ��һЩ���ܵĿ���״̬
						 */
						if(arg2<=5)
						{
							/*
							 * ϵͳԤ���б��ɱ��
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
							 * Ĭ���б������
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
					 * ����ý���б����¼������Ÿ��б�
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
					 * ����ý���б����¼��������б�����
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
							.setTitle("���б���")
							.setView(editnewlist)
							.setNegativeButton("ȡ��", 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									})
							.setPositiveButton("����", 
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
					 * ����ý���б����¼���ɾ���б�
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
					 * ����ý���б����¼���������б�
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
					 * ����ý���б����¼���ȡ������
					 * @author Streamer
					 */
					group_longpress_cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							group_longpressForm.dismiss();
						}
					});//End
					//Log.i("����", ((TextView)arg1.findViewById(R.id.music_mainlist_exlist_textview)).getText().toString());
				}//End if
				/*
				 * �������������Ԫ��
				 */
				else if(arg1.getId() == R.id.music_listchild)
				{
					//������Ԫ��
					child_longpressForm.showAsDropDown(arg1, 20, 10);
					/**
					 * �����б����е��������ӵ��б���
					 * @author Streamer
					 */
					child_longpress_addtolist.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String [] listsname = new String[main_video_list.size()-1];
							/*
							 * ��ȡ�����б�����
							 */
							for(int i=0;i<main_video_list.size()-1;i++)
							{
								listsname[i] = main_video_list.get(i).getListName();
							}
							Dialog dialog = new AlertDialog.Builder(VideoPlayer.this)
							.setTitle("��ӵ�")
							.setItems(listsname, 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											main_video_list.get(which).getList().add
											((VideoInfo)video_listview.getItemAtPosition(index));
										}
									})
							.setNegativeButton("ȡ��", 
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
					 * �����б����е�����ĸ������ֹ���
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
							.setTitle("����Ƶ��")
							.setView(editnewlist)
							.setNegativeButton("ȡ��", 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									})
							.setPositiveButton("����",  
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
				                            // �ж��ļ��Ƿ����   
				                            if (new File(newPath).exists()) 
				                            {  
				                                // �ų��޸��ļ�ʱû�޸�ֱ���ͳ������   
				                                if (!modname.equals(file.getName())) 
				                                {  
				                                    // �������� �ļ����ظ�����ȷ��ʱ���޸�   
				                                    new AlertDialog.Builder(  
				                                           VideoPlayer.this)  
				                                            .setTitle("���棡")  
				                                            .setMessage("�ļ��Ѵ��ڣ��Ƿ�Ҫ���ǣ�")  
				                                            .setPositiveButton("ȷ��",
				                                            		new DialogInterface.OnClickListener() {  
				  
				                                                        @Override  
				                                                        public void onClick(  
				                                                                DialogInterface dialog,  
				                                                                int which)
				                                                        {  
				                                                            // TODO   
				                                                            // Auto-generated   
				                                                            // method stub   
				                                                            // ����ȷ��������ԭ�����ļ�   
				                                                            file.renameTo(new File(  
				                                                                    newPath));
				                                                            /*
				                                                             * ���ĸ�ý������
				                                                             */
				                                                            mp.getAllVideoList().get(groupid).getList().get(i).
				            												setDisplayName(editnewlist.getText().toString());
				            												mp.getAllVideoList().get(groupid).getList().get(i).
				            												setTitle(editnewlist.getText().toString());
				            												initialPlayList();
				                                                            
				                                                        }
				                                                    })
				                                            .setNegativeButton(  
				                                                            "ȡ��",  
				                                                            new DialogInterface.OnClickListener() {  
				          
				                                                                @Override  
				                                                                public void onClick(  
				                                                                        DialogInterface dialog,  
				                                                                        int which) {  
				                                                                    // TODO   
				                                                                    // Auto-generated   
				                                                                    // method stub   
				                                                                }  
				                                                            }).show();//�����ĶԻ���
				                                }//if
				                            }//if
											dialog.dismiss();
										}//���ȷ���¼��������
									}).create();//�����
							dialog.show();//�����
						}
					});//End
					/**
					 * ȡ������
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
					 * �����б����е�������Ƴ��б���
					 * @author Streamer
					 */
					child_longpress_remove.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							/*
							 * �Ƴ�ѡ��ý��
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
					 * �����б����е�������ʾý����Ϣ����
					 * @author Streamer
					 */
					child_longpress_songinfo.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							child_longpressForm.dismiss();
							final VideoInfo s = (VideoInfo)video_listview.getItemAtPosition(index);
							Dialog dialog = new AlertDialog.Builder(VideoPlayer.this)
							.setTitle("ý����Ϣ")
							.setItems(new String[]
											{
											 "�ļ�����"+s.getTitle(),
											 "ר������"+s.getAlbum(),
											 "��   �ͣ�"+s.getMimeType(),
											 "�����ң�"+s.getArtist(),
											 "ʱ   ����"+s.getDuration()+"ms",
											 "��   С��"+s.getSize()+"byte",
											 "·	����"+s.getPath()
											 }, null)
							.setPositiveButton("ȷ��", 
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
					 * �����б����е�����ķ�����
					 * @author Streamer
					 */
					chile_longpress_share.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							child_longpressForm.dismiss();
							/*
							 * ��ʾϵͳ��Ϣ
							 */
							showSysInfo("�ù������ݲ����ã�");
						}
					});
					//Log.i("����",((TextView)arg1.findViewById(R.id.songinfo_textview01)).getText().toString());
				}//End
				/*
				 * ����true������ϵͳ���ݰ�����Ϣ����ֹ��click��ͻ
				 */
				return true;
			}
		});//End
		
		/**
		 * ȫ������
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
		 * ��ҳ����ʾ
		 * @author Streamer
		 */
		video_titlebar_video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(video_viewflipper.getCurrentView().getId() == R.id.video_page1)
				{
					//���������ȷ��ҳ��
				}
				else
				{
					video_viewflipper.setDisplayedChild(0);
				}
		        change_titlebar_bnt_bg();
			}
		});//End
		
		/**
		 * ��ʾ�б�ť����¼�
		 * @author Streamer
		 */
		video_titlebar_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(video_viewflipper.getCurrentView().getId() == R.id.video_Listview)
				{
					/*
					 * ��������б�����ʾ���ڲ����б�������ʾ�б���
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
		 * ѡ���������ҳ���¼�
		 * @author Streamer
		 */
		video_titlebar_online.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(video_viewflipper.getCurrentView().getId() == R.id.video_onlineweb)
				{
					//���������ȷ��ҳ��
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
		 * �������ֲ�������ť����¼�
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
		 * ������Ű�ť�¼�
		 * @author Streamer
		 */
		video_toolbar_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ��������ڲ���״̬������ͣ�����򲥷ţ�����¼�������Ĳ���״̬
				 *
				 * 
				 */
				if(CURR_STATE==STATE_PAUSE)
				{
					if(ispause)		//�Ǵ�����ͣ״̬
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
				//�ı�ð�ť�ı���
				seekbar.setEnabled(true);
				video_toolbar_play.setBackgroundResource
				(video_toolbar_play_state[CURR_STATE]);
			}
		});//End
		
		/**
		 * ���˰�ť����¼�������
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
				 * �¼������أ���ֹ�����¼���ͻ
				 */
				return true;
			}
		});//End
		
		/**
		 * ���˰�ť�����¼���������һý��
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
		 * ǰ����ť����¼������
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
		 * ����ǰ����ť�¼���������һý��
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
		 * �������ڰ�ť����¼�
		 * @author Streamer 
		 */
		video_toolbar_voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * �¼�������ý�������ɵ���ʱ�򵯳������������ڴ��ڣ���رմ���
				 * �Լ���¼�������ڴ��ڵ�״̬
				 */
				 audioManager.adjustStreamVolume(
				           AudioManager.STREAM_MUSIC,
				           AudioManager.ADJUST_SAME,
				           AudioManager.FLAG_PLAY_SOUND |AudioManager.FLAG_SHOW_UI);
			}
		});//End
		
		/**
		 * ѡ��˵���ť����¼�
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
		 * ѡ�񳣼��˵�
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
		 * ѡ�����ò˵�
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
		 * ѡ�����˵�
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
		 * ѡ���Ƽ�����
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
				 * ��ʾϵͳ��Ϣ
				 */
				showSysInfo("�ù����ݲ����ã�");
			}
		});//End
		
		/**
		 * ѡ��ı�����
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
		 * ����Ϊ�ı䲥��������
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
		 * ˢ�±��������б�
		 * @author Streamer
		 */
		menu_refresh_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/*
				 * ��Ҫ��������Ӧ��ˢ������
				 */
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * ˢ��Ӧ�õ�����
				 */
				mp.refreshLocalVideoInfo(VideoPlayer.this);
				mp.getAllVideoList().get(0).getList().clear();
				mp.getAllVideoList().get(0).getList().addAll(mp.getLocalVideoInfo());
				initialPlayList();
			}
		});//End

		/**
		 * ѡ���˳�����
		 * @author Streamer
		 */
		menu_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��ʾ�˳���ʾ����
				 */
				showExitInfo();
			}
		});//End

		/**
		 * ������繦��
		 * @author Streamer
		 */
		menu_online_check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ��������
				 */
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				
				/*
				 * ��ʾϵͳ��Ϣ
				 */
				showSysInfo("�ù����ݲ����ã�");
			}
		});//End
		
		/**
		 * �������͹���
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
				 * ��ʾϵͳ��Ϣ
				 */
				showSysInfo("�ù����ݲ����ã�");
			}
		});//End
		
		/**
		 * ѡ���ļ����ر���·��
		 * @author Streamer
		 */
		menu_downloadpath.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ������·��ѡ��
				 */
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * �����ļ������ȷ��������
				 */
				scanfile_ok_bnt.setEnabled(true);
				/*
				 * �����ļ��������
				 */
				video_viewflipper.setDisplayedChild(4);
			}
		});//End

		/**
		 * �ļ����������ļ��¼�
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
							/* ������ļ��о��ٽ�ȥ��ȡ */
							filelist = getFile(filelist.get(arg2).getFilePath());
							scanfile_ok_bnt.setEnabled(true);
						}
						else
						{
							/* ������ļ�*/ 
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
				      /* ����AlertDialog��ʾȨ�޲��� */
				    	showInfo("û��Ȩ��");
				    }
				  }
			}
		});//End
		
		/**
		 * �ļ���������ؼ�����¼�
		 * @author Streamer
		 */
		scanfile_return_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!scanfile_Textview.getText().toString().equals("/"))
				{
					/*
					 * ��������ϼ��ļ��У�������һ��
					 */
					filelist = getFile(new File(scanfile_Textview.getText().toString()).getParent());
					scanfile_ok_bnt.setEnabled(false);
				}
				else
				{
					/*
					 * ��������ϼ��ļ��У�����������
					 */
					video_viewflipper.setDisplayedChild(0);
					change_titlebar_bnt_bg();
				}
			}
		});//End
		
		/**
		 * �ļ���������ż�����¼�
		 * @author Streamer
		 */
		scanfile_play_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					File file = new File(scanfile_Textview.getText().toString());
					/*
					 * ����һ���µ�ý��ṹ��
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
					 * ��ӵ���ǰ�����б�
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
		 * �ļ������ѡ��·�����ȷ���¼�
		 * @author Streamer
		 */
		scanfile_ok_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog dialog = new AlertDialog.Builder(VideoPlayer.this).setTitle("����·�����£�")
						.setMessage(scanfile_Textview.getText().toString()).
						setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								downloadpath = scanfile_Textview.getText().toString();
								dialog.dismiss();
								/*
								 * �ص������� 
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
		 * �ļ������ȡ����ť����¼�
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
		 * ��ʱ����
		 * @author Streamer
		 */
		menu_timing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����ʱ����
				 */
				mymemu.dismiss();
				/*
				 * ����ʱ��ѡ�񴰿�
				 */
				final TimePicker timePicker = new TimePicker(VideoPlayer.this);
				timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR));
				timePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
				timePicker.setIs24HourView(true);
				Dialog dialog = new AlertDialog.Builder(VideoPlayer.this).
						setTitle("��ʱ�˳�").setView(timePicker)
						.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).setPositiveButton("ȷ��",  new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								hour1 = timePicker.getCurrentHour();
								minute1 = timePicker.getCurrentMinute();
								myHandler.sendEmptyMessage(TIMING);
								showInfo("Ӧ�ý���"+hour1+":"+minute1+"�رգ�");
								dialog.dismiss();
							}
						}).create();
				dialog.show();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
			}
		});//End
		
		/**
		 * ��ʾͳ��������Ϣ
		 * @author Streamer
		 */
		menu_flux_statistics.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ��ͳ������
				 */
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				//��ʾϵͳ��Ϣ
				showSysInfo("û������ͳ����Ϣ��");
			}
		});//End
		
		/**
		 * ��ʾʹ��˵������
		 * @author Streamer
		 */
		menu_useexplain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				//��ʾϵͳ��Ϣ
				//showSysInfo("�ù����ݲ����ã�");
				
				dl_dialog  = new AlertDialog.Builder(VideoPlayer.this)
				.setView(downloadview)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
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
		 * ���������������
		 * @author Streamer
		 */
		menu_opition_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ�������������ڣ�Message���ʹ���
				 */
				mymemu.dismiss();
				video_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				final EditText editopinion = new EditText(VideoPlayer.this);
				editopinion.setHeight(200);
				editopinion.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
				editopinion.setGravity(Gravity.LEFT);
				Dialog dialog = new AlertDialog.Builder(VideoPlayer.this).
						setTitle("�������").setView(editopinion)
						.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).setPositiveButton("����",  new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								SmsManager.getDefault().sendTextMessage
								("18061608810", null, "���������"+editopinion.getText().toString(), null, null);
								showInfo("��������ɹ������Ĺ��������ǵĶ�����");
								dialog.dismiss();
							}
						}).create();
				dialog.show();
			}
		});//End return opinion
		
		/**
		 * �����ʾ�����Ŷ���Ϣ�¼�
		 * @author Streamer
		 */
		menu_dev_team.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����ʾ�����Ŷ���Ϣ
				 */
				mymemu.dismiss();
				
				final TimePicker timePicker = new TimePicker(VideoPlayer.this);
				timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR));
				timePicker .setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
				
				Dialog dialog = new AlertDialog.Builder(VideoPlayer.this).
						setTitle("Streamer�Ŷ���Ϣ").setItems(new String[]
								{
								  "�� �� ��    ���ӳ���"
								 ,"�»�����   ���Ѽ���"
								 ,"�� �� ��    ��֧�֣�"
								 ,"�� �� ѧ    �����ϣ�"
								 ,"�� �� ��    ��ʵʩ��"
								 ,"Dev by Streamer."}, null)
						.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener() {
							
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
		 * ���ֲ�����׼�������¼�
		 * @author Streamer
		 */
		player.setOnPreparedListener(new OnPreparedListener() {

			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����ʾ����������Ϣ�����������ʱ��
				 */
				videotext.setText("��ǰ���� :"+currVideo.getDisplayName()
						.substring(0, currVideo.getDisplayName().indexOf('.')));
				if(!currVideo.getPath().startsWith("rtsp://")&&!currVideo.getPath().startsWith("http://"))
				{
					Log.i("player", "׼������");
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
		 * ���ֲ�������¼�
		 * @author Streamer
		 */
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stu
				/*
				 * ��Ҫ��������Ӧ��������һý��
				 */
				mp.reset();
				player.invalidate();
				//���Ͳ�����һý����Ϣ
				myHandler.sendEmptyMessageDelayed(PLAYNEXT, 100);
				//Log.i("player","����������"+currVideo.getDisplayName());
			}
		});
		
		/**
		 * ��������������ʱ��
		 * @author Streamer
		 */
		player.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				/*
				 * ���貥���������Զ�������һý��
				 */
				mp.reset();
				myHandler.sendEmptyMessageDelayed(PLAYNEXT, 100);
				/*Returns
				 *True if the method handled the error, 
				 *false if it didn't. Returning false, or not having an 
				 *OnErrorListener at all, will cause the OnCompletionListener to be called. 
				 */
				return false;				//����false��������
			}
		});
			
		/**
		 * �������ı��¼�
		 * @author Streamer
		 */
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekbar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				/*
				 * �û��ı����ʱ
				 * ��Ҫ��������Ӧ����λ����
				 */
				if (fromUser) {
					if(isplay)
					{
						/*
						 * ���û�����������ʱ��ֻ�е�ǰ���ڲ���״̬�Ż���Ӧ
						 */
						player.seekTo(progress);
					}
				}
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				//�û���ʼ����
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				//�û���������
			}
		});	
		
	}//End Create()
	
	/**
	 * ����ָ��·����ý�壬���ı���Ӧ��״̬
	 * @author Streamer
	 * @param MediaPath	ý��·��
	 * @return ���ųɹ�����true�����򷵻�false
	 * @see {@link VideoPlayer#play(String)}
	 */
	private boolean	play(String MediaPath)
    {
    	
		File file = new File(MediaPath);
    	if(file.exists())
    	{
    		/*
    		 * �ļ�����ʱ
    		 */
    		player.setVideoURI(Uri.parse(MediaPath));
    		//player.setVideoPath(MediaPath);
    		isplay = true;
    		ispause = false;
    		video_toolbar_play.setEnabled(true);
    		seekbar.setEnabled(true);	
    		Log.i("MediaPath", MediaPath);//����������
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
    		Log.i("MediaPath", MediaPath);//����������
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    		
    }//End play()

	/**
	 * ����һ���������жϲ��������������ͣ״̬�����²��ţ�
	 * ����������ڲ���״̬������ͣ��
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
	 * ���ݵ�ǰ�Ĳ�����Ŀ�Լ���ǰ�Ĳ����б��ȡ��һ��Ŀ��
	 * ��Ҫע�⣬�÷��������Զ���ȡ��һ��Ƶ
	 * @author Streamer
	 * @param currVideo  ��ǰ��Ƶ
	 * @return ��һ��Ƶ
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
	 * ���ݵ�ǰ�Ĳ�����Ƶ�Լ���ǰ�Ĳ����б��ȡǰһ��Ƶ��
	 * @author Streamer
	 * @param currVideo  ��ǰ��Ƶ
	 * @return ��һ��Ƶ
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
	 * ���ݵ�ǰ״̬�ı�������ĸ���ͼ��ı���
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
	 * ���ݵ�ǰ״̬���ı�˵������еı���
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
	 * ��ʾ�˳���ʾ����
	 * @author Streamer
	 */
	private void showExitInfo()
	{
		Dialog exitdialog = new AlertDialog.Builder(VideoPlayer.this)
		.setIcon(R.drawable.ic_launcher)
		.setTitle("ϵͳ��Ϣ")
		.setMessage("�㽫Ҫ�˳���������")
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		})
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				/*
				 * �˳�����
				 */
				finish();
			}
		})
		.create();
		exitdialog.show();
	}
	
	/**
	 * ��ʾ����Ϣ
	 * @author Streamer
	 * @param info ��ʾ����Ϣ
	 */
	private void showInfo(String info)
	{
		Toast.makeText(getApplicationContext(), info
				, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * ��ʾϵͳ��Ϣ����
	 * @author Streamer
	 * @param info ��Ϣ
	 * @see {@link VideoPlayer#showSysInfo(String)}
	 */
	private void showSysInfo(String info)
	{
		new AlertDialog.Builder(VideoPlayer.this).
				setTitle("ϵͳ��Ϣ").setMessage(info)
				.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create().show();
	}
	
	/**
	 * ��ʾ��ҳ��������ҳ����ش���
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
	 	            String urlStr = "";                                                                        //��Ž�����url
	 	            //�����utf8����
	 	            if (chartools.isUtf8Url(url))
	 	            {
	 	                    urlStr = chartools.Utf8URLdecode(url);
	 	            }
	 	            else 
	 	            {		//�������utf8����
	 	                    urlStr = URLDecoder.decode(url);
	 	            }
	                 String ss[] = urlStr.split("/");
	                 String fileName = ss[ss.length-1];                                //�õ������ļ���ȫ����������׺��
	                 Log.i("info", "musicfile: " + fileName);
	                 //���������Ӻ��ļ������ݸ�����ģ��
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
				//Activity��Webview���ݼ��س̶Ⱦ����������Ľ��ȴ�С
		        //�����ص�100%��ʱ�� �������Զ���ʧ
		        VideoPlayer.this.setProgress(newProgress * 100);
				super.onProgressChanged(view, newProgress);
			}
			
		});
		video_webview.loadUrl("http://192.168.42.62/Streamer/");//http://m.kugou.com
	}
	
	/**
	 * ��ʾ�ļ��б�
	 * @author Streamer
	 * @param path ��ʾ��·��
	 * @see {@link MediaPlayer#getFile()}
	 */
	public List<myFile> getFile(String path)
	{
		//����·��
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
						//���ļ���
						id = R.drawable.ic_fold;
					}
					else if(filepath.endsWith("MP3")||filepath.endsWith("mp3"))
					{
						//�����ļ�
						id = R.drawable.ic_mp3;
					}
					else if(filepath.endsWith("3gp")||filepath.endsWith("3GP")
							||filepath.endsWith("MP4")||filepath.endsWith("mp4"))
					{
						id = R.drawable.ic_mp4;
					}
					else
					{
						//�ļ�
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
	 * ��ʼ�����ֲ������ĸ����б�����������Ӧ��listview
	 * @author Streamer
	 */
	private void initialPlayList()
	{
		/*
		 * ��ʼ�����ֲ������ĸ��������б�
		 */
		main_video_list  = mp.getAllVideoList();
		/*
		 * ��ȡ��ǰ�Ĳ����б��Ժ�������ݴ����ݿ��ȡ����֤ϵͳ�ļ�����
		 * ��ȡ����Ĭ���б����Ĭ���б�Ϊ�գ�����ر��������б�
		 */
		video_list = main_video_list.get(3).getList();	
		if(video_list.isEmpty())
		{
			/*
			 * ���Ĭ���б�Ϊ�գ����½���Ĭ���б�
			 */
			video_list = main_video_list.get(0).getList();
			main_video_list.get(3).getList().addAll(video_list);
		}
		/*
		 * ��ʼ��������
		 */
		curr_video_adapter = new VideoInfoListAdapter(this,video_list);
		currplaylist_listview.setAdapter(curr_video_adapter);
		main_video_adapter = new VideoListExpandableListAdapter(this,main_video_list);
		//�����ֵ������б���ʾ���������
		video_listview.setAdapter(main_video_adapter);		
	}
	
	/**
	 * ��д�˵���ʾ����ʾ�Լ��Ĳ˵�
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
		showInfo("���ڱ�������...");
		MyDataBase mydatabase;
		mydatabase = new MyDataBase();
		mp.setLastVideoIndex(video_list.lastIndexOf(currVideo));
		main_video_list.get(3).getList().clear();
		/*
		 * ����ǰ�����б����Ĭ���б�
		 */
		main_video_list.get(3).getList().addAll(video_list);
		mp.resetAllVideoList(main_video_list);
		mydatabase.savaVideoData(mp.getAllVideoList());
		mp.SavaData();
		showInfo("��Ƶ�������˳��ɹ�����ӭ�ٴ�ʹ�ã�");
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	/**
	 * ���������¼�
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
				 * ��������¼�
				 */
				if(video_viewflipper.getCurrentView().getId()==R.id.video_online)
				{
					//��ʾ������ҳʱ
					if(video_webview.canGoBack())
					{
						//��ʾ��һҳ��
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
