package com.please.khs.shower.SignUpActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.please.khs.shower.R;
import com.please.khs.shower.Request.RegisterRequest;

public class SignUp2Activity extends AppCompatActivity {
    private String email;
    private AlertDialog dialog;
    private long time = 0;
    boolean canAttemptRegister = true;

    EditText et_id, et_pw, et_pw_chk;
    String Email, Pw, Pw_chk;
    Button signInButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up2);

        et_id = findViewById(R.id.Email);
        et_pw = findViewById(R.id.password);
        et_pw_chk = findViewById(R.id.passwordCheck);
        signInButt = findViewById(R.id.signInButton);

        signInButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canAttemptRegister) {
                    email = et_id.getText().toString();
                    Pw = et_pw.getText().toString();
                    Pw_chk = et_pw_chk.getText().toString();

                    canAttemptRegister = false;

                    Log.d("val", email + Pw + Pw_chk);

                    if (email.equals("") || Pw.equals("") || Pw_chk.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp2Activity.this);
                        dialog = builder.setMessage("빈 칸 없이 입력해주세요.").setPositiveButton("확인", null).create();
                        canAttemptRegister = true;
                        dialog.show();
                    } else {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //JSONObject jsonResponse = new JSONObject(response);
                                    //int success = jsonResponse.getInt("success");
                                    String success = response.toString();
                                    Log.d("test response", response);
                                    Log.d("test", String.valueOf(success));

                                    if (success.equals("100")) {
                                        canAttemptRegister = true;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp2Activity.this);
                                        savePreferencesString("Email", email);
                                        savePreferencesString("Password",Pw);

                                        Log.d("test : ", "login "+email + Pw);
                                        builder.setMessage("성공적으로 회원가입 되었습니다.");
                                        builder.setCancelable(false);//바깥 클릭해도 꺼지지 않음
                                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                        dialog = builder.create();
                                        dialog.show();

                                    } else if (success.equals("3")) {
                                        canAttemptRegister = true;
                                        Toast.makeText(SignUp2Activity.this, "Password가 일치하는지 확인해주세요", Toast.LENGTH_SHORT).show();

                                    } else if (success.equals("2")) {
                                        canAttemptRegister = true;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp2Activity.this);
                                        dialog = builder.setMessage("이미 존재하는 아이디 입니다.").setPositiveButton("확인", null).create();
                                        dialog.show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();  //에러 메시지 출력 logcat
                                }
                            }
                        };
                        RegisterRequest registerRequest = new RegisterRequest(email, Pw, Pw_chk, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(SignUp2Activity.this);
                        queue.add(registerRequest);
                        Log.d("test", "request !");
                    }
                }
            }
        });
    }
    @Override
    protected void onStop(){
        super.onStop();
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - time >= 2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 돌아갑니다.",Toast.LENGTH_SHORT).show();
        } else if(System.currentTimeMillis() - time < 2000){
            finish();
        }
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

}