package com.milab.ido.socketioclient;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import static com.milab.ido.socketioclient.Constants.CANNOT_PARSE_SERVER_RESULTS;
import static com.milab.ido.socketioclient.Constants.CONNECTION_ENDED;
import static com.milab.ido.socketioclient.Constants.CONNECTION_ERROR;
import static com.milab.ido.socketioclient.Constants.CONNECTION_ESTABLISHED;
import static com.milab.ido.socketioclient.Constants.LAST_UPDATED_KEY;
import static com.milab.ido.socketioclient.Constants.NOT_FOUND;
import static com.milab.ido.socketioclient.Constants.POST_STOCK_PRICE;
import static com.milab.ido.socketioclient.Constants.PRICE_KEY;
import static com.milab.ido.socketioclient.Constants.SEND_STOCK_NAME;
import static com.milab.ido.socketioclient.Constants.STOCK_NOT_FOUND;

public class SocketIOHandler {
    private Socket mSocket;
    private SocketIOListener mListener;

    /**
     * Constructor for the class
     * @param socket The application socket bound to the server address
     * @param listener Fragment listener for comm results
     */
    public SocketIOHandler(Socket socket, SocketIOListener listener) {
        mSocket = socket;
        mListener = listener;
        setSocketListeners(mSocket);
    }

    /**
     * Set listeners for all server related events
     * @param socket The defined server socket
     */
    public void setSocketListeners(Socket socket) {
        socket.on(Socket.EVENT_CONNECT,onConnect);
        socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        socket.on(NOT_FOUND, onNotFound);
    }

    /**
     * Emit the given stockName String to the server in upper case
     * @param stockName
     */
    public void sendStockName(String stockName) {
        mSocket.emit(SEND_STOCK_NAME, stockName.toUpperCase());
    }

    /**
     * Event for socket connection init
     */
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mListener.onConnected(CONNECTION_ESTABLISHED);
        }
    };

    /**
     * Event for socket disconnection
     */
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mListener.onError(CONNECTION_ENDED);
        }
    };

    /**
     * Event for connection error
     */
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mListener.onError(CONNECTION_ERROR);
        }
    };

    /**
     * Event for processing request result
     */
    private Emitter.Listener onPostStockPrice = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            String price;
            String lastUpdated;
            try {
                price = data.getString(PRICE_KEY);
                lastUpdated = data.getString(LAST_UPDATED_KEY);
                mListener.onReceiveStock(price, lastUpdated);
            } catch (JSONException e) {
                mListener.onError(CANNOT_PARSE_SERVER_RESULTS);
                return;
            }
        }
    };

    /**
     * Event for processing a request result where stock does not exist
     */
    private Emitter.Listener onNotFound = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mListener.onError(STOCK_NOT_FOUND);
        }
    };

    /**
     * Disconnect the socket and turn off custom event listener on listener destroy
     * Note: Not shutting off connect/disconnect/error listeners, as is demonstrated in the examples.
     */
    public void destroy() {
        mSocket.disconnect();
        mSocket.off(POST_STOCK_PRICE, onPostStockPrice);
    }

    /**
     * Stop listening to a post stock price (to make sure client won't handle incoming data)
     * and disconnect the socket
     */
    public void disconnect() {
        mSocket.off(POST_STOCK_PRICE, onPostStockPrice);
        mSocket.disconnect();
    }

    /**
     * Start listening to post stock price events and connect the socket
     */
    public void connect() {
        mSocket.on(POST_STOCK_PRICE, onPostStockPrice);
        mSocket.connect();
    }
}
