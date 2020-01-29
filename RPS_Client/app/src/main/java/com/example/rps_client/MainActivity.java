package com.example.rps_client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private Controller controller;

    private TextView tvResponse, tvChoice, tvPlayer, tvResult, tvScore;
    private Button buttonConnect, buttonDisconnect, buttonSend;
    private ImageView imgRock, imgPaper, imgScissor;

    private String message;
    private int s1 = 0, s2 = 0, player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        controller = new Controller(this);

        registerListeners();
    }

    public void initComponents(){
        buttonSend = (Button) findViewById(R.id.sendButton);
        setSendEnabled(false);
        buttonConnect = (Button) findViewById(R.id.connectButton);
        buttonDisconnect = (Button) findViewById(R.id.disconnectButton);
        setDisconnectEnabled(false);

        tvResponse = (TextView) findViewById(R.id.responseTextView);
        tvChoice = (TextView) findViewById(R.id.selectedChoice);
        tvPlayer = (TextView) findViewById(R.id.tvPlayer);
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvScore = (TextView) findViewById(R.id.tvScore);

        imgRock = (ImageView) findViewById(R.id.rock);
        imgPaper = (ImageView) findViewById(R.id.paper);
        imgScissor = (ImageView) findViewById(R.id.scissor);
    }

    public void registerListeners(){
        buttonConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                controller.connect();
            }
        });

        buttonDisconnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                controller.disconnect();
            }
        });

        buttonSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (message){
                    case "Rock":
                        message = "[R]";
                        break;
                    case "Paper":
                        message = "[P]";
                        break;
                    case "Scissor":
                        message = "[S]";
                        break;
                }
                controller.sendRequest(message);
            }
        });

        imgRock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChoice("Rock");
            }
        });

        imgPaper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChoice("Paper");
            }
        });

        imgScissor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChoice("Scissor");
            }
        });

    }

    public void setScore(int p1, int p2) {
        String text = "";
        s1 += p1;
        s2 += p2;

        if (player == 1 ){
            text = "Player 1: " + s1 + "\n" + "Player 2: " + s2;
        }
        else {
            text = "Player 1: " + s2 + "\n" + "Player 2: " + s1;
        }

        tvScore.setText(text);
    }

    public void setTvResult(String result){
        tvResult.setText(result);
    }

    public void setChoice(String choice) {
        String out = choice + " is selected.";
        tvChoice.setText(out);
        message = choice;
    }

    public void setTvPlayer(String text) {
        tvPlayer.setText(text);
    }

    public void setSendEnabled(boolean b) {
        buttonSend.setEnabled(b);
    }

    public void setDisconnectEnabled(boolean b) {
        buttonDisconnect.setEnabled(b);
    }

    public void setConnectEnabled(boolean b) {
        buttonConnect.setEnabled(b);
    }

    public void setResult(String message){
//        String m = (String) tvResponse.getText();
//        m = m + "\n" + message;
        tvResponse.setText(message);
    }

    public void setPlayer(int i){
        this.player = i;
    }
}