package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class Login extends Application {


    /////////////////////////////////////////////
    final ImageView imv = new ImageView();
    final Image image2 = new Image(this.getClass().getResourceAsStream("image/bg1.jpg"));


    final HBox pictureRegion = new HBox();



    ////////////////////////////////////////////

    static Stage stage = new Stage();
    Socket socket = null;
    String ip="";
    @Override
    public void start(Stage primaryStage) {
        //////////////////////////////////
        imv.setImage(image2);
        pictureRegion.getChildren().add(imv);
        //////////////////////////////////
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
        Pane pane = new Pane();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Label titleLabel = new Label("MickeyGO");
        titleLabel.setTextFill(Color.valueOf("#ff4000"));
        Label usernameLabel = new Label("使用者名稱：");
        Label passwordLabel = new Label("密碼：");
        Button login_button = new Button("登入");
        Button register_button = new Button("註冊");
        login_button.setDefaultButton(true);
        Group group = new Group(pane, usernameField, passwordField, usernameLabel, passwordLabel, login_button, register_button, titleLabel);
        usernameLabel.setLayoutX(110);
        usernameLabel.setLayoutY(230);
        usernameField.setLayoutX(180);
        usernameField.setLayoutY(230);
        passwordLabel.setLayoutX(110);
        passwordLabel.setLayoutY(265);
        passwordField.setLayoutX(180);
        passwordField.setLayoutY(260);
        login_button.setLayoutX(280);
        register_button.setLayoutX(220);
        login_button.setLayoutY(285);
        register_button.setLayoutY(285);
        pane.setPrefWidth(500);
        pane.setPrefHeight(300);
///////////////////////////////////////////////////////////
        pane.getChildren().addAll(pictureRegion);
/////////////////////////////////////////////////////////
        titleLabel.setStyle("-fx-font-size:68px");
        titleLabel.setLayoutX(80);
        titleLabel.setLayoutY(135);
        Scene scene = new Scene(group, 500, 300);

        scene.setCursor(new ImageCursor(new Image("sample/image/ram.png")));
        stage.getIcons().add(new Image("sample/image/sai.jpg"));
        stage.setResizable(false);
        stage.setTitle("MickeyGO登入器");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (usernameField.getText() != null && passwordField.getText() != null) {
                    if (usernameField.getText().length() >= 3 && usernameField.getText().length() <= 8) {
                        if (passwordField.getText().length() <= 10 && passwordField.getText().length() >= 3) {
                            boolean flag = true;
                            for (int i = 0; i < usernameField.getText().length(); i++) {
                                if (usernameField.getText().charAt(i) == ' ') {
                                    flag = false;
                                }
                            }
                            for (int i = 0; i < passwordField.getText().length(); i++) {
                                if (passwordField.getText().charAt(i) == ' ') {
                                    flag = false;
                                }
                            }
                            if (flag) {
                                try {
                                    socket = new Socket(InetAddress.getByName(ip), 4444);
                                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());//輸出資料
                                    dataOutputStream.writeUTF(usernameField.getText() + " " + passwordField.getText());
                                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                    String str = dataInputStream.readUTF();
                                    if (str.equals("true")) {
                                        Lobby lobby = new Lobby();
                                        lobby.setStr(dataInputStream.readUTF());
                                        try {
                                            lobby.start(new Stage());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }else {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setHeaderText(null);
                                        alert.setTitle("登入驗證");
                                        alert.setContentText("登入失敗");
                                        alert.showAndWait();
                                    }
                                } catch (ConnectException c) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setHeaderText(null);
                                    alert.setTitle("錯誤訊息");
                                    alert.setContentText("伺服器連線失敗");
                                    alert.showAndWait();
                                    System.exit(-1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText(null);
                                alert.setContentText("帳號密碼禁止使用空格！");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText(null);
                            alert.setContentText("密碼請介於3~10字！");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("使用者名稱請介於3~8字！");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("請輸入使用者名稱與密碼！");
                    alert.show();
                }
            }
        });
        register_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (usernameField.getText() != null && passwordField.getText() != null) {
                    if (usernameField.getText().length() >= 3 && usernameField.getText().length() <= 8) {
                        if (passwordField.getText().length() <= 10 && passwordField.getText().length() >= 3) {
                            boolean flag = true;
                            for (int i = 0; i < usernameField.getText().length(); i++) {
                                if (usernameField.getText().charAt(i) == ' ') {
                                    flag = false;
                                }
                            }
                            for (int i = 0; i < passwordField.getText().length(); i++) {
                                if (passwordField.getText().charAt(i) == ' ') {
                                    flag = false;
                                }
                            }
                            if (flag) {
                                try {
                                    socket = new Socket(InetAddress.getByName(ip), 4444);
                                    DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                                    dataOutputStream1.writeUTF("##register## " + usernameField.getText() + " " + passwordField.getText());
                                    DataInputStream dataInputStream1 = new DataInputStream(socket.getInputStream());
                                    String str = dataInputStream1.readUTF();
                                    if (str.equals("true")) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setHeaderText(null);
                                        alert.setTitle("註冊驗證");
                                        alert.setContentText("註冊成功");
                                        alert.showAndWait();
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setHeaderText(null);
                                        alert.setTitle("註冊驗證");
                                        alert.setContentText("使用者名稱已被使用");
                                        alert.showAndWait();
                                    }
                                } catch (ConnectException c) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setHeaderText(null);
                                    alert.setTitle("錯誤訊息");
                                    alert.setContentText("伺服器連線失敗");
                                    alert.showAndWait();
                                    System.exit(-1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText(null);
                                alert.setContentText("帳號密碼禁止使用空格！");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText(null);
                            alert.setContentText("密碼請介於3~10字！");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("使用者名稱請介於3~8字！");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("請輸入使用者名稱與密碼！");
                    alert.show();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
