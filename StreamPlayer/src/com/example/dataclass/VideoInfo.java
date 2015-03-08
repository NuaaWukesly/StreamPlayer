package com.example.dataclass;

/**
 * 用于存放视频基本信息的类
 * @author 吴香礼
 * @see VideoInfo
 */
public class VideoInfo {

	/**
	 * 视频的编号
	 * @see {@link VideoInfo#id}
	 */
	private int id;
	/**
	 * 视频的标题
	 * @see {@link VideoInfo#title}
	 */
	private String title;
	/**
	 * 视频的专辑名
	 * @see {@link VideoInfo#album}
	 */
	private String album;
	/**
	 * 艺术家
	 * @see {@link VideoInfo#artist}
	 */
	private String artist;
	/**
	 * 显示名称
	 * @see {@link VideoInfo#displayName}
	 */
	private String displayName;
	/**
	 * 视频类型
	 * @see {@link VideoInfo#mimeType}
	 */
	private String mimeType;
	/**
	 * 视频路径
	 * @see {@link VideoInfo#path}
	 */
	private String path;
	/**
	 * 视频的大小
	 * @see {@link VideoInfo#size}
	 */
	private long size;
	/**
	 * 视频的长度
	 * @see {@link VideoInfo#duration}
	 */
	private long duration;

	/**
	 * 根据给出参数构造视频信息类
	 * @author 吴香礼
	 * @param id 视频编号
	 * @param title 视频标题
	 * @param album 视频专辑
	 * @param artist 艺术家
	 * @param displayName 显示名称
	 * @param mimeType 视频类型
	 * @param path 视频路径
	 * @param size 视频大小
	 * @param duration 视频长度
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
	 * 默认构造函数
	 * @author 吴香礼
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
	 * 获取视频的编号
	 * @author 吴香礼
	 * @return 视频的编号
	 * @see {@link VideoInfo#getId()}
	 */
	public int getId() {
		return id;
	}

	/**
	 * 设置视频的编号
	 * @param id 新的视频编号
	 * @see {@link VideoInfo#setId(int)}
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 获取视频的标题
	 * @author 吴香礼
	 * @return 视频的标题
	 * @see {@link VideoInfo#getTitle()}
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置视频的标题
	 * @author 吴香礼
	 * @param title 新的视频标题
	 * @see {@link VideoInfo#setTitle(String)}
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取视频专辑
	 * @author 吴香礼
	 * @return 视频的专辑
	 * @see {@link VideoInfo#getAlbum()}
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * 设置视频的专辑名
	 * @author 吴香礼
	 * @param album 新的专辑名
	 * @see {@link VideoInfo#setAlbum(String)}
	 */
	public void setAlbum(String album) {
		this.album = album;
	}

	/**
	 * 获取视频的艺术家
	 * @author 吴香礼
	 * @return 视频的艺术及
	 * @see {@link VideoInfo#getArtist()}
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * 设置视频的艺术家
	 * @param artist 新的艺术家
	 * @see {@link VideoInfo#setArtist(String)}
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * 获取视频显示的名字
	 * @author 吴香礼
	 * @return 视频显示的名称
	 * @see {@link VideoInfo#getDisplayName()}
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 设置视频显示的名字
	 * @author 吴香礼
	 * @param displayName 视频新的显示名字
	 * @see {@link VideoInfo#setDisplayName(String)}
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 获取视频的类型
	 * @author 吴香礼
	 * @return 视频的类型
	 * @see {@link VideoInfo#getMimeType()}
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * 设置文件的类型
	 * @author 吴香礼
	 * @param mimeType 新的视频类型
	 * @see {@link VideoInfo#setMimeType(String)}
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * 获取视频的路径
	 * @author 吴香礼
	 * @return 文件的路径
	 * @see {@link VideoInfo#getPath()}
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 设置视频的路径
	 * @author 吴香礼
	 * @param path 新的视频路径
	 * @see {@link VideoInfo#setPath(String)}
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 获取视频的大小
	 * @author 吴香礼
	 * @return 视频的大小
	 * @see {@link VideoInfo#getSize()}
	 */
	public long getSize() {
		return size;
	}

	/**
	 * 设置视频的大小
	 * @author 吴香礼
	 * @param size 视频新的大小
	 * @see {@link VideoInfo#setSize(long)}
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * 获取视频的长度
	 * @author 吴香礼
	 * @return 视频的长度
	 * @see {@link VideoInfo#getDuration()}
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * 设置视频的长度
	 * @author 吴香礼
	 * @param duration 视频新的长度
	 * @see {@link VideoInfo#setDuration(long)}
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
}
