package com.yyl.vlc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.yyl.vlc.ijk.IjkPlayerActivity;
import com.yyl.vlc.vlc.VlcPlayerActivity;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.vlc.ThumbnailUtils;
import org.videolan.vlc.util.VLCInstance;

import java.util.ArrayList;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {

//    public static final String path = "/mnt/sdcard/aaa.flac";
      public static final String path = "/mnt/sdcard/aaa.ape";
//    public static final String path ="https://nbcache00.baidupcs.com/file/bb8772a4f2d0484afae55d3b9787f292?bkt=p3-00001b97b598bea91c57cbeaee142434b4b6&xcode=9a0449be5b42492a6727428b5545c829aa5671ef952690d494b92e463168dc1b&fid=3610725717-250528-512492963724487&time=1506677627&sign=FDTAXGERLQBHSK-DCb740ccc5511e5e8fedcff06b081203-nLKSCoPAioYmG35CLv7HiQrS5j0%3D&to=h5&size=27481616&sta_dx=27481616&sta_cs=65&sta_ft=flac&sta_ct=2&sta_mt=1&fm2=MH,Yangquan,Netizen-anywhere,,on,any&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=00001b97b598bea91c57cbeaee142434b4b6&sl=83034191&expires=8h&rt=sh&r=224413601&mlogid=6302227724051374528&vuk=282335&vbdid=3783877243&fin=%E8%BF%9C%E8%B5%B0%E9%AB%98%E9%A3%9E+%28%E7%8B%AC%E5%94%B1%E7%89%88%29.flac&fn=%E8%BF%9C%E8%B5%B0%E9%AB%98%E9%A3%9E+%28%E7%8B%AC%E5%94%B1%E7%89%88%29.flac&rtype=1&iv=0&dp-logid=6302227724051374528&dp-callid=0.1.1&hps=1&tsl=300&csl=300&csign=jxOBRm%2B9AOkpJPmKw2etyaLT0P0%3D&so=0&ut=6&uter=4&serv=0&uc=1081066042&ic=498809746&ti=34c2fb83db821803972193e80f4f3b3c417803d2e712441f&by=themis";

//    public static final String path = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    // public static final String path = "http://192.168.1.27/demo2.mp4";
    //public static final String path = "rtsp://video.fjtu.com.cn/vs01/flws/flws_01.rm";
    String tag = "MainActivity";
    private ImageView thumbnail;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载库文件
        if (VLCInstance.testCompatibleCPU(this)) {
            new IjkMediaPlayer();
            Log.i(tag, "support   cpu");
            setContentView(R.layout.activity_main);
        } else {
            Log.i(tag, "not support  cpu");
        }
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        context = this;
    }

    public void myClick1(View view) {
        startActivity(new Intent(this, IjkPlayerActivity.class));
    }

    public void myClick2(View view) {
        startActivity(new Intent(this, VlcPlayerActivity.class));
    }

    public void myClick3(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }


    public void myClick4(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> options = new ArrayList<String>(10);
                //   options.add("vlc --help");
//                options.add("-vvv");
                LibVLC libVLC = new LibVLC(context, options);
                Media media = new Media(libVLC, Uri.parse(path));//网络截图效率不高
//                media.addOption("--vout=android_display,none");
                //   media.addOption("--no-audio");

//                options.add(":file-caching=3500");
//                options.add(":network-caching=3500");
//                options.add(":live-caching=1500");//直播缓存
//                options.add(":sout-mux-caching=3500");//输出缓存

//                options.add(":codec=mediacodec,iomx,all");
//                options.add(":demux=h264");

//                   media.addOption("--start-time=0");
//                  media.setHWDecoderEnabled(true, true);
                media.parse(Media.Parse.ParseNetwork);
                Log.i("yyl", "getTrackCount=" + media.getTrackCount());
                Media.VideoTrack track = (Media.VideoTrack) media.getTrack(Media.Track.Type.Video);
                Log.i("yyl", "track=" + track.toString());
                final Bitmap bitmap = ThumbnailUtils.getThumbnail(media, track.width / 2, track.height / 2);
                Log.i("yyl", "bitmap=" + bitmap);
                thumbnail.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null)
                            thumbnail.setImageBitmap(bitmap);
                    }
                });

            }
        }).start();

    }
}
