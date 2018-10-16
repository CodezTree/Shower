package com.please.khs.shower.SignUpActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.please.khs.shower.R;


public class SignUp1Activity extends AppCompatActivity {

    CheckBox option1;
    CheckBox option2;
    private long time= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        option1 = findViewById(R.id.checkBox1); // option1체크박스
        // 선언
        option2 = findViewById(R.id.checkBox2); // option1체크박스
        // 선언
    }

    public void onClick(View view) {
        if (view.getId() == R.id.nextButton) {
            if ( option1.isChecked() && option2.isChecked() ) {

            Intent intent = new Intent(SignUp1Activity.this, SignUp2Activity.class);
            startActivity(intent);
            finish();
            }
            else{
                Toast.makeText(SignUp1Activity.this, "모두 동의했는지 확인해주세요", Toast.LENGTH_SHORT).show();
            }
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
}
