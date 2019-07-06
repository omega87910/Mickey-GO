package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
//import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
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


public class Play_Single extends Application {
    private Label[][] go = new Label[19][19];
    private Background black_go = new Background(new BackgroundFill(Color.BLACK, new CornerRadii(50), new Insets(3)));
    private Background white_go = new Background(new BackgroundFill(Color.WHITE, new CornerRadii(50), new Insets(3)));
    private Background null_go = null;
    private int block[] = new int[19 * 19];
    private int blocklength;
    private String username = "";
    private String opponent = "";
    static String msg = "";
    Label nums_lable = new Label("貼目：" + 6.5);
    Label hands_num_lable = new Label("手數：");
    Label me = new Label();
    Label op = new Label();
    Label me_time = new Label();
    Label op_time = new Label();
    Slider slider = new Slider();
    int time = 3600;
    ArrayList<String> goarraylist = new ArrayList();
    boolean flag = true;
    int position = 0;
    int black_sum = 0;
    int white_sum = 0;
    int black_eat_white_sum = 0;
    int white_eat_black_sum = 0;
    int black_died_sum = 0;
    int white_died_sum = 0;
    boolean chosedied_go = false;
    boolean showfield = false;
    int onlyonego_be_ate_pos = 0;
    int lastgopos = 0;
    String skip_state = "";

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.getIcons().add(new Image("sample/image/sai.jpg"));
        primaryStage.setTitle("MickeyGO");
        /////////////////////////////Sound////////////////////////////////////
        AudioClip black_audio = new AudioClip(this.getClass().getResource("sound/black.mp3").toString());
        AudioClip white_audio = new AudioClip(this.getClass().getResource("sound/white.mp3").toString());
        /////////////////////////////Menu/////////////////////////////////////

        /////////////////////////////BUTTON////////////////////////////////////
        Button field_button = new Button("局勢分析");
        Button final_button = new Button("勝負判定");
        Button skip_button = new Button("虛一手");
        Button readgolist_button = new Button("讀檔");
        Button savegolist_button = new Button("存檔");
        Button back_button = new Button("上一步");
        Button next_button = new Button("下一步");
        final_button.setLayoutX(1135);
        final_button.setLayoutY(360);
        final_button.setPrefWidth(100);
        final_button.setPrefHeight(50);
        field_button.setLayoutX(1025);
        field_button.setLayoutY(360);
        field_button.setPrefWidth(100);
        field_button.setPrefHeight(50);
        skip_button.setLayoutX(1025);
        skip_button.setLayoutY(420);
        skip_button.setPrefWidth(100);
        skip_button.setPrefHeight(50);
        readgolist_button.setLayoutX(1025);
        readgolist_button.setLayoutY(480);
        readgolist_button.setPrefWidth(100);
        readgolist_button.setPrefHeight(50);
        savegolist_button.setLayoutX(1135);
        savegolist_button.setLayoutY(480);
        savegolist_button.setPrefWidth(100);
        savegolist_button.setPrefHeight(50);
        back_button.setLayoutX(1025);
        back_button.setLayoutY(540);
        back_button.setPrefWidth(100);
        back_button.setPrefHeight(50);
        next_button.setLayoutX(1135);
        next_button.setLayoutY(540);
        next_button.setPrefWidth(100);
        next_button.setPrefHeight(50);
        slider.setLayoutX(1025);
        slider.setLayoutY(740);
        slider.setPrefWidth(200);
        slider.setPrefHeight(30);
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
        final_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!chosedied_go) {
                    chosedied_go = true;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("請點選死子");
                    alert.show();
                    next_button.setDisable(true);
                    back_button.setDisable(true);
                    field_button.setDisable(true);
                    savegolist_button.setDisable(true);
                    readgolist_button.setDisable(true);
                    skip_button.setDisable(true);
                } else {
                    black_sum = 0;
                    white_sum = 0;
                    black_died_sum = 0;
                    white_died_sum = 0;
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            if (go[i][j].getText().equals("◎") &&go[i][j].getStyle().equals("-fx-text-fill:Red;-fx-alignment:center;")) {
                                go[i][j].setText("");
                            }
                        }
                    }
                    check_field();
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            if (go[i][j].getText().equals("◎") && go[i][j].getStyle().equals("-fx-text-fill:white;-fx-alignment:center;")) {
                                white_sum++;
                                if (go[i][j].getBackground() != null_go) {
                                    black_died_sum++;
                                }
                            }
                            if (go[i][j].getText().equals("◎") && go[i][j].getStyle().equals("-fx-text-fill:black;-fx-alignment:center;")) {
                                black_sum++;
                                if (go[i][j].getBackground() != null_go) {
                                    white_died_sum++;
                                }
                            }
                        }
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("黑棋：" + (black_sum + black_eat_white_sum + white_died_sum) + "目，白棋：" + (white_sum + white_eat_black_sum + black_died_sum + 6.5) + "目\n黑吃了：" + black_eat_white_sum + "\n白吃了：" + white_eat_black_sum + "\n黑死子：" + black_died_sum + "\n白死子：" + white_died_sum);
                    alert.showAndWait();
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
                    chosedied_go = false;
                    skip_button.setDisable(false);
                    next_button.setDisable(false);
                    back_button.setDisable(false);
                    field_button.setDisable(false);
                    savegolist_button.setDisable(false);
                    readgolist_button.setDisable(false);
                }
            }
        });
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                black_eat_white_sum = 0;
                white_eat_black_sum = 0;
                skip_state = "";
                flag = true;
                for (int i = 0; i < 19; i++) {
                    for (int j = 0; j < 19; j++) {
                        go[i][j].setBackground(null_go);
                    }
                }
                if (position > 0) {
                    position--;
                    if (position == 0) {
                        for (int i = 0; i < 19; i++) {
                            for (int j = 0; j < 19; j++) {
                                go[i][j].setText("");
                                go[i][j].setStyle("-fx-text-fill:Black;-fx-alignment:center;");
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
                        hands_num_lable.setText("手數：" + position);
                    }
                }
                for (int k = 0; k < position; k++) {
                    int m = 0;
                    int n = 0;
                    if (goarraylist.get(k).charAt(0) == '!') {
                        flag = !flag;
                        m = Integer.parseInt(goarraylist.get(k).replaceFirst("!", "")) / 100;
                        n = Integer.parseInt(goarraylist.get(k).replaceFirst("!", "")) % 100;
                    } else {
                        m = Integer.parseInt(goarraylist.get(k)) / 100;
                        n = Integer.parseInt(goarraylist.get(k)) % 100;
                    }
                    if (go[m][n].getBackground() == null_go) {
                        go[m][n].setText("");
                        posi_change();
                        if (flag) {
                            go[m][n].setBackground(black_go);
                            go[m][n].setText("◎");
                            go[m][n].setStyle("-fx-text-fill:Red;-fx-alignment:center;");
                            if (k == position - 1) {
                                black_audio.play();
                            }
                            eat_check(m, n);
                            flag = false;
                        } else {
                            go[m][n].setBackground(white_go);
                            go[m][n].setText("◎");
                            go[m][n].setStyle("-fx-text-fill:Red;-fx-alignment:center;");
                            if (k == position - 1) {
                                white_audio.play();
                            }
                            eat_check(m, n);
                            flag = true;
                        }
                        for (int i = 3; i <= 15; i += 6) {
                            for (int j = 3; j <= 15; j += 6) {
                                if (go[i][j].getBackground() == null_go) {
                                    go[i][j].setText("◎");
                                    go[i][j].setStyle("-fx-text-fill:Black;-fx-alignment:center;");
                                }
                            }
                        }
                        hands_num_lable.setText("手數：" + position);
                    }
                }
                slider.setValue(position);
            }
        });
        next_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                black_eat_white_sum = 0;
                white_eat_black_sum = 0;
                skip_state = "";
                flag = true;
                for (int i = 0; i < 19; i++) {
                    for (int j = 0; j < 19; j++) {
                        go[i][j].setBackground(null_go);
                    }
                }
                if (position < goarraylist.size()) {
                    position++;
                }
                for (int k = 0; k < position; k++) {
                    int m = 0;
                    int n = 0;
                    if (goarraylist.get(k).charAt(0) == '!') {
                        flag = !flag;
                        m = Integer.parseInt(goarraylist.get(k).replaceFirst("!", "")) / 100;
                        n = Integer.parseInt(goarraylist.get(k).replaceFirst("!", "")) % 100;
                    } else {
                        m = Integer.parseInt(goarraylist.get(k)) / 100;
                        n = Integer.parseInt(goarraylist.get(k)) % 100;
                    }
                    if (go[m][n].getBackground() == null_go) {
                        go[m][n].setText("");
                        posi_change();
                        if (flag) {
                            go[m][n].setBackground(black_go);
                            go[m][n].setText("◎");
                            go[m][n].setStyle("-fx-text-fill:Red;-fx-alignment:center;");
                            if (k == position - 1) {
                                black_audio.play();
                            }
                            eat_check(m, n);
                            flag = false;
                        } else {
                            go[m][n].setBackground(white_go);
                            go[m][n].setText("◎");
                            go[m][n].setStyle("-fx-text-fill:Red;-fx-alignment:center;");
                            if (k == position - 1) {
                                white_audio.play();
                            }
                            eat_check(m, n);
                            flag = true;
                        }
                        for (int i = 3; i <= 15; i += 6) {
                            for (int j = 3; j <= 15; j += 6) {
                                if (go[i][j].getBackground() == null_go) {
                                    go[i][j].setText("◎");
                                    go[i][j].setStyle("-fx-text-fill:Black;-fx-alignment:center;");
                                }
                            }
                        }
                        hands_num_lable.setText("手數：" + position);
                    }
                }
                slider.setValue(position);
            }
        });
        skip_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                flag = !flag;
                if (skip_state.equals("")) {
                    skip_state = "!";
                } else {
                    skip_state = "";
                }
            }
        });
        field_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!showfield) {
                    showfield = true;
                    black_sum = 0;
                    white_sum = 0;
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
                                white_sum++;
                                if (go[i][j].getBackground() != null_go) {
                                    black_died_sum++;
                                }
                            }
                            if (go[i][j].getText().equals("◎") && go[i][j].getStyle().equals("-fx-text-fill:black;-fx-alignment:center;")) {
                                black_sum++;
                                if (go[i][j].getBackground() != null_go) {
                                    white_died_sum++;
                                }
                            }
                        }
                    }
                    chosedied_go = false;
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
        ///////////////////////////////////////////////////////////////////////////////////
        Pane pane2 = new Pane();
        pane2.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        pane2.setPrefWidth(1280);
        pane2.setPrefHeight(1000);
        Pane pane = new Pane();
        Group group = new Group(pane2, pane, gridPane, gridPane2, nums_lable, hands_num_lable, me, op, me_time, op_time, field_button, skip_button, readgolist_button, savegolist_button, back_button, next_button, final_button, slider);
        Scene scene = new Scene(group, 1280, 1000);
        scene.setCursor(new ImageCursor(new Image("sample/image/ram.png")));
        primaryStage.setScene(scene);
        gridPane.addColumn(17);
        gridPane.addRow(17);
        gridPane2.addColumn(18);
        gridPane2.addRow(18);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                position = newValue.intValue();
                black_eat_white_sum = 0;
                white_eat_black_sum = 0;
                skip_state = "";
                flag = true;
                for (int i = 0; i < 19; i++) {
                    for (int j = 0; j < 19; j++) {
                        go[i][j].setBackground(null_go);
                    }
                }
                if (position == 0) {
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            go[i][j].setText("");
                            go[i][j].setStyle("-fx-text-fill:Black;-fx-alignment:center;");
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
                    hands_num_lable.setText("手數：" + position);
                }

                for (int k = 0; k < position; k++) {
                    int m = 0;
                    int n = 0;
                    if (goarraylist.get(k).charAt(0) == '!') {
                        flag = !flag;
                        m = Integer.parseInt(goarraylist.get(k).replaceFirst("!", "")) / 100;
                        n = Integer.parseInt(goarraylist.get(k).replaceFirst("!", "")) % 100;
                    } else {
                        m = Integer.parseInt(goarraylist.get(k)) / 100;
                        n = Integer.parseInt(goarraylist.get(k)) % 100;
                    }
                    if (go[m][n].getBackground() == null_go) {
                        if (suicide(m, n)) {
                            System.out.println("禁手點");
                        } else {
                            go[m][n].setText("");
                            posi_change();
                            if (flag) {
                                go[m][n].setBackground(black_go);
                                go[m][n].setText("◎");
                                go[m][n].setStyle("-fx-text-fill:Red;-fx-alignment:center;");
                                eat_check(m, n);
                                flag = false;
                            } else {
                                go[m][n].setBackground(white_go);
                                go[m][n].setText("◎");
                                go[m][n].setStyle("-fx-text-fill:Red;-fx-alignment:center;");
                                eat_check(m, n);
                                flag = true;
                            }
                            for (int i = 3; i <= 15; i += 6) {
                                for (int j = 3; j <= 15; j += 6) {
                                    if (go[i][j].getBackground() == null_go) {
                                        go[i][j].setText("◎");
                                        go[i][j].setStyle("-fx-text-fill:Black;-fx-alignment:center;");
                                    }
                                }
                            }
                            hands_num_lable.setText("手數：" + position);
                        }
                    }
                }
            }
        });
        readgolist_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(null);
                try {
                    if (!goarraylist.isEmpty()) {
                        goarraylist = new ArrayList<String>();
                    }
                    FileReader fileReader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String str = bufferedReader.readLine();
                    while (str != null) {
                        goarraylist.add(str);
                        str = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                    fileReader.close();
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            go[i][j].setBackground(null_go);
                            go[i][j].setText("");
                            go[i][j].setStyle("-fx-text-fill:black;-fx-alignment:center;");
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
                    flag = true;
                    for (int i = 0; i < goarraylist.size(); i++) {
                        int m=0,n=0;
                        if (goarraylist.get(i).charAt(0) == '!') {
                            flag = !flag;
                            m = Integer.parseInt(goarraylist.get(i).replaceFirst("!", "")) / 100;
                            n = Integer.parseInt(goarraylist.get(i).replaceFirst("!", "")) % 100;
                        } else {
                            m = Integer.parseInt(goarraylist.get(i)) / 100;
                            n = Integer.parseInt(goarraylist.get(i)) % 100;
                        }
                        if (go[m][n].getBackground() == null_go) {
                            go[m][n].setText("");
                            if (flag) {
                                go[m][n].setBackground(black_go);
                                eat_check(m, n);
                                flag = false;
                            } else {
                                go[m][n].setBackground(white_go);
                                eat_check(m, n);
                                flag = true;
                            }
                        }
                    }
                    for (int i = 3; i <= 15; i += 6) {
                        for (int j = 3; j <= 15; j += 6) {
                            if (go[i][j].getBackground() == null_go) {
                                go[i][j].setText("◎");
                                go[i][j].setStyle("-fx-text-fill:black;-fx-alignment:center;");
                            }
                        }
                    }
                    position = goarraylist.size();
                    hands_num_lable.setText("手數：" + position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                slider.setMin(0);
                slider.setMax(goarraylist.size());
            }
        });
        savegolist_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showSaveDialog(null);
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    for (int i = 0; i < goarraylist.size(); i++) {
                        bufferedWriter.write(goarraylist.get(i) + "\r\n");
                    }
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
                gridPane2.add(go[i][j], i, j);
                int a = i * 100 + j;
                go[i][j].setFont(new Font(30));
                go[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Label label = (Label) event.getSource();
                        if (chosedied_go) {
                            if (go[a / 100][a % 100].getBackground() != null_go && !go[a / 100][a % 100].getText().equals("◎")) {
                                block[0] = a;
                                blocklength = 1;
                                findblock(a / 100, a % 100);
                                for (int i = 0; i < blocklength; i++) {
                                    go[block[i] / 100][block[i] % 100].setText("◎");
                                    if (go[block[i] / 100][block[i] % 100].getBackground() == white_go) {
                                        go[block[i] / 100][block[i] % 100].setStyle("-fx-text-fill:black;-fx-alignment:center;");
                                    } else if (go[block[i] / 100][block[i] % 100].getBackground() == black_go) {
                                        go[block[i] / 100][block[i] % 100].setStyle("-fx-text-fill:white;-fx-alignment:center;");
                                    }
                                }
                            } else if (go[a / 100][a % 100].getBackground() != null_go && go[a / 100][a % 100].getText().equals("◎")) {
                                block[0] = a;
                                blocklength = 1;
                                findblock(a / 100, a % 100);
                                for (int i = 0; i < blocklength; i++) {
                                    go[block[i] / 100][block[i] % 100].setText("");
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
                                    goarraylist.add(skip_state + a);
                                    skip_state = "";
                                    position++;
                                    hands_num_lable.setText("手數：" + position);
                                    slider.setMax(goarraylist.size());
                                    slider.setValue(goarraylist.size());
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
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void posi_change() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (go[i][j].getText().equals("◎")) {
                    go[i][j].setText("");
                    go[i][j].setStyle("-fx-text-fill:black");
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
                        System.out.println(block[k]);
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

    public static void main(String[] args) {
        launch(args);
    }
}
