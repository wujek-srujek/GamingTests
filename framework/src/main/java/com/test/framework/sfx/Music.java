package com.test.framework.sfx;


import android.media.MediaPlayer;

import java.io.IOException;


public class Music implements MediaPlayer.OnCompletionListener {

    private final MediaPlayer player;

    private volatile boolean prepared;

    public Music(MediaPlayer player) {
        this.player = player;
        player.setOnCompletionListener(this);
    }

    public void play() {
        if (isPlaying()) {
            return;
        }
        if (!prepared) {
            try {
                player.prepare();
                prepared = true;
            } catch (IOException e) {
                throw new RuntimeException("couldn't prepare player", e);
            }
        }
        player.start();
    }

    public void stop() {
        if (isPlaying()) {
            player.pause();
            player.seekTo(0);
        }
    }

    public void pause() {
        if (isPlaying()) {
            player.pause();
        }
    }

    public void setLooping(boolean looping) {
        player.setLooping(looping);
    }

    public void setVolume(float volume) {
        player.setVolume(volume, volume);
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public boolean isLooping() {
        return player.isLooping();
    }

    public void dispose() {
        if (isPlaying()) {
            stop();
        }
        player.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        prepared = false;
    }
}
