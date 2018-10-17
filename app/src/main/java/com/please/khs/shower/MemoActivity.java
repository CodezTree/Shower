package com.please.khs.shower;

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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MemoActivity extends AppCompatActivity {
    private AlertDialog dialog;
    TextView textViewDate;
    Button rgisbtn, closebtn;
    EditText memoText;
    LinearLayout layoutOut;
    private int deviceWidth, deviceHeight;
    private LinearLayout blue_layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //TODO: 팝업 액티비티 생성
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //상단바 제거
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //액티비티 둥근모양으로
//ColorDrawable(Color.TRANSPARENT)
        final Intent intent = getIntent();
        int e = intent.getIntExtra("emotion",0);
        if (e > 0 && e < 5) {
            //if (데이터 속 감정이 슬픔 쪽이라면){   }
            setContentView(R.layout.activity_memo_blue);//memo bule pink
        } else {
            setContentView(R.layout.activity_memo_pink);
        }


        // 1. 디스플레이 화면 사이즈 구하기
        Display dp = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        // 2. 화면 비율 설정
        DisplayMetrics disp = getApplicationContext().getResources().getDisplayMetrics();
        deviceWidth = disp.widthPixels;
        deviceHeight = disp.heightPixels;
        int width = (int)(deviceWidth*0.7);
        int height = (int)(deviceHeight*0.65);

        // 3. 현재 화면에 적용
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        // 액티비티 바깥화면이 클릭되어도 종료되지 않게 설정하기
        this.setFinishOnTouchOutside(false);


        memoText = findViewById(R.id.memotext);

        textViewDate = findViewById(R.id.dateText);
        textViewDate.setText(intent.getStringExtra("time"));

        rgisbtn = findViewById(R.id.registerbtn);
        rgisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("timeOrder", intent.getIntExtra("timeOrder", 0));
                resultIntent.putExtra("memo", memoText.getText().toString());
                resultIntent.putExtra("emotion", intent.getIntExtra("emotion",0));
                resultIntent.putExtra("time", getCurrentTime());
                setResult(RESULT_OK,  resultIntent);
                finish();
            }
        });

        closebtn = findViewById(R.id.closebutton);
        closebtn.setOnClickListener(new View.OnClickListener() {  //close했을 때 저장안할꺼냐고 dialog 띄우기
            @Override
            public void onClick(View v) {
                Log.d("before finish","memo" );  //두 번 눌러야 꺼짐, 로그는 두 번 다 찍힘 근데뭔가 이상...
                finish();
            }
        });

    }

    //메모 데이터 받기(저장)
    //   public getMemoData(String mtxt, Date time){
    //     memoText = findViewById(R.id.memotext);
    //      memotxt= memoText.getText().toString();
    //       MemoData.mtxt = memotxt;
    //      return;
    //  }
    //메모 넘겨주깅
    //   public void insertMemoData(String mtxt, Date time, String tableName){

    //  }
    //메모 불러오기
    //  public void

//
//    private String getPreferencesString(String memo) {
//        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
//        return sharedPreferences.getString(memo, "");
//    }
//
//    private void savePreferencesString(String memo, String value) {// 야!!!뭘 어떤형식으로 저장할지!!넣어!!!내일 1교시에!!
//        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(memo, value);
//        editor.commit();
//    }

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

