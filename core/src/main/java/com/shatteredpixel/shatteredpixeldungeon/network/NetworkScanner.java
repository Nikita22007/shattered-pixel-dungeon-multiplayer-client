package com.shatteredpixel.shatteredpixeldungeon.network;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.network.scanners.RelaySD;

import com.shatteredpixel.shatteredpixeldungeon.network.scanners.ServerInfo;
import com.shatteredpixel.shatteredpixeldungeon.network.scanners.ServiceDiscovery;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NetworkScanner {
    protected static NetworkScannerListener scannerListener;
    protected static RelaySD relayServer = null;

    public static boolean start(@NotNull NetworkScannerListener scannerListener) {
        initListener();
        NetworkScanner.scannerListener = scannerListener;
        if (ShatteredPixelDungeon.onlineMode()) {
            relayServer = new RelaySD();
            relayServer.startDiscovery(listener);
        }
        return res;
    }

    public static boolean stop() {
        boolean res = true;
            res &= true;
        if (relayServer != null) {
            res &= relayServer.stopDiscovery();
            relayServer = null;
        }
        scannerListener = null;
        return res;
    }

    public static List<ServerInfo> getServerList() {
        List<ServerInfo> result = new ArrayList<ServerInfo>(nsd.getServerList());
        if (relayServer != null) {
            result.addAll(relayServer.getServerList());
        }
        return result;
    }

    protected static ServiceDiscovery.ServiceDiscoveryListener listener = null;

    protected static void initListener() {
        listener = new ServiceDiscovery.ServiceDiscoveryListener() {
            public void onServiceFound(ServerInfo info) {
                if (scannerListener != null)
                    scannerListener.OnServerFound(info);
            }
            public void onServiceLost(ServerInfo info) {
                scannerListener.OnServerFound(info);
            }
        }
        ;
    }

    public interface NetworkScannerListener {
        public void OnServerFound(ServerInfo info);
        public void OnServerLost(ServerInfo info);
    }

    public static int getPortForServerID(int id) {
        if (relayServer == null){
            return 0;
        }
        return relayServer.getPortForServerID(id);
    }
}