package com.pref.processors;

import com.pref.annotations.DefaultBoolean;
import com.pref.annotations.DefaultFloat;
import com.pref.annotations.DefaultInt;
import com.pref.annotations.DefaultLong;
import com.pref.annotations.DefaultString;
import com.pref.annotations.ObjectType;
import com.pref.annotations.PrefKey;
import com.pref.annotations.SharePref;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Iterator;
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
import javax.lang.model.element.VariableElement;

/**
 * @author： Liudy
 * @description：
 * @date： 2022-09-16
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({
        "com.pref.annotations.SharePref",
        "com.pref.annotations.PrefKey",
        "com.pref.annotations.DefaultInt",
        "com.pref.annotations.DefaultFloat",
        "com.pref.annotations.DefaultLong",
        "com.pref.annotations.DefaultBoolean",
        "com.pref.annotations.DefaultString",
        "com.pref.annotations.ObjectType"
})
public class PrefProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) throws PreferenceProcessorsException {
        Iterator<?> iterator = roundEnvironment.getElementsAnnotatedWith(SharePref.class).iterator();
        while (iterator.hasNext()) {
            Element elementRoot = (Element) iterator.next();
            if (elementRoot == null) {
                continue;
            }
            SharePref sharePref = elementRoot.getAnnotation(SharePref.class);
            if (sharePref.enable()) {
                String classNameString = "";
                ClassName superClass = null;
                if (!sharePref.name().isEmpty()) {
                    MethodBuilder.PrefName = elementRoot.getAnnotation(SharePref.class).name();
                    MethodBuilder.PrefClassName = elementRoot.getSimpleName() + "_";
                    TypeElement typeElement = (TypeElement) elementRoot;
                    classNameString = typeElement.getQualifiedName().toString();
                    superClass = ClassName.bestGuess(classNameString);
                } else {
                    throw new PreferenceProcessorsException("Preference name must be initialized.");
                }
                TypeSpec.Builder builder = TypeSpec.classBuilder(MethodBuilder.PrefClassName);
                if (superClass != null) {
                    builder.superclass(superClass.withoutAnnotations());
                }
                builder.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addField(ClassName.get(MethodBuilder.PrefPackageName, MethodBuilder.PrefClassName), MethodBuilder.PrefInstanceName, Modifier.PRIVATE, Modifier.STATIC)
                        .addField(MethodBuilder.SharedPreferences, MethodBuilder.preference, Modifier.PRIVATE, Modifier.STATIC)
                        .addField(MethodBuilder.Editor, MethodBuilder.editor, Modifier.PRIVATE, Modifier.STATIC)
                        .addField(MethodBuilder.Context, MethodBuilder.context, Modifier.PRIVATE, Modifier.STATIC)
                        .addMethod(MethodBuilder.createInitialize())
                        .addMethod(MethodBuilder.createConstructor())
                        .addMethod(MethodBuilder.createInstance());
                for (Element element : roundEnvironment.getElementsAnnotatedWith(PrefKey.class)) {
                    PrefKey prefKey = element.getAnnotation(PrefKey.class);
                    if (!prefKey.enable()) {
                        continue;
                    }
                    TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                    String tempClassNameString = typeElement.getQualifiedName().toString();
                    if (!classNameString.equals(tempClassNameString)) {
                        continue;
                    }
                    String fieldName = element.getSimpleName().toString();
                    String key = prefKey.key();
                    key = key.isEmpty() ? fieldName.toLowerCase() : key;
                    builder.addMethod(MethodBuilder.createRemove(fieldName, key));
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
                            String objectType = null;
                            String genericObjectType = null;
                            if (element.getAnnotation(DefaultString.class) != null) {
                                defaultString = element.getAnnotation(DefaultString.class).value();
                            } else if (element.getAnnotation(ObjectType.class) != null) {
                                String type = element.asType().toString();
                                if (type.contains("<") || type.contains(">")) {
                                    genericObjectType = type;
                                } else {
                                    objectType = type;
                                }
                            }
                            if (objectType != null) {
                                builder.addMethod(MethodBuilder.createGetObject(fieldName, key, objectType));
                                builder.addMethod(MethodBuilder.createPutObject(fieldName, key, objectType));
                            } else if (genericObjectType != null) {
                                builder.addMethod(MethodBuilder.createGetGenericObject(fieldName, key, genericObjectType));
                                builder.addMethod(MethodBuilder.createPutGenericObject(fieldName, key, genericObjectType));
                            } else {
                                builder.addMethod(MethodBuilder.createGetString(fieldName, key, defaultString));
                                builder.addMethod(MethodBuilder.createPutString(fieldName, key));
                            }
                            break;
                        default:
                            break;
                    }
                }
                TypeSpec typeSpec = builder.build();
                JavaFile javaFile = JavaFile.builder(MethodBuilder.PrefPackageName, typeSpec).build();
                try {
                    javaFile.writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
