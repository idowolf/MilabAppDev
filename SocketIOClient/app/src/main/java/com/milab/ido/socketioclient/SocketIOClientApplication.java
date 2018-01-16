package com.milab.ido.socketioclient;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by ido on 1/7/18.
 */

public class SocketIOClientApplication extends Application {
    /**
     * Extend the Application class to create a single Socket instance
     * connecting to the server and not to use multiple connections from a single client
     * (Following examples)
     */
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
