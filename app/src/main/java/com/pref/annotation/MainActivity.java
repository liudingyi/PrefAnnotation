package com.pref.annotation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.pref.Pref_;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pref_ pref = new Pref_(this);
        pref.putOrder(new Order(1000));
        Log.i("liudingyi", Order.class.getCanonicalName());
        Log.i("liudingyi", new Gson().toJson(pref.getOrder()));
    }
}
