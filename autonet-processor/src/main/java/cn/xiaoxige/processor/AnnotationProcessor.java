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
import cn.xiaoxige.annotation.AutoNetDisposableBaseUrlAnontation;
import cn.xiaoxige.annotation.AutoNetDisposableHeadAnnontation;
import cn.xiaoxige.annotation.AutoNetEncryptionAnontation;
import cn.xiaoxige.annotation.AutoNetMediaTypeAnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.annotation.entity.ProxyInfo;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 *         Annotate the processor, collect the content of the annotations and handle
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
        Set<String> set = new HashSet<>(10);
        set.add(AutoNetPatternAnontation.class.getCanonicalName());
        set.add(AutoNetEncryptionAnontation.class.getCanonicalName());
        set.add(AutoNetBaseUrlKeyAnontation.class.getCanonicalName());
        set.add(AutoNetResponseEntityClass.class.getCanonicalName());
        set.add(AutoNetTypeAnontation.class.getCanonicalName());
        set.add(AutoNetMediaTypeAnontation.class.getCanonicalName());
        set.add(AutoNetAnontation.class.getCanonicalName());
        set.add(AutoNetStrategyAnontation.class.getCanonicalName());
        set.add(AutoNetDisposableBaseUrlAnontation.class.getCanonicalName());
        set.add(AutoNetDisposableHeadAnnontation.class.getCanonicalName());
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

        if (!isAnnotatedWithClass(roundEnvironment, AutoNetMediaTypeAnontation.class)) {
            return false;
        }

        if (!isAnnotatedWithClass(roundEnvironment, AutoNetTypeAnontation.class)) {
            return false;
        }

        if (!isAnnotatedWithClass(roundEnvironment, AutoNetStrategyAnontation.class)) {
            return false;
        }

        if (!isAnnotatedWithClass(roundEnvironment, AutoNetDisposableBaseUrlAnontation.class)) {
            return false;
        }

        if (!isAnnotatedWithClass(roundEnvironment, AutoNetDisposableHeadAnnontation.class)) {
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

            String fullTargetPath = typeElement.getQualifiedName().toString();
            String targetPackage = mElementUtils.getPackageOf(element).getQualifiedName().toString();
            String targetClassSimpleName = typeElement.getSimpleName().toString();

            ProxyInfo proxyInfo = mInfoMap.get(fullTargetPath);
            if (proxyInfo == null) {
                proxyInfo = new ProxyInfo();
                mInfoMap.put(fullTargetPath, proxyInfo);
            }

            proxyInfo.fullTargetPath = fullTargetPath;
            proxyInfo.targetPackage = targetPackage;
            proxyInfo.targetClassSimpleName = targetClassSimpleName;

            Annotation annotation = typeElement.getAnnotation(clazz);
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
            } else if (annotation instanceof AutoNetTypeAnontation) {
                autoNetReqTypeProc(proxyInfo, (AutoNetTypeAnontation) annotation);
            } else if (annotation instanceof AutoNetMediaTypeAnontation) {
                autoNetMediaTypeProc(proxyInfo, (AutoNetMediaTypeAnontation) annotation);
            } else if (annotation instanceof AutoNetStrategyAnontation) {
                autoNetStrategyProc(proxyInfo, (AutoNetStrategyAnontation) annotation);
            } else if (annotation instanceof AutoNetDisposableBaseUrlAnontation) {
                autoNetDisposableBaseUrlProc(proxyInfo, (AutoNetDisposableBaseUrlAnontation) annotation);
            } else if (annotation instanceof AutoNetDisposableHeadAnnontation) {
                autoNetDisposableHeadProc(proxyInfo, (AutoNetDisposableHeadAnnontation) annotation);
            } else {
                return false;
            }
        }
        return true;
    }

    private void autoNetDisposableHeadProc(ProxyInfo proxyInfo, AutoNetDisposableHeadAnnontation annotation) {
        String[] disposableHeads = annotation.value();
        proxyInfo.disposableHeads = disposableHeads;
    }

    private void autoNetDisposableBaseUrlProc(ProxyInfo proxyInfo, AutoNetDisposableBaseUrlAnontation annotation) {
        String disposableBaseUrl = annotation.value();
        proxyInfo.disposableBaseUrl = disposableBaseUrl;
    }

    private void autoNetStrategyProc(ProxyInfo proxyInfo, AutoNetStrategyAnontation annotation) {
        AutoNetStrategyAnontation.NetStrategy netStrategy = annotation.value();
        proxyInfo.netStrategy = netStrategy;
    }

    private void autoNetMediaTypeProc(ProxyInfo proxyInfo, AutoNetMediaTypeAnontation annotation) {
        String mediaType = annotation.value();
        proxyInfo.mediaType = mediaType;
    }

    private void autoNetReqTypeProc(ProxyInfo proxyInfo, AutoNetTypeAnontation annotation) {
        AutoNetTypeAnontation.Type reqType = annotation.reqType();
        AutoNetTypeAnontation.Type resType = annotation.resType();
        proxyInfo.reqType = reqType;
        proxyInfo.resType = resType;
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
        proxyInfo.domainNameKey = value;
    }

    private void autoNetProc(ProxyInfo proxyInfo, AutoNetAnontation annotation) {
        String suffixUrl = annotation.value();
        long writeOutTime = annotation.writeTime();
        long readOutTime = annotation.readTime();
        long connectOutTime = annotation.connectOutTime();
        proxyInfo.suffixUrl = suffixUrl;
        proxyInfo.writeOutTime = writeOutTime;
        proxyInfo.readOutTime = readOutTime;
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
