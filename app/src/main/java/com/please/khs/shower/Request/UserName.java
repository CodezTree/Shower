package com.please.khs.shower.Request;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.please.khs.shower.Fragment_main;
import com.please.khs.shower.R;

public class UserName extends AppCompatActivity {
    private long time = 0;
    private String NickName;
    private String Email;
    EditText nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        nameText = findViewById(R.id.nameEditText);
    }

    private String getPreferencesString(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    private void savePreferencesString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void onClick(View view) {//닉네임을 서버로 보내는 리퀘스트 추가
        if (view.getId() == R.id.nextButton) {
            Log.d("test","getNickName");
            NickName = nameText.getText().toString();

            savePreferencesString("NickName", NickName);
            SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE);
            Email = pref.getString("Email", "");

            if (NickName.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(UserName.this);
                AlertDialog dialog = builder.setMessage("빈 칸 없이 입력해주세요.").setPositiveButton("확인", null).create();
                dialog.show();
            }else {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String namekey = response;
                            Log.d("test response", response);
                            Log.d("test", String.valueOf(namekey));

                            if (namekey.equals("100")) {//닉네임 리퀘스트
                                Toast.makeText(UserName.this, "반가워요"+" "+NickName+" 님!", Toast.LENGTH_SHORT).show();//!!!!!닉네임띄워주기!!
                                Intent intent = new Intent(UserName.this, Fragment_main.class);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();  //에러 메시지 출력 logcat
                        }
                    }
                };
                Log.d("test : ", "email: "+Email+"nickname: "+NickName);
                UserNameRequest userNameRequest = new UserNameRequest(Email, NickName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UserName.this);
                queue.add(userNameRequest);
                Log.d("test", "namekey request!");
            }
        }
    }

    @Override
    public void onBackPressed(){
        /*if(System.currentTimeMillis() - time >= 2000){
            time=System.currentTimeMillis();
            finish();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis() - time < 2000){
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }*/
    }

}
