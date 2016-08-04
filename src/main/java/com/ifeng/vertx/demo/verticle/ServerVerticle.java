package com.ifeng.vertx.demo.verticle;

import as.leap.vertx.rpc.impl.RPCClientOptions;
import as.leap.vertx.rpc.impl.VertxRPCClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by licheng1 on 2016/8/4.
 */
public abstract class ServerVerticle extends AbstractVerticle {

    /**
     *  绑定RPC服务
     * @param clazz
     * @param busAddress
     * @param <T>
     * @return
     */
    protected <T> T bindService(Class<T> clazz, String busAddress) {
        RPCClientOptions<T> rpcClientOptions = new RPCClientOptions<T>(vertx)
                .setBusAddress(busAddress)
                .setServiceClass(clazz);
        return new VertxRPCClient<T>(rpcClientOptions).bindService();
    }

    /**
     * 启动httpServer监听指定端口
     * @param port
     */
    public void startServer(int port) {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        //请求路径过滤及处理handle
        routes(router);
        //监听端口等待接收请求
        server.requestHandler(router::accept).listen(port);
    }

    /**
     * 输出
     * @param out
     * @param routingContext
     */
    protected void responseEnd(String out, RoutingContext routingContext) {
        routingContext.response().end(out);
        routingContext.response().close();
    }

    /**
     * 响应码
     * @param code
     * @param routingContext
     */
    protected void statusCode(int code, RoutingContext routingContext) {
        routingContext.response().setStatusCode(code).end();
        routingContext.response().close();
    }

    /**
     * 请求路径handle设置
     * @param router
     */
    protected abstract void routes(Router router);
}
