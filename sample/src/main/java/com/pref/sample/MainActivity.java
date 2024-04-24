package com.pref.sample;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    public static final boolean isEnable = true;
    public static final boolean isEnable1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
