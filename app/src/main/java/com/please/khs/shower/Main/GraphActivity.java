package com.please.khs.shower.Main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.please.khs.shower.MemoActivity;
import com.please.khs.shower.MemoData;
import com.please.khs.shower.R;
import com.please.khs.shower.SONAGIData;
import com.please.khs.shower.SONAGIDatabase;
import com.please.khs.shower.SONAGIGlobalClass;
import com.please.khs.shower.SONAGIListAdapter;
import com.please.khs.shower.SONAGIMarkerView;
import com.please.khs.shower.SONAGIService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.abs;

public class GraphActivity extends AppCompatActivity {
    private long time = 0;
    private LineChart lineChart;
    Button memobtn;
    TextView txtResult;
    List<Entry> entries;
    IntentFilter mIntentFilter;

    int startHour = 1; // 1시 정상적 Start

    private LinearLayoutManager mLinearLayoutManager;
    RecyclerView RV;

    SONAGIListAdapter madapter;

    Boolean endLogging = false;

    Toolbar appToolbar;

    public void showMemoActivity(int timeOrder) {
        Intent intent = new Intent(GraphActivity.this, MemoActivity.class);
        intent.putExtra("timeOrder", timeOrder);
        intent.putExtra("time", SONAGIGlobalClass.graphData.get(SONAGIGlobalClass.graphDataConnector.get(timeOrder)).dateTime);
        intent.putExtra("emotion", SONAGIGlobalClass.graphData.get(SONAGIGlobalClass.graphDataConnector.get(timeOrder)).emotion);
        // TODO : get 이 null 인경우도 처리...?? ==> 뭐죠
        startActivityForResult(intent, 3000);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("logging :","broadcast received!!");
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case "memoBoard":
                        Log.d("logging : ", "memoBoard");
                        showMemoActivity(intent.getIntExtra("timeOrder",0));
                        break;
                    case "graphRefresh":
                        Log.d("test","refresh intent broadcast");
                        refreshGraphData();
                        break;
                    default:
                        Log.d("test", "Unknown Intent Name " + action);
                        break;
                }
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int test;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3000:
                    int emotion = data.getIntExtra("emotion", 0);
                    String time = data.getStringExtra("time"), memo = data.getStringExtra("memo");
                    //Log.d("test", "got data extra " + Integer.toString(test));
                    // SONAGIGlobalClass.memoData.add(data.getIntExtra("timeOrder", 0), new MemoData(data.getStringExtra("time"), data.getStringExtra("memo"), data.getIntExtra("emotion", 0)));
                    SONAGIGlobalClass.Sdb.putMemoData(time, memo, emotion);
                    // DATABASE SAVE LATER
                    refreshTimelineData();
            }
        }
    }

    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("test","on New Intent");

        if (intent != null) {
            if (intent.getStringExtra("kind").equals("memo")) {
                showMemoActivity(intent.getIntExtra("timeOrder", 0));
            }
        }
    }*/

    @SuppressLint("ClickableViewAccessibility")  // 노란색 워닝 보기 실어서
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        appToolbar = (Toolbar) findViewById(R.id.graphToolbar);
        setSupportActionBar(appToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV = findViewById(R.id.RV);

        RV.setLayoutManager(mLinearLayoutManager);

        madapter = new SONAGIListAdapter(GraphActivity.this, SONAGIGlobalClass.memoData);
        RV.setAdapter(madapter);

        // tv_hour = (TextView)findViewById(R.id.tv_hour);

        if (!isServiceRunningCheck()) {
            Intent intent = new Intent(GraphActivity.this, SONAGIService.class);
            startService(intent);
        }
        //Start Service TODO : Check whether service is working. if not, start service for works.


        /*
        //메모버튼
        memobtn = findViewById(R.id.memoButton);

        memobtn.setOnClickListener(new View.OnClickListener() {//nullpointException 오류!!
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GraphActivity.this, Memo.class);//원래memeo
                intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);
                startActivity(intent);
            }

        });

        //메모버튼끝 -> // TODO : 필요없으면 제거 요망
        */
        // Memo 정점 클릭

        // Dummy Data
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 00:00:00", "아", 6));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 01:00:00", "아", 1));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 02:00:00", "아", 2));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 03:00:00", "아", 7));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 04:00:00", "아", 4));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 05:00:00", "아", 5));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 06:00:00", "아", 8));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 07:00:00", "아", 3));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 08:00:00", "아", 2));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 09:00:00", "아", 5));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 10:00:00", "아", 7));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 11:00:00", "아", 1));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 12:00:00", "아", 4));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 13:00:00", "아", 4));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 14:00:00", "아", 6));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 15:00:00", "아", 5));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 16:00:00", "아", 4));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 17:00:00", "아", 1));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 18:00:00", "아", 8));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 19:00:00", "아", 6));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 20:00:00", "아", 5));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 21:00:00", "아", 1));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 22:00:00", "아", 7));
        SONAGIGlobalClass.graphData.add(new SONAGIData("2018-09-29 23:00:00", "아", 6));


        // for test
        SONAGIGlobalClass.memoData.add(new MemoData("2018-09-29 01:00:00", "슬퍼라...", 1));
        SONAGIGlobalClass.memoData.add(new MemoData("2018-09-29 02:00:00", "에구... 연습 잘 못한날..", 1));
        SONAGIGlobalClass.memoData.add(new MemoData("2018-09-29 03:00:00", "난 왜 실수투성이일까...싶다", 3));

        madapter.notifyDataSetChanged();
        //for test


        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("memoBoard");
        mIntentFilter.addAction("graphRefresh");

        registerReceiver(mReceiver, mIntentFilter);


        // 감정 심화도 리스트
        SONAGIGlobalClass.emotionSet.add("우울함");
        SONAGIGlobalClass.emotionSet.add("짜증");
        SONAGIGlobalClass.emotionSet.add("긴장");
        SONAGIGlobalClass.emotionSet.add("지침");
        SONAGIGlobalClass.emotionSet.add("만족");
        SONAGIGlobalClass.emotionSet.add("신남");
        SONAGIGlobalClass.emotionSet.add("행복");
        SONAGIGlobalClass.emotionSet.add("즐거움");

        lineChart = (LineChart)findViewById(R.id.chart);

        entries = new ArrayList<>(); // Dummy data
        entries.add(new Entry(0, 6));
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 7));
        entries.add(new Entry(4, 5));
        entries.add(new Entry(5, 5));
        entries.add(new Entry(6, 8));
        entries.add(new Entry(7, 3));
        entries.add(new Entry(8, 2));
        entries.add(new Entry(9, 5));
        entries.add(new Entry(10, 7));
        entries.add(new Entry(11, 1));
        entries.add(new Entry(12, 5));
        entries.add(new Entry(13, 4));
        entries.add(new Entry(14, 6));
        entries.add(new Entry(15, 5));
        entries.add(new Entry(16, 4));
        entries.add(new Entry(17, 1));
        entries.add(new Entry(18, 8));
        entries.add(new Entry(19, 6));
        entries.add(new Entry(20, 4));
        entries.add(new Entry(21, 1));
        entries.add(new Entry(22, 7));
        entries.add(new Entry(23, 6));
        // dummy data --------

        LineDataSet lineDataSet = new LineDataSet(entries, "나의 감정");//라벨은 line이 무엇을 나타내는지
        lineDataSet.setLineWidth(3);
        lineDataSet.setCircleHoleRadius(4f);
        lineDataSet.setCircleRadius(8f); //정점
        lineDataSet.setCircleColor(Color.parseColor("#FF7A83"));
        // #ff7a83
        lineDataSet.setCircleColorHole(Color.WHITE);//정점 안 정점
        lineDataSet.setColor(Color.parseColor("#FF7A83"));//line color
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(true);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setHighlightLineWidth(0);

        LineData lineData = new LineData(lineDataSet);
        lineData.setHighlightEnabled(true);
        lineChart.setData(lineData);
        lineChart.setDragEnabled(true);
        lineChart.setScaleYEnabled(false);
        lineChart.setScaleXEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDrawMarkers(true);

        // 그래프 확대 축소 안되게
        lineChart.setPinchZoom(false);
        lineChart.getViewPortHandler().setMaximumScaleX((float)entries.size() / 7);
        lineChart.getViewPortHandler().setMinimumScaleX((float)entries.size() / 7);
        lineChart.getViewPortHandler().setDragOffsetX(7f);

        lineChart.setHighlightPerTapEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);

        // Memo 버튼 클릭 Listener
        lineChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean handled = true;
                // if there is no marker view or drawing marker is disabled
                if (isShowingMarker() && lineChart.getMarker() instanceof SONAGIMarkerView){
                    SONAGIMarkerView markerView = (SONAGIMarkerView) lineChart.getMarker();
                    Rect rect = new Rect((int)markerView.drawingPosX,(int)markerView.drawingPosY,(int)markerView.drawingPosX + markerView.getWidth(), (int)markerView.drawingPosY + markerView.getHeight());
                    if (rect.contains((int) event.getX(),(int) event.getY())) {
                        // touch on marker -> dispatch touch event in to marker
                        markerView.dispatchTouchEvent(event);
                    }else{
                        handled = GraphActivity.super.onTouchEvent(event);
                    }
                }else{
                    handled = GraphActivity.super.onTouchEvent(event);
                }
                return handled;
            }

            private boolean isShowingMarker() {
                return lineChart.getMarker() != null && lineChart.isDrawMarkersEnabled() && lineChart.valuesToHighlight();
            }

        });

        //-- custom ( time data )
        final String[] quarters = new String[] {"24시", "1시", "2시", "3시", "4시", "5시","6시", "7시", "8시", "9시", "10시", "11시", "12시", "13시", "14시", "15시", "16시", "17시", "18시", "19시", "20시", "21시", "22시", "23시"};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.d("refresh", Float.toString(value));
                if (startHour == 0) {
                    return quarters[(int) value % 24];
                } else {
                    return quarters[(int) (startHour + value) % 24];
                }
            }
        };
        //-- custom

        // Axis Setting
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.parseColor("#F3C4CC"));
        xAxis.enableGridDashedLine(6, 10, 0);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.parseColor("#FFFFFF")); //y축 존재, UI에는 안 보임.
        yLAxis.setAxisMinimum(0f);
        yLAxis.setAxisMaximum(9f);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        LimitLine ll = new LimitLine(4.5f, "");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(0.4f);

        /*LimitLine l1 = new LimitLine(1f, "우울함");
        LimitLine l2 = new LimitLine(2f, "짜증");
        LimitLine l3 = new LimitLine(3f, "긴장");
        LimitLine l4 = new LimitLine(4f, "지침");
        LimitLine l5 = new LimitLine(5f, "만족");
        LimitLine l6 = new LimitLine(6f, "신남");
        LimitLine l7 = new LimitLine(7f, "행복");
        LimitLine l8 = new LimitLine(8f, "즐거움");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(0.45f);
        l1.setLineColor(Color.BLACK);
        l1.setLineWidth(0.15f);
        l2.setLineColor(Color.BLACK);
        l2.setLineWidth(0.15f);
        l3.setLineColor(Color.BLACK);
        l3.setLineWidth(0.15f);
        l4.setLineColor(Color.BLACK);
        l4.setLineWidth(0.15f);
        l5.setLineColor(Color.BLACK);
        l5.setLineWidth(0.15f);
        l6.setLineColor(Color.BLACK);
        l6.setLineWidth(0.15f);
        l7.setLineColor(Color.BLACK);
        l7.setLineWidth(0.15f);
        l8.setLineColor(Color.BLACK);
        l8.setLineWidth(0.15f);


        yLAxis.addLimitLine(ll);
        yLAxis.addLimitLine(l1);
        yLAxis.addLimitLine(l2);
        yLAxis.addLimitLine(l3);
        yLAxis.addLimitLine(l4);
        yLAxis.addLimitLine(l5);
        yLAxis.addLimitLine(l6);
        yLAxis.addLimitLine(l7);
        yLAxis.addLimitLine(l8);
        */

        Description description = new Description();
        description.setText("( 단위 : 시간 )");
        // Axis Settingl


        // Marker View
        SONAGIMarkerView mv = new SONAGIMarkerView(this, R.layout.graph_marker);
        lineChart.setMarker(mv);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setHighlightPerTapEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();


        // test
        /*

        DateFormat dateFormat = new SimpleDateFormat("HH", Locale.KOREA);
        Date now = new Date();
        Toast.makeText(getApplicationContext(), dateFormat.format(now), Toast.LENGTH_LONG).show();
        */
    }

    public void refreshGraphData() {  // GraphRefresh
        Log.d("test" ,"GraphRefresh");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        DateFormat dateFormat1 = new SimpleDateFormat("HH", Locale.KOREA);
        Date now = new Date();
        Date start = new Date();
        SparseIntArray tempArr = new SparseIntArray();

        now.setTime(cutToHour(now.getTime()) + 59 * 60 * 1000 + 999); // XX시 59분 59초
        start.setTime(cutToHour(now.getTime() - (3 * 24 * 60 * 60 * 1000))); // 3일 전

        int tempIndexer = 0;

        SONAGIGlobalClass.graphData = SONAGIGlobalClass.Sdb.getEmotionDataListFromTime(dateFormat.format(start), dateFormat.format(now)); // 지금 Hour로 부터 3일 전까지 불러온다.
        List<Entry> tempEntries = new ArrayList<>();

        startHour = Integer.parseInt(dateFormat1.format(start)); // 계산 시작 시간
        int hour;

        try {
            for (int i = 0; i < 72; i++) {
                if (tempIndexer == SONAGIGlobalClass.graphData.size()) {
                    break;
                }

                hour = Integer.parseInt(dateFormat1.format(dateFormat.parse(SONAGIGlobalClass.graphData.get(tempIndexer).dateTime)));

                if (hour == (startHour + i) % 24) {
                    tempEntries.add(new Entry((float)i, SONAGIGlobalClass.graphData.get(tempIndexer).emotion));
                    tempArr.append(i, tempIndexer);
                    tempIndexer++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tempEntries.size() == 0) {
            tempEntries.add(new Entry(0f, -1f));
        } // 튕김 방지.... 0일때

        SONAGIGlobalClass.graphDataConnector = tempArr;

        LineDataSet tempLineDataSet = new LineDataSet(tempEntries, "나의 감정");
        tempLineDataSet.setLineWidth(4);
        tempLineDataSet.setCircleHoleRadius(4f);
        tempLineDataSet.setCircleRadius(8f); //정점
        tempLineDataSet.setCircleColor(Color.parseColor("#FC8C94"));
        tempLineDataSet.setCircleColorHole(Color.WHITE);//정점 안 정점
        tempLineDataSet.setColor(Color.parseColor("#FF7A83"));//line color
        tempLineDataSet.setDrawCircleHole(true);
        tempLineDataSet.setDrawCircles(true);
        tempLineDataSet.setDrawHorizontalHighlightIndicator(true);
        tempLineDataSet.setDrawHighlightIndicators(false);
        tempLineDataSet.setDrawValues(false);
        tempLineDataSet.setHighlightLineWidth(3.0f);
        tempLineDataSet.setHighlightEnabled(true);

        LineData tempLineData = new LineData(tempLineDataSet);

        lineChart.setData(tempLineData);
        lineChart.getViewPortHandler().setMaximumScaleX((float)24 / 7);
        lineChart.getViewPortHandler().setMinimumScaleX((float)24 / 7);
        //lineChart.getViewPortHandler().setZoom(0.1f, 0.1f);
        lineChart.invalidate();
    }

    private long cutToHour(long time) {
        return time - (time % (60 * 60 * 1000));
    }

    public void refreshTimelineData() {  // TimeLine Refresh
        Log.d("test" ,"Timeline Data Refresh");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        Date now = new Date();
        Date start = new Date();

        now.setTime(cutToHour(now.getTime()) + 59 * 60 * 1000 + 999); // XX시 59분 59초
        start.setTime(cutToHour(now.getTime() - (3 * 24 * 60 * 60 * 1000))); // 3일 전

        SONAGIGlobalClass.memoData = SONAGIGlobalClass.Sdb.getMemoDataListFromTime(dateFormat.format(start), dateFormat.format(now)); // 지금 Hour로 부터 24시간 전까지 불러온다.

        madapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) // Test Key
    {
        switch(keycode)
        {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("tester");
                broadcastIntent.putExtra("emotion", SONAGIGlobalClass.Sdb.getLatestEmotion().emotion);
                sendBroadcast(broadcastIntent);
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                // SONAGIGlobalClass.Sdb.testWrite();
                refreshGraphData();
                break;
            case KeyEvent.KEYCODE_BACK:
                if(System.currentTimeMillis() - time >= 1000) {
                    time=System.currentTimeMillis();
                    Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
                }else if(System.currentTimeMillis() - time < 1000){
                    finishAffinity();
                    System.runFinalization();
                    System.exit(0);
                }
                break;
        }
        return true;
    }

    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.khs.shower.SONAGIService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}