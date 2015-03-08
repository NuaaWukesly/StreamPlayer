package com.example.dataclass;

/**
 * ���ڴ�Ÿ���������Ϣ����
 * @author ������
 * @see SongInfo
 */
public class SongInfo {

	/**
	 * mfileName �������ļ���
	 * @see {@link SongInfo#mFileName}
	 */
	private String mFileName = "";

	/**
	 * mFileTitle ��������
	 * {@link SongInfo#mFileTitle}
	 */
	private String mFileTitle ="";

	/**
	 * mDuration ������ʱ��
	 * @see {@link SongInfo#mDuration}
	 */
	private String mDuration = "";

	/**
	 *mSinger ������
	 *@see {@link SongInfo#mSinger}
	 */
	private String mSinger = "";

	/**
	 * mAlbum ר���� 
	 * @see {@link SongInfo#mAlbum}
	 */
	private String mAlbum = "";

	/**
	 * mYear �������
	 * @see {@link SongInfo#mYear}
	 */
	private String mYear = "";

	/**
	 * mFileType �����ļ�������
	 * @see {@link SongInfo#mFileTitle}
	 */
	private String mFileType = "";

	/**
	 * mFileSize �ļ��Ĵ�С
	 * @see {@link SongInfo#mFileSize}
	 */
	private String mFileSize = "";

	/**
	 * mFilePath �ļ���·��
	 * @see {@link SongInfo#mFilePath}
	 */
	private String mFilePath = "";

	/**
	 * mFileUrl �ļ���URL
	 * @see {@link SongInfo#mFileUrl}
	 */
	private String mFileUrl="";

	/**
	 * Ĭ�Ϲ��캯��
	 * @see {@link SongInfo#SongInfo()}
	 */
	public SongInfo() {
		super();
	}

	/**
	 * ���캯�����ø����Ĳ�������
	 * @param mFileName �ļ���
	 * @param mFileTitle  �ļ�����
	 * @param mDuration �ļ�����
	 * @param mSinger ������	
	 * @param mAlbum ר����
	 * @param mYear ���
	 * @param mFileType �ļ�����
	 * @param mFileSize �ļ���С
	 * @param mFilePath �ļ�·��
	 * @param mFileUrl �ļ���URL
	 * @see {@link SongInfo#SongInfo(String, String, String, String, String, String, String, String, String, String)}
	 */
	public SongInfo(String mFileName, String mFileTitle, String mDuration,
			String mSinger, String mAlbum, String mYear, String mFileType,
			String mFileSize, String mFilePath, String mFileUrl) {
		super();
		this.mFileName = mFileName;
		this.mFileTitle = mFileTitle;
		this.mDuration = mDuration;
		this.mSinger = mSinger;
		this.mAlbum = mAlbum;
		this.mYear = mYear;
		this.mFileType = mFileType;
		this.mFileSize = mFileSize;
		this.mFilePath = mFilePath;
		this.mFileUrl = mFileUrl;
	}

	/**
	 * ��ȡ�ļ���RUL
	 * @author ������
	 * @return �ļ���URL
	 * @see {@link SongInfo#getmFileUrl()}
	 */
	public String getmFileUrl() {
		return mFileUrl;
	}

	/**
	 * �����ļ���URL
	 * @author ������
	 * @param mFileUrl �µ�URL
	 * @see {@link SongInfo#setmFileUrl(String)}
	 */
	public void setmFileUrl(String mFileUrl) {
		this.mFileUrl = mFileUrl;
	}

	/**
	 * ��ȡ�ļ�����
	 * @author ������
	 * @return �ļ�������
	 * @see {@link SongInfo#getmFileName()}
	 */
	public String getmFileName() {
		return mFileName;
	}

	/**
	 * �����ļ���
	 * @author ������
	 * @param mFileName �µ��ļ���
	 * @see {@link SongInfo#setmFileName(String)}
	 */
	public void setmFileName(String mFileName) {
		this.mFileName = mFileName;
	}

	/**
	 * ��ȡ�ļ��ı���
	 * @author ������
	 * @return �ļ��ı���
	 * @see {@link SongInfo#getmFileTitle()}
	 */
	public String getmFileTitle() {
		return mFileTitle;
	}

	/**
	 * �����ļ��ı���
	 * @author ������
	 * @param mFileTitle �µ��ļ�����
	 * @see {@link SongInfo#setmFileTitle(String)}
	 */
	public void setmFileTitle(String mFileTitle) {
		this.mFileTitle = mFileTitle;
	}

	/**
	 * ��ȡ�ļ��ĳ���
	 * @author ������
	 * @return �ļ��ĳ���
	 * @see {@link SongInfo#getmDuration()}
	 */
	public String getmDuration() {
		return mDuration;
	}

	/**
	 * �����ļ��ĳ���
	 * @author ������
	 * @param mDuration �µ�ʱ�䳤��
	 * @see {@link SongInfo#setmDuration(String)}
	 */
	public void setmDuration(String mDuration) {
		this.mDuration = mDuration;
	}

	/**
	 * ��ȡ����
	 * @author ������
	 * @return ����
	 * @see {@link SongInfo#getmSinger()}
	 */
	public String getmSinger() {
		return mSinger;
	}

	/**
	 * ���ø���
	 * @author ������
	 * @param mSinger  �µĸ���
	 * @see {@link SongInfo#setmSinger(String)}
	 */
	public void setmSinger(String mSinger) {
		this.mSinger = mSinger;
	}

	/**
	 * ��ȡר������
	 * @author ������
	 * @return �ļ���ר������
	 * @see {@link SongInfo#getmAlbum()}
	 */
	public String getmAlbum() {
		return mAlbum;
	}

	/**
	 * �����ļ���ר������
	 * @author ������
	 * @param mAlbum �µ�ר������
	 * @see {@link SongInfo#setmAlbum(String)}
	 */
	public void setmAlbum(String mAlbum) {
		this.mAlbum = mAlbum;
	}

	/**
	 * ��ȡ�ļ������
	 * @author ������
	 * @return �ļ������
	 * @see {@link SongInfo#getmYear()}
	 */
	public String getmYear() {
		return mYear;
	}

	/**
	 * �����ļ������
	 * @author ������
	 * @param mYear �µ����
	 * @see {@link SongInfo#setmYear(String)}
	 */
	public void setmYear(String mYear) {
		this.mYear = mYear;
	}

	/**
	 * ��ȡ�ļ�����
	 * @author ������
	 * @return �ļ�������
	 * @see {@link SongInfo#getmFileType()}
	 */
	public String getmFileType() {
		return mFileType;
	}

	/**
	 * �����ļ�����
	 * @author ������
	 * @param mFileType �µ��ļ�����
	 * @see {@link SongInfo#setmFileType(String)}
	 */
	public void setmFileType(String mFileType) {
		this.mFileType = mFileType;
	}

	/**
	 * ��ȡ�ļ��Ĵ�С
	 * @author ������
	 * @return �ļ��Ĵ�С
	 * @see {@link SongInfo#getmFileSize()}
	 */
	public String getmFileSize() {
		return mFileSize;
	}

	/**
	 * �����ļ��Ĵ�С
	 * @author ������
	 * @param mFileSize �µ��ļ���С��Ϣ
	 * @see {@link SongInfo#setmFileSize(String)}}
	 */
	public void setmFileSize(String mFileSize) {
		this.mFileSize = mFileSize;
	}

	/**
	 * ��ȡ�ļ���·��
	 * @author ������
	 * @return �ļ���·��
	 * @see {@link SongInfo#getmFilePath()}
	 */
	public String getmFilePath() {
		return mFilePath;
	}

	/**
	 * �����ļ���·��
	 * @author ������
	 * @param mFilePath �µ��ļ�·��
	 * @see {@link SongInfo#setmFilePath(String)}
	 */
	public void setmFilePath(String mFilePath) {
		this.mFilePath = mFilePath;
	}

}
