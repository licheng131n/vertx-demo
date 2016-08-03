package com.ifeng.vertx.demo.test;

import com.ifeng.vertx.demo.verticle.ProductRPCClientVerticle;
import com.ifeng.vertx.demo.verticle.ProductRPCServerVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Created by licheng1 on 2016/8/2.
 */
public class VerticlesTest {

    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();

        Vertx vertx = Vertx.vertx(options);

        //注册verticle
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        vertx.deployVerticle(ProductRPCServerVerticle.class.getName(), deploymentOptions, result -> {
            if(result.succeeded()) {
                vertx.deployVerticle(ProductRPCClientVerticle.class.getName(), deploymentOptions);
            } else {
                System.out.println(result.cause());
            }
        });
    }

}
