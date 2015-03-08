package com.example.myadapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dataclass.MediaList;
import com.example.dataclass.SongInfo;
import com.example.dataclass.VideoInfo;
import com.example.streamplayer.R;

public class VideoListExpandableListAdapter extends BaseExpandableListAdapter{

	/**
	 * �����ģ����ڲ���
	 * @see {@link MusicListExpandableListAdapter#context}
	 */
	private Context context;
	/**
	 * �����б��ϣ����д洢���������б�
	 * @see {@link MusicListExpandableListAdapter#music_class_list}
	 */
	private List<MediaList<VideoInfo>> video_class_list;
	
	/**
	 * ���캯��
	 * @author Streamer
	 * @param context ��������Ϣ
	 * @param music_class_list �����б���
	 * @see {@link MusicListExpandableListAdapter#MusicListExpandableListAdapter(Context, List)}
	 */
	public VideoListExpandableListAdapter(Context context,List<MediaList<VideoInfo>> video_class_list) {
		// TODO Auto-generated constructor stub
		super();
		this.context = context;
		this.video_class_list = video_class_list;
	}

	/**
	 * ��ȡָ���б��е�ָ������
	 * @author Streamer
	 * @param arg0 ���ڵ������б��ţ��������б������
	 * @param arg1 �����б��еı�ţ������б��е�����
	 * @see {@link MusicListExpandableListAdapter#getChild(int, int)}
	 */
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		/*ȡ����ָ�����顢ָ������Ŀ���������ݡ� 
		 * ��ȡ����ĸ�����Ϣ
		 */
		return video_class_list.get(arg0).getList().get(arg1);
	}

	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * �����Զ������б���
	 * @author Streamer
	 * {@link MusicListExpandableListAdapter#getChildView(int, int, boolean, View, ViewGroup)}
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		/*
		 * groupPositionΪ�ڼ��������б�
		 * childPosition Ϊ���б�ĵڼ��׸�
		 * isLastChild �Ƿ��Ǹ������б�����һ�׸�
		 */
		View view = ((Activity)context).getLayoutInflater().inflate
				(R.layout.songinfo_show_layout, null);
		ImageView imageview = (ImageView)view.findViewById(R.drawable.ic_mp4);
		TextView textview01 = (TextView)view.findViewById(R.id.songinfo_textview01);
		TextView textview02 = (TextView)view.findViewById(R.id.songinfo_textview02);
		textview01.setText(video_class_list.get(groupPosition).getList().get(childPosition).getDisplayName());
		Log.i("name", "video_class_list.get(groupPosition).getList().get(childPosition).getDisplayName()");
		textview02.setText(video_class_list.get(groupPosition).getList().get(childPosition).getArtist());
		return view;
	}

	/**
	 * ��ȡ�������б��е�������
	 * @author Streamer
	 * @param groupPosition �����б������
	 * @see {@link MusicListExpandableListAdapter#music_class_list}
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return video_class_list.get(groupPosition).getList().size();
	}

	/**
	 * ��ȡ�������б�
	 * @author Streamer
	 * @param groupPosition �����б�ı��
	 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return video_class_list.get(groupPosition);
	}

	/**
	 * ��ȡ�����б����Ŀ
	 * @author Streamer
	 * @see {@link MusicListExpandableListAdapter#getGroupCount()}
	 */
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return video_class_list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = ((Activity)context).getLayoutInflater().inflate
				(R.layout.music_mainlist_exlist_group, null);
		ImageView imageview = (ImageView)view.findViewById(R.id.music_mainlist_exlist_imageview);
		TextView textview = (TextView)view.findViewById(R.id.music_mainlist_exlist_textview);
		TextView t = (TextView)view.findViewById(R.id.music_mainlist_exlist_textview_num);
		textview.setText(video_class_list.get(groupPosition).getListName());
		if(groupPosition<video_class_list.size()-1)
		{
			t.setText(""+video_class_list.get(groupPosition).getList().size()+"����Ƶ");
		}
		else
		{
			t.setText("");
		}
		return view;
	}

	/**
	 * �������б�չ��ʱ���ر�����򿪵������б�
	 * @author Streamer
	 * @param groupPosition ���򿪵������б���
	 * @see {@link MusicListExpandableListAdapter#onGroupCollapsed(int)}
	 */
	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		//���������Լ�Ŀ¼��������һ��Ŀ¼�ر�
	    for(int i=0 ; i< this.getGroupCount(); i++)
	    {
	    	if(i != groupPosition)
	    	{
	    		onGroupCollapsed(i);
	    	}
	    }
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		/*
	     * �����ĸ�����Ŀ¼��Ĭ��ѡ�� 
	     */  
		// ָ��λ�õ�����ͼ�Ƿ��ѡ��
		return true;   //����true�������ѡ�����򲻿�ѡ
	}

}
