package sample;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class Send implements Runnable {
    ArrayList<Socket> socketArrayList = new ArrayList<>();
    Vector<String> messageflush = new Vector<>();
    String msg = "";
    Boolean flag = true;

    @Override
    public void run() {
        while (flag) {
            try {
                if (messageflush.isEmpty()) {

                } else {
                    msg = messageflush.firstElement();
                    System.out.println("msg=" + msg);
                    messageflush.removeElement(msg);
                    for (int i = 0; i < socketArrayList.size(); i++) {
                        DataOutputStream out = new DataOutputStream(socketArrayList.get(i).getOutputStream());//傳送資料
                        out.writeUTF(msg);
                        out.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Send執行緒停止");
    }

    public void add(Socket socket) {
        socketArrayList.add(socket);
        System.out.println(socketArrayList.size());
    }

    public void setMsg(String str) {
        messageflush.addElement(str);
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
