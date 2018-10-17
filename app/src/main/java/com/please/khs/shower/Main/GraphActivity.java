package com.please.khs.shower.Main;

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
import android.util.Log;
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

public class GraphActivity extends AppCompatActivity {
    private long time = 0;
    private LineChart lineChart;
    Button memobtn;
    TextView txtResult;
    List<Entry> entries;
    IntentFilter mIntentFilter;

    private LinearLayoutManager mLinearLayoutManager;
    RecyclerView RV;

    SONAGIListAdapter madapter;
    /*
    Intent memointent = new Intent(GraphActivity.this, MemoActivity.class);
        memointent.putExtra("mintent",  ); //감정num

        MemoButton

    */

    public void showMemoActivity(int timeOrder) {
        Intent intent = new Intent(GraphActivity.this, MemoActivity.class);
        intent.putExtra("timeOrder", timeOrder);
        intent.putExtra("time", SONAGIGlobalClass.graphData.get(timeOrder).dateTime);
        intent.putExtra("emotion", SONAGIGlobalClass.graphData.get(timeOrder).emotion);
        // TODO : get 이 null 인경우도 처리...
        startActivityForResult(intent, 3000);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case "memoBroad":
                        showMemoActivity(intent.getIntExtra("timeOrder",0));
                    default:
                        Log.d("test", "Unknown Intent Name " + action);
                        break;
                }
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3000:
                    // SONAGIGlobalClass.memoData.put(data.getIntExtra("timeOrder", 0), new MemoData(data.getStringExtra("time"), data.getStringExtra("memo"), data.getIntExtra("emotion", 0)));
                    SONAGIGlobalClass.memoData.add(new MemoData(data.getStringExtra("time"), data.getStringExtra("memo"), data.getIntExtra("emotion", 0)));
                    // DATABASE SAVE LATER
                    refreshTimelineData();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("test","on New Intent");

        if (intent != null) {
            if (intent.getStringExtra("kind").equals("memo")) {
                showMemoActivity(intent.getIntExtra("timeOrder", 0));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_graph);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_actionbar);


        mLinearLayoutManager = new LinearLayoutManager(GraphActivity.this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV = findViewById(R.id.RV);
        RV.setHasFixedSize(true);
        RV.setLayoutManager(mLinearLayoutManager);

        madapter = new SONAGIListAdapter(GraphActivity.this, SONAGIGlobalClass.memoData);
        RV.setAdapter(madapter);

        Intent intent = new Intent(GraphActivity.this, SONAGIService.class);
        startService(intent);
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

        //메모버튼끝
        */ // Memo 정점 클릭

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

        // TODO : 정점 데이터 여기서 더미 만들자
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("memoBroad");

        registerReceiver(mReceiver, mIntentFilter);

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
        entries.add(new Entry(4, 4.5f));
        entries.add(new Entry(5, 4.5f));
        entries.add(new Entry(6, 8));
        entries.add(new Entry(7, 3));
        entries.add(new Entry(8, 2));
        entries.add(new Entry(9, 5));
        entries.add(new Entry(10, 7));
        entries.add(new Entry(11, 1));
        entries.add(new Entry(12, 4.5f));
        entries.add(new Entry(13, 4.5f));
        entries.add(new Entry(14, 6));
        entries.add(new Entry(15, 5));
        entries.add(new Entry(16, 4));
        entries.add(new Entry(17, 1));
        entries.add(new Entry(18, 8));
        entries.add(new Entry(19, 6));
        entries.add(new Entry(20, 4.5f));
        entries.add(new Entry(21, 1));
        entries.add(new Entry(22, 7));
        entries.add(new Entry(23, 6));

        LineDataSet lineDataSet = new LineDataSet(entries, "나의 감정");//라벨은 line이 무엇을 나타내는지
        lineDataSet.setLineWidth(1);
        lineDataSet.setCircleHoleRadius(2);
        lineDataSet.setCircleRadius(3); //정점
        lineDataSet.setCircleColor(Color.parseColor("#FC8C94"));
        lineDataSet.setCircleColorHole(Color.WHITE);//정점 안 정점
        lineDataSet.setColor(Color.parseColor("#FF7A83"));//line color
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(true);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setHighlightLineWidth(2);

        lineDataSet.setHighlightEnabled(true);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setDragEnabled(true);
        lineChart.setScaleYEnabled(false);
        lineChart.setScaleXEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDrawMarkers(true);
        lineChart.setPinchZoom(false);
        lineChart.getViewPortHandler().setMaximumScaleX((float)entries.size() / 7);
        lineChart.getViewPortHandler().setMinimumScaleX((float)entries.size() / 7);
        lineChart.setHighlightPerTapEnabled(true);
        lineChart.setHighlightPerDragEnabled(false);
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


        /*lineChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                // User Here to Dynamic Data Adding
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });*/

        // Marker View
        SONAGIMarkerView mv = new SONAGIMarkerView(this, R.layout.graph_marker);
        //lineChart.setMarker(mv);
        lineChart.setMarker(mv);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setHighlightPerTapEnabled(true);


        //-- custom ( time data )
        final String[] quarters = new String[] { "1시", "2시", "3시", "4시", "5시","6시", "7시", "8시", "9시", "10시", "11시", "12시"};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value % 12];
            }
        };
        //-- custom

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.parseColor("#F3C4CC"));
        xAxis.enableGridDashedLine(6, 10, 0);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);



        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.parseColor("#FFFFFF")); //y축 존재, UI에는 안 보임.

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(true);
        yRAxis.setDrawGridLines(false);

        LimitLine ll = new LimitLine(4.5f, "");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(0.4f);

        yLAxis.addLimitLine(ll);

        Description description = new Description();
        description.setText("( 단위 : 시간 )");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();

        // test

        DateFormat dateFormat = new SimpleDateFormat("HH", Locale.KOREA);
        Date now = new Date();
        Toast.makeText(getApplicationContext(), dateFormat.format(now), Toast.LENGTH_LONG).show();
    }

    public void refreshGraphData() {  // GraphRefresh

    }

    public void refreshTimelineData() {  // TimeLine Refresh
        madapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event)
    {
        switch(keycode)
        {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("tester");
                broadcastIntent.putExtra("emotion", 1);
                sendBroadcast(broadcastIntent);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - time >= 2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis() - time < 2000){
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }
}