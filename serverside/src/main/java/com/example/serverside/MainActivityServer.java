package com.example.serverside;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivityServer extends AppCompatActivity {

    private TextView tvServerName, tvServerPort, tvStatus;
    private String serverIP= "10.0.2.15";
    private int serverPort= 2222;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_server);

        tvServerName = findViewById(R.id.tvServerName);
        tvServerPort = findViewById(R.id.tvServerPort);
        tvStatus = findViewById(R.id.tvStatus);
        tvServerName.setText(serverIP);
        tvServerPort.setText(String.valueOf(serverPort));
    }
    private ServerThread serverThread;
    public void onClickStartServer (View view) {
        serverThread = new ServerThread();
        serverThread.startServer();

    }
    public void onClickStopServer (View view) {
        serverThread.stopServer();

    }
    class ServerThread extends Thread implements Runnable {

        private boolean serverRunning;
        private ServerSocket serverSocket;
        private int count=0;
        public void startServer(){
            serverRunning=true;
            start();
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(serverPort);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvStatus.setText("Waiting for client");

                    }
                });

                while (serverRunning){

                    Socket socket = serverSocket.accept();
                    count++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvStatus.setText("Connected to:" + socket.getInetAddress()+ " : " + socket.getLocalPort());
                        }
                    });
                    PrintWriter output_Server = new PrintWriter(socket.getOutputStream());
                    output_Server.write("Welcome to Server:" + count);
                    output_Server.flush();
                    socket.close();

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
        public void stopServer(){
            serverRunning=false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (serverSocket!=null){
                        try {
                            serverSocket.close();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvStatus.setText("Server stopped");
                                }
                            });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }

                }
            }).start();
        }
    }
}