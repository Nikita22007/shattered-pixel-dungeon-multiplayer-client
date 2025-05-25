package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.watabou.network.ServiceInfo;
import com.watabou.network.ServiceInfoHandler;
import com.watabou.network.ServiceInfoListener;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.net.InetAddress;
import java.util.Arrays;

public class DesktopServiceInfoHandler extends ServiceInfoHandler {
    JmmDNS dns = JmmDNS.Factory.getInstance();
    Listener serviceListener = new Listener();

    public void startDiscovery() {
        dns.addServiceListener(serviceType +"local.", serviceListener);
    }

    @Override
    public void stopDiscovery() {
        //Don't know if it can or should ever be stopped
        if (dns != null) {
            dns.removeServiceListener(serviceType + "local.", serviceListener);
        }
    }

    public DesktopServiceInfoHandler(ServiceInfoListener listener) {
        super(listener);
    }
    protected class Listener implements ServiceListener {

        @Override
        public void serviceAdded(ServiceEvent event) {
            if (event.getInfo() != null){
                ServiceInfo info = fromServiceEvent(event);
                if (info != null) {
                    listener.onServiceFound(info);
                }
            }
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            if (event.getInfo() != null){
                ServiceInfo info = fromServiceEvent(event);
                if (info != null) {
                    listener.onServiceLost(info);
                }
            }
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            if (event.getInfo() != null){
                ServiceInfo info = fromServiceEvent(event);
                if (info != null) {
                    listener.onServiceResolved(info);
                }
            }
        }
    }
    protected ServiceInfo fromServiceEvent(ServiceEvent event){
        try {
            return new com.watabou.network.ServiceInfo(event.getName(), event.getInfo().getInetAddresses()[0], event.getType(), event.getInfo().getPort());
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            //Happens when service is added and IP is not yet resolved
            return null;
        }
    }
}
