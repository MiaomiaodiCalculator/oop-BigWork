package user;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import static javafx.application.Application.launch;

/**
 * @author Bu Xinran
 * @Description:
 * @date 2023/12/17 10:11
 */
public class Shift {
    public ImageView view;
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Test.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 500, 730);
        String latex = "\\sqrt{{a^{2}+b^{2}}";
        BufferedImage image = (BufferedImage) image(latex);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageInByte = baos.toByteArray();
        javafx.scene.image.Image fxImage = new javafx.scene.image.Image(new ByteArrayInputStream(imageInByte));
        //view.setImage(fxImage);
        Pane rootPane = new Pane();
        //rootPane.getChildren().add(view);
        scene.setRoot(rootPane);
        stage.setTitle("登录");
        stage.setScene(scene);
        stage.show();
    }
    public static java.awt.Image image(String latex) throws Exception {
        int style = TeXConstants.STYLE_DISPLAY; // 样式 符号以最大的尺寸呈现
        float size = 24; // 生成公式图片的字体大小
        Color fg = Color.BLACK; // 字体颜色，黑色
        Color bg = null; // 图片背景色，默认为透明北京
        return TeXFormula.createBufferedImage(latex, style, size, fg, bg);
    }

    public static void main(String[] args) throws Exception {
        launch();
    }

    public void click(MouseEvent mouseEvent) throws Exception {
        String latex = "\\sqrt{{\\mathrm{a}}^{2}+{\\mathrm{b}}^{2}}";
        BufferedImage image = (BufferedImage)image(latex);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageInByte = baos.toByteArray();
        System.out.println(Arrays.toString(imageInByte));
        javafx.scene.image.Image fxImage = new javafx.scene.image.Image(new ByteArrayInputStream(imageInByte));

    }
}
