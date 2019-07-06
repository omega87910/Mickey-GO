package sample;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Userlist_server implements Runnable {
    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8484);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket socket = null;
        Userlist_thread userlist_thread = new Userlist_thread();
        Thread thread = new Thread(userlist_thread);
        thread.start();
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(socket + "已連線");
            DataInputStream dataInputStream = null;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String str = null;
            try {
                str = dataInputStream.readUTF();
                if (str.split(" ")[0].equals("User")) {
                    System.out.println(str + "已連線");
                    if (userlist_thread.list_check(str, socket)) {
                        userlist_thread.addStr(str.split(" ")[1] + " " + str.split(" ")[2] + " " + str.split(" ")[3] + " " + str.split(" ")[4],socket);
//                        userlist_thread.addSocket();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
