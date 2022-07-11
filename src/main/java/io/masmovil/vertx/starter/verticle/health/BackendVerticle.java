package io.masmovil.vertx.starter.verticle.health;


import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.Status;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.healthchecks.HealthCheckHandler;
import io.vertx.rxjava3.ext.healthchecks.HealthChecks;
import io.vertx.rxjava3.ext.web.Router;
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
                .doOnSuccess(server -> log.info("Server started and listening on port {}", server.actualPort()))
                .doOnError(event -> {
                            log.error("Having problem to start Server");
                            System.exit(1);
                        });
    }


//    public Completable rxStart() {
//        return vertx.createHttpServer()
//                .requestHandler(req -> req.response().end("Hello World"))
//                .rxListen(HTTP_PORT)
//                .ignoreElement();
//    }

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
