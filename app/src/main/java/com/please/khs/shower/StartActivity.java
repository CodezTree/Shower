package com.please.khs.shower;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.please.khs.shower.Main.GraphActivity;

public class StartActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (getPreferencesInt("ContentTime") == -1) {
            savePreferencesInt("ContentTime", 0); // 0 is 1 hour
        }

        if (getPreferencesInt("UserGrade") == -1) {
            savePreferencesInt("UserGrade", 0);
        }

        if (getPreferencesString("UserQuote") == null) {
            savePreferencesString("UserQuote", "문구를 설정해 주세요!");
        }

        if (getPreferencesInt("ContentUse") == -1) {
            savePreferencesInt("ContentUse", 1);
        }

        if (getPreferencesString("AppFirstTime") == null) {
            savePreferencesString("AppFirstTime", "true");
        }

        if (getPreferencesInt("EmotionAnalyzedTime") == -1) {
            savePreferencesInt("EmotionAnalyzedTime", 0);
        }

        SONAGIGlobalClass.Sdb = new SONAGIDatabase(getApplicationContext(), "SONAGI", null, 1);
        /*

        Preference 정리

        NickName - 유저 이름 - Name String
        UserQuote - 유저 문구 - String
        UserGrade - 유저 등급 - 0 1 2... - grade int
        ContentUse - 컨텐츠 제공 알림 여부 0 1 - false true int
        ContentTime - 컨텐츠 제공 시간 0 1 2 - 1 3 10 int
        AppFirstTime - 앱이 처음인지. true , false String
        EmotionAnalyzedTime - 감정 분석 횟수. 칭호 얻을 수 있다!

         */

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(StartActivity.this, LoginActivity.class);
                StartActivity.this.startActivity(mainIntent);
                StartActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onBackPressed(){

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
