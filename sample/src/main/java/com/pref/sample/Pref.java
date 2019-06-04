package com.pref.sample;

import com.pref.annotations.*;
import com.pref.sample.data.User;

import java.util.List;

@SharePref(name = "sample_pref")
public class Pref {

    @PrefKey(key = "user_name")
    public String username;

    @PrefKey(key = "user")
    @ObjectType
    public User user;

    @PrefKey(key = "user_list")
    @ObjectType
    public List<User> userList;
}
