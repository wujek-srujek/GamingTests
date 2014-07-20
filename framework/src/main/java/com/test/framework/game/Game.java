package com.test.framework.game;


import com.test.framework.gfx.Graphics;
import com.test.framework.input.Input;
import com.test.framework.io.FileIO;
import com.test.framework.sfx.Audio;


public interface Game {

    Input getInput();

    FileIO getFileIO();

    Graphics getGraphics();

    Audio getAudio();

    // the new screen must be ready to be drawn
    void switchToScreen(GameScreen screen);

    void update(float deltaTime);

    void render(float deltaTime);
}
