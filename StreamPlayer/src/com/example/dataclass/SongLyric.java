package com.example.dataclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * ��ʶ���
 * @author ������
 * @see SongLyric
 */
public final class SongLyric {
		
	/**
	 * ����
	 * @see {@link SongLyric#title}
	 */
	private String title = "";
	/**
	 * ������
	 * @see {@link SongLyric#artist}
	 */
	private String artist = "";
	/**
	 * ר����
	 * @see {@link SongLyric#album}
	 */
	private String album = "";
	/**
	 * ��ʵ�ƫ��ʱ��
	 * @see {@link SongLyric#offset}
	 */
	private long offset = 0;
	/**
	 * ���ʱ��
	 * @see {@link SongLyric#maxTime}
	 */
	private long maxTime = 0;
	/**
	 * �������
	 * @see {@link SongLyric#lrcs}
	 */
	private Map<Long, String> lrcs = new HashMap<Long, String>();
	/**
	 * ��֤�Ƿ�ͨ��
	 * @see {@link SongLyric#valid}
	 */
	private boolean valid = false;
	
	private String Code = "GBK";

	/**
	 * ���캯����ʹ�ø�ʵ�·�������ʶ���
	 * @author ������
	 * @param url ���·��
	 * @see {@link SongLyric#SongLyric(String)}
	 */
	public SongLyric(String url) {
		File file = new File(url);
		if (file.exists()) {
			try {
				/*
				 * ������ȡ����ע���ļ��ı��뷽ʽ��gbk��utf8
				 */
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), Code));
				String line = null;
				while ((line = br.readLine()) != null) {
					dealLine(line);
					//Log.i(file.toString(), line+valid);
				}
				valid = true;
				/*
				 * �رն�ȡ��
				 */
				br.close();
			} catch (Exception e) {
				System.out.println("Exception");
			}
		}
		else
			Log.i(file.toString(), "������"+valid);
	}
	
	/**
	 * �����ʶ���
	 * @author ������
	 * @param path �µĸ�ʵ�·��
	 * @see {@link SongLyric#setLrc(String)}
	 */
	public void setLrc(String path,String Code)
	{
		/*
		 * ����ʶ����س�ʼ״̬
		 */
		this.lrcs.clear();
		this.valid = false;
		this.album = "";
		this.artist ="";
		this.maxTime = 0;
		this.offset = 0;
		this.title = "";
		File file = new File(path);
		if (file.exists()) {
			try {
				/*
				 * ������ȡ����ע���ļ��ı��뷽ʽ��gbk��utf8
				 */
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), Code));
				String line = null;
				while ((line = br.readLine()) != null) {
					dealLine(line);
				}
				valid = true;
				/*
				 * �رն�ȡ��
				 */
				br.close();
			} catch (Exception e) {
				System.out.println("Exception");
			}
		}
		else
			Log.i(file.toString(), "������"+valid);
	}
	
	/**
	 * ��ȡ��ʱ���
	 * @author ������
	 * @return ��ʱ���
	 * @see {@link SongLyric#getTitle()}
	 */
	public String getTitle() 
	{
		return this.title;
	}

	/**
	 * ��ȡ������
	 * @author ������
	 * @return ������
	 * @see {@link SongLyric#getArtist()}
	 */
	public String getArtist() {
		return this.artist;
	}

	/**
	 * ��ȡר����
	 * @author ������
	 * @return ר����
	 * @see {@link SongLyric#getAlbum()}
	 */
	public String getAlbum() {
		return this.album;
	}

	/**
	 * ��ȡ��֤״̬
	 * @author ������
	 * @return ��֤״̬
	 * @see {@link SongLyric#isValid()}
	 */
	public boolean isValid() {
		return this.valid;
	}

	/**
	 * ��ȡ���ʱ��
	 * @author ������
	 * @return ���ʱ��
	 * @see {@link SongLyric#getMaxTime()}
	 */
	public long getMaxTime() {
		return this.maxTime;
	}

	/**
	 * �������ʱ��
	 * @author ������
	 * @param time ʱ��
	 * @see {@link SongLyric#setMaxTime(long)}
	 */
	public void setMaxTime(long time) {
		this.maxTime = time;
	}

	/**
	 * ��ȡ��ʱ��Ӧ����ʾ�ĸ��
	 * @author ������
	 * @param ls ����ʱ��
	 * @return ��Ӧ���
	 * @see {@link SongLyric#get(long)}
	 */
	public String get(long ls) {
		long time = ls + offset;
		Long curr = -1l;
		for (Long l : lrcs.keySet()) {
			curr = l > time ? curr : l < curr ? curr : l;
		}
		//Log.i(lrcs.get(curr).toString(), "����"+valid);
		return lrcs.get(curr);
	}
	
	/**
	 * ��ȡ��ʱ����Ҫ��ʾ�ĸ�ʳ�ʼʱ�������
	 * @author ������
	 * @param ls ����ʱ��
	 * @return ��ʵ�����
	 * @see {@link SongLyric#getIndex(long)}
	 */
	public int getIndex(long ls) {
		Long[] ts = getAllTimes();
		for (int i = 0; i < ts.length - 1; i++) {
			if (ls + offset >= ts[i] && ls + offset < ts[i + 1]) {
				return i;
			}
		}
		if(ts.length>0)
		{
			if(ls+offset<ts[0])
			{
				//ʱ��С�ڵ�һ�����ʱ���Ƿ���0
				return 0;
			}
			else if(ls+offset>ts[ts.length-1])
			{
				//ʱ��������һ����ʱ���������һ����
				return ts.length-1;
			}
		}
		return 0;
	}
	
	/**
	 * ��ȡ��ʱ�����ʳ�ʼʱ���ֵ
	 * @author ������
	 * @param ls ʱ��
	 * @return	 ʱ���
	 * @see {@link SongLyric#getOffset(long)}
	 */	
	public int getOffset(long ls) {
		Long[] ts = getAllTimes();
		int index = getIndex(ls);
		if (index < ts.length && index >= 0) {
			return (int) (ls + offset - ts[index]);
		}
		return 0;
	}

	/**
	 * ��ȡ��ʱ��β��ŵĸ�ʹ�����ʱ��
	 * @author ������
	 * @param ls ʱ��
	 * @return ��һʱ��
	 * @see {@link SongLyric#getNextTime(long)}
	 */
	public int getNextTime(long ls) {
		Long[] ts = getAllTimes();
		int index = getIndex(ls);
		if (index < ts.length - 1) {
			return (int) (ts[index + 1] - ts[index]);
		}
		return 0;
	}
	
	/**
	 * ��������
	 * @author ������
	 * @param line �����
	 * @see {@link SongLyric#dealLine(String)}
	 */
	private void dealLine(String line)
	{
		if (line != null && !line.equals(""))
		{
			if (line.startsWith("[ti:"))
			{
				//����
				title = line.substring(4, line.length() - 1);
			} 
			else if (line.startsWith("[ar:")) 
			{
				//����
				artist = line.substring(4, line.length() - 1);
			}
			else if (line.startsWith("[al:")) 
			{
				//ר��
				album = line.substring(4, line.length() - 1);
			} 
			else if (line.startsWith("[offset:")) 
			{
				//ר��
				offset = Long.parseLong(line.substring(8, line.length() - 1));
			}
			else 
			{
				//���и������
				Pattern ptn = Pattern.compile("\\[(\\d{2}:\\d{2}\\.\\d{2})\\]");
				Matcher mth = ptn.matcher(line);
				while (mth.find())
				{
					//�õ�ʱ���
					long time = strToLong(mth.group(1));
					//�õ�ʱ���������
					String[] str = ptn.split(line);
					String lrc = str.length > 0 ? str[str.length - 1] : "";
					lrcs.put(time, lrc);
					maxTime = maxTime > time ? maxTime : time;
				}
			}
		}
	}
	
	/**
	 * ��00:00.00��ʽ�ĸ��ʱ��ת��Ϊlong
	 * @author ������
	 * @param timeStr ʱ����ַ�����ʽ
	 * @return ʱ���long��ʽ
	 * @see {@link SongLyric#strToLong(String)}
	 */
	public static long strToLong(String timeStr) {
		String[] s = timeStr.split(":");
		int min = Integer.parseInt(s[0]);
		String[] ss = s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);
		return min * 60 * 1000 + sec * 1000 + mill * 10;
	}
	
	/**
	 * �������������00:00�ķ�ʽ����
	 * @author ������
	 * @param ts ������
	 * @return ָ��ʱ���ʽ
	 * @see {@link SongLyric#longToString(long)}
	 */
	public static String longToString(long ts) {
		int time = (int) ts / 1000;
		int ms = time % 60;
		int ss = time / 60;
		ss = ss > 99 ? 99 : ss;
		StringBuffer str = new StringBuffer();
		str.append(ss < 10 ? "0" + ss + ":" : ss + ":");
		str.append(ms < 10 ? "0" + ms : ms + "");
		return str.toString();
	}
	
	/**
	 * ��ȡ˳��ʱ���������
	 * @author ������
	 * @return ʱ������
	 * @see {@link SongLyric#getAllTimes()}
	 */
	public Long[] getAllTimes() {
		Long[] ts = new Long[lrcs.size()];
		int index = 0;
		for (Long l : lrcs.keySet()) {
			ts[index++] = l;
		}
		for (int i = 0; i < ts.length - 1; i++) {
			for (int j = i; j < ts.length; j++) {
				if (ts[i] > ts[j]) {
					Long tmp = ts[i];
					ts[i] = ts[j];
					ts[j] = tmp;
				}
			}
		}
		return ts;
	}

}