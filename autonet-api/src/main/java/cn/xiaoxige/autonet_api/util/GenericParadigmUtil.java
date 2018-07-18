package cn.xiaoxige.autonet_api.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author by zhuxiaoan on 2018/7/18 0018.
 *         泛型相关的工具类
 */

public class GenericParadigmUtil {

    public static Class parseClass(Object o) {
        return parseClass(o, 0);
    }

    public static Class parseClass(Object o, int index) {
        Class clazz;

        clazz = tryRequtstClass1(o, index);
        if (clazz == null) {
            clazz = tryRequtstClass2(o, index);
        }

        return clazz;
    }

    private static Class tryRequtstClass1(Object o, int index) {
        Class clazz = null;
        try {
            Type genType = o.getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            clazz = (Class) params[index];
        } catch (Exception e) {
        }

        return clazz;
    }

    private static Class tryRequtstClass2(Object o, int index) {
        Class clazz = null;

        try {
            Type[] genType = o.getClass().getGenericInterfaces();
            Type[] params = ((ParameterizedType) genType[0]).getActualTypeArguments();
            clazz = (Class) params[index];
        } catch (Exception e) {
        }

        return clazz;
    }

}
