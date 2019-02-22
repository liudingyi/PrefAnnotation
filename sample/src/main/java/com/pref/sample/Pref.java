package com.pref.sample;

import com.pref.annotations.*;

@SharePref(superPackage = "com.pref.sample")
public class Pref {

    @PrefKey(key = "user_name")
    public String username;

}
