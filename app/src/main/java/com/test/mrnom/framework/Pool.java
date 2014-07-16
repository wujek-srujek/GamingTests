package com.test.mrnom.framework;


import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;


public abstract class Pool<T> {

    private final T[] pool;

    private int index;

    public Pool(int size) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        @SuppressWarnings("unchecked")
        T[] pool = (T[]) Array.newInstance(clazz, size);
        this.pool = pool;
        index = 0;
    }

    public T getObject() {
        if (index == 0) {
            // pool empty, new object
            return newObject();
        }
        // get pooled object
        --index;
        return pool[index];
    }

    public void free(T object) {
        if (index == pool.length) {
            // full pool
            return;
        }
        pool[index] = object;
        ++index;
    }

    protected abstract T newObject();
}
