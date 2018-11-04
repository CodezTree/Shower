package com.please.khs.shower;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LogoutActivity extends AppCompatActivity {

    Button confirmButton, disconfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        confirmButton = (Button) findViewById(R.id.bt_confirm);
        disconfirmButton = (Button) findViewById(R.id.bt_disconfirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferencesString("Email", null);
                savePreferencesString("NickName", null);
                savePreferencesString("Password", null);
                savePreferencesString("AppFirstTime", "true");
                savePreferencesString("UserQuote", "문구를 설정 해 주세요!");
                savePreferencesInt("UserGrade", 0);
                savePreferencesInt("ContentTime", 0);
                savePreferencesInt("ContentUse", 1);
                SONAGIGlobalClass.graphData.clear();
                SONAGIGlobalClass.memoData.clear();
                SONAGIGlobalClass.Sdb.clearDatabase();

                Toast.makeText(LogoutActivity.this, "모든 앱 데이터를 삭제하고 로그아웃 합니다.", Toast.LENGTH_SHORT).show();

                finishAffinity();
                finish();
            }
        });

        disconfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getPreferencesString(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    private int getPreferencesInt(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    private void savePreferencesString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    private void savePreferencesInt(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
