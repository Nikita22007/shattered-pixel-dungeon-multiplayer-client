package com.nikita22007.pixeldungeonmultiplayer;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandleStream;
import com.watabou.utils.Point;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class TexturePack {
    private boolean isServerTexturePack = false;
    ZipFile file;
    String name;
    String description;
    String version;

    Point frameSize;

    public TexturePack(InputStream stream, boolean isServerTexturePack) throws IOException {
        this.isServerTexturePack = isServerTexturePack;
        try {
            File tmpFile = File.createTempFile("texturePack-", ".zip");
            tmpFile.deleteOnExit();
            FileOutputStream filestream = new FileOutputStream(tmpFile);
            byte[] buf = new byte[8192];
            int length;
            while ((length = stream.read(buf)) != -1) {
                filestream.write(buf, 0, length);
            }
            file = new ZipFile(tmpFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadTexturePack();
    }

    public TexturePack(ZipFile file) throws IOException {
        this.file = file;
        loadTexturePack();
    }

    public void unload() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isServerTexturePack() {
        return isServerTexturePack;
    }

    private void loadManifest() throws IOException {
        try {
            JSONObject reader = new JSONObject(
                    JavaUtils.StringFromInputStream(
                            file.getInputStream(
                                    file.getEntry("manifest.json")
                            )
                    )
            );
            name = reader.optString("name", file.getName());
            description = reader.optString("description", "No description");
            version = reader.optString("version", "0.0.0");
            frameSize = new Point(
                    reader.optInt("frame_size_x", 16),
                    reader.optInt("frame_size_y", 16)
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadTexturePack() throws IOException {

        loadManifest();
    }

    public boolean hasAsset(String src) {
        return (file.getEntry("assets/" + src) != null);
    }
    public boolean hasSound(String src){
        return (file.getEntry("sounds/" + src) != null);
    }
    public Sound getSound(String src){
        try {
            return Gdx.audio.newSound(new FileHandleByteStream(src, file.getInputStream(file.getEntry("sounds/" + src))));
        } catch (IOException e) {
            Gdx.app.error("TexturePack", "Failed to load sound", e);
        }
        return null;
    }
    public boolean hasMusic(String src) {
        return (file.getEntry("music/" + src) != null);
    }
    public Music getMusic(String src){
        try {
            return Gdx.audio.newMusic(new FileHandleByteStream(src, file.getInputStream(file.getEntry("music/" + src))));
        } catch (IOException e) {
            Gdx.app.error("TexturePack", "Failed to load music", e);
        }
        return null;
    }

    protected InputStream getStream(String path) {
        ZipEntry entry = file.getEntry(path);
        if (entry == null) {
            return null;
        }
        try {
            return file.getInputStream(entry);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream getAssetStream(String src) {
        return getStream("assets/" + src);
    }

    public InputStream getAnimationStream(String animationsFile) {
        return getStream("animations/" + animationsFile);
    }


    private static class FileHandleByteStream extends FileHandleStream {

        private final InputStream data;

        public FileHandleByteStream(String path, InputStream data) {
            super(path);
            this.data = data;
        }

        @Override
        public InputStream read() {
            return data;
        }

        public void close() throws Exception {
            data.close();
        }
    }
}