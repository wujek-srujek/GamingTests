package com.test.framework.game;


import com.test.framework.gfx.Graphics;
import com.test.framework.input.Input;
import com.test.framework.io.FileIO;
import com.test.framework.sfx.Audio;


public interface Game<R> {

    Input getInput();

    FileIO getFileIO();

    Graphics getGraphics();

    Audio getAudio();

    // the new screen must be ready to be drawn
    void switchToScreen(GameScreen<R> screen);

    void update(float deltaTime);

    void render(R renderer, float deltaTime);
}
