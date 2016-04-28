package com.kyozipop.kyozipop;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kyozipop.kyozipop.chat.ChattingClient;
import com.kyozipop.kyozipop.db.DBManager;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * // 실행 방법 // java MultichatGUIClient 대화명 
 *
 * @since 2012. 07. 11. 
 * @author yanggun7201
 */
public class ChattingActivity extends Activity {
    // ==========================p=====
    public TextView chat_field;
    public EditText etNickName;
    public ChattingClient chattingClient;
    public ChatHandler mainHandler;
    public DBManager dbManager;
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    Bundle bun;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        chat_field = (TextView)findViewById(R.id.chat_field);
        etNickName = (EditText)findViewById(R.id.chat_nickname);

        mainHandler = new ChatHandler();

        chattingClient = new ChattingClient(etNickName.getText().toString(),mainHandler);
        chattingClient.start();

        final EditText etSendMsg = (EditText)findViewById(R.id.chat_send_msg);
        etSendMsg.requestFocus();

        chat_field.append("\n");

        Button submitBtn = (Button)findViewById(R.id.submit_chat_msg);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chattingClient.sendMsgToServer(etSendMsg.getText().toString());
                etSendMsg.setText("");
            }
        });

    }
    class ChatHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ////////서버에서 메세지를 받았을 때의 콜백내용
            chat_field.append((String) msg.obj + "\n");
        }
    }
}
//
