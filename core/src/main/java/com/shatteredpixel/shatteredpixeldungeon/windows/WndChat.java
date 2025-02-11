package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.network.SendData;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndChat extends WndTextInput {

    public WndChat(){
        super("Chat", "", "", 255, false, "Ok", "Cancel");
    }

    @Override
    public void onSelect(boolean positive, String text) {
        hide();
        if (positive) {
            SendData.sendChatMessage(text);
        }
    }
}
