package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Login_server implements Runnable {
    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket socket = null;
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(socket + "已連線");
            Login_check login_check = new Login_check();
            login_check.setSocket(socket);
            Thread thread = new Thread(login_check);
            thread.start();
        }
    }
}