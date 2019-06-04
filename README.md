
## 快捷的使用SharePreferences

[![](https://jitpack.io/v/liudingyi/PrefAnnotation.svg)](https://jitpack.io/#liudingyi/PrefAnnotation)

### 1.在project中的build.gradle

    allprojects {
        repositories {
            ...
            //添加仓库路径
            maven { url "https://jitpack.io" }
        }
    }

### 2.在module中的build.gradle

    dependencies {
        ...
        implementation 'com.github.liudingyi.PrefAnnotation:annotations:@version'
        annotationProcessor 'com.github.liudingyi.PrefAnnotation:processors:@version'
    }


### 注解说明


注解名| 注解作用 | 注解属性
---|---|---
@SharePref | 标记对应的preference数据类 | name:偏好名称<br>superPackage：所在包路径
@PrefKey | 标记属性的key | key：属性的key
@ObjectType | 标记属性对象类型 | value：完整的包名 例：<br>@PrefKey(key = "user_info")<br>@ObjectType(" com.ldy.data.UserInfo")<br>private UserInfo userInfo;
@DefaultInt | 标记默认的Int值 | value：默认值
@DefaultFloat | 标记默认的Float值 | value：默认值
@DefaultLong | 标记默认的Long值 | value：默认值
@DefaultBoolean | 标记默认的Boolean值 | value：默认值
@DefaultString | 标记默认的String值 | value：默认值

## 简单例子

```

@SharePref(name = "pref", superPackage = "com.liudy.pref")
public class Pref {

    @PrefKey(key = "token")
    @DefaultString(value = "token123456789")
    public String token;

    @PrefKey(key = "user_info")
    @ObjectType(value = "com.liudy.data.UserInfo")
    public UserInfo userInfo;
    
}

```

## 编译生成的文件

```

public final class Pref_ extends Pref {
  private static Pref_ instance;

  private static SharedPreferences preference;

  private static Editor editor;

  private Pref_(Context context) {
    preference = context.getSharedPreferences("pref",0);
    editor = preference.edit();
  }

  public static Pref_ getInstance(Context context) {
    if(instance == null) {
      instance = new Pref_(context);
    }
    return instance;
  }

  public void removeToken() {
    synchronized(Pref_.class) {
      if(editor != null) {
        editor.remove("token").apply();
      }
    }
  }

  public String getToken() {
    synchronized(Pref_.class) {
      if(preference != null) {
        return preference.getString("token", "token123456789");
      }
      return "token123456789";
    }
  }

  public void putToken(String value) {
    synchronized(Pref_.class) {
      if(editor != null && value != null) {
        editor.putString("token", value).apply();
      }
    }
  }

  public void removeUserInfo() {
    synchronized(Pref_.class) {
      if(editor != null) {
        editor.remove("user_info").apply();
      }
    }
  }

  public UserInfo getUserInfo() {
    synchronized(Pref_.class) {
      if(preference != null) {
        String json = preference.getString("user_info", "");
        if(json.isEmpty()) {
          return null;
        }
        return new Gson().fromJson(json, UserInfo.class);
      }
      return null;
    }
  }

  public void putUserInfo(UserInfo userinfo) {
    synchronized(Pref_.class) {
      if(editor != null) {
        String json = "";
        if(userinfo != null) {
          json = new Gson().toJson(userinfo);
        }
        editor.putString("user_info", json).apply();
      }
    }
  }
}

```


