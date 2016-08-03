package com.ifeng.vertx.demo.bean;

/**
 * 产品pojo
 * Created by licheng1 on 2016/8/2.
 */
public class Product {

    /**
     * id
     */
    private String _id;
    /**
     * 产品名称
     */
    private String name;

    public Product() {
    }

    public Product(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
