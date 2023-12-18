package com.calculator.calculation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import user.User;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import static Database.SqlUser.*;

/**
 * @author Bu Xinran
 * @Description 实现用户登录界面
 * @date 2023/12/5 23:36
 */
public class LoginController {
    public TextField usernameArea;
    public PasswordField passwordArea;
    public static boolean state;
    public static boolean userState=false;
    public static String userName="abc";
    public static String passWord=" ";
    public Button loginButton;
    public Button signup;
    public Button visitor;
    public Label Text1;
    public Label Text2;
    public Label Title;

    // 完善逻辑
    public static Stage PrimaryStage = new Stage();

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
            passWord=password;
            state=true;
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
            Stage stage = PrimaryStage;
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("Main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 730);
            MainController.mainController=fxmlLoader.getController();
            stage.setTitle("喵喵滴计算器");
            Image icon = new Image("icon.png");
            stage.getIcons().add(icon);
            Image cursorImage = new Image("cur.png");
            ImageCursor cursor = new ImageCursor(cursorImage);
            stage.setScene(scene);
            scene.setCursor(cursor);
            // 改动tableView，统一前端风格
//            scene.getStylesheets().add(getClass().getResource("com/calculator/calculation/css/tableViewStyle.css").toExternalForm());
            // 完善关闭逻辑，主舞台关闭需要把其他子舞台都关闭UI
            stage.setOnCloseRequest(event -> {
                // 关闭所有子舞台
                MainController.PersonStage.close();
                // 退出应用程序
                System.exit(0);
            });
            stage.show();
        }else{
            Dialog alert = new Dialog();
            alert.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("style/dialog.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("success");
            alert.setTitle("错误");
            alert.setHeaderText("用户名不存在或密码错误！");
            alert.showAndWait();
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
        Scene dialogScene = dialog.getDialogPane().getScene();
        dialogScene.getStylesheets().add(getClass().getResource("style/dialog.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        dialog.setTitle(title);
        dialog.setHeaderText("新用户注册");
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("用户名："), 0, 0);
        gridPane.add(new Label("密码："), 0, 1);
        gridPane.add(new Label("确认密码:"), 0, 2);
        TextField name = new TextField();
        name.setPromptText("6-15位数字或字母");
        PasswordField password=new PasswordField();
        password.setPromptText("大于等于6位字符");
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
                Dialog<ButtonType> alert= new Dialog<>();
                alert.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("style/dialog.css").toExternalForm());
                alert.getDialogPane().getStyleClass().add("success");
                alert.setTitle("注册");
                alert.setHeaderText("注册成功!");
                alert.setContentText("用户" + username + ": 您已注册成功！");
                alert.showAndWait();
                dialog.close();
                add(user);
            }else{
                Dialog<ButtonType> alert= new Dialog<>();
                alert.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("style/dialog.css").toExternalForm());
                alert.getDialogPane().getStyleClass().add("error");
                alert.setTitle("注册");
                alert.setHeaderText("注册失败!");
                alert.setContentText("用户名或密码不合法！");
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
        String regex = "^[a-zA-Z0-9]{6,15}$";
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
        return password.length() >= 6;
    }
    /***
     * @Description 游客模式进入
     * @author Bu Xinran
     * @date 2023/12/9 14:10
    **/
    public void justVisitor() throws IOException {
        userName=String.valueOf(System.currentTimeMillis());
        passWord=" ";
        state=false;
        User user=new User(userName,passWord);
        add(user);
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();
        Stage stage=new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 730);
        MainController.mainController=fxmlLoader.getController();
        stage.setTitle("喵喵滴计算器");
        stage.setResizable(false);  // ui1
        Image cursorImage = new Image("cur.png");
        ImageCursor cursor = new ImageCursor(cursorImage);
        stage.setResizable(false);
        stage.setScene(scene);
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
        scene.setCursor(cursor);
        stage.show();
        stage.setOnCloseRequest(event ->{
            if(!state)delete(new User(userName,passWord));
        });
    }
    /***
     * @Description 登录回车直接判断是否可以登录
     * @param event 是否敲击键盘enter
     * @author Bu Xinran
     * @date 2023/12/9 14:41
    **/
    @FXML
    private void log(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            if (event.getTarget() == usernameArea)
                passwordArea.requestFocus();
            else {
                login();
            }
        }
    }
}
