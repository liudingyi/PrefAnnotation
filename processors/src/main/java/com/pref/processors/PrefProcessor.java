package com.pref.processors;

import com.pref.annotations.DefaultBoolean;
import com.pref.annotations.DefaultFloat;
import com.pref.annotations.DefaultInt;
import com.pref.annotations.DefaultLong;
import com.pref.annotations.DefaultString;
import com.pref.annotations.PrefKey;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"com.pref.annotations.PrefKey"})
public class PrefProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        String class_name = MethodBuilder.PrefClassName;
        TypeSpec.Builder builder = TypeSpec.classBuilder(class_name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(MethodBuilder.SharedPreferences, MethodBuilder.preference, Modifier.PRIVATE)
                .addField(MethodBuilder.Editor, MethodBuilder.editor, Modifier.PRIVATE)
                .addMethod(MethodBuilder.createConstructor());
        for (Element element : roundEnvironment.getElementsAnnotatedWith(PrefKey.class)) {
            String fieldName = element.getSimpleName().toString();
            String key = element.getAnnotation(PrefKey.class).key();
            switch (element.asType().getKind()) {
                case INT:
                    int defaultInt = 0;
                    if (element.getAnnotation(DefaultInt.class) != null) {
                        defaultInt = element.getAnnotation(DefaultInt.class).value();
                    }
                    builder.addMethod(MethodBuilder.createGetInt(fieldName, key, defaultInt));
                    builder.addMethod(MethodBuilder.createPutInt(fieldName, key));
                    break;
                case FLOAT:
                    float defaultFloat = 0f;
                    if (element.getAnnotation(DefaultFloat.class) != null) {
                        defaultFloat = element.getAnnotation(DefaultFloat.class).value();
                    }
                    builder.addMethod(MethodBuilder.createGetFloat(fieldName, key, defaultFloat));
                    builder.addMethod(MethodBuilder.createPutFloat(fieldName, key));
                    break;
                case LONG:
                    long defaultLong = 0;
                    if (element.getAnnotation(DefaultLong.class) != null) {
                        defaultLong = element.getAnnotation(DefaultLong.class).value();
                    }
                    builder.addMethod(MethodBuilder.createGetLong(fieldName, key, defaultLong));
                    builder.addMethod(MethodBuilder.createPutLong(fieldName, key));
                    break;
                case BOOLEAN:
                    boolean defaultBoolean = false;
                    if (element.getAnnotation(DefaultBoolean.class) != null) {
                        defaultBoolean = element.getAnnotation(DefaultBoolean.class).value();
                    }
                    builder.addMethod(MethodBuilder.createGetBoolean(fieldName, key, defaultBoolean));
                    builder.addMethod(MethodBuilder.createPutBoolean(fieldName, key));
                    break;
                case DECLARED:
                    String defaultString = "";
                    if (element.getAnnotation(DefaultString.class) != null) {
                        defaultString = element.getAnnotation(DefaultString.class).value();
                    }
                    builder.addMethod(MethodBuilder.createGetString(fieldName, key, defaultString));
                    builder.addMethod(MethodBuilder.createPutString(fieldName, key));
                    break;
                default:
                    break;
            }
        }
        TypeSpec typeSpec = builder.build();
        String package_name = MethodBuilder.PrefPackageName;
        JavaFile javaFile = JavaFile.builder(package_name, typeSpec).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
