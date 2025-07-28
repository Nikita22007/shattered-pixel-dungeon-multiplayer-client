package com.shatteredpixel.shatteredpixeldungeon.network.scanners;


import com.shatteredpixel.shatteredpixeldungeon.network.ServerAddress;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public abstract class ServerInfo {
    public String name = "no-name";
    public int players = 0;
    public int maxPlayers = 0;
    public boolean haveChallenges = false;
    public int currentFloor = 0;
    public String motd;
    public abstract ServerAddress getAddress();
    public int icon(){
        return ItemSpriteSheet.CHEST;
    }
}