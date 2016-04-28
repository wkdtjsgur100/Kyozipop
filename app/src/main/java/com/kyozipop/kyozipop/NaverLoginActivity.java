package com.kyozipop.kyozipop;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.crypto.SecretKeyFactory;

public class NaverLoginActivity extends AppCompatActivity {
    static public OAuthLogin mOAuthLoginModule;
    public OAuthLoginHandler mOAuthLoginHandler;
    public Context mContext;

    public class RequestApiTask extends AsyncTask<Void, Void, String> {

        public NetworkingWithServer mNetwork = null;
        @Override
        protected String doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = mOAuthLoginModule.getAccessToken(mContext);
            String s = mOAuthLoginModule.requestApi(mContext, at, url);

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonResponse = new JSONObject(jsonObject.getString("response"));

                String senMsg = "id=naver:"+jsonResponse.getString("id")+"&user_name="+jsonResponse.getString("name");

                if(mNetwork != null) return;

                mNetwork = (NetworkingWithServer) new NetworkingWithServer(new NetworkingWithServer.AsyncResponse() {
                    @Override
                    public void processFinish(String success) {
                        if (!success.equals("Fail")) {
                            mNetwork = null;
                            startActivity(new Intent(NaverLoginActivity.this, LoginCompleteActivity.class));
                            //Toast.makeText(LoginActivity.this,"로그인 성공",Toast.LENGTH_LONG);
                        } else {
                            mNetwork = null;
                        }
                    }
                },senMsg,"sign_in.php").execute();
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_login);

        mContext = this;

        mOAuthLoginModule = OAuthLogin.getInstance();

        mOAuthLoginModule.init(
                this
                , "MOKFJN0mTBYNQwt9DVrB"
                , "ek5rc8isz9"
                , "교집합"
        );
        mOAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    /////////////////////네이버 로그인 성공 시
                    // Toast.makeText(mContext, "errorCode:" + mOAuthLoginModule.getState(mContext).name(),Toast.LENGTH_LONG).show();
                    new RequestApiTask().execute();
                } else {
                    String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                    String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                    Toast.makeText(mContext, "errorCode:" + errorCode
                            + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            }
        };
        mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler);
    }
}
