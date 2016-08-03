package com.ifeng.vertx.demo;

import com.ifeng.vertx.demo.verticle.HttpPostServerVerticle;
import com.ifeng.vertx.demo.verticle.HttpServerVerticle;
import com.ifeng.vertx.demo.verticle.ProductExtRPCServerVerticle;
import com.ifeng.vertx.demo.verticle.ProductRPCServerVerticle;
import io.vertx.core.*;

import java.util.function.Consumer;

/**
 * demo启动类
 * Created by licheng1 on 2016/8/1.
 */
public class ServerStart extends AbstractVerticle {

    public static void main(String[] args) {

        //默认设置
        VertxOptions options = new VertxOptions();
        //启用集群模式
//        options.setClustered(true);

        //默认部署参数
        DeploymentOptions deploymentOptions = new DeploymentOptions();

        //部署启动verticle
        run(ServerStart.class, options, deploymentOptions);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        //部署verticles
        //需要部署的verticle

        //部署rpc远程调用服务
//        vertx.deployVerticle(ProductRPCServerVerticle.class.getName());
        vertx.deployVerticle(ProductExtRPCServerVerticle.class.getName());
        //部署httpServer
//        vertx.deployVerticle(HttpServerVerticle.class.getName());
        vertx.deployVerticle(HttpPostServerVerticle.class.getName());
    }

    /**
     * 根据设置启动集群或非集群模式
     * @param clazz
     * @param options
     * @param deploymentOptions
     */
    public static void run(Class<?> clazz, VertxOptions options, DeploymentOptions deploymentOptions) {
        Consumer<Vertx> runner = vertx -> {
            try {
                if (deploymentOptions != null) {
                    vertx.deployVerticle(clazz.getName(), deploymentOptions);
                } else {
                    vertx.deployVerticle(clazz.getName());
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };
        if (options.isClustered()) {
            Vertx.clusteredVertx(options, res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    runner.accept(vertx);
                } else {
                    res.cause().printStackTrace();
                }
            });
        } else {
            Vertx vertx = Vertx.vertx(options);
            runner.accept(vertx);
        }
    }

}
