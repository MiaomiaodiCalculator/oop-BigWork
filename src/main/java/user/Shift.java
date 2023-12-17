package user;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.nfunk.jep.JEP;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static javafx.application.Application.launch;

/**
 * @author Bu Xinran
 * @Description:
 * @date 2023/12/17 10:11
 */
public class Shift {
    public static Image image(String latex) throws Exception {
        int style = TeXConstants.STYLE_DISPLAY; // 样式 符号以最大的尺寸呈现
        float size = 24; // 生成公式图片的字体大小
        Color fg = Color.BLACK; // 字体颜色，黑色
        Color bg = null; // 图片背景色，默认为透明北京
        return TeXFormula.createBufferedImage(latex, style, size, fg, bg);
    }
    /***
     * @Description  展示泊松分布latex表达式
     * @param a 泊松分布参数
     * @param b 泊松分布参数
     * @return javafx.scene.image.Image
     * @author Bu Xinran
     * @date 2023/12/17 13:51
    **/
    public static javafx.scene.image.Image PDFExpressShift(double a,double b) throws Exception {
        String latex="y =\\cfrac{x!}{(e^(-$a$))*($b$^x)}";
        latex = latex.replace("$a$", String.valueOf(a));
        latex=latex.replace("$b$",String.valueOf(b));
        BufferedImage image = (BufferedImage) image(latex);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteOutput);
        byte[] imageInByte = byteOutput.toByteArray();
        javafx.scene.image.Image fxImage = new javafx.scene.image.Image(new ByteArrayInputStream(imageInByte));
        byteOutput.close();
        return fxImage;
    }
    /***
     * @Description  展示泊松分布latex表达式
     * @param a 高斯分布参数
     * @param b 高斯分布参数
     * @param c 高斯分布参数
     * @return javafx.scene.image.Image
     * @author Bu Xinran
     * @date 2023/12/17 14:16
    **/
    public static javafx.scene.image.Image GaussShift(double a,double b,double c) throws Exception {
        String latex="y =\\cfrac{\\sqrt{2*π*$a$^{2}}}{e^{-((x-$b$)^{2}/2*$c$^{2}}}";
        latex = latex.replace("$a$", String.valueOf(a));
        latex=latex.replace("$b$",String.valueOf(b));
        latex=latex.replace("$c$",String.valueOf(c));
        BufferedImage image = (BufferedImage) image(latex);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteOutput);
        byte[] imageInByte = byteOutput.toByteArray();
        javafx.scene.image.Image fxImage = new javafx.scene.image.Image(new ByteArrayInputStream(imageInByte));
        byteOutput.close();
        return fxImage;
    }
    /***
     * @Description  将回归表达式转化为latex图片
     * @param equation 回归表达式
     * @return javafx.scene.image.Image
     * @author Bu Xinran
     * @date 2023/12/17 14:30
    **/
    public static javafx.scene.image.Image regressionShift(String equation) throws Exception {
        String latex=shiftLatex(equation);
        BufferedImage image = (BufferedImage) image(latex);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteOutput);
        byte[] imageInByte = byteOutput.toByteArray();
        javafx.scene.image.Image fxImage = new javafx.scene.image.Image(new ByteArrayInputStream(imageInByte));
        byteOutput.close();
        return fxImage;
    }
    /***
     * @Description 将表达式转化为latex
     * @param equation 表达式
     * @return java.lang.String
     * @author Bu Xinran
     * @date 2023/12/17 14:30
    **/
    private static String shiftLatex(String equation) {
        if(equation.contains("^")){
            equation=equation.replace("(","{");
            equation=equation.replace(")","}");
            return equation;
        }else return equation;
    }
    /***
     * @Description  返回向量latex图片
     * @param equation 答案
     * @param x 运算序号
     * @param z 几维
     * @return javafx.scene.image.Image
     * @author Bu Xinran
     * @date 2023/12/17 17:26
    **/
    public static javafx.scene.image.Image VectorShift(String equation,int x,int z) throws Exception {
        String latex="";
        if(x==1){
            if(z==2){
                latex="\\vec{a}+\\vec{b}"+equation;
            }else{
                latex="\\vec{a}+\\vec{b}+\\vec{c}"+equation;
            }
        }else if(x==2){
            if(z==2){
                latex="\\vec{a}·\\vec{b}"+equation;
            }else{
                latex="\\vec{a}·\\vec{b}·\\vec{c}"+equation;
            }
        }else if(x==3){
            if(z==2){
                latex="\\vec{a}\\times\\vec{b}"+equation;
            }else{
                latex="\\vec{a}\\times\\vec{b}\\times\\vec{c}"+equation;
            }
        }else{
            if(z==2){
                latex="cos<\\vec{a},\\vec{b}>"+equation;
            }else{
                latex="cos<\\vec{a},\\vec{b},\\vec{c}>"+equation;
            }
        }
        BufferedImage image = (BufferedImage) image(latex);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteOutput);
        byte[] imageInByte = byteOutput.toByteArray();
        javafx.scene.image.Image fxImage = new javafx.scene.image.Image(new ByteArrayInputStream(imageInByte));
        byteOutput.close();
        return fxImage;
    }
}
