package com.please.khs.shower;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    Button alarmButton, usernameButton, userquoteButton, keyboardButton;
    EditText usernameEt, userquoteEt;
    Switch contentSwitch;
    String text;
    RadioButton rb_1, rb_2, rb_3;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        alarmButton = (Button) findViewById(R.id.bt_alarm_gap_set);
        usernameButton = (Button) findViewById(R.id.bt_username_set);
        userquoteButton = (Button) findViewById(R.id.bt_userquote_set);
        keyboardButton = (Button) findViewById(R.id.bt_keyboard_setting);

        contentSwitch = (Switch)  findViewById(R.id.sw_content_set);

        usernameEt = (EditText) findViewById(R.id.et_username_setting);
        userquoteEt = (EditText) findViewById(R.id.et_userquote_setting);

        rb_1 = (RadioButton) findViewById(R.id.rb_1hour);
        rb_2 = (RadioButton) findViewById(R.id.rb_3hour);
        rb_3 = (RadioButton) findViewById(R.id.rb_10hour);

        usernameEt.setText(getPreferencesString("NickName"));
        userquoteEt.setText(getPreferencesString("UserQuote"));

        rb_1.setChecked(false);
        rb_2.setChecked(false);
        rb_3.setChecked(false);

        switch(getPreferencesInt("ContentTime")) {
            case 0:
                Log.d("test", "content time 0 set");
                rb_1.setChecked(true);
                break;
            case 1:
                rb_2.setChecked(true);
                break;
            case 2:
                rb_3.setChecked(true);
                break;
        }

        if (getPreferencesInt("ContentUse") == 0) {
            contentSwitch.setChecked(false);
        } else {
            contentSwitch.setChecked(true);
        }


        contentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    savePreferencesInt("ContentUse", 1);
                } else {
                    savePreferencesInt("ContentUse", 0);
                }
            }
        });

        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_1.isChecked()) {
                    savePreferencesInt("ContentTime", 0);
                } else if (rb_2.isChecked()) {
                    savePreferencesInt("ContentTime", 1);
                } else if (rb_3.isChecked()) {
                    savePreferencesInt("ContentTime", 2);
                }

                Toast.makeText(SettingActivity.this, "컨텐츠 제공 시간 변경 완료", Toast.LENGTH_SHORT).show();
            }
        });

        userquoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = userquoteEt.getText().toString();

                if (!text.equals("") && text != null ) {
                    savePreferencesString("UserQuote", text);

                    Toast.makeText(SettingActivity.this, "유저 문구 변경 완료", Toast.LENGTH_SHORT).show();
                }
            }
        });

        usernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = usernameEt.getText().toString();

                if (!text.equals("") && text != null ) {
                    savePreferencesString("NickName", text);

                    Toast.makeText(SettingActivity.this, "유저 이름 변경 완료", Toast.LENGTH_SHORT).show();
                }
            }
        });

        keyboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, KeyboardSettingActivity.class);
                startActivity(intent);
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
