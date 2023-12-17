package com.calculator.calculation;

import Database.SqlProb;
import Database.SqlScientific;
import Probability.BasicSolve;
import Probability.Exception.NotInputDataException;
import Probability.InputData;
import Probability.RegressionAnalysis;
import Scientific.ScientificSolve;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author 郑悦
 * @Description: 处理概率统计的点击事件
 * @date 2023/11/30 11:30
 */
public class ProbabilityController implements Initializable {

    public StackPane ProbabilityCardContainer;
    public ImageView historyImg;
    public TextField ActionName;
    public Pane PoissonDistribution;
    public Pane GaussianDistribution;
    public Pane RegressionAnalysis;
    public Pane BasicAnalysis;
    public ChoiceBox<Integer> choiceBox;
    @FXML
    public Text input1;
    @FXML
    public Text input2;
    public TextField processMethod;
    public TextField Mean1;
    public TextField Median1;
    public TextField Mode1;
    public TextField Range1;
    public TextField Cov;
    public TextField Variance1;
    public TextField StandardDeviation1;
    public TextField Percent;
    public TextField correlationCoefficient;
    public TextField StandardDeviation2;
    public TextField Median2;
    public TextField Variance2;
    public TextField Mode2;
    public TextField Range2;
    public TextField Mean2;
    public TextField Percentiles;
    public TextField InputData1;
    public TextField InputData2;
    public MenuButton PercentRV;
    private final Integer[] paraNum = {1, 2};
    public Text CovText;
    public Text CorrelationText;
    public MenuButton processMenu;
    public TableView DataTable;
    public TableColumn inputColumn1;
    public TableColumn inputColumn2;
    public ArrayList<InputData> DataToShow = new ArrayList<>();
    public Text RVX;
    public Text RVY;
    public Button PercentEnter;
    public TableView oneInputTable;
    public TableColumn oneInputColumn;
    public LineChart lineChart;
    public TextField yInput;
    public TextField xInput;
    public MenuItem linear;
    public MenuItem poly;
    public MenuItem exp;
    public MenuItem log;
    public Button yShow;
    public TableColumn residual;
    public TableColumn mse;
    public TableColumn r;
    public Button xShow;
    public NumberAxis xExtend;
    public NumberAxis yExtend;
    public TextField regressionExpression;
    public TableView TableRegression;
    public TextField PExpression;
    public Label pExpressionLabel;
    public LineChart lineChartGauss;
    public NumberAxis xExtend1;
    public NumberAxis yExtend1;
    public ChoiceBox<String> PolyJie;
    public TextField newInputX;
    public TextField answerY;
    public Label pExpressionLabel1;
    public TextField PExpression1;
    public TextField lamda;
    public TextField pOut1;
    public LineChart lineChartPossion;
    public NumberAxis xExtend11;
    public NumberAxis yExtend11;
    public TextField xIn1;
    public TextField pOut;
    public TextField u;
    public TextField o;
    public ImageView returnImg;
    public TextField xIn;
    public Pane HistoryPane;
    public TableView tableView;
    public TableColumn timeList;
    public TableColumn data1List;
    public TableColumn data2List;
    public Menu actionMenu;
    boolean flagTwoInput = false; // 判断两行都输入后开始在表格中显示
    boolean flagWithProbability = false;
    boolean flagRawProcess = false; // 判断是不是两个随机变量输入-true即不带概率
    boolean flagInput1 = false;
    boolean flagInput2 = false;
    boolean flagOneInput = false; // 只有一行数据输入
    boolean flagHasBasicSolve = false; // 确认基础数据处理模型是否建立完毕
    char chosenRV = 'X'; // 注意只有在页面初始化和切换时重置？其他时候保持目前的X或者Y选项
    public static ArrayList<String> dataMemory1 = new ArrayList<>(); // 用于历史记录
    public static ArrayList<String> dataMemory2 = new ArrayList<>();

    BasicSolve basicProb;
    double[] data1;
    double[] data2;
    public XYChart.Series <Number, Number> dataSeries, fitSeries;

    int PolyNum = 1;
    int RegressionType;
    RegressionAnalysis regress;
    boolean flagHasRegress = false;

    int RVType = 0;
    boolean flagHasPossion = false;
    boolean flagHasNormal = false;
    private ArrayList<InputData> list = new ArrayList<>();

    /**
     * @Description 点击图片加载历史记录页面
     * @param event
     * @author 郑悦
     * @date 2023/11/27 10:50
     **/
    @FXML
    private void handleHisImageClick(MouseEvent event) throws IOException {
        list = SqlProb.getAllHis();
        actionMenu.setVisible(false);
        ActionName.setVisible(false);
        BasicAnalysis.setVisible(false);
        GaussianDistribution.setVisible(false);
        PoissonDistribution.setVisible(false);
        RegressionAnalysis.setVisible(false);
        HistoryPane.setVisible(true);
//        ObservableList<InputData> observableList = FXCollections.observableArrayList(list);
//        tableView.setItems(observableList);
        if(list==null)
            return;
        tableView.getItems().setAll(list);
        tableView.refresh();
    }
    public void handleReturnClick(MouseEvent mouseEvent) {
        BasicAnalysis.setVisible(true);
        actionMenu.setVisible(true);
        ActionName.setVisible(true);
        GaussianDistribution.setVisible(false);
        PoissonDistribution.setVisible(false);
        RegressionAnalysis.setVisible(false);
        HistoryPane.setVisible(false);
        ActionName.setText("Basic Analysis");
    }
    /**
     * @Description 切换到基础数字特征计算页面        
     * @author 郑悦
     * @date 2023/12/4 9:58
    **/
    public void BasicAnalysisShift() {
        flagRawProcess = true;
        flagOneInput = false;
        flagTwoInput = true;
        flagInput1 = false; // 是否已经确定输入一个文本框（必须 要在不同文本框
        flagInput2 = false;
        BasicAnalysis.setVisible(true);
        GaussianDistribution.setVisible(false);
        PoissonDistribution.setVisible(false);
        RegressionAnalysis.setVisible(false);
        ActionName.setText("Basic Analysis");
        HistoryPane.setVisible(false);
    }
    /**
     * @Description 切换到高斯分布页面
     * @param actionEvent        
     * @author 郑悦
     * @date 2023/12/4 9:59
    **/
    public void GaussianDistributionShift(ActionEvent actionEvent) {
        RVType = 1;
        flagHasNormal = false;
        GaussianDistribution.setVisible(true);
        BasicAnalysis.setVisible(false);
        PoissonDistribution.setVisible(false);
        RegressionAnalysis.setVisible(false);
        ActionName.setText("Gaussian Distribution");
        HistoryPane.setVisible(false);
    }
    /**
     * @Description 切换到泊松分布页面
     * @param actionEvent
     * @author 郑悦
     * @date 2023/12/4 9:59
    **/
    public void PoissonDistributionShift(ActionEvent actionEvent) {
        RVType = 2;
        flagHasPossion = false;
        PoissonDistribution.setVisible(true);
        BasicAnalysis.setVisible(false);
        GaussianDistribution.setVisible(false);
        RegressionAnalysis.setVisible(false);
        ActionName.setText("Poisson Distribution");
        HistoryPane.setVisible(false);
    }
    /**
     * @Description 切换到回归分析页面
     * @param actionEvent
     * @author 郑悦
     * @date 2023/12/4 9:59
    **/
    public void RegressionAnalysisShift(ActionEvent actionEvent) {
        flagHasRegress = false;
        linearAnalysis(); // 切换这个页面时以线性回归为初始化
        RegressionAnalysis.setVisible(true);
        BasicAnalysis.setVisible(false);
        GaussianDistribution.setVisible(false);
        PoissonDistribution.setVisible(false);
        ActionName.setText("Regression Analysis");
        HistoryPane.setVisible(false);
    }

    /**
     * @Description 对应用户选择的处理变量数目
     * @param event
     * @return java.lang.Integer
     * @author 郑悦
     * @date 2023/12/3 20:47
    **/
    public Integer getParaNum(ActionEvent event){
        int num= choiceBox.getValue();
        switch (num) {
            case 1 -> {
                oneInputTable.setVisible(true);
                DataTable.setVisible(false);
                // 单变量把协方差和相关系数设置为不可见，以及第二个随机变量的相关数字特征不可见，其余可见
                Cov.setVisible(false);
                correlationCoefficient.setVisible(false);
                CovText.setVisible(false);
                CorrelationText.setVisible(false);
                PercentRV.setDisable(true);
                processMenu.setDisable(false);
                Mean2.setVisible(false);
                Median2.setVisible(false);
                Mode2.setVisible(false);
                Range2.setVisible(false);
                Variance2.setVisible(false);
                input2.setVisible(false);
                InputData2.setVisible(false);
                StandardDeviation2.setVisible(false);
                RVX.setVisible(true);
                RVY.setVisible(false);

                // flag初始化
                flagTwoInput = false;
                flagOneInput = true;
                flagInput1 = false;
                flagInput2 = false;
            }
            case 2 -> {
                DataTable.setVisible(true);
                oneInputTable.setVisible(false);
                Cov.setVisible(true);
                correlationCoefficient.setVisible(true);
                CovText.setVisible(true);
                CorrelationText.setVisible(true);
                PercentRV.setDisable(false);
                // 只有两列表格，所以含概率的数据处理仅限一个随机变量的时候
                processMenu.setDisable(true);
                Mean2.setVisible(true);
                Median2.setVisible(true);
                Mode2.setVisible(true);
                Range2.setVisible(true);
                Variance2.setVisible(true);
                input2.setVisible(true);
                InputData2.setVisible(true);
                StandardDeviation2.setVisible(true);
                RVX.setVisible(true);
                RVY.setVisible(true);

                // 防止在处理带概率部分切到2个变量（注意概率处理只能1个变量）
                input2.setText("变量Y");
                inputColumn2.setText("Y");

                // flag初始化
                flagTwoInput = true;
                flagOneInput = false;
                flagInput1 = false;
                flagInput2 = false;
            }
        }
        return num;
    }
    /**
     * @Description 初始化概率统计功能布局
     * @param url
     * @param resourceBundle        
     * @author 郑悦
     * @date 2023/12/4 9:58
    **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { // 初始时页面显示基础数字特征获取
        choiceBox.getItems().addAll(paraNum);
        choiceBox.setValue(2);
        choiceBox.setOnAction(this::getParaNum);
        flagTwoInput = true;
        flagRawProcess = true;
        BasicAnalysisShift();
        DataTable.setPlaceholder(new Label("表中无数据"));
        ActionName.setText("Basic Analysis");
        // 建立表格各列的一一映射
        inputColumn1.setCellValueFactory(new PropertyValueFactory<>("input1"));
        inputColumn2.setCellValueFactory(new PropertyValueFactory<>("input2"));
        inputColumn1.setText("X");
        inputColumn2.setText("Y");
        oneInputColumn.setCellValueFactory(new PropertyValueFactory<>("input1"));
        oneInputColumn.setText("X");
        // 回归的合理性参考数据的表格映射
        residual.setCellValueFactory(new PropertyValueFactory<>("residual"));
        r.setCellValueFactory(new PropertyValueFactory<>("r"));
        mse.setCellValueFactory(new PropertyValueFactory<>("MSE"));
        xShow.setDisable(true); // 只用来显示
        yShow.setDisable(true);
        tableView.setPlaceholder(new Label("无历史记录"));//占位文本
        timeList.setCellValueFactory(new PropertyValueFactory<>("saveTime"));
        data1List.setCellValueFactory(new PropertyValueFactory<>("data1"));
        data2List.setCellValueFactory(new PropertyValueFactory<>("data2"));
        // 初始化多项式模拟阶数的choiceBox
        PolyJie.getItems().addAll("一阶多项式", "二阶多项式", "三阶多项式", "四阶多项式", "五阶多项式"); // 添加选项
        PolyJie.setOnAction(PolySim -> {
            switch (PolyJie.getValue()) {
                case "一阶多项式" -> PolyNum = 1;
                case "二阶多项式" -> PolyNum = 2;
                case "三阶多项式" -> PolyNum = 3;
                case "四阶多项式" -> PolyNum = 4;
                case "五阶多项式" -> PolyNum = 5;
            }
        });
        lineChartInit();
    }
    
    /**
     * @Description  处理原始数据
     * @author 郑悦
     * @date 2023/12/3 20:48
    **/
    public void ProcessRaw() {
        choiceBox.setDisable(false);
        returnPage();
        processMethod.setText("原始数据");
        input2.setText("变量Y");
        inputColumn2.setText("Y");
        flagRawProcess = true;
        flagWithProbability = false;
    }
    /**
     * @Description  处理带概率的随机变量
     * @author 郑悦
     * @date 2023/12/3 20:49
    **/
    public void ProcessWithProbability() {
        // 概率处理指只对一个随机变量
        choiceBox.setDisable(true);
        processMethod.setText("概率处理");
        input2.setVisible(true);
        InputData2.setVisible(true);
        input2.setText("概率");
        oneInputTable.setVisible(false);
        DataTable.setVisible(true);
        inputColumn2.setText("P");
        // column的输入和初始化仍保持一致
        flagWithProbability = true;
        flagRawProcess = false;
        flagTwoInput = true; // 需要两行输入，变量值和对应概率
    }

    String cntString1, cntString2;
    Exception exception = new NotInputDataException("未输入数据");
    /**
     * @Description 在第一个文本框输入数据并按下enter键
     * @param keyEvent        
     * @author 郑悦
     * @date 2023/12/4 9:57
    **/
    public void handleInput1KeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String input = InputData1.getText();
            // 获取数据到数组
            try {
                getData(input, 1);
            } catch (NotInputDataException e) {
                // 展示javafx的即时提示词
                showAlert("错误提示", "未输入数据", 2000);
                return; // 不return的话catch完会继续执行方法剩下的语句
            }
            if (flagInput2) {
                cntString1 = input;
                inputListInit();
            }
            else {
                cntString1 = input;
                flagInput1 = true;
                if (flagOneInput) {
                    oneInputListInit();
                    return;
//                    addDataToTable(); // 只有一行输入时，只保留上方的文本框
                }
                InputData2.requestFocus(); // 输入完这一行切换到下一行
            }
        }
    }
    /**
     * @Description 在第二个文本框输入数据并按下enter键
     * @param keyEvent
     * @author 郑悦
     * @date 2023/12/4 10:26
    **/
    public void handleInput2KeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String input = InputData2.getText();
            // 获取数据到数组
            try {
                getData(input, 2);
            } catch (NotInputDataException e) {
                // 展示javafx的即时提示词
                showAlert("错误提示", "未输入数据", 2000);
                return; // 不return的话catch完会继续执行方法剩下的语句
            }
            if (flagInput1) {
                cntString2 = input;
                inputListInit();
            }
            else {
                cntString2 = input;
                flagInput2 = true;
                InputData1.requestFocus();
            }
        }
    }
    /**
     * @Description 处理一行输入时表格中各项数据（以一行为单位的初始化）
     * @author 郑悦
     * @date 2023/12/4 10:16
     **/
    private void oneInputListInit() {
        int dataNum = data1.length;
        for (int i = 0; i < dataNum; i++) {
            oneInputTable.getItems().add(new InputData(data1[i], 0.0));
        }
        basicProb = new BasicSolve(data1);
        flagHasBasicSolve = true;
        rawShowMathCharacter1(basicProb, 1);
        // 处理完数据的显示开始重置
        DataToShow.clear();
        flagInput1 = false;
    }
    /**
     * @Description 处理表格中各项数据（以一行为单位的初始化）
     * @author 郑悦
     * @date 2023/12/4 10:16
    **/
    private void inputListInit() {
        int dataNum = data1.length;
        if (data2.length != dataNum) {
            showAlert("错误提示", "数据数量不匹配", 2000);
            return;
        }
        SqlProb.add(new InputData(cntString1, cntString2));
        for (int i = 0; i < dataNum; i++) {
            DataToShow.add(new InputData(data1[i], data2[i]));
            DataTable.getItems().add(new InputData(data1[i], data2[i]));
        }
        // 处理数字特征的显示
        basicProb = new BasicSolve(data1, data2, true);
        flagHasBasicSolve = true;
        rawShowMathCharacter1(basicProb, 1);
        rawShowMathCharacter1(basicProb, 2);
        showRelate(basicProb);
        // 处理完数据的显示开始重置
        DataToShow.clear();
        flagInput1 = false;
    }

    /**
     * @Description 显示原始数据输入时，各变量各数字特征
     * @param basicSolve
     * @author 郑悦
     * @date 2023/12/4 14:38
    **/
    private void rawShowMathCharacter1(BasicSolve basicSolve, int ToWhich) {
        switch (ToWhich) {
            case 1:
                Mean1.setText(String.valueOf(basicSolve.mean));
                Median1.setText(String.valueOf(basicSolve.median));
                Mode1.setText(basicSolve.Mode.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(" ")));
                Range1.setText(String.valueOf(basicSolve.Range));
                Variance1.setText(String.valueOf(basicSolve.Variance));
                StandardDeviation1.setText(String.valueOf(basicSolve.StandardDeviation));
                break;
            case 2:
                Mean2.setText(String.valueOf(basicSolve.meanY));
                Median2.setText(String.valueOf(basicSolve.medianY));
                Mode2.setText(basicSolve.ModeY.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(" ")));
                Range2.setText(String.valueOf(basicSolve.RangeY));
                Variance2.setText(String.valueOf(basicSolve.VarianceY));
                StandardDeviation2.setText(String.valueOf(basicSolve.StandardDeviationY));
                break;
        }
    }
    /**
     * @Description 展示相关性
     * @param basicSolve
     * @author 郑悦
     * @date 2023/12/4 15:05
    **/
    private void showRelate(BasicSolve basicSolve) {
        Cov.setText(String.valueOf(basicSolve.Cov));
        correlationCoefficient.setText(String.valueOf(basicSolve.correlationCoefficient));
    }
    /**
     * @Description 处理用户删改数据
     * @author 郑悦
     * @date 2023/12/9 15:23
    **/
    public void doWithEdit() {
    }

    /**
     * @Description 从输入的字符串中获取对应数据
     * @param input
     * @param ToWhich
     * @author 郑悦
     * @date 2023/12/3 22:11
    **/
    public void getData(String input, int ToWhich) throws NotInputDataException  {
        if (input.isEmpty()) {
            throw new NotInputDataException("未输入数据");
        }
        String[] numberStrings;
        // 格式化用户乱七八糟的输入，防止java异常乱飞
        numberStrings = input.split(" ");
        double[] numbers = new double[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            numbers[i] = Double.parseDouble(numberStrings[i]);
        }
        switch (ToWhich) {
            case 1 -> data1 = numbers;
            case 2 -> data2 = numbers;
        }
    }
    /**
     * @Description 对用户错误使用而抛出异常进行警告处理
     * @param title
     * @param message
     * @param duration
     * @author 郑悦
     * @date 2023/12/3 22:12
    **/
    public static void showAlert(String title, String message, int duration) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(500), alert.getDialogPane());
        fadeInTransition.setFromValue(0);
        fadeInTransition.setToValue(1);

        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(500), alert.getDialogPane());
        fadeOutTransition.setFromValue(1);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setDelay(Duration.millis(duration));
        fadeOutTransition.setOnFinished(event -> alert.close());

        fadeInTransition.play();
        fadeOutTransition.play();

        alert.showAndWait();
    }
    /**
     * @Description 重写警告方法，持续显示，不自动消失
     * @param title
     * @param message
     * @author 郑悦
     * @date 2023/12/16 20:28
    **/
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
    /**
     * @Description 把获得的数据添加到数据表格中进行显示
     * @author 郑悦
     * @date 2023/12/3 22:15
    **/
    public void addDataToTable() {
        ObservableList<Double> data = FXCollections.observableArrayList();
        double[] dataToProcess = data1; // 创建引用对象但不实例化，只为了区分是对那个进行操作
        TableColumn<Double, Double> column = inputColumn1;
        for (double value : dataToProcess) {
            data.add(value);
        }
        DataTable.setItems(data);
        // 置零清空逻辑有误，要是想只换其中一组数据
        // 处理完数据的显示开始重置
        DataToShow.clear();
        flagInput1 = false;
        flagTwoInput = false;
        // 这里处理两组数据输入的添加表格显示，注意补充一组数据的新函数（新表格）
    }
    /**
     * @Description 处理用户进行百分数读取
     * @param keyEvent
     * @author 郑悦
     * @date 2023/12/4 15:11
    **/
    public void handlePercentKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            // 自己多少百分位没输入抛出异常
            String input = Percent.getText();
            double percent = Double.parseDouble(input);
            if (!flagHasBasicSolve) {
                return; // 这里应该增加无数据输入想获得输出的异常
            }
            // 没数据时抛出异常
            System.out.println(flagTwoInput);
            if (flagTwoInput) {
                if (flagWithProbability) { // 两组输入包括一组概率时
                    Percentiles.setText(String.valueOf(BasicSolve.getWeightedPercent(basicProb.values, basicProb.probabilities, percent/100)));
                }
                // 输入两组随机变量时应结合相应X，Y的判断
                switch (chosenRV) {
                    case 'X': // 初始值直接置成X？
                        Percentiles.setText(String.valueOf(basicProb.getPercentilesInStat(1, percent)));
                        break;
                    case 'Y':
                        Percentiles.setText(String.valueOf(basicProb.getPercentilesInStat(2, percent)));
                        break;
                }
            }
            else {
                // 处理一组数据
                Percentiles.setText(String.valueOf(basicProb.getPercentilesInStat(1, percent)));
            }
        }
    }
    /**
     * @Description 获取百分位数
     * @param mouseEvent
     * @author 郑悦
     * @date 2023/12/4 16:17
    **/
    public void PercentGetClick(ActionEvent mouseEvent) {
        String input = Percent.getText();
        double percent = Double.parseDouble(input);
        if (!flagHasBasicSolve) {
            return; // 这里应该增加无数据输入想获得输出的异常
        }
        // 没数据时抛出异常
        System.out.println(530);
        // 输入两组随机变量时应结合相应X，Y的判断
        switch (chosenRV) {
            case 'X': // 初始值直接置成X？
                System.out.println(666);
                Percentiles.setText(String.valueOf(basicProb.getPercentilesInStat(1, percent)));
                break;
            case 'Y':
                Percentiles.setText(String.valueOf(basicProb.getPercentilesInStat(2, percent)));
                break;
        }
    }

    /**
     * @Description 处理对应的变量选择，要求X还是Y的百分数位
     * @param actionEvent
     * @author 郑悦
     * @date 2023/12/9 15:13
    **/
    public void varXPercent(ActionEvent actionEvent) {
        chosenRV = 'X';
    }
    public void varYPercent(ActionEvent actionEvent) {
        chosenRV = 'Y';
    }
    /**
     * @Description 用代码模拟控件操作 防止切换页面的时候出错
     * @author 郑悦
     * @date 2023/12/9 17:24
    **/
    public void returnPage() {
        // 模拟选择第一个选项的操作
        choiceBox.getSelectionModel().selectLast();
        choiceBox.fireEvent(new javafx.scene.input.MouseEvent(javafx.scene.input.MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, javafx.scene.input.MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
    }
    /**
     * @Description 切换页面时把本页面文本框以及表格内的内容清空
     * @author 郑悦
     * @date 2023/12/9 17:29
    **/
    public void clearPage() {
        InputData1.clear(); InputData2.clear();
        Mean1.clear();  Mean2.clear();
        Median1.clear();    Median2.clear();
        Mode1.clear();  Mode2.clear();
        Range1.clear(); Range2.clear();
        Variance1.clear();  Variance2.clear();
        StandardDeviation1.clear(); StandardDeviation2.clear();
        Percentiles.clear();    Percent.clear();
        Cov.clear();    correlationCoefficient.clear();
        DataTable.getItems().clear();   oneInputTable.getItems().clear();
    }
    public void clearRegressionPage() {
        flagHasRegress = false;
        xInput.clear(); yInput.clear();
        regressionExpression.clear();
        TableRegression.getItems().clear();
    }
    /**
     * @Description 线性回归
     * @author 郑悦
     * @date 2023/12/10 16:30
    **/
    public void linearAnalysis() {
        clearRegressionPage();
        PolyJie.setVisible(false);
        flagTwoInput = true;
        flagInput1 = false;
        flagInput2 = false;
        RegressionType = 1;
    }
    /**
     * @Description 多项式回归
     * @param actionEvent
     * @author 郑悦
     * @date 2023/12/10 16:30
    **/
    public void polyAnalysis(ActionEvent actionEvent) {
        clearRegressionPage();
        PolyJie.setVisible(true);
        flagTwoInput = true;
        flagInput1 = false;
        flagInput2 = false;
        RegressionType = 2;
    }
    /**
     * @Description 指数回归
     * @param actionEvent
     * @author 郑悦
     * @date 2023/12/10 16:30
    **/
    public void expAnalysis(ActionEvent actionEvent) {
        clearRegressionPage(); // 先清空再显示页面
        PolyJie.setVisible(false);
        flagTwoInput = true;
        flagInput1 = false;
        flagInput2 = false;
        RegressionType = 3;
    }
    /**
     * @Description 对数回归
     * @param actionEvent
     * @author 郑悦
     * @date 2023/12/10 16:30
    **/
    public void logAnalysis(ActionEvent actionEvent) {
        clearRegressionPage();
        PolyJie.setVisible(false);
        flagTwoInput = true;
        flagInput1 = false;
        flagInput2 = false;
        RegressionType = 4;
    }
    /**
     * @Description 处理线性回归时自变量输入
     * @param keyEvent
     * @author 郑悦
     * @date 2023/12/10 20:15
    **/
    public void handleIndependentVar(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            processInputEnter(xInput, 1);
        }
    }
    /**
     * @Description 处理线性回归时因变量输入
     * @param keyEvent
     * @author 郑悦
     * @date 2023/12/10 20:15
    **/
    public void handleDependentVar(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            processInputEnter(yInput, 2);
        }
    }
    /**
     * @Description 把重复的获取输入数据code归并成函数进行处理
     * @param inputText
     * @param num
     * @author 郑悦
     * @date 2023/12/10 20:25
    **/
    public void processInputEnter(TextField inputText, int num) {
        String input = inputText.getText();
        // 获取数据到数组
        try {
            getData(input, num);
        } catch (NotInputDataException e) {
            // 展示javafx的即时提示词
            showAlert("错误提示", "未输入数据", 2000);
            return; // 不return的话catch完会继续执行方法剩下的语句
        }
        boolean flagHasPre = false;
        switch (num) {
            case 1:
                flagHasPre = flagInput2; // 如果现在处理的是1textField的enter，应该判断是否2中有字段
                break;
            case 2:
                flagHasPre = flagInput1;
                break;
        }
        if (flagHasPre) { // 两行输入都准备完毕，进行回归分析
            int dataNum = data1.length;
            if (data2.length != dataNum) {
                showAlert("错误提示", "数据数量不匹配", 2000);
                return;
            }
            showRegressionLineChart();
            showRegressionTable();
        }
        else {
            switch (num) {
                case 1:
                    flagInput1 = true;
                    yInput.requestFocus();
                    break;
                case 2:
                    flagInput2 = true;
                    xInput.requestFocus();
                    break;
            }
        }
    }
    /**
     * @Description 显示回归有关的数字参量
     * @author 郑悦
     * @date 2023/12/11 22:09
    **/
    private void showRegressionTable() {
        regress = new RegressionAnalysis(data1, data2, RegressionType, PolyNum);
        InputData data = new InputData(regress.sumOfResidual, regress.MSE, regress.R);
        TableRegression.getItems().add(data);
        flagInput1 = false;
        flagInput2 = false;
    }

    /**
     * @Description 计算拟合得到的多项式的值
     * @param coefficients
     * @param x
     * @return double
     * @author 郑悦
     * @date 2023/12/11 16:03
    **/
    private double evaluatePolynomial(double[] coefficients, double x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, i);
        }
        return result;
    }
    /**
     * @Description 统一每种回归方式的图像显示
     * @author 郑悦
     * @date 2023/12/10 9:58
    **/
    public void showRegressionLineChart() {
        WeightedObservedPoints points = new WeightedObservedPoints();
        for(int i = 0; i < data1.length; i++) {
            points.add(data1[i], data2[i]);
        }
        double min = data1[0];
        double max = data1[0];
        for (int i = 1; i < data1.length; i++) {
            if (data1[i] < min) {
                min = data1[i];
            }
            if (data1[i] > max) {
                max = data1[i];
            }
        }
        // 线性回归
        SimpleRegression regression = new SimpleRegression();
        // 添加数据点
        for (int i = 0; i < data1.length; i++) {
            regression.addData(data1[i], data2[i]);
        }
        // 获取回归方程的系数
        double intercept = regression.getIntercept();   // 截距
        double slope = regression.getSlope();   // 斜率
        String equation = "y = " + slope + "x + " + intercept;
        // 多项式回归
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(PolyNum);
        double[] coefficients = fitter.fit(points.toList());
        // 创建原始数据点
        lineChart.getData().remove(dataSeries);
        lineChart.getData().remove(fitSeries);
        dataSeries = new XYChart.Series<>();
//        XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Data Points");
        for (int i = 0; i < points.toList().size(); i++) {
            WeightedObservedPoint point = points.toList().get(i);
            double x = point.getX();
            double y = point.getY();
            dataSeries.getData().add(new XYChart.Data<>(x, y));
        }
        regress = new RegressionAnalysis(data1, data2, RegressionType, PolyNum);
        flagHasRegress = true;
        // 创建拟合曲线
        fitSeries = new XYChart.Series<>();
//        XYChart.Series<Number, Number> fitSeries = new XYChart.Series<>();
        fitSeries.setName("Fitted Curve");
        double y;
        for (double x = min; x <= max; x += 0.01) {
            y = regression.predict(x); // 默认为线性回归
            switch (RegressionType) {
                case 1:
                    y = regression.predict(x);
                    break;
                case 2:
                    y = evaluatePolynomial(coefficients, x);
                    break;
                case 3:
                    y = regress.parameters[0] * Math.exp(regress.parameters[1] * x);
                    break;
                case 4:
                    y = regress.parameters[0] * Math.log(x) + regress.parameters[1];
                    break;
            }
            fitSeries.getData().add(new XYChart.Data<>(x, y));
        }
        lineChart.getData().addAll(fitSeries, dataSeries);
        lineChart.getStyleClass().add("line-chart");
        lineChart.getStylesheets().add(getClass().getResource("css/lineChartStyle.css").toExternalForm());
        dataSeries.getNode().getStyleClass().add("data-line"); // 设置原始数据和拟合数据在折线图中的样式
        fitSeries.getNode().getStyleClass().add("fit-line");
        for (XYChart.Data<Number, Number> data : fitSeries.getData()) { // 不想要拟合线上为作图取的点
            data.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: transparent;");
        }
        equation = regress.regressionExpression;
        regressionExpression.setText(equation);
    }
    /**
     * @Description 获取带预测的X的值
     * @param keyEvent
     * @author 郑悦
     * @date 2023/12/12 16:18
    **/
    public void getPredictedX(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String input = newInputX.getText();
            double ans = 0.0;
            if (isDouble(input)) {
                double x = Double.parseDouble(input);
                if (flagHasRegress) {
                    ans = regress.evaluatePolynomial(regress.parameters, x);
                    answerY.setText(String.valueOf(ans));
                }
                else {
                    showAlert("错误提示", "未输入待拟合数据", 2400);
                }
            }
            else {
                showAlert("错误提示", "请输入一个待预测自变量的值", 2400);
            }
        }
    }
    /**
     * @Description 判断一个字符串是不是只代表一个double数字
     * @param str
     * @return boolean
     * @author 郑悦
     * @date 2023/12/12 16:23
    **/
    public static boolean isDouble(String str) {
        try {
            // 尝试将字符串转换为 double 类型
            double number = Double.parseDouble(str);
            return true; // 转换成功，表示是一个有效的 double 数字
        } catch (NumberFormatException e) {
            return false; // 转换失败，表示不是一个有效的 double 数字
        }
    }
    public static boolean isInteger(String str) {
        return str.matches("-?\\d+");
    }
    // 处理概率模型
    double standardDeviation = 0.0;
    double mean = 0.0;
    double possionLamda = 0.0;
    String PDFExpress = "";
    org.apache.commons.math3.distribution.NormalDistribution normalDistribution
            = new org.apache.commons.math3.distribution.NormalDistribution(0, 1);
    org.apache.commons.math3.distribution.PoissonDistribution poissonDistribution
            = new org.apache.commons.math3.distribution.PoissonDistribution(1);
    public void handleGaussU(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            processGaussInputEnter(u);
        }
    }
    public void handleGaussO(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            processGaussInputEnter(o);
        }
    }
    public void handlePossionL(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String input = lamda.getText();
            if (isDouble(input)) {
                possionLamda = Double.parseDouble(input);
                flagHasPossion = true;
                poissonDistribution = new org.apache.commons.math3.distribution.PoissonDistribution(possionLamda);
                showDensityPDF();
            }
            else {
                showAlert("错误提示", "请输入一个待预测自变量的值", 2400);
            }
        }
    }
    /**
     * @Description 展示概率密度函数
     * @author 郑悦
     * @date 2023/12/12 18:58
    **/
    private void showDensityPDF() {
        lineChartPossion.getData().remove(fitSeries);
        lineChartGauss.getData().remove(fitSeries);
        fitSeries = new XYChart.Series<>();
        dataSeries = new XYChart.Series<>();
        double y;
        switch (RVType) {
            case 1:
                for (double x = mean-50; x <= mean+50; x += 0.1) {
                    y = normalDistribution.density(x);
                    fitSeries.getData().add(new XYChart.Data<>(x, y));
                }
                lineChartGauss.getData().addAll(fitSeries);
                lineChartGauss.getStyleClass().add("line-chart");
                lineChartGauss.getStylesheets().add(getClass().getResource("css/lineChartStyle.css").toExternalForm());
                fitSeries.getNode().getStyleClass().add("fit-line");
                fitSeries.setName("高斯分布概率密度曲线");
                for (XYChart.Data<Number, Number> data : fitSeries.getData()) { // 连续分布留线去点
                    data.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: transparent;");
                }
                PDFExpress = String.format("y = (e^(-((x-%.3f)^2/2*%.3f^2)) / sqrt(2*pi*%.3f^2)", mean, standardDeviation, standardDeviation);
                PExpression.setText(PDFExpress);
                break;
            case 2:
                for (int x = 0; x <= 30; x += 1) {
                    y = calculatePoissonPMF(possionLamda, x);
                    dataSeries.getData().add(new XYChart.Data<>(x, y));
                }
                lineChartPossion.getData().addAll(dataSeries);
                yExtend11.setLabel("P(X=k)");
                xExtend11.setLabel("k");
                lineChartPossion.getStyleClass().add("line-chart");
                lineChartPossion.getStylesheets().add(getClass().getResource("css/lineChartStyle.css").toExternalForm());
                dataSeries.getNode().getStyleClass().add("data-line"); // 离散型分布留点（X只能为整数）
                dataSeries.setName("泊松分布的概率质量函数");
                PDFExpress = String.format("y = ((e^(-%.3f))*(%.3f^x))/x!", possionLamda, possionLamda);
                PExpression1.setText(PDFExpress);
                break;
        }
    }
    /**
     * @Description 计算泊松分布的概率质量函数（PMF）
     * @param lambda
     * @param k
     * @return double
     * @author 郑悦
     * @date 2023/12/12 20:26
    **/
    private double calculatePoissonPMF(double lambda, int k) {
        double numerator = Math.pow(lambda, k) * Math.exp(-lambda);
        double denominator = factorial(k);
        return numerator / denominator;
    }
    /**
     * @Description 计算阶乘
     * @param n
     * @return double
     * @author 郑悦
     * @date 2023/12/12 20:26
    **/
    private double factorial(int n) {
        double result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    /**
     * @Description 处理高斯分布的输入参数
     * @param inputText
     * @author 郑悦
     * @date 2023/12/11 20:25
     **/
    public void processGaussInputEnter(TextField inputText) {
        String input = inputText.getText();
        int getWhich = 0;
        if (inputText == u) {
            getWhich = 1;
        } else {
            getWhich = 2;
        }
        if (isDouble(input)) {
            double x = Double.parseDouble(input);
            if (getWhich == 1) {
                mean = x;
            } else {
                standardDeviation = x;
            }
        }
        boolean flagHasPre = false;
        switch (getWhich) {
            case 1:
                flagHasPre = flagInput2;
                break;
            case 2:
                flagHasPre = flagInput1;
                break;
        }
        if (flagHasPre) { // 两行输入都准备完毕，进行高斯分布
            flagHasNormal = true;
            normalDistribution = new org.apache.commons.math3.distribution.NormalDistribution(mean, standardDeviation);
            if (isDouble(input)) {
                showDensityPDF();
            }
            else {
                showAlert("错误提示", "请输入一个待预测自变量的值", 2400);
            }
        }
        else {
            switch (getWhich) {
                case 1:
                    flagInput1 = true;
                    o.requestFocus();
                    break;
                case 2:
                    flagInput2 = true;
                    u.requestFocus();
                    break;
            }
        }
    }
    /**
     * @Description 处理高斯分布对应x的分布函数值获取
     * @param keyEvent
     * @author 郑悦
     * @date 2023/12/12 20:32
    **/
    public void getXin(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String input = xIn.getText();
            double ans = 0.0;
            if (isDouble(input)) {
                double x = Double.parseDouble(input);
                if (flagHasNormal) {
                    ans = normalDistribution.cumulativeProbability(x);
                    pOut.setText(String.valueOf(ans));
                }
                else {
                    showAlert("错误提示", "未输入待拟合数据", 2400);
                }
            }
            else {
                showAlert("错误提示", "请输入一个待预测自变量的值", 2400);
            }
        }
    }
    public void getXin1(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String input = xIn1.getText();
            double ans = 0.0;
            if (isInteger(input)) {
                int x = Integer.parseInt(input);
                if (flagHasPossion) {
                    ans = poissonDistribution.cumulativeProbability(x);
                    pOut1.setText(String.valueOf(ans));
                }
                else {
                    showAlert("错误提示", "未输入待拟合数据", 2400);
                }
            }
            else {
                showAlert("错误提示", "请输入一个待预测自变量的值", 2400);
            }
        }
    }
    Tooltip tooltip = new Tooltip();
    private Label coordinatesLabel = new Label();
    // 创建鼠标与坐标图的联系
    public void lineChartInit() {
        Tooltip.install(lineChart, tooltip);

        // 注册鼠标移动事件监听器
        lineChart.setOnMouseMoved(event -> {
            coordinatesLabel.setText("");
            tooltip.setText("");
        });

        // 注册鼠标移出事件监听器
        lineChart.setOnMouseExited(event -> {
            handleMouseMoved(event);
            tooltip.setText(coordinatesLabel.getText());
        });

        lineChart.setOnMouseMoved(this::handleMouseMoved);
    }
    private void handleMouseMoved(MouseEvent event) {
        // 获取鼠标的 x 坐标
        double mouseX = event.getX();
        double mouseY = event.getY();

        // 获取 LineChart 的 x 轴
        NumberAxis xAxis = (NumberAxis) ((LineChart) event.getSource()).getXAxis();
        NumberAxis yAxis = (NumberAxis) ((LineChart) event.getSource()).getYAxis();

        // 获取最接近鼠标位置的数据点
        double nearestX = xAxis.getValueForDisplay(mouseX).doubleValue();
        double nearestY = yAxis.getValueForDisplay(mouseY).doubleValue();

        // 更新坐标值标签的位置和文本
        coordinatesLabel.setText(String.format("(%.2f , ", nearestX) + String.format("%.2f)", nearestY));
        coordinatesLabel.setLayoutX(event.getSceneX() + 10);
        coordinatesLabel.setLayoutY(event.getSceneY() - 10);
    }

    public void handleRowClick(MouseEvent event) {
        // 判断是否双击行
        if (event.getClickCount() == 2&&event.getButton()== MouseButton.PRIMARY) {
            InputData selectedItem = (InputData) tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String data11 = selectedItem.getData1();
                String data22 = selectedItem.getData2();
                BasicAnalysis.setVisible(true);
                InputData1.setText(data11);
                try {
                    getData(data11, 1);
                    getData(data22, 1);
                } catch (NotInputDataException e) {
                    // 展示javafx的即时提示词
                    showAlert("错误提示", "未输入数据", 2000);
                    return; // 不return的话catch完会继续执行方法剩下的语句
                }
                inputListInit();
            }
        } else if(event.getButton()==MouseButton.SECONDARY){//右键单击选择是否删除
            InputData ift = (InputData) tableView.getSelectionModel().getSelectedItem();
            if(SqlProb.exists(ift.getSaveTime())){
                Dialog alert1 = new Dialog();
                alert1.getDialogPane().getScene().getStylesheets().add(getClass().getResource("style/dialog.css").toExternalForm());
                alert1.getDialogPane().getStyleClass().add("alertFunction");
                alert1.setContentText("删除这条历史记录？");
                alert1.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                Optional<ButtonType> result = alert1.showAndWait();
                if (result.get() == ButtonType.OK){
                    SqlProb.delete(ift);
                    getHistoryList();
                }
            }
        }
    }
    private void getHistoryList(){
        list = SqlProb.getAllHis();
        tableView.getItems().setAll(list);
        tableView.refresh();
    }
}
