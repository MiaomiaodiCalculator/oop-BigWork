package com.calculator.calculation;

import Visualizing.AxisFormatter;
import Visualizing.Expression;
import Visualizing.Parser;
import Visualizing.UserData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.commons.math3.distribution.PoissonDistribution;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * @author ZhouYH
 * @Description 处理函数图像绘制的点击事件
 * @date 2023/11/29 16:35
 */
public class VisualizationController implements Initializable {
    public TextField f1, f2, f3, f4, f5;
    public TextField[] funcs;
    public Button show1, show2, show3, show4, show5;
    public Button[] shows;
    @FXML public Slider posYSlider, negYSlider, negXSlider, posXSlider;
    @FXML public LineChart<Double, Double> graphChart;
    @FXML public NumberAxis xAxis, yAxis;
    public Series<Double, Double> data1, data2, data3, data4, data5;
    public String[] dataShow; //保存到数据库时只需存储这个String数组即可
    public String[] Last; //避免重复保存
    public Parser parser;
    public Pane Visualization;
    public Pane History;
    public ImageView returnImg;
    public ImageView historyImg;
    public TableView<UserData> tableView;
    public TableColumn<UserData,String> historyList;
    private ArrayList<UserData> list =new ArrayList<>();


    /**
     * @Description 绘制函数图像（num处）或判断无法绘制返回ERROR
     * @param num 需要绘制的函数列表序号，为 1~5
     * @author ZhouYH
     * @date 2023/11/30 17:24
     **/
    public void Go(int num) {
        unshow(num);
        if (num == 1){
            data1 = new Series<>();
            graphChart.getData().add(data1);
            data1.getNode().setStyle("-fx-stroke: RED; -fx-stroke-width: 2px;");
        }else if (num == 2){
            data2 = new Series<>();
            graphChart.getData().add(data2);
            data2.getNode().setStyle("-fx-stroke: BLUE; -fx-stroke-width: 2px;");
        }else if (num == 3){
            data3 = new Series<>();
            graphChart.getData().add(data3);
            data3.getNode().setStyle("-fx-stroke: GREEN; -fx-stroke-width: 2px;");
        }else if (num == 4){
            data4 = new Series<>();
            graphChart.getData().add(data4);
            data4.getNode().setStyle("-fx-stroke: BROWN; -fx-stroke-width: 2px;");
        }else if (num == 5){
            data5 = new Series<>();
            graphChart.getData().add(data5);
            data5.getNode().setStyle("-fx-stroke: PURPLE; -fx-stroke-width: 2px;");
        }
        try {
            if(funcs[num].getText().matches("POISSON(.+)")){
                String getLambda=funcs[num].getText().substring(8, funcs[num].getText().indexOf(')'));
                double lambda=Double.parseDouble(getLambda);
                Poisson(num, lambda);
            }else {
                HashMap<String, Double> vars = new HashMap<>();
                Expression exp = parser.eval(funcs[num].getText(), vars);
                double incr;     //增量
                if (isTrigFunc(funcs[num].getText())) incr = Math.PI/18;
                else incr = 0.1;
                for (double i = -100; i <= 100; i+=incr) {
                    vars.put("x", i); //x=i
                    if (num == 1) data1.getData().add(new Data<>(i, exp.eval()));
                    if (num == 2) data2.getData().add(new Data<>(i, exp.eval()));
                    if (num == 3) data3.getData().add(new Data<>(i, exp.eval()));
                    if (num == 4) data4.getData().add(new Data<>(i, exp.eval()));
                    if (num == 5) data5.getData().add(new Data<>(i, exp.eval()));
                }
            }
            dataShow[num]=funcs[num].getText();
            shows[num].setText("√");
        }catch (Exception e) {
            funcs[num].setText("ERROR");
            dataShow[num]=null;
        }
    }

    /**
     * @Description 判断字符串是否表达三角函数
     * @param in 输入的字符串（即用户输入函数）
     * @return boolean
     * @author ZhouYH
     * @date 2023/12/1 16:53
     **/
    private boolean isTrigFunc(String in) {
        Pattern p = Pattern.compile("sin|cos|tan|sec|csc|cot");
        return p.matcher(in).find();
    }

    /**
     * @Description 泊松分布图像绘制
     * @param num 绘制的图像标号
     * @param lambda 参数 λ
     * @author ZhouYH
     * @date 2023/12/2 10:41
     **/
    public void Poisson(int num, double lambda){
        int incr = 1;
        double val;
        PoissonDistribution poissonDistribution = new PoissonDistribution(lambda);
        for (int i = -100; i <= 100; i+=incr) {
            val=poissonDistribution.probability(i);;  // 这行替换为泊松分布取表
            if (num == 1) data1.getData().add(new Data<>((double)i, val));
            if (num == 2) data2.getData().add(new Data<>((double)i, val));
            if (num == 3) data3.getData().add(new Data<>((double)i, val));
            if (num == 4) data4.getData().add(new Data<>((double)i, val));
            if (num == 5) data5.getData().add(new Data<>((double)i, val));
        }
    }

    /**
     * @Description 隐藏函数图像
     * @param num 需要隐藏的函数序号
     * @author ZhouYH
     * @date 2023/11/30 17:25
     **/
    public void unshow(int num){
        if (num == 1) graphChart.getData().remove(data1);
        if (num == 2) graphChart.getData().remove(data2);
        if (num == 3) graphChart.getData().remove(data3);
        if (num == 4) graphChart.getData().remove(data4);
        if (num == 5) graphChart.getData().remove(data5);
        dataShow[num]=null;
        shows[num].setText("");
    }

    /**
     * @Description 显示历史记录页面
     * @param mouseEvent 无意义
     * @author ZhouYH
     * @date 2023/12/9 11:09
     **/
    public void handleReturnClick(MouseEvent mouseEvent) {
        Visualization.setVisible(true);
        History.setVisible(false);
    }

    /**
     * @Description 将所选历史记录页面显示在函数图像上
     * @param event 记录选中的历史记录项
     * @author ZhouYH
     * @date 2023/12/9 11:09
     **/
    public void handleRowClick(MouseEvent event) throws RuntimeException {
        // 判断是否双击行
        if (event.getClickCount() == 2) {
            String[] selectedItem = tableView.getSelectionModel().getSelectedItem().getItem();
            if(selectedItem!=null) {
                initData();
                for (int i = 1; i <= 5; i++){
                    unshow(i);
                    if (selectedItem[i] != null) {
                        funcs[i].setText(selectedItem[i]);
                        Go(i);
                    }
                }
            }
        }
    }

    /**
     * @Description 从文件中提取存储历史记录的list
     * @author ZhouYH
     * @date 2023/12/9 11:10
     **/
    private void getListFromFile() throws IOException, ClassNotFoundException {
        String path="./data/Visualization.out";
        File file=new File(path);
        if(file.exists()){
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list =(ArrayList<UserData>)ois.readObject();
            ois.close();
            fis.close();
        }
    }

    /**
     * @Description 跳转到历史记录页面
     * @param event 无意义
     * @author ZhouYH
     * @date 2023/12/9 11:10
     **/
    public void handleHisImageClick(MouseEvent event) {
        try {
            getListFromFile();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        tableView.setPlaceholder(new Label("无历史记录"));//占位文本
        historyList.setCellValueFactory(cellData -> cellData.getValue().dataNameProperty());
        tableView.getItems().setAll(list);
        Visualization.setVisible(false);
        History.setVisible(true);
    }

    /**
     * @Description 序列化存储过程
     * @author ZhouYH
     * @date 2023/12/9 11:17
     **/
    public void pushListToHistory(ActionEvent actionEvent){
        try{
            String path="./data/Visualization.out";
            File file=new File(path);
            if(!file.exists()){
                file.getParentFile().mkdirs();
            }else{
                file.delete();
                file.getParentFile().mkdirs();
            }
            boolean exists=true;
            for(int i=1;i<=5;i++)
                if((Last[i]==null&&dataShow[i]!=null)||(Last[i]!=null&&dataShow[i]==null)){
                    exists=false;
                    break;
                }
                else if(Last[i]!=null&&dataShow[i]!=null)
                    if (!Last[i].equals(dataShow[i])){
                        exists=false;
                        break;
                    }
            if(!exists) {
                Last = new String[6];
                for(int i=1;i<=5;i++){
                    if(dataShow[i]!=null) Last[i]=dataShow[i].intern();
                    else Last[i]=null;
                }
                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                UserData userData=new UserData();
                userData.setName(ft.format(dNow));
                userData.setItem(Last);
                list.add(userData);
            }
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list); //这行写不进去，会报错
            oos.flush();
            oos.close();
            fos.flush();
            fos.close();
            tableView.getItems().setAll(list);
        }catch(Exception e){
            System.out.println("File operation failed");
        }
    }

    /**
     * @Description 处理截图事件
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/12/9 12:05
     **/
    public void PrintScreen(ActionEvent actionEvent) {
        try {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd_hh-mm-ss");
            String path="./data/screenshots/"+ft.format(dNow)+".png";
            File file=new File(path);
            if(!file.exists()){
                file.getParentFile().mkdirs();
            }else{
                file.delete();
                file.getParentFile().mkdirs();
            }
            WritableImage image = new WritableImage(500, 720);
            Visualization.getScene().snapshot(image);
            int width = 500;
            int height = 720;
            BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            int[] buffer = new int[width];
            PixelReader reader = image.getPixelReader();
            WritablePixelFormat<IntBuffer> format = PixelFormat.getIntArgbInstance();
            for (int y = 0; y < height; y++) {
                reader.getPixels(0, y, width, 1, format, buffer, 0, width);
                bimage.getRaster().setDataElements(0, y, width, 1, buffer);
            }
            ImageIO.write(bimage, "png", file);
        } catch (IOException e) {
            System.out.println("Screenshot operation failed");
        }
    }

    /**
     * @Description 初始化具体的数据存储
     * @author ZhouYH
     * @date 2023/12/9 18:30
     **/
    public void initData(){
        funcs = new TextField[]{null, f1, f2, f3, f4, f5};
        shows = new Button[]{null, show1, show2, show3, show4, show5};
        dataShow = new String[]{null,null,null,null,null,null};
        graphChart.getData().removeAll();
        Visualization.setVisible(true);
        History.setVisible(false);
        for(int i=1;i<=5;i++){
            funcs[i].setText("");
            shows[i].setText("");
        }
    }

    /**
     * @Description 初始化界面
     * @author ZhouYH
     * @date 2023/11/29 22:37
     **/
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize the actual graph/chart
        xAxis.lowerBoundProperty().bind(negXSlider.valueProperty());
        xAxis.upperBoundProperty().bind(posXSlider.valueProperty());
        yAxis.lowerBoundProperty().bind(negYSlider.valueProperty());
        yAxis.upperBoundProperty().bind(posYSlider.valueProperty());
        // set the label formatter for our axis to only show ints
        xAxis.setTickLabelFormatter(new AxisFormatter());
        yAxis.setTickLabelFormatter(new AxisFormatter());
        parser = new Parser();
        Last = new String[]{null,null,null,null,null,null};
        initData();
        list=new ArrayList<>();
        for(int i=1;i<=5;i++){
            int finalI = i;
            funcs[i].setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override //键入ENTER时尝试绘制
                public void handle(KeyEvent event) {
                    if(event.getCode() == KeyCode.ENTER)
                        Go(finalI);
                }
            });
            funcs[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override //隐藏错误信息并开始编辑函数
                public void handle(MouseEvent mouseEvent) {
                    if (funcs[finalI].getText().equals("ERROR") || funcs[finalI].getText().equals("NO FUNCTION ENTERED"))
                        funcs[finalI].setText("");
                }
            });
            shows[i].setOnMouseClicked((new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(shows[finalI].getText().isEmpty()){ //点击空的show键时，尝试绘制当前文本框的函数
                        if (funcs[finalI].getText().isEmpty() || funcs[finalI].getText().equals("ERROR"))
                            funcs[finalI].setText("NO FUNCTION ENTERED");
                        if (!funcs[finalI].getText().equals("NO FUNCTION ENTERED"))
                            Go(finalI);
                    }else //点击已打勾的show键时，隐藏函数图像
                        unshow(finalI);
                }
            }));
        }
    }
    /**
     * @Description  获取自定义函数页面跳转来的函数式
 * @param f 跳转得到的函数式
     * @author sxq
     * @date 2023/12/11 10:33
    **/
    public void getJumpFunction(String f){
        funcs[1].setText(f);
    }
}
