package com.please.khs.shower;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.please.khs.shower.Request.UserName;

public class Rainny extends AppCompatActivity {
    private long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainny);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.nextButton) {

                Intent intent2 = new Intent(Rainny.this, UserName.class);
                startActivity(intent2);
                finish();


                // So Cute Lol

        }
    }

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - time >= 2000){
            time=System.currentTimeMillis();
            finish();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis() - time < 2000){
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }
}
