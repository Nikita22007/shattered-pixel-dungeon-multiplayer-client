package com.watabou.network;

//NO-OP class so we don't return null and then forget to check for null
public class NOOPServiceInfoHandler extends ServiceInfoHandler {
    public NOOPServiceInfoHandler(ServiceInfoListener listener) {
        super(listener);
    }

    @Override
    public void startDiscovery() {

    }

    @Override
    public void stopDiscovery() {

    }
}
