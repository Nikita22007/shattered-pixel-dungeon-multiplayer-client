package com.watabou.gltextures;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface TextureManagerInterface {
    void loadTexturePack(InputStream stream) throws IOException;

    boolean hasAsset(String src);

    InputStream getAssetStream(String s);

    File getAssetFile(String s);
}
