package com.test.mrnom;


import java.util.Random;


public class World {

    private static final int WORLD_WIDTH = 10;

    private static final int WORLD_HEIGHT = 13;

    private static final float TICK_INITIAL = 0.5F;

    private static final float TICK_MINIMUM = 0.05F;

    private static final float TICK_DECREMENT = 0.05F;

    private static final int TICK_ACCELERATION_POINTS = 5;

    private final boolean cells[][];

    final Snake snake;

    final Food food;

    boolean gameOver;

    int score;

    private final Random random;

    private float tick;

    private float tickDelta;

    public World() {
        cells = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
        snake = new Snake(WORLD_WIDTH, WORLD_HEIGHT);
        food = new Food();
        random = new Random();
        placeFood();
        tick = TICK_INITIAL;
    }

    private void placeFood() {
        // initialize the cells to represent current world state
        for (int x = 0; x < WORLD_WIDTH; ++x) {
            for (int y = 0; y < WORLD_HEIGHT; ++y) {
                cells[x][y] = false;
            }
        }
        int len = snake.parts.size();
        for (int i = 0; i < len; ++i) {
            SnakePart part = snake.parts.get(i);
            cells[part.x][part.y] = true;
        }

        // find the x, y of the first empty cell (no snake)
        // take a random start point, and increase its x (overflowing to the next row)
        // as long as the cell is not empty
        int foodX = random.nextInt(WORLD_WIDTH);
        int foodY = random.nextInt(WORLD_HEIGHT);
        while (true) {
            if (!cells[foodX][foodY]) {
                break;
            }
            foodX += 1;
            if (foodX >= WORLD_WIDTH) {
                foodX = 0;
                foodY += 1;
                if (foodY >= WORLD_HEIGHT) {
                    foodY = 0;
                }
            }
        }

        // place the food
        food.x = foodX;
        food.y = foodY;
        Food.Type[] types = Food.Type.values();
        food.type = types[random.nextInt(types.length)];
    }

    public int update(float deltaTime) {
        if (gameOver) {
            return 0;
        }
        tickDelta += deltaTime;
        int ticks = 0;
        // update the game as many times as the current tick delta dictates
        // for example, when tick delta >= 2 * tick (because the device was
        // very busy or sth) we will update twice so that the game can catch
        // up; this would mean losing frames but it doesn't matter in this game ;d
        // collect the number of ticks and return
        while (tickDelta >= tick) {
            ++ticks;
            tickDelta -= tick;
            snake.move();
            if (snake.bitItself()) {
                gameOver = true;
                break;
            }
            SnakePart head = snake.parts.get(0);
            if (head.x == food.x && head.y == food.y) {
                ++score;
                snake.eat();
                placeFood();
                if (score % TICK_ACCELERATION_POINTS == 0) {
                    float newTick = tick - TICK_DECREMENT;
                    if (newTick > TICK_MINIMUM) {
                        tick = newTick;
                    }
                }
            }
        }
        return ticks;
    }
}
