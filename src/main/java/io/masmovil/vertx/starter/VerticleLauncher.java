package io.masmovil.vertx.starter;

import io.masmovil.vertx.starter.verticle.health.BackendVerticle;
import io.masmovil.vertx.starter.verticle.health.RxBackendVerticle;
import io.vertx.rxjava3.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleLauncher {

    private static final Logger log = LoggerFactory.getLogger(VerticleLauncher.class);
    public static void main(String[] args) {
        Vertx.vertx()
                .rxDeployVerticle(new RxBackendVerticle())
                .subscribe();
        Vertx.vertx()
                .rxDeployVerticle(new BackendVerticle())
                .subscribe();
    }
}
