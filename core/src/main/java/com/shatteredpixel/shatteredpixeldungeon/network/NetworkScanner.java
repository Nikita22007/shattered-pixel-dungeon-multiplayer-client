package com.shatteredpixel.shatteredpixeldungeon.network;

import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.network.scanners.NSD;
import com.watabou.pixeldungeon.network.scanners.RelaySD;
import com.watabou.pixeldungeon.network.scanners.ServerInfo;
import com.watabou.pixeldungeon.network.scanners.ServiceDiscovery.ServiceDiscoveryListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NetworkScanner {
    protected static NetworkScannerListener scannerListener;
    protected static RelaySD relayServer = null;
    protected static NSD nsd = null;

    public static boolean start(@NotNull NetworkScannerListener scannerListener) {
        initListener();
        nsd = new NSD();
        boolean res = nsd.startDiscovery(listener);
        NetworkScanner.scannerListener = scannerListener;
        if (PixelDungeon.onlineMode()) {
            relayServer = new RelaySD();
            relayServer.startDiscovery(listener);
        }
        return res;
    }

    public static boolean stop() {
        boolean res = true;
        if (nsd != null) {
            res &= nsd.stopDiscovery();
        } else {
            res &= true;
        }
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