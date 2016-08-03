package com.ifeng.vertx.demo.verticle;

import as.leap.vertx.rpc.impl.RPCServerOptions;
import as.leap.vertx.rpc.impl.VertxRPCServer;
import com.ifeng.vertx.demo.Service.ProductService;
import com.ifeng.vertx.demo.Service.impl.ProductServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * mongo微服务RPCServer端
 * Created by licheng1 on 2016/8/1.
 */
public class ProductRPCServerVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        RPCServerOptions rpcServerOptions = new RPCServerOptions(vertx).setBusAddress(ProductService.rpcAddress)
                .addService(new ProductServiceImpl(vertx));
        VertxRPCServer rpcServer = new VertxRPCServer(rpcServerOptions);
        System.out.println("productService RPCServer start!");
        startFuture.complete();
    }
}
