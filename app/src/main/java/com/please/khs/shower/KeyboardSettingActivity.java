package com.please.khs.shower;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class KeyboardSettingActivity extends AppCompatActivity {

    Button btStep1, btStep2, btEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_setting);

        btStep1 = (Button) findViewById(R.id.bt_step_1);
        btStep2 = (Button) findViewById(R.id.bt_step_2);
        btEnd = (Button) findViewById(R.id.bt_end);

        btStep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                startActivity(intent);
            }
        });

        btStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager mgr = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.showInputMethodPicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
