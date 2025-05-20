package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.watabou.network.ServiceInfo;
import com.watabou.network.ServiceInfoHandler;
import com.watabou.network.ServiceInfoListener;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;

public class DesktopServiceInfoHandler extends ServiceInfoHandler {
    JmDNS dns;

    {
        try {
            dns = JmDNS.create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startDiscovery() {
        try {
            dns = JmDNS.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dns.addServiceListener(serviceType + "local.", new Listener());
    }

    @Override
    public void stopDiscovery() {
        try {
            dns.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DesktopServiceInfoHandler(ServiceInfoListener listener) {
        super(listener);
    }
    protected class Listener implements ServiceListener {

        @Override
        public void serviceAdded(ServiceEvent event) {
            if (event.getInfo() != null){
                listener.onServiceFound(fromServiceEvent(event));
            }
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            if (event.getInfo() != null){
                listener.onServiceLost(fromServiceEvent(event));
            }
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            if (event.getInfo() != null){
                listener.onServiceResolved(fromServiceEvent(event));
            }
        }
    }
    protected ServiceInfo fromServiceEvent(ServiceEvent event){
        return new com.watabou.network.ServiceInfo(event.getName(), event.getInfo().getInetAddresses()[0], event.getType(), event.getInfo().getPort());
    }
}
