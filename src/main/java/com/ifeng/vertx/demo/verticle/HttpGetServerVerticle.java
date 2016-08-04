package com.ifeng.vertx.demo.verticle;

import com.ifeng.vertx.demo.Service.ProductService;
import com.ifeng.vertx.demo.bean.Product;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by licheng1 on 2016/8/1.
 */
public class HttpGetServerVerticle extends ServerVerticle {

    /**
     * mongo操作业务
     */
    private ProductService productService;

    /**
     * 启动httpServer，初始化微服务RPC调用以及请求处理
     *
     * @param startFuture
     * @throws Exception
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {

        //mongo微服务RPC调用客户端
        productService = bindService(ProductService.class, ProductService.rpcAddress);

        //启动httpServer端口8080
        startServer(8080);
    }

    @Override
    public void routes(Router router) {
        router.route().handler(BodyHandler.create());
        //过滤所有请求
        router.route().handler(this::handleAll);
        //查询产品列表
        router.route("/mongoDemo/products").handler(this::handleList);
        //过滤save
        router.route("/mongoDemo/save!pname=:productName").handler(this::handleSave);
        //过滤update
        router.route("/mongoDemo/update!pid=:productId&pname=:productName").handler(this::handleUpdate);
        //过滤remove
        router.route("/mongoDemo/remove!pid=:productId").handler(this::handleRemove);
        //过滤单元素查找
        router.route("/mongoDemo/findOne!pid=:productId").handler(this::handleFindOne);
        //过滤多元素查找
        router.route("/mongoDemo/find!pname=:productName").handler(this::handleFind);
        //过滤数量统计
        router.route("/mongoDemo/count!pname=:productName").handler(this::handleCount);
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
     * 产品列表
     *
     * @param routingContext
     */
    public void handleList(RoutingContext routingContext) {

        productService.collectionList(result -> {
            HttpServerResponse response = routingContext.response();
            if (result.succeeded()) {
                System.out.println("查询产品列表成功");
                JsonArray array = new JsonArray(result.result());
                responseEnd("产品列表 : " + array.toString(), routingContext);
            } else {
                System.out.println("查询产品列表失败 : " + result.cause().getMessage());
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 产品保存
     *
     * @param routingContext
     */
    public void handleSave(RoutingContext routingContext) {
        String productName = routingContext.request().getParam("productName");
        Product product = new Product();
        product.setName(productName);

        productService.save(product, result -> {
            HttpServerResponse response = routingContext.response();
            if (result.succeeded()) {
                System.out.println("产品save成功,productId : " + result.result());
                responseEnd("产品save成功,productId : " + result.result(), routingContext);
            } else {
                System.out.println("产品save失败 : " + result.cause().getMessage());
                statusCode(400, routingContext);
            }
        });

    }

    /**
     * 产品修改
     *
     * @param routingContext
     */
    public void handleUpdate(RoutingContext routingContext) {
        String productId = routingContext.request().getParam("productId");
        String productName = routingContext.request().getParam("productName");
        Product product = new Product();
        product.set_id(productId);
        product.setName(productName);

        productService.update(product, result -> {
            HttpServerResponse response = routingContext.response();
            if (result.succeeded()) {
                System.out.println("产品update成功");
                responseEnd("产品update成功, update产品数量 : " + result.result(), routingContext);
            } else {
                System.out.println("产品update失败 : " + result.cause().getMessage());
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 产品删除
     *
     * @param routingContext
     */
    public void handleRemove(RoutingContext routingContext) {
        String productId = routingContext.request().getParam("productId");
        Product product = new Product();
        product.set_id(productId);

        productService.remove(product, result -> {
            HttpServerResponse response = routingContext.response();
            if (result.succeeded()) {
                System.out.println("remove产品成功，remove数量:" + result.result());
                responseEnd("remove产品成功，remove数量:" + result.result(), routingContext);
            } else {
                System.out.println("remove产品失败");
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 查询产品。复数
     *
     * @param routingContext
     */
    public void handleFind(RoutingContext routingContext) {
        String productName = routingContext.request().getParam("productName");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", productName);

        productService.find(parameters, result -> {
            HttpServerResponse response = routingContext.response();
            if (result.succeeded()) {
                System.out.println("find产品成功");
                responseEnd("find产品成功，products:" + new JsonArray(result.result()).toString(), routingContext);
            } else {
                System.out.println("find产品失败");
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 查询单个产品
     *
     * @param routingContext
     */
    public void handleFindOne(RoutingContext routingContext) {
        String productId = routingContext.request().getParam("productId");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("_id", productId);

        productService.findOne(parameters, result -> {
            HttpServerResponse response = routingContext.response();
            if (result.succeeded()) {
                System.out.println("find产品成功");
                responseEnd("find产品成功，product:" + result.result(), routingContext);
            } else {
                System.out.println("find产品失败");
                statusCode(400, routingContext);
            }
        });
    }

    /**
     * 统计符合条件的产品数量
     *
     * @param routingContext
     */
    public void handleCount(RoutingContext routingContext) {
        String productName = routingContext.request().getParam("productName");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", productName);

        productService.count(parameters, result -> {
            HttpServerResponse response = routingContext.response();
            if (result.succeeded()) {
                System.out.println("count产品成功，count数量:" + result.result());
                responseEnd("count产品成功，count数量:" + result.result(), routingContext);
            } else {
                System.out.println("count产品失败");
                statusCode(400, routingContext);
            }
        });
    }
}
