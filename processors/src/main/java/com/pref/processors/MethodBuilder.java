package com.pref.processors;

import com.google.gson.Gson;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

public class MethodBuilder {

    public static final String PrefPackageName = "com.pref";
    public static String PrefClassName = "Pref";
    public static final String PrefInstanceName = "instance";
    public static String PrefName = "pref_data";

    private static final ClassName Context = ClassName.get("android.content", "Context");
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
                .addParameter(Context, "context")
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
}
