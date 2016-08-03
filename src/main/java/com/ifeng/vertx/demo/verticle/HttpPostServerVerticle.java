package com.ifeng.vertx.demo.verticle;

import as.leap.vertx.rpc.impl.RPCClientOptions;
import as.leap.vertx.rpc.impl.VertxRPCClient;
import com.ifeng.vertx.demo.Service.ProductExtService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by licheng1 on 2016/8/3.
 */
public class HttpPostServerVerticle extends AbstractVerticle {
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
        RPCClientOptions<ProductExtService> rpcClientOptions = new RPCClientOptions<ProductExtService>(vertx)
                .setBusAddress(ProductExtService.rpcAddress)
                .setServiceClass(ProductExtService.class);
        //绑定服务
        productExtService = new VertxRPCClient<ProductExtService>(rpcClientOptions).bindService();

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        //请求体可读
        router.route().handler(BodyHandler.create());

        /*
        route设置
         */
        router.route().handler(this::handleAll);
        router.route("/demo/save").handler(this::handleSave);
        router.route("/demo/remove").handler(this::handleRemove);
        router.route("/demo/list").handler(this::handleList);


        //监听8080端口等待接收请求
        server.requestHandler(router::accept).listen(8080);
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
            if(result.succeeded()) {
                responseEnd("保存成功,_id : " + result.result(), routingContext);
            } else {
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 删除请求处理handle
     * @param routingContext
     */
    public void handleRemove(RoutingContext routingContext) {
        JsonObject product = routingContext.getBodyAsJson();
        productExtService.remove(product, result -> {
            if(result.succeeded()) {
                responseEnd("删除成功,被删除数量 : " + result.result(), routingContext);
            } else {
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 产品列表请求handle
     * @param routingContext
     */
    public void handleList(RoutingContext routingContext) {
        productExtService.collectionList(result -> {
            if(result.succeeded()) {
                responseEnd("产品列表获取成功,products : " + result.result().toString(), routingContext);
            } else {
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 输出
     * @param out
     * @param routingContext
     */
    public void responseEnd(String out, RoutingContext routingContext) {
        routingContext.response().end(out);
        routingContext.response().close();
    }

    /**
     * 响应码
     * @param code
     * @param routingContext
     */
    public void statusCode(int code, RoutingContext routingContext) {
        routingContext.response().setStatusCode(code).end();
        routingContext.response().close();
    }
}
