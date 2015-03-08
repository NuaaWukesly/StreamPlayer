package com.example.mydatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.dataclass.MediaList;
import com.example.dataclass.SongInfo;
import com.example.dataclass.VideoInfo;
import com.example.myapplication.MyApplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * 用于对数据库的操作
 * @author 吴香礼
 * @see {@link MyDataBase}
 */
@SuppressLint("SdCardPath")
public class MyDataBase{
	
	/**
	 * 数据库对象
	 * @see {@link MyDataBase#mydb}
	 */
	private SQLiteDatabase mydb;
	/**
	 * 数据库所在文件夹路径
	 * @see {@link MyDataBase#path}
	 */
	File path = new File("/sdcard/StreamPlayer");
	/**
	 * 数据库路径
	 * @see {@link MyDataBase#file}
	 */
	File file = new File("/sdcard/StreamPlayer/data.db");
	
	/**
	 * 记录sd卡当前是否具有读写权限
	 * @see {@link MyDataBase#sdstate}
	 */
	boolean sdstate = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	
	/**
	 * 构造函数，创建或打开数据库对象
	 * @author 吴香礼
	 * @see {@link MyDataBase}
	 */
	public MyDataBase()
	{
		if(sdstate)
		{
			
			/*
			 * sd卡可读写时
			 */
			if(!path.exists())
			{
				/*
				 * 路劲不存在，新建路径
				 */
				path.mkdir();
			}
			if(!file.exists())
			{
				/*
				 * 数据库不存在,新建数据库文件
				 */
				try
				{
					if(file.createNewFile())
					{
						//新建数据库成功,打开或则创建数据库
						mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
						/*
						 * 如果不存表单在新建表单,有编号，歌曲信息及该歌曲所在的音乐列表名
						 */
						createMusicTab("musiclisttab");
						createVideoTab("videolisttab");
					}
				}catch(IOException e)
				{
					e.printStackTrace();
					Log.i("Sdcard", "sd卡不能存在！");
					/*
					 * 需要在此做一弹出窗口
					 */
				}
			}
			else
			{
				//存在数据库文件时，打开或则创建数据库，表单已存在
				mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
				/*
				 * 如果不存表单在新建表单,有编号，歌曲/视频信息及该歌曲、视频所在的音乐/视频列表名
				 */
				createMusicTab("musiclisttab");
				createVideoTab("videolisttab");
			}
			/*
			 * 关闭数据库
			 */
			mydb.close();
		}
		else
		{
			/*
			 * sd卡不可读写，数据库创建失败，需要一些提示
			 */
		}
		
	}
	
	/**
	 * 创建一个表名为tabName的存储音乐信息的表
	 * @author 吴香礼
	 * @param tabName 表名
	 */
	private void createMusicTab(String tabName)
	{
		mydb.execSQL("CREATE TABLE IF NOT EXISTS " +
				tabName +
				" (_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
				"filename  TEXT," +
				"filetitle  TEXT," +
				"duration  TEXT," +
				"singer  TEXT," +
				"album  TEXT," +
				"year  TEXT," +
				"filetype  TEXT," +
				"filesize  TEXT," +
				"filepath  TEXT," +
				"fileurl  TEXT," +
				"listname TEXT);");
	}
	
	/**
	 * 创建一个表名为tabName的存储视频信息的表
	 * @since 火炬大厦酒红色大家好
	 * @author 吴香礼
	 * @param tabName 表名
	 * @see {@link MyDataBase#createVideoTab(String)}
	 */
	private void createVideoTab(String tabName)
	{
		//注意表明前有一空格
		mydb.execSQL("CREATE TABLE IF NOT EXISTS " +
				tabName +
				" (_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
				"videoid  INTEGER," +
				"videotitle  TEXT," +
				"duration  TEXT," +
				"artist  TEXT," +
				"album  TEXT," +
				"videoname  TEXT," +
				"videotype  TEXT," +
				"filesize  TEXT," +
				"filepath  TEXT," +
				"listname TEXT);");
	}
	
	/**
	 * 将音乐列表集合写入数据库
	 * @author 吴香礼
	 * @param data 音乐列表集合
	 * @return sdcard state
	 * @see {@link MyDataBase#SavaData(List)}
	 */
	public boolean savaMusicData(List<MediaList<SongInfo>> data)
	{
		if(sdstate)
		{
			//Log.i("info", "到此！datasize="+data.get(0).getList().size());

			/*
			 * sd卡可读写时
			 */
			/*
			 * 将所有的音乐列表保存，按照顺序
			 */
			/*
			 *1， 打开或创建数据库
			 */
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			/*
			 *2，如果原表存在，删除原有表
			 */
			
			mydb.execSQL("DROP TABLE IF EXISTS musiclisttab;");
			/*
			 * 3，新建表
			 */
			createMusicTab("musiclisttab");
			
			/*
			 * 4，循环保存数据
			 */
			for(int i=0;i<data.size();i++)
			{
				//Log.i("info", "到此！i="+i);
				//i为音乐列表的序号
				for(int j=0;j<data.get(i).getList().size();j++)
				{
					//j为音乐列表i中的歌曲数目
					//Log.i("info", "到此！j="+j);
					/*
					 * 插入数据
					 */
					
					if(mydb.insert("musiclisttab", null, 
							putSongDataToValue(data.get(i).getList().get(j), data.get(i).getListName()))==-1)
					{
						//Log.i("第"+j+"个数据插入失败！", "");
					}		
				}
				
			}
			/*
			 * 5，关闭数据库
			 */
			mydb.close();	
		}
		return sdstate;
	}
	
	private ContentValues putSongDataToValue(SongInfo song,String listname)
	{
		ContentValues value = new ContentValues();
		value.put("filename", song.getmFileName());
		value.put("filetitle", song.getmFileTitle());
		value.put("duration", song.getmDuration());
		value.put("singer", song.getmSinger());
		value.put("album", song.getmAlbum());
		value.put("year", song.getmYear());
		value.put("filetype", song.getmFileType());
		value.put("filesize", song.getmFileSize());
		value.put("filepath", song.getmFilePath());
		value.put("fileurl", song.getmFileUrl());
		value.put("listname", listname);
		return value;
	}
	
	/**
	 * 将数据库中的数据用于初始化应用的音乐列表集合
	 * @author 吴香礼
	 * @param mp 应用对象
	 * @return sdcard state
	 * @see {@link MyDataBase#getData(MyApplication)}
	 */
	public boolean	 getMusicData(MyApplication mp)
	{
		if(sdstate)
		{
			
			/*
			 * sd卡可读写时
			 */
			
			/*
			 * 音乐列表
			 */
			List<SongInfo> musiclist = new ArrayList<SongInfo>();
			/*
			 * 打开或则创建数据库
			 */
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			/*
			 * 获取所有数据
			 */
			Cursor cursor = mydb.query("musiclisttab", null, null, null, null, null, null);
			
			if(cursor.getCount()!=0)
			{
				
				/*
				 * 数据存在，移到第一条数据
				 */
				cursor.moveToFirst();
				
				/*
				 * 读取第一个音乐列表名字
				 */
				//Log.i("标志", "到此!");
				String listname = cursor.getString(cursor
						.getColumnIndex("listname"));
				
				/*
				 * 循环获取所有数据，并将数据归入应用中音乐集合的响应列表
				 */
				for (int j = 0; j < cursor.getCount(); j++) 
				{
					
					//如果列表名有变，读到了新的列表
					if( !listname.equals(cursor.getString(cursor
							.getColumnIndex("listname"))))
					{
						//Log.i("listname", cursor.getString(cursor
							//.getColumnIndex("listname")));
						/*
						 * 加入应用的列表集合
						 */
						if(!mp.addToMusicListByListName(new MediaList<SongInfo>(listname, 0, musiclist)))
						{
							/*
							 * 如果音乐列表不是应用默认的列表，将添加失败
							 * 在应用中新建一个列表
							 */
							mp.newMusicListByListName(new MediaList<SongInfo>(listname, 0, musiclist));
						}
						/*
						 * 记录新的列表名
						 */
						listname = cursor.getString(cursor
								.getColumnIndex("listname"));
						//清除音乐列表
						musiclist.clear();
					}
					/*
					 * 记录歌曲信息，将歌曲加入列表
					 */
					musiclist.add(getSongFromCursor(cursor));
					/*
					 * 移到下一数据
					 */
					cursor.moveToNext(); 
				}
				/*
				 * 将最后一音乐列表存入应用的音乐集合
				 */
				if(!mp.addToMusicListByListName(new MediaList<SongInfo>(listname, 0, musiclist)))
				{
					
					/*
					 * 如果音乐列表不是应用默认的列表，将添加失败
					 * 在应用中新建一个列表
					 */
					mp.newMusicListByListName(new MediaList<SongInfo>(listname, 0, musiclist));
				}
			}
			/*
			 * 关闭数据库
			 */
			
			cursor.close();
			mydb.close();	
		}
		else
		{
			/*
			 * sd卡不存在
			 */
		}
		return sdstate;
	}
	
	/**
	 * 读取音乐信息并将其返回
	 * @author 吴香礼
	 * @param cursor
	 * @see {@link MyDataBase#getSongData(MyApplication)}
	 */
	private SongInfo getSongFromCursor(Cursor cursor)
	{
		String mFileName = cursor.getString(cursor
				.getColumnIndex("filename"));
		String mFileTitle = cursor.getString(cursor
				.getColumnIndex("filetitle"));
		String mDuration = cursor.getString(cursor
				.getColumnIndex("duration"));
		String mSinger = cursor.getString(cursor
				.getColumnIndex("singer"));
		String mAlbum = cursor.getString(cursor
				.getColumnIndex("album"));
		String mYear = cursor.getString(cursor
				.getColumnIndex("year"));
		String mFileType = cursor.getString(cursor
				.getColumnIndex("filetype"));
		String mFileSize = cursor.getString(cursor
				.getColumnIndex("filesize"));
		String mFilePath = cursor.getString(cursor
				.getColumnIndex("filepath"));
		String mFileUrl =  cursor.getString(cursor
				.getColumnIndex("fileurl"));
		SongInfo song = new SongInfo(mFileName, mFileTitle, mDuration, mSinger,
				mAlbum, mYear, mFileType, mFileSize, mFilePath, mFileUrl);
		return song;
	}
	
	/**
	 * 将音乐列表集合写入数据库
	 * @author 吴香礼
	 * @param data 音乐列表集合
	 * @return sdcard state
	 * @see {@link MyDataBase#SavaData(List)}
	 */
	public boolean savaVideoData(List<MediaList<VideoInfo>> data)
	{
		if(sdstate)
		{
			//Log.i("info", "到此！datasize="+data.get(0).getList().size());

			/*
			 * sd卡可读写时
			 */
			/*
			 * 将所有的音乐列表保存，按照顺序
			 */
			/*
			 *1， 打开或创建数据库
			 */
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			/*
			 *2，如果原表存在，删除原有表
			 */
			
			mydb.execSQL("DROP TABLE IF EXISTS videolisttab;");
			/*
			 * 3，新建表
			 */
			createVideoTab("videolisttab");
			
			/*
			 * 4，循环保存数据
			 */
			for(int i=0;i<data.size();i++)
			{
				//Log.i("info", "到此！i="+i);
				//i为音乐列表的序号
				for(int j=0;j<data.get(i).getList().size();j++)
				{
					//j为音乐列表i中的歌曲数目
					//Log.i("info", "到此！j="+j);
					/*
					 * 插入数据
					 */
					if(mydb.insert("videolisttab", null, 
							putVideoDataToValue(data.get(i).getList().get(j), data.get(i).getListName()))==-1)
					{
						//Log.i("第"+j+"个数据插入失败！", "");
					}//if	
				}//for
				
			}//for
			/*
			 * 5，关闭数据库
			 */
			mydb.close();	
		}
		return sdstate;
	}
	
	/**
	 * 将视频信息存入一个ContenValues
	 * @author 吴香礼
	 * @param video
	 * @param listname
	 * @return value
	 * @see {@link MyDataBase#putVideoDataToValue(VideoInfo, String)}
	 */
	private ContentValues putVideoDataToValue(VideoInfo video,String listname)
	{
		ContentValues value = new ContentValues();
		value.put("videoid", video.getId());
		value.put("videotitle", video.getTitle());
		value.put("videoname", video.getDisplayName());
		value.put("duration", video.getDuration());
		value.put("artist", video.getArtist());
		value.put("album", video.getAlbum());
		value.put("videotype", video.getMimeType());
		value.put("filesize", video.getSize());
		value.put("filepath", video.getPath());
		value.put("listname", listname);
		return value;
	}
	
	/**
	 * 将数据库中的数据用于初始化应用的音乐列表集合
	 * @author 吴香礼
	 * @param mp 应用对象
	 * @return sdcard state
	 * @see {@link MyDataBase#getData(MyApplication)}
	 */
	public boolean	getVideoData(MyApplication mp)
	{
		if(sdstate)
		{
			
			/*
			 * sd卡可读写时
			 */
			
			/*
			 * 音乐列表
			 */
			List<VideoInfo> videolist = new ArrayList<VideoInfo>();
			/*
			 * 打开或则创建数据库
			 */
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			/*
			 * 获取所有数据
			 */
			Cursor cursor = mydb.query("videolisttab", null, null, null, null, null, null);
			
			if(cursor.getCount()!=0)
			{
				
				/*
				 * 数据存在，移到第一条数据
				 */
				cursor.moveToFirst();
				
				/*
				 * 读取第一个视频列表名字
				 */
				//Log.i("标志", "到此!");
				String listname = cursor.getString(cursor
						.getColumnIndex("listname"));
				
				/*
				 * 循环获取所有数据，并将数据归入应用中音乐集合的响应列表
				 */
				for (int j = 0; j < cursor.getCount(); j++) 
				{
					
					//如果列表名有变，读到了新的列表
					if( !listname.equals(cursor.getString(cursor
							.getColumnIndex("listname"))))
					{
						//Log.i("listname", cursor.getString(cursor
							//.getColumnIndex("listname")));
						/*
						 * 加入应用的列表集合
						 */
						if(!mp.addToVideoListByListName(new MediaList<VideoInfo>(listname, 0, videolist)))
						{
							/*
							 * 如果音乐列表不是应用默认的列表，将添加失败
							 * 在应用中新建一个列表
							 */
							mp.newVideoListByListName(new MediaList<VideoInfo>(listname, 0, videolist));
						}
						/*
						 * 记录新的列表名
						 */
						listname = cursor.getString(cursor
								.getColumnIndex("listname"));
						//清除音乐列表
						videolist.clear();
					}
					/*
					 * 记录歌曲信息,将歌曲加入列表
					 */
					videolist.add(getVideoFromCursor(cursor));
					/*
					 * 移到下一数据
					 */
					cursor.moveToNext();
					
				}
				/*
				 * 将最后一音乐列表存入应用的音乐集合
				 */
				if(!mp.addToVideoListByListName(new MediaList<VideoInfo>(listname, 0, videolist)))
				{
					
					/*
					 * 如果音乐列表不是应用默认的列表，将添加失败
					 * 在应用中新建一个列表
					 */
					mp.newVideoListByListName(new MediaList<VideoInfo>(listname, 0, videolist));
				}
			}
			/*
			 * 关闭数据库
			 */
			cursor.close();
			mydb.close();	
		}
		else
		{
			/*
			 * sd卡不存在
			 */
		}
		return sdstate;
	}
	
	/**
	 * 读取视频信息并将其返回
	 * @author 吴香礼
	 * @param cursor
	 * @see {@link MyDataBase#getVideoData(MyApplication)}
	 */
	private VideoInfo getVideoFromCursor(Cursor cursor)
	{
		int id = cursor.getInt(cursor
				.getColumnIndex("videoid"));
		String title = cursor.getString(cursor
				.getColumnIndex("videotitle"));
		String displayName = cursor.getString(cursor
				.getColumnIndex("videoname"));
		long duration = cursor.getLong(cursor
				.getColumnIndex("duration"));
		String artist = cursor.getString(cursor
				.getColumnIndex("artist"));
		String album = cursor.getString(cursor
				.getColumnIndex("album"));
		String mimeType = cursor.getString(cursor
				.getColumnIndex("videotype"));
		long size = cursor.getLong(cursor
				.getColumnIndex("filesize"));
		String path = cursor.getString(cursor
				.getColumnIndex("filepath"));
		VideoInfo video = new VideoInfo(id,title,album,artist,displayName,mimeType,path,size,duration);
		return video;
	}
	
}