package com.example.dataclass;

import java.util.ArrayList;
import java.util.List;

/**
 * ģ���࣬���ڴ洢һ��ý���б������������У�
 * {@link MediaList#list}
 * {@link MediaList#ListName}
 * {@link MediaList#alterNum}
 * @author ������
 * @see MediaList
 */
public class MediaList<T extends Object> {
	
	/**
	 * ListName ý���б������籾������,��Ϊһ��;
	 * @see {@link MediaList#ListName}
	 */
	private String ListName;
	/**
	 * list �洢ý���б�
	 * @see {@link MediaList#list}
	 */
	private List<T> list;
	/**
	 * alterNum��¼���б��¸��ĵ�ý�����Ŀ
	 * @see {@link MediaList#alterNum}
	 */
	private int alterNum;
	
	/**
	 * Ĭ�Ϲ��캯���������г�Ա����ֵ
	 * @author ������
	 * @see {@link MediaList#MediaList()}
	 */
	public MediaList()
	{
		super();
		this.ListName = "";
		this.list = new ArrayList<T>();
		this.alterNum = 0;
	}
	
	/**
	 * ʹ�ø��������ݹ�����
	 * @author ������
	 * @param listname ���ڳ�ʼ��ý�����͵����ơ�
	 * @param list	���ڳ�ʼ��ý�����еĳ�Ա
	 * @param num ���ڳ�ʼ������ӵ�ý����
	 * @see {@link MediaList#MediaList(String, int, List)}
	 */
	public MediaList(String listname,int num,List<T> list)
	{
		super();
		this.ListName = listname;
		this.alterNum = num;
		this.list = list;
	}
	
	/**
	 * ����ý�����������
	 * @author ������
	 * @param listname ý��������
	 * @see {@link MediaList#setListName(String)}
	 */
	public void setListName(String listname)
	{
		this.ListName = listname;
	}
	
	/**
	 * ����ý����ĳ�Ա,�����滻ԭ������
	 * @author ������
	 * @param list �������õ�����
	 * @see {@link MediaList#setList(List)}
	 */
	public void setList(List<T> list)
	{
		this.list = list;
	}
	
	/**
	 * �������
	 * @author ������
	 * @param num ������������ý����
	 * @see {@link MediaList#setNew_num(int)}
	 */
	public void setAlterNum(int num)
	{
		this.alterNum = num;
	}
	
	/**
	 * ��ȡ������ĵ�ý����
	 * @author ������
	 * @return �������ӵ�ý����
	 * @see {@link MediaList#getNew_num()}
	 */
	public int getAlterNum()
	{
		return alterNum;
	}

	/**
	 * ��ȡý���������
	 * @author ������
	 * @return ��ý���������
	 * @see {@link MediaList#getListName()}
	 */
	public String getListName()
	{
		return ListName;
	}
	
	/**
	 * ��ȡ��ý��������г�Ա
	 * @author ������
	 * @return �����е����г�Ա
	 * @see {@link MediaList#getList()}
	 */
	public List<T> getList()
	{
		return list;
	}

	/**
	 * ��ո�ý��������г�Ա,alterNum��¼��յĳ�Ա�����б������䡣
	 * @author ������
	 * @see {@link MediaList#clearMediaList()}
	 */
    public void clearMediaList()
    {
    	this.alterNum = this.list.size() ;
    	this.list = new ArrayList<T>();
    }

    /**
     * ������ݵ���ý���б��У�alterNum��Ϊ����list�ĳ���
     * @author ������
     * @param list ����ӵ�����
     * @see {@link MediaList#addAll(List)}
     */
    public void addAll(List<T> list)
    {
    	this.list.addAll(list);
    	this.alterNum = list.size();
    }

    /**
     * ���һ����Ա���б���
     * @param item ��ӵĳ�Ա
     */
    public void add(T item)
    {
    	this.list.add(item);
    	this.alterNum = 1;
    }
    
}
