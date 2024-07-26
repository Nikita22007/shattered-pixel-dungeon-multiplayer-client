package com.shatteredpixel.shatteredpixeldungeon.network;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.network.scanners.RelaySD;

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

    //TODO: remove this?
    public static List<ServerInfo> getServerList() {
        return null;
    }
    protected static ServiceDiscoveryListener listener = null;

    protected static void initListener() {
        listener = new ServiceDiscoveryListener() {

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