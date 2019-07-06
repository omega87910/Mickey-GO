package sample;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RuleFrame extends Application{
    static Stage stage=new Stage();
    float sum = 0.0f;
    int minutes = 0;
   Double nums=0.0;
    Spinner spinner =new Spinner();
    Spinner spinner2 =new Spinner();
    Spinner spinner3 =new Spinner();
    Spinner spinner4 =new Spinner();
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane=new Pane();
        Button button=new Button("確定");
        Button button2=new Button("取消");
        pane.setBackground(new Background(new BackgroundFill(Color.GRAY,null,null)));
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,180,60));
        spinner2.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(5.5,7.5,6.5,1));
        spinner3.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(5,60,30,1));
        spinner4.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,5,3,1));
        Label rule = new Label("規則：");
        Label num = new Label("貼目：");
        Label time = new Label("時間：");
        Label min = new Label("分鐘");
        Label timeout = new Label("讀秒：");
        Label timeout_sec = new Label("秒");
        Label timeout_times = new Label("次");
        MenuButton rule_menu = new MenuButton();
        MenuItem rule_1 = new MenuItem("分先");
        MenuItem rule_2 = new MenuItem("讓先");
        rule_menu.setText(rule_1.getText());
        rule_menu.getItems().addAll(rule_1, rule_2);
        Group group = new Group(pane,rule, num, time, rule_menu,min,spinner,spinner2,spinner3,spinner4,timeout,timeout_times,timeout_sec,button,button2);
        rule.setLayoutX(70);
        rule.setLayoutY(35);
        num.setLayoutX(70);
        num.setLayoutY(65);
        time.setLayoutX(70);
        time.setLayoutY(95);
        rule_menu.setLayoutX(120);
        rule_menu.setLayoutY(30);
        rule_menu.setPrefWidth(80);
       spinner2.setLayoutX(120);
        spinner2.setLayoutY(60);
        spinner2.setPrefWidth(80);
        spinner.setLayoutX(120);
        spinner.setLayoutY(90);
        spinner.setPrefWidth(80);
        spinner3.setLayoutX(120);
        spinner3.setLayoutY(120);
        spinner3.setPrefWidth(60);
        spinner4.setLayoutX(200);
        spinner4.setLayoutY(120);
        spinner4.setPrefWidth(60);
        timeout.setLayoutX(70);
        timeout.setLayoutY(125);
        timeout_sec.setLayoutX(180);
        timeout_sec.setLayoutY(125);
        timeout_times.setLayoutX(260);
        timeout_times.setLayoutY(125);
        min.setLayoutX(200);
        min.setLayoutY(95);
        pane.setPrefHeight(200);
        pane.setPrefWidth(300);
        button.setDefaultButton(true);
        button.setLayoutX(120);
        button.setLayoutY(160);
        button2.setCancelButton(true);
        button2.setLayoutX(170);
        button2.setLayoutY(160);
        stage.setScene(new Scene(group, 300, 200));
        stage.setTitle("對局規則");
        stage.setResizable(false);
        stage.show();
        rule_1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rule_menu.setText(rule_1.getText());
            }
        });
        rule_2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rule_menu.setText(rule_2.getText());
            }
        });
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
    }
    public int getMinutes() {
        minutes=(int)spinner.getValueFactory().getValue();
        return minutes;
    }

    public Double getNums() {
        nums= (Double) spinner2.getValueFactory().getValue();
        return nums;
    }
}
