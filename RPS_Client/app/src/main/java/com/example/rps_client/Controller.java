package com.example.rps_client;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class Controller {
    private MainActivity activity;
    private TCPConnection connection;
    private ReceiveListener listener;

    private boolean connected = false;
    private boolean bound = false;

    private String IP_ADDRESS = "192.168.43.19";
    private int PORT = 7777;

    public Controller(MainActivity activity) {
        this.activity = activity;
        listener = new RL();
        connection = new TCPConnection(IP_ADDRESS, PORT, listener);
    }

    public void connect() {
        connection.connect();
    }

    public void disconnect() {
        if (connected) {
            connection.send("[D]");
            connection.disconnect();
        }
    }

    public void sendRequest(String communications) {
        connection.send(communications);
    }

    private class RL implements ReceiveListener {
        public void newMessage(final String answer) {
            Log.d(TAG, "newMessage: " + answer);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    String message = answer;
                    Exception e = connection.getException();
                    if ("CONNECTED".equals(answer)) {
                        activity.setConnectEnabled(false);
                        activity.setDisconnectEnabled(true);
                        connected = true;
                        activity.setResult(message);
                    }
                    else if ("CLOSED".equals(answer)) {
                        activity.setConnectEnabled(true);
                        activity.setDisconnectEnabled(false);
                        activity.setSendEnabled(false);
                        connected = false;
                        activity.setResult(message);
                    }
                    else if ("1".equals(answer)) {
                        activity.setTvPlayer("Player: 1");
                        activity.setPlayer(1);
                        activity.setSendEnabled(true);
                    }
                    else if ("2".equals(answer)) {
                        activity.setTvPlayer("Player: 2");
                    }
                    else if ("DRAW".equals(answer)) {
                        activity.setTvResult("It's a draw!");
                    }
                    else if ("LOSE".equals(answer)) {
                        activity.setTvResult("Sorry, you lost!");
                        activity.setScore(0,1);
                    }
                    else if ("WIN".equals(answer)) {
                        activity.setTvResult("Congratulations, you win!");
                        activity.setScore(1,0);
                    }
                    else if ("GO".equals(answer)) {
                        activity.setSendEnabled(true);
                    }
                    else if ("WAIT".equals(answer)) {
                        activity.setSendEnabled(false);
                    }
                    else if ("EXCEPTION".equals(answer) && e != null) {
                        message = e.toString();
                        activity.setResult(message);
                    }
                }
            });
        }
    }
}
