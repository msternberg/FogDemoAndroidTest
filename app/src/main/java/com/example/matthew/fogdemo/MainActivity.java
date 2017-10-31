package com.example.matthew.fogdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> messageList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private static final String FOG_IP = "123.123.123.123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView discoveryView =  (ListView) findViewById(R.id.discoverylist);
        String[] messageValues = new String[] {
          "Hi", "How you doin?", "Bye"
        };
        messageList.addAll(Arrays.asList(messageValues));
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.simplerow, messageList);
        discoveryView.setAdapter(adapter);

        Log.d("MESSAGES THREAD", "CREATING MESSAGES THREAD");
        SendMessagesThread t = new SendMessagesThread(FOG_IP);
        t.start();
        Log.d("MESSAGES THREAD", "RUNNING MESSAGES THREAD");
        for (String s : messageValues) {
            MessageQueue.getInstance().addMessage(s);
        }
        Log.d("FOG IP THREAD", "STARTING FOG IP THREAD");
        FogIPThread fogThread = new FogIPThread();
        fogThread.start();
    }

    private void addIPtoList(String ip) {
        adapter.add(ip);
        Log.d("ADDED", messageList.get(messageList.size() - 1));
    }

//    private class Client extends AsyncTask<String, Void, ArrayList<String>> {
//
//        @Override
//        protected ArrayList<String> doInBackground(String... params) {
////            try {
////                InetAddress serverAddr = InetAddress.getByName(params[0]);
////                Log.d("ServerAddress", serverAddr.getHostName());
////                Socket clientSocket = new Socket(serverAddr, SERVERPORT);
////                Log.d("CLIENT SOCKET", "" + clientSocket.isConnected());
////                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
////
////                ArrayList<String> ips = new ArrayList<>();
////                String message = bufferedReader.readLine();
////                Log.d("ConnectionMessage", message);
////                while (!message.equals("FIN")) {
////                    Log.d("MESSAGE", message);
////                    ips.add(message);
////                    message = bufferedReader.readLine();
////                } while (!message.equals("FIN"));
////
////                return ips;
////
////            } catch (IOException e) {
////                e.printStackTrace();
////                Log.d("Connection Failure", "Socket connection isn't working");
////                return null;
////            }
//            return new ArrayList<>();
//        }
//
//    }
}
