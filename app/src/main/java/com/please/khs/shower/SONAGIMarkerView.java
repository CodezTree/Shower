package com.please.khs.shower;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.please.khs.shower.Main.GraphActivity;

import java.util.Calendar;

public class SONAGIMarkerView extends MarkerView{

    public float drawingPosX;
    public float drawingPosY;
    private TextView memoText;
    private Button memoButton;
    private int timeOrder;
    private static final int MAX_CLICK_DURATION = 500;
    private long startClickTime;
    private Context Ccontext;

    public SONAGIMarkerView(final Context context, int layoutResource) {
        super(context, layoutResource);
        Ccontext = context;

        memoText = (TextView) findViewById(R.id.memoText);
        memoButton = (Button) findViewById(R.id.memoButton);

        memoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","onClick");
            }
        });
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        memoText.setText(SONAGIGlobalClass.emotionSet.get((int)e.getY() - 1));
        /*if (SONAGIGlobalClass.memoData.get((int)e.getX()) != null) {
            memoText.setTextColor(Color.RED);
        } else {
            memoText.setTextColor(Color.BLACK);
        }*/
        timeOrder = (int)e.getX();

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -(int)(getHeight() * 1.5));
        }
        return mOffset;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d("test", "motion down");
                startClickTime = Calendar.getInstance().getTimeInMillis();
                break;
            }
            case MotionEvent.ACTION_UP: {
                Log.d("test", "motion up");
                long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                if (clickDuration < MAX_CLICK_DURATION) {
                    performClick();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        super.performClick();

        Log.d("test", "perform click");
        Intent broadcastIntent = new Intent();
        Log.d("logging :", "new Intent..!");
        broadcastIntent.setAction("memoBoard");
        broadcastIntent.putExtra("timeOrder", timeOrder);
        Ccontext.sendBroadcast(broadcastIntent);

        return true;
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        Log.d("test", "perform draw");
        super.draw(canvas, posX, posY);
        this.drawingPosX = posX + memoButton.getX()+mOffset.x;
        this.drawingPosY = posY + memoButton.getY()+mOffset.y;
    }
}