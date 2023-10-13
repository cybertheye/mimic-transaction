package xyz.cybertheye.utils;

import xyz.cybertheye.bean.Item;

import java.lang.reflect.Field;

/**
 * @description:
 */
public class BeanUtils {
    public static <T> void copyProperties(T src,T des) throws IllegalAccessException {
        Class<?> srcClazz = src.getClass();
        Class<?> desClazz = des.getClass();

        while(srcClazz != Object.class){
            Field[] srcFields = srcClazz.getDeclaredFields();
            Field[] desFields = desClazz.getDeclaredFields();

            for(int i = 0 ; i < srcFields.length;i++){
                Field desField = desFields[i];
                Field srcField = srcFields[i];
                desField.setAccessible(true);
                srcField.setAccessible(true);

                desField.set(des, srcField.get(src));
            }

            srcClazz = srcClazz.getSuperclass();
            desClazz = desClazz.getSuperclass();
        }
    }
}
