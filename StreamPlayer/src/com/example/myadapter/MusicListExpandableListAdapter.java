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
 * 定义一个音乐可展开列表模板适配器，将音乐列表集合显示于一个可展开列表中。
 * 包含的参数：{@link MusicListExpandableListAdapter#context}；
 * {@link MusicListExpandableListAdapter#music_class_list}
 * @author 吴香礼
 * @see {@link MusicListExpandableListAdapter}
 */
public class MusicListExpandableListAdapter extends BaseExpandableListAdapter {

	/**
	 * 上下文，用于布局
	 * @see {@link MusicListExpandableListAdapter#context}
	 */
	private Context context;
	/**
	 * 音乐列表集合，其中存储所有音乐列表
	 * @see {@link MusicListExpandableListAdapter#music_class_list}
	 */
	private List<MediaList<SongInfo>> music_class_list;
	
	/**
	 * 构造函数
	 * @author 吴香礼
	 * @param context 上下文信息
	 * @param music_class_list 音乐列表集合
	 * @see {@link MusicListExpandableListAdapter#MusicListExpandableListAdapter(Context, List)}
	 */
	public MusicListExpandableListAdapter(Context context,List<MediaList<SongInfo>> music_class_list) {
		// TODO Auto-generated constructor stub
		super();
		this.context = context;
		this.music_class_list = music_class_list;
	}

	/**
	 * 获取指定列表中的指定音乐
	 * @author 吴香礼
	 * @param arg0 所在的音乐列表编号，即音乐列表的索引
	 * @param arg1 所在列表中的编号，即在列表中的索引
	 * @see {@link MusicListExpandableListAdapter#getChild(int, int)}
	 */
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		/*取得与指定分组、指定子项目关联的数据。 
		 * 获取该项的歌曲信息
		 */
		return music_class_list.get(arg0).getList().get(arg1);
	}

	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 设置自定义子列表布局
	 * @author 吴香礼
	 * {@link MusicListExpandableListAdapter#getChildView(int, int, boolean, View, ViewGroup)}
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		/*
		 * groupPosition为第几个音乐列表
		 * childPosition 为该列表的第几首歌
		 * isLastChild 是否是该音乐列表的最后一首歌
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
	 * 获取该音乐列表中的音乐数
	 * @author 吴香礼
	 * @param groupPosition 音乐列表的索引
	 * @see {@link MusicListExpandableListAdapter#music_class_list}
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return music_class_list.get(groupPosition).getList().size();
	}

	/**
	 * 获取该音乐列表
	 * @author 吴香礼
	 * @param groupPosition 音乐列表的编号
	 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return music_class_list.get(groupPosition);
	}

	/**
	 * 获取音乐列表的数目
	 * @author 吴香礼
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
			textview2.setText(""+music_class_list.get(groupPosition).getList().size()+"首歌曲");
		}
		else
		{
			textview2.setText("");
		}
		return view;
	}

	/**
	 * 当音乐列表展开时，关闭另外打开的音乐列表
	 * @author 吴香礼
	 * @param groupPosition 欲打开的音乐列表编号
	 * @see {@link MusicListExpandableListAdapter#onGroupCollapsed(int)}
	 */
	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		//遍历所有以及目录，将其他一级目录关闭
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
	     * 设置哪个二级目录被默认选中 
	     */  
		// 指定位置的子视图是否可选择。
		return true;   //返回true则歌曲可选，否则不可选
	}
	
	public void setMediaList(List<MediaList<SongInfo>> Medialist)
	{
		this.music_class_list = Medialist;
	}

	
}
