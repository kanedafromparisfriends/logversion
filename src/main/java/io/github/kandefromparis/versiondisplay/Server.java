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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author csabourdin
 */
public class Server {

    static final String VERSION = "1.0";

    public static void main(String[] args) throws IOException {

        int port = 8080;
        System.out.println("[starting] ");

        Thread logVersionthread = new Thread() {
            @Override
            public void run() {

                while (true) {
                    try {
                        Thread.sleep(1500);
                        
                        JSONObject message = new JSONObject();
                        InetAddress myHost = InetAddress.getLocalHost();
                        message.put("Log_Level", Level.WARNING);
                        message.put("hosname", myHost.getHostName());
                        message.put("version", VERSION);

                        Logger.getLogger(Server.class.getName()).log(Level.WARNING, "W:"+message.toString());
                        Thread.yield();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        Thread.currentThread().interrupt();

                    } catch (UnknownHostException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        };

        HttpServer server
                = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext(
                "/", new VersionServerHandler());
        server.setExecutor(
                null);
        server.start();
        logVersionthread.start();
        
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

            t.sendResponseHeaders(200, message.toString().length());
            os.write(message.toString().getBytes());
            os.close();
        }

    }
}
