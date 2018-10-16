package com.please.khs.shower.Tutorial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.please.khs.shower.Fragment_main;
import com.please.khs.shower.R;

public class Tutorial1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial1);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.nextButton) {

            Intent intent = new Intent(Tutorial1.this, Fragment_main.class);
            startActivity(intent);
            finish();
        }
    }
}
