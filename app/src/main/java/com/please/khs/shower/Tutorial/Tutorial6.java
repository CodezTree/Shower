package com.please.khs.shower.Tutorial;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.please.khs.shower.ComingSoon;
import com.please.khs.shower.Main.GraphActivity;
import com.please.khs.shower.R;
import com.please.khs.shower.SignUpActivity.SignUp2Activity;

public class Tutorial6 extends AppCompatActivity {
    Button startbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial6);

        startbtn = findViewById(R.id.nextButton);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tutorial6.this, GraphActivity.class);
                startActivity(intent);

                //토스트 메시지 - 성공적으로 로그인 되었습니다.
                Toast.makeText(Tutorial6.this, "로그인되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}