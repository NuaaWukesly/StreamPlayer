package com.example.myadapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dataclass.MediaList;
import com.example.dataclass.SongInfo;
import com.example.streamplayer.R;

/**
 * ����һ�����ֿ�չ���б�ģ�����������������б�����ʾ��һ����չ���б��С�
 * �����Ĳ�����{@link MusicListExpandableListAdapter#context}��
 * {@link MusicListExpandableListAdapter#music_class_list}
 * @author ������
 * @see {@link MusicListExpandableListAdapter}
 */
public class MusicListExpandableListAdapter extends BaseExpandableListAdapter {

	/**
	 * �����ģ����ڲ���
	 * @see {@link MusicListExpandableListAdapter#context}
	 */
	private Context context;
	/**
	 * �����б��ϣ����д洢���������б�
	 * @see {@link MusicListExpandableListAdapter#music_class_list}
	 */
	private List<MediaList<SongInfo>> music_class_list;
	
	/**
	 * ���캯��
	 * @author ������
	 * @param context ��������Ϣ
	 * @param music_class_list �����б���
	 * @see {@link MusicListExpandableListAdapter#MusicListExpandableListAdapter(Context, List)}
	 */
	public MusicListExpandableListAdapter(Context context,List<MediaList<SongInfo>> music_class_list) {
		// TODO Auto-generated constructor stub
		super();
		this.context = context;
		this.music_class_list = music_class_list;
	}

	/**
	 * ��ȡָ���б��е�ָ������
	 * @author ������
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
		return music_class_list.get(arg0).getList().get(arg1);
	}

	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * �����Զ������б���
	 * @author ������
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
		 * 
		 */
		View view = ((Activity)context).getLayoutInflater().inflate
				(R.layout.songinfo_show_layout, null);
		ImageView imageview = (ImageView)view.findViewById(R.id.songinfo_imageview);
		TextView textview01 = (TextView)view.findViewById(R.id.songinfo_textview01);
		TextView textview02 = (TextView)view.findViewById(R.id.songinfo_textview02);
		textview01.setText(music_class_list.get(groupPosition).getList().get(childPosition).getmFileTitle());
		textview02.setText(music_class_list.get(groupPosition).getList().get(childPosition).getmSinger());
		return view;
	}

	/**
	 * ��ȡ�������б��е�������
	 * @author ������
	 * @param groupPosition �����б������
	 * @see {@link MusicListExpandableListAdapter#music_class_list}
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return music_class_list.get(groupPosition).getList().size();
	}

	/**
	 * ��ȡ�������б�
	 * @author ������
	 * @param groupPosition �����б�ı��
	 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return music_class_list.get(groupPosition);
	}

	/**
	 * ��ȡ�����б����Ŀ
	 * @author ������
	 * @see {@link MusicListExpandableListAdapter#getGroupCount()}
	 */
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return music_class_list.size();
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
		TextView textview2 = (TextView)view.findViewById(R.id.music_mainlist_exlist_textview_num);
		textview.setText(music_class_list.get(groupPosition).getListName());
		if(groupPosition<music_class_list.size()-1)
		{
			textview2.setText(""+music_class_list.get(groupPosition).getList().size()+"�׸���");
		}
		else
		{
			textview2.setText("");
		}
		return view;
	}

	/**
	 * �������б�չ��ʱ���ر�����򿪵������б�
	 * @author ������
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
	
	public void setMediaList(List<MediaList<SongInfo>> Medialist)
	{
		this.music_class_list = Medialist;
	}

	
}
