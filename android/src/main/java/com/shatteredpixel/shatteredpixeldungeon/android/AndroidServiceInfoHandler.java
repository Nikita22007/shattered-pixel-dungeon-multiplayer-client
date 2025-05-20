package com.shatteredpixel.shatteredpixeldungeon.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import com.badlogic.gdx.Gdx;
import com.watabou.network.ServiceInfo;
import com.watabou.network.ServiceInfoHandler;
import com.watabou.network.ServiceInfoListener;

import java.util.concurrent.Executor;

@SuppressLint("NewApi")

public class AndroidServiceInfoHandler extends ServiceInfoHandler {
    NsdManager nsdManager;

    NsdManager.DiscoveryListener discoveryListener = new NsdManager.DiscoveryListener() {
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
    public AndroidServiceInfoHandler(ServiceInfoListener listener) {
        super(listener);
    }
    @Override
    public void startDiscovery() {
        try {
            nsdManager = (NsdManager) AndroidLauncher.instance.getSystemService(Context.NSD_SERVICE);
            nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
            Gdx.app.log("NSD", "discovery started");
        } catch (Exception e){}
    }

    @Override
    public void stopDiscovery() {
        try {
            nsdManager.stopServiceDiscovery(discoveryListener);
        } catch (Exception e){}
    }
    private static ServiceInfo fromNsd(NsdServiceInfo nsdServiceInfo){
        return new ServiceInfo(nsdServiceInfo.getServiceName(), nsdServiceInfo.getHost(), nsdServiceInfo.getServiceType(), nsdServiceInfo.getPort());
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
