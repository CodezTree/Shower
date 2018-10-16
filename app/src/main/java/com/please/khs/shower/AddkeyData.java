package com.please.khs.shower;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

public class AddkeyData extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.키보드);
    }

   // @Override
    public void keydataCollect(String keyData){
        //데이터는 어떤 형식으로 받는거죠 EditText인가요..?아님...
        //만약 key값이 영어면 or 특수문자면 거른다
        //데이터가 일정 양 이상 모이면 서버로 방출
    }


    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                int requestnum = Character.getNumericValue(response.charAt(0));
                Log.d("test response", response);
                Log.d("test", String.valueOf(requestnum));

                if (requestnum == 5){

                }

            } catch (Exception e) {
                e.printStackTrace();  //에러 메시지 출력 logcat
            }
        }
    };
    //RegisterRequest registerRequest = new RegisterRequest(email, responseListener);
    //RequestQueue queue = Volley.newRequestQueue(AddkeyData.this);
      //              queue.add(registerRequest);
        //            Log.d("test","KeyData request!");
}
