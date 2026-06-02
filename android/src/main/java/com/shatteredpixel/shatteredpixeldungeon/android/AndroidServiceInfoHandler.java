package com.shatteredpixel.shatteredpixeldungeon.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import com.badlogic.gdx.Gdx;
import com.watabou.network.ServiceInfo;
import com.watabou.network.ServiceInfoHandler;
import com.watabou.network.ServiceInfoListener;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.nio.charset.Charset;

@SuppressLint("NewApi")

public class AndroidServiceInfoHandler extends ServiceInfoHandler {
    NsdManager nsdManager;
    List<NsdManager.DiscoveryListener> discoveryListeners = new ArrayList<>();

    private NsdManager.DiscoveryListener discoveryListener() {
        return new NsdManager.DiscoveryListener() {
        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {

        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {

        }

        @Override
        public void onDiscoveryStarted(String serviceType) {
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {

        }

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            listener.onServiceFound(fromNsd(serviceInfo));
            nsdManager.resolveService(serviceInfo, new ResolveListener());
        }

        @Override
        public void onServiceLost(NsdServiceInfo serviceInfo) {
            listener.onServiceLost(fromNsd(serviceInfo));
        }
        };
    }
    public AndroidServiceInfoHandler(ServiceInfoListener listener) {
        super(listener);
    }
    @Override
    public void startDiscovery() {
        try {
            nsdManager = (NsdManager) AndroidLauncher.instance.getSystemService(Context.NSD_SERVICE);
            for (String serviceType : serviceTypes) {
                NsdManager.DiscoveryListener discoveryListener = discoveryListener();
                discoveryListeners.add(discoveryListener);
                nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
            }
            Gdx.app.log("NSD", "discovery started");
        } catch (Exception e){}
    }

    @Override
    public void stopDiscovery() {
        for (NsdManager.DiscoveryListener discoveryListener : discoveryListeners) {
            try {
                nsdManager.stopServiceDiscovery(discoveryListener);
            } catch (Exception e){}
        }
        discoveryListeners.clear();
    }
    private static ServiceInfo fromNsd(NsdServiceInfo nsdServiceInfo){
        Map<String, String> properties = new HashMap<>();
        for (Map.Entry<String, byte[]> entry : nsdServiceInfo.getAttributes().entrySet()) {
            byte[] value = entry.getValue();
            if (value != null) {
                properties.put(entry.getKey(), new String(value, Charset.forName("UTF-8")));
            }
        }
        return new ServiceInfo(nsdServiceInfo.getServiceName(), nsdServiceInfo.getHost(), nsdServiceInfo.getServiceType(), nsdServiceInfo.getPort(), properties);
    }
    private class ResolveListener implements NsdManager.ResolveListener {

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            AndroidServiceInfoHandler.this.onServiceResolved(fromNsd(serviceInfo));
        }
    }
}
