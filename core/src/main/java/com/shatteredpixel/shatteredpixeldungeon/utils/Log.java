package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.badlogic.gdx.Gdx;
// Will replace all of this someday
public class Log {
    public static void e(String message){
        Gdx.app.error("GAME", message);
    }
    public static void w(String from, String message){
        Gdx.app.log(from, message);
    }
    public static void e(String from, String message){
        Gdx.app.error(from, message);
    }
    public static void i(String from, String message){
        Gdx.app.log(from, message);
    }
    public static void wtf(String from, String message){
        Gdx.app.error(from, message);
    }
}
