package com.example.dataclass;

/**
 * ���ڴ����Ƶ������Ϣ����
 * @author ������
 * @see VideoInfo
 */
public class VideoInfo {

	/**
	 * ��Ƶ�ı��
	 * @see {@link VideoInfo#id}
	 */
	private int id;
	/**
	 * ��Ƶ�ı���
	 * @see {@link VideoInfo#title}
	 */
	private String title;
	/**
	 * ��Ƶ��ר����
	 * @see {@link VideoInfo#album}
	 */
	private String album;
	/**
	 * ������
	 * @see {@link VideoInfo#artist}
	 */
	private String artist;
	/**
	 * ��ʾ����
	 * @see {@link VideoInfo#displayName}
	 */
	private String displayName;
	/**
	 * ��Ƶ����
	 * @see {@link VideoInfo#mimeType}
	 */
	private String mimeType;
	/**
	 * ��Ƶ·��
	 * @see {@link VideoInfo#path}
	 */
	private String path;
	/**
	 * ��Ƶ�Ĵ�С
	 * @see {@link VideoInfo#size}
	 */
	private long size;
	/**
	 * ��Ƶ�ĳ���
	 * @see {@link VideoInfo#duration}
	 */
	private long duration;

	/**
	 * ���ݸ�������������Ƶ��Ϣ��
	 * @author ������
	 * @param id ��Ƶ���
	 * @param title ��Ƶ����
	 * @param album ��Ƶר��
	 * @param artist ������
	 * @param displayName ��ʾ����
	 * @param mimeType ��Ƶ����
	 * @param path ��Ƶ·��
	 * @param size ��Ƶ��С
	 * @param duration ��Ƶ����
	 * @see {@link VideoInfo#VideoInfo(int, String, String, String, String, String, String, long, long)}
	 */
	public VideoInfo(int id, String title, String album, String artist,
			String displayName, String mimeType, String path, long size,
			long duration) {
		super();
		this.id = id;
		this.title = title;
		this.album = album;
		this.artist = artist;
		this.displayName = displayName;
		this.mimeType = mimeType;
		this.path = path;
		this.size = size;
		this.duration = duration;
	}

	/**
	 * Ĭ�Ϲ��캯��
	 * @author ������
	 * @see {@link VideoInfo#VideoInfo()}
	 */
	public VideoInfo() {
		// TODO Auto-generated constructor stub
		super();
		this.id = 0;
		this.title = "";
		this.album = "";
		this.artist = "";
		this.displayName = "";
		this.mimeType = "";
		this.path = "";
		this.size = 0;
		this.duration = 0;
	}

	/**
	 * ��ȡ��Ƶ�ı��
	 * @author ������
	 * @return ��Ƶ�ı��
	 * @see {@link VideoInfo#getId()}
	 */
	public int getId() {
		return id;
	}

	/**
	 * ������Ƶ�ı��
	 * @param id �µ���Ƶ���
	 * @see {@link VideoInfo#setId(int)}
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * ��ȡ��Ƶ�ı���
	 * @author ������
	 * @return ��Ƶ�ı���
	 * @see {@link VideoInfo#getTitle()}
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * ������Ƶ�ı���
	 * @author ������
	 * @param title �µ���Ƶ����
	 * @see {@link VideoInfo#setTitle(String)}
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * ��ȡ��Ƶר��
	 * @author ������
	 * @return ��Ƶ��ר��
	 * @see {@link VideoInfo#getAlbum()}
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * ������Ƶ��ר����
	 * @author ������
	 * @param album �µ�ר����
	 * @see {@link VideoInfo#setAlbum(String)}
	 */
	public void setAlbum(String album) {
		this.album = album;
	}

	/**
	 * ��ȡ��Ƶ��������
	 * @author ������
	 * @return ��Ƶ��������
	 * @see {@link VideoInfo#getArtist()}
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * ������Ƶ��������
	 * @param artist �µ�������
	 * @see {@link VideoInfo#setArtist(String)}
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * ��ȡ��Ƶ��ʾ������
	 * @author ������
	 * @return ��Ƶ��ʾ������
	 * @see {@link VideoInfo#getDisplayName()}
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * ������Ƶ��ʾ������
	 * @author ������
	 * @param displayName ��Ƶ�µ���ʾ����
	 * @see {@link VideoInfo#setDisplayName(String)}
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * ��ȡ��Ƶ������
	 * @author ������
	 * @return ��Ƶ������
	 * @see {@link VideoInfo#getMimeType()}
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * �����ļ�������
	 * @author ������
	 * @param mimeType �µ���Ƶ����
	 * @see {@link VideoInfo#setMimeType(String)}
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * ��ȡ��Ƶ��·��
	 * @author ������
	 * @return �ļ���·��
	 * @see {@link VideoInfo#getPath()}
	 */
	public String getPath() {
		return path;
	}

	/**
	 * ������Ƶ��·��
	 * @author ������
	 * @param path �µ���Ƶ·��
	 * @see {@link VideoInfo#setPath(String)}
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * ��ȡ��Ƶ�Ĵ�С
	 * @author ������
	 * @return ��Ƶ�Ĵ�С
	 * @see {@link VideoInfo#getSize()}
	 */
	public long getSize() {
		return size;
	}

	/**
	 * ������Ƶ�Ĵ�С
	 * @author ������
	 * @param size ��Ƶ�µĴ�С
	 * @see {@link VideoInfo#setSize(long)}
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * ��ȡ��Ƶ�ĳ���
	 * @author ������
	 * @return ��Ƶ�ĳ���
	 * @see {@link VideoInfo#getDuration()}
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * ������Ƶ�ĳ���
	 * @author ������
	 * @param duration ��Ƶ�µĳ���
	 * @see {@link VideoInfo#setDuration(long)}
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
}
