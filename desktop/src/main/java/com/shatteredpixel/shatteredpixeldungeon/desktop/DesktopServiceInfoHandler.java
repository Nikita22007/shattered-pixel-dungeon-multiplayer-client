package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.watabou.network.ServiceInfo;
import com.watabou.network.ServiceInfoHandler;
import com.watabou.network.ServiceInfoListener;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class DesktopServiceInfoHandler extends ServiceInfoHandler {
    JmmDNS dns = JmmDNS.Factory.getInstance();
    Listener serviceListener = new Listener();

    public void startDiscovery() {
        for (String serviceType : serviceTypes) {
            dns.addServiceListener(serviceType +"local.", serviceListener);
        }
    }

    @Override
    public void stopDiscovery() {
        //Don't know if it can or should ever be stopped
        if (dns != null) {
            for (String serviceType : serviceTypes) {
                dns.removeServiceListener(serviceType + "local.", serviceListener);
            }
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
            Map<String, String> properties = new HashMap<>();
            Enumeration<String> names = event.getInfo().getPropertyNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                String value = event.getInfo().getPropertyString(name);
                if (value != null) {
                    properties.put(name, value);
                }
            }
            return new com.watabou.network.ServiceInfo(event.getName(), event.getInfo().getInetAddresses()[0], event.getType(), event.getInfo().getPort(), properties);
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            //Happens when service is added and IP is not yet resolved
            return null;
        }
    }
}
