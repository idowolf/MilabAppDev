package com.milab.ido.socketioclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static com.milab.ido.socketioclient.Constants.LAST_UPDATED;
import static com.milab.ido.socketioclient.Constants.ON_CONNECTED;
import static com.milab.ido.socketioclient.Constants.ON_ERROR;
import static com.milab.ido.socketioclient.Constants.ON_RECEIVE_STOCK_PRICE;

public class MainActivityFragment extends Fragment implements SocketIOListener {
    private SocketIOHandler socketIOHandler;
    private boolean buttonToggled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        socketIOHandler = new SocketIOHandler(((SocketIOClientApplication)getActivity().getApplication()).getSocket(),
                this);
        final EditText stockNameBox = view.findViewById(R.id.stock_name_box);
        final Button toggleButton = view.findViewById(R.id.toggle_button);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonToggled) {
                    buttonToggled = false;
                    toggleButton.setText(R.string.button_start);
                    socketIOHandler.disconnect();
                } else {
                    buttonToggled = true;
                    socketIOHandler.connect();
                    toggleButton.setText(R.string.button_stop);
                    socketIOHandler.sendStockName(stockNameBox.getText().toString());
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socketIOHandler.destroy();
    }

    @Override
    public void onConnected(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                Log.d(TAG, ON_CONNECTED + message);
            }
        });
    }

    @Override
    public void onReceiveStock(final String price, final String lastUpdated) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), getString(R.string.message_price) + price, Toast.LENGTH_SHORT).show();
                Log.d(TAG, ON_RECEIVE_STOCK_PRICE + price + LAST_UPDATED + lastUpdated);
            }
        });
    }

    @Override
    public void onError(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                Log.e(TAG, ON_ERROR + message);
            }
        });
    }
}
