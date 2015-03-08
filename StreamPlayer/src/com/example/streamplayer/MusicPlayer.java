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
 *��Activity����ʵ�����ֲ��Ų��ֵ���ع��ܡ�������������ֲ��������ܣ�
 *�粥��ģʽ����һ������һ��������/��ͣ�������л����棬�������������
 *�����б���½���ѡ����ӡ�ɾ�������������Ӻ�ɾ����
 *��ʵ�ͬ����ʾ�������ĵ��ڣ�����ı任�������ˢ�£���������
 *�������������أ���ʱ������ͳ�ƹ��ܡ�
 *@author Streamer
 *@see {@link MusicPlayer}
 */

@SuppressWarnings("deprecation")
@SuppressLint({ "NewApi", "HandlerLeak", "UnlocalizedSms", "SetJavaScriptEnabled" })
public class MusicPlayer extends Activity implements OnGestureListener {
	
	/*
	 * ���ֲ������ı������ϵ������ť����
	 */
	/**
	 * ���ֲ�����������������������ѡ��ť
	 * @see {@link MusicPlayer#music_titlebar_music}
	 */
	ImageButton music_titlebar_music;
	/**
	 * ���ֲ������������Ĳ������б����ѡ��ť
	 * @see {@link MusicPlayer#music_titlebar_list}
	 */
	ImageButton music_titlebar_list;
	/**
	 * ���ֲ���������������ҳ�������ѡ��ť
	 * @see {@link MusicPlayer#music_titlebar_online}
	 */
	ImageButton music_titlebar_online;
	/**
	 * ���ֲ��������������������ڴ���ѡ��ť
	 * @see {@link MusicPlayer#music_titlebar_voice}
	 */
	ImageButton music_titlebar_voice;
	/**
	 * ���ֲ���������Ƶ�������л���ť
	 * @see {@link MusicPlayer#music_titlebar_video}
	 */
	ImageButton music_titlebar_video;
	
	/*
	 * ���ֲ������Ĺ������ϵ������ť����
	 */
	/**
	 * ���ֲ������������Ĳ���ģʽѡ��ť
	 * @see {@link MusicPlayer#music_toolbar_mode}
	 */
	ImageButton music_toolbar_mode;
	/**
	 * ���ֲ���������������һ��ѡ��ť
	 * @see {@link MusicPlayer#music_toolbar_pre}
	 */
	ImageButton music_toolbar_pre;
	/**
	 * ���ֲ������������Ĳ���/��ͣ��ť
	 * @see {@link MusicPlayer#music_toolbar_play}
	 */
	ImageButton music_toolbar_play;
	/**
	 * ���ֲ���������������һ��ѡ��ť
	 * @see {@link MusicPlayer #music_toolbar_next}
	 */
	ImageButton music_toolbar_next;
	/**
	 * ���ֲ������������Ĳ˵�ѡ��ť
	 * @see {@link MusicPlayer#music_toolbar_menu}
	 */
	ImageButton music_toolbar_menu;
	
	/*
	 * ���ֲ����������沼�ֶ��壬�м䲿��
	 */
	/**
	 * �����л��鲼�֣����ڻ����л�
	 * @see {@link MusicPlayer#music_viewflipper}
	 */
	ViewFlipper music_viewflipper;
	/**
	 * �����л�ҳ��һ�ĸ����ʾҳ��,�������
	 * @see {@link MusicPlayer#curr_music_listview}
	 */
	//��linearLayout��Ÿ�ʿؼ�
	LinearLayout layout_lrc;
	//��ʶ���
	private SongLyric lrc;
	//��ʿؼ�
	private LyricView vwLrc;
	/*
	 * ע�⣬������ʹ�õ�ʱ��MediaPlayer ��getCurrentPosition������ʿؼ���setTime�У�
	 * ˢ�µ�ʱ��ֻ�ø÷�������
	 */
	
	/**
	 * �����л�ҳ��һ�ĸ������Ž�����
	 * @see {@link MusicPlayer#seekbar1}
	 */
	SeekBar seekbar1;											
	/**
	 * �����л�ҳ��Ĳ�����Ŀ��ʾ
	 * @see {@link MusicPlayer#songtext}
	 */
	TextView songtext;									
	/**
	 * �����л�ҳ��һ�ĵ�ǰ����λ����ʾ
	 * @see {@link MusicPlayer#songcurrtext}
	 */
	TextView songcurrtext;									
	/**
	 * �����л�ҳ��һ�ĸ���������ʾ
	 * @see {@link MusicPlayer#songlentext}
	 */
	TextView songlentext;									
	/**
	 * �����л�ҳ��һ�ĸ������ţ�ʹ��videoview
	 * @see {@link MusicPlayer#player}
	 */
	VideoView player;
	
	/**
	 * �����л�ҳ����Ĳ������б���ʾҳ�棬ʹ�ÿ���չ����
	 * @see {@link MusicPlayer#main_music_list}
	 */
	ExpandableListView main_music_listview;
	/**
	 * �����л�ҳ��������ҳ���ҳ��
	 * @see {@link MusicPlayer#onlinepage_music_webview}
	 */
	WebView  onlinepage_music_webview;
	/**
	 * �����л�ҳ����,���Բ��ִ������view
	 */
	LinearLayout page4;
	/**
	 * �����л�ҳ�������ڲ����б�
	 */
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
	/*
	 * ���ֲ������Ĳ˵����ֶ���
	 */
	/**
	 * ���ֲ������Ĳ˵�����
	 * @see {@link MusicPlayer#mymemu}
	 */
	private PopupWindow mymemu;
	
	/**
	 * ���ֲ������Ĳ˵�������ҳ�沼��
	 * @see {@link MusicPlayer#menuView}
	 */
	View  menuView;
	/**
	 * ���ֲ������Ĳ˵����ڵĻ����л�����
	 * @see {@link MusicPlayer#menu_viewflipper}
	 */
	ViewFlipper menu_viewflipper;
	/**
	 * ���ֲ������Ĳ˵����ڵı������ĳ��ò˵�ѡ��ť
	 * @see {@link MusicPlayer#menu_common_bnt}
	 */
	Button menu_common_bnt;
	/**
	 * ���ֲ������Ĳ˵����ڵı����������ò˵�ѡ��ť
	 * @see {@link MusicPlayer#menu_setting_bnt}
	 */
	Button menu_setting_bnt;
	/**
	 * ���ֲ������Ĳ˵����ڵı������İ���˵�ѡ��ť
	 * @see {@link MusicPlayer#menu_help_bnt}
	 */
	Button menu_help_bnt;
	/**
	 * ���ֲ������ĵĲ˵����ڳ��ò˵��µ���ʾ���ѡ��
	 * @see {@link MusicPlayer#menu_showLRC_bnt}
	 */
	ImageButton menu_LRCCode_bnt;
	/**
	 * ���ֲ������Ĳ˵����ڳ��ò˵��µĸı�����ѡ��
	 * @see {@link MusicPlayer#menu_change_theme}
	 */
	ImageButton menu_change_theme;
	/**
	 * ���ֲ������Ĳ˵����ڳ��ò˵��µ�ˢ���б�ѡ��
	 * @see {@link MusicPlayer#menu_refresh_list}
	 */
	ImageButton menu_refresh_list;
	/**
	 * ���ֲ������Ĳ˵����ڳ��ò˵��µ��˳�������ѡ��
	 * @see {@link MusicPlayer#menu_exit}
	 */
	ImageButton menu_exit;
	/**
	 * ���ֲ������Ĳ˵��������ò˵��µ��������ѡ��
	 * @see {@link MusicPlayer#menu_online_check}
	 */
	ImageButton menu_online_check;
	/**
	 * ���ֲ������Ĳ˵��������ò˵��µ��������͹���ѡ��
	 * @see {@link MusicPlayer#menu_bluetooth_share}
	 */
	ImageButton menu_bluetooth_share;
	/**
	 * ���ֲ������Ĳ˵��������ò˵��µ�����·��ѡ��ѡ��
	 * @see {@link MusicPlayer#menu_downloadpath}
	 */
	ImageButton menu_downloadpath;
	/**
	 * ���ֲ������Ĳ˵��������ò˵��µĶ�ʱ����ѡ��
	 * @see {@link MusicPlayer#menu_timing}
	 */
	ImageButton menu_timing;
	/**
	 * ���ֲ������˵����ڰ����˵��µ�����ͳ��ѡ��
	 * @see {@link MusicPlayer#menu_flux_statistics}
	 */
	ImageButton menu_flux_statistics;
	/**
	 * ���ֲ������˵����ڰ����˵��µ������б�ѡ��
	 * @see {@link MusicPlayer#menu_download_list}
	 */
	ImageButton menu_download_list;
	/**
	 * ���ֲ������˵����ڰ����˵��µ��������ѡ��
	 * @see {@link MusicPlayer#menu_opition_return}
	 */
	ImageButton menu_opition_return;
	/**
	 * ���ֲ������˵����ڰ����˵��µĿ����Ŷ�ѡ��
	 */
	ImageButton menu_dev_team;
	
	/**
	 * ���ֲ�������ʱ���ѡ�񴰿�
	 */
	private PopupWindow LrcCodeform;
	View LrcCodeView;
	private Button GBK_bnt;
	private Button UTF8_bnt;
	private Button UTF16_bnt;
	private Button Unicode_bnt;
	private Button LrcCode_cancelbnt;
	
	/**
	 * ���ֲ������б��鳤����������
	 */
	private PopupWindow group_longpressForm;
	View group_longpressView;
	private Button group_longpress_addtolist;
	private Button group_longpress_del;
	private Button group_longpress_altername;
	private Button group_longpress_cancel;
	private Button group_longpress_clear;
	/**
	 * ���ֲ������б��ӳ�����������
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
	 * ���ֲ���������ѡ�񵯳��˵�
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
	 * �������
	 *///////////////////////////////////////////////////////////
	/*
	 * �б��ͱ���
	 *///////////////////////////////////////////////////////////
	
	/**
	 * ���ڴ�����е������б������������֡��ҵ����ء��ҵ�����б�
	 * @see {@link MusicPlayer#main_music_list}
	 */
	List<MediaList<SongInfo>> main_music_list;

	/**
	 * ���ڴ�ŵ�ǰ���ڲ��ŵ������б�
	 * @see {@link MusicPlayer#currPlayList}
	 */
	List<SongInfo> currPlayList;
	
	/*
	 * �������ͱ���
	 */////////////////////////////////////////////////////////////
	/**
	 * ����һ����������������ʾ���ڲ��ŵ������б�����������Ϊ
	 * {@link SongInfoListAdapter}
	 * @see {@link MusicPlayer#curr_music_adapter}
	 */
	private SongInfoListAdapter curr_music_adapter;
	/**
	 * ����һ����������������ʾ���е������б����б�Ϊ����չ�б�
	 * ����������Ϊ{@link MusicListExpandableListAdapter}
	 */
	private MusicListExpandableListAdapter main_music_adapter;
	
	/**
	 * ������ʾ�ļ�
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
	 * @see {@link MusicPlayer#menu_isShow}
	 */
	boolean menu_isShow = false;
	/**
	 * ���ڼ�¼���ֲ������������ĵ���һ����ť�Ŀ���״̬������ʱΪtrue��
	 * ������ʱΪfalse
	 * @see {@link MusicPlayer#pre_isEnable}
	 */
	boolean pre_isEnable = false;
	/**
	 * ���ڼ�¼���ֲ������������Ĳ���/��ͣ��ť�Ŀ���״̬������ʱΪtrue
	 * ������ʱΪfalse
	 * @see {@link MusicPlayer#play_isEnable}
	 */
	boolean play_isEnable = false;
	/**
	 * ���ڼ�¼���ֲ���������������һ�װ�ť�Ŀ���״̬������ʱΪtrue
	 * ������ʱΪfalse
	 * @see {@link MusicPlayer#next_isEnable}
	 */
	boolean next_isEnable = false;
	/**
	 * ���ڼ�¼���ֲ������Ƿ��ڲ���״̬����Ϊtrue������Ϊfalse
	 * @see {@link MusicPlayer#isplay}
	 */
	private  boolean isplay = false;
	/**
	 * ���ڼ�¼���ֲ������Ƿ�����ͣ״̬�����ɲ���״̬������ͣ��
	 * ��ʱΪtrue������Ϊfalse
	 */
	private boolean ispause = false;
	
	/*
	 * �����������
	 */////////////////////////////////////////////////////////////////
	/**
	 * ���ڴ�����ֲ������������Ĳ���/��ͣ��ť�ڲ�ͬ״̬�µı���ͼ���id
	 * @see {@link MusicPlayer#music_toolbar_mode_state}
	 */
	int []music_toolbar_play_state = new int[]
				{R.drawable.ic_bnt_play,R.drawable.ic_bnt_pause};
	/**
	 * ���ڴ�����ֲ�������������ģʽѡ��ť�ڲ�ͬ״̬�µ�ͼ���id
	 * @see {@link MusicPlayer#music_toolbar_mode_state}
	 */
	int []music_toolbar_mode_state = new int[]
				{R.drawable.ic_mode_order,R.drawable.ic_mode_random,
			R.drawable.ic_mode_loopall,R.drawable.ic_mode_loop};
	
	/*
	 * ���ͳ���/����
	 *//////////////////////////////////////////////////////////////////
	/**
	 * ��������¼����״̬�µ�ͼ������
	 * {@link MusicPlayer#music_toolbar_play_state}�ж�Ӧ���±�
	 * @see {@link MusicPlayer#STATE_PLAY}
	 */
	private final static int STATE_PLAY = 1;
	/**
	 * ��������¼��ͣ״̬�µ�ͼ������
	 * {@link MusicPlayer#music_toolbar_play_state}�ж�Ӧ���±�
	 * @see {@link MusicPlayer#STATE_PAUSE}
	 */
	private final static int STATE_PAUSE = 0;
	/**
	 * ��������¼˳�򲥷�ģʽ�µ�ͼ������
	 * {@link MusicPlayer#music_toolbar_mode_state}��Ӧ���±�
	 * @see {@link MusicPlayer#MODE_ORDER}
	 */
	private final static int MODE_ORDER = 0;
	/**
	 * ��������¼�������ģʽ�µ�ͼ������
	 * {@link MusicPlayer#music_toolbar_mode_state}��Ӧ���±�
	 * @see {@link MusicPlayer#MODE_RANDOM}
	 */
	private final static int MODE_RANDOM = 1;
	/**
	 * ��������¼�б�ѭ������ģʽ�µ�ͼ������
	 * {@link MusicPlayer#music_toolbar_mode_state}��Ӧ���±�
	 * @see {@link MusicPlayer#MODE_LOOPALL}
	 */
	private final static int MODE_LOOPALL = 2;
	/**
	 * ��������¼����ѭ������ģʽ�µ�ͼ������
	 * {@link MusicPlayer#music_toolbar_mode_state}��Ӧ���±�
	 * @see {@link MusicPlayer#MODE_LOOP}
	 */
	private final static int MODE_LOOP = 3;
	/**
	 * ��Ϣ���б�֮���Ž��̸ı���Ϣ���
	 * @see {@link MusicPlayer#PROGRESS_CHANGED}
	 */
	private final static int PROGRESS_CHANGED = 0;
	/**
	 * ��Ϣ����֮������һ����Ϣ����
	 */
	private final static int PLAYNEXT = 1;
	/**
	 * �ж�ʱ����Ϣ
	 */
	private final static int TIMING = 2;
	/**
	 * ��¼��ǰ���ŵ�״̬��1Ϊ����״̬��0Ϊ��ͣ״̬
	 * @see {@link MusicPlayer#CURR_STATE}
	 */
	private int CURR_STATE = STATE_PAUSE;
	/**
	 * ��¼��ǰ���ŵ�ģʽ��
	 * @see {@link MusicPlayer#MODE_CURR_STATE}
	 */
	private  int MODE_CURR_STATE;
	/**
	 * ��¼���ֲ��ŵ�λ��
	 */
	private int currtime=0;
	/**
	 * ��¼���õ�ʱ��
	 */
	private int hour1 = 25;
	private int minute1;
	/*
	 * ��¼��ǰ�򿪵����
	 */
	private int groupid;
	/*
	 * ��¼����·��
	 */
	@SuppressWarnings("unused")
	private String downloadpath;
	/*
	 * ������Ϣ�ṹ����
	 */
	/**
	 * ��¼��ǰ���ڲ��ŵĸ�������ϸ��Ϣ
	 * @see {@link MusicPlayer#currSong}
	 */
	private SongInfo currSong;
	
	/*
	 * �����ඨ��
	 */
	/**
	 * ����һ������ʶ���࣬���ڼ����û��Ļ����¼�
	 * @see {@link MusicPlayer#detector}
	 */
	private GestureDetector detector;
	
	/*
	 * �ҵ�Ӧ������
	 */
	/**
	 * �����ҵ�Ӧ��{@link MyApplication}�����д�ų�����������Ҫ�ĸ�����Ϣ�����ݿ������
	 * ���˳�Ӧ��ʱ�����¼�����ݣ�
	 * @see {@link MusicPlayer#mp}
	 */
	MyApplication mp;
	
	/*
	 * �ҵ���ϸ���д���
	 */
	/**
	 * ��Ϣ���д��������������ֲ���������ʱ�����ĵ���Ϣ
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
						songcurrtext.setText(String.format("%02d:%02d:%02d", hour,
							minute, second));
						//���¸��
						vwLrc.setTime(player.getCurrentPosition());
						vwLrc.postInvalidate();
					}
					else
					{
						seekbar1.setProgress(0);
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
					currSong = getNextSong(currSong);
					play(currSong.getmFilePath());
					break;
				}
				case TIMING:
				{
					/*
					 * ��ʱ�жϣ��Ƿ�ʱ�䣬ʵ��ر�Ӧ��
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
	 * ��ȡ��������
	 * @see {@link MusicPlayer#audioManager}
	 */
	AudioManager audioManager;
	/*
	 * ��������view
	 */
	View showThemeView;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * ��ȡ������view
		 */
		showThemeView =getLayoutInflater().inflate(R.layout.activity_music_player,null);
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
		 * ʵ�������ֲ�����������������ؼ�
		 */
		music_titlebar_music = (ImageButton)findViewById(R.id.music_music);
		music_titlebar_list = (ImageButton)findViewById(R.id.music_list);
		music_titlebar_online = (ImageButton)findViewById(R.id.music_online);
		music_titlebar_voice = (ImageButton)findViewById(R.id.music_voice);
		music_titlebar_video = (ImageButton)findViewById(R.id.music_video);
		/*
		 * ʵ�������ֲ�����������������ؼ�
		 */
		music_toolbar_mode = (ImageButton)findViewById(R.id.music_toolbar_mode);
		music_toolbar_pre = (ImageButton)findViewById(R.id.music_toolbar_pre);
		music_toolbar_play = (ImageButton)findViewById(R.id.music_toolbar_play);
		music_toolbar_next =(ImageButton)findViewById(R.id.music_toolbar_next);
		music_toolbar_menu = (ImageButton)findViewById(R.id.music_toolbar_menu);
		/*
		 * ʵ���������л�����
		 */
		music_viewflipper = (ViewFlipper)findViewById(R.id.music_viewflipper);
		/*
		 * �����л�ҳ��һ
		 *//////////////////////////////////////////////////////////////////////
		//ʵ������ʶ���
		lrc = new SongLyric("");
		layout_lrc = (LinearLayout)findViewById(R.id.Linear_songLRC);
		vwLrc = new LyricView(layout_lrc.getContext());
		layout_lrc.addView(vwLrc);
		//Ϊ��ʿؼ���ʼ�������ø��
		player = (VideoView)findViewById(R.id.music_videoview);
		seekbar1 = (SeekBar)findViewById(R.id.music_seekbar);
		songtext = (TextView)findViewById(R.id.music_showsongname);
		songcurrtext = (TextView)findViewById(R.id.music_showsongcurr);
		songlentext = (TextView)findViewById(R.id.music_showsonglen);
		/*
		 * �����л�ҳ���
		 *//////////////////////////////////////////////////////////////////////////////////
		main_music_listview = (ExpandableListView)findViewById(R.id.musicpage_mainListview);
		/*
		 * �����л�ҳ����
		 *////////////////////////////////////////////////////////////////////////////
		onlinepage_music_webview = (WebView)findViewById(R.id.musicpage_onlineweb);
		/*
		 * �����л�ҳ����
		 */
		page4 = (LinearLayout)findViewById(R.id.music_page4);
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
		page5 = (LinearLayout)findViewById(R.id.music_page5);
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
		/*
		 * ʵ�����˵����ڲ���
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
		 * ��ʼ����ʱ���ѡ�񴰿�
		 */
		LrcCodeView = this.getLayoutInflater().inflate(R.layout.lrccode_layout, null);
		GBK_bnt = (Button)LrcCodeView.findViewById(R.id.GBK_bnt);
		UTF8_bnt = (Button)LrcCodeView.findViewById(R.id.UTF8_bnt);
		UTF16_bnt = (Button)LrcCodeView.findViewById(R.id.UTF16_bnt);
		Unicode_bnt = (Button)LrcCodeView.findViewById(R.id.Unicode_bnt);
		LrcCode_cancelbnt = (Button)LrcCodeView.findViewById(R.id.LrcCode_cancelbnt);
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
		/*
		 * �����ĳ�ʼ��
		 */
		//��ʼ�����ֲ������Ĳ����б�,��������listview
		initialPlayList();
		
		mymemu = new PopupWindow(menuView, LayoutParams.FILL_PARENT, 150);
		LrcCodeform = new PopupWindow
		(LrcCodeView, getWindowManager().getDefaultDisplay().getWidth()-40, 200);
		/*
		 * ����һЩ�����ĳ�ʼ״̬
		 */
		mymemu.setAnimationStyle(R.style.menuAnimation);	//�˵����ڶ������
		//Log.i("Index", "msg"+mp.getLastSongIndex());
		/*
		 * ��ʼ������ģʽ�Լ����ֲ�����
		 */
		MODE_CURR_STATE = mp.getMusicPlayerMode();
		if(mp.getLastSongIndex()<currPlayList.size()&&mp.getLastSongIndex()>=0)
		{
			currSong = currPlayList.get(mp.getLastSongIndex());
			play(currSong.getmFilePath());
			pause();
			myHandler.sendEmptyMessage(PROGRESS_CHANGED);
			music_viewflipper.setDisplayedChild(0);	//��ʼʱ��ʾ��һҳ
		}
		else
		{
			music_toolbar_next.setEnabled(false);	//��һ����ť������
			music_toolbar_pre.setEnabled(false);		//��һ����ť������
			music_toolbar_play.setEnabled(false);	//���ż������ã��иĶ�
			seekbar1.setEnabled(false);
			music_viewflipper.setDisplayedChild(3);	//��ʼʱ��ʾ����ҳ
		}
		/*
		 * ���س�ʼ��Դ
		 */
		music_toolbar_mode.setBackgroundResource(music_toolbar_mode_state[MODE_CURR_STATE]);
		music_toolbar_play.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
		music_titlebar_music.setBackgroundResource(R.drawable.ic_music_checked);
		main_music_listview.setIndicatorBounds(
				getWindowManager().getDefaultDisplay().getWidth()-40, 
				getWindowManager().getDefaultDisplay().getWidth()-9);
		//��ʾ��ҳ
		showWebView();
		//��ʾ�ļ����
		filelist = getFile("/");
		//Log.i("scanfile", "����");
		/**
		 * �����л�ҳ���û����������¼�����,���¼���������ʶ���ദ��
		 * @author Streamer
		 */
		music_viewflipper.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//��������ʶ����
				return detector.onTouchEvent(event);
			}
		});//End
		
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
				
				 if(music_viewflipper.getCurrentView().getId() == R.id.music_page1)
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
	                		//�û����»�
	                		Log.i("���ϻ�", ""+y);
	                		player.pause();
	                		//��ʱyΪ����
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
				//�û��ᴥ��Ļ������
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
		 * ���ֲ����������������ֲ���������ѡ��ť����¼�����
		 * @author Streamer
		 */
		music_titlebar_music.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * �¼���������ʾ�����л������ֲ��ŵ������棬���ı���
				 * �е����ֲ�������������ѡ�ť�ı���ͼƬ
				 */
				if(music_viewflipper.getCurrentView().getId() == R.id.music_page1)
				{
					//�������ȷ��ҳ�棬��������
				}
				else
				{
					//������Ǵ�����ȷ��ҳ�棬�л�ҳ��
					music_viewflipper.setDisplayedChild(0);
				}
				//�ı䰴ť����ͼƬ
		        change_titlebar_bnt_bg();
			}
		});//End
		
		/**
		 * ���ֲ����������������ֲ����������б���ʾ����ѡ��ť����¼�����
		 * �����ѡ��ť����¼�
		 * @author Streamer
		 */
		music_titlebar_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * �¼���������ʾ�����л������ֲ��ŵ����������б���ʾ���棬
				 * ���ı����е����ֲ�������������ѡ�ť�ı���ͼƬ
				 */
				if(music_viewflipper.getCurrentView().getId() == R.id.musicpage_mainListview)
				{
					//�������ȷ��ҳ�棬��������
					music_viewflipper.setDisplayedChild(3);
				}
				else
				{
					//������Ǵ�����ȷ��ҳ�棬�л�ҳ��
					music_viewflipper.setDisplayedChild(1);
				}
				//�ı䰴ť����ͼƬ
		        change_titlebar_bnt_bg();
			}
		});//End
		
		/**
		 * ���ֲ����������������ֲ��������������ʾ����ѡ��ť��������ѡ��ť����¼�
		 * @author Streamer
		 */
		music_titlebar_online.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * �¼���������ʾ�����л������ֲ��ŵ����������ʾ���棬
				 * ���ı����е����ֲ�������������ѡ�ť�ı���ͼƬ
				 */
				if(music_viewflipper.getCurrentView().getId() == R.id.musicpage_onlineweb)
				{
					//�������ȷ��ҳ�棬��������
				}
				else
				{
					//������Ǵ�����ȷ��ҳ�棬�л�ҳ��
					music_viewflipper.setDisplayedChild(2);
				}
				//�ı䰴ť����ͼƬ
		        change_titlebar_bnt_bg();
			}
		});//End
		
		/**
		 * ���ֲ����������������ֲ������������ѡ�ť����¼�
		 * @author Streamer
		 */
		music_titlebar_voice.setOnClickListener(new OnClickListener() {
			
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
		 * ���ֲ����������������ֲ������л�����Ƶ��������ѡ��ť����¼�
		 * @author Streamer
		 */
		music_titlebar_video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * �¼����������ֲ�������Ϊ������񱣴棬�����أ������л�����Ƶ������
				 */
				//Log.i(this.toString(), "������");
				Intent videoIntent = new Intent();
				videoIntent.setClass(MusicPlayer.this,VideoPlayer.class);
				//�ر����ֲ�����
				MusicPlayer.this.finish();
				//������Ƶ������
				MusicPlayer.this.startActivity(videoIntent);
			}
		});//End

		/**
		 * ���ֲ������������Ĳ���ģʽѡ��ť����¼�
		 * @author Streamer
		 */
		music_toolbar_mode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ�����θı��ǰ�Ĳ���ģʽ����ʾ��Ӧ��ģʽͼ�ꣻ
				 * �ı�һЩ�ؼ��Ŀ���״̬
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
		 * ���ֲ���������������һ��ѡ��ť����¼�
		 * @author Streamer
		 */
		music_toolbar_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ�����ݵ�ǰ��ģʽ��ȡ��һ�ײ�����Ŀ��������
				 */
				//��ȡǰһ����Ŀ��Ϣ���滻��ǰ��Ŀ
				currSong = getPreSong(currSong);	
				play(currSong.getmFilePath());
				CURR_STATE = STATE_PLAY;
				//�ı�ð�ť�ı���
				music_toolbar_play.setBackgroundResource
				(music_toolbar_play_state[CURR_STATE]);
			}
		});//End

		/**
		 * ���ֲ������������Ĳ���/��ͣѡ��ť����¼�
		 * @author Streamer
		 */
		music_toolbar_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ��������ڲ���״̬������ͣ�����򲥷ţ�����¼�������Ĳ���״̬
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
				seekbar1.setEnabled(true);
				music_toolbar_play.setBackgroundResource
				(music_toolbar_play_state[CURR_STATE]);
			}
		});//End

		/**
		 * ���ֲ���������������һ��ѡ�ť����¼�
		 * @author Streamer
		 */
		music_toolbar_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ�����ݵ�ǰ��ģʽ��ȡ��һ�ײ�����Ŀ��������
				 */
				//��ȡдһ����Ŀ��Ϣ���滻��ǰ��Ŀ
				int state= MODE_CURR_STATE;
				if(MODE_CURR_STATE == MODE_LOOP)
				{
					//������ѭ��״̬ʱ��getNextSong����ȡԭ���ĸ���������ȡ���ɹ�
					MODE_CURR_STATE = MODE_ORDER;
				}
				currSong = getNextSong(currSong);	
				MODE_CURR_STATE = state;
				play(currSong.getmFilePath());
				CURR_STATE = STATE_PLAY;
				//�ı�ð�ť�ı���
				music_toolbar_play.setBackgroundResource
				(music_toolbar_play_state[CURR_STATE]);
			}
		});//End

		/**
		 * ���ֲ������������Ĳ˵���ʾѡ��ť����¼�
		 * @author Streamer
		 */
		music_toolbar_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ������˵�������ʾ״̬����رգ�����򿪣�����¼�˵�����ʾ״̬
				 */
				if(menu_isShow)
				{
					//�˵�������ʾ״̬,�رղ˵�����
					mymemu.dismiss();
					//�ı䰴ť����
					music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
					//��¼״̬
					menu_isShow = false;
				}
				else
				{
					//�˵����ڹر�״̬����ʾ�˵�
					mymemu.showAtLocation(music_toolbar_menu, Gravity.BOTTOM, 0, 80);
					music_toolbar_menu.setBackgroundResource(R.drawable.ic_menu_checked);
					//�򿪲˵�Ĭ�ϳ���˵�ҳ��һ
					menu_viewflipper.setDisplayedChild(0);
					//�ı�˵�ҳ��������ı���״̬
					change_menu_bg();
					//��¼״̬
					menu_isShow = true;
				}
			}
		});//End

		/**
		 * �˵����ڵĳ��ò˵�ѡ�ť�����¼�
		 * @author Streamer
		 */
		menu_common_bnt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����ʾ���ò˵�ҳ�棬���ı�������������ť����
				 */
				menu_viewflipper.setDisplayedChild(0);
				change_menu_bg();
				return false;
			}
		});//End
		
		/**
		 * �˵����ڵ����ò˵�ѡ�ť�����¼�
		 * @author Streamer
		 */
		menu_setting_bnt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����ʾ���ò˵�ҳ�棬���ı�������������ť����
				 */
				menu_viewflipper.setDisplayedChild(1);
				change_menu_bg();
				return false;
			}
		});//End

		/**
		 * �˵����ڵİ����˵�ѡ�ť�����¼�
		 * @author Streamer
		 */
		menu_help_bnt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����ʾ�����˵�ҳ�棬���ı�������������ť����
				 */
				menu_viewflipper.setDisplayedChild(2);
				change_menu_bg();
				return false;
			}
		});//End

		/**
		 * �˵����ڵĳ��ò˵��µ���ʾ���ѡ�ť����¼�
		 * @author Streamer
		 */
		menu_LRCCode_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ��ѡ���ʱ�������
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
				Log.i("menu_showLRC_bnt", "�������");
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
				 * �رձ���ѡ�񴰿�
				 */
				LrcCodeform.dismiss();
			}
		});//End
		
		/**
		 * �˵����ڵĳ��ò˵��µĸ�������ѡ�ť����¼�
		 * @author Streamer
		 */
		menu_change_theme.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����������
				 */
				mymemu.dismiss();
				bgtheme_form.showAtLocation(bgtheme_choicView, Gravity.CENTER, 0, 0);
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				Log.i("menu_change_theme", "��������");
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
		 * �˵����ڵĳ��ò˵��µ�ˢ������ѡ�ť����¼�
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
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * ˢ��Ӧ�õ�����
				 */
				mp.refreshLocalMusicInfo(MusicPlayer.this);
				mp.getAllMusicList().get(0).getList().clear();
				mp.getAllMusicList().get(0).getList().addAll(mp.getLocalMusicInfo());
				initialPlayList();
				Log.i("menu_refresh_list", "ˢ������");
			}
		});//End
		
		/**
		 * �˵����ڵĳ��ò˵��µ��˳�����ѡ�ť����¼�
		 * @author Streamer
		 */
		menu_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ���˳�����������������
				 */
				//Log.i("menu_exit", "�˳�������");
				showExitInfo();
			}
		});//End
		
		/**
		 * �˵����ڵ����ò˵��µ�������ѡ�ť����¼�
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
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * ��ʾϵͳ��Ϣ
				 */
				showSysInfo("�ù����ݲ����ã�");
				//Log.i("menu_online_check", "������");
			}
		});//End
		
		/**
		 * �˵����ڵ����ò˵��µ���������ѡ�ť����¼�
		 * @author Streamer
		 */
		menu_bluetooth_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����������
				 */
				mymemu.dismiss();
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * ��ʾϵͳ��Ϣ
				 */
				showSysInfo("�ù����ݲ����ã�");
				Log.i("menu_bluetooth_share", "��������");
			}
		});//End
		
		/**
		 * �˵����ڵ����ò˵��µ�����·��ѡ�ť����¼�
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
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * �����ļ������ȷ��������
				 */
				scanfile_ok_bnt.setEnabled(true);
				music_viewflipper.setDisplayedChild(4);
				//Log.i("menu_downloadpath", "����·��ѡ��");
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
				      /* ����AlertDialog��ʾȨ�޲��� */
				    	showInfo("û��Ȩ�ޣ�");
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
		 * �ļ���������ż�����¼�
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
		 * �ļ������ѡ��·�����ȷ���¼�
		 * @author Streamer
		 */
		scanfile_ok_bnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).setTitle("����·�����£�")
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
								music_viewflipper.setDisplayedChild(0);
							}
						}).create();
				dialog.show();
				scanfile_ok_bnt.setEnabled(false);
				change_titlebar_bnt_bg();
			}
		});
		
		/**
		 * �ļ������ȡ����ť����¼�
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
		 * �˵����ڵ����ò˵��µĶ�ʱ����ѡ�ť����¼�
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
				final TimePicker timePicker = new TimePicker(MusicPlayer.this);
				timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR));
				timePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
				timePicker.setIs24HourView(true);
				Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
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
								showInfo("����������"+hour1+":"+minute1+"�˳���");
								dialog.dismiss();
							}
						}).create();
				dialog.show();
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				Log.i("menu_timing", "��ʱ����");
			}
		});
		
		/**
		 * �˵����ڵİ����˵��µ�����ͳ��ѡ�ť����¼�
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
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				showSysInfo("û������ͳ����Ϣ");
				Log.i("menu_flux_statistics", "ͳ������");
			}
		});
		
		/**
		 * �˵����ڵİ����˵��µ������б�ѡ�ť����¼�
		 * @author Streamer
		 */
		menu_download_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����ʾ�����б�
				 */
				mymemu.dismiss();
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				showSysInfo("û��������Ϣ");
				//Log.i("menu_download_list", "��ʾ�����б�");
			}
		});//End
		
		/**
		 * �˵����ڵĳ��ð����µ��������ѡ�ť����¼�
		 * @author Streamer
		 */
		menu_opition_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ�������������ڣ�E_mail���ʹ���
				 */
				mymemu.dismiss();
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				
				final EditText editopinion = new EditText(MusicPlayer.this);
				editopinion.setHeight(200);
				editopinion.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
				editopinion.setGravity(Gravity.LEFT);
				Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
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
				Log.i("menu_opinion_return", "�û��������");
			}
		});//End
	
		/**
		 * �˵����ڵİ����˵��µĿ����Ŷ�ѡ�ť����¼�
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
				
				final TimePicker timePicker = new TimePicker(MusicPlayer.this);
				timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR));
				timePicker .setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
				
				Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
						setTitle("Streamer �Ŷ���Ϣ")
						.setItems(
								new String[]
								{
								  "�� � ��    ���ӳ���"
								 ,"�»�����   ���Ѽ���"
								 ,"�� �� ��    ��֧�֣�"
								 ,"�� �� ѧ    �����ϣ�"
								 ,"�� �� ��    ��ʵʩ��"
								 ,"Dev By Streamer."}, null)
						.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).create();
				dialog.show();
				music_toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				Log.i("menu_dev_team", "��ʾ�����Ŷ���Ϣ");
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
				if(arg2<currPlayList.size())
				{
					Log.i("������Ŀ·��", currPlayList.get(arg2).getmFilePath());
					currSong = currPlayList.get(arg2);
					play(currSong.getmFilePath());
					CURR_STATE = STATE_PLAY;
					music_viewflipper.setDisplayedChild(0);
					change_titlebar_bnt_bg();
					music_toolbar_play.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
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
		main_music_listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int index = arg2;
				if(arg1.getId() == R.id.music_listgroup)
				{
					//������
					if(arg2!=main_music_list.size()-1)
					{
						/*
						 * ��ʼ��һЩ���ܵĿ���״̬
						 */
						if(arg2<=4)
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
					 * ����ý���б����¼������Ÿ��б�
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
					 * ����ý���б����¼��������б�����
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
									setTitle("���б���").setView(editnewlist)
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
											mp.getAllMusicList().get(index).setListName(editnewlist.getText().toString());
											initialPlayList();
											dialog.dismiss();
										}
									}).create();
							dialog.show();
						}
					});
					/**
					 * ����ý���б����¼���ɾ���б�
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
					 * ����ý���б����¼���������б�
					 * @author Streamer
					 */
					group_longpress_clear.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mp.getAllMusicList().get(index).getList().clear();
							initialPlayList();
							Log.i("listclear", "����");
							group_longpressForm.dismiss();
						}
					});
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
					});
					Log.i("����", ((TextView)arg1.findViewById(R.id.music_mainlist_exlist_textview)).getText().toString());
				}
				else if(arg1.getId() == R.id.music_listchild)
				{
					//������
					child_longpressForm.showAsDropDown(arg1, 20, 10);
					/**
					 * �����б����е��������ӵ��б���
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
							Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).setTitle("��ӵ�")
									.setItems(listsname, new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											
											main_music_list.get(which).getList().add
											((SongInfo)main_music_listview.getItemAtPosition(index));
										
										}
									})
									.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
										
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
					 * �����б����е�����ĸ������ֹ���
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
									setTitle("��������").setView(editnewlist)
									.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									})
									.setPositiveButton("����",  new DialogInterface.OnClickListener() {
										
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
				                            // �ж��ļ��Ƿ����   
				                            if (new File(newPath).exists()) 
				                            {  
				                                // �ų��޸��ļ�ʱû�޸�ֱ���ͳ������   
				                                if (!modname.equals(file.getName())) {  
				                                    // �������� �ļ����ظ�����ȷ��ʱ���޸�   
				                                    new AlertDialog.Builder(  
				                                            MusicPlayer.this)  
				                                            .setTitle("���棡")  
				                                            .setMessage("�ļ��Ѵ��ڣ��Ƿ�Ҫ���ǣ�")  
				                                            .setPositiveButton(  
				                                                    "ȷ��",  
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
				                                                            mp.getAllMusicList().get(groupid).getList().get(i).
				            												setmFileName(editnewlist.getText().toString());
				            												mp.getAllMusicList().get(groupid).getList().get(i).
				            												setmFileTitle(editnewlist.getText().toString());
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
							mp.getAllMusicList().get(groupid).getList().remove(main_music_list.get(groupid).
									getList().indexOf((SongInfo)main_music_listview.getItemAtPosition(index)));
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
							final SongInfo s = (SongInfo)main_music_listview.getItemAtPosition(index);
							Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
									setTitle("������Ϣ").setItems(new String[]
											{
											 "�ļ�����"+s.getmFileTitle(),
											 "ר������"+s.getmAlbum(),
											 "��   �ͣ�"+s.getmFileType(),
											 "��   �֣�"+s.getmSinger(),
											 "ʱ   ����"+s.getmDuration()+"ms",
											 "��   С��"+s.getmFileSize()+"byte",
											 "��	�ݣ�"+s.getmYear()+"��",
											 "·	����"+s.getmFilePath()
											 }, null)
									.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener() {
										
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
					 * �����б����е�����ķ�����
					 * @author Streamer
					 */
					chile_longpress_share.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							showSysInfo("�ù����ݲ����ã�");
							child_longpressForm.dismiss();
						}
					});
					//Log.i("����",((TextView)arg1.findViewById(R.id.songinfo_textview01)).getText().toString());
				}
				/*
				 * ����true������ϵͳ���ݰ�����Ϣ����ֹ��click��ͻ
				 */
				return true;
			}
		});//End
		
		/**
		 * ֮չ��һ���飬�ر�����չ������
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
		 * �����б��ϵ�չ���б��е���Ƶ����¼�����
		 * ���ŵ��������Ƶ����ҳ���л�������ҳ�棬�ı�
		 * ��Ӧ�ؼ�ͼ��
		 * @author Streamer
		 */
		main_music_listview.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Log.i("������Ŀ·��", main_music_list.get(groupPosition).getList().get(childPosition).getmFilePath());
				/*
				 * ��ȡ�����Ŀ
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
		 * ���������б������Ŀ����¼�
		 * @author Streamer
		 */
		main_music_listview.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ��չ���������б�
				 */
				
				if(groupPosition==main_music_list.size()-1)
				{
					/*
					 * ��������½��б�
					 */
					final EditText editnewlist = new EditText(MusicPlayer.this);
					editnewlist.setHeight(70);
					editnewlist.setWidth(getWindowManager().getDefaultDisplay().getWidth()-60);
					editnewlist.setGravity(Gravity.LEFT);
					Dialog dialog = new AlertDialog.Builder(MusicPlayer.this).
							setTitle("�½��б���").setView(editnewlist)
							.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).setPositiveButton("�½�",  new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									MediaList<SongInfo> newlist = new MediaList<SongInfo>
									(editnewlist.getText().toString(), 0, new ArrayList<SongInfo>());
									if(mp.newMusicListByListName(newlist))
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
				
				//Log.i("������",main_music_list.get(groupPosition).getListName());
				return false;				//����trueʱ�¼��˵�����ʾ
			}
		});		
		
		/**
		 * ���ֲ�����׼�������¼�
		 */
		player.setOnPreparedListener(new OnPreparedListener() {

			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����ʾ����������Ϣ�����������ʱ��
				 */
				Log.i("player", "׼������");
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
				songtext.setText("��ǰ���� :"+currSong.getmFileTitle().substring(0, currSong.getmFileTitle().indexOf('.')));
				if(isplay)
				{
						player.start();
				}
				myHandler.sendEmptyMessage(PROGRESS_CHANGED);		
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
				 * ��Ҫ��������Ӧ��������һ������
				 */
				mp.reset();
				player.invalidate();
				Log.i("player",currSong.getmFileTitle()+"�������" );
				//���Ͳ�����һ����Ϣ
				myHandler.sendEmptyMessageDelayed(PLAYNEXT, 100);
				Log.i("player","����������"+currSong.getmFileTitle() );
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
				return false;				//����false��������
			}
		});
			
		/**
		 * �������ı��¼�
		 * @author Streamer
		 */
		seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
		
	}

	/**
	 * ����ָ��·����ý�壬���ı���Ӧ��״̬
	 * @author Streamer
	 * @param MediaPath	ý��·��
	 * @return ���ųɹ�����true�����򷵻�false
	 * @see {@link MusicPlayer#play(String)}
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
    		isplay = true;
    		ispause = false;
    		
    		int index = currPlayList.indexOf(currSong);
    		if(index<currPlayList.size()-1||MODE_CURR_STATE == MODE_RANDOM)
    		{
    				music_toolbar_next.setEnabled(true);			//����
    		}
    		else 
    		{
    			music_toolbar_next.setEnabled(false);
    		}
    		if(index>0||MODE_CURR_STATE == MODE_RANDOM)
    		{
    			music_toolbar_pre.setEnabled(true);				//����
    		}
    		else
    		{
    			music_toolbar_pre.setEnabled(false);
    		}
    		music_toolbar_play.setEnabled(true);
    		seekbar1.setEnabled(true);	
    		Log.i("MediaPath", MediaPath);//����������
    		if(mp.getAllMusicList().get(3).getList().lastIndexOf(currSong)==-1)
    		{
    			/*
    			 * �����������������
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
	 * ����һ���������жϲ��������������ͣ״̬�����²��ţ�
	 * ����������ڲ���״̬������ͣ��
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
	 * ���ݵ�ǰ�Ĳ�����Ŀ�Լ���ǰ�Ĳ����б��ȡ��һ��Ŀ��
	 * ��Ҫע�⣬�÷��������Զ���ȡ��һ�ף��뵥��ѭ�����
	 * @author Streamer
	 * @param currSong  ��ǰ��Ŀ
	 * @return ��һ��Ŀ
	 * @see {@link MusicPlayer#getNextSong(SongInfo)}
	 */
	private SongInfo getNextSong(SongInfo currSong)
	{
		
		SongInfo nextSong = currSong;
		if(MODE_CURR_STATE == MODE_LOOPALL)
		{
			//�б�ѭ��
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
			//˳�򲥷Ż���ѭ��
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
			//�������
			//����ָ����Χ�������(m-n֮��)�Ĺ�ʽ[2]��Math.random()*(n-m)+m��
			int index1 = (int) (Math.random()*currPlayList.size());
			if(index1>0&&index1<currPlayList.size())
				nextSong = currPlayList.get(index1);
		}
		else if(MODE_CURR_STATE == MODE_LOOP) 
		{
			//����ѭ��
			nextSong = currSong;
		}
		
		return nextSong;
		
	}
	
	/**
	 * ���ݵ�ǰ�Ĳ�����Ŀ�Լ���ǰ�Ĳ����б����ݲ���ģʽ��ȡǰһ��Ŀ��
	 * @author Streamer
	 * @param currSong  ��ǰ��Ŀ
	 * @return ��һ��Ŀ
	 * @see {@link MusicPlayer#getPreSong(SongInfo)}
	 */
	private SongInfo getPreSong(SongInfo currSongInfo)
	{
		SongInfo preSong = new SongInfo();
		if(MODE_CURR_STATE == MODE_LOOPALL)
		{
			//�б�ѭ��
			int index = currPlayList.indexOf(currSong);
			if(index ==  0)
				preSong = currPlayList.get(currPlayList.size()-1);
			else if(index>0&&index<currPlayList.size())
				preSong = currPlayList.get(index-1);
		}
		else if(MODE_CURR_STATE == MODE_ORDER)
		{
			//˳�򲥷Ż���ѭ��
			
			int index = currPlayList.indexOf(currSong)-1;
			if(index>=0&&index<currPlayList.size())
				preSong = currPlayList.get(index);
		}
		else if(MODE_CURR_STATE == MODE_RANDOM)
		{
			//�������
			//����ָ����Χ�������(m-n֮��)�Ĺ�ʽ[2]��Math.random()*(n-m)+m��
			int index = (int) (Math.random()*currPlayList.size());
			if(index>0&&index<currPlayList.size())
				preSong = currPlayList.get(index);
		}
		return preSong;
	}
	
	/**
	 * ���ݲ�������ǰ״̬���±������ĸ���ͼ��
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
	 * ��ʾ�ļ��б�
	 * @author Streamer
	 * @param path ��ʾ��·��
	 * @see {@link VideoPlayer#getFile()}
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
			fileAdapter = new MyFileAdapter(MusicPlayer.this, list);
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
		main_music_list  = mp.getAllMusicList();
		/*
		 * ��ȡ��ǰ�Ĳ����б��Ժ�������ݴ����ݿ��ȡ����֤ϵͳ�ļ�����
		 * ��ȡ����Ĭ���б����Ĭ���б�Ϊ�գ�����ر��������б�
		 */
		currPlayList = main_music_list.get(2).getList();	
		if(currPlayList.isEmpty())
		{
			/*
			 * ���Ĭ���б�Ϊ�գ����½���Ĭ���б�
			 */
			currPlayList = main_music_list.get(0).getList();
			main_music_list.get(2).getList().addAll(currPlayList);
		}
		Log.i("test", "����"+currPlayList.size());
		/*
		 * ��ʼ��������
		 */
		curr_music_adapter = new SongInfoListAdapter(this,currPlayList);
		currplaylist_listview.setAdapter(curr_music_adapter);
		main_music_adapter = new MusicListExpandableListAdapter(this,main_music_list);
		//�����ֵ������б���ʾ���������
		main_music_listview.setAdapter(main_music_adapter);		
	}
	
	/**
	 * ��ʾ�˳���ʾ����
	 * @author Streamer
	 */
	private void showExitInfo()
	{
		Dialog exitdialog = new AlertDialog.Builder(MusicPlayer.this)
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
	 * @see {@link MusicPlayer#showSysInfo(String)}
	 */
	private void showSysInfo(String info)
	{
		new AlertDialog.Builder(MusicPlayer.this).
				setTitle("ϵͳ��Ϣ").setMessage(info)
				.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener() {
					
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
				//Activity��Webview���ݼ��س̶Ⱦ����������Ľ��ȴ�С
		           //�����ص�100%��ʱ�� �������Զ���ʧ
		            MusicPlayer.this.setProgress(newProgress * 100);
				super.onProgressChanged(view, newProgress);
			}
			
		});
		onlinepage_music_webview.loadUrl("http://m.kugou.com");//http://m.hao123.com
	}
	
	/**
	 * ���ݲ˵�ѡ��ĵ�ǰ״̬������������ı���
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
	 * �������������̨ʱ
	 * @author Streamer
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		/*
		 * �������ֲ������Ĳ���״̬�����������˵���̨ʱ
		 */
		if(isplay)
		{
			/*
			 * ������ڲ���״̬
			 */
			currtime = player.getCurrentPosition();
		}
		super.onPause();
	}//End

	/**
	 * ��Ӧ�ôӺ�̨���»ص�ǰ̨ʱ
	 * @author Streamer
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(isplay)
		{
			/*
			 * ������˵���̨ʱʱ�ǲ���״̬�����²���
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
		showInfo("���ڱ�������...");
		MyDataBase mydatabase;
		mydatabase = new MyDataBase();
		mp.setLastSongIndex(currPlayList.lastIndexOf(currSong));
		//Log.i("Index", "msg"+currPlayList.lastIndexOf(currSong));
		main_music_list.get(2).getList().clear();
		/*
		 * ����ǰ�����б����Ĭ���б�
		 */
		main_music_list.get(2).getList().addAll(currPlayList);
		mp.resetAllMusicList(main_music_list);
		mydatabase.savaMusicData(mp.getAllMusicList());
		mp.setMusicPlayerMode(MODE_CURR_STATE);
		mp.SavaData();
		showInfo("���ֲ������˳��ɹ�����ӭ�ٴ�ʹ�ã�");
		super.finish();
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
				if(music_viewflipper.getCurrentView().getId()==R.id.musicpage_onlineweb)
				{
					//��ʾ������ҳʱ
					if(onlinepage_music_webview.canGoBack())
					{
						//�����ҳ���Ժ��ˣ�����ʾ��һ��ҳ
						onlinepage_music_webview.goBack();
					}
					else
					{
						/*
						 * ��ʾǰһҳ��
						 */
						music_viewflipper.showPrevious();
						change_titlebar_bnt_bg();
					}
					return true;
				}
				if(music_viewflipper.getCurrentView().getId()!=R.id.music_page1)
				{
					/*
					 * �����ǰ���Ǵ��ڵ�һҳ�棬��ʾ��һҳ
					 */
					music_viewflipper.showPrevious();
					change_titlebar_bnt_bg();
					return true;
				}
				else
				{
					/*
					 * �����ǰ���ڵ�һҳ�棬�����˳���ʾ����
					 */
					showExitInfo();
				}	
			}	
		}
		return super.onKeyDown(keyCode, event);
	}//End

}
