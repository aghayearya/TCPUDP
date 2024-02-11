package com.example.tessst;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private TextView Message;
    private EditText editTextText, editTextText2;
    private Button button;
    private String ServerName;
    private int ServerPort;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Message = findViewById(R.id.Message);
        editTextText = findViewById(R.id.editTextText);
        editTextText2 = findViewById(R.id.editTextText2);
        button = findViewById(R.id.button);

    }
    public void onClickconnect(View view){
    ServerName = editTextText.getText().toString();
    ServerPort = Integer.valueOf(editTextText2.getText().toString());

    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Socket socket = new Socket(ServerName,ServerPort);

                BufferedReader br_input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String txtFromServer = br_input.readLine();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Message.setText(txtFromServer);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }).start();
    }
}