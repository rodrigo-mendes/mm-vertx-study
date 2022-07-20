package io.masmovil.vertx.starter.verticle.health;


import io.reactivex.rxjava3.core.Completable;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.Status;

import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.healthchecks.HealthCheckHandler;
import io.vertx.rxjava3.ext.healthchecks.HealthChecks;
import io.vertx.rxjava3.ext.web.openapi.RouterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RxBackendVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(RxBackendVerticle.class);

    private static final int HTTP_PORT = 8090;


   public Completable rxStart() {
        return vertx.createHttpServer()
                .requestHandler(request -> RouterBuilder.create(vertx, "src/main/resources/healthEndpoint.yaml")
                        .doOnSuccess(builder -> {
                            log.info("OpenAPi Specification loaded with success");
                            HealthChecks checks = HealthChecks.create(vertx).register("verticle-health", promise ->
                                    promise.complete(Status.OK(new JsonObject()
                                            .put(VERSION, System.getenv().getOrDefault(VERSION, "0.1"))
                                            .put(ENV_NAME, System.getenv().getOrDefault(ENV_NAME, "local"))
                                            .put(SWAGGER_DEFINITION, System.getenv().getOrDefault(SWAGGER_DEFINITION, "unknown"))
                                            .put(STATUS, "UP")
                                    ))
                            );
                            HealthCheckHandler healthCheckHandler = HealthCheckHandler.createWithHealthChecks(checks);
                            builder.operation("health")
                                    .handler(healthCheckHandler);
                        })
                        .doOnError(err -> log.error("Having problem to start Server, review OpenAPi Specification")).subscribe())
                .rxListen(HTTP_PORT)
                .doOnSuccess(server -> log.info("Server started and listening on port {}", server.actualPort()))
                .doOnError(event -> {
                    log.error("Having problem to start Server");
                    System.exit(1);
                })
                .ignoreElement();
    }

    private static final String VERSION = "version";
    private static final String ENV_NAME = "environment";
    private static final String SWAGGER_DEFINITION = "swagger-version";
    private static final String STATUS = "status";

}
