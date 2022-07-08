package io.masmovil.vertx.starter;

import io.masmovil.vertx.starter.verticle.health.BackendVerticle;
import io.vertx.core.Vertx;

public class VerticleLauncher {
    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new BackendVerticle());
    }
}
