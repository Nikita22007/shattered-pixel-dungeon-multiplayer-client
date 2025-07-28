package com.shatteredpixel.shatteredpixeldungeon.network;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.network.scanners.*;

import com.watabou.network.ServiceInfo;
import com.watabou.network.ServiceInfoHandler;
import com.watabou.network.ServiceInfoListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NetworkScanner {
    protected static NetworkScannerListener scannerListener;
    protected static RelaySD relayServer = null;
    protected static ServerListScanner serverListScanner;
    protected static ServiceInfoListener serviceInfoListener = new ServiceInfoListener();
    protected static ServiceInfoHandler serviceInfoHandler = ShatteredPixelDungeon.platform.createServiceInfoHandler(serviceInfoListener);

    public static boolean start(@NotNull NetworkScannerListener scannerListener) {
        boolean res = true;
        initListener();
        NetworkScanner.scannerListener = scannerListener;
        serverListScanner = new ServerListScanner();
        serverListScanner.startDiscovery(listener);
        serviceInfoHandler.startDiscovery();
        if (ShatteredPixelDungeon.onlineMode()) {
            relayServer = new RelaySD();
            res &= relayServer.startDiscovery(listener);
        }
        return res;
    }

    public static boolean stop() {
        boolean res = true;
        if (relayServer != null) {
            res &= relayServer.stopDiscovery();
            relayServer = null;
        }
        serviceInfoHandler.stopDiscovery();
        serviceInfoListener.serverList.clear();
        scannerListener = null;
        return res;
    }

    public static List<ServerInfo> getServerList() {
        List<ServerInfo> result = new ArrayList<ServerInfo>();
        result.addAll(serviceInfoListener.getServerList());
        if (relayServer != null) {
            result.addAll(relayServer.getServerList());
        }
        result.addAll(serverListScanner.getServerList());
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
    private static class ServiceInfoListener implements com.watabou.network.ServiceInfoListener {
        protected ArrayList<ServerInfo> serverList = new ArrayList<>();
        @Override
        public void onServiceResolved(ServiceInfo info) {
            serverList.add(fromServiceInfo(info));
        }

        @Override
        public void onServiceFound(ServiceInfo info) {
            scannerListener.OnServerFound(fromServiceInfo(info));
        }

        @Override
        public void onServiceLost(ServiceInfo info) {
            scannerListener.OnServerLost(fromServiceInfo(info));
        }
        private ServerInfo fromServiceInfo(ServiceInfo info){
            return new DirectServerInfo(info.getServiceName(), info.getHost(), info.getPort(), -1,-1, false);
        }

        public ArrayList<ServerInfo> getServerList() {
            return serverList;
        }
    }
}