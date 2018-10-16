/*package com.example.khs.shower.TimeLine;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.khs.shower.R;

import org.json.JSONObject;

public class TimeLineDetailActivity {

    TextView tv_time;
    TextView tv_memo;

    ConstraintLayout tv_proConBar;

    String time;
    String memoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ag_dag_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_time = (TextView) findViewById(R.id.timeText);
        tv_memo = (TextView) findViewById(R.id.memoText);

        btn_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> response = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jRes = new JSONObject(response);
                            boolean success = jRes.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(),"succeed",Toast.LENGTH_SHORT);
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ADLikeRequest adLikeRequest = new ADLikeRequest("pro", Cid, response);
                RequestQueue queue = Volley.newRequestQueue(AgDagDetailActivity.this);
                queue.add(adLikeRequest);
            }
        });


        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jRes = new JSONObject(response);
                            boolean success = jRes.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(),"succeed",Toast.LENGTH_SHORT);
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ADLikeRequest adLikeRequest = new ADLikeRequest("con", Cid, response);
                RequestQueue queue = Volley.newRequestQueue(AgDagDetailActivity.this);
                queue.add(adLikeRequest);
            }
        });

        Intent intent = getIntent();

        tv_title.setText(intent.getStringExtra("title"));
        proCount = intent.getIntExtra("pro",0);
        conCount = intent.getIntExtra("con",0);
        imageUrl = intent.getStringExtra("imageUrl");
        tv_content.setText(intent.getStringExtra("content"));
        replyCount = intent.getIntExtra("replyCount",0);
        Cid = intent.getStringExtra("Cid");


        tv_pro.setText(" 찬 "+proCount);
        tv_con.setText(conCount+" 반 ");

        loadImage(imageUrl);

        int proConSum = proCount + conCount;

        Log.d("test", "proConsum is " + proConSum);
        Log.d("test", "proConbar width is " + tv_proConBar.getWidth());
        tv_pro.setLayoutParams(new LinearLayout.LayoutParams((int)(((float)proCount/(float)proConSum)*(float)tv_proConBar.getWidth()) , 30));
        tv_con.setLayoutParams(new LinearLayout.LayoutParams((int)(((float)conCount/(float)proConSum)*(float)tv_proConBar.getWidth()) , 30));
    }

    public void loadImage(String url) {
        if (url != null && !url.isEmpty()) {
            Picasso.with(this).load(url).into(iv_battle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}*/
