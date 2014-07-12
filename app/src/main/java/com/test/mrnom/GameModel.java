package com.test.mrnom;


import java.util.ArrayList;
import java.util.List;


public class GameModel implements GameUpdater {

    private List<GameObject> objects;

    public GameModel() {
        objects = new ArrayList<GameObject>();
    }

    public void addGameObject(GameObject object) {
        objects.add(object);
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    @Override
    public void update() {
        for (GameObject gameObject : objects) {
            gameObject.update();
        }
    }
}
