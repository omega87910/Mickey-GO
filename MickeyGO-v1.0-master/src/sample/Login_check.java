package sample;

import java.io.*;
import java.net.Socket;

public class Login_check implements Runnable {
    Socket socket = null;
    String[] line = null;


    @Override
    public void run() {
        while (true) {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String[] username = dataInputStream.readUTF().split(" ");
                if (username[0].equals("##register##")) {
                    FileReader fileReader = new FileReader("user.db");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String str = bufferedReader.readLine();
                    String userlist = "";
                    boolean flag = false;
                    while (str != null) {
                        if (username[1].equals(str.split(" ")[0])) {
                            flag = true;
                            break;
                        }
                        userlist = userlist + str + "\n";
                        str = bufferedReader.readLine();
                    }
                    if (flag) {
                        DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                        dataOutputStream1.writeUTF("false");
                    } else {
                        FileWriter fileWriter = new FileWriter("user.db");
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(userlist + username[1] + " " + username[2]);
                        bufferedWriter.close();
                        fileWriter.close();
                        /////////////////////////////////////////////////////////////////////////////////////////
                        fileReader = new FileReader("userinfo.db");
                        bufferedReader = new BufferedReader(fileReader);
                        str = bufferedReader.readLine();
                        userlist = "";
                        while (str != null) {
                            userlist = userlist + str + "\n";
                            str = bufferedReader.readLine();
                        }
                        fileWriter = new FileWriter("userinfo.db");
                        bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(userlist + username[1] + " " + "TWN" + " " + 0 + " " + 0);
                        DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                        dataOutputStream1.writeUTF("true");
                        bufferedWriter.close();
                        fileWriter.close();
                    }
                } else {
                    System.out.println("搜尋的使用者是" + username[0]);
                    FileReader fileReader = null;
                    fileReader = new FileReader("user.db");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    line = bufferedReader.readLine().split(" ");
                    while (line != null) {
                        if (!line[0].equals(username[0])) {
                            System.out.println("正在找使用者" + username[0]);
                        } else {
                            System.out.println("找到了使用者" + username[0]);
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            if (line[1].equals(username[1])) {
                                System.out.println("密碼正確");
                                dataOutputStream.writeUTF("true");
                                fileReader = new FileReader("userinfo.db");
                                bufferedReader = new BufferedReader(fileReader);
                                String[] str = bufferedReader.readLine().split(" ");
                                while (!str[0].equals(line[0])) {
                                    str = bufferedReader.readLine().split(" ");
                                }
                                dataOutputStream.writeUTF(str[0] + " " + str[1] + " " + str[2] + " " + str[3]);
                            } else {
                                dataOutputStream.writeUTF("false");
                                System.out.println("密碼錯誤");
                            }
                            break;
                        }
                        String str = bufferedReader.readLine();
                        if (str == null) {
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            dataOutputStream.writeUTF("false");
                            System.out.println("找不到使用者");
                        }
                        line = str.split(" ");
                    }
                }
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
            break;
        }
        System.out.println("Login_check執行緒停止");
    }

    public void setSocket(Socket socket1) {
        socket = socket1;
    }
}
