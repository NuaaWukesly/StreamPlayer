package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.dataclass.MediaList;
import com.example.dataclass.SongInfo;
import com.example.dataclass.VideoInfo;
import com.example.mydatabase.MyDataBase;
import com.example.streamplayer.R;

/**
 * �����ҵ�Application,���ڴ������Ӧ���õ������ݡ�
 * @author ������
 * @see MyApplication
 */
public class MyApplication extends Application{

	/**
	 *�����б��ϣ� ���ڴ洢�����б�ÿһ������������б�����ƺ͸��б�ĳ�Ա��������ĵ�ý������
	 * ÿһ��Ϊ{@link MediaList}�ĳ�Ա��һ���б�
	 */
	private List<MediaList<SongInfo>> music_class_list;  
	/**
	 * ��Ƶ�б��ϣ����ڴ洢��Ƶ�б�ÿһ���������Ƶ�б�����ƺ͸��б�ĳ�Ա��������ĵ�ý������
	 * ÿһ��Ϊ{@link MediaList}�ĳ�Ա��һ���б�
	 */
	private List<MediaList<VideoInfo>> video_class_list;	
	/**
	 * ��¼��������ȫ�������������л�ʱ������
	 */
	private int video_currtime;
	private boolean video_isplay;
	/**
	 * �˳���Ƶ������ʱ���ڲ��ŵ���Ƶ���б������
	 */
	private int lastVideoIndex;
	/**
	*�˳����ֲ�����ʱ���ڲ��ŵ��������б������
	*/
	private int lastSongIndex;
	/**
	 * ��¼���ֲ������˳�ʱ�Ĳ���ģʽ
	 */
	private int musicplayer_mode;
	/**
	 * ��¼���ֲ�����������id
	 */
	private int musicplayer_bgId;
	/*
	 * ��ʵ���԰�����������������������
	 */
	private List<SongInfo> local_music_list;
	private List<VideoInfo> local_video_list;
	private int local_music_num;
	private int local_video_num;
	private int new_num;
	private int old_num;     
	
	@SuppressLint({ "WorldReadableFiles", "WorldWriteableFiles" })
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		music_class_list = new ArrayList<MediaList<SongInfo>>();			
		video_class_list = new ArrayList<MediaList<VideoInfo>>();
		local_music_list = new ArrayList<SongInfo>();
		local_video_list = new ArrayList<VideoInfo>();
		local_music_num = 0;
		local_video_num = 0;
		new_num = 0;
		old_num = 0;
		video_currtime = 0;
		video_isplay = true;
		@SuppressWarnings("deprecation")
		SharedPreferences sp = this.getApplicationContext().getSharedPreferences
				("playerStateData", Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
		lastSongIndex = sp.getInt("lastSongIndex", 0);
		lastVideoIndex = sp.getInt("lastVideoIndex", 0);
		musicplayer_mode = sp.getInt("musicplayer_mode", 0);
		musicplayer_bgId = sp.getInt("musicplayer_bgId", R.drawable.bg2);
		MyDataBase mydatabase = new MyDataBase();
		
		/*
		 * ���Ӽ�������Ĭ�ϴ������
		 */
		//////////////////////////////////////////////////
		
		music_class_list.add(new MediaList<SongInfo>
		("��������", 0,new ArrayList<SongInfo>()));
		music_class_list.add(new MediaList<SongInfo>
		("�ҵ�����", 0,new ArrayList<SongInfo>()));
		music_class_list.add(new MediaList<SongInfo>
		("Ĭ���б�", 0,new ArrayList<SongInfo>()));
		music_class_list.add(new MediaList<SongInfo>
		("�������", 0,new ArrayList<SongInfo>()));
		music_class_list.add(new MediaList<SongInfo>
		("�ҵ��", 0,new ArrayList<SongInfo>()));
		music_class_list.add(new MediaList<SongInfo>
		("�½��б�", 0,new ArrayList<SongInfo>()));
		mydatabase.getMusicData(this);
		if(music_class_list.get(0).getList().size()==0)
		{
			//�����������Ϊ��
			Log.i("��", "ˢ��");
			this.refreshLocalMusicInfo(getApplicationContext());
			//ˢ�±�������
			this.addToMusicListByListName(new MediaList<SongInfo>("��������", 0, local_music_list));
		}
		//////////////////////////////////////////////////
		video_class_list.add(new MediaList<VideoInfo>
		("������Ƶ", 0,new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("��ý�����",0,new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("�ҵ�����", 0,new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("Ĭ���б�", 0, new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("����ۿ�", 0,new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("�Ƽ��ۿ�", 0,new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("�½��б�", 0,new ArrayList<VideoInfo>()));
		//////////////////////////////////////////////////
		mydatabase.getVideoData(this);
		if(video_class_list.get(0).getList().size()==0)
		{
			//�����������Ϊ��
			Log.i("��", "ˢ��");
			this.refreshLocalVideoInfo(getApplicationContext());
			//ˢ�±�������
			this.addToVideoListByListName(new MediaList<VideoInfo>("������Ƶ", 0, local_video_list));
		}
		if(video_class_list.get(1).getList().size()==0)
		{
			VideoInfo v= new VideoInfo(); 
			v.setDisplayName("��ý��1");
			//"rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp"
			v.setPath("http://192.168.42.142/Streamer/1.mp3");
			video_class_list.get(1).getList().add(v);
			VideoInfo v1 = new VideoInfo();
			v1.setDisplayName("��ý��2");
			//rtsp://realdemo.rbn.com/demos/helixdemo/mynameishelix.mp4
			v1.setPath("http://192.168.42.142/Streamer/�����һ��.mp4");
			video_class_list.get(1).getList().add(v1);
		}
	}
	
	
	public void setMusicPlayer_bgID(int id)
	{
		this.musicplayer_bgId = id;
	}
	
	public int getMusicPlayer_bgID()
	{
		return this.musicplayer_bgId;
	}
	
	public void setMusicPlayerMode(int mode)
	{
		this.musicplayer_mode = mode;
	}
	
	public int getMusicPlayerMode()
	{
		return this.musicplayer_mode;
	}
	/**
	 * 
	 * @param currtime
	 */
	public void setVideoCurrTime(int currtime)
	{
		this.video_currtime = currtime;
	}
	
	public int getVideoCurrTime()
	{
		return this.video_currtime;
	}
	
	public void setVideoState(boolean isplay)
	{
		this.video_isplay = isplay;
	}
	
	public boolean getVideo_isplay()
	{
		return this.video_isplay;
	}
	
	public void setLastVideoIndex(int index)
	{
		this.lastVideoIndex = index;
	}
	
	public int getLastVideoIndex()
	{
		return this.lastVideoIndex;
	}
	
	public void setLastSongIndex(int index)
	{
		this.lastSongIndex = index;
	}
	
	public int getLastSongIndex()
	{
		return this.lastSongIndex;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////�����б��������/////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * ͨ�������б������籾�����֣�����ȡ�����б����ص�һ��ƥ����б��һ�����б�
	 * @author ������
	 * @param ListName �����б������
	 * @return �����б����б�
	 * @see {@link MyApplication#getMusicListByListName(String)}
	 */
	public MediaList<SongInfo> getMusicListByListName(String ListName)
	{
		/*
		 * �������������б����б�����ΪListName ���б��أ�
		 * ���򷵻�һ�����б�
		 */
		for(int i = 0 ; i<music_class_list.size() ; i++)
		{
			if(music_class_list.get(i).getListName().equals(ListName))
			{
				/*
				 * ����ָ���б�
				 */
				return music_class_list.get(i);				
			}
		}
		/*
		 * �Ҳ������򷵻�һ�����б�
		 */
		return new MediaList<SongInfo>();
	}
	
	/**
	 * ͨ�������б����������ȡ�����б����ص�һ��ƥ����б���һ�����б�
	 * @author ������
	 * @param index �����б������ֵ
	 * @return �����б�
	 * @see {@link MyApplication#getMusicListByIndex(int)}
	 */
	public MediaList<SongInfo> getMusicListByIndex(int index)
	{
		/*
		 * �������ֵ���ڷ�����Ӧ�б����򷵻�һ�����б�
		 */
		if( index>= music_class_list.size()||index<0)
		{
			/*
			 * ����������,����һ�����б�
			 */
			return new MediaList<SongInfo>();
		}
		else
		{
			return music_class_list.get(index);
		}
	}

	/**
	 * �����µ������б��������б��Ѿ����������ʧ�ܡ�
	 * @author ������
	 * @param Medialist ���½��������б�
	 * @return ��ӳɹ�����true�����򷵻�false
	 * @see {@link MyApplication#newMusicListByListName(MediaList)}
	 */
	public boolean newMusicListByListName(MediaList<SongInfo> list)
	{
		/*
		 * ͨ����ȡ�������ж��б��Ƿ��Ѵ���
		 */
		int index = getMusicListIndexByListName(list.getListName());
		if( index == -1)
		{
			/*
			 * δ���ڸ��б�,�����λ�ü�����б�
			 */
			music_class_list.add(music_class_list.size()-1,list);
			return true;
		}
		else
		{
			/*
			 * �б��Ѵ��ڣ��½�ʧ�ܡ�
			 */
			return false;
		}
		
	}
	
	/**
	 * ͨ�������б�����ֻ�ȡ��һ��ƥ����б���������ҵ�������Ӧ����ֵ�����򷵻�-1��
	 * @author ������
	 * @param ListName �����б������
	 * @return �����б�����ֵ��-1
	 * @see {@link MyApplication#getMusicClassIndexByListName(String)}
	 */
	public int getMusicListIndexByListName(String ListName)
	{
		/*
		 * �������������б����ص�һ������ƥ��������б�����������򷵻�-1
		 */
		for(int i=0 ; i<music_class_list.size();i++)
		{
			if(music_class_list.get(i).getListName().equals(ListName))
			{
				//�ҵ�������������ֵ
				return i;
			}
		}
		//δ�ҵ�������-1
		return -1;
	}
	
	/**
	 * ͨ�����ֵ����������������б����ͬ�����б��У������ͱ�����ڣ��������ʧ�ܣ�������ӵ��ҵ����
	 * @author ������
	 * @param  list �����б�
	 * @return ��ӳɹ�����true�����򷵻�false
	 * @see {@link MyApplication#addToMusicListByListName(MediaList)}
	 */
 	public boolean addToMusicListByListName(MediaList<SongInfo> list)
	{
 		/*
 		 * �ж������б��Ƿ���ڡ��������б����ʱ����ԭ���б�����ݳ��������һ����ʱ����temp,
 		 * ���������б���б�����ȥ���������ԭλ����Ӹ������б�
 		 */
 		int index = getMusicListIndexByListName(list.getListName());
		if( index != -1)
		{
			MediaList<SongInfo> temp = music_class_list.get(index);
			//����������ӵ������б�
			temp.addAll(list.getList());
			//�Ƴ�ԭ���ݣ����Բ���Ҫ�����
			music_class_list.remove(index);
			/*
			 *���listִ��list.add("thank you !",20);������������������
			 *���ж�����Ϊ20��Ԫ���Ƿ����
			 *1��������ڵĻ��������ȰѴ�20��ʼ��ֱ����������ȫ����Ų��Ȼ������ֵ������ǰ20λ���ϵ�ֵ
			 *2����������ڵĻ�����ֱ�ӷ���Ŀǰ���һ��Ԫ�غ��� 
			 */
			music_class_list.add(index, temp);
			return true;
		}
		else
		{
			return false;
		}
	}
	
 	/**
 	 * ��һ�����ּ��뵽�б���ΪListName�������б��С��������б�����ʱ���ʧ�ܡ�
 	 * @author ������
 	 * @param ListName ������������б���
 	 * @param song ������ĸ���
 	 * @return ��ӳɹ�����true,���򷵻�false.
 	 * @see {@link MyApplication#addToMusicListByListName(String, SongInfo)}
 	 */ 	
 	public boolean addToMusicListByListName(String ListName, SongInfo song)
 	{
 		int index = getMusicListIndexByListName(ListName);
 		if(index != -1)
 		{
 			//ȡ��ԭ�����б�
 			MediaList<SongInfo> temp = music_class_list.get(index);
 			//���б�����ȥ���������б�
 			music_class_list.remove(index);
 			//������Ƶ���������б�
 			temp.add(song);
 			//�������б����¼����б���
 			music_class_list.add(index, temp);
 			return true;
 		}
 		else
 		{
 			//�����ڸ������б�
 			return false;
 		}
 	}
 	
 	/**
 	 * ��ȡ�����б���ΪListName����������������������б����ڣ�����-1
 	 * @author ������
 	 * @param ListName �����б���
 	 * @return �б���ڷ�������������������������߷���-1
 	 * @see {@link MyApplication#getMusicListAlterNumByListName(String)}
 	 */
 	public int getMusicListAlterNumByListName(String ListName)
 	{
 		if(getMusicListIndexByListName(ListName) != -1)
 		{
 			/*
 			 * �����б����
 			 */
 			return getVideoListByListName(ListName).getAlterNum();
 		}
 		return -1;
 	}  

 	/**
 	 * ��ȡ�����б���
 	 * @author ������
 	 * @return ���е������б��������б���
 	 * @see {@link MyApplication#getAllMusicList()}
 	 */
 	public List<MediaList<SongInfo>> getAllMusicList()
 	{
 		return music_class_list;
 	}
 	
 	/**
 	 * ���������б��ϡ�������ʹ�á�
 	 * @param music_class_list �µ������б���
 	 * @return ���óɹ�����true
 	 * @see {@link MyApplication#resetAllMusicList(List)}
 	 */
 	public boolean resetAllMusicList(List<MediaList<SongInfo>> music_class_list)
 	{
 		this.music_class_list = music_class_list;
 		return true;
 	}
 	///////////////////////////////////////////////////////////////////////////////////
 	//////////////////////////////////��Ƶ�б��������/////////////////////////////////
 	///////////////////////////////////////////////////////////////////////////////////
 	
 	/**
	 * ͨ����Ƶ�б������籾����Ƶ������ȡ��Ƶ�б����ص�һ��ƥ����б��һ�����б�
	 * @author ������
	 * @param ListName ��Ƶ�б������
	 * @return ��Ƶ�б����б�
	 * @see {@link MyApplication#getVideoListByListName(String)}
	 */
  	public MediaList<VideoInfo> getVideoListByListName(String ListName)
	{
		/*
		 * ����������Ƶ�б���
		 */
		for(int i = 0 ; i<video_class_list.size() ; i++)
		{
			if(video_class_list.get(i).getListName().equals(ListName))
			{
				/*
				 * ����ָ���б�
				 */
				return video_class_list.get(i);				
			}
		}
		/*
		 * �Ҳ������򷵻�һ�����б�
		 */
		return new MediaList<VideoInfo>();
	}
 	/**
 	 * ͨ����Ƶ�б����������ȡ��Ƶ�б����ص�һ��ƥ����б���һ�����б�
	 * @author ������
	 * @param index ��Ƶ�б������ֵ
	 * @return ��Ƶ�б�
	 * @see {@link MyApplication#getVideoListByIndex(int)}
	 */
	public MediaList<VideoInfo> getVideoListByIndex(int index)
	{
		/*
		 * �������ֵ���ڷ�����Ӧ�б����򷵻�һ�����б�
		 */
		if( index>= video_class_list.size()||index<0)
		{
			/*
			 * ����������,����һ�����б�
			 */
			return new MediaList<VideoInfo>();
		}
		else
		{
			return video_class_list.get(index);
		}
	}

	/**
	 * �����µ���Ƶ�б��������б��Ѿ����������ʧ�ܡ�
	 * @author ������
	 * @param Medialist ���½�����Ƶ�б�
	 * @return ��ӳɹ�����true�����򷵻�false
	 * @see {@link MyApplication#newVideoListByListName(MediaList)}
	 */
	public boolean newVideoListByListName(MediaList<VideoInfo> list)
	{
		/*
		 * ͨ����ȡ�������ж��б��Ƿ��Ѵ���
		 */
		int index = getVideoListIndexByListName(list.getListName());
		if( index == -1)
		{
			/*
			 * δ���ڸ��б�
			 */
			video_class_list.add(video_class_list.size()-1,list);
			return true;
		}
		else
		{
			/*
			 * �б��Ѵ��ڣ����ʧ�ܡ�
			 */
			return false;
		}
		
	}
	
	/**
	 * ͨ����Ƶ�б�����ֻ�ȡ��һ��ƥ����б���������ҵ�������Ӧ����ֵ�����򷵻�-1��
	 * @author ������
	 * @param ListName ��Ƶ�б������
	 * @return ��Ƶ�б�����ֵ��-1
	 * @see {@link MyApplication#getVideoClassIndexByListName(String)}
	 */
	public int getVideoListIndexByListName(String ListName)
	{
		/*
		 * ����������Ƶ�б����ص�һ������ƥ�����Ƶ�б�����������򷵻�-1
		 */
		for(int i=0 ; i<video_class_list.size();i++)
		{
			if(video_class_list.get(i).getListName().equals(ListName))
			{
				//�ҵ�������������ֵ
				return i;
			}
		}
		//δ�ҵ�������-1
		return -1;
	}
	
	/**
	 * ͨ����Ƶ��������������Ƶ�б����ͬ�����б��У������ͱ�����ڣ��������ʧ�ܣ�������ӵ��ҵ����
	 * @author ������
	 * @param ListName ��Ƶ�б�����ƣ����ҵ��
	 * @param list ����ӵ���Ƶ�б�����
	 * @return ��ӳɹ�����true�����򷵻�false
	 * @see {@link MyApplication#addToVideoListByListName(String, List)}
	 */
 	public boolean addToVideoListByListName(MediaList<VideoInfo> list)
	{
 		/*
 		 * �ж���Ƶ�б��Ƿ���ڡ�����Ƶ�б����ʱ����ԭ���б�����ݳ��������һ����ʱ����temp,
 		 * ������Ƶ�б���б�����ȥ���������ԭλ����Ӹ���Ƶ�б�
 		 */
 		int index = getVideoListIndexByListName(list.getListName());
		if( index != -1)
		{
			MediaList<VideoInfo> temp = video_class_list.get(index);
			//����������ӵ���Ƶ�б�
			temp.addAll(list.getList());
			//�Ƴ�ԭ���ݣ����Բ���Ҫ�����
			video_class_list.remove(index);
			/*
			 *���listִ��list.add("thank you !",20);������������������
			 *���ж�����Ϊ20��Ԫ���Ƿ����
			 *1��������ڵĻ��������ȰѴ�20��ʼ��ֱ����������ȫ����Ų��Ȼ������ֵ������ǰ20λ���ϵ�ֵ
			 *2����������ڵĻ�����ֱ�ӷ���Ŀǰ���һ��Ԫ�غ��� 
			 */
			video_class_list.add(index, temp);
			return true;
		}
		else
		{
			return false;
		}
	}
	
 	/**
 	 * ��һ����Ƶ���뵽�б���ΪListName����Ƶ�б��С�����Ƶ�б�����ʱ���ʧ�ܡ�
 	 * @author ������
 	 * @param ListName ���������Ƶ�б���
 	 * @param song ���������Ƶ
 	 * @return ��ӳɹ�����true,���򷵻�false.
 	 * @see {@link MyApplication#addToVideoListByListName(String, SongInfo)}
 	 */ 	
 	public boolean addToVideoListByListName(String ListName, VideoInfo video)
 	{
 		int index = getVideoListIndexByListName(ListName);
 		if(index != -1)
 		{
 			//ȡ��ԭ��Ƶ�б�
 			MediaList<VideoInfo> temp = video_class_list.get(index);
 			//���б�����ȥ������Ƶ�б�
 			video_class_list.remove(index);
 			//���ø���������Ƶ�б�
 			temp.add(video);
 			//����Ƶ�б����¼����б���
 			video_class_list.add(index, temp);
 			return true;
 		}
 		else
 		{
 			//�����ڸ���Ƶ�б�
 			return false;
 		}
 	}
 	
 	/**
 	 * ��ȡ��Ƶ�б���ΪListName�����������Ƶ���������б����ڣ�����-1
 	 * @author ������
 	 * @param ListName ��Ƶ�б���
 	 * @return �б���ڷ����������������Ƶ�������߷���-1
 	 * @see {@link MyApplication#getVideoListAlterNumByListName(String)}
 	 */
 	public int getVideoListAlterNumByListName(String ListName)
 	{
 		if(getVideoListIndexByListName(ListName) != -1)
 		{
 			/*
 			 * ��Ƶ�б����
 			 */
 			return getVideoListByListName(ListName).getAlterNum();
 		}
 		return -1;
 	}  
 	/**
 	 * ��ȡ��Ƶ�б���
 	 * @author ������
 	 * @return ���е���Ƶ�б�����Ƶ�б���
 	 * @see {@link MyApplication#getAllMusicList()}
 	 */
 	public List<MediaList<VideoInfo>> getAllVideoList()
 	{
 		return video_class_list;
 	}
 	
 	/**
 	 * ������Ƶ�б��ϡ�������ʹ�á�
 	 * @param music_class_list �µ���Ƶ�б���
 	 * @return ���óɹ�����true
 	 * @see {@link MyApplication#resetAllMusicList(List)}
 	 */
 	public boolean resetAllVideoList(List<MediaList<VideoInfo>> video_class_list)
 	{
 		this.video_class_list = video_class_list;
 		return true;
 	}
 
 	public boolean refreshLocalMusicInfo(Context context)
	{
		
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			Cursor cursor = context.getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			
			if(cursor != null){
				
				new_num = 0;
				if(!local_music_list.isEmpty())
				{
					local_music_list.clear();
				}
				old_num = local_music_num;
				cursor.moveToFirst();
				for (int j = 0; j < cursor.getCount(); j++) {
					String mFileName = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media._ID));
					String mFileTitle = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
					String mDuration = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.DURATION));
					String mSinger = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ARTIST));
					String mAlbum = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ALBUM));
					String mYear = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.YEAR));
					String mFileType = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
					String mFileSize = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.SIZE));
					String mFilePath = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.DATA));
					SongInfo song = new SongInfo(mFileName, mFileTitle, mDuration, mSinger,
							mAlbum, mYear, mFileType, mFileSize, mFilePath, "");
					local_music_list.add(song);
					Log.i("getLocalMusicInfo", "@@@"+local_music_num);
					cursor.moveToNext();
				}
				local_music_num = cursor.getCount();
				new_num = local_music_num - old_num;	
			}
			Log.i("getLocalMusicInfo", "@@@"+local_music_num+"num"+local_music_list.size());
			cursor.close();
			return true;
		}
		else
		{
			return false;
		}
	}	

	public boolean refreshLocalVideoInfo(Context context)
	{
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			Cursor cursor = context.getContentResolver().query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
					MediaStore.Video.Media.DEFAULT_SORT_ORDER);

			if(cursor != null)
			{
				if(!local_video_list.isEmpty())
				{
					local_video_list.clear();
				}
				old_num = local_video_num;
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					int id = cursor.getInt(cursor
							.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
					String title = cursor.getString(cursor
							.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
					String album = cursor.getString(cursor
							.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
					String artist = cursor.getString(cursor
							.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
					String displayName = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
					String mimeType = cursor.getString(cursor
							.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
					String path = cursor.getString(cursor
							.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
					long size = cursor.getLong(cursor
							.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
					long duration = cursor.getInt(cursor
							.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
					VideoInfo v = new VideoInfo(id, title, album, artist, displayName,
							mimeType, path, size, duration);
					local_video_list.add(v);
					cursor.moveToNext();
				}
				local_video_num = cursor.getCount();
				new_num = local_video_num - old_num;
			}
			cursor.close();
			return true;
		}
		else
		{
			return false;
		}
	}

	public List<SongInfo> getLocalMusicInfo()
	{
		//Log.i("getLocalMusicInfo", "@@@"+local_music_num);
		return local_music_list;
	}

	public List<VideoInfo> getLocalVideoInfo()
	{
		return local_video_list;
	}

	public int getLocalMusicNum()
	{
		return local_music_num;
	}

	public int getLocalVideoNum()
	{
		return local_video_num;
	}

	public int getNewAddNum()
	{
		return new_num;
	}
	
	public boolean SavaData()
	{
		
		SharedPreferences sp = getApplicationContext().getSharedPreferences
				("playerStateData", Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor edt = sp.edit();
		/*
		 * �������һ�׸衢��Ƶ������
		 */
		edt.putInt("lastSongIndex",lastSongIndex);
		edt.putInt("lastVideoIndex",lastVideoIndex);
		edt.putInt("musicplayer_bgId", musicplayer_bgId);
		/*
		 * �������ֲ�������ģʽ
		 */
		edt.putInt("musicplayer_mode", musicplayer_mode);
		edt.commit();
		
		return true;
	}
	
}