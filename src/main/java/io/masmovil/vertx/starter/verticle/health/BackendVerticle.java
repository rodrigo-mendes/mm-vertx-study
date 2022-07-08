package io.masmovil.vertx.starter.verticle.health;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackendVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(BackendVerticle.class);

    private static final int HTTP_PORT = 8090;
    @Override
    public void start() {

        Router router = setupRouter();

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(HTTP_PORT)
                .onSuccess(server -> log.info("Server started and listening on port {}", server.actualPort()))
                .onFailure(event -> {
                            log.error("Having problem to start Server");
                            System.exit(1);
                        });
    }

    private static final String VERSION = "version";
    private static final String ENV_NAME = "environment";
    private static final String SWAGGER_DEFINITION = "swagger-version";
    private static final String STATUS = "status";
    private Router setupRouter() {
        Router router = Router.router(vertx);

        HealthChecks checks = HealthChecks.create(vertx).register("verticle-health", promise ->
            promise.complete(Status.OK(new JsonObject()
                    .put(VERSION, System.getenv().getOrDefault(VERSION, "0.1"))
                    .put(ENV_NAME, System.getenv().getOrDefault(ENV_NAME, "local"))
                    .put(SWAGGER_DEFINITION, System.getenv().getOrDefault(SWAGGER_DEFINITION, "unknown"))
                    .put(STATUS, "UP")
            ))
        );

        HealthCheckHandler healthCheckHandler = HealthCheckHandler.createWithHealthChecks(checks);
        router.get("/health").handler(healthCheckHandler);

        return router;
    }
}
