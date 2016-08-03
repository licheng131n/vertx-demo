package com.ifeng.vertx.demo.util;

import com.ifeng.vertx.demo.bean.Product;
import io.vertx.core.json.JsonObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * mongo客户端辅助类
 * Created by licheng1 on 2016/8/2.
 */
public class MongoClientUtils {

    public static void main(String[] args) {
        Product p = new Product();
        p.set_id("232323123asdasdasd");
        p.setName("TV");

        JsonObject document = bean2Update(p);
//        JsonObject document = bean2Document(p);
        System.out.println(document.toString());
    }

    /**
     * bean对象转换为mongo对象
     * @param t
     * @param <T>
     * @return
     */
    public static <T> JsonObject bean2Document(T t) {

        JsonObject document = new JsonObject();
        iterateFields(t, (field, value) -> {
            document.put(field.getName(), value);
        });
        return document;
    }

    /**
     * mongo对象转实体对象
     * @param clazz
     * @param document
     * @param <T>
     * @return
     */
    public static <T> T document2Bean(Class<T> clazz, JsonObject document) {
        T t = null;
        try {
            t = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = document.getValue(fieldName);
                if(value != null) {
                    field.set(t, value);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return t;
    }

    /**
     * bean对象转换为mongo更新对象
     * @param t
     * @param <T>
     * @return
     */
    public static <T> JsonObject bean2Update(T t) {
        JsonObject update = new JsonObject();
        JsonObject entry = bean2Document(t);
        update.put("$set", entry);
        return update;
    }

    /**
     * mongo集合转换为实体集合
     * @param clazz
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> List<T> collection2BeanList(Class<T> clazz, List<JsonObject> collection) {

        return collection.stream().map(jsonObject -> {
            return document2Bean(clazz, jsonObject);
        }).collect(Collectors.toList());
    }

    /**
     * 迭代实例所有属性,并映射
     * @param t
     * @param mapper
     * @param <T>
     */
    public static <T> void iterateFields(T t, Mapper mapper) {
        Field[] fields = t.getClass().getDeclaredFields();
        for(Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(t);
                if(value != null) {
                    mapper.map(field, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @FunctionalInterface
    private interface Mapper {
        void map(Field field, Object value);
    }
}
