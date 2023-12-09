package com.calculator.calculation;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @author sxq
 * @Description 主程序入口
 * @date 2023/11/26 13:52
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 730);
        stage.setTitle("Calculator");
        stage.setResizable(false);  // ui1
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}