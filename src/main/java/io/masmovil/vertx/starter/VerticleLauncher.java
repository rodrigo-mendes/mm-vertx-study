package io.masmovil.vertx.starter;

import io.masmovil.vertx.starter.verticle.health.BackendVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.rxjava3.core.Vertx;

public class VerticleLauncher {
    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        options.setWorkerPoolSize(10);
        Vertx.vertx().deployVerticle(new BackendVerticle());
    }
}
