package com.test.mrnom.framework.game;


import com.test.mrnom.framework.gfx.Graphics;
import com.test.mrnom.framework.input.Input;
import com.test.mrnom.framework.io.FileIO;
import com.test.mrnom.framework.sfx.Audio;


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
