package com.example.dataclass;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

/**
 * ����һ�������ʾ�ռ�
 * @author ������
 * @see LyricView
 */
public class LyricView  extends View{
	
	/**
	 *  ��ʶ���
	 *  @see {@link LyricView#lrc}
	 */
	private SongLyric lrc = null;
	/**
	 *  ��ǰ����ʱ��
	 *  @see {@link LyricView#time}
	 */
	private long time = 0l;
	/**
	 *  ���廭��
	 *  @see {@link LyricView#fontPaint}
	 */
	private Paint fontPaint = null;
	/**
	 * ��ǰ������廭��
	 * @see {@link LyricView#lrcPaint}
	 */
	private Paint lrcPaint = null;
	/**
	 * ������ɫ
	 * @see {@link LyricView#fontColor}
	 */
	private int fontColor = Color.WHITE;
	/**
	 * ��ǰ���������ɫ
	 * @see {@link LyricView#lrcColor}
	 */
	private int lrcColor = Color.RED;
	/**
	 * �����С
	 * @see {@link LyricView#fontSize}
	 */
	private int fontSize = 20;
	/**
	 * ��ʿ�ʼ��X����
	 * @see {@link LyricView#mX}
	 */
	private float mX = 0;
	/**
	 * ���ڲ��Ÿ�ʵ�Y����
	 * @see {@link LyricView#mMiddleY}
	 */
	private float mMiddleY = 0;
	/**
	 * �����ʾ��Y����
	 * @see {@link LyricView#mY}
	 */
	private float mY = 0;
	/**
	 * ���Y����ļ��
	 * @see {@link LyricView#DY}
	 */
	private static final int DY = 50;
	/**
	 * ��ʵ�����
	 * @see {@link LyricView#mIndex}
	 */
	private int mIndex = 0;
	/**
	 * �������
	 * @see {@link LyricView#mWordsList}
	 */
	private List<String> mWordsList = new ArrayList<String>();
	
	/**
	 * Ĭ�Ϲ��캯��
	 * @author ������
	 * @param context ������
	 * @see {@link LyricView#LyricView(Context)}
	 */
	public LyricView(Context context) {
		super(context);
	}
	
	/**
	 * �ؼ����躯��
	 * @author ������
	 * @see {@link LyricView#reset()}
	 */
	public void reset()
	{
		if(mWordsList.isEmpty())
		{}
		else
			{
				mWordsList.clear();
				Long[] ts = lrc.getAllTimes();
				if(ts.length>0)
					time = ts[0];
				mIndex = 0;
				// ѭ������ÿһ�и�ʣ���ǰ���Ÿ���������
				Log.i("Time", ""+time);
				for (Long l : ts) {
					mWordsList.add(lrc.get(l));	
				}
			}
	}
	/**
	 * �ػ���ͼ
	 * @author ������
	 * @param canvas ����
	 * @see {@link LyricView#onDraw(Canvas)}
	 */
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		if(lrc != null)
		{
			try
			{
				if(fontPaint == null)
					fontPaint = new Paint();
				if(lrcPaint == null)
					lrcPaint = new Paint();
				lrcPaint.setAntiAlias(true);
				lrcPaint.setTextSize(fontSize);
				lrcPaint.setColor(fontColor);
				lrcPaint.setTypeface(Typeface.SERIF);
				lrcPaint.setTextAlign(Paint.Align.CENTER);
				fontPaint.setAntiAlias(true);
				fontPaint.setTextSize(fontSize+4);
				fontPaint.setColor(lrcColor);
				fontPaint.setTypeface(Typeface.SERIF);
				fontPaint.setTextAlign(Paint.Align.CENTER);
				Long[] ts = lrc.getAllTimes();
				for (Long l : ts) {
					mWordsList.add(lrc.get(l));
				}
				if(mWordsList.isEmpty())
				{
					canvas.drawText("�ø�����ʲ����ڣ�", mX, mMiddleY, lrcPaint);
				}
				else
				{
					canvas.drawText(mWordsList.get(mIndex), mX, mMiddleY, fontPaint);
					int alphaValue = 25;
					float tempY = mMiddleY;
					for (int i = mIndex - 1; i >= 0; i--) 
					{
						//�����������֣����ϲ���
						tempY -= DY;
						if (tempY < 0)
						{
							break;
							}
						lrcPaint.setColor(Color.argb(255 - alphaValue, 245, 245, 245));
						canvas.drawText( mWordsList.get(i), mX, tempY, lrcPaint);
						alphaValue += 25;
						}
					alphaValue = 25;
					tempY = mMiddleY;
					for (int i = mIndex + 1, len = mWordsList.size(); i < len; i++) 
					{
						//�����°벿�ָ�ʣ��ӵ�ǰ���ŵĸ�ʿ�ʼ���»���
						tempY += DY;
						if (tempY > mY||mIndex == ts.length-1) 
						{
							//�����Ƶ�����ʱ,���ѻ��Ƶ����һ�䡣
							break;
							}
						lrcPaint.setColor(Color.argb(255 - alphaValue, 245, 245, 245));
						canvas.drawText(mWordsList.get(i), mX, tempY, lrcPaint);
						alphaValue += 25;
						}
					mIndex = lrc.getIndex(time);	
					
					}
				}catch(Exception e)
				{
				
				}
		}
		
	}
	/**
	 * �����С�ı�
	 * @author ������
	 * @param w
	 * @param h
	 * @param ow
	 * @param oh
	 * @see {@link LyricView#onSizeChanged(int, int, int, int)}
	 */
	@Override
	protected void onSizeChanged(int w, int h, int ow, int oh)
	{
		super.onSizeChanged(w, h, ow, oh);
		mX = w * 0.5f;
		mY = h;
		mMiddleY = h * 0.3f;
		}
	

	/**
	 * ���ø�ʶ���
	 * @author ������
	 * @param lrc �µĸ�ʶ���
	 * @see {@link LyricView#setLyric(SongLyric)}
	 */
	public void setLyric(SongLyric lrc) {
		this.lrc = lrc;
	}

	/**
	 * ���õ�ǰʱ��
	 * @author ������
	 * @param ms ʱ��
	 * @see {@link LyricView#setTime(long)}
	 */
	public void setTime(long ms) {
		this.time = ms;
	}
	/**
	 * ���ø��������ɫ
	 * @author ������
	 * @param color �µ���ɫ
	 * @see {@link LyricView#setFontColor(int)}
	 */
	public void setFontColor(int color) {
		this.fontColor = color;
	}
	/**
	 * ���õ�ǰ���������ɫ
	 * @author ������
	 * @param color �µ���ɫ
	 * @see {@link LyricView#setLyricColor(int)}
	 */
	public void setLyricColor(int color) {
		this.lrcColor = color;
	}
	/**
	 * ���������С
	 * @author ������
	 * @param size �µ������С
	 * @see {@link LyricView#setFontSize(int)}
	 */
	public void setFontSize(int size) {
		this.fontSize = size;
	}

}
