package com.ifeng.vertx.demo.Service.impl;

import com.ifeng.vertx.demo.Service.ProductService;
import com.ifeng.vertx.demo.bean.Product;
import com.ifeng.vertx.demo.util.MongoClientUtils;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;
import java.util.Map;

/**
 * mongo操作业务实现类
 * Created by licheng1 on 2016/8/1.
 */
public class ProductServiceImpl implements ProductService {

    private Vertx vertx;
    private MongoClient mongoClient;

    public ProductServiceImpl(Vertx vertx) {

        this.vertx = vertx;
        //初始化mongo客户端
        JsonObject config = Vertx.currentContext().config();

        String uri = config.getString("mongo_uri");
        if (uri == null) {
//            uri = "mongodb://localhost:27017";
            uri = "mongodb://localhost:27017";
        }
        String db = config.getString("demo_db");
        if (db == null) {
            db = "test";
        }
        JsonObject mongoconfig = new JsonObject()
                .put("connection_string", uri)
                .put("db_name", db);

        if (vertx.isClustered()) {
            //集群模式采用共享数据源
            mongoClient = MongoClient.createShared(this.vertx, mongoconfig);
        } else {
            mongoClient = MongoClient.createNonShared(this.vertx, mongoconfig);
        }
    }

    @Override
    public void save(Product product, Handler<AsyncResult<String>> resultHandler) {

        JsonObject document = MongoClientUtils.bean2Document(product);
        if (document.isEmpty()) {
            resultHandler.handle(Future.failedFuture("object is empty"));
        } else {
            mongoClient.save("products", document, result -> {
                if (result.succeeded()) {
                    String id = result.result();
                    resultHandler.handle(Future.succeededFuture(id));
                } else {
                    resultHandler.handle(Future.failedFuture(result.cause()));
                }
            });
        }

    }


    @Override
    public void update(Product product, Handler<AsyncResult<Long>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", product.get_id());

        JsonObject update = MongoClientUtils.bean2Update(product);
        mongoClient.updateCollection("products", query, update, result -> {
            if (result.succeeded()) {
                resultHandler.handle(Future.succeededFuture(result.result().getDocModified()));
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
    }

    @Override
    public void collectionList(Handler<AsyncResult<List<Product>>> resultHandler) {
        JsonObject query = new JsonObject();

        mongoClient.find("products", query, result -> {
            if (result.succeeded()) {
                resultHandler.handle(Future.succeededFuture(MongoClientUtils.collection2BeanList(Product.class, result.result())));
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
    }

    @Override
    public void remove(Product product, Handler<AsyncResult<Long>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", product.get_id());

        mongoClient.removeDocument("products", query, result -> {
            if (result.succeeded()) {
                resultHandler.handle(Future.succeededFuture(result.result().getRemovedCount()));
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
    }

    @Override
    public void findOne(Map<String, Object> parameters, Handler<AsyncResult<Product>> resultHandler) {
        JsonObject query = new JsonObject(parameters);
        JsonObject fields = new JsonObject();

        mongoClient.findOne("products", query, fields, result -> {
            if (result.succeeded()) {
                resultHandler.handle(Future.succeededFuture(MongoClientUtils.document2Bean(Product.class, result.result())));
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
    }

    @Override
    public void find(Map<String, Object> parameters, Handler<AsyncResult<List<Product>>> resultHandler) {
        JsonObject query = new JsonObject(parameters);
        mongoClient.find("products", query, result -> {
            if (result.succeeded()) {
                resultHandler.handle(Future.succeededFuture(MongoClientUtils.collection2BeanList(Product.class, result.result())));
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
    }

    @Override
    public void count(Map<String, Object> parameters, Handler<AsyncResult<Long>> resultHandler) {
        JsonObject query = new JsonObject(parameters);

        mongoClient.count("products", query, result -> {
            if (result.succeeded()) {
                resultHandler.handle(Future.succeededFuture(result.result()));
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
    }

}
