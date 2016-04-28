package com.kyozipop.kyozipop;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by jang on 2016-03-16.
 */
public class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
    private Context mContext = null;

    public DeleteTokenTask(Context context)
    {
        mContext = context;
    }
    @Override
    protected Void doInBackground(Void... params) {
        boolean isSuccessDeleteToken = NaverLoginActivity.mOAuthLoginModule.logoutAndDeleteToken(mContext);

        if (!isSuccessDeleteToken) {
            // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
            // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
            System.out.println("errorCode:" + NaverLoginActivity.mOAuthLoginModule.getLastErrorCode(mContext));
            System.out.println("errorDesc:" + NaverLoginActivity.mOAuthLoginModule.getLastErrorDesc(mContext));
        }

        return null;
    }

    protected void onPostExecute(Void v) {

    }
}