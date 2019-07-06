package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Play extends Lobby {
    static Stage stage;
    private Label[][] go = new Label[19][19];
    private Background black_go = new Background(new BackgroundFill(Color.BLACK, new CornerRadii(50), new Insets(3)));
    private Background white_go = new Background(new BackgroundFill(Color.WHITE, new CornerRadii(50), new Insets(3)));
    private Background null_go = null;
    private int block[] = new int[19 * 19];
    private int blocklength;
    private String userinfo = "";
    private String username = "";
    private String opponent = "";
    static String msg = "";
    int position = 0;
    ArrayList<String> goarraylist = new ArrayList();
    Label nums_lable = new Label("貼目：");
    Label hands_num_lable = new Label("手數：");
    Label me = new Label();
    Label op = new Label();
    Label me_time = new Label("TIME");
    Label op_time = new Label("TIME");

    int time = 1800;
    boolean time_flag = false;
    double black_sum = 0;
    double white_sum = 0;
    int black_field_sum = 0;
    int white_field_sum = 0;
    int black_eat_white_sum = 0;
    int white_eat_black_sum = 0;
    int black_died_sum = 0;
    int white_died_sum = 0;
    boolean chosedied_go = false;
    boolean flag = true;
    boolean showfield = false;
    String chracterwho;
    boolean final_check = false;
    int onlyonego_be_ate_pos = 0;
    int lastgopos = 0;

    @Override
    public void start(Stage primarystage) throws Exception {
        try {
            FileReader fileReader =new FileReader("ipconfig.conf");
            BufferedReader bufferedReader =new BufferedReader(fileReader);
            ip=bufferedReader.readLine();//extends Lobby
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
        ChatFrame chatFrame = new ChatFrame();
        chatFrame.setUsername(username);
        try {
            chatFrame.start(new Stage());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ChatFrame.stage.hide();
        stage = primarystage;
        Socket socket = new Socket(InetAddress.getByName(ip), 9090);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("whoisme");
        Timer timer = new Timer();
        stage.getIcons().add(new Image("sample/image/sai.jpg"));
        stage.setTitle("MickeyGO");
        /////////////////////////////Sound////////////////////////////////////
        AudioClip black_audio = new AudioClip(this.getClass().getResource("sound/black.mp3").toString());
        AudioClip white_audio = new AudioClip(this.getClass().getResource("sound/white.mp3").toString());
        /////////////////////////////Menu/////////////////////////////////////

        /////////////////////////////BUTTON////////////////////////////////////
        Button field_button = new Button("局勢分析");
        Button chat_button = new Button("聊天");
        Button final_button = new Button("勝負判定");
        Button report_button = new Button("審核");
        Button skip_button = new Button("虛手");

        report_button.setDisable(true);
        final_button.setLayoutX(1135);
        final_button.setLayoutY(360);
        final_button.setPrefWidth(100);
        final_button.setPrefHeight(50);
        field_button.setLayoutX(1025);
        field_button.setLayoutY(360);
        field_button.setPrefWidth(100);
        field_button.setPrefHeight(50);
        chat_button.setLayoutX(1025);
        chat_button.setLayoutY(300);
        chat_button.setPrefWidth(100);
        chat_button.setPrefHeight(50);
        report_button.setLayoutX(1135);
        report_button.setLayoutY(300);
        report_button.setPrefWidth(100);
        report_button.setPrefHeight(50);
        skip_button.setLayoutX(1025);
        skip_button.setLayoutY(420);
        skip_button.setPrefWidth(100);
        skip_button.setPrefHeight(50);
        nums_lable.setLayoutX(1025);
        nums_lable.setLayoutY(600);
        nums_lable.setStyle("-fx-font-size:30px");
        hands_num_lable.setLayoutX(1025);
        hands_num_lable.setLayoutY(650);
        hands_num_lable.setStyle("-fx-font-size:30px");
        me.setLayoutX(1025);
        me.setLayoutY(10);
        me.setStyle("-fx-font-size:30px");
        op.setLayoutX(1025);
        op.setLayoutY(100);
        op.setStyle("-fx-font-size:30px");
        me_time.setLayoutX(1025);
        me_time.setLayoutY(40);
        me_time.setStyle("-fx-font-size:30px");
        op_time.setLayoutX(1025);
        op_time.setLayoutY(130);
        op_time.setStyle("-fx-font-size:30px");
        report_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream1.writeUTF("report " + chracterwho + "：" + username + " " + opponent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                report_button.setDisable(true);
                try {
                    DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream1.writeUTF("stop");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        skip_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream1.writeUTF("skip " + chracterwho);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                time_flag = false;
                flag = !flag;
                skip_button.setDisable(true);
                final_button.setDisable(true);
                for (int i = 0; i < 19; i++) {
                    for (int j = 0; j < 19; j++) {
                        go[i][j].setDisable(true);
                        go[i][j].setOpacity(1.0);
                    }
                }
            }
        });
        chat_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChatFrame.stage.show();
            }
        });
        field_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!showfield) {
                    black_field_sum = 0;
                    white_field_sum = 0;
                    black_died_sum = 0;
                    white_died_sum = 0;
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            if (go[i][j].getText().equals("◎")) {
                                go[i][j].setText("");
                            }
                        }
                    }
                    check_field();
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            if (go[i][j].getText().equals("◎") && go[i][j].getStyle().equals("-fx-text-fill:white;-fx-alignment:center;")) {
                                white_field_sum++;
                                if (go[i][j].getBackground() != null_go) {
                                    black_died_sum++;
                                }
                            }
                            if (go[i][j].getText().equals("◎") && go[i][j].getStyle().equals("-fx-text-fill:black;-fx-alignment:center;")) {
                                black_field_sum++;
                                if (go[i][j].getBackground() != null_go) {
                                    white_died_sum++;
                                }
                            }

                        }
                    }
                    showfield = true;
                } else {
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            go[i][j].setText("");
                        }
                    }
                    for (int i = 3; i <= 15; i += 6) {
                        for (int j = 3; j <= 15; j += 6) {
                            if (go[i][j].getBackground() == null_go) {
                                go[i][j].setText("◎");
                                go[i][j].setStyle("-fx-text-fill:Black;-fx-alignment:center;");
                            }
                        }
                    }
                    showfield = false;
                }
            }
        });
        ///////////////////////////////////////////////////////////////////////
        ImageView imageView = new ImageView("sample/image/board.jpg");
        Label[][] backboard = new Label[19][19];
        GridPane gridPane = new GridPane();
        GridPane gridPane2 = new GridPane();
        /////////////////////////message///////////////////////////////////////
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (!final_check) {
                    try {
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        dataOutputStream.writeUTF("disconnect " + chracterwho);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                timer.cancel();
                Lobby.stage.show();
                chatFrame.stopChat();
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////
        Pane pane2 = new Pane();
        pane2.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        pane2.setPrefWidth(1280);
        pane2.setPrefHeight(1000);
        Pane pane = new Pane();
        Group group = new Group(pane2, pane, gridPane, gridPane2, chat_button, nums_lable, hands_num_lable, me, op, me_time, op_time, final_button, field_button, report_button, skip_button);
        Scene scene = new Scene(group, 1280, 1000);
        scene.setCursor(new ImageCursor(new Image("sample/image/ram.png")));
        stage.setScene(scene);
        gridPane.addColumn(17);
        gridPane.addRow(17);
        gridPane2.addColumn(18);
        gridPane2.addRow(18);
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                backboard[i][j] = new Label();
                backboard[i][j].setPrefHeight(50);
                backboard[i][j].setPrefWidth(50);
                gridPane.add(backboard[i][j], i, j);
            }
        }
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                go[i][j] = new Label();
                go[i][j].setPrefHeight(50);
                go[i][j].setPrefWidth(50);
                go[i][j].setStyle("-fx-alignment:center;");
                go[i][j].setFont(new Font(30));
                gridPane2.add(go[i][j], i, j);
                int a = i * 100 + j;
                go[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Label label = (Label) event.getSource();
                        if (chosedied_go) {
                            if (chracterwho.equals("white")) {
                                try {
                                    DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                                    dataOutputStream1.writeUTF("" + a);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (label.getBackground() == null_go) {
                                if (suicide(a / 100, a % 100)) {
                                    System.out.println("禁手點");
                                } else {
                                    label.setText("");
                                    posi_change();
                                    if (flag) {
                                        label.setBackground(black_go);
                                        label.setText("◎");
                                        label.setStyle("-fx-text-fill:Red;-fx-alignment:center;");
                                        black_audio.play(1);
                                        eat_check(a / 100, a % 100);
                                        flag = false;
                                    } else {
                                        label.setBackground(white_go);
                                        label.setText("◎");
                                        label.setStyle("-fx-text-fill:Red;-fx-alignment:center;");
                                        white_audio.play(1);
                                        eat_check(a / 100, a % 100);
                                        flag = true;
                                    }
                                    for (int i = 3; i <= 15; i += 6) {
                                        for (int j = 3; j <= 15; j += 6) {
                                            if (go[i][j].getBackground() == null_go) {
                                                go[i][j].setText("◎");
                                                go[i][j].setStyle("-fx-text-fill:black;-fx-alignment:center;");
                                            }
                                        }
                                    }
                                    goarraylist.add("" + a);
                                    position++;
                                    hands_num_lable.setText("手數：" + position);
                                    time_flag = false;
                                    skip_button.setDisable(true);
                                    final_button.setDisable(true);
                                    for (int i = 0; i < 19; i++) {
                                        for (int j = 0; j < 19; j++) {
                                            go[i][j].setDisable(true);
                                            go[i][j].setOpacity(1.0);
                                        }
                                    }
                                    if(time<60){
                                        time=60;
                                    }
                                    try {
                                        DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                                        dataOutputStream1.writeUTF("" + a);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
        go[3][3].setText("◎");
        go[9][3].setText("◎");
        go[15][3].setText("◎");
        go[3][9].setText("◎");
        go[9][9].setText("◎");
        go[15][9].setText("◎");
        go[3][15].setText("◎");
        go[9][15].setText("◎");
        go[15][15].setText("◎");
        gridPane.setLayoutX(50);
        gridPane.setLayoutY(50);
        gridPane2.setLayoutX(25);
        gridPane2.setLayoutY(25);
        imageView.setFitHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        pane.getChildren().add(imageView);//棋盤圖片設定
        gridPane.setStyle("-fx-border-style:solid");
        gridPane.setStyle("-fx-grid-lines-visible:true");
        stage.setResizable(false);
        stage.show();
        final_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (goarraylist.size() >= 50) {
                    if (!chosedied_go) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("向對手申請勝負判定?");
                        alert.showAndWait();
                        if (alert.getResult().getButtonData().isDefaultButton()) {
                            try {
                                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                dataOutputStream.writeUTF("final_check_alert " + chracterwho);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                            dataOutputStream1.writeUTF("final_score");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("低於一定步數，無法進行勝負判定");
                    alert.show();
                }
            }
        });
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if(time>=0) {
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        dataOutputStream.writeUTF("time " + time);
                    }
                    else{
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        dataOutputStream.writeUTF("timeout " + chracterwho);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("你已超時負。");
                        alert.show();
                        chat_button.setDisable(true);
                        report_button.setDisable(true);
                        final_button.setDisable(true);
                        for(int i=0;i<19;i++){
                            for (int j=0;j<19;j++){
                                go[i][j].setDisable(true);
                            }
                        }
                            }
                        });
                        this.cancel();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (time_flag) {
                    time--;
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        System.out.println("play準備接受資料");
                        String str = dataInputStream.readUTF();
                        System.out.println("play已接受資料" + str);
                        if (str.equals("black")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    me.setText("黑番： " + me.getText());
                                    op.setText("白番： " + op.getText());
                                    chracterwho = "black";
                                    skip_button.setDisable(false);
                                    final_button.setDisable(false);
                                    time_flag = true;
                                    timer.schedule(timerTask, 0, 1000);
                                }
                            });
                        } else if (str.equals("white")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    me.setText("白番： " + me.getText());
                                    op.setText("黑番： " + op.getText());
                                    chracterwho = "white";
                                    skip_button.setDisable(true);
                                    final_button.setDisable(true);
                                    time_flag = false;
                                    timer.schedule(timerTask, 0, 1000);
                                }

                            });
                            for (int m = 0; m < 19; m++) {
                                for (int n = 0; n < 19; n++) {
                                    go[m][n].setDisable(true);
                                    go[m][n].setOpacity(1.0);
                                }
                            }
                        } else if (str.equals("skip")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText(null);
                                    alert.setContentText("對方虛一手");
                                    alert.show();
                                    skip_button.setDisable(false);
                                    final_button.setDisable(false);
                                    for (int m = 0; m < 19; m++) {
                                        for (int n = 0; n < 19; n++) {
                                            go[m][n].setDisable(false);
                                            go[m][n].setOpacity(1.0);
                                        }
                                    }
                                    time_flag = true;
                                    flag = !flag;
                                }
                            });
                        } else if (str.equals("final_check")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setHeaderText(null);
                                    alert.setContentText("對手要求勝負判定");
                                    alert.showAndWait();
                                    if (alert.getResult().getButtonData().isDefaultButton()) {
                                        try {
                                            DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                                            dataOutputStream1.writeUTF("choose_died");
                                            for (int i = 0; i < 19; i++) {
                                                for (int j = 0; j < 19; j++) {
                                                    go[i][j].setDisable(true);
                                                    go[i][j].setOpacity(1.0);
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        } else if (str.equals("choose_died")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText(null);
                                    alert.setContentText("白方開始點選死子");
                                    alert.show();
                                    time_flag = false;
                                    chosedied_go = true;
                                    skip_button.setDisable(true);
                                    if (chracterwho.equals("black")) {
                                        final_button.setDisable(true);
                                    } else if (chracterwho.equals("white")) {
                                        final_button.setDisable(false);
                                    }
                                    for (int i = 0; i < 19; i++) {
                                        for (int j = 0; j < 19; j++) {
                                            go[i][j].setText("");
                                            go[i][j].setDisable(false);
                                        }
                                    }
                                    for (int i = 3; i <= 15; i += 6) {
                                        for (int j = 3; j <= 15; j += 6) {
                                            if (go[i][j].getBackground() == null_go) {
                                                go[i][j].setText("◎");
                                                go[i][j].setStyle("-fx-text-fill:Black;-fx-alignment:center;");
                                            }
                                        }
                                    }
                                    if (chracterwho.equals("black")) {
                                        final_button.setDisable(true);
                                    }
                                    field_button.setDisable(true);
                                }
                            });
                        } else if (str.equals("final_score")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    black_field_sum = 0;
                                    white_field_sum = 0;
                                    black_died_sum = 0;
                                    white_died_sum = 0;
                                    for (int i = 0; i < 19; i++) {
                                        for (int j = 0; j < 19; j++) {
                                            if (go[i][j].getText().equals("◎") && go[i][j].getStyle().equals("-fx-text-fill:Red;-fx-alignment:center;")) {
                                                go[i][j].setText("");
                                            }
                                        }
                                    }
                                    check_field();
                                    for (int i = 0; i < 19; i++) {
                                        for (int j = 0; j < 19; j++) {
                                            if (go[i][j].getText().equals("◎") && go[i][j].getStyle().equals("-fx-text-fill:white;-fx-alignment:center;")) {
                                                white_field_sum++;
                                                if (go[i][j].getBackground() != null_go) {
                                                    black_died_sum++;
                                                }
                                            }
                                            if (go[i][j].getText().equals("◎") && go[i][j].getStyle().equals("-fx-text-fill:black;-fx-alignment:center;")) {
                                                black_field_sum++;
                                                if (go[i][j].getBackground() != null_go) {
                                                    white_died_sum++;
                                                }
                                            }
                                        }
                                    }
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setHeaderText(null);
                                    alert.setContentText("黑棋：" + (black_field_sum + black_eat_white_sum + white_died_sum) + "目，白棋：" + (white_field_sum + white_eat_black_sum + black_died_sum + 6.5) + "目\n" +
                                            "黑吃了：" + black_eat_white_sum +
                                            "\n白吃了：" + white_eat_black_sum +
                                            "\n黑死子：" + black_died_sum +
                                            "\n白死子：" + white_died_sum +
                                            "\n你同意以上結果嗎?");
                                    black_sum = black_field_sum + black_eat_white_sum + white_died_sum;
                                    white_sum = white_field_sum + white_eat_black_sum + black_died_sum + 6.5;
                                    alert.showAndWait();
                                    if (alert.getResult().getButtonData().isDefaultButton()) {
                                        try {
                                            DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                                            dataOutputStream1.writeUTF("over " + chracterwho);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (alert.getResult().getButtonData().isCancelButton()) {
                                        try {
                                            DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                                            dataOutputStream1.writeUTF("choose_died");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                                        @Override
                                        public void handle(DialogEvent event) {
                                            try {
                                                DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                                                dataOutputStream1.writeUTF("choose_died");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    for (int i = 0; i < 19; i++) {
                                        for (int j = 0; j < 19; j++) {
                                            go[i][j].setText("");
                                        }
                                    }
                                    for (int i = 3; i <= 15; i += 6) {
                                        for (int j = 3; j <= 15; j += 6) {
                                            if (go[i][j].getBackground() == null_go) {
                                                go[i][j].setText("◎");
                                                go[i][j].setStyle("-fx-text-fill:Black;-fx-alignment:center;");
                                            }
                                        }
                                    }
                                }
                            });
                            chosedied_go = true;
                        } else if (str.equals("over")) {
                            DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                            if (chracterwho.equals("black")) {
                                if (black_sum < white_sum) {
                                    dataOutputStream1.writeUTF("lose " + username);
                                } else if (black_sum > white_sum) {
                                    dataOutputStream1.writeUTF("win " + username);
                                }
                            } else if (chracterwho.equals("white")) {
                                if (black_sum < white_sum) {
                                    dataOutputStream1.writeUTF("win " + username);
                                } else if (black_sum > white_sum) {
                                    dataOutputStream1.writeUTF("lose " + username);
                                }
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < 19; i++) {
                                        for (int j = 0; j < 19; j++) {
                                            go[i][j].setDisable(true);
                                            go[i][j].setOpacity(1.0);
                                        }
                                    }
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText(null);
                                    alert.setContentText("棋局已結束。若想重審結果，點選右方審核按鈕，將由官方人員判定勝負。");
                                    final_check = true;
                                    report_button.setDisable(false);
                                    final_button.setDisable(true);
                                    alert.show();
                                }
                            });
                        } else if (str.split(" ")[0].equals("time")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (me.getText().split(" ")[0].equals("黑番：")) {
                                            me_time.setText(Integer.toString(Integer.parseInt(str.split(" ")[1]) / 60) + "：" + Integer.toString(Integer.parseInt(str.split(" ")[1]) % 60));
                                            op_time.setText(Integer.parseInt(str.split(" ")[2]) / 60 + "：" + Integer.toString(Integer.parseInt(str.split(" ")[2]) % 60));
                                        } else if (me.getText().split(" ")[0].equals("白番：")) {
                                            me_time.setText(Integer.parseInt(str.split(" ")[2]) / 60 + "：" + Integer.toString(Integer.parseInt(str.split(" ")[2]) % 60));
                                            op_time.setText(Integer.parseInt(str.split(" ")[1]) / 60 + "：" + Integer.toString(Integer.parseInt(str.split(" ")[1]) % 60));
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("等待使用者加入連線");
                                    }
                                }
                            });
                        } else if (str.split(" ")[0].equals("disconnect")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText(null);
                                    alert.setContentText("你的對手已離開，若低於一定步數將不計勝負。");
                                    alert.show();
                                    chat_button.setDisable(true);
                                    report_button.setDisable(true);
                                    final_button.setDisable(true);
                                    timer.cancel();
                                    if (goarraylist.size() > 40) {
                                        try {
                                            DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                                            dataOutputStream1.writeUTF("win " + username);
                                            dataOutputStream1.writeUTF("lose " + opponent);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    for(int i=0;i<19;i++){
                                        for (int j=0;j<19;j++){
                                            go[i][j].setDisable(true);
                                        }
                                    }
                                    try {
                                        DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                                        dataOutputStream1.writeUTF("stop");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else if(str.split(" ")[0].equals("timeout")){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText(null);
                            alert.setContentText("你的對手超時負，若低於一定步數將不計勝負。");
                            alert.show();
                            chat_button.setDisable(true);
                            report_button.setDisable(true);
                            final_button.setDisable(true);
                            timer.cancel();
                            if (goarraylist.size() > 40) {
                                try {
                                    DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                                    dataOutputStream1.writeUTF("win " + username);
                                    dataOutputStream1.writeUTF("lose " + opponent);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                                dataOutputStream1.writeUTF("stop");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                                }
                            });
                        } else if (str.split(" ")[0].equals("stop")) {

                        } else {
                            int b = Integer.parseInt(str);
                            int i = b / 100;
                            int j = b % 100;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (chosedied_go) {
                                        if (go[b / 100][b % 100].getBackground() != null_go && !go[b / 100][b % 100].getText().equals("◎")) {
                                            block[0] = b;
                                            blocklength = 1;
                                            findblock(b / 100, b % 100);
                                            for (int i = 0; i < blocklength; i++) {
                                                go[block[i] / 100][block[i] % 100].setText("◎");
                                                if (go[block[i] / 100][block[i] % 100].getBackground() == white_go) {
                                                    go[block[i] / 100][block[i] % 100].setStyle("-fx-text-fill:black;-fx-alignment:center;");
                                                } else if (go[block[i] / 100][block[i] % 100].getBackground() == black_go) {
                                                    go[block[i] / 100][block[i] % 100].setStyle("-fx-text-fill:white;-fx-alignment:center;");
                                                }
                                            }
                                        } else if (go[b / 100][b % 100].getBackground() != null_go && go[b / 100][b % 100].getText().equals("◎")) {
                                            block[0] = b;
                                            blocklength = 1;
                                            findblock(b / 100, b % 100);
                                            for (int i = 0; i < blocklength; i++) {
                                                go[block[i] / 100][block[i] % 100].setText("");
                                            }
                                        }
                                    } else {
                                        if (go[i][j].getBackground() == null_go) {
                                            for (int m = 0; m < 19; m++) {
                                                for (int n = 0; n < 19; n++) {
                                                    go[m][n].setDisable(false);
                                                    go[m][n].setOpacity(1.0);
                                                }
                                            }
                                            if (suicide(b / 100, b % 100)) {
                                                System.out.println("禁手點");
                                            } else {
                                                go[i][j].setText("");
                                                posi_change();
                                                if (flag) {
                                                    go[i][j].setBackground(black_go);
                                                    go[i][j].setText("◎");
                                                    go[i][j].setStyle("-fx-text-fill:Red;-fx-alignment:center;");
                                                    black_audio.play(1);
                                                    eat_check(b / 100, b % 100);
                                                    flag = false;
                                                } else {
                                                    go[i][j].setBackground(white_go);
                                                    go[i][j].setText("◎");
                                                    go[i][j].setStyle("-fx-text-fill:Red;-fx-alignment:center;");
                                                    white_audio.play(1);
                                                    eat_check(b / 100, b % 100);
                                                    flag = true;
                                                }
                                                for (int i = 3; i <= 15; i += 6) {
                                                    for (int j = 3; j <= 15; j += 6) {
                                                        if (go[i][j].getBackground() == null_go) {
                                                            go[i][j].setText("◎");
                                                            go[i][j].setStyle("-fx-text-fill:black;-fx-alignment:center;");
                                                        }
                                                    }
                                                }
                                                goarraylist.add("" + b);
                                                position++;
                                                hands_num_lable.setText("手數：" + position);
                                                time_flag = true;
                                                skip_button.setDisable(false);
                                                final_button.setDisable(false);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    } catch (SocketException e) {
                        System.out.println("與伺服器斷線");
                        System.exit(-1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void posi_change() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (go[i][j].getText().equals("◎")) {
                    go[i][j].setText("");
                    go[i][j].setStyle("-fx-text-fill:Black");
                }
            }
        }
    }

    private void eat_check(int posi, int posj) {
        if (posi - 1 >= 0) {
            ///////////////////////////////LEFT//////////////////////////////
            block[0] = (posi - 1) * 100 + posj;
            blocklength = 1;
            findblock(posi - 1, posj);
            if (!hasSpace()) {
                for (int k = 0; k < blocklength; k++) {
                    if (go[block[k] / 100][block[k] % 100].getBackground() == black_go) {
                        white_eat_black_sum++;
                    } else if (go[block[k] / 100][block[k] % 100].getBackground() == white_go) {
                        black_eat_white_sum++;
                    }
                    go[block[k] / 100][block[k] % 100].setBackground(null_go);
                }
            }
        }
        /////////////////////////////////RIGHT/////////////////////////////////////
        if (posi + 1 < 19) {
            block[0] = (posi + 1) * 100 + posj;
            blocklength = 1;
            findblock(posi + 1, posj);
            if (!hasSpace()) {
                for (int k = 0; k < blocklength; k++) {
                    if (go[block[k] / 100][block[k] % 100].getBackground() == black_go) {
                        white_eat_black_sum++;
                    } else if (go[block[k] / 100][block[k] % 100].getBackground() == white_go) {
                        black_eat_white_sum++;
                    }
                    go[block[k] / 100][block[k] % 100].setBackground(null_go);
                }
            }
        }
        /////////////////////////////////UP//////////////////////////////////////////
        if (posj - 1 >= 0) {
            block[0] = posi * 100 + posj - 1;
            blocklength = 1;
            findblock(posi, posj - 1);
            if (!hasSpace()) {
                for (int k = 0; k < blocklength; k++) {
                    if (go[block[k] / 100][block[k] % 100].getBackground() == black_go) {
                        white_eat_black_sum++;
                    } else if (go[block[k] / 100][block[k] % 100].getBackground() == white_go) {
                        black_eat_white_sum++;
                    }
                    go[block[k] / 100][block[k] % 100].setBackground(null_go);
                }
            }
        }
        //////////////////////////////////DOWN//////////////////////////////////////
        if (posj + 1 < 19) {
            block[0] = posi * 100 + posj + 1;
            blocklength = 1;
            findblock(posi, posj + 1);
            if (!hasSpace()) {
                for (int k = 0; k < blocklength; k++) {
                    if (go[block[k] / 100][block[k] % 100].getBackground() == black_go) {
                        white_eat_black_sum++;
                    } else if (go[block[k] / 100][block[k] % 100].getBackground() == white_go) {
                        black_eat_white_sum++;
                    }
                    go[block[k] / 100][block[k] % 100].setBackground(null_go);
                }
            }
        }
    }

    private void findblock(int i, int j) {
        try {
            if (i - 1 >= 0 && go[i][j].getBackground() == go[i - 1][j].getBackground() && hasbeenfound((i - 1) * 100 + j)) {
                block[blocklength] = (i - 1) * 100 + j;
                blocklength++;
                findblock(i - 1, j);//left
            }
            if (i + 1 < 19 && go[i][j].getBackground() == go[i + 1][j].getBackground() && hasbeenfound((i + 1) * 100 + j)) {
                block[blocklength] = (i + 1) * 100 + j;
                blocklength++;
                findblock(i + 1, j);//right
            }
            if (j - 1 >= 0 && go[i][j].getBackground() == go[i][j - 1].getBackground() && hasbeenfound(i * 100 + j - 1)) {
                block[blocklength] = i * 100 + j - 1;
                blocklength++;
                findblock(i, j - 1);//UP
            }
            if (j + 1 < 19 && go[i][j].getBackground() == go[i][j + 1].getBackground() && hasbeenfound(i * 100 + j + 1)) {
                block[blocklength] = i * 100 + j + 1;
                blocklength++;
                findblock(i, j + 1);//Down
            }
        } catch (ArrayIndexOutOfBoundsException e) {

        }
    }

    private boolean hasbeenfound(int go_pos) {
        for (int i = 0; i < blocklength; i++) {
            if (block[i] == go_pos) {
                return false;
            }
        }
        return true;
    }

    private boolean hasSpace() {
        for (int i = 0; i < blocklength; i++) {
            int k = block[i] / 100;
            int j = block[i] % 100;
            try {
                if (go[k + 1][j].getBackground() == null_go && k + 1 < 19) {//right
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (go[k - 1][j].getBackground() == null_go && k - 1 >= 0) {//left
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (go[k][j + 1].getBackground() == null_go && j + 1 < 19) {//down
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (go[k][j - 1].getBackground() == null_go && j - 1 >= 0) {//up
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }

        }
        return false;
    }

    private boolean suicide(int posi, int posj) {
        Background background = null;
        if (flag) {
            background = black_go;
            go[posi][posj].setBackground(black_go);
        } else {
            background = white_go;
            go[posi][posj].setBackground(white_go);
        }
        if (posi - 1 >= 0) {
            ///////////////////////////////LEFT//////////////////////////////
            block[0] = (posi - 1) * 100 + posj;
            blocklength = 1;
            findblock(posi - 1, posj);
            if (!hasSpace()) {
                if (background != go[posi - 1][posj].getBackground() && go[posi - 1][posj].getBackground() != null_go) {
                    //
                    if (posi * 100 + posj == onlyonego_be_ate_pos && block[0] == lastgopos && blocklength == 1) {
                        go[posi][posj].setBackground(null_go);
                        System.out.println("打劫");
                        return true;
                    }
                    if (blocklength == 1) {
                        onlyonego_be_ate_pos = block[0];
                    }
                    //
                    for (int k = 0; k < blocklength; k++) {
                        if (go[block[k] / 100][block[k] % 100].getBackground() == black_go) {
                            white_eat_black_sum++;
                        } else if (go[block[k] / 100][block[k] % 100].getBackground() == white_go) {
                            black_eat_white_sum++;
                        }
                        go[block[k] / 100][block[k] % 100].setBackground(null_go);
                    }
                }
            }
        }
        /////////////////////////////////RIGHT/////////////////////////////////////
        if (posi + 1 < 19) {
            block[0] = (posi + 1) * 100 + posj;
            blocklength = 1;
            findblock(posi + 1, posj);
            if (!hasSpace()) {
                if (background != go[posi + 1][posj].getBackground() && go[posi + 1][posj].getBackground() != null_go) {
                    //
                    if (posi * 100 + posj == onlyonego_be_ate_pos && block[0] == lastgopos && blocklength == 1) {
                        go[posi][posj].setBackground(null_go);
                        System.out.println("打劫");
                        return true;
                    }
                    if (blocklength == 1) {
                        onlyonego_be_ate_pos = block[0];
                    }
                    //
                    for (int k = 0; k < blocklength; k++) {
                        if (go[block[k] / 100][block[k] % 100].getBackground() == black_go) {
                            white_eat_black_sum++;
                        } else if (go[block[k] / 100][block[k] % 100].getBackground() == white_go) {
                            black_eat_white_sum++;
                        }
                        go[block[k] / 100][block[k] % 100].setBackground(null_go);
                    }
                }
            }
        }
        /////////////////////////////////UP//////////////////////////////////////////
        if (posj - 1 >= 0) {
            block[0] = posi * 100 + posj - 1;
            blocklength = 1;
            findblock(posi, posj - 1);
            if (!hasSpace()) {
                if (background != go[posi][posj - 1].getBackground() && go[posi][posj - 1].getBackground() != null_go) {
                    if (posi * 100 + posj == onlyonego_be_ate_pos && block[0] == lastgopos && blocklength == 1) {
                        go[posi][posj].setBackground(null_go);
                        System.out.println("打劫");
                        return true;
                    }
                    //
                    if (blocklength == 1) {
                        onlyonego_be_ate_pos = block[0];
                    }
                    //
                    for (int k = 0; k < blocklength; k++) {
                        if (go[block[k] / 100][block[k] % 100].getBackground() == black_go) {
                            white_eat_black_sum++;
                        } else if (go[block[k] / 100][block[k] % 100].getBackground() == white_go) {
                            black_eat_white_sum++;
                        }
                        go[block[k] / 100][block[k] % 100].setBackground(null_go);
                    }
                }
            }
        }
        //////////////////////////////////DOWN//////////////////////////////////////
        if (posj + 1 < 19) {
            block[0] = posi * 100 + posj + 1;
            blocklength = 1;
            findblock(posi, posj + 1);
            if (!hasSpace()) {
                System.out.println("DOWN");
                if (background != go[posi][posj + 1].getBackground() && go[posi][posj + 1].getBackground() != null_go) {
                    if (posi * 100 + posj == onlyonego_be_ate_pos && block[0] == lastgopos && blocklength == 1) {
                        go[posi][posj].setBackground(null_go);
                        System.out.println("打劫");
                        return true;
                    }
                    //
                    if (blocklength == 1) {
                        onlyonego_be_ate_pos = block[0];
                    }
                    //
                    for (int k = 0; k < blocklength; k++) {
                        if (go[block[k] / 100][block[k] % 100].getBackground() == black_go) {
                            white_eat_black_sum++;
                        } else if (go[block[k] / 100][block[k] % 100].getBackground() == white_go) {
                            black_eat_white_sum++;
                        }
                        go[block[k] / 100][block[k] % 100].setBackground(null_go);
                        System.out.println("DOWN_DO");
                    }
                }
            }
        }
        block[0] = posi * 100 + posj;
        blocklength = 1;
        findblock(posi, posj);
        if (!hasSpace()) {
            System.out.println("CENTER");
            if (background != go[posi][posj].getBackground()) {
                for (int k = 0; k < blocklength; k++) {
                    go[block[k] / 100][block[k] % 100].setBackground(null_go);
                    System.out.println("CENTER_DO");
                }
                return true;
            } else if (background == go[posi][posj].getBackground()) {
                go[posi][posj].setBackground(null_go);
                System.out.println("CENTER_DO2");
                return true;
            }
        }
        lastgopos = posi * 100 + posj;
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void check_field() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (go[i][j].getBackground() == null_go && hasbeenfound(i * 100 + j)) {
                    block[0] = i * 100 + j;
                    blocklength = 1;
                    findblock(i, j);
                    whofield();
                }
            }
        }
    }

    public void whofield() {
        boolean blackfield = false;
        boolean whitefield = false;
        for (int i = 0; i < blocklength; i++) {
            int m = block[i] / 100;
            int n = block[i] % 100;
            try {
                if (go[m + 1][n].getBackground() == black_go && m + 1 < 19) {
                    if (go[m + 1][n].getText().equals("")) {
                        blackfield = true;
                    } else {
                        whitefield = true;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {

                if (go[m - 1][n].getBackground() == black_go && m - 1 >= 0) {
                    if (go[m - 1][n].getText().equals("")) {
                        blackfield = true;
                    } else {
                        whitefield = true;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (go[m][n + 1].getBackground() == black_go && n + 1 < 19) {
                    if (go[m][n + 1].getText().equals("")) {
                        blackfield = true;
                    } else {
                        whitefield = true;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (go[m][n - 1].getBackground() == black_go && n - 1 >= 0) {
                    if (go[m][n - 1].getText().equals("")) {
                        blackfield = true;
                    } else {
                        whitefield = true;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }

            //////////////////////////////////////////////
            try {
                if (go[m + 1][n].getBackground() == white_go && m + 1 < 19) {
                    if (go[m + 1][n].getText().equals("")) {
                        whitefield = true;
                    } else {
                        blackfield = true;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (go[m - 1][n].getBackground() == white_go && m - 1 >= 0) {
                    if (go[m - 1][n].getText().equals("")) {
                        whitefield = true;
                    } else {
                        blackfield = true;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (go[m][n + 1].getBackground() == white_go && n + 1 < 19) {
                    if (go[m][n + 1].getText().equals("")) {
                        whitefield = true;
                    } else {
                        blackfield = true;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (go[m][n - 1].getBackground() == white_go && n - 1 >= 0) {
                    if (go[m][n - 1].getText().equals("")) {
                        whitefield = true;
                    } else {
                        blackfield = true;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        }
        //////////////////////////////////////////////////
        if (whitefield && blackfield) {
            whitefield = false;
            blackfield = false;
            for (int j = 0; j < blocklength; j++) {
                go[block[j] / 100][block[j] % 100].setText("X");
                go[block[j] / 100][block[j] % 100].setStyle("-fx-text-fill:black;-fx-alignment:center;");
            }
        }
        if (whitefield) {
            for (int j = 0; j < blocklength; j++) {
                if (!go[block[j] / 100][block[j] % 100].getText().equals("X")) {
                    go[block[j] / 100][block[j] % 100].setText("◎");
                    go[block[j] / 100][block[j] % 100].setStyle("-fx-text-fill:white;-fx-alignment:center;");
                }
            }
        }
        if (blackfield) {
            for (int j = 0; j < blocklength; j++) {
                if (!go[block[j] / 100][block[j] % 100].getText().equals("X")) {
                    go[block[j] / 100][block[j] % 100].setText("◎");
                    go[block[j] / 100][block[j] % 100].setStyle("-fx-text-fill:black;-fx-alignment:center;");
                }
            }
        }
    }

    public void setUsername(String username) {
        me.setText(username);
        this.username = username;
    }

    public void setOpponent(String opponent1) {
        op.setText(opponent1);
        this.opponent = opponent1;
    }

    public void setNums_lable(Double f) {
        nums_lable.setText(nums_lable.getText() + Double.toString(f));
    }

    public void setTime(int time1) {
        this.time = time1 * 60;
    }

    public void setUserinfo(String userinfo1) {
        this.userinfo = userinfo1;
    }
}
