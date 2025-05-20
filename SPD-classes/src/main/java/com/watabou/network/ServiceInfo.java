package com.watabou.network;

import java.net.InetAddress;

public class ServiceInfo {
    public String name;
    public InetAddress address;
    public String type;
    public int port;
    public String getServiceName() {
        return name;
    }

    public InetAddress getHost() {
        return address;
    }

    public String getType() {
        return type;
    }

    public int getPort() {
        return port;
    }

    public ServiceInfo(String name, InetAddress address, String type, int port) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.port = port;
    }
}
