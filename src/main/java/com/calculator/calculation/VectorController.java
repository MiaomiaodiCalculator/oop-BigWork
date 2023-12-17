package com.calculator.calculation;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.canvas.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import vector.VectorSolve;
import java.io.*;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.PerspectiveCamera;
import javafx.scene.shape.Cylinder;

/**
 * @author Bu Xinran
 * @Description 处理向量计算器的点击事件
 * @date 2023/11/23 17:26
 */
public class VectorController{
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
    public Label addResult;
    public Label dotResult;
    public Label crossResult;
    public Label angleResult;
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
    /***
     * @Description  加载二维向量对应的文件
     * @param actionEvent 无意义
     * @author Bu Xinran
     * @date 2023/11/25 22:52
     **/
    public void Vector2DShift(ActionEvent actionEvent) {
        loadPage("Vector2D.fxml");
    }
    /***
     * @Description  加载三维向量对应的文件
     * @param actionEvent 无意义
     * @author Bu Xinran
     * @date 2023/11/25 22:52
     **/
    public void Vector3DShift(ActionEvent actionEvent) {
        loadPage("Vector3D.fxml");
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
    public void setNewVector2(ActionEvent newVector) {
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
            addResult.setText("=(" + add[0] + "," + add[1] + ")");
            dotResult.setText("=" + dot);
            crossResult.setText("=" + cross);
            angleResult.setText("=" + angle);
            drawCoordinateSystem(X1, Y1, X2, Y2);
        } else {
            add2D.setVisible(false);
            dot2D.setVisible(false);
            cross2D.setVisible(false);
            angle2D.setVisible(false);
            addResult.setText("");
            dotResult.setText("");
            crossResult.setText("");
            angleResult.setText("");
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
        double K1 = Y1/X1;
        double K2 = Y2/X2;
        double theta1 = Math.atan(K1);
        double theta2 = Math.atan(K2);
        double angle1 = Math.toDegrees(theta1);
        double angle2 = Math.toDegrees(theta2);
        LineChart2D.getStyleClass().add(getClass().getResource("css/Vector2DDraw.css").toExternalForm());
        dataSeries1.getData().get(0).getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: transparent;");
        dataSeries2.getData().get(0).getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: transparent;");
        // TODO:是否更换箭头形状
        dataSeries1.getData().get(1).getNode().lookup(".chart-line-symbol").setStyle("-fx-shape: \"M0,1 L0,2 L1,2 L1,3 L2,3 L2,2 L3,2 L3,1 L2,1 L2,0 L1,0 L1,2 z\";-fx-rotate: 45;-fx-scale-x: 1.5; -fx-scale-y: 1.5;");
        dataSeries2.getData().get(1).getNode().lookup(".chart-line-symbol").setStyle("-fx-shape: \"M 0 0 L 1 0 L 0.5 1 Z\";-fx-rotate: 45;-fx-scale-x: 1.5; -fx-scale-y: 1.5;");
//        ;-fx-rotate: angle1
        dataSeries1.getData().get(1).getNode().setRotate(270 - angle1);
        dataSeries2.getData().get(1).getNode().setRotate(270 - angle2);
//        dataSeries1.getNode().getStyleClass().add("data1-line");
//        dataSeries2.getNode().getStyleClass().add("data2-line");
//        dataSeries1.getNode().getStyleClass().add("chart-line-symbol");
//        dataSeries1.getData().get(1).getNode().getStyleClass().add("special-data-point");
//        dataSeries2.getData().get(1).getNode().getStyleClass().add("special-data-point");
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
    public void setNewVector3(ActionEvent newVector3) {
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
            addResult.setText("=(" + add[0] + "," + add[1] + ","+add[2]+")");
            dotResult.setText("=" + dot);
            crossResult.setText("=" + cross);
            angleResult.setText("=" + angle);
            Group axisGroup = new Group();
            PerspectiveCamera camera = new PerspectiveCamera(true);
            camera.setTranslateX(0); // 将相机位置设置为中心
            camera.setTranslateY(0);
            camera.setTranslateZ(-5000); // 将相机向后移动 5000 个距离单位，增加视野范围
            subScene.setCamera(camera);
            // 计算新的 subScene 尺寸
            double subSceneWidth = 500.0;
            double subSceneHeight = 240.0;
            // 创建 x 轴
            Cylinder xAxis = new Cylinder(2, subSceneWidth * 2);
            xAxis.setTranslateX(subSceneWidth / 2);
            axisGroup.getChildren().add(xAxis);
            // 创建 y 轴
            Cylinder yAxis = new Cylinder(2, subSceneHeight * 2);
            yAxis.setTranslateY(subSceneHeight / 2);
            yAxis.setRotate(90);
            axisGroup.getChildren().add(yAxis);
            // 创建 z 轴
            Cylinder zAxis = new Cylinder(2, subSceneWidth * 2);
            zAxis.setTranslateZ(-subSceneHeight * 2); // 将 z 轴位置向后移动到场景外
            zAxis.setRotate(90);
            axisGroup.getChildren().add(zAxis);
            // 将坐标轴添加到 3D 场景中
            subScene.setRoot(axisGroup);
            subScene.setVisible(true);

        } else {
            add3D.setVisible(false);
            dot3D.setVisible(false);
            cross3D.setVisible(false);
            angle3D.setVisible(false);
            addResult.setText("");
            dotResult.setText("");
            crossResult.setText("");
            angleResult.setText("");
            error.setVisible(true);
        }
        cntHistory++;
        historyVector.put(cntHistory, vector);
        pushMapToHistory();
    }
}
