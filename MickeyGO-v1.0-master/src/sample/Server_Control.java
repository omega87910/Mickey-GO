package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Server_Control extends Application {
    Login_server login_server = new Login_server();
    Userlist_server userlist_server = new Userlist_server();
    Playgo_server playgo_server = new Playgo_server();
    Chat_Server chat_Server = new Chat_Server();
    Thread login_thread = new Thread(login_server);
    Thread userlist_thread = new Thread(userlist_server);
    Thread playgo_thread = new Thread(playgo_server);
    Thread chat_thread = new Thread(chat_Server);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Label label = new Label("MickeyGO伺服器");
        label.setStyle("-fx-font-size:48px");
        Button login_button = new Button("Login_Server");
        Button userlist_button = new Button("UserList_Server");
        Button playgo_button = new Button("PlayGO_Server");
        Button chat_button = new Button("Chat_Server");
        label.setLayoutX(50);
        label.setLayoutY(50);
        login_button.setPrefHeight(50);
        login_button.setPrefWidth(100);
        login_button.setLayoutY(180);
        login_button.setLayoutX(30);
        playgo_button.setPrefHeight(50);
        playgo_button.setPrefWidth(100);
        playgo_button.setLayoutX(140);
        playgo_button.setLayoutY(180);
        userlist_button.setPrefHeight(50);
        userlist_button.setPrefWidth(100);
        userlist_button.setLayoutX(250);
        userlist_button.setLayoutY(180);
        chat_button.setPrefHeight(50);
        chat_button.setPrefWidth(100);
        chat_button.setLayoutX(360);
        chat_button.setLayoutY(180);
        login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                login_thread = new Thread(login_server);
                login_thread.start();
                login_button.setDisable(true);
            }
        });
        userlist_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                userlist_thread = new Thread(userlist_server);
                userlist_thread.start();
                userlist_button.setDisable(true);

            }
        });
        playgo_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playgo_thread = new Thread(playgo_server);
                playgo_thread.start();
                playgo_button.setDisable(true);

            }
        });
        chat_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                chat_thread = new Thread(chat_Server);
                chat_thread.start();
                chat_button.setDisable(true);
            }
        });
        Pane pane = new Pane();
        pane.setPrefHeight(300);
        pane.setPrefWidth(500);
        pane.setStyle("-fx-background-color:pink");
        Group group = new Group(pane, login_button, userlist_button, playgo_button, chat_button, label);
        Scene scene = new Scene(group, 500, 300);
        scene.setCursor(new ImageCursor(new Image("sample/image/ram.png")));
        primaryStage.setTitle("MickeyGO伺服器");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("sample/image/sai.jpg"));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }
}
