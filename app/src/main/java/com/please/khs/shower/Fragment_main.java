package com.please.khs.shower;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.please.khs.shower.Tutorial.Tutorial6;
import com.github.paolorotolo.appintro.AppIntro;

public class Fragment_main extends AppIntro {
    int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(CustomSlide.newInstance(R.layout.activity_tutorial2));
        addSlide(CustomSlide.newInstance(R.layout.activity_tutorial3));
        addSlide(CustomSlide.newInstance(R.layout.activity_tutorial4));
        addSlide(CustomSlide.newInstance(R.layout.activity_tutorial5));

        setColorDoneText(Color.parseColor("#000000"));
        setNavBarColor(R.color.colorBlack);
        setSeparatorColor(Color.parseColor("#000000"));
        setColorSkipButton(Color.parseColor("#000000"));
        setNextArrowColor(Color.parseColor("#000000"));
        setIndicatorColor(R.color.colorBlack, R.color.colorBlack);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(Fragment_main.this, Tutorial6.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(Fragment_main.this, Tutorial6.class);
        startActivity(intent);
        finish();
        }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

    }

}
