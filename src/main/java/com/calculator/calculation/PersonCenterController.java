package com.calculator.calculation;

import Database.SqlUser;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import static Database.SqlUser.*;
import static com.calculator.calculation.LoginController.checkPassword;
/**
 * @author Bu Xinran
 * @Description 个人中心控制
 * @date 2023/12/7 23:21
 */
public class PersonCenterController implements Initializable {
    public Label usernameShow;
    public ImageView avatarImageView;
    public Hyperlink logoutButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameShow.setText(LoginController.userName);
        Image image=SqlUser.loadAvatar(LoginController.userName);
        if(image!=null){
            avatarImageView.setImage(image);
            double targetSize = 128.0;
            double scaleFactor = Math.min(targetSize / image.getWidth(), targetSize / image.getHeight());
            double scaledWidth = image.getWidth() * scaleFactor;
            double scaledHeight = image.getHeight() * scaleFactor;
            avatarImageView.setFitWidth(scaledWidth);
            avatarImageView.setFitHeight(scaledHeight);
        }
    }
    /***
     * @Description  注销用户
     * @author Bu Xinran
     * @date 2023/12/7 23:59
    **/
    public void handleLogout() {
        User user=new User(LoginController.userName,LoginController.passWord);
        delete(user);
    }
    /***
     * @Description  修改密码
     * @author Bu Xinran
     * @date 2023/12/7 23:59
    **/
    public void changePassword() {
        String title="修改密码";
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("修改密码");
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("旧密码："), 0, 0);
        gridPane.add(new Label("新密码："), 0, 1);
        gridPane.add(new Label("确认新密码:"), 0, 2);
        PasswordField oldPassword = new PasswordField();
        oldPassword.setPromptText("请输入旧密码");
        PasswordField newPassword=new PasswordField();
        newPassword.setPromptText("7-15位由英文字母、数字和特殊字符（@，_，%，$）组成");
        PasswordField again=new PasswordField();
        again.setPromptText("再次确认密码");
        oldPassword.setPrefWidth(300);
        newPassword.setPrefWidth(300);
        again.setPrefWidth(300);
        oldPassword.setPrefHeight(40);
        newPassword.setPrefHeight(40);
        again.setPrefHeight(40);
        gridPane.add(oldPassword, 1, 0);
        gridPane.add(newPassword, 1, 1);
        gridPane.add(again, 1, 2);
        gridPane.setPrefWidth(400);
        gridPane.setPrefHeight(150);
        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            String old=oldPassword.getText();
            String newWord=newPassword.getText();
            String rewrite=again.getText();
            if(!Objects.equals(old, "")&&getByName(LoginController.userName).getPassword().equals(old)&&checkPassword(newWord)&&newWord.equals(rewrite)){
                SqlUser.modify(LoginController.userName,newWord);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("修改密码成功");
                alert.setHeaderText("用户"+LoginController.userName+":您已修改密码成功！");
                alert.showAndWait();
                dialog.close();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText("旧密码输入错误或新密码不合法！");
                alert.showAndWait();
                dialog.close();
            }
        }
    }
    /***
     * @Description 修改头像
     * @author Bu Xinran
     * @date 2023/12/7 23:59
    **/
    public void changeAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                //获取文件输入流
                FileInputStream fis = new FileInputStream(selectedFile);
                uploadAvatar(fis,LoginController.userName);
                fis.close();
                usernameShow.setText(LoginController.userName);
                Image image=SqlUser.loadAvatar(LoginController.userName);
                if(image!=null){
                    avatarImageView.setImage(image);
                    double targetSize = 128.0;
                    double scaleFactor = Math.min(targetSize / image.getWidth(), targetSize / image.getHeight());
                    double scaledWidth = image.getWidth() * scaleFactor;
                    double scaledHeight = image.getHeight() * scaleFactor;
                    avatarImageView.setFitWidth(scaledWidth);
                    avatarImageView.setFitHeight(scaledHeight);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /***
     * @Description 查看历史记录
     * @author Bu Xinran
     * @date 2023/12/8 9:53
    **/
    public void checkHistory() {
    }
    /***
     * @Description 用户登出
     * @author Bu Xinran
     * @date 2023/12/8 13:01
    **/
    public void logout() throws IOException {
        Stage currentStage = (Stage) logoutButton.getScene().getWindow();
        currentStage.close();
        Stage stage=new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 370, 369);
        stage.setTitle("登录");
        stage.setScene(scene);
        stage.show();
    }
}
