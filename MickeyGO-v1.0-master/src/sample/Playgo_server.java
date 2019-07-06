package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Playgo_server implements Runnable {
    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9090);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            PlayGO_output playGO_output = new PlayGO_output();
            Thread thread1 = new Thread(playGO_output);
            thread1.start();
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            playGO_output.addsocketList(socket);
            PlayGO_thread playGO_thread = new PlayGO_thread();
            playGO_thread.setsocket(socket);
            playGO_thread.setPlayGO_output(playGO_output);
            System.out.println(socket + "已連線");
            Thread thread = new Thread(playGO_thread);
            thread.start();
            //////////////////////////////////////////////////////
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            playGO_output.addsocketList(socket);
            playGO_thread = new PlayGO_thread();
            playGO_thread.setsocket(socket);
            playGO_thread.setPlayGO_output(playGO_output);
            System.out.println(socket + "已連線");
            thread = new Thread(playGO_thread);
            thread.start();
            //////////////////////////////////////////////////////
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
