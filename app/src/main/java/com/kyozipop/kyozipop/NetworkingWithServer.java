package com.kyozipop.kyozipop;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.crypto.SecretKeyFactory;

/**
 * Created by jang on 2016-03-11.
 */
public class NetworkingWithServer extends AsyncTask<Void, Void, String> {

    private String url = "http://kyozipop.kr/mobile/";
    private String loginCheck = "";
    private String sendPostMsg = "";

    public interface AsyncResponse{
        void processFinish(String success);
    }

    public AsyncResponse delegate = null;

    public NetworkingWithServer(AsyncResponse delegate,String sendPostMsg,String php_src) {
        this.delegate = delegate;
        this.sendPostMsg = sendPostMsg;
        url += php_src;
    }
    public void setUrl(String url){
        this.url = url;
    }
    protected String login() {
        try {
            SecretKeyFactory keyfac = null;

            URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setConnectTimeout(1000);
            huc.setReadTimeout(3000);

            huc.setRequestMethod("POST");
            huc.setDoInput(true);
            huc.setDoOutput(true);
            huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            huc.setUseCaches(false);
            huc.setDefaultUseCaches(false);

            OutputStream os = huc.getOutputStream();

            os.write(sendPostMsg.getBytes());
            os.flush();

            os.close();

            if (huc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));

                while (true) {
                    String line = br.readLine();
                    if (line == null) break;
                    return line;
                }
                br.close();
            }

            huc.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "Connection Failed";
    }

    @Override
    protected String doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.
        try {
            loginCheck = login();          // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return loginCheck;
        // TODO: register the new account here.
    }

    @Override
    protected void onPostExecute(final String success) {
        delegate.processFinish(success);
    }

    @Override
    protected void onCancelled() {
    }
}


