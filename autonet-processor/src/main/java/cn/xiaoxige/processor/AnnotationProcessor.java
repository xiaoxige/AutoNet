package cn.xiaoxige.processor;

import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import cn.xiaoxige.annotation.AutoNetAnontation;
import cn.xiaoxige.annotation.AutoNetBaseUrlKeyAnontation;
import cn.xiaoxige.annotation.AutoNetEncryptionAnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetResponseEntityClass;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private Elements mElementUtils;
    private Filer mFiler;
    private Messager mMessager;

    private Map<String, ProxyInfo> mInfoMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mInfoMap = new HashMap<>();
        mElementUtils = processingEnvironment.getElementUtils();
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set set = new HashSet();
        set.add(AutoNetPatternAnontation.class.getCanonicalName());
        set.add(AutoNetEncryptionAnontation.class.getCanonicalName());
        set.add(AutoNetBaseUrlKeyAnontation.class.getCanonicalName());
        set.add(AutoNetAnontation.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        if (set == null || set.size() <= 0) {
            return false;
        }

        if (!isAnnotatedWithClass(roundEnvironment, AutoNetPatternAnontation.class)) {
            return false;
        }

        if (!isAnnotatedWithClass(roundEnvironment, AutoNetEncryptionAnontation.class)) {
            return false;
        }

        if (!isAnnotatedWithClass(roundEnvironment, AutoNetBaseUrlKeyAnontation.class)) {
            return false;
        }

        if (!isAnnotatedWithClass(roundEnvironment, AutoNetAnontation.class)) {
            return false;
        }

        if (!isAnnotatedWithClass(roundEnvironment, AutoNetResponseEntityClass.class)) {
            return false;
        }

        try {
            ProxyWriteUtil.write(mInfoMap, mFiler);
        } catch (Exception e) {
            printError(e.toString());
        } finally {
            mInfoMap.clear();
        }
        return true;
    }

    private boolean isAnnotatedWithClass(RoundEnvironment roundEnvironment,
                                         Class<? extends Annotation> clazz) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(clazz);
        for (Element element : elements) {
            if (isValid(element)) {
                return false;
            }
            if (!element.getKind().isClass()) {
                return false;
            }
            TypeElement typeElement = (TypeElement) element;
            String fullPackageName = typeElement.getQualifiedName().toString();
            String packageName = mElementUtils.getPackageOf(element).getQualifiedName().toString();
            String className = typeElement.getSimpleName().toString();
            Annotation annotation = typeElement.getAnnotation(clazz);

            ProxyInfo proxyInfo = mInfoMap.get(fullPackageName);
            if (proxyInfo == null) {
                proxyInfo = new ProxyInfo();
                mInfoMap.put(fullPackageName, proxyInfo);
            }
            proxyInfo.fullPackageName = fullPackageName;
            proxyInfo.className = className;
            proxyInfo.packageName = packageName;
            proxyInfo.typeElement = typeElement;

            if (fullPackageName != null && fullPackageName.length() > 0) {
                String outClassFullName;
                try {
                    outClassFullName = fullPackageName.substring(0, fullPackageName.length() - className.length() - 1);
                    if (outClassFullName.equals(packageName)) {
                        outClassFullName = fullPackageName;
                    }
                } catch (Exception e) {
                    outClassFullName = "";
                }
                proxyInfo.outClassFullPackageName = outClassFullName;
            }


            if (annotation instanceof AutoNetPatternAnontation) {
                autoNetPatternProc(proxyInfo, (AutoNetPatternAnontation) annotation);
            } else if (annotation instanceof AutoNetEncryptionAnontation) {
                autoNetEncryptionProc(proxyInfo, (AutoNetEncryptionAnontation) annotation);
            } else if (annotation instanceof AutoNetBaseUrlKeyAnontation) {
                autoNetBaseUrlKeyProc(proxyInfo, (AutoNetBaseUrlKeyAnontation) annotation);
            } else if (annotation instanceof AutoNetAnontation) {
                autoNetProc(proxyInfo, (AutoNetAnontation) annotation);
            } else if (annotation instanceof AutoNetResponseEntityClass) {
                autoNetResponseEntityClassProc(proxyInfo, (AutoNetResponseEntityClass) annotation, element);
            } else {
                return false;
            }
        }
        return true;
    }


    private void autoNetPatternProc(ProxyInfo proxyInfo, AutoNetPatternAnontation annotation) {
        AutoNetPatternAnontation.NetPattern value = annotation.value();
        proxyInfo.netPattern = value;
    }

    private void autoNetEncryptionProc(ProxyInfo proxyInfo, AutoNetEncryptionAnontation annotation) {
        boolean value = annotation.value();
        long key = annotation.key();
        proxyInfo.encryptionKey = key;
        proxyInfo.isEncryption = value;
    }

    private void autoNetBaseUrlKeyProc(ProxyInfo proxyInfo, AutoNetBaseUrlKeyAnontation annotation) {
        String value = annotation.value();
        proxyInfo.baseUrlKey = value;
    }

    private void autoNetProc(ProxyInfo proxyInfo, AutoNetAnontation annotation) {
        String url = annotation.url();
        long writeTime = annotation.writeTime();
        long readTime = annotation.readTime();
        long connectOutTime = annotation.connectOutTime();
        proxyInfo.url = url;
        proxyInfo.writeTime = writeTime;
        proxyInfo.readTime = readTime;
        proxyInfo.connectOutTime = connectOutTime;
    }

    private void autoNetResponseEntityClassProc(ProxyInfo proxyInfo, AutoNetResponseEntityClass annotation, Element element) {
        try {
            annotation.value();
        } catch (MirroredTypeException e) {
            String responseEntityClassName = e.getTypeMirror().toString();
            proxyInfo.responseClazzName = responseEntityClassName;
        }
    }

    private void printError(String error) {
        this.mMessager.printMessage(Diagnostic.Kind.ERROR, error);
    }

    private void printNote(String note) {
        this.mMessager.printMessage(Diagnostic.Kind.NOTE, note);
    }

    private boolean isValid(Element element) {
        if (element.getModifiers().contains(Modifier.ABSTRACT) || element.getModifiers().contains
                (Modifier.PRIVATE)) {
            printError(element.getSimpleName() + "must could not be abstract or private");
            return true;
        }

        return false;
    }

}
