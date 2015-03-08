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
 * ���ڶ����ݿ�Ĳ���
 * @author ������
 * @see {@link MyDataBase}
 */
@SuppressLint("SdCardPath")
public class MyDataBase{
	
	/**
	 * ���ݿ����
	 * @see {@link MyDataBase#mydb}
	 */
	private SQLiteDatabase mydb;
	/**
	 * ���ݿ������ļ���·��
	 * @see {@link MyDataBase#path}
	 */
	File path = new File("/sdcard/StreamPlayer");
	/**
	 * ���ݿ�·��
	 * @see {@link MyDataBase#file}
	 */
	File file = new File("/sdcard/StreamPlayer/data.db");
	
	/**
	 * ��¼sd����ǰ�Ƿ���ж�дȨ��
	 * @see {@link MyDataBase#sdstate}
	 */
	boolean sdstate = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	
	/**
	 * ���캯��������������ݿ����
	 * @author ������
	 * @see {@link MyDataBase}
	 */
	public MyDataBase()
	{
		if(sdstate)
		{
			
			/*
			 * sd���ɶ�дʱ
			 */
			if(!path.exists())
			{
				/*
				 * ·�������ڣ��½�·��
				 */
				path.mkdir();
			}
			if(!file.exists())
			{
				/*
				 * ���ݿⲻ����,�½����ݿ��ļ�
				 */
				try
				{
					if(file.createNewFile())
					{
						//�½����ݿ�ɹ�,�򿪻��򴴽����ݿ�
						mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
						/*
						 * �����������½���,�б�ţ�������Ϣ���ø������ڵ������б���
						 */
						createMusicTab("musiclisttab");
						createVideoTab("videolisttab");
					}
				}catch(IOException e)
				{
					e.printStackTrace();
					Log.i("Sdcard", "sd�����ܴ��ڣ�");
					/*
					 * ��Ҫ�ڴ���һ��������
					 */
				}
			}
			else
			{
				//�������ݿ��ļ�ʱ���򿪻��򴴽����ݿ⣬���Ѵ���
				mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
				/*
				 * �����������½���,�б�ţ�����/��Ƶ��Ϣ���ø�������Ƶ���ڵ�����/��Ƶ�б���
				 */
				createMusicTab("musiclisttab");
				createVideoTab("videolisttab");
			}
			/*
			 * �ر����ݿ�
			 */
			mydb.close();
		}
		else
		{
			/*
			 * sd�����ɶ�д�����ݿⴴ��ʧ�ܣ���ҪһЩ��ʾ
			 */
		}
		
	}
	
	/**
	 * ����һ������ΪtabName�Ĵ洢������Ϣ�ı�
	 * @author ������
	 * @param tabName ����
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
	 * ����һ������ΪtabName�Ĵ洢��Ƶ��Ϣ�ı�
	 * @since �����þƺ�ɫ��Һ�
	 * @author ������
	 * @param tabName ����
	 * @see {@link MyDataBase#createVideoTab(String)}
	 */
	private void createVideoTab(String tabName)
	{
		//ע�����ǰ��һ�ո�
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
	 * �������б���д�����ݿ�
	 * @author ������
	 * @param data �����б���
	 * @return sdcard state
	 * @see {@link MyDataBase#SavaData(List)}
	 */
	public boolean savaMusicData(List<MediaList<SongInfo>> data)
	{
		if(sdstate)
		{
			//Log.i("info", "���ˣ�datasize="+data.get(0).getList().size());

			/*
			 * sd���ɶ�дʱ
			 */
			/*
			 * �����е������б��棬����˳��
			 */
			/*
			 *1�� �򿪻򴴽����ݿ�
			 */
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			/*
			 *2�����ԭ����ڣ�ɾ��ԭ�б�
			 */
			
			mydb.execSQL("DROP TABLE IF EXISTS musiclisttab;");
			/*
			 * 3���½���
			 */
			createMusicTab("musiclisttab");
			
			/*
			 * 4��ѭ����������
			 */
			for(int i=0;i<data.size();i++)
			{
				//Log.i("info", "���ˣ�i="+i);
				//iΪ�����б�����
				for(int j=0;j<data.get(i).getList().size();j++)
				{
					//jΪ�����б�i�еĸ�����Ŀ
					//Log.i("info", "���ˣ�j="+j);
					/*
					 * ��������
					 */
					
					if(mydb.insert("musiclisttab", null, 
							putSongDataToValue(data.get(i).getList().get(j), data.get(i).getListName()))==-1)
					{
						//Log.i("��"+j+"�����ݲ���ʧ�ܣ�", "");
					}		
				}
				
			}
			/*
			 * 5���ر����ݿ�
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
	 * �����ݿ��е��������ڳ�ʼ��Ӧ�õ������б���
	 * @author ������
	 * @param mp Ӧ�ö���
	 * @return sdcard state
	 * @see {@link MyDataBase#getData(MyApplication)}
	 */
	public boolean	 getMusicData(MyApplication mp)
	{
		if(sdstate)
		{
			
			/*
			 * sd���ɶ�дʱ
			 */
			
			/*
			 * �����б�
			 */
			List<SongInfo> musiclist = new ArrayList<SongInfo>();
			/*
			 * �򿪻��򴴽����ݿ�
			 */
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			/*
			 * ��ȡ��������
			 */
			Cursor cursor = mydb.query("musiclisttab", null, null, null, null, null, null);
			
			if(cursor.getCount()!=0)
			{
				
				/*
				 * ���ݴ��ڣ��Ƶ���һ������
				 */
				cursor.moveToFirst();
				
				/*
				 * ��ȡ��һ�������б�����
				 */
				//Log.i("��־", "����!");
				String listname = cursor.getString(cursor
						.getColumnIndex("listname"));
				
				/*
				 * ѭ����ȡ�������ݣ��������ݹ���Ӧ�������ּ��ϵ���Ӧ�б�
				 */
				for (int j = 0; j < cursor.getCount(); j++) 
				{
					
					//����б����б䣬�������µ��б�
					if( !listname.equals(cursor.getString(cursor
							.getColumnIndex("listname"))))
					{
						//Log.i("listname", cursor.getString(cursor
							//.getColumnIndex("listname")));
						/*
						 * ����Ӧ�õ��б���
						 */
						if(!mp.addToMusicListByListName(new MediaList<SongInfo>(listname, 0, musiclist)))
						{
							/*
							 * ��������б���Ӧ��Ĭ�ϵ��б������ʧ��
							 * ��Ӧ�����½�һ���б�
							 */
							mp.newMusicListByListName(new MediaList<SongInfo>(listname, 0, musiclist));
						}
						/*
						 * ��¼�µ��б���
						 */
						listname = cursor.getString(cursor
								.getColumnIndex("listname"));
						//��������б�
						musiclist.clear();
					}
					/*
					 * ��¼������Ϣ�������������б�
					 */
					musiclist.add(getSongFromCursor(cursor));
					/*
					 * �Ƶ���һ����
					 */
					cursor.moveToNext(); 
				}
				/*
				 * �����һ�����б����Ӧ�õ����ּ���
				 */
				if(!mp.addToMusicListByListName(new MediaList<SongInfo>(listname, 0, musiclist)))
				{
					
					/*
					 * ��������б���Ӧ��Ĭ�ϵ��б������ʧ��
					 * ��Ӧ�����½�һ���б�
					 */
					mp.newMusicListByListName(new MediaList<SongInfo>(listname, 0, musiclist));
				}
			}
			/*
			 * �ر����ݿ�
			 */
			
			cursor.close();
			mydb.close();	
		}
		else
		{
			/*
			 * sd��������
			 */
		}
		return sdstate;
	}
	
	/**
	 * ��ȡ������Ϣ�����䷵��
	 * @author ������
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
	 * �������б���д�����ݿ�
	 * @author ������
	 * @param data �����б���
	 * @return sdcard state
	 * @see {@link MyDataBase#SavaData(List)}
	 */
	public boolean savaVideoData(List<MediaList<VideoInfo>> data)
	{
		if(sdstate)
		{
			//Log.i("info", "���ˣ�datasize="+data.get(0).getList().size());

			/*
			 * sd���ɶ�дʱ
			 */
			/*
			 * �����е������б��棬����˳��
			 */
			/*
			 *1�� �򿪻򴴽����ݿ�
			 */
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			/*
			 *2�����ԭ����ڣ�ɾ��ԭ�б�
			 */
			
			mydb.execSQL("DROP TABLE IF EXISTS videolisttab;");
			/*
			 * 3���½���
			 */
			createVideoTab("videolisttab");
			
			/*
			 * 4��ѭ����������
			 */
			for(int i=0;i<data.size();i++)
			{
				//Log.i("info", "���ˣ�i="+i);
				//iΪ�����б�����
				for(int j=0;j<data.get(i).getList().size();j++)
				{
					//jΪ�����б�i�еĸ�����Ŀ
					//Log.i("info", "���ˣ�j="+j);
					/*
					 * ��������
					 */
					if(mydb.insert("videolisttab", null, 
							putVideoDataToValue(data.get(i).getList().get(j), data.get(i).getListName()))==-1)
					{
						//Log.i("��"+j+"�����ݲ���ʧ�ܣ�", "");
					}//if	
				}//for
				
			}//for
			/*
			 * 5���ر����ݿ�
			 */
			mydb.close();	
		}
		return sdstate;
	}
	
	/**
	 * ����Ƶ��Ϣ����һ��ContenValues
	 * @author ������
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
	 * �����ݿ��е��������ڳ�ʼ��Ӧ�õ������б���
	 * @author ������
	 * @param mp Ӧ�ö���
	 * @return sdcard state
	 * @see {@link MyDataBase#getData(MyApplication)}
	 */
	public boolean	getVideoData(MyApplication mp)
	{
		if(sdstate)
		{
			
			/*
			 * sd���ɶ�дʱ
			 */
			
			/*
			 * �����б�
			 */
			List<VideoInfo> videolist = new ArrayList<VideoInfo>();
			/*
			 * �򿪻��򴴽����ݿ�
			 */
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			/*
			 * ��ȡ��������
			 */
			Cursor cursor = mydb.query("videolisttab", null, null, null, null, null, null);
			
			if(cursor.getCount()!=0)
			{
				
				/*
				 * ���ݴ��ڣ��Ƶ���һ������
				 */
				cursor.moveToFirst();
				
				/*
				 * ��ȡ��һ����Ƶ�б�����
				 */
				//Log.i("��־", "����!");
				String listname = cursor.getString(cursor
						.getColumnIndex("listname"));
				
				/*
				 * ѭ����ȡ�������ݣ��������ݹ���Ӧ�������ּ��ϵ���Ӧ�б�
				 */
				for (int j = 0; j < cursor.getCount(); j++) 
				{
					
					//����б����б䣬�������µ��б�
					if( !listname.equals(cursor.getString(cursor
							.getColumnIndex("listname"))))
					{
						//Log.i("listname", cursor.getString(cursor
							//.getColumnIndex("listname")));
						/*
						 * ����Ӧ�õ��б���
						 */
						if(!mp.addToVideoListByListName(new MediaList<VideoInfo>(listname, 0, videolist)))
						{
							/*
							 * ��������б���Ӧ��Ĭ�ϵ��б������ʧ��
							 * ��Ӧ�����½�һ���б�
							 */
							mp.newVideoListByListName(new MediaList<VideoInfo>(listname, 0, videolist));
						}
						/*
						 * ��¼�µ��б���
						 */
						listname = cursor.getString(cursor
								.getColumnIndex("listname"));
						//��������б�
						videolist.clear();
					}
					/*
					 * ��¼������Ϣ,�����������б�
					 */
					videolist.add(getVideoFromCursor(cursor));
					/*
					 * �Ƶ���һ����
					 */
					cursor.moveToNext();
					
				}
				/*
				 * �����һ�����б����Ӧ�õ����ּ���
				 */
				if(!mp.addToVideoListByListName(new MediaList<VideoInfo>(listname, 0, videolist)))
				{
					
					/*
					 * ��������б���Ӧ��Ĭ�ϵ��б������ʧ��
					 * ��Ӧ�����½�һ���б�
					 */
					mp.newVideoListByListName(new MediaList<VideoInfo>(listname, 0, videolist));
				}
			}
			/*
			 * �ر����ݿ�
			 */
			cursor.close();
			mydb.close();	
		}
		else
		{
			/*
			 * sd��������
			 */
		}
		return sdstate;
	}
	
	/**
	 * ��ȡ��Ƶ��Ϣ�����䷵��
	 * @author ������
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