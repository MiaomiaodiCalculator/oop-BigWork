package com.calculator.calculation;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import com.calculator.calculation.Visualizing.*;

import java.net.URL;
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
    public Parser parser;

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
        double incr = 0.1;
        double val;
        for (double i = -100; i <= 100; i+=incr) {
            val=1;  // 这行替换为泊松分布取表
            if (num == 1) data1.getData().add(new Data<>(i, val));
            if (num == 2) data2.getData().add(new Data<>(i, val));
            if (num == 3) data3.getData().add(new Data<>(i, val));
            if (num == 4) data4.getData().add(new Data<>(i, val));
            if (num == 5) data5.getData().add(new Data<>(i, val));
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
        shows[num].setText("");
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
        // set Texts to null or ""
        funcs = new TextField[]{null, f1, f2, f3, f4, f5};
        shows = new Button[]{null, show1, show2, show3, show4, show5};
        dataShow = new String[]{null,null,null,null,null,null};
        graphChart.getData().removeAll();
        for(int i=1;i<=5;i++){
            funcs[i].setText("");
            shows[i].setText("");
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
}
