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

import com.example.dataclass.VideoInfo;


/**
 * �Զ���һ������������������Զ������Ƶ��ʾ�б�ListView���֡�
 * ��������Ϊ��{@link SongInfoListAdapter#videoinfolist}
 * {@link SongInfoListAdapter#context}
 * @author ������
 *@see {@link VideoInfoListAdapter}
 */
public class VideoInfoListAdapter extends BaseAdapter{

	/**
	 * ����������Ƶ�б�����ݡ�
	 * @see {@link SongInfoListAdapter#videoinfolist}
	 */
	private List<VideoInfo> videoinfolist = new ArrayList<VideoInfo>();
	/**
	 * �����ģ����ڻ�ȡ����
	 * @see {@link VideoInfoListAdapter#context}
	 */
	private Context context;
	

	public VideoInfoListAdapter(Context context,List<VideoInfo> list)
	{
		super();
		this.context = context;
		this.videoinfolist = list;
		Log.i("SongInfoListAdapter","num"+ videoinfolist.size());
	}
	
	/**
	 * ��ȡ�б����ݵĸ���
	 * @author ������
	 * @return ���ݸ���
	 * @see {@link VideoInfoListAdapter#getCount()}
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return videoinfolist.size();
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
	 * @see {@link VideoInfoListAdapter#getView(int, View, ViewGroup)}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View view = ((Activity)context).getLayoutInflater().inflate(
				com.example.streamplayer.R.layout.videoinfo_show_layout, null);
		ImageView imageview = (ImageView)view.findViewById(
				com.example.streamplayer.R.id.videoinfo_imageview);
		TextView SongName_textview = (TextView)view.findViewById(
				com.example.streamplayer.R.id.videoinfo_textview01);
		TextView SongArtist_textview = (TextView)view.findViewById(
				com.example.streamplayer.R.id.videoinfo_textview02);
		
		SongName_textview.setText(videoinfolist.get(position).getDisplayName());
		SongArtist_textview.setText(videoinfolist.get(position).getArtist());
		//imageview.setBackgroundResource(
		//		com.example.streamplayer.R.drawable.ic_mp3);
		
		return view;
	}

    /**
     * ������Ƶ�б�
     * @author ������
     * @param list �µ���Ƶ�б�
     */
	public void setList(List<VideoInfo> list)
	{
		//this.videoinfolist.clear();
		this.videoinfolist = list;
	}
	
}
