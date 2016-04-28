package com.kyozipop.kyozipop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

public class KakaoSignupActivity extends AppCompatActivity {
    NetworkingWithServer mAuthTask = null;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                if(mAuthTask != null) return;

                String senMsg = "id=kakao:"+userProfile.getId()+"&user_name="+userProfile.getNickname();

                mAuthTask = (NetworkingWithServer) new NetworkingWithServer(new NetworkingWithServer.AsyncResponse() {
                    @Override
                    public void processFinish(String success) {
                        if (!success.equals("Fail")) {
                            mAuthTask = null;
                            startActivity(new Intent(KakaoSignupActivity.this, LoginCompleteActivity.class));
                            //Toast.makeText(LoginActivity.this,"로그인 성공",Toast.LENGTH_LONG);
                        } else {
                            mAuthTask = null;
                        }
                    }
                },senMsg,"sign_in.php").execute();
            }
        });
    }

    private void redirectMainActivity() {
        startActivity(new Intent(this, LoginCompleteActivity.class));
        finish();
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
