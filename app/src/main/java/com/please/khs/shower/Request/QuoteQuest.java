package com.please.khs.shower.Request;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.please.khs.shower.Fragment_main;
import com.please.khs.shower.R;

public class QuoteQuest extends AppCompatActivity {
    private long time = 0;
    private String UserQuote;
    private String Email;
    EditText quoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_quote);

        quoteText = findViewById(R.id.et_quote);
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

    public void onClick(View view) {
        if (view.getId() == R.id.nextButton) {
            Log.d("test","getNickName");
            UserQuote = quoteText.getText().toString();

            savePreferencesString("UserQuote", UserQuote);

            if (UserQuote.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(QuoteQuest.this);
                AlertDialog dialog = builder.setMessage("빈 칸 없이 입력해주세요.").setPositiveButton("확인", null).create();
                dialog.show();
            }else {
                Intent intent = new Intent(this, Fragment_main.class);
                startActivity(intent);
                finish();
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
