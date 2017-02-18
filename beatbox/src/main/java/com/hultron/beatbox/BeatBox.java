package com.hultron.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";

    public static final String SOUNDS_FOLDER = "sample_sounds";

    private AssetManager mAssetManager;

    public static final int MAX_SOUNDS = 5;

    private List<Sound> mSounds = new ArrayList<>();

    private SoundPool mSoundPool;

    public BeatBox(Context context) {
        mAssetManager = context.getAssets();

        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);

    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    private void loadSounds() {
        String[] soundName;
        try {
            soundName = mAssetManager.list(SOUNDS_FOLDER);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (String filename : soundName) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                loadSound(sound);
                mSounds.add(sound);
            } catch (IOException e) {
                Log.e(TAG, "loadSounds: Could not load sound " + filename, e);
            }
        }
    }

    /*加载音频*/
    private void loadSound(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssetManager.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    /*播放音频*/
    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    /*释放音频*/
    public void release() {
        mSoundPool.release();
    }
}
