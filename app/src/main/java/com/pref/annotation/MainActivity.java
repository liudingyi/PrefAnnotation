package com.pref.annotation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pref.Pref_;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pref_ pref =  Pref_.getInstance(this);
        pref.putOrder(new Order(1000));
    }
}
