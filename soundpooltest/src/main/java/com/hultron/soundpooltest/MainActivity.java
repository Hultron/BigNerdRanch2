package com.hultron.soundpooltest;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SoundPool mSoundPool;
    private int soundID;
    boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager mAudioManager;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //通过 AudioManager 配置音量控制
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        //设置音量按钮调节音量
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //播放次数
        counter = 0;

        //创建 SoundPool
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        //加载音频文件
        soundID = mSoundPool.load(this, R.raw.indios3, 1);
    }

    //播放音频
    public void playSound(View v) {
        if (loaded && !plays) {
            mSoundPool.play(soundID, volume, volume, 1, 0, 1f);
            counter = counter++;
            Toast.makeText(this, "Played sound", Toast.LENGTH_SHORT).show();
            plays = true;
        }
    }

    public void playLoop(View v) {
        if (loaded && !plays) {
            //设置 loop parameter 为 -1， 无限循环播放
            mSoundPool.play(soundID, volume, volume, 1, -1, 1f);
            counter = counter++;
            Toast.makeText(this, "Plays loop", Toast.LENGTH_SHORT).show();
            plays = true;
        }
    }

    public void pauseSound(View v) {
        if (plays) {
            mSoundPool.pause(soundID);
            soundID = mSoundPool.load(this, R.raw.indios3, counter);
            Toast.makeText(this, "Pause sound", Toast.LENGTH_SHORT).show();
            plays = false;
        }
    }

    public void stopSound(View v) {
        if (plays) {
            mSoundPool.stop(soundID);
            soundID = mSoundPool.load(this, R.raw.indios3, counter);
            Toast.makeText(this, "Stop sound", Toast.LENGTH_SHORT).show();
            plays = false;
        }
    }
}
