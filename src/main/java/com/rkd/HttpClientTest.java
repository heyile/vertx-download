package com.rkd;

import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.streams.Pump;

public class HttpClientTest {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        String filePath = "src\\main\\java\\com\\rkd\\to.txt";
        HttpClientOptions http2ClientOption = new HttpClientOptions();
        http2ClientOption.setProtocolVersion(HttpVersion.HTTP_2)
                .setUseAlpn(true)
                .setHttp2ClearTextUpgrade(false)
                .setTrustAll(true)
                .setSsl(true)
                .setDefaultPort(8080)
                .setDefaultHost("localhost");
        HttpClient httpClient = vertx.createHttpClient(http2ClientOption);
        HttpClientRequest httpClientRequest = httpClient.request(HttpMethod.POST, "", res -> {
            res.pause();
            vertx.fileSystem().open(filePath, new OpenOptions(), ares -> {
                AsyncFile file = ares.result();
                Pump pump = Pump.pump(res, file);
                res.exceptionHandler(Throwable::printStackTrace);
                res.endHandler(v1 -> {
                    System.out.println("end...");
                    file.flush().close(v2 -> {
                        System.out.println(" download success");
                    });
                });
                pump.start();
                res.resume();
            });
        });
        httpClientRequest.setChunked(true);
        httpClientRequest.end("hello");
    }

}
