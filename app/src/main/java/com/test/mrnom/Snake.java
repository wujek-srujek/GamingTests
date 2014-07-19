package com.test.mrnom;


import java.util.ArrayList;
import java.util.List;


public class Snake {

    public enum Direction {

        NORTH {
            @Override
            Direction turn(Turn turn) {
                return turn == Turn.RIGHT ? EAST : WEST;
            }
        },

        EAST {
            @Override
            Direction turn(Turn turn) {
                return turn == Turn.RIGHT ? SOUTH : NORTH;
            }
        },

        SOUTH {
            @Override
            Direction turn(Turn turn) {
                return turn == Turn.RIGHT ? WEST : EAST;
            }
        },

        WEST {
            @Override
            Direction turn(Turn turn) {
                return turn == Turn.RIGHT ? NORTH : SOUTH;
            }
        };

        abstract Direction turn(Turn turn);
    }

    public enum Turn {

        LEFT {
            @Override
            public Turn opposite() {
                return RIGHT;
            }
        },

        RIGHT {
            @Override
            public Turn opposite() {
                return LEFT;
            }
        };

        public abstract Turn opposite();
    }

    private final static int MAX_LENGTH = 40;


    private final int worldWidth;

    private final int worldHeight;

    public List<SnakePart> parts = new ArrayList<SnakePart>(MAX_LENGTH);

    public Direction direction;

    public Snake(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        direction = Direction.EAST;
        parts.add(new SnakePart(1, 1));
        parts.add(new SnakePart(0, 1));
    }

    public void turn(Turn turn) {
        direction = direction.turn(turn);
    }

    // works from the end to the front: the last part takes the coordinates of the
    // last but one, etc., eventually the second one takes the coordinates of the
    // head, and the head advances by one
    public void move() {
        // there is always at least the head and one more part
        int length = parts.size() - 1;
        for (int i = length; i > 0; --i) {
            SnakePart back = parts.get(i);
            SnakePart front = parts.get(i - 1);
            back.x = front.x;
            back.y = front.y;
        }

        // move the head by 1 in its direction
        SnakePart head = parts.get(0);
        switch (direction) {
            case NORTH:
                head.y -= 1;
                break;

            case EAST:
                head.x += 1;
                break;

            case SOUTH:
                head.y += 1;
                break;

            case WEST:
                head.x -= 1;
                break;
        }

        if (head.x < 0) {
            head.x = worldWidth - 1;
        } else if (head.x == worldWidth) {
            head.x = 0;
        } else if (head.y < 0) {
            head.y = worldHeight - 1;
        } else if (head.y == worldHeight) {
            head.y = 0;
        }
    }

    public void eat() {
        if (parts.size() < MAX_LENGTH) {
            // add a new part at the same point as current last part
            // when the snake moves next time, this part will be moved
            // to new coordinates (see #move)
            SnakePart end = parts.get(parts.size() - 1);
            parts.add(new SnakePart(end.x, end.y));
        }
    }

    public boolean bitItself() {
        int len = parts.size();
        SnakePart head = parts.get(0);
        for (int i = 1; i < len; ++i) {
            SnakePart part = parts.get(i);
            if (part.x == head.x && part.y == head.y) {
                return true;
            }
        }
        return false;
    }
}
