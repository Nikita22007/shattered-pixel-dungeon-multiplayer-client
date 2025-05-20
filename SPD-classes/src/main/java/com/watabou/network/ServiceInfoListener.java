package com.watabou.network;

public interface ServiceInfoListener {
    default void onServiceResolved(ServiceInfo info){};
    default void onServiceFound(ServiceInfo info){};
    default void onServiceLost(ServiceInfo info){};
}
