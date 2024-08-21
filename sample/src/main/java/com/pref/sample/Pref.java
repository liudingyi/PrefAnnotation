package com.pref.sample;

import com.pref.annotations.ObjectType;
import com.pref.annotations.PrefKey;
import com.pref.annotations.SharePref;
import com.pref.sample.data.User;

import java.util.List;
import java.util.Map;

@SharePref(name = "sample_pref", enable = true)
public class Pref {

    @PrefKey(key = "user_name")
    public String username;

    @PrefKey(key = "user_phone", enable = true)
    public String userPhone;
//
//    @PrefKey(key = "user")
//    @ObjectType
//    public User user;

//    @PrefKey(key = "user_list")
//    @ObjectType
//    public List<User> userList;

//    @PrefKey(key = "user_map")
//    @ObjectType
//    public Map<List<User>, List<User>> userMap;
//    public Map<String, User> userMap;

}
