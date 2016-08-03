package com.ifeng.vertx.demo.verticle;

import as.leap.vertx.rpc.impl.RPCServerOptions;
import as.leap.vertx.rpc.impl.VertxRPCServer;
import com.ifeng.vertx.demo.Service.ProductExtService;
import com.ifeng.vertx.demo.Service.impl.ProductExtServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Created by licheng1 on 2016/8/3.
 */
public class ProductExtRPCServerVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        RPCServerOptions rpcServerOptions = new RPCServerOptions(vertx).setBusAddress(ProductExtService.rpcAddress)
                .addService(new ProductExtServiceImpl(vertx));
        VertxRPCServer rpcServer = new VertxRPCServer(rpcServerOptions);
        System.out.println("productExtService RPCServer start!");
        startFuture.complete();
    }
}
