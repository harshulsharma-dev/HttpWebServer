package com.tricipy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private ServerSocket serverSocket;

    private final int port;

    private AtomicInteger conns;

    public Server(int port) {
        this.port = port;
        this.conns = new AtomicInteger(0);
    }
    public void start() {
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Started listening to port " + port + " on localhost");
        } catch (IOException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        if (serverSocket==null) {
            System.out.println("[ERROR] : Server didn't start");
            return;
        }

        while (true) {
            //System.out.println("Clients Connected: " + conns.incrementAndGet());
            try {
                Thread req = new Thread(new RequestHandler(serverSocket.accept(), conns));
                req.start();
            } catch (IOException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
            //System.out.println("A Client Disconnected. Total connections " + conns.decrementAndGet());
        }

    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

}
