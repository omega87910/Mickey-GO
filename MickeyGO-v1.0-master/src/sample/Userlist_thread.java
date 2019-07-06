package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Vector;

public class Userlist_thread implements Runnable {
    ArrayList<Socket> socketArrayList = new ArrayList();
    ArrayList<String> arrayList = new ArrayList<>();
    Vector<String> vector = new Vector<>();
    String userinfo;
    boolean flag = true;

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(vector.size());
                if (vector.isEmpty()) {
                    System.out.println("空的");
                    System.out.println("連線過的人共:" + arrayList.size());
                    for (int i = 0; i < socketArrayList.size(); i++) {
                        DataInputStream dataInputStream = null;
                        if (socketArrayList.get(i) != null) {
                            dataInputStream = new DataInputStream(socketArrayList.get(i).getInputStream());
                            try {
                                String string = dataInputStream.readUTF();
                                String[] state = string.split(" ");
                                switch (state[0]) {
                                    case "invite":
                                        for (int j = 0; j < arrayList.size(); j++) {
                                            if (arrayList.get(j) != null) {
                                                String[] str = arrayList.get(j).split(" ");
                                                if (str[0].equals(state[1])) {
                                                    DataOutputStream dataOutputStream = new DataOutputStream(socketArrayList.get(j).getOutputStream());
                                                    dataOutputStream.writeUTF("invited " + state[2] + " " + state[1] + " " + state[3] + " " + state[4]);
                                                }
                                            }
                                        }
                                        break;
                                    case "accepted":
                                        for (int j = 0; j < arrayList.size(); j++) {
                                            if (arrayList.get(j) != null) {
                                                String[] str = arrayList.get(j).split(" ");
                                                if (str[0].equals(state[1])) {
                                                    DataOutputStream dataOutputStream = new DataOutputStream(socketArrayList.get(j).getOutputStream());
                                                    System.out.println("傳送接受對局" + state[2] + state[1]);
                                                    dataOutputStream.writeUTF("accepted " + state[2] + " " + state[1] + " " + state[3] + " " + state[4]);
                                                }
                                                if (str[0].equals(state[2])) {
                                                    DataOutputStream dataOutputStream = new DataOutputStream(socketArrayList.get(j).getOutputStream());
                                                    System.out.println("傳送接受對局" + state[2] + state[1]);
                                                    dataOutputStream.writeUTF("accepted " + state[2] + " " + state[1] + " " + state[3] + " " + state[4]);
                                                }
                                            }
                                        }
                                        break;
                                    case "message":
                                        for (int j = 0; j < arrayList.size(); j++) {
                                            if (arrayList.get(j) != null) {
                                                DataOutputStream dataOutputStream = new DataOutputStream(socketArrayList.get(j).getOutputStream());
                                                dataOutputStream.writeUTF(string);
                                            }
                                        }
                                        break;
                                }
                            } catch (SocketException e) {
                                System.out.println(socketArrayList.get(i) + "失去連線");
                                socketArrayList.remove(i);
                                String[] str = arrayList.get(i).split(" ");
                                arrayList.remove(i);
                                for (int j = 0; j < socketArrayList.size(); j++) {
                                    if (socketArrayList.get(j) != null) {
                                        DataOutputStream dataOutputStream = new DataOutputStream(socketArrayList.get(j).getOutputStream());
                                        dataOutputStream.writeUTF("remove " + str[0]);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    userinfo = vector.firstElement();
                    vector.removeElement(userinfo);
                    for (int i = 0; i < socketArrayList.size(); i++) {
                        System.out.println("回傳資料");
                        DataOutputStream dataOutputStream = null;
                        if (socketArrayList.get(i) != null) {
                            dataOutputStream = new DataOutputStream(socketArrayList.get(i).getOutputStream());
                            for (int j = 0; j < arrayList.size(); j++) {
                                if (arrayList.get(j) != null) {
                                    String[] str = arrayList.get(j).split(" ");
                                    dataOutputStream.writeUTF(str[0] + " " + str[1] + " " + str[2] + " " + str[3]);
                                }
                            }
                            dataOutputStream.flush();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    public void addSocket() {
//
//    }

    public void addStr(String str,Socket socket1) {
        int i = 0 ;
        boolean islog = false;
        socketArrayList.add(socket1);
        while (i < arrayList.size()){
            if (str.equals(arrayList.get(i))){
                System.out.println("登入過");
                islog = true;
                break;
            }else {
                i++;
            }
        }
        if (islog){

            DataOutputStream dataOutputStream = null;
            try {
                dataOutputStream = new DataOutputStream(socketArrayList.get(socketArrayList.size()-1).getOutputStream());
                dataOutputStream.writeUTF("islog ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            socketArrayList.remove(socketArrayList.size()-1);
        }else {
            arrayList.add(str);
            vector.addElement(str);
            System.out.println("新增使用者" + str);
            System.out.println("使用者數" + arrayList.size());
            System.out.println("socket數" + socketArrayList.size());
        }
    }

    public boolean list_check(String str, Socket socket) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) != null && socketArrayList.get(i) != null) {
                if (arrayList.get(i).equals(str) || socketArrayList.get(i) == socket) {
                    return false;
                }
            }
        }
        return true;
    }
}
