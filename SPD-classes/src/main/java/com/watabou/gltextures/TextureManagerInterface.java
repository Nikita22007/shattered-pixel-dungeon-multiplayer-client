package com.watabou.gltextures;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class TextureManagerInterface {
    public static TextureManagerInterface INSTANCE;
    public abstract void loadTexturePack(InputStream stream) throws IOException;

    public abstract boolean hasAsset(String src);

    public abstract InputStream getAssetStream(String s);

    public abstract File getAssetFile(String s);
    public abstract boolean hasSound(String src);
    public abstract Sound getSound(String src);
    public abstract boolean hasMusic(String src);
    public abstract Music getMusic(String src);
}
