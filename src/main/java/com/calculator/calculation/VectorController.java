package com.calculator.calculation;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.canvas.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import vector.VectorSolve;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.PerspectiveCamera;
import javafx.scene.shape.Cylinder;

import static user.Shift.VectorShift;

/**
 * @author Bu Xinran
 * @Description 处理向量计算器的点击事件
 * @date 2023/11/23 17:26
 */
public class VectorController implements Initializable{
    protected static LinkedHashMap<Integer, VectorSolve> historyVector = new LinkedHashMap<>();
    public static int cntHistory = 0;
    public ImageView historyImg;
    public TextField inputX1;
    public TextField inputY1;
    public TextField inputX2;
    public TextField inputY2;
    public TextField inputZ1;
    public TextField inputZ2;
    public ImageView angle2D;
    public ImageView cross2D;
    public ImageView dot2D;
    public ImageView add2D;
    public Label error;
    public Canvas vectorCanvas;
    public ImageView add3D;
    public ImageView dot3D;
    public ImageView cross3D;
    public ImageView angle3D;
    public SubScene subScene;
    public Button Button2D;
    public Button Button3D;
    // 用于二维向量绘制
    public XYChart.Series <Number, Number> dataSeries1, dataSeries2;
    public LineChart LineChart2D;
    @FXML
    private StackPane cardContainer;
    private GraphicsContext gc=null;
    public static boolean flag=false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!flag) {
            flag = true;
            Vector2DShift();
        }
    }
    /***
     * @Description  加载二维向量对应的文件
     * @author Bu Xinran
     * @date 2023/11/25 22:52
     **/
    public void Vector2DShift() {
        loadPage("Vector2D.fxml");
        Button2D.getStyleClass().add("active");
        Button3D.getStyleClass().remove("active");
    }
    /***
     * @Description  加载三维向量对应的文件
     * @author Bu Xinran
     * @date 2023/11/25 22:52
     **/
    public void Vector3DShift() {
        loadPage("Vector3D.fxml");
        Button3D.getStyleClass().add("active");
        Button2D.getStyleClass().remove("active");
    }
    /***
     * @Description  加载卡片布局：fxml文件
     * @param fxmlFileName 要打开的fxml文件名称
     * @author Bu Xinran
     * @date 2023/11/25 22:51
     **/

    private void loadPage(String fxmlFileName) {
        try {
            Pane page = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFileName)));
            cardContainer.getChildren().setAll(page);
        } catch (Exception e) {
            System.out.println("error");
        }
    }
    /***
     * @Description  处理二维向量的计算
     * @param newVector 二维向量
     * @author Bu Xinran
     * @date 2023/11/25 20:49
     **/
    public void setNewVector2(ActionEvent newVector) throws Exception {
        error.setVisible(false);
        if(gc!=null){
            gc.clearRect(0, 0, vectorCanvas.getWidth(), vectorCanvas.getHeight()); // 清空画布内容
        }
        String x1 = inputX1.getText();
        String y1 = inputY1.getText();
        String x2 = inputX2.getText();
        String y2 = inputY2.getText();
        VectorSolve vector = new VectorSolve(x1, y1, null, x2, y2, null, 2);
        if (vector.getError()) {
            double X1 = Double.parseDouble(x1);
            double Y1 = Double.parseDouble(y1);
            double X2 = Double.parseDouble(x2);
            double Y2 = Double.parseDouble(y2);
            double[] add = vector.addVector();
            double dot = vector.dotVector();
            double cross = vector.crossVector();
            double angle = vector.angleVector();
            add2D.setVisible(true);
            dot2D.setVisible(true);
            cross2D.setVisible(true);
            angle2D.setVisible(true);
            String addResult="=(" + add[0] + "," + add[1] + ")";
            Image i1=VectorShift(addResult,1,2);
            add2D.setImage(i1);
            String dotResult="=" + dot;
            Image i2=VectorShift(dotResult,2,2);
            dot2D.setImage(i2);
            String crossResult="=" + cross;
            Image i3=VectorShift(crossResult,3,2);
            cross2D.setImage(i3);
            String angleResult="=" + angle;
            Image i4=VectorShift(angleResult,4,2);
            angle2D.setImage(i4);
            drawCoordinateSystem(X1, Y1, X2, Y2);
        } else {
            add2D.setVisible(false);
            dot2D.setVisible(false);
            cross2D.setVisible(false);
            angle2D.setVisible(false);
            error.setVisible(true);
        }
        cntHistory++;
        historyVector.put(cntHistory, vector);
        pushMapToHistory();
    }
    /**
     * @Description 处理二维向量的绘制
     * @param X1
     * @param Y1
     * @param X2
     * @param Y2
     * @author 郑悦
     * @date 2023/12/17 11:14
    **/
    private void drawCoordinateSystem(double X1, double Y1, double X2, double Y2) {
        LineChart2D.getData().remove(dataSeries1);
        LineChart2D.getData().remove(dataSeries2);
        dataSeries1 = new XYChart.Series<>();
        dataSeries2 = new XYChart.Series<>();
        dataSeries1.getData().add(new XYChart.Data<>(0, 0));
        dataSeries1.getData().add(new XYChart.Data<>(X1, Y1));
        dataSeries2.getData().add(new XYChart.Data<>(0, 0));
        dataSeries2.getData().add(new XYChart.Data<>(X2, Y2));
        dataSeries1.setName("Vector 1");
        dataSeries2.setName("Vector 2");
        LineChart2D.getData().addAll(dataSeries1, dataSeries2);
        LineChart2D.getStyleClass().add(getClass().getResource("css/Vector2DDraw.css").toExternalForm());
        dataSeries1.getData().get(0).getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: transparent;");
        dataSeries2.getData().get(0).getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: transparent;");
    }
    /***
     * @Description 根据直线方向绘制箭头
     * @param gc GraphicsContext
     * @param startX 起始x轴坐标
     * @param startY 起始y轴坐标
     * @param endX 终止x轴坐标
     * @param endY  终止y轴坐标
     * @author Bu Xinran
     * @date 2023/11/25 20:46
     **/
    private void drawArrow(GraphicsContext gc, double startX, double startY, double endX, double endY) {
        double arrowSize = 10;
        double angle = Math.atan2(endY - startY, endX - startX);
        double lineStartX = endX - arrowSize * Math.cos(angle + Math.toRadians(45));
        double lineStartY = endY - arrowSize * Math.sin(angle + Math.toRadians(45));
        double lineEndX = endX - arrowSize * Math.cos(angle - Math.toRadians(45));
        double lineEndY = endY - arrowSize * Math.sin(angle - Math.toRadians(45));
        gc.strokeLine(startX, startY, endX, endY);
        gc.strokeLine(endX, endY, lineStartX, lineStartY);
        gc.strokeLine(endX, endY, lineEndX, lineEndY);
    }
    /***
     * @Description  将向量计算器的计算结果存入Map，然后序列化储存在文件中
     * @author Bu Xinran
     * @date 2023/11/25 20:45
     **/
    private void pushMapToHistory(){
        try{
            String path="./data/Vector.out";
            File file=new File(path);
            if(!file.exists()){
                file.getParentFile().mkdirs();
            }else{
                file.delete();
                file.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(historyVector);
            oos.flush();
            oos.close();
            fos.flush();
            fos.close();
        }catch(SecurityException e){
            System.out.println("File operation failed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /***
     * @Description  处理输入的三维向量
     * @param newVector3 新建三维向量
     * @author Bu Xinran
     * @date 2023/11/25 22:49
     **/
    public void setNewVector3(ActionEvent newVector3) throws Exception {
        if(gc!=null){
            gc.clearRect(0, 0, vectorCanvas.getWidth(), vectorCanvas.getHeight()); // 清空画布内容
        }
        String x1 = inputX1.getText();
        String y1 = inputY1.getText();
        String z1=inputZ1.getText();
        String x2 = inputX2.getText();
        String y2 = inputY2.getText();
        String z2=inputZ2.getText();
        VectorSolve vector = new VectorSolve(x1, y1, z1, x2, y2, z2, 3);
        if (vector.getError()) {
            double X1 = Double.parseDouble(x1);
            double Y1 = Double.parseDouble(y1);
            double Z1 = Double.parseDouble(z1);
            double X2 = Double.parseDouble(x2);
            double Y2 = Double.parseDouble(y2);
            double Z2 = Double.parseDouble(z2);
            double[] add = vector.addVector();
            double dot = vector.dotVector();
            double cross = vector.crossVector();
            double angle = vector.angleVector();
            add3D.setVisible(true);
            dot3D.setVisible(true);
            cross3D.setVisible(true);
            angle3D.setVisible(true);
            String addResult="=(" + add[0] + "," + add[1] + ","+add[2]+")";
            Image i1=VectorShift(addResult,1,3);
            add3D.setImage(i1);
            String dotResult="=" + dot;
            Image i2=VectorShift(dotResult,2,3);
            dot3D.setImage(i2);
            String crossResult="=" + cross;
            Image i3=VectorShift(crossResult,3,3);
            cross3D.setImage(i3);
            String angleResult="=" + angle;
            Image i4=VectorShift(angleResult,4,3);
            angle3D.setImage(i4);
        } else {
            add3D.setVisible(false);
            dot3D.setVisible(false);
            cross3D.setVisible(false);
            angle3D.setVisible(false);
            error.setVisible(true);
        }
        cntHistory++;
        historyVector.put(cntHistory, vector);
        pushMapToHistory();
    }
}
