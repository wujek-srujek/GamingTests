package com.test.mrnom;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;

import com.test.framework.game.Game;
import com.test.framework.game.GameScreen;
import com.test.framework.input.TouchEvent;

import java.util.List;


public class PlayScreen extends GameScreen {

    private enum State {
        READY, RUNNING, PAUSED, GAME_OVER
    }

    private State state = State.READY;

    private final World world;

    private int oldScore;

    private final int[] scoreDigits;

    private final Rect srcRect;

    private final Rect dstRect;

    private final Paint paint;

    private boolean active;

    protected PlayScreen(Game game, Canvas canvas) {
        super(game, canvas);
        world = new World();
        scoreDigits = new int[6];
        srcRect = new Rect();
        dstRect = new Rect();
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().clearKeyBuffer();

        switch (state) {
            case READY:
                updateReady(!touchEvents.isEmpty());
                break;

            case RUNNING:
                updateRunning(touchEvents, deltaTime);
                break;

            case PAUSED:
                updatePaused(touchEvents);
                break;

            case GAME_OVER:
                updateGameOver(touchEvents);
                break;
        }
    }

    @Override
    protected void render(float deltaTime) {
        canvas.drawBitmap(Assets.background, 0F, 0F, null);
        drawWorld();
        canvas.drawLine(0, 416, 480, 416, paint);
        drawScore();

        switch (state) {
            case READY:
                drawReady();
                break;

            case RUNNING:
                drawRunning();
                break;

            case PAUSED:
                drawPaused();
                break;

            case GAME_OVER:
                drawGameOver();
                break;
        }
    }

    @Override
    public void saveState(Bundle state) {
    }

    @Override
    public void loadState(Bundle state) {
        this.state = State.PAUSED;
    }

    private void updateReady(boolean touched) {
        if (touched) {
            state = State.RUNNING;
            active = true;
        }
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for (int i = 0; i < len; ++i) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.Type.UP) {
                if (event.x < 64 && event.y > 416) {
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    state = State.PAUSED;
                    return;
                }
            }
            if (event.y < 416 && active && event.type == TouchEvent.Type.DOWN) {
                SnakePart head = world.snake.parts.get(0);
                int headX = head.x * 32;
                int headY = head.y * 32;
                Snake.Direction direction = world.snake.direction;
                Snake.Turn turn = null;
                if (direction == Snake.Direction.NORTH || direction == Snake.Direction.SOUTH) {
                    if (event.x < headX - 5) {
                        turn = Snake.Turn.LEFT;
                    } else if (event.x > headX + 32 + 5) {
                        turn = Snake.Turn.RIGHT;
                    }
                    if (turn != null && direction == Snake.Direction.SOUTH) {
                        turn = turn.opposite();
                    }
                } else if (direction == Snake.Direction.EAST || direction == Snake.Direction.WEST) {
                    if (event.y < headY - 5) {
                        turn = Snake.Turn.LEFT;
                    } else if (event.y > headY + 32 + 5) {
                        turn = Snake.Turn.RIGHT;
                    }
                    if (turn != null && direction == Snake.Direction.WEST) {
                        turn = turn.opposite();
                    }
                }
                if (turn != null) {
                    // touching movement buttons disables input for this tick
                    // prevents multiple movements in one update
                    active = false;
                    world.snake.turn(turn);
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                }
                return;
            }
        }

        if (world.update(deltaTime) > 0) {
            // the world ticked, unblock movement
            active = true;
        }

        if (world.gameOver) {
            if (Settings.soundEnabled) {
                Assets.bitten.play(1);
            }
            state = State.GAME_OVER;
        }

        // normally, we could skip this part for GAME_OVER
        // but, the world update might have skipped a frame or two
        // (see World#update) in which case we could show a wrong score
        if (oldScore != world.score) {
            if (Settings.soundEnabled) {
                Assets.eat.play(1);
            }
            changeScore(world.score);
        }
    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; ++i) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.Type.UP) {
                if (event.x > 80 && event.x <= 240) {
                    if (event.y > 100 && event.y <= 148) {
                        if (Settings.soundEnabled) {
                            Assets.click.play(1);
                        }
                        state = State.RUNNING;
                        return;
                    }
                    if (event.y > 148 && event.y < 196) {
                        if (Settings.soundEnabled) {
                            Assets.click.play(1);
                        }
                        game.switchToScreen(new MainScreen(game, canvas));
                        return;
                    }
                }
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.Type.UP) {
                if (event.x >= 128 && event.x <= 192 && event.y >= 200 && event.y <= 264) {
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    game.switchToScreen(new MainScreen(game, canvas));
                    return;
                }
            }
        }
    }

    private void changeScore(final int score) {
        int rest = score;
        for (int i = 0; rest > 0; ++i) {
            scoreDigits[scoreDigits.length - 1 - i] = rest % 10;
            rest /= 10;
        }
        oldScore = score;
    }

    private void drawWorld() {
        Snake snake = world.snake;
        SnakePart head = snake.parts.get(0);
        Food food = world.food;
        Bitmap foodBitmap;
        switch (food.type) {
            case TYPE_1:
                foodBitmap = Assets.food1;
                break;

            case TYPE_2:
                foodBitmap = Assets.food2;
                break;

            case TYPE_3:
                foodBitmap = Assets.food3;
                break;

            default:
                throw new RuntimeException("unknown food type " + food.type);
        }
        int x = food.x * 32;
        int y = food.y * 32;
        canvas.drawBitmap(foodBitmap, x, y, null);

        int len = snake.parts.size();
        for (int i = 1; i < len; i++) {
            SnakePart part = snake.parts.get(i);
            x = part.x * 32;
            y = part.y * 32;
            canvas.drawBitmap(Assets.tail, x, y, null);
        }
        Bitmap headBitmap;
        switch (snake.direction) {
            case NORTH:
                headBitmap = Assets.headUp;
                break;

            case EAST:
                headBitmap = Assets.headRight;
                break;

            case SOUTH:
                headBitmap = Assets.headDown;
                break;

            case WEST:
                headBitmap = Assets.headLeft;
                break;

            default:
                throw new RuntimeException("unknown direction " + snake.direction);
        }
        // the head is bigger by 10 pixels, center it in its cell
        x = head.x * 32;
        y = head.y * 32;
        canvas.drawBitmap(headBitmap, x - 5, y - 5, null);
    }

    private void drawReady() {
        canvas.drawBitmap(Assets.ready, 47, 100, null);
    }

    private void drawRunning() {
        prepareRect(srcRect, 64, 128, 64, 64);
        prepareRect(dstRect, 0, 416, 64, 64);
        canvas.drawBitmap(Assets.buttons, srcRect, dstRect, null);
    }

    private void drawPaused() {
        canvas.drawBitmap(Assets.pause, 80, 100, null);
    }

    private void drawGameOver() {
        canvas.drawBitmap(Assets.gameOver, 62, 100, null);
        prepareRect(srcRect, 0, 128, 64, 64);
        prepareRect(dstRect, 128, 200, 64, 64);
        canvas.drawBitmap(Assets.buttons, srcRect, dstRect, null);
    }

    private void drawScore() {
        int index = 0;
        while (scoreDigits[index] == 0 && index < scoreDigits.length - 1) {
            ++index;
        }
        int length = scoreDigits.length - index;

        int x = (game.getGraphics().getWidth() - length * 20) / 2;
        for (; index < scoreDigits.length; ++index) {
            prepareRect(srcRect, scoreDigits[index] * 20, 0, 20, 32);
            prepareRect(dstRect, x, 438, 20, 32);
            canvas.drawBitmap(Assets.numbers, srcRect, dstRect, null);
            x += 20;
        }
    }

    private void prepareRect(Rect rect, int x, int y, int width, int height) {
        rect.left = x;
        rect.top = y;
        rect.right = x + width;
        rect.bottom = y + height;
    }
}
