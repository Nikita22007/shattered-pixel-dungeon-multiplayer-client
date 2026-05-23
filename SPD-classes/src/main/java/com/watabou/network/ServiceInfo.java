package com.watabou.network;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ServiceInfo {
    public String name;
    public InetAddress address;
    public String type;
    public int port;
    public Map<String, String> properties;
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

    public String getProperty(String key) {
        return properties.get(key);
    }

    public ServiceInfo(String name, InetAddress address, String type, int port) {
        this(name, address, type, port, new HashMap<String, String>());
    }

    public ServiceInfo(String name, InetAddress address, String type, int port, Map<String, String> properties) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.port = port;
        this.properties = properties;
    }
}
