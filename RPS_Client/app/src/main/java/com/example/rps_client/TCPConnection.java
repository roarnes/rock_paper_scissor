package com.example.rps_client;

import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static android.content.ContentValues.TAG;

public class TCPConnection {

    private RunOnThread thread;
    private Receive receive;
    private ReceiveListener listener;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private InetAddress address;
    private int connectionPort;
    private String ip;
    private Exception exception;
    private boolean connected = false;

    public TCPConnection(String ip, int connectionPort, ReceiveListener listener) {
        this.ip = ip;
        this.connectionPort = connectionPort;
        this.listener = listener;
        thread = new RunOnThread();
    }

    public void connect() {
        thread.start();
        thread.execute(new Connect());
    }

    public void disconnect() {
        thread.execute(new Disconnect());
        thread.stop();
    }

    public void send(String communications) {
        if (!connected) {
            thread.execute(new Connect());
        }
        thread.execute(new Send(communications));    }

    public Exception getException() {
        Exception result = exception;
        exception = null;
        return result;
    }

    private class Receive extends Thread {
        public void run() {
            String result = "";
            try {
                while (receive != null) {
                    result += ((char) (input.readByte()));
                    Log.d(TAG, "result: " + result);
                    if (result.toString().equals("CONNECTED") || result.toString().equals("CLOSED") ||
                            result.toString().equals("1") || result.toString().equals("2")||
                            result.toString().equals("WIN") || result.toString().equals("LOSE") ||
                            result.toString().equals("DRAW") || result.toString().equals("GO") ||
                            result.toString().equals("WAIT")){
                        listener.newMessage(result.toString());
                        result = "";
                    }
                }
            } catch (final Exception e) { // IOException, ClassNotFoundException
                receive = null;
                listener.newMessage("EXCEPTION");
            }

        }
    }

    private class Connect implements Runnable {
        public void run() {
            try {
                Log.d("TCPConnection","Connect-run");
                address = InetAddress.getByName(ip);
                Log.d("TCPConnection-Connect","Creating socket");
                socket = new Socket(address, connectionPort);
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                output.flush();
                Log.d("TCPConnection-Connect","Stream ready");
                receive = new Receive();
                receive.start();
                connected = true;
            } catch (Exception e) { // SocketException, UnknownHostException
                Log.d("TCPConnection-Connect",e.toString());
                exception = e;
                listener.newMessage("EXCEPTION");
            }
        }
    }

    public class Disconnect implements Runnable {
        public void run() {
            try {
                if (socket != null)
                    socket.close();
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
                thread.stop();
            } catch(IOException e) {
                exception = e;
                listener.newMessage("EXCEPTION");
            }
        }
    }

    public class Send implements Runnable {
        private String communications;

        public Send(String communications) {
            this.communications = communications;
        }

        public void run() {
            try {
                Log.d("Send message",communications);
                output.writeUTF(communications);
                output.flush();
            } catch (IOException e) {
                exception = e;
                listener.newMessage("EXCEPTION");
            }
        }
    }

}
