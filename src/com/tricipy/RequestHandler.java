package com.tricipy;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestHandler implements Runnable {

    private final Socket clientSocket;
    private final AtomicInteger connsCount;

    public RequestHandler(Socket clientSocket, AtomicInteger connsCount) {
        this.clientSocket = clientSocket;
        this.connsCount = connsCount;
    }


    @Override
    public void run() {
        System.out.println("Client Connected with address: " + clientSocket.getInetAddress().getHostAddress());
        System.out.println("Total connections: " + connsCount.incrementAndGet());
        try {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(),true);

            String line;
            while ((line = inputStream.readLine()) != null) {
                if (".".equals(line)) {
                    outputStream.println("Good Bye!");
                    break;
                }
                outputStream.println(line);
            }

            outputStream.close();
            inputStream.close();
            clientSocket.close();
            System.out.println("Client Disconnected. Total connections: " + connsCount.decrementAndGet());
        } catch (IOException e) {
            System.out.println("Error while processing request");
            System.out.println("[ERROR] " + e.getMessage());
        }

    }
}
