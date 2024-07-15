package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.network.Scanner;
import com.shatteredpixel.shatteredpixeldungeon.network.ServerInfo;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.*;
import com.watabou.noosa.*;
import com.watabou.noosa.audio.Music;


/* TODO
    * Add UpdateButton
    * Add Next/Previous buttons to navigate on server  menu
    * TABLE_SIZE should be calculated based on the height of the screen
*/

public class ConnectScene extends PixelScene {

    private static final int DEFAULT_COLOR = 0xCCCCCC;
    private static final int TABLE_SIZE = 6;

    private static final String TXT_TITLE = "Servers";
    private static final String TXT_NO_GAMES = "No servers found.";
    private static final String TXT_ERROR = "Servers search error.";

    private static final float ROW_HEIGHT_L = 22;
    private static final float ROW_HEIGHT_P = 28;

    private static final float MAX_ROW_WIDTH = 180;

    private static final float GAP = 4;

    private Archs archs;

    private int page;//Page of servers

    @Override
    public void create() {

        super.create();

        Music.INSTANCE.play(Assets.Music.THEME_1, true);
        Music.INSTANCE.volume(1f);

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        ServerInfo[] serverList = Scanner.getServerList();
        if (serverList == null) {

            BitmapText title = new BitmapText();
            title.height = 8;
            title.text(TXT_ERROR);
            title.hardlight(DEFAULT_COLOR);
            title.measure();
            title.x = align((w - title.width()) / 2);
            title.y = align((h - title.height()) / 2);
            add(title);

        } else {
            if (serverList.length > 0) {

                float rowHeight = SPDSettings.landscape() ? ROW_HEIGHT_L : ROW_HEIGHT_P;

                float left = (w - Math.min(MAX_ROW_WIDTH, w)) / 2 + GAP;
                float top = align((h - rowHeight * serverList.length) / 2);

                BitmapText title = new BitmapText();
                title.height = 9;
                title.text(TXT_TITLE);
                title.hardlight(Window.TITLE_COLOR);
                title.measure();
                title.x = align((w - title.width()) / 2);
                title.y = align(top - title.height() - GAP);
                add(title);

                int pos = 0;

                for (int i = 0; i < TABLE_SIZE; i += 1) {
                    Record row = new Record(pos, false, serverList[i], this);
                    row.setRect(left, top + pos * rowHeight, w - left * 2, rowHeight);
                    add(row);

                    pos++;
                }

                if (serverList.length > TABLE_SIZE) {
                    //todo previous/next
                }

            } else {

                BitmapText title = new BitmapText();
                title.height = 8;
                title.text(TXT_NO_GAMES);
                title.hardlight(DEFAULT_COLOR);
                title.measure();
                title.x = align((w - title.width()) / 2);
                title.y = align((h - title.height()) / 2);
                add(title);

            }
        }
        ExitButton btnExit = new ExitButton();
        btnExit.setPos(Camera.main.width - btnExit.width(), 0);
        add(btnExit);

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
    }

    public static class Record extends Button {
        public Scene ConnectScene;

        private static final float GAP = 4;

        private static final int TEXT_WIN = 0xFFFF88;
        private static final int TEXT_LOSE = 0xCCCCCC;
        private static final int FLARE_WIN = 0x888866;
        private static final int FLARE_LOSE = 0x666666;

        private ServerInfo rec;

        private ItemSprite shield;
        private Flare flare;
        private BitmapText position;
        private BitmapText desc;
        private Image classIcon;

        public Record(int pos, boolean withFlare, ServerInfo rec, Scene scene) {
            super();
            this.ConnectScene = scene;
            this.rec = rec;

            if (withFlare) {
                flare = new Flare(6, 24);
                flare.angularSpeed = 90;
                //  flare.color( rec.win ? FLARE_WIN : FLARE_LOSE );
                flare.color(FLARE_WIN);
                addToBack(flare);
            }

            position.text(Integer.toString(pos + 1));
            position.measure();

            desc.text(rec.name);
            desc.measure();

            if (rec.haveChallenges) {
                shield.view(ItemSpriteSheet.AMULET, null);
                position.hardlight(TEXT_WIN);
                desc.hardlight(TEXT_WIN);
            } else {
                position.hardlight(TEXT_LOSE);
                desc.hardlight(TEXT_LOSE);
            }

            classIcon.copy(Icons.get(Icons.PREFS));
        }

        @Override
        protected void createChildren() {

            super.createChildren();

            shield = new ItemSprite(ItemSpriteSheet.CHEST, null);
            add(shield);

            position = new BitmapText(null);
            add(position);

            desc = new BitmapTextMultiline(null);
            add(desc);

            classIcon = new Image();
            add(classIcon);
        }

        @Override
        protected void layout() {

            super.layout();

            shield.x = x;
            shield.y = y + (height - shield.height) / 2;

            position.x = align(shield.x + (shield.width - position.width()) / 2);
            position.y = align(shield.y + (shield.height - position.height()) / 2 + 1);

            if (flare != null) {
                flare.point(shield.center());
            }

            classIcon.x = align(x + width - classIcon.width);
            classIcon.y = shield.y;

            desc.x = shield.x + shield.width + GAP;
            desc.width = (int) (classIcon.x - desc.x);
            desc.measure();
            desc.y = position.y + position.baseLine() - desc.baseLine();
        }

        @Override
        protected void onClick() {
            this.ConnectScene.add(new WndConnectServer(rec.name, rec.players, rec.maxPlayers, rec.IP, rec.port));
        }
    }
}