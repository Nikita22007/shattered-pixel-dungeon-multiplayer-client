package com.shatteredpixel.shatteredpixeldungeon.network.scanners;

import com.shatteredpixel.shatteredpixeldungeon.network.ServerInfo;

public interface ServiceDiscovery {
    public abstract boolean startDiscovery(ServiceDiscoveryListener listener);
    public abstract boolean stopDiscovery();

    public interface ServiceDiscoveryListener {
        public void onServiceFound(ServerInfo info);

        public void onServiceLost(ServerInfo info);
    }
}