package com.calculator.calculation;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author sxq
 * @Description 主程序入口
 * @date 2023/11/26 13:52
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 加载光标图像
        Image cursorImage = new Image("cur-L.png");
        // 创建光标
        ImageCursor cursor = new ImageCursor(cursorImage);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 370, 369);
        stage.setTitle("登录");
        // 创建一个图标对象
        Image icon = new Image("icon.png");
        // 将图标添加到窗口的图标列表中
        stage.getIcons().add(icon);
        stage.setResizable(false);  // ui1
        stage.setScene(scene);
        // 应用光标到场景
        scene.setCursor(cursor);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
    /**
     * @Description 使用“#”将字符串数组合成长字符串 
     * @param inputList 需要序列化的数组
     * @return java.lang.String
     * @author sxq
     * @date 2023/12/8 11:07
    **/
    public static String serializeStringList(List<String> inputList) {
        StringJoiner joiner = new StringJoiner("#");
        for (String str : inputList) {
            joiner.add(str);
        }
        return joiner.toString();
    }
    /**
     * @Description 将数据库中的字符串反序列化
 * @param serializedString 需要反序列化的数组
 * @return java.util.List<java.lang.String>       
     * @author sxq
     * @date 2023/12/8 11:09
    **/
    public static List<String> deserializeStringList(String serializedString) {
        String[] splitArray = serializedString.split("#");
        return Arrays.asList(splitArray);
    }

}