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
 * 定义我的Application,用于存放整个应用用到的数据。
 * @author 吴香礼
 * @see MyApplication
 */
public class MyApplication extends Application{

	/**
	 *音乐列表集合， 用于存储音乐列表，每一项包含该音乐列表的名称和该列表的成员和最近更改的媒体数。
	 * 每一项为{@link MediaList}的成员即一个列表。
	 */
	private List<MediaList<SongInfo>> music_class_list;  
	/**
	 * 视频列表集合，用于存储视频列表，每一项包含该视频列表的名称和该列表的成员和最近更改的媒体数。
	 * 每一项为{@link MediaList}的成员即一个列表。
	 */
	private List<MediaList<VideoInfo>> video_class_list;	
	/**
	 * 记录播放器在全屏与正常播放切换时的数据
	 */
	private int video_currtime;
	private boolean video_isplay;
	/**
	 * 退出视频播放器时正在播放的视频在列表的索引
	 */
	private int lastVideoIndex;
	/**
	*退出音乐播放器时正在播放的音乐在列表的索引
	*/
	private int lastSongIndex;
	/**
	 * 记录音乐播放器退出时的播放模式
	 */
	private int musicplayer_mode;
	/**
	 * 记录音乐播放器背景的id
	 */
	private int musicplayer_bgId;
	/*
	 * 其实可以把下面两个归入上面的类表中
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
		 * 增加几个程序默认大类类表
		 */
		//////////////////////////////////////////////////
		
		music_class_list.add(new MediaList<SongInfo>
		("本地音乐", 0,new ArrayList<SongInfo>()));
		music_class_list.add(new MediaList<SongInfo>
		("我的下载", 0,new ArrayList<SongInfo>()));
		music_class_list.add(new MediaList<SongInfo>
		("默认列表", 0,new ArrayList<SongInfo>()));
		music_class_list.add(new MediaList<SongInfo>
		("最近播放", 0,new ArrayList<SongInfo>()));
		music_class_list.add(new MediaList<SongInfo>
		("我的最爱", 0,new ArrayList<SongInfo>()));
		music_class_list.add(new MediaList<SongInfo>
		("新建列表", 0,new ArrayList<SongInfo>()));
		mydatabase.getMusicData(this);
		if(music_class_list.get(0).getList().size()==0)
		{
			//如果本地音乐为空
			Log.i("空", "刷新");
			this.refreshLocalMusicInfo(getApplicationContext());
			//刷新本地曲库
			this.addToMusicListByListName(new MediaList<SongInfo>("本地音乐", 0, local_music_list));
		}
		//////////////////////////////////////////////////
		video_class_list.add(new MediaList<VideoInfo>
		("本地视频", 0,new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("流媒体测试",0,new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("我的下载", 0,new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("默认列表", 0, new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("最近观看", 0,new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("推荐观看", 0,new ArrayList<VideoInfo>()));
		video_class_list.add(new MediaList<VideoInfo>
		("新建列表", 0,new ArrayList<VideoInfo>()));
		//////////////////////////////////////////////////
		mydatabase.getVideoData(this);
		if(video_class_list.get(0).getList().size()==0)
		{
			//如果本地音乐为空
			Log.i("空", "刷新");
			this.refreshLocalVideoInfo(getApplicationContext());
			//刷新本地曲库
			this.addToVideoListByListName(new MediaList<VideoInfo>("本地视频", 0, local_video_list));
		}
		if(video_class_list.get(1).getList().size()==0)
		{
			VideoInfo v= new VideoInfo(); 
			v.setDisplayName("流媒体1");
			//"rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp"
			v.setPath("http://192.168.42.142/Streamer/1.mp3");
			video_class_list.get(1).getList().add(v);
			VideoInfo v1 = new VideoInfo();
			v1.setDisplayName("流媒体2");
			//rtsp://realdemo.rbn.com/demos/helixdemo/mynameishelix.mp4
			v1.setPath("http://192.168.42.142/Streamer/做你的一半.mp4");
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
	//////////////////////////////////音乐列表操做函数/////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 通过音乐列表名（如本地音乐）来获取音乐列表，返回第一个匹配的列表或一个空列表。
	 * @author 吴香礼
	 * @param ListName 音乐列表的名称
	 * @return 音乐列表或空列表
	 * @see {@link MyApplication#getMusicListByListName(String)}
	 */
	public MediaList<SongInfo> getMusicListByListName(String ListName)
	{
		/*
		 * 遍历所有音乐列表，将列表名字为ListName 的列表返回，
		 * 否则返回一个空列表。
		 */
		for(int i = 0 ; i<music_class_list.size() ; i++)
		{
			if(music_class_list.get(i).getListName().equals(ListName))
			{
				/*
				 * 返回指定列表
				 */
				return music_class_list.get(i);				
			}
		}
		/*
		 * 找不到，则返回一个空列表
		 */
		return new MediaList<SongInfo>();
	}
	
	/**
	 * 通过音乐列表的索引来获取音乐列表，返回第一个匹配的列表，或一个空列表。
	 * @author 吴香礼
	 * @param index 音乐列表的索引值
	 * @return 音乐列表
	 * @see {@link MyApplication#getMusicListByIndex(int)}
	 */
	public MediaList<SongInfo> getMusicListByIndex(int index)
	{
		/*
		 * 如果索引值存在返回相应列表，否则返回一个空列表。
		 */
		if( index>= music_class_list.size()||index<0)
		{
			/*
			 * 索引不存在,返回一个空列表
			 */
			return new MediaList<SongInfo>();
		}
		else
		{
			return music_class_list.get(index);
		}
	}

	/**
	 * 增加新的音乐列表，如若该列表已经存在则添加失败。
	 * @author 吴香礼
	 * @param Medialist 欲新建的音乐列表
	 * @return 添加成功返回true，否则返回false
	 * @see {@link MyApplication#newMusicListByListName(MediaList)}
	 */
	public boolean newMusicListByListName(MediaList<SongInfo> list)
	{
		/*
		 * 通过获取索引，判断列表是否已存在
		 */
		int index = getMusicListIndexByListName(list.getListName());
		if( index == -1)
		{
			/*
			 * 未存在该列表,在最后位置加入该列表
			 */
			music_class_list.add(music_class_list.size()-1,list);
			return true;
		}
		else
		{
			/*
			 * 列表已存在，新建失败。
			 */
			return false;
		}
		
	}
	
	/**
	 * 通过音乐列表的名字获取第一个匹配的列表的索引，找到返回相应索引值，否则返回-1。
	 * @author 吴香礼
	 * @param ListName 音乐列表的名字
	 * @return 音乐列表索引值或-1
	 * @see {@link MyApplication#getMusicClassIndexByListName(String)}
	 */
	public int getMusicListIndexByListName(String ListName)
	{
		/*
		 * 遍历所有音乐列表，返回第一个名称匹配的音乐列表的索引，否则返回-1
		 */
		for(int i=0 ; i<music_class_list.size();i++)
		{
			if(music_class_list.get(i).getListName().equals(ListName))
			{
				//找到索引返回索引值
				return i;
			}
		}
		//未找到，返回-1
		return -1;
	}
	
	/**
	 * 通过音乐的类型名将该音乐列表添加同名的列表中（该类型必须存在，否则存入失败），如添加到我的最爱。
	 * @author 吴香礼
	 * @param  list 音乐列表
	 * @return 添加成功返回true，否则返回false
	 * @see {@link MyApplication#addToMusicListByListName(MediaList)}
	 */
 	public boolean addToMusicListByListName(MediaList<SongInfo> list)
	{
 		/*
 		 * 判断音乐列表是否存在。当音乐列表存在时，将原来列表的内容抽出，存于一个临时变量temp,
 		 * 将该音乐列表从列表集合中去除。最后在原位置添加该音乐列表。
 		 */
 		int index = getMusicListIndexByListName(list.getListName());
		if( index != -1)
		{
			MediaList<SongInfo> temp = music_class_list.get(index);
			//将新内容添加到音乐列表
			temp.addAll(list.getList());
			//移除原内容，可以不需要此语句
			music_class_list.remove(index);
			/*
			 *你对list执行list.add("thank you !",20);操作，它会这样处理：
			 *先判断索引为20的元素是否存在
			 *1、如果存在的话，它会先把从20开始，直到最后的数据全往后挪，然后用新值代替以前20位置上的值
			 *2、如果不存在的话，就直接放在目前最后一个元素后面 
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
 	 * 将一首音乐加入到列表名为ListName的音乐列表中。该音乐列表不存在时添加失败。
 	 * @author 吴香礼
 	 * @param ListName 欲加入的音乐列表名
 	 * @param song 欲加入的歌曲
 	 * @return 添加成功返回true,否则返回false.
 	 * @see {@link MyApplication#addToMusicListByListName(String, SongInfo)}
 	 */ 	
 	public boolean addToMusicListByListName(String ListName, SongInfo song)
 	{
 		int index = getMusicListIndexByListName(ListName);
 		if(index != -1)
 		{
 			//取出原音乐列表
 			MediaList<SongInfo> temp = music_class_list.get(index);
 			//在列表集合中去除该音乐列表
 			music_class_list.remove(index);
 			//将该视频加入音乐列表
 			temp.add(song);
 			//将音乐列表重新加入列表集合
 			music_class_list.add(index, temp);
 			return true;
 		}
 		else
 		{
 			//不存在该音乐列表
 			return false;
 		}
 	}
 	
 	/**
 	 * 获取音乐列表名为ListName的最近更新音乐数，若该列表不存在，返回-1
 	 * @author 吴香礼
 	 * @param ListName 音乐列表名
 	 * @return 列表存在返回最难最近更新音乐数，否者返回-1
 	 * @see {@link MyApplication#getMusicListAlterNumByListName(String)}
 	 */
 	public int getMusicListAlterNumByListName(String ListName)
 	{
 		if(getMusicListIndexByListName(ListName) != -1)
 		{
 			/*
 			 * 音乐列表存在
 			 */
 			return getVideoListByListName(ListName).getAlterNum();
 		}
 		return -1;
 	}  

 	/**
 	 * 获取音乐列表集合
 	 * @author 吴香礼
 	 * @return 所有的音乐列表，即音乐列表集合
 	 * @see {@link MyApplication#getAllMusicList()}
 	 */
 	public List<MediaList<SongInfo>> getAllMusicList()
 	{
 		return music_class_list;
 	}
 	
 	/**
 	 * 重设音乐列表集合。不建议使用。
 	 * @param music_class_list 新的音乐列表集合
 	 * @return 设置成功返回true
 	 * @see {@link MyApplication#resetAllMusicList(List)}
 	 */
 	public boolean resetAllMusicList(List<MediaList<SongInfo>> music_class_list)
 	{
 		this.music_class_list = music_class_list;
 		return true;
 	}
 	///////////////////////////////////////////////////////////////////////////////////
 	//////////////////////////////////视频列表操做函数/////////////////////////////////
 	///////////////////////////////////////////////////////////////////////////////////
 	
 	/**
	 * 通过视频列表名（如本地视频）来获取视频列表，返回第一个匹配的列表或一个空列表。
	 * @author 吴香礼
	 * @param ListName 视频列表的名称
	 * @return 视频列表或空列表
	 * @see {@link MyApplication#getVideoListByListName(String)}
	 */
  	public MediaList<VideoInfo> getVideoListByListName(String ListName)
	{
		/*
		 * 遍历整个视频列表集合
		 */
		for(int i = 0 ; i<video_class_list.size() ; i++)
		{
			if(video_class_list.get(i).getListName().equals(ListName))
			{
				/*
				 * 返回指定列表
				 */
				return video_class_list.get(i);				
			}
		}
		/*
		 * 找不到，则返回一个空列表
		 */
		return new MediaList<VideoInfo>();
	}
 	/**
 	 * 通过视频列表的索引来获取视频列表，返回第一个匹配的列表，或一个空列表。
	 * @author 吴香礼
	 * @param index 视频列表的索引值
	 * @return 视频列表
	 * @see {@link MyApplication#getVideoListByIndex(int)}
	 */
	public MediaList<VideoInfo> getVideoListByIndex(int index)
	{
		/*
		 * 如果索引值存在返回相应列表，否则返回一个空列表。
		 */
		if( index>= video_class_list.size()||index<0)
		{
			/*
			 * 索引不存在,返回一个空列表
			 */
			return new MediaList<VideoInfo>();
		}
		else
		{
			return video_class_list.get(index);
		}
	}

	/**
	 * 增加新的视频列表，如若该列表已经存在则添加失败。
	 * @author 吴香礼
	 * @param Medialist 欲新建的视频列表
	 * @return 添加成功返回true，否则返回false
	 * @see {@link MyApplication#newVideoListByListName(MediaList)}
	 */
	public boolean newVideoListByListName(MediaList<VideoInfo> list)
	{
		/*
		 * 通过获取索引，判断列表是否已存在
		 */
		int index = getVideoListIndexByListName(list.getListName());
		if( index == -1)
		{
			/*
			 * 未存在该列表
			 */
			video_class_list.add(video_class_list.size()-1,list);
			return true;
		}
		else
		{
			/*
			 * 列表已存在，添加失败。
			 */
			return false;
		}
		
	}
	
	/**
	 * 通过视频列表的名字获取第一个匹配的列表的索引，找到返回相应索引值，否则返回-1。
	 * @author 吴香礼
	 * @param ListName 视频列表的名字
	 * @return 视频列表索引值或-1
	 * @see {@link MyApplication#getVideoClassIndexByListName(String)}
	 */
	public int getVideoListIndexByListName(String ListName)
	{
		/*
		 * 遍历所有视频列表，返回第一个名称匹配的视频列表的索引，否则返回-1
		 */
		for(int i=0 ; i<video_class_list.size();i++)
		{
			if(video_class_list.get(i).getListName().equals(ListName))
			{
				//找到索引返回索引值
				return i;
			}
		}
		//未找到，返回-1
		return -1;
	}
	
	/**
	 * 通过视频的类型名将该视频列表添加同名的列表中（该类型必须存在，否则存入失败），如添加到我的最爱。
	 * @author 吴香礼
	 * @param ListName 视频列表的名称，如我的最爱
	 * @param list 新添加的视频列表数据
	 * @return 添加成功返回true，否则返回false
	 * @see {@link MyApplication#addToVideoListByListName(String, List)}
	 */
 	public boolean addToVideoListByListName(MediaList<VideoInfo> list)
	{
 		/*
 		 * 判断视频列表是否存在。当视频列表存在时，将原来列表的内容抽出，存于一个临时变量temp,
 		 * 将该视频列表从列表集合中去除。最后在原位置添加该视频列表。
 		 */
 		int index = getVideoListIndexByListName(list.getListName());
		if( index != -1)
		{
			MediaList<VideoInfo> temp = video_class_list.get(index);
			//将新内容添加到视频列表
			temp.addAll(list.getList());
			//移除原内容，可以不需要此语句
			video_class_list.remove(index);
			/*
			 *你对list执行list.add("thank you !",20);操作，它会这样处理：
			 *先判断索引为20的元素是否存在
			 *1、如果存在的话，它会先把从20开始，直到最后的数据全往后挪，然后用新值代替以前20位置上的值
			 *2、如果不存在的话，就直接放在目前最后一个元素后面 
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
 	 * 将一首视频加入到列表名为ListName的视频列表中。该视频列表不存在时添加失败。
 	 * @author 吴香礼
 	 * @param ListName 欲加入的视频列表名
 	 * @param song 欲加入的视频
 	 * @return 添加成功返回true,否则返回false.
 	 * @see {@link MyApplication#addToVideoListByListName(String, SongInfo)}
 	 */ 	
 	public boolean addToVideoListByListName(String ListName, VideoInfo video)
 	{
 		int index = getVideoListIndexByListName(ListName);
 		if(index != -1)
 		{
 			//取出原视频列表
 			MediaList<VideoInfo> temp = video_class_list.get(index);
 			//在列表集合中去除该视频列表
 			video_class_list.remove(index);
 			//将该歌曲加入视频列表
 			temp.add(video);
 			//将视频列表重新加入列表集合
 			video_class_list.add(index, temp);
 			return true;
 		}
 		else
 		{
 			//不存在该视频列表
 			return false;
 		}
 	}
 	
 	/**
 	 * 获取视频列表名为ListName的最近更新视频数，若该列表不存在，返回-1
 	 * @author 吴香礼
 	 * @param ListName 视频列表名
 	 * @return 列表存在返回最难最近更新视频数，否者返回-1
 	 * @see {@link MyApplication#getVideoListAlterNumByListName(String)}
 	 */
 	public int getVideoListAlterNumByListName(String ListName)
 	{
 		if(getVideoListIndexByListName(ListName) != -1)
 		{
 			/*
 			 * 视频列表存在
 			 */
 			return getVideoListByListName(ListName).getAlterNum();
 		}
 		return -1;
 	}  
 	/**
 	 * 获取视频列表集合
 	 * @author 吴香礼
 	 * @return 所有的视频列表，即视频列表集合
 	 * @see {@link MyApplication#getAllMusicList()}
 	 */
 	public List<MediaList<VideoInfo>> getAllVideoList()
 	{
 		return video_class_list;
 	}
 	
 	/**
 	 * 重设视频列表集合。不建议使用。
 	 * @param music_class_list 新的视频列表集合
 	 * @return 设置成功返回true
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
		 * 保存最后一首歌、视频的索引
		 */
		edt.putInt("lastSongIndex",lastSongIndex);
		edt.putInt("lastVideoIndex",lastVideoIndex);
		edt.putInt("musicplayer_bgId", musicplayer_bgId);
		/*
		 * 保存音乐播放器的模式
		 */
		edt.putInt("musicplayer_mode", musicplayer_mode);
		edt.commit();
		
		return true;
	}
	
}