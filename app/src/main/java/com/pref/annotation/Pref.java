package com.pref.annotation;

import com.pref.annotations.*;

@SharePref(superPackage = "com.pref.annotation")
public class Pref {

    @PrefKey(key = "user_name")
    public String username;

    @PrefKey(key = "order")
    @ObjectType("com.pref.annotation.Order")
    public Order order;

}
