package com.please.khs.shower;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MemoActivity extends AppCompatActivity {
    private Dialog memodialog = null;
    TextView textViewDate;
    Button rgisbtn, closebtn;
    EditText memoText;
    LinearLayout layoutOut;
    private int deviceWidth, deviceHeight;
    private LinearLayout blue_layout;

    private PopupWindow mPopupWindow ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("logging :","memo popup");

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //상단바 제거
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //액티비티 둥근모양으로

        final Intent intent = getIntent();
        int e = intent.getIntExtra("emotion",0);
        if (e > 0 && e < 5) {
            setContentView(R.layout.activity_memo_blue);//memo bule pink
        } else {
            setContentView(R.layout.activity_memo_pink);
        }

        // 액티비티 바깥화면이 클릭되어도 종료되지 않게 설정하기
        this.setFinishOnTouchOutside(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // SONAGI 안뜨게

        memoText = findViewById(R.id.memotext);
        memoText.setText(intent.getStringExtra("memo"));

        textViewDate = findViewById(R.id.dateText);
        textViewDate.setText(String.format("%s시", intent.getStringExtra("time").substring(0, 13)));

        rgisbtn = findViewById(R.id.registerbtn);
        rgisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("timeOrder", intent.getIntExtra("timeOrder", 0));
                resultIntent.putExtra("memo", memoText.getText().toString());
                resultIntent.putExtra("emotion", intent.getIntExtra("emotion",0));
                resultIntent.putExtra("time", intent.getStringExtra("time"));
                setResult(Activity.RESULT_OK,  resultIntent);
                finish();
            }
        });

        closebtn = findViewById(R.id.closebutton);
        closebtn.setOnClickListener(new View.OnClickListener() {  //close했을 때 저장안할꺼냐고 dialog 띄우기
            @Override
            public void onClick(View v) {
                Log.d("before finish","memo");  //두 번 눌러야 꺼짐, 로그는 두 번 다 찍힘 근데뭔가 이상...
                finish();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        //안드로이드 백버튼 막기
       return;
    }

    private String getCurrentTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String getTime = sdf.format(date);

        return getTime;
    }
}

