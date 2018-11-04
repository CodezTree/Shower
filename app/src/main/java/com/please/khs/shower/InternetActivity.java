package com.please.khs.shower;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

public class InternetActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet);

        Intent intent = getIntent();

        String url = intent.getStringExtra("URL");
        Intent urlIntent = new Intent(Intent.ACTION_VIEW);
        Uri u = Uri.parse(url);
        urlIntent.setData(u);
        startActivity(urlIntent);

        Log.d("content test", "content received");

        finish(); // End this Activity when showing is over
    }

}
