package com.milab.ido.socketioclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;

import static com.milab.ido.socketioclient.Constants.LAST_UPDATED_KEY;
import static com.milab.ido.socketioclient.Constants.NOT_FOUND;
import static com.milab.ido.socketioclient.Constants.POST_STOCK_PRICE;
import static com.milab.ido.socketioclient.Constants.PRICE_KEY;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Socket mSocket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SocketIOClientApplication app = (SocketIOClientApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(POST_STOCK_PRICE, onPostStockPrice);
        mSocket.on(NOT_FOUND, onNotFound);
        mSocket.connect();
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: Connection Established");
                    sendStockName();
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: Disconnected");
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: Connection Error");
                }
            });
        }
    };

    public void sendStockName() {
        Log.d(TAG, "sendStockName: Sending request");
        mSocket.emit("sendStockName", "SCPH");
    }

    private Emitter.Listener onPostStockPrice = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String price;
                    String lastUpdated;
                    try {
                        price = data.getString(PRICE_KEY);
                        lastUpdated = data.getString(LAST_UPDATED_KEY);
                        Log.d(TAG, "run: Price: " + price + ", lastUpdated: " + lastUpdated);
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener onNotFound = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: Reached notFound");
                }
            });
        }
    };

    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(POST_STOCK_PRICE, onPostStockPrice);
    }
}