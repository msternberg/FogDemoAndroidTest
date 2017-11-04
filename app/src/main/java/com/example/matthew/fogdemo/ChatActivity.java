package com.example.matthew.fogdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.matthew.fogdemo.messages.Message;
import com.example.matthew.fogdemo.messages.MessageInfo;
import com.example.matthew.fogdemo.messages.TextMessage;
import com.example.matthew.fogdemo.networkThreads.FogIPThread;
import com.example.matthew.fogdemo.networkThreads.ReceiveMessagesThread;
import com.example.matthew.fogdemo.networkThreads.SendMessagesThread;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mMessagesView;
    private EditText mInputMessageView;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;


    //////////////////////////////////////////////////////////////

    private static final String FOG_IP = "128.61.126.81";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAdapter = new MessageAdapter(getApplicationContext(), mMessages);

        mMessagesView = (RecyclerView) findViewById(R.id.messages);
        mMessagesView.setLayoutManager(new LinearLayoutManager(this));
        mMessagesView.setAdapter(mAdapter);

        mInputMessageView = (EditText) findViewById(R.id.message_input);

        ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });

        Log.d("MESSAGES THREAD", "CREATING MESSAGES THREAD");
        SendMessagesThread t = new SendMessagesThread(FOG_IP);
        t.start();
        Log.d("MESSAGES THREAD", "RUNNING MESSAGES THREAD");


        Log.d("FOG IP THREAD", "STARTING FOG IP THREAD");
        FogIPThread fogThread = new FogIPThread();
        fogThread.start();

        Log.d("RECEIVER THREAD", "STARTING RECEIVER THREAD");
        ReceiveMessagesThread receiveThread = new ReceiveMessagesThread(this);
        receiveThread.start();

        sendEnterMessage();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_leave) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Called by ReceiveMessagesThread when a text message has been received
    // TextMessage is of the form TEXT (username) (message)
    public void onReceiveTextMessage(String message) {
        TextMessage m = TextMessage.parseMessage(message);
        // Display the new message on the screen
        addMessage(m.getUsername(), m.getText(), false);
    }

    // Called by ReceivedMessagesThread when user number message has been received
    // TextMessage is of the form USERS (number)
    public void onReceiveUserNumbersMessage(String message) {
        message = message.substring(6);
        int num = Integer.parseInt(message);
        String contents = String.format("# of users is %d", num);
        addMessage("Chatbot", contents, false);
    }

    /////////////////////////////////////////////////////////////////////////
    public void sendEnterMessage() {
        String username = SessionInfo.getInstance().getUsername();
        String protocolForm = MessageInfo.createEnterMessageString(username);
        MessageQueue.getInstance().addMessage(protocolForm);
    }

    //Sent when the user leaves the chat
    public void sendLeaveMessage() {
        String username = SessionInfo.getInstance().getUsername();
        String protocolForm = MessageInfo.createLeaveMessage(username);
        MessageQueue.getInstance().addMessage(protocolForm);
    }

    public void attemptSend() {
        if (mInputMessageView.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Messages can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String username = SessionInfo.getInstance().getUsername();
        String message = mInputMessageView.getText().toString().trim();
        mInputMessageView.setText("");
        addMessage(username, message, true);
        String protocolForm = MessageInfo.createTextMessageString(username, message);
        MessageQueue.getInstance().addMessage(protocolForm);
    }


    // Adds a text message to the screen
    // If sending == false, then we shouldn't scroll to the bottom of the page
    private void addMessage(String username, String message, boolean sending) {
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(username).message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        if (!sending) {
            Toast.makeText(getApplicationContext(), "New Message", Toast.LENGTH_SHORT).show();
        } else {
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    protected void onDestroy() {
        sendLeaveMessage();
        Toast.makeText(getApplicationContext(), "USER DESTROYED", Toast.LENGTH_SHORT).show();
        SessionInfo.getInstance().setUsername(null);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        sendLeaveMessage();
        Toast.makeText(getApplicationContext(),  "LEAVING CHAT", Toast.LENGTH_SHORT).show();
        SessionInfo.getInstance().setUsername(null);
        super.onBackPressed();
    }
}