package com.please.khs.shower;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.please.khs.shower.Main.GraphActivity;
import com.please.khs.shower.Tutorial.Tutorial6;

public class Actionbar extends AppCompatActivity {
    ImageButton settingbtn, slidebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionbar);

        slidebtn = findViewById(R.id.slidebarButton);
        slidebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Actionbar.this, GraphActivity.class);//슬라이드 바
                startActivity(intent);
            }
        });

        settingbtn = findViewById(R.id.settingButton);
        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Actionbar.this, ComingSoon.class);
                Log.d("setting check", "check..");
                startActivity(intent);
            }
        });


    }
}

