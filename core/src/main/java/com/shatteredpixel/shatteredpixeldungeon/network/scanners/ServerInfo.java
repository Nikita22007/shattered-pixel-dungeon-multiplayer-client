package com.shatteredpixel.shatteredpixeldungeon.network.scanners;

import com.watabou.pixeldungeon.network.ServerAddress;

public abstract class ServerInfo {
    public String name = "no-name";
    public int players = 0;
    public int maxPlayers = 0;
    public boolean haveChallenges = false;

    public abstract ServerAddress getAddress();
}