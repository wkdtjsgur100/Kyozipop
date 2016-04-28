package com.kyozipop.kyozipop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_complete);

        Button bt_naver_logout = (Button)findViewById(R.id.naver_logout);

        bt_naver_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTokenTask dtt = new DeleteTokenTask(LoginCompleteActivity.this);
                dtt.execute();
                startActivity(new Intent(LoginCompleteActivity.this, LoginActivity.class));
            }
        });


        Button chat_btn = (Button)findViewById(R.id.chat_btn);

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginCompleteActivity.this, ChattingActivity.class));
                finish();
            }
        });
    }
}
