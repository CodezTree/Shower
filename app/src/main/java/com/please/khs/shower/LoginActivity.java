package com.please.khs.shower;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
//import com.example.khs.shower.Main.GraphActivity;
import com.please.khs.shower.Main.GraphActivity;
import com.please.khs.shower.Request.UserName;
import com.please.khs.shower.SignUpActivity.SignUp1Activity;

import android.content.SharedPreferences;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private long time = 0;
    private String email_key, password_key;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText id = findViewById(R.id.ID_text);
        final EditText pw = findViewById(R.id.PW_text);
        Button Signupbutton = findViewById(R.id.signup_button);
        Button LoginButton = findViewById(R.id.Login_button);



        //pw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

//        Shared -> 아이디 패스워드 존재하는지 확인.
//                존재 함. 그러면 바로 -> 로그인 시도 -> email, password -> sharedPreferences
//
//                존재 X -> 그러면 로그인 버튼 -> email, password -> EditText

        if (getPreferencesString("AppFirstTime") == null) {
            savePreferencesString("AppFirstTime", "true");
        }

        Signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUp1Activity.class);
                startActivity(intent);
            }
        });


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPreferencesString("Email")==null) {//처음 로그인할 때에는 아무것도 없음
                    String Email = id.getText().toString();
                    String Password = pw.getText().toString();
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //JSONObject jsonResponse = new JSONObject(response);
                                //int success = jsonResponse.getInt("success");
                                int login = Character.getNumericValue(response.charAt(0));
                                Log.d("test", "login response" + response);
                                Log.d("test", "login response" +String.valueOf(login));

                                if (login == 4) {
                                    Toast.makeText(LoginActivity.this, "성공적으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                    savePreferencesString("AppFirstTime", "false");
                                    savePreferencesString("Email", id.getText().toString());
                                    savePreferencesString("Password", pw.getText().toString());

                                    Intent intent = new Intent(LoginActivity.this,Rainny.class);
                                    startActivity(intent);
                                } else if (login == 5) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    dialog = builder.setMessage("존재하지 않는 아이디입니다.").setPositiveButton("확인", null).create();
                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();  //에러 메시지 출력 logcat
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(Email, Password, responseListener);
                    RequestQueue loginqueue = Volley.newRequestQueue(LoginActivity.this);
                    loginqueue.add(loginRequest);
                    Log.d("test", "request !" + Email + "  " + Password);
                }
            }
        });
    }

    //앱에 로그인정보 저장
    private String getPreferencesString(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    private void savePreferencesString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    @Override
    public void onResume() {
        super.onResume();

        // ID 가 이미 존재하고 저장되어있습니다.
        if (getPreferencesString("Email")!=null) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        //JSONObject jsonResponse = new JSONObject(response);
                        //int success = jsonResponse.getInt("success");
                        int login = Character.getNumericValue(response.charAt(0));
                        Log.d("login response", response);
                        Log.d("login", String.valueOf(login));

                        if (login == 4) {
                            if (getPreferencesString("AppFirstTime").equals("true")) {
                                savePreferencesString("AppFirstTime", "false");
                                Intent intent = new Intent(LoginActivity.this, Rainny.class);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "성공적으로 로그인되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Intent intent = new Intent(LoginActivity.this, GraphActivity.class);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "로그인되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();  //에러 메시지 출력 logcat
                    }
                }
            };
            Log.d("test", " Email + Ps " + getPreferencesString("Email") + getPreferencesString("Password"));
            LoginRequest loginRequest = new LoginRequest(getPreferencesString("Email"), getPreferencesString("Password"), responseListener);
            RequestQueue loginqueue = Volley.newRequestQueue(LoginActivity.this);
            loginqueue.add(loginRequest);
            Log.d("test", "request !");
        }
    }


    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - time >= 2000) {
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis() - time < 2000){
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }
}

