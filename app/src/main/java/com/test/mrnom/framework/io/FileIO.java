package com.test.mrnom.framework.io;


import android.content.res.AssetManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;


public class FileIO {

    private final AssetManager assets;

    private final Callable<File> externalStorageDirFetcher;

    public FileIO(AssetManager assets, Callable<File> externalStorageDirFetcher) {
        this.assets = assets;
        this.externalStorageDirFetcher = externalStorageDirFetcher;
    }

    public InputStream readAsset(String assetName) {
        try {
            return assets.open(assetName);
        } catch (IOException e) {
            throw new RuntimeException("error reading asset " + assetName, e);
        }
    }

    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(new File(getExternalStorageDir(), fileName));
    }

    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(new File(getExternalStorageDir(), fileName));
    }

    private File getExternalStorageDir() throws IOException {
        try {
            File dir = externalStorageDirFetcher.call();
            if (dir == null) {
                throw new IllegalStateException("external storage unavailable");
            }
            return dir;
        } catch (Exception e) {
            throw new IOException("couldn't fetch external storage directory", e);
        }
    }
}
