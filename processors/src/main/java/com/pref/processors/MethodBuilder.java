package com.pref.processors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

public class MethodBuilder {

    public static final String PrefPackageName = "com.pref";
    public static String PrefClassName = "Pref";
    public static final String PrefInstanceName = "instance";
    public static String PrefName = "pref_data";
    public static final String context = "context";

    public static final ClassName Context = ClassName.get("android.content", "Context");
    public static final ClassName SharedPreferences = ClassName.get("android.content", "SharedPreferences");
    public static final ClassName Editor = ClassName.get("android.content.SharedPreferences", "Editor");

    private static final int MODE_PRIVATE = 0;
    public static final String preference = "preference";
    public static final String editor = "editor";

    /**
     * @param str String
     * @return String
     */
    private static String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * @return MethodSpec
     */
    public static MethodSpec createInitialize() {
        return MethodSpec.methodBuilder("initialize")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Context, "mContext")
                .addStatement("$N = mContext", context)
                .build();
    }

    /**
     * @return MethodSpec
     */
    public static MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(Context, "context")
                .addStatement("$N = context.getSharedPreferences($S,$L)", preference, PrefName, MODE_PRIVATE)
                .addStatement("$N = $N.edit()", editor, preference)
                .build();
    }

    /**
     * @return MethodSpec
     */
    public static MethodSpec createInstance() {
        return MethodSpec.methodBuilder("getInstance")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get(PrefPackageName, PrefClassName))
//                .addParameter(Context, "context")
                .beginControlFlow("if($N == null)", PrefInstanceName)
                .addStatement("$N = new $N(context)", PrefInstanceName, PrefClassName)
                .endControlFlow()
                .addStatement("return $N", PrefInstanceName)
                .build();
    }

    /**
     * @param fieldName  String
     * @param key        String
     * @param defaultInt int
     * @return MethodSpec
     */
    public static MethodSpec createGetInt(String fieldName, String key, int defaultInt) {
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", preference)
                .addStatement("return $N.getInt($S, $L)", preference, key, defaultInt)
                .endControlFlow()
                .addStatement("return $L", defaultInt)
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createRemove(String fieldName, String key) {
        return MethodSpec.methodBuilder("remove" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", editor)
                .addStatement("$N.remove($S).apply()", editor, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createPutInt(String fieldName, String key) {
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(int.class, "value")
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", editor)
                .addStatement("$N.putInt($S, value).apply()", editor, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName    String
     * @param key          String
     * @param defaultFloat float
     * @return MethodSpec
     */
    public static MethodSpec createGetFloat(String fieldName, String key, float defaultFloat) {
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(float.class)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", preference)
                .addStatement("return $N.getFloat($S, $L)", preference, key, defaultFloat + "f")
                .endControlFlow()
                .addStatement("return $L", defaultFloat + "f")
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createPutFloat(String fieldName, String key) {
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(float.class, "value")
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", editor)
                .addStatement("$N.putFloat($S, value).apply()", editor, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }


    /**
     * @param fieldName   String
     * @param key         String
     * @param defaultLong Long
     * @return MethodSpec
     */
    public static MethodSpec createGetLong(String fieldName, String key, long defaultLong) {
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(long.class)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", preference)
                .addStatement("return $N.getLong($S, $L)", preference, key, defaultLong)
                .endControlFlow()
                .addStatement("return $L", defaultLong)
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createPutLong(String fieldName, String key) {
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(long.class, "value")
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", editor)
                .addStatement("$N.putLong($S, value).apply()", editor, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName      String
     * @param key            String
     * @param defaultBoolean Boolean
     * @return MethodSpec
     */
    public static MethodSpec createGetBoolean(String fieldName, String key, boolean defaultBoolean) {
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", preference)
                .addStatement("return $N.getBoolean($S, $L)", preference, key, defaultBoolean)
                .endControlFlow()
                .addStatement("return $L", defaultBoolean)
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createPutBoolean(String fieldName, String key) {
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(boolean.class, "value")
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", editor)
                .addStatement("$N.putBoolean($S, value).apply()", editor, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName     String
     * @param key           String
     * @param defaultString String
     * @return MethodSpec
     */
    public static MethodSpec createGetString(String fieldName, String key, String defaultString) {
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", preference)
                .addStatement("return $N.getString($S, $S)", preference, key, defaultString)
                .endControlFlow()
                .addStatement("return $S", defaultString)
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createPutString(String fieldName, String key) {
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "value")
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null && value != null)", editor)
                .addStatement("$N.putString($S, value).apply()", editor, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName  String
     * @param key        String
     * @param objectType String
     * @return MethodSpec
     */
    public static MethodSpec createGetObject(String fieldName, String key, String objectType) {
        int index = objectType.lastIndexOf(".");
        String package_name = objectType.substring(0, index);
        String class_name = objectType.substring(index + 1);
        ClassName obj = ClassName.get(package_name, class_name);
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(obj)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", preference)
                .addStatement("String json = $N.getString($S, $S)", preference, key, "")
                .beginControlFlow("if(json.isEmpty())", preference)
                .addStatement("return null")
                .endControlFlow()
                .addStatement("return new $T().fromJson(json, $N.class)", Gson.class, obj.simpleName())
                .endControlFlow()
                .addStatement("return null")
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName  String
     * @param key        String
     * @param objectType String
     * @return MethodSpec
     */
    public static MethodSpec createPutObject(String fieldName, String key, String objectType) {
        int index = objectType.lastIndexOf(".");
        String package_name = objectType.substring(0, index);
        String class_name = objectType.substring(index + 1);
        ClassName obj = ClassName.get(package_name, class_name);
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(obj, class_name.toLowerCase())
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", editor)
                .addStatement("String json = \"\"")
                .beginControlFlow("if($N != null)", class_name.toLowerCase())
                .addStatement("json = new $T().toJson($N)", Gson.class, class_name.toLowerCase())
                .endControlFlow()
                .addStatement("$N.putString($S, json).apply()", editor, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName         String
     * @param key               String
     * @param genericObjectType String
     * @return MethodSpec
     */
    public static MethodSpec createGetGenericObject(String fieldName, String key, String genericObjectType) {
        int start = genericObjectType.indexOf("<");
        int end = genericObjectType.lastIndexOf(">");
        //构建主类型
        String mainPackagePath = genericObjectType.substring(0, start);
        int mainIndex = mainPackagePath.lastIndexOf(".");
        String mainPackageName = mainPackagePath.substring(0, mainIndex);
        String mainClassName = mainPackagePath.substring(mainIndex + 1);
        ClassName mainClass = ClassName.get(mainPackageName, mainClassName);
        //构建泛型类型
        String innerPackagePath = genericObjectType.substring(start + 1, end);
//        int innerIndex = innerPackagePath.lastIndexOf(".");
//        String innerPackageName = innerPackagePath.substring(0, innerIndex);
//        String innerClassName = innerPackagePath.substring(innerIndex + 1);
//        ClassName innerClass = ClassName.get(innerPackageName, innerClassName);
//        ParameterizedTypeName obj = ParameterizedTypeName.get(mainClass, innerClass);
        List<TypeName> list = getParameterizedTypeName(innerPackagePath);
        ParameterizedTypeName obj = ParameterizedTypeName.get(mainClass, list.toArray(new TypeName[list.size()]));
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(obj)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", preference)
                .addStatement("String json = $N.getString($S, $S)", preference, key, "")
                .beginControlFlow("if(json.isEmpty())", preference)
                .addStatement("return null")
                .endControlFlow()
                .addStatement("return new $T().fromJson(json, new $T<$T>() {}.getType())", Gson.class, TypeToken.class, obj)
                .endControlFlow()
                .addStatement("return null")
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName         String
     * @param key               String
     * @param genericObjectType String
     * @return MethodSpec
     */
    public static MethodSpec createPutGenericObject(String fieldName, String key, String genericObjectType) {
        int start = genericObjectType.indexOf("<");
        int end = genericObjectType.lastIndexOf(">");
        //构建主类型
        String mainPackagePath = genericObjectType.substring(0, start);
        int mainIndex = mainPackagePath.lastIndexOf(".");
        String mainPackageName = mainPackagePath.substring(0, mainIndex);
        String mainClassName = mainPackagePath.substring(mainIndex + 1);
        ClassName mainClass = ClassName.get(mainPackageName, mainClassName);
        //构建泛型类型
        String innerPackagePath = genericObjectType.substring(start + 1, end);
//        int innerIndex = innerPackagePath.lastIndexOf(".");
//        String innerPackageName = innerPackagePath.substring(0, innerIndex);
//        String innerClassName = innerPackagePath.substring(innerIndex + 1);
//        ClassName innerClass = ClassName.get(innerPackageName, innerClassName);
//        ParameterizedTypeName obj = ParameterizedTypeName.get(mainClass, innerClass);
        List<TypeName> list = getParameterizedTypeName(innerPackagePath);
        ParameterizedTypeName obj = ParameterizedTypeName.get(mainClass, list.toArray(new TypeName[list.size()]));
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(obj, fieldName.toLowerCase())
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", editor)
                .addStatement("String json = \"\"")
                .beginControlFlow("if($N != null)", fieldName.toLowerCase())
                .addStatement("json = new $T().toJson($N)", Gson.class, fieldName.toLowerCase())
                .endControlFlow()
                .addStatement("$N.putString($S, json).apply()", editor, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * 获取类型集合，目前只支持两成嵌套
     */
    public static List<TypeName> getParameterizedTypeName(String innerPackagePath) {
        List<TypeName> list = new ArrayList<>();
        String[] paths = innerPackagePath.split(",");
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            if (path.contains("<")) {
                int start = path.lastIndexOf("<");
                int end = path.lastIndexOf(">");
                String pPath = path.substring(0, start);
                int index = pPath.lastIndexOf(".");
                String packageName = pPath.substring(0, index);
                String className = pPath.substring(index + 1);
                ClassName mClass = ClassName.get(packageName, className);
                //构建泛型类型
                String innerPath = path.substring(start + 1, end);
                int innerIndex = innerPath.lastIndexOf(".");
                String innerPackageName = innerPath.substring(0, innerIndex);
                String innerClassName = innerPath.substring(innerIndex + 1);
                ClassName innerClass = ClassName.get(innerPackageName, innerClassName);
                ParameterizedTypeName obj = ParameterizedTypeName.get(mClass, innerClass);
                list.add(obj);
            } else {
                int index = path.lastIndexOf(".");
                String packageName = path.substring(0, index);
                String className = path.substring(index + 1);
                ClassName mClass = ClassName.get(packageName, className);
                list.add(mClass);
            }
        }
        return list;
    }
}
