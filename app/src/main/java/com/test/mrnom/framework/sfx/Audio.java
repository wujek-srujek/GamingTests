package com.test.mrnom.framework.sfx;


import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.IOException;


public class Audio {

    private final AssetManager assets;

    private final SoundPool soundPool;

    public Audio(AssetManager assets, SoundPool soundPool) {
        this.assets = assets;
        this.soundPool = soundPool;
    }

    public Music newMusic(String assetName) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(assetName);
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(
                    assetDescriptor.getFileDescriptor(),
                    assetDescriptor.getStartOffset(),
                    assetDescriptor.getLength());
            return new Music(player);
        } catch (IOException e) {
            throw new RuntimeException("couldn't load music " + assetName);
        }
    }

    public Sound newSound(String assetName) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(assetName);
            int soundId = soundPool.load(assetDescriptor, 1);
            return new Sound(soundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException("couldn't load sound " + assetName, e);
        }
    }
}
