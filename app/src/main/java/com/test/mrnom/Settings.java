package com.test.mrnom;


import android.util.Log;

import com.test.framework.io.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Settings {

    private static final String TAG = Settings.class.getName();

    public static boolean soundEnabled = true;

    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(".mrnom")));
            soundEnabled = Boolean.parseBoolean(in.readLine());
        } catch (FileNotFoundException e) {
            // OK, the file might simply not be there yet or the user deleted it or whatever
            Log.i(TAG, "settings file does not exist, defaults used");
        } catch (IOException e) {
            Log.e(TAG, "cannot read settings", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // swallow
            }
        }
    }

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(".mrnom")));
            out.write(Boolean.toString(soundEnabled));
        } catch (IOException e) {
            Log.e(TAG, "cannot save settings", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // swallow
            }
        }
    }
}
