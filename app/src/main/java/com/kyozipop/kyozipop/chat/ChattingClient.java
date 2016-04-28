package com.kyozipop.kyozipop.chat;

import android.app.AlertDialog;
import android.os.Message;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.logging.LogRecord;

/**
 * Created by jang on 2016-03-25.
 */
public class ChattingClient extends Thread {
    Socket socket = null;
    String client_name;
    ClientSender sender;
    Handler mHandler;

    public ChattingClient(String client_name,Handler handler){
        this.client_name = client_name;
        mHandler = handler;
    }
    public void sendMsgToServer(String msg){
        if(sender != null)
            sender.send(msg);
        else
            Log.v("kyozipop","sender is null..");
    }

    public void closeSocket(){
        try {
            if (socket != null)
                socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        super.run();
        try {
            String serverIp = "192.168.43.45";
            socket = new Socket(serverIp, 7777); // 소켓을 생성하여 연결을 요청한다.
            socket.setSoTimeout(5000);

            // 메시지 전송용 Thread 생성
            sender = new ClientSender(socket,client_name);
            // 메시지 수신용 Thread 생성
            Thread receiver = new Thread(new ClientReceiver(socket));

            sender.start();
            receiver.start();
        } catch (ConnectException ce) {
            ce.printStackTrace();
            Log.v("kyozipop","connection err"); //여기에 서버에 연결할 수 없습니다. 인터넷 연결상태를 확인해주세요 출력
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("kyozipop", "other err");
        }
    }
    public class ClientSender extends Thread{
        Socket socket;
        DataOutputStream out;
        String name;

        ClientSender(Socket socket,String name) {
            this.socket = socket;
            this.name = name;

            try {
                this.out = new DataOutputStream(socket.getOutputStream());

                // 시작하자 마자, 자신의 대화명을 서버로 전송
                if (out != null) {
                    out.writeUTF(name);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void send(String message) {
            if (out != null) {
                try {
                    // 키보드로 입력받은 데이터를 서버로 전송
                    out.writeUTF("[" + name + "] " + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 메시지 수신용 Thread
    class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream in;

        // 생성자
        ClientReceiver(Socket socket) {
            this.socket = socket;

            try {
                // 서버로 부터 데이터를 받을 수 있도록 DataInputStream 생성
                this.in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (in != null) {
                try {
                    // 서버로 부터 전송되는 데이터를 출력
                    String newString = in.readUTF();
                    Message msg = mHandler.obtainMessage();
                    msg.obj = newString;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } // end run

    }//end ClientReceiver
}
