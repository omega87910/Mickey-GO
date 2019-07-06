package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

public class Lobby extends Application {
    static Stage stage = new Stage();
    boolean flag = false;
    String userinfo = "";
    String opponent = "";
    String state = "";
    RuleFrame ruleFrame;
    int minute = 60;
    Double nums = 6.5;
    String msg = "";
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
        Vector<String> msg_vector = new Vector<>();
        Socket socket = new Socket(InetAddress.getByName(ip), 8484);
        ObservableList<User> observableList = FXCollections.observableArrayList();
        TableView<User> tableView = new TableView();
        TableColumn<User, String> tableColumn1 = new TableColumn("Username");
        TableColumn<User, String> tableColumn2 = new TableColumn("Country");
        TableColumn<User, String> tableColumn3 = new TableColumn("Win");
        TableColumn<User, String> tableColumn4 = new TableColumn("Lose");
        tableView.getColumns().addAll(tableColumn1, tableColumn2, tableColumn3, tableColumn4);
        tableColumn1.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        tableColumn2.setCellValueFactory(new PropertyValueFactory<User, String>("Country"));
        tableColumn3.setCellValueFactory(new PropertyValueFactory<User, String>("Win"));
        tableColumn4.setCellValueFactory(new PropertyValueFactory<User, String>("Lose"));
        tableView.setItems(observableList);
        tableView.setLayoutY(100);
        TextArea message_area = new TextArea("聊天室\n");
        TextField textField = new TextField();
        message_area.setEditable(false);
        message_area.setLayoutX(330);
        message_area.setLayoutY(100);
        message_area.setPrefHeight(370);
        message_area.setPrefWidth(150);
        textField.setLayoutX(330);
        textField.setLayoutY(470);
        textField.setPrefHeight(20);
        textField.setPrefWidth(150);
        Button invite_button = new Button("申請對局");
        Button single_button = new Button("單人模式");
//        Button setting_button =new Button("對局設定");
        invite_button.setStyle("-fx-font-size:20px");
        single_button.setStyle("-fx-font-size:20px");
        invite_button.setLayoutX(200);
        invite_button.setLayoutY(50);
        single_button.setLayoutX(350);
        single_button.setLayoutY(50);

        invite_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("申請對局的對手是：" + tableView.getSelectionModel().getSelectedItem().getUsername());
                if (!tableView.getSelectionModel().getSelectedItem().getUsername().equals(userinfo.split(" ")[0])) {
                    opponent = tableView.getSelectionModel().getSelectedItem().getUsername();
                    state = "invite";
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("不能對自己申請對局");
                    alert.show();
                }
            }
        });
        single_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Play_Single play_single = new Play_Single();
                try {
                    play_single.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Label label = new Label("使用者：" + userinfo.split(" ")[0]);
        label.setStyle("-fx-font-size:24px");
        Pane pane = new Pane();
        pane.setPrefWidth(500);
        pane.setPrefHeight(500);
        pane.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        Group group = new Group(pane, tableView, invite_button, label, single_button, message_area, textField);
        Scene scene = new Scene(group, 500, 500);
        scene.setCursor(new ImageCursor(new Image("sample/image/ram.png")));
        stage.setTitle("MickeyGO大廳");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("sample/image/sai.jpg"));
        Login.stage.close();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!textField.getText().equals("")) {
                    msg_vector.add(textField.getText());
                    DataOutputStream dataOutputStream = null;
                    try {
                        dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        dataOutputStream.writeUTF("message " + userinfo.split(" ")[0] + "＞" + msg_vector.firstElement());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    msg_vector.remove(msg_vector.firstElement());
                }
                textField.setText("");
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("執行緒開始");
                try {
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("User " + userinfo);
                    flag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        System.out.println("準備讀取使用者");
                        System.out.println("增加使用者列表");
                        String str = dataInputStream.readUTF();
                        String[] strsplit = str.split(" ");
                        if (strsplit[0].equals("remove")) {
                            for (int i = 0; i < observableList.size(); i++) {
                                if (observableList.get(i).getUsername().equals(strsplit[1])) {
                                    observableList.remove(i);
                                }
                            }
                        } else if (strsplit[0].equals("invited")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    alert.setHeaderText(null);
                                    alert.setContentText(strsplit[1] + "向你提出對局申請");
                                    opponent = strsplit[1];
                                    minute = Integer.parseInt(strsplit[3]);
                                    nums = Double.parseDouble(strsplit[4]);
                                    alert.showAndWait();
                                    if (alert.getResult().getButtonData().isDefaultButton()) {
                                        Play play = new Play();
                                        play.setOpponent(opponent);
                                        play.setUsername(userinfo.split(" ")[0]);
                                        play.setNums_lable(nums);
                                        play.setTime(minute);
                                        state = "accept";
                                    }
                                }
                            });
                        } else if (strsplit[0].equals("accepted")) {
                            System.out.println("對局開始");
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Play play = new Play();
                                    play.setUsername(userinfo.split(" ")[0]);
                                    play.setOpponent(opponent);
                                    play.setNums_lable(nums);
                                    play.setUserinfo(userinfo);
                                    try {
                                        play.start(new Stage());
                                        Lobby.stage.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (strsplit[0].equals("message")) {
                            System.out.println(str);
                            String ms = "";
                            for (int k = 7; k < str.length(); k++) {
                                ms = ms + str.charAt(k);
                            }
                            message_area.setText(message_area.getText() + ms + "\n");
                            message_area.setScrollTop(message_area.getHeight());
                        }else if (strsplit[0].equals("islog")) {
                            try {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                                        alert1.setHeaderText(null);
                                        alert1.setContentText("已被登入");
                                        alert1.showAndWait();
                                        System.exit(-1);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

//                            System.exit(-1);
                        }else {
                            if (observableList.size() > 0) {
                                boolean flag = false;
                                for (int i = 0; i < observableList.size(); i++) {
                                    if (observableList.get(i).getUsername().equals(strsplit[0])) {
                                        flag = true;
                                    }
                                }
                                if (!flag) {
                                    observableList.add(new User(strsplit[0], strsplit[1], strsplit[2], strsplit[3]));
                                }
                            } else {
                                observableList.add(new User(strsplit[0], strsplit[1], strsplit[2], strsplit[3]));
                            }
                        }

                        System.out.println("使用者連線");
                    } catch (SocketException e) {
                        System.out.println("與伺服器失去連線");
                        System.exit(-1);
                    } catch (IOException e) {
                        System.exit(-1);
                    }

                }
            }
        });
        thread.start();
        tableView.setItems(observableList);
        Thread connect = new Thread(new Runnable() {
            @Override
            public void run() {
                DataOutputStream dataOutputStream = null;
                while (true) {
                    if (flag) {
                        try {
                            dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            if (state.equals("invite")) {
                                dataOutputStream.writeUTF("invite " + opponent + " " + userinfo.split(" ")[0] + " " + minute + " " + nums);
                                state = "";
                            } else if (state.equals("accept")) {
                                System.out.println(userinfo + "接受了對局");
                                dataOutputStream.writeUTF("accepted " + opponent + " " + userinfo.split(" ")[0] + " " + minute + " " + nums);
                                state = "";
                            } else {
                                if (msg_vector.isEmpty()) {
                                    dataOutputStream.writeUTF("1");
                                }
                            }
                            Thread.sleep(100);
                        } catch (IOException e) {
                            System.exit(-1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        connect.start();
        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    System.out.println(1);
                }
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setStr(String str) {
        userinfo = str;
        System.out.println(userinfo);
    }

}
