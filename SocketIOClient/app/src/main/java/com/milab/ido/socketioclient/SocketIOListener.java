package com.milab.ido.socketioclient;

/**
 * Created by ido on 1/15/18.
 */

public interface SocketIOListener {
    void onConnected(String message);
    void onReceiveStock(String price, String lastUpdated);
    void onError(String message);
}
