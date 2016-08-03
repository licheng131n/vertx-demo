package com.ifeng.vertx.demo.Service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * 扩展产品服务，参数统一接收json对象
 * Created by licheng1 on 2016/8/3.
 */
public interface ProductExtService {

    String rpcAddress = "jsonProductAddress";

    void save(JsonObject product, Handler<AsyncResult<String>> resultHandler);

    void collectionList(Handler<AsyncResult<List<JsonObject>>> resultHandler);

    void remove(JsonObject product, Handler<AsyncResult<Long>> resultHandler);
}
