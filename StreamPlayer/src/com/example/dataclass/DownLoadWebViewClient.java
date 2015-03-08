package com.example.dataclass;

import java.net.URLDecoder;

import com.example.myservice.DownloadService;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DownLoadWebViewClient extends WebViewClient 
{
    private Context context;
    private CharTools chartools;
    
    public DownLoadWebViewClient(Context context)
    {
            this.context = context;
            this.chartools = new CharTools();
    }
    
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) 
    {
            Log.i("info", "open an url");
            String urlStr = "";                                                                        //存放解码后的url
            //如果是utf8编码
            if (chartools.isUtf8Url(url))
            {
                    urlStr = chartools.Utf8URLdecode(url);
            }
            else 
            {		//如果不是utf8编码
                    urlStr = URLDecoder.decode(url);
            }
            //如果链接是下载top100.cn中的mp3文件
            if (url.substring(url.length()-4).equals(".mp3"))//&&url.substring(7,10).equals("221")
            {
                    Log.i("info", "mp3 file");
                    String ss[] = urlStr.split("/");
                    String musicName = ss[ss.length-1];                                //得到音乐文件的全名（包括后缀）
                    Log.i("info", "musicfile: " + musicName);
                    //将下载链接和文件名传递给下载模块
                    Intent intent = new Intent(context,DownloadService.class);
                    intent.putExtra("url", urlStr);
                    intent.putExtra("musicName", musicName);
                    Log.i(urlStr, musicName);
                    context.startService(intent);
              }
            return super.shouldOverrideUrlLoading(view, url);
    }

}