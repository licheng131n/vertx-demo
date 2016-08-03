package com.ifeng.vertx.demo.verticle;

import as.leap.vertx.rpc.impl.RPCClientOptions;
import as.leap.vertx.rpc.impl.VertxRPCClient;
import com.ifeng.vertx.demo.Service.ProductService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 *
 * Created by licheng1 on 2016/8/2.
 */
public class ProductRPCClientVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        String address = ProductService.rpcAddress;
        RPCClientOptions<ProductService> rpcClientOptions = new RPCClientOptions<ProductService>(vertx)
                .setBusAddress(address).setServiceClass(ProductService.class);
        ProductService service = new VertxRPCClient<>(rpcClientOptions).bindService();

//        service.save("phone", result -> {
//            System.out.println("callback");
//            if(result.succeeded()) {
//                String output = result.result();
//                System.out.println("output : " + output);
//            } else {
//                System.out.println(result.cause());
//            }
//        });
    }
}
