package com.ifeng.vertx.demo.Service;

import com.ifeng.vertx.demo.bean.Product;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by licheng1 on 2016/8/1.
 */
public interface ProductService {

    String rpcAddress = "productService";

    //保存商品
    void save(Product product, Handler<AsyncResult<String>> resultHandler);
    //更新商品
    void update(Product product, Handler<AsyncResult<Long>> resultHandler);
    //查询集合所有元素
    void collectionList(Handler<AsyncResult<List<Product>>> resultHandler);
    //删除商品
    void remove(Product product, Handler<AsyncResult<Long>> resultHandler);
    //查询一个产品
    void findOne(Map<String, Object> parameters, Handler<AsyncResult<Product>> resultHandler);
    //查询产品，可查询多个
    void find(Map<String, Object> parameters, Handler<AsyncResult<List<Product>>> resultHandler);
    //统计符合条件的元素个数
    void count(Map<String, Object> parameters, Handler<AsyncResult<Long>> resultHandler);
}
