package com.calculator.calculation;

import Probability.BasicSolve;
import Probability.Exception.NotInputDataException;
import Probability.InputData;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
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
    double[] probability;
    double percent;

    /**
     * @Description 点击图片加载历史记录页面
     * @param event
     * @author 郑悦
     * @date 2023/11/27 10:50
     **/
    @FXML
    private void handleHisImageClick(MouseEvent event) {
        loadPage("DiscreteMathHistory.fxml");
    }
    public void handleReturnClick(MouseEvent mouseEvent) {
        loadPage("DiscreteMathBoolean.fxml");
        ActionName.setText("Basic Analysis");
    }
    /**
     * @Description  加载卡片布局：fxml文件
     * @param fxmlFileName 要打开的fxml文件名称
     * @author 郑悦
     * @date 2023/11/25 22:51
     **/
    private void loadPage(String fxmlFileName) {
        try {
            Pane page = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFileName)));
            ProbabilityCardContainer.getChildren().setAll(page);
        } catch (Exception e) {
            System.out.println("error");
        }
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
        flagInput1 = false; // 是否已经确定输入一个文本框（必须要在不同文本框
        flagInput2 = false;
        BasicAnalysis.setVisible(true);
        GaussianDistribution.setVisible(false);
        PoissonDistribution.setVisible(false);
        RegressionAnalysis.setVisible(false);
        ActionName.setText("Basic Analysis");
    }
    /**
     * @Description 切换到高斯分布页面
     * @param actionEvent        
     * @author 郑悦
     * @date 2023/12/4 9:59
    **/
    public void GaussianDistributionShift(ActionEvent actionEvent) {
        GaussianDistribution.setVisible(true);
        BasicAnalysis.setVisible(false);
        PoissonDistribution.setVisible(false);
        RegressionAnalysis.setVisible(false);
        ActionName.setText("Gaussian Distribution");
    }
    /**
     * @Description 切换到泊松分布页面
     * @param actionEvent
     * @author 郑悦
     * @date 2023/12/4 9:59
    **/
    public void PoissonDistributionShift(ActionEvent actionEvent) {
        PoissonDistribution.setVisible(true);
        BasicAnalysis.setVisible(false);
        GaussianDistribution.setVisible(false);
        RegressionAnalysis.setVisible(false);
        ActionName.setText("Poisson Distribution");
    }
    /**
     * @Description 切换到回归分析页面
     * @param actionEvent
     * @author 郑悦
     * @date 2023/12/4 9:59
    **/
    public void RegressionAnalysisShift(ActionEvent actionEvent) {
        RegressionAnalysis.setVisible(true);
        BasicAnalysis.setVisible(false);
        GaussianDistribution.setVisible(false);
        PoissonDistribution.setVisible(false);
        ActionName.setText("Regression Analysis");
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
//        inputColumn1.setCellValueFactory(new PropertyValueFactory<>("X"));
//        inputColumn2.setCellValueFactory(new PropertyValueFactory<>("P"));
        flagWithProbability = true;
        flagRawProcess = false;
        flagTwoInput = true; // 需要两行输入，变量值和对应概率
    }

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
                inputListInit();
            }
            else {
                flagInput1 = true;
                if (flagOneInput) {
                    oneInputListInit();
//                    addDataToTable(); // 只有一行输入时，只保留上方的文本框
                }
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
                inputListInit();
            }
            else {
                flagInput2 = true;
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
    private void showAlert(String title, String message, int duration) {
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
}
