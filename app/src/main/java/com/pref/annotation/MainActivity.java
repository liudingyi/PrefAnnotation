package com.pref.annotation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pref.Pref;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pref pref = new Pref(this);
    }
}
