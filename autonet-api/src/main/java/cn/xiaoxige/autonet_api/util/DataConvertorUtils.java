package cn.xiaoxige.autonet_api.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Data conversion tool class, such as model->entity
 */
public class DataConvertorUtils {
    /**
     * convert the data table model to entity
     *
     * @param source data table corresponding to model
     * @param result result data
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
     * convert the data table entity to model
     *
     * @param source data entity
     * @param result result data
     */
    public static <T, R> R convertEntityToModel(T source, R result) {
        return convertModelToEntity(source, result);
    }

    /**
     * map the fields and values of entity classes to map, considering only type fields that are String or ArrayList<String> types.
     *
     * @param source entity data
     *               does @param includeParentClass contain fields in the parent class?
     * @return returns map with field name key and field value value.
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
     * mapping the attributes and values declared by the parent class to map recursively, only considering the type fields are String or ArrayList<String> types.
     *
     * @param map storage properties and values of map
     * @return returns map with field name key and field value value.
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