package sample;

import java.io.IOException;
import java.net.*;

public class Chat_Server implements Runnable {
    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(25565);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket socket = null;
        Send sendmes = new Send();
        Thread thread = new Thread(sendmes);
        thread.start();
        while (true) {
            System.out.println("等待使用者加入連線");
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("使用者" + socket + "加入了連線");
            Chat_client chat_client = new Chat_client(socket, sendmes);
            Thread clienthread = new Thread(chat_client);
            clienthread.start();
            sendmes.add(socket);
            /////////////////////////////////////////////////////////////
            System.out.println("等待使用者加入連線");
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("使用者" + socket + "加入了連線");
            chat_client = new Chat_client(socket, sendmes);
            clienthread = new Thread(chat_client);
            clienthread.start();
            sendmes.add(socket);
            sendmes = new Send();
            thread = new Thread(sendmes);
            thread.start();
        }
    }
}
