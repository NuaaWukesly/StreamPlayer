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
 * 自定义一个适配器，用于填充自定义的视频显示列表ListView布局。
 * 包含参数为：{@link SongInfoListAdapter#videoinfolist}
 * {@link SongInfoListAdapter#context}
 * @author 吴香礼
 *@see {@link VideoInfoListAdapter}
 */
public class VideoInfoListAdapter extends BaseAdapter{

	/**
	 * 用于填充该视频列表的数据。
	 * @see {@link SongInfoListAdapter#videoinfolist}
	 */
	private List<VideoInfo> videoinfolist = new ArrayList<VideoInfo>();
	/**
	 * 上下文，用于获取布局
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
	 * 获取列表数据的个数
	 * @author 吴香礼
	 * @return 数据个数
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
	 * 自定义布局信息，用自定义布局文件来填充。
	 * @author 吴香礼
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
     * 设置视频列表
     * @author 吴香礼
     * @param list 新的视频列表
     */
	public void setList(List<VideoInfo> list)
	{
		//this.videoinfolist.clear();
		this.videoinfolist = list;
	}
	
}
