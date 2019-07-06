package sample;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Chat_client implements Runnable {
    Socket socket;
    Send send;
    boolean flag = true;

    public Chat_client(Socket socket1, Send send1) {
        socket = socket1;
        send = send1;
    }

    @Override
    public void run() {
        while (flag) {
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());//接收資料
                String str = in.readUTF();
                if (str.equals("##StopChatThread##")) {
                    send.setFlag(false);
                    flag = false;
                } else {
                    send.setMsg(str);
                }
            } catch (SocketException e) {
                System.out.println("與客戶端斷線");
                send.setFlag(false);
                flag = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Chat_client執行緒已停止");
    }
}
