package com.rkd;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PemKeyCertOptions;

public class HttpServerDownloadTest {
    public static void main(String[] args) {
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setUseAlpn(true)
                .setSsl(true)
                .setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("tsl/server-key.pem").setCertPath("tsl/server-cert.pem"));
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer(httpServerOptions);
        httpServer.requestHandler(request -> {
            request.bodyHandler(body -> {
                System.out.println("server : " + body.toString());
            });
            String file = "src\\main\\java\\com\\rkd\\from.txt";
            request.response().headers().set("Content-Disposition", "attachment;filename=tempFileEntity.txt");
            request.response().sendFile(file);
        });
        httpServer.listen(8080, "localhost", req -> {
            if (req.succeeded()) {
                System.out.println("bind port :" + req.result().actualPort());
            } else {
                System.out.println("bind port error");
            }
        });
    }
}
