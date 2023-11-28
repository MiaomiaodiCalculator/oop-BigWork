package com.calculator.calculation;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
            drawCoordinateSystem(new double[]{X1, Y1, X2, Y2});
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
    /***
     * @Description 绘制向量的坐标图
     * @param coordinates 坐标数组
     * @author Bu Xinran
     * @date 2023/11/25 20:47
     **/
    private void drawCoordinateSystem(double[] coordinates) {
        gc = vectorCanvas.getGraphicsContext2D();
        double width = vectorCanvas.getWidth();
        double height = vectorCanvas.getHeight();
        //获得x，y的最大值以确定最小分度值
        double xMax = Math.max(Math.abs(coordinates[0]), Math.abs(coordinates[2]));
        double yMax = Math.max(Math.abs(coordinates[1]), Math.abs(coordinates[3]));
        double max = Math.max(xMax, yMax);
        int minScale = max > 10 ? (int)(max / 10) : 1;
        gc.setStroke(Color.BLACK);
        //绘制x，y坐标值
        gc.setLineWidth(1);
        gc.strokeLine(0, height / 2, width, height / 2);
        gc.setLineWidth(1);
        gc.strokeLine(width / 2, 0, width / 2, height);
        //绘制箭头
        drawArrow(gc, 0, height / 2, width, height / 2);
        drawArrow(gc,width / 2, height,(width / 2), 0);
        // 绘制刻度
        int j=-9;
        for (double i =width/20; i <= width; i += width/20,j++) {
            gc.setLineWidth(0.3);
            gc.strokeLine( i, height / 2-5,i, height / 2);
            gc.setFill(Color.BLACK);
            gc.fillText(String.format("%d", j*minScale), i - 5, height / 2 + 15);
        }
        j=9;
        for (double i =height/20; i <height; i += height/20,j--) {
            gc.setLineWidth(0.3);
            gc.strokeLine(width / 2 - 5, i, width / 2, i);
            if(j%2==0&&j!=0){
                gc.setFill(Color.BLACK);
                gc.fillText(String.format("%d", j*minScale), width / 2-20, i + 5);
            }
        }
        //绘制向量
        gc.setLineWidth(1.5);
        gc.setStroke(Color.RED);
        gc.strokeLine(width/2,height/2,width/2+coordinates[0]/minScale*(width/20), height/2-coordinates[1]/minScale*(height/20));
        gc.setStroke(Color.BLUE);
        gc.strokeLine(width/2,height/2,width/2+coordinates[2]/minScale*(width/20), height/2-coordinates[3]/minScale*(height/20));
        gc.setLineWidth(1);
        gc.setStroke(Color.RED);
        drawArrow(gc,width/2,height/2,width/2+coordinates[0]/minScale*(width/20), height/2-coordinates[1]/minScale*(height/20));
        gc.setStroke(Color.BLUE);
        drawArrow(gc,width/2,height/2,width/2+coordinates[2]/minScale*(width/20), height/2-coordinates[3]/minScale*(height/20));
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
