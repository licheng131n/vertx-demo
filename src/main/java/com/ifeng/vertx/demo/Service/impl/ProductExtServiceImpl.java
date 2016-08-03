package com.ifeng.vertx.demo.Service.impl;

import com.ifeng.vertx.demo.Service.ProductExtService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

/**
 * Created by licheng1 on 2016/8/3.
 */
public class ProductExtServiceImpl implements ProductExtService {

    private Vertx vertx;
    private MongoClient mongoClient;

    public ProductExtServiceImpl(Vertx vertx) {
        this.vertx = vertx;

        //初始化mongoClient
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
    public void save(JsonObject product, Handler<AsyncResult<String>> resultHandler) {
        mongoClient.save("products", product, result -> {
            if(result.succeeded()) {
                resultHandler.handle(Future.succeededFuture(result.result()));
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
    }

    @Override
    public void collectionList(Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        JsonObject query = new JsonObject();
        mongoClient.find("products", query, result -> {
            if(result.succeeded()) {
                resultHandler.handle(Future.succeededFuture(result.result()));
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
    }

    @Override
    public void remove(JsonObject product, Handler<AsyncResult<Long>> resultHandler) {
        mongoClient.removeDocument("products", product, result -> {
            if(result.succeeded()) {
                resultHandler.handle(Future.succeededFuture(result.result().getRemovedCount()));
            } else {
                resultHandler.handle(Future.failedFuture(result.cause()));
            }
        });
    }
}
