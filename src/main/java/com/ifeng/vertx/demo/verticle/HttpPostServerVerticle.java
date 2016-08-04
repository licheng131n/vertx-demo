package com.ifeng.vertx.demo.verticle;

import com.ifeng.vertx.demo.Service.ProductExtService;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by licheng1 on 2016/8/3.
 */
public class HttpPostServerVerticle extends ServerVerticle {
    /**
     * mongo操作业务
     */
    private ProductExtService productExtService;

    /**
     * 启动httpServer，初始化微服务RPC调用以及请求处理
     *
     * @param startFuture
     * @throws Exception
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {

        //mongo微服务RPC调用客户端
        productExtService = bindService(ProductExtService.class, ProductExtService.rpcAddress);

        //启动httpServer
        startServer(8080);
    }

    @Override
    public void routes(Router router) {
        /*
        route设置
         */
        router.route().handler(BodyHandler.create());
        router.route().handler(this::handleAll);
        router.route("/demo/list").handler(this::handleList);
        router.route("/demo/save").handler(this::handleSave);
        router.route("/demo/update").handler(this::handleUpdate);
        router.route("/demo/remove").handler(this::handleRemove);
        router.route("/demo/findOne").handler(this::handleFindOne);
        router.route("/demo/find").handler(this::handleFind);
        router.route("/demo/count").handler(this::handleCount);
    }

    /**
     * 更新请求处理handle
     * @param routingContext
     */
    public void handleUpdate(RoutingContext routingContext) {
        JsonObject product = routingContext.getBodyAsJson();
        productExtService.update(product, result -> {
            if (result.succeeded()) {
                responseEnd("update成功,更新数量 : " + result.result(), routingContext);
            } else {
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 单个实体查询请求处理handle
     * @param routingContext
     */
    public void handleFindOne(RoutingContext routingContext) {
        JsonObject parameters = routingContext.getBodyAsJson();
        productExtService.findOne(parameters, result -> {
            if (result.succeeded()) {
                responseEnd("findOne成功,product : " + result.result(), routingContext);
            } else {
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 多个实体查询请求处理handle
     * @param routingContext
     */
    public void handleFind(RoutingContext routingContext) {
        JsonObject parameters = routingContext.getBodyAsJson();
        productExtService.find(parameters, result -> {
            if (result.succeeded()) {
                responseEnd("find成功,products : " + result.result().toString(), routingContext);
            } else {
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 按条件统计实体个数请求处理handle
     * @param routingContext
     */
    public void handleCount(RoutingContext routingContext) {
        JsonObject parameters = routingContext.getBodyAsJson();
        productExtService.count(parameters, result -> {
            if (result.succeeded()) {
                responseEnd("count成功,匹配数量 : " + result.result(), routingContext);
            } else {
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 全请求过滤
     * 设置写响应
     * 设置content-type响应头
     *
     * @param routingContext
     */
    public void handleAll(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.setChunked(true);
        response.putHeader("content-type", "text/plain;charset=utf-8");
        //调用后续handle
        routingContext.next();
    }

    /**
     * 保存请求处理handle
     *
     * @param routingContext
     */
    public void handleSave(RoutingContext routingContext) {
        JsonObject product = routingContext.getBodyAsJson();
        productExtService.save(product, result -> {
            if (result.succeeded()) {
                responseEnd("save成功,_id : " + result.result(), routingContext);
            } else {
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 删除请求处理handle
     *
     * @param routingContext
     */
    public void handleRemove(RoutingContext routingContext) {
        JsonObject product = routingContext.getBodyAsJson();
        productExtService.remove(product, result -> {
            if (result.succeeded()) {
                responseEnd("remove成功,被删除数量 : " + result.result(), routingContext);
            } else {
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 产品列表请求handle
     *
     * @param routingContext
     */
    public void handleList(RoutingContext routingContext) {
        productExtService.collectionList(result -> {
            if (result.succeeded()) {
                responseEnd("产品列表获取成功,products : " + result.result().toString(), routingContext);
            } else {
                statusCode(400, routingContext);
            }
        });
    }

}
