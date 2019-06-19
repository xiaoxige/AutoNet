package cn.xiaoxige.autonet_api.util;

/**
 * @author xiaoxige
 * @date 2019/6/19 0019 16:44
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: Determine whether the classes are the same, or whether the parent classes are the same, similar to InstanceOf, but this compares Classes
 */
public class ClassInstanceofPlusUtil {

    public static boolean instanceOf(Object o, Class clazz) {

        if (o == null || clazz == null) {
            return false;
        }

        if (clazz.equals(Object.class)) {
            return true;
        }
        return isSomeObj(o, clazz);
    }

    private static boolean isSomeObj(Object o, Class clazz) {
        if (o == null || o instanceof Class) {
            return false;
        }

        Class<?> oClass = o.getClass();
        // check self
        if (oClass.equals(clazz)) {
            return true;
        }

        // check interface
        Class<?>[] interfaces = oClass.getInterfaces();
        for (Class interfaceClass : interfaces) {
            if (isSomeInterface(interfaceClass, clazz)) {
                return true;
            }
        }

        // check parent
        if (isSomeObj(oClass.getSuperclass(), clazz)) {
            return true;
        }
        return false;
    }

    private static boolean isSomeInterface(Class iClazz, Class clazz) {
        if (iClazz == null) {
            return false;
        }

        // check self
        if (iClazz.equals(clazz)) {
            return true;
        }

        // check parent
        Class[] interfaces = iClazz.getInterfaces();
        for (Class interfaceClass : interfaces) {
            if (isSomeInterface(interfaceClass, clazz)) {
                return true;
            }
        }

        return false;
    }

}
