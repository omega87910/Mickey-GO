package sample;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

public class PlayGO_output implements Runnable {
    ArrayList<Socket> socketList = new ArrayList();
    Vector<String> vector = new Vector();
    Vector<String> who = new Vector<>();
    ArrayList<String> timelist = new ArrayList<>();
    ArrayList<String> log = new ArrayList<>();
    Random random = new Random();
    int ran = random.nextInt(2);
    boolean blackflag = false;
    boolean whiteflag = false;
    boolean flag = true;

    @Override
    public void run() {
        who.addElement("black");
        who.addElement("white");
        timelist.add("time1");
        timelist.add("time2");
        while (flag) {
            if (vector.isEmpty()) {

            } else {
                for (int i = 0; i < socketList.size(); i++) {
                    DataOutputStream dataOutputStream = null;
                    try {
                        dataOutputStream = new DataOutputStream(socketList.get(i).getOutputStream());
                        if (vector.firstElement().split(" ")[0].equals("final_check_alert")) {
                            if (vector.firstElement().split(" ")[1].equals("black")) {
                                dataOutputStream = new DataOutputStream(socketList.get(1).getOutputStream());
                                dataOutputStream.writeUTF("final_check");
                                i = socketList.size();
                            } else if (vector.firstElement().split(" ")[1].equals("white")) {
                                dataOutputStream = new DataOutputStream(socketList.get(0).getOutputStream());
                                dataOutputStream.writeUTF("final_check");
                                i = socketList.size();
                            }
                        } else if (vector.firstElement().split(" ")[0].equals("skip")) {
                            if (vector.firstElement().split(" ")[1].equals("black")) {
                                DataOutputStream dataOutputStream1 = new DataOutputStream(socketList.get(1).getOutputStream());
                                dataOutputStream1.writeUTF("skip");
                                i = socketList.size();
                            } else if (vector.firstElement().split(" ")[1].equals("white")) {
                                DataOutputStream dataOutputStream1 = new DataOutputStream(socketList.get(0).getOutputStream());
                                dataOutputStream1.writeUTF("skip");
                                i = socketList.size();
                            }
                        } else if (vector.firstElement().split(" ")[0].equals("choose_died")) {
                            blackflag = false;
                            whiteflag = false;
                            dataOutputStream.writeUTF("choose_died");
                        } else if (vector.firstElement().split(" ")[0].equals("over")) {
                            if (vector.firstElement().split(" ")[1].equals("black")) {
                                blackflag = true;
                            }
                            if (vector.firstElement().split(" ")[1].equals("white")) {
                                whiteflag = true;
                            }
                            if (blackflag && whiteflag) {
                                dataOutputStream.writeUTF("over");
                            }
                        } else if (vector.firstElement().split(" ")[0].equals("report")) {
                            int num = 1;
                            File file;
                            do {
                                file = new File("log/log" + num);
                                num++;
                            } while (file.exists());
                            FileWriter fileWriter = new FileWriter(file);
                            for (int j = 0; j < log.size(); j++) {
                                fileWriter.write(log.get(j) + "\n");
                            }
                            System.out.println(vector.firstElement());
                            fileWriter.write(vector.firstElement().split(" ")[1] + "\n");
                            fileWriter.write(vector.firstElement().split(" ")[2] + "\n");
                            i = socketList.size();
                            fileWriter.close();
                        } else if (vector.firstElement().split(" ")[0].equals("win")) {
                            FileReader fileReader = new FileReader("userinfo.db");
                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            String str = bufferedReader.readLine();
                            String[] strsplit = str.split(" ");
                            String a = "";
                            while (str != null) {
                                strsplit = str.split(" ");
                                if (strsplit[0].equals(vector.firstElement().split(" ")[1])) {
                                    a = a + strsplit[0] + " " + strsplit[1] + " " + (Integer.parseInt(strsplit[2]) + 1) + " " + strsplit[3] + "\n";
                                } else {
                                    a = a + str + "\n";
                                }
                                System.out.println(str);
                                str = bufferedReader.readLine();
                            }
                            FileWriter fileWriter = new FileWriter("userinfo.db");
                            fileWriter.write(a);
                            fileWriter.close();
                            fileReader.close();
                            bufferedReader.close();
                            i = socketList.size();
                        } else if (vector.firstElement().split(" ")[0].equals("lose")) {
                            FileReader fileReader = new FileReader("userinfo.db");
                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            String str = bufferedReader.readLine();
                            String[] strsplit = str.split(" ");
                            String a = "";
                            while (str != null) {
                                strsplit = str.split(" ");
                                if (strsplit[0].equals(vector.firstElement().split(" ")[1])) {
                                    a = a + strsplit[0] + " " + strsplit[1] + " " + strsplit[2] + " " + (Integer.parseInt(strsplit[3]) + 1) + "\n";
                                } else {
                                    a = a + str + "\n";
                                }
                                System.out.println(str);
                                str = bufferedReader.readLine();
                            }
                            FileWriter fileWriter = new FileWriter("userinfo.db");
                            fileWriter.write(a);
                            fileWriter.close();
                            fileReader.close();
                            bufferedReader.close();
                            i = socketList.size();
                        } else if (vector.firstElement().split(" ")[0].equals("disconnect")) {
                            if (vector.firstElement().split(" ")[1].equals("black")) {
                                DataOutputStream dataOutputStream1 = new DataOutputStream(socketList.get(1).getOutputStream());
                                dataOutputStream1.writeUTF("disconnect");
                                i = socketList.size();
                            } else if (vector.firstElement().split(" ")[1].equals("white")) {
                                DataOutputStream dataOutputStream1 = new DataOutputStream(socketList.get(0).getOutputStream());
                                dataOutputStream1.writeUTF("disconnect");
                                i = socketList.size();
                            }
                        }else if(vector.firstElement().split(" ")[0].equals("timeout")){
                            if (vector.firstElement().split(" ")[1].equals("black")) {
                                DataOutputStream dataOutputStream1 = new DataOutputStream(socketList.get(1).getOutputStream());
                                dataOutputStream1.writeUTF("timeout");
                                i = socketList.size();
                            } else if (vector.firstElement().split(" ")[1].equals("white")) {
                                DataOutputStream dataOutputStream1 = new DataOutputStream(socketList.get(0).getOutputStream());
                                dataOutputStream1.writeUTF("timeout");
                                i = socketList.size();
                            }
                        } else {
                            dataOutputStream.writeUTF(vector.firstElement());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                log.add(vector.firstElement());
                vector.remove(vector.firstElement());
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Play_GO_output執行緒停止");
    }

    public void addsocketList(Socket socket1) {
        socketList.add(socket1);
    }

    public void addVector(String str) {
        vector.addElement(str);
    }

    public void whoisme(Socket socket1) {
        try {
            if (who.size() > 1) {
                DataOutputStream dataOutputStream = new DataOutputStream(socket1.getOutputStream());
                dataOutputStream.writeUTF(who.elementAt(ran));
                who.removeElementAt(ran);
            } else {
                DataOutputStream dataOutputStream = new DataOutputStream(socket1.getOutputStream());
                dataOutputStream.writeUTF(who.firstElement());
                if (who.firstElement().equals("black")) {
                    Socket socket2 = socketList.get(0);
                    socketList.set(0, socket1);
                    socketList.set(1, socket2);
                } else {
                    socketList.set(1, socket1);
                }
                who.removeElement(who.firstElement());
                who.addElement("black");
                who.addElement("white");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void time(Socket socket1, String t) {
        for (int i = 0; i < socketList.size(); i++) {
            if (socketList.get(i) == socket1) {
                timelist.set(i, t);
            }
        }
        for (int i = 0; i < socketList.size(); i++) {
            DataOutputStream dataOutputStream = null;
            try {
                dataOutputStream = new DataOutputStream(socketList.get(i).getOutputStream());
                dataOutputStream.writeUTF("time " + timelist.get(0) + " " + timelist.get(1));
            } catch (IOException e) {
                System.out.println("客戶端斷線");
            }
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
