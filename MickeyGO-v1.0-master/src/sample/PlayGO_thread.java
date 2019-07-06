package sample;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;


public class PlayGO_thread implements Runnable {
    Socket socket = null;
    PlayGO_output playGO_output = null;
    boolean flag = true;

    @Override
    public void run() {
        while (flag) {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String str = dataInputStream.readUTF();
                if (str.equals("whoisme")) {
                    playGO_output.whoisme(socket);
                } else if (str.split(" ")[0].equals("time")) {
                    playGO_output.time(socket, str.split(" ")[1]);
                } else if (str.equals("final_check_alert")) {
                    playGO_output.addVector(str);
                } else if (str.equals("stop")) {
                    playGO_output.setFlag(false);
                    flag = false;
                } else {
                    playGO_output.addVector(str);
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (SocketException e) {
                System.out.println("與客戶端斷線");
                break;
            } catch (EOFException e1) {
                System.out.println("客戶端離線");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("PlayGO_thread執行緒已停止");
    }

    public void setsocket(Socket socket1) {
        socket = socket1;
    }

    public void setPlayGO_output(PlayGO_output playGO_output1) {
        playGO_output = playGO_output1;
    }
}
