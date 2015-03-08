package com.example.myadapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dataclass.SongInfo;

/**
 * �Զ���һ������������������Զ����������ʾ�б�ListView���֡�
 * ��������Ϊ��{@link SongInfoListAdapter#songinfolist}
 * {@link SongInfoListAdapter#context}
 * @author ������
 *@see {@link SongInfoListAdapter}
 */
public class SongInfoListAdapter extends BaseAdapter{

	/**
	 * �������������б�����ݡ�
	 * @see {@link SongInfoListAdapter#songinfolist}
	 */
	private List<SongInfo> songinfolist = new ArrayList<SongInfo>();
	/**
	 * �����ģ����ڻ�ȡ����
	 * @see {@link SongInfoListAdapter#context}
	 */
	private Context context;
	
	public SongInfoListAdapter(Context context,List<SongInfo> curr_music_list)
	{
		super();
		this.context = context;
		this.songinfolist = curr_music_list;
		Log.i("SongInfoListAdapter","num"+ songinfolist.size());
	}
	
	/**
	 * ��ȡ�б����ݵĸ���
	 * @author ������
	 * @return ���ݸ���
	 * @see {@link SongInfoListAdapter#getCount()}
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return songinfolist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * �Զ��岼����Ϣ�����Զ��岼���ļ�����䡣
	 * @author ������
	 * @see {@link SongInfoListAdapter#getView(int, View, ViewGroup)}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(position<= songinfolist.size())
		{
			View view = ((Activity)context).getLayoutInflater().inflate(
					com.example.streamplayer.R.layout.songinfo_show_layout, null);
			ImageView imageview = (ImageView)view.findViewById(
					com.example.streamplayer.R.id.songinfo_imageview);
			TextView SongName_textview = (TextView)view.findViewById(
					com.example.streamplayer.R.id.songinfo_textview01);
			TextView SongArtist_textview = (TextView)view.findViewById(
					com.example.streamplayer.R.id.songinfo_textview02);
		
			SongName_textview.setText(songinfolist.get(position).getmFileTitle());
			SongArtist_textview.setText(songinfolist.get(position).getmSinger());
		//imageview.setBackgroundResource(
		//		com.example.streamplayer.R.drawable.ic_mp3);
		
			return view;
		}
		else
			return null;
	}

	/**
     * ���������б�
     * @author ������
     * @param list �µ������б�
     */
	public void setList(List<SongInfo> list)
	{
		//this.videoinfolist.clear();
		this.songinfolist = list;
	}
	
}
