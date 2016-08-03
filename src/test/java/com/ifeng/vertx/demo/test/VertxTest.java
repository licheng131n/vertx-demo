package com.ifeng.vertx.demo.test;

import io.vertx.core.Vertx;

/**
 * Created by licheng1 on 2016/8/3.
 */
public class VertxTest {
    public static void main(String[] args) {
        Vertx vertx1 = Vertx.vertx();
        Vertx vertx2 = Vertx.vertx();
        Vertx vertx3 = Vertx.vertx();
        System.out.println(vertx1 == vertx2);
        System.out.println(vertx1 == vertx3);
    }
}
