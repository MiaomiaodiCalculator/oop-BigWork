package com.calculator.calculation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import user.User;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import static Database.DataInit.*;

/**
 * @author Bu Xinran
 * @Description 实现用户登录界面
 * @date 2023/12/5 23:36
 */
public class LoginController {
    public TextArea usernameArea;
    public PasswordField passwordArea;
    public static boolean userState=false;
    public static String userName;
    public Button loginButton;

    /***
     * @Description 登录
     * @author Bu Xinran
     * @date 2023/12/7 10:52
    **/
    public void login() throws IOException {
        String name=usernameArea.getText();
        String password=passwordArea.getText();
        User user=new User(name,password);
        if(!Objects.equals(name, "") && !Objects.equals(password, "")&&exists(user)&&getByName(name).getPassword().equals(password)){
            userState=true;
            userName=name;
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
            Stage stage=new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("Main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 730);
            stage.setTitle("Calculator");
            stage.setScene(scene);
            stage.show();
        }
    }
    /***
     * @Description 注册
     * @author Bu Xinran
     * @date 2023/12/7 10:52
    **/
    public void signUp() {
        String title="注册";
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("新用户注册");
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("用户名："), 0, 0);
        gridPane.add(new Label("密码："), 0, 1);
        gridPane.add(new Label("确认密码:"), 0, 2);
        TextArea name = new TextArea();
        name.setPromptText("7-15位数字或字母");
        PasswordField password=new PasswordField();
        password.setPromptText("7-15位由英文字母、数字和特殊字符（@，_，%，$）组成");
        PasswordField again=new PasswordField();
        again.setPromptText("再次确认密码");
        name.setPrefWidth(300);
        password.setPrefWidth(300);
        again.setPrefWidth(300);
        name.setPrefHeight(40);
        password.setPrefHeight(40);
        again.setPrefHeight(40);
        gridPane.add(name, 1, 0);
        gridPane.add(password, 1, 1);
        gridPane.add(again, 1, 2);
        gridPane.setPrefWidth(400);
        gridPane.setPrefHeight(150);
        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            String username=name.getText();
            String pass=password.getText();
            String rewrite=again.getText();
            User user=new User(username,pass);
            if(checkName(username)&&checkPassword(pass)&&pass.equals(rewrite)&&!exists(user)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("注册成功");
                alert.setHeaderText("用户"+username+":您已注册成功！");
                alert.showAndWait();
                dialog.close();
                add(user);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText("用户名或密码不合法！");
                alert.showAndWait();
            }
        }
    }
    /***
     * @Description  检查用户名是否合法
     * @param name 用户名
     * @return boolean
     * @author Bu Xinran
     * @date 2023/12/7 10:47
    **/
    public static boolean checkName(String name){
        String regex = "^[a-zA-Z0-9]{7,15}$";
        return name.matches(regex);
    }
    /***
     * @Description  检查密码是否正确
     * @param password 用户输入的密码
     * @return boolean
     * @author Bu Xinran
     * @date 2023/12/7 10:51
    **/
    public static boolean checkPassword(String password){
        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@_$%]).{7,15}$";
        return password.matches(regex);
    }
}
