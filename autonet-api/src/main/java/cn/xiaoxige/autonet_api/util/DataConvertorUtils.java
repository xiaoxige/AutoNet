package cn.xiaoxige.autonet_api.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据转换工具类,比如model->entity
 */
public class DataConvertorUtils {
    /**
     * 将数据表model转换为entity
     *
     * @param source 数据表对应的model
     * @param result 结果数据
     */
    public static <T, R> R convertModelToEntity(T source, R result) {

        Field[] fields = source.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                String fieldName = field.getName();
                Field targetField = result.getClass().getDeclaredField(fieldName);
                targetField.setAccessible(true);

                targetField.set(result, field.get(source));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将数据表entity转换为model
     *
     * @param source 数据实体
     * @param result 结果数据
     */
    public static <T, R> R convertEntityToModel(T source, R result) {
        return convertModelToEntity(source, result);
    }

    /**
     * 将实体类的字段和值映射为map,仅考虑类型字段是String或者ArrayList<String>类型的
     *
     * @param source             实体数据
     * @param includeParentClass 是否包含父类中的字段
     * @return 返回以字段名为key, 字段值为value的map
     */
    public static <T> Map<String, String> convertEntityToMap(T source, boolean includeParentClass) {
        Map<String, String> results = new HashMap<>();
        Field[] fields = source.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String fieldName = field.getName();
                Object fieldValue = field.get(source);
                if (fieldValue == null) {
                    continue;
                }

                if (fieldValue instanceof ArrayList) {
                    results.put(fieldName, ((ArrayList<String>) fieldValue).toString());
                } else {
                    results.put(fieldName, fieldValue.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (includeParentClass) {
            convertParentEntityToMap(results, source);
        }
        return results;
    }

    /**
     * 递归的将父类声明的属性和值映射到map中,仅考虑类型字段是String或者ArrayList<String>类型的
     *
     * @param map 存储属性和值的map
     * @return 返回以字段名为key, 字段值为value的map
     */
    public static <T> Map<String, String> convertParentEntityToMap(Map<String, String> map, T source) {
        if (map == null) {
            map = new HashMap<>();
        }
        Class<?> clazz = source.getClass().getSuperclass();

        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    String fieldName = field.getName();
                    Object fieldValue = field.get(source);

                    if (fieldValue == null) {
                        continue;
                    }

                    if (fieldValue instanceof ArrayList) {
                        map.put(fieldName, ((ArrayList<String>) fieldValue).toString());
                    } else {
                        map.put(fieldName, fieldValue.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            clazz = clazz.getSuperclass();
        }

        return map;
    }
}