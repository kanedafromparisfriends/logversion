/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.kandefromparis.versiondisplay;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author csabourdin
 */
public class Server {

    static final String VERSION = "3.1";

    public static void main(String[] args) throws IOException {

        int port = 8080;
        System.out.println("[starting] ");

        randomSleep();

        Logger.getLogger(Server.class.getName()).log(Level.WARNING, "W:[log should be appears with probes ");

        HttpServer server
                = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext(
                "/", new VersionServerHandler());
        server.setExecutor(
                null);
        server.start();

    }

    private static void randomSleep() {
        try {
            int[] intArray = {1, 10, 500, 5000, 5000, 5000, 10000, 10000};

            int idx = new Random().nextInt(intArray.length);
            Logger.getLogger(Server.class.getName()).log(Level.WARNING, "W:[version 3.0 tend to have issue to start and reply] : " + intArray[idx]);
            Thread.sleep(intArray[idx] * 1000);

            Thread.yield();
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }

    static class VersionServerHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().getPath().trim();
            System.out.println("[access] " + path);
            if (path.equals("/api/1.0/version/info")) {
                getVersion(t);
                return;
            }
            if (path.equals("/healthz")) {
                getVersion(t);
                return;
            }
            if (path.equals("/liveness")) {
                getVersion(t);
                return;
            }
            if (path.equals("/readiness")) {
                getVersion(t);
                return;
            }
            if (path.equals("/")) {
                getDefaultPage(t);
                return;
            }
            getDefaultPage(t);
            return;

        }

        private void getDefaultPage(HttpExchange t) throws IOException {
            OutputStream os = t.getResponseBody();
            StringBuilder sb = new StringBuilder();
            sb.append("<html><title>Version Server</title><body>");
            sb.append("<div><h3>Version Server</h3>This server is here to show log <a href=\"/api/0.0.1/version/info\">version</a><div>");
            t.sendResponseHeaders(200, sb.length());
            os.write(sb.toString().getBytes());
            os.close();
        }

        private void getVersion(HttpExchange t) throws IOException {
            OutputStream os = t.getResponseBody();
            JSONObject message = new JSONObject();
            message.put("status", "ok");
            message.put("version", VERSION);
//
            randomSleep();
            t.sendResponseHeaders(200, message.toString().length());
            os.write(message.toString().getBytes());
            os.close();
        }

    }
}
