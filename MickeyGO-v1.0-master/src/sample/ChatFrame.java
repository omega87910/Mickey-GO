package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ChatFrame extends Application {
    static Stage stage;
    String msg = "";
    String username = "";
    Socket socket = null;
    String ip="";
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FileReader fileReader =new FileReader("ipconfig.conf");
            BufferedReader bufferedReader =new BufferedReader(fileReader);
            ip=bufferedReader.readLine();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            try {
                FileWriter fileWriter= new FileWriter("ipconfig.conf");
                fileWriter.write("192.168.1.103");
                fileWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = primaryStage;
        socket = new Socket(InetAddress.getByName(ip), 25565);
        TextArea message_area = new TextArea();
        TextField textField = new TextField();
        message_area.setEditable(false);
        message_area.setPrefHeight(350);
        message_area.setPrefWidth(600);
        message_area.setStyle("-fx-font-size:24px");
        textField.setStyle("-fx-font-size:24px");
        textField.setPrefHeight(50);
        textField.setPrefWidth(600);
        textField.setLayoutY(350);
        Group group = new Group();
        group.getChildren().addAll(message_area, textField);
        Scene scene = new Scene(group, 600, 400);
        scene.setCursor(new ImageCursor(new Image("sample/image/ram.png")));
        stage.setTitle("聊天視窗");
        stage.setScene(scene);
        stage.getIcons().add(new Image("sample/image/sai.jpg"));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        message_area.setText(message_area.getText() + dataInputStream.readUTF() + "\n");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stage.show();
                            }
                        });
                    } catch (IOException e) {
                        System.out.println("失去連線...");
                        System.exit(-1);
                    }
                }
            }
        });
        thread.start();
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                msg = textField.getText();
                try {
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());//輸出資料到伺服器端
                    dataOutputStream.writeUTF(username + "＞" + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textField.setText("");
            }
        });
    }

    public void setUsername(String username1) {
        username = username1;
    }


    public void stopChat() {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("##StopChatThread##");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
