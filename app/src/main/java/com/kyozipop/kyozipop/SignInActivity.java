package com.kyozipop.kyozipop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {
    private NetworkingWithServer mNetworkingSync = null;
    private View mProgressView = null;
    private View mLoginFormView = null;
    public EditText et_email = null;
    NetworkingWithServer mAuthTask = null;
    public EditText mAuthInputNumber = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button prove_but = (Button)findViewById(R.id.prove_button);
        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.sign_in_form);
        mAuthInputNumber = (EditText)findViewById(R.id.auth_number);
        Button auth_btn = (Button)findViewById(R.id.submit_auth_number);

        auth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = CookieManager.getInstance().getCookie("http://kyozipop.kr");
                System.out.println(CookieManager.getInstance().hasCookies());
            }
        });

        prove_but.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                et_email = (EditText) findViewById(R.id.email_address);
                String s = et_email.getText().toString();
                String user_name = ((EditText) findViewById(R.id.user_name)).getText().toString();

                String new_pwd = ((EditText) findViewById(R.id.new_password)).getText().toString();
                EditText new_check_password = (EditText) findViewById(R.id.new_check_password);
                String new_pwd_chk = new_check_password.getText().toString();

                String phone_number = ((EditText) findViewById(R.id.phone_number)).getText().toString();

                if (!emailCheck(s)) {
                    et_email.setError("이메일 형식이 올바르지 않습니다");
                    et_email.requestFocus();
                    return;
                }
                if (!new_pwd.equals(new_pwd_chk)) {
                    new_check_password.setError("비밀번호가 일치하지 않습니다.");
                    new_check_password.requestFocus();
                    return;
                }
                if(mAuthTask != null) return;

                String senMsg = "phone_number="+phone_number;

                mAuthTask = (NetworkingWithServer) new NetworkingWithServer(new NetworkingWithServer.AsyncResponse() {
                    @Override
                    public void processFinish(String success) {
                        if (!success.equals("Fail")) {
                            mAuthTask = null;
                            System.out.println(success);
                            //Toast.makeText(LoginActivity.this,"로그인 성공",Toast.LENGTH_LONG);
                        } else {
                            mAuthTask = null;
                        }
                    }
                },senMsg,"send_msg.php").execute();
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
            private void showProgress(final boolean show) {
                // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
                // for very easy animations. If available, use these APIs to fade-in
                // the progress spinner.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    mProgressView.animate().setDuration(shortAnimTime).alpha(
                            show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
                } else {
                    // The ViewPropertyAnimator APIs are not available, so simply show
                    // and hide the relevant UI components.
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            }
            public Boolean emailCheck(String s){
                if(s.contains("@"))
                    return true;
                else
                    return false;
            }
        });
    }
}
