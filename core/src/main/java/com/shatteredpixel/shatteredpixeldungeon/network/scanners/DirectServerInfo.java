package com.shatteredpixel.shatteredpixeldungeon.network.scanners;


import com.shatteredpixel.shatteredpixeldungeon.network.ServerAddress;

import java.net.InetAddress;
import java.util.Objects;

public class DirectServerInfo extends ServerInfo {
    private InetAddress IP;
    private int port;

    public DirectServerInfo(String name, InetAddress ip, int port, int players, int maxPlayers, boolean haveChallenges) {
        this.name = name;
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.IP = ip;
        this.port = port;
        this.haveChallenges = haveChallenges;
    }

    @Override
    public ServerAddress getAddress() {
        ServerAddress address = new ServerAddress();
        address.host = IP.getHostAddress();
        address.port = port;
        return address;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof DirectServerInfo)) return false;

        DirectServerInfo that = (DirectServerInfo) o;
        return port == that.port && Objects.equals(IP, that.IP);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(IP);
        result = 31 * result + port;
        return result;
    }
}
