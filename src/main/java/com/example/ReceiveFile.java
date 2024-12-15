package com.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveFile {

    public static void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // System.out.println("Server listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                // System.out.println("Connection accepted from " + socket.getInetAddress());

                try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
                    int fileNameLength = dataInputStream.readInt();

                    if (fileNameLength > 0) {
                        byte[] fileNameBytes = new byte[fileNameLength];
                        dataInputStream.readFully(fileNameBytes);
                        String fileName = new String(fileNameBytes);

                        int fileContentLength = dataInputStream.readInt();
                        if (fileContentLength > 0) {
                            byte[] fileContentBytes = new byte[fileContentLength];
                            dataInputStream.readFully(fileContentBytes);

                            File receivedFile = new File("received_" + fileName);
                            try (FileOutputStream fos = new FileOutputStream(receivedFile)) {
                                fos.write(fileContentBytes);
                                // System.out.println("File received and saved as " + receivedFile.getName());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
