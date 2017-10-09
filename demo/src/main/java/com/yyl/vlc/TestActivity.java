package com.yyl.vlc;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    private IVLCVout vlcVout;
    private MediaPlayer mediaPlayer;

    private IVLCVout.Callback callback;
    private MediaPlayer.EventListener eventListener;
    private String path ="file:///mnt/sdcard/aaa.ape";

    Button button;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
        button = new Button(this);
        button.setText("000");
        button.setHeight(1080);
        button.setWidth(720);
        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
               if (mediaPlayer.isPlaying()){
                   mediaPlayer.pause();
               }else {
                   mediaPlayer.getAudioTrack();
                   mediaPlayer.play();
               }
            }
        });
        setContentView(button);
        initPlayer();
        mediaPlayer.play();

    }



    private void initPlayer(){
        ArrayList<String> options = new ArrayList<String>(10);
        options.add("vlc --help");
        options.add("-vvv");
        LibVLC libVLC = new LibVLC(this, options);
        options.add(":file-caching=500");
        options.add(":network-caching=500");
        options.add(":live-caching=500");//直播缓存
        options.add(":sout-mux-caching=500");//输出缓存

        options.add(":codec=mediacodec,iomx,all");
        options.add(":demux=h264");

        mediaPlayer = new org.videolan.libvlc.MediaPlayer(libVLC);

        Media media = new Media(libVLC, Uri.parse(path));//网络截图效率不高
        media.addOption("--vout=android_display,none");
        media.addOption("--no-audio");
        media.addOption("--no-audio");
        media.addOption("--no-audio");
        media.addOption("--skip-frames");
        media.addOption("--start-time=0");
        media.setHWDecoderEnabled(true, true);
        media.parse(Media.Parse.ParseLocal);
        mediaPlayer.setMedia(media);

        eventListener = new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                Log.d("test中", event.getBuffering()+"");
                Log.d("test中", event.getEsChangedType()+"");
                Log.d("test中", event.getPausable()+"");
                Log.d("test中", event.getPositionChanged()+"");
                Log.d("test中", event.getSeekable()+"");
                Log.d("test中", event.getTimeChanged()+"");
                Log.d("test中", event.getVoutCount()+"");

                try {
//                    if (event.getTimeChanged() == 0 || totalTime == 0 || event.getTimeChanged() > totalTime) {
//                        return; }
//                    seekBarTime.setProgress((int) event.getTimeChanged());
//                    tvCurrentTime.setText(SystemUtil.getMediaTime((int) event.getTimeChanged()));

                    if (mediaPlayer.getPlayerState() == Media.State.Ended) {//播放结束
//                        seekBarTime.setProgress(0);
                        mediaPlayer.setTime(0);
//                        tvTotalTime.setText(SystemUtil.getMediaTime((int) totalTime));
                        mediaPlayer.stop();
//                        imgPlay.setBackgroundResource(R.drawable.videoviewx_play);
                    }
                } catch (Exception e) {
                    Log.d("test中", e.toString());
                }
            }
        };
        mediaPlayer.setEventListener(eventListener);
        callback = new IVLCVout.Callback() {
            @Override public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {

            }

            @Override public void onSurfacesCreated(IVLCVout vlcVout) { }
            @Override public void onSurfacesDestroyed(IVLCVout vlcVout) { }
            @Override public void onHardwareAccelerationError(IVLCVout vlcVout) { }
        };

    }


}
