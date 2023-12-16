package com.calculator.calculation;

import DiscreteMath.GraphOfMatrix;
import Probability.Exception.NotInputDataException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import vector.VectorSolve;

import java.net.URL;
import java.util.*;

import static com.calculator.calculation.ProbabilityController.isInteger;
import static com.calculator.calculation.ProbabilityController.showAlert;

import DiscreteMath.Circle;

/**
 * @author 郑悦
 * @Description: 处理离散数学计算器的点击事件
 * @date 2023/11/26 20:49
 */
public class DiscreteMathController implements Initializable {
    protected static LinkedHashMap<Integer, VectorSolve> historyVector = new LinkedHashMap<>();
    public TextField FunctionName;
    public TextField inputName;
    public TextField outputName;
    public Button ButtonRemove;
    public Button ButtonRename;
    public TableView<String> variableInCnt;
    public TableView<String> variableOutCnt;
    public Button ButtonAdd;
    public Label error;
    public Button ButtonInput;
    public Button ButtonOutput;
    public Button ButtonTable;
    public Button ButtonExpression;
    private static boolean flag = false;
    public TextArea ExpressionInput;
    public Button ButtonClear;
    public Button ButtonEnter;
    public TableView<String> ValueTable;
    public TableColumn<String, String> inputNameColumn;
    public Pane Input;
    public Pane Table;
    public Pane Expression;
    public Pane Output;
    public Button ButtonAdd0;
    public Button ButtonRename0;
    public Button ButtonRemove0;
    public TableColumn<String, String> outputNameColumn;
    public AnchorPane DMPane;
    public Pane MinTreePane;
    public Button ButtonGenerateGraph;
    @FXML
    public Canvas CanvasShowGraph;
    public TableView<ObservableList<String>> GraphTable;
    public Button ButtonGraphDataEnter;
    public TextField minAns;
    public Pane ShortPathPane;
    public Button ButtonDataConfirm;
    public TableView GraphPathTable;
    public TextField sourceInputText;
    public TextField numInputText;
    public TextField destInputText;
    public TextField costOutputText;
    public TextField PointNumText;
    public AnchorPane GraphPane;
    @FXML
    private StackPane DiscreteMathCardContainer;
    @FXML
    private StackPane DiscreteMathBooleanCardContainer;
    @FXML
    private StackPane DiscreteMathGraphCardContainer;

    static int MAX = 1000;
    int pointNum = MAX; // 记录目前图论计算的点的个数
    private boolean flagConfirmData; // 记录是否确认输入数据

    // 最小生成树问题
    private boolean flagHasMTSolve = false;
    GraphOfMatrix MTGraphSolve = new GraphOfMatrix(MAX, true);

    // 最短路径问题
    private boolean flagHasSPSolve = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        DMPane.setStyle("-fx-background-color: BEIGE;");
        if(!flag) {
            flag = true;
            BooleanAlgebraShift();
        }
    }

    public void BooleanAlgebraShift() {
        loadPage("DiscreteMathBoolean.fxml");
        FunctionName.setText("Boolean Algebra");
    }
    public void GraphTheoryShift() {
        loadPage("DiscreteMathGraph.fxml");
//        MinTreePane.setVisible(true);
//        ShortPathPane.setVisible(false);
        FunctionName.setText("Graph Theory");
    }

    /**
     * @Description 加载输入变量定义页面
     * @param actionEvent
     * @author 郑悦
     * @date 2023/11/28 10:33
    **/
    public void InputShift(ActionEvent actionEvent) {
        loadPage("DiscreteMathInput.fxml");
    }
    /**
     * @Description 加载输出变量名字的定义页面
     * @param actionEvent
     * @author 郑悦
     * @date 2023/11/28 10:34
    **/
    public void OutputShift(ActionEvent actionEvent) {
        loadPage("DiscreteMathOutput.fxml");
    }
    /**
     * @Description 加载真值表对应页面
     * @param actionEvent
     * @author 郑悦
     * @date 2023/11/27 11:02
    **/
    public void TableShift(ActionEvent actionEvent) {
        loadPage("DiscreteMathTable.fxml");
    }
    /**
     * @Description 加载逻辑表达式对应页面
     * @param actionEvent
     * @author 郑悦
     * @date 2023/11/27 11:02
    **/
    public void ExpressionShift(ActionEvent actionEvent) {
        loadPage("DiscreteMathExpression.fxml");
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
            DiscreteMathCardContainer.getChildren().setAll(page);
        } catch (Exception e) {
            System.out.println("error");
        }
    }
    /**
     * @Description 存储用户输入的表达式变量
     * @param
     * @author 郑悦
     * @date 2023/11/28 23:53
     **/
    @FXML
    private void handleInputName() {
        String newValue = inputName.getText();
        if (newValue.isEmpty()) {
            System.out.println("文本框为空");
        } else {
            System.out.println("文本框内容为: " + newValue);
        }
    }
    @FXML
    private void handleOutputName() {
        String newValue = outputName.getText();
        if (newValue.isEmpty()) {
            System.out.println("文本框为空");
        } else {
            System.out.println("文本框内容为: " + newValue);
        }
    }
    private void loadBoolPage(String fxmlFileName) {
        try {
            Pane page = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFileName)));
            DiscreteMathBooleanCardContainer.getChildren().setAll(page);
        } catch (Exception e) {
            System.out.println("error");
        }
    }
    @FXML
    private void BoolShift(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        switch (buttonText) {
            case "Input" -> {
                Input.setVisible(true);
                Output.setVisible(false);
                Expression.setVisible(false);
                Table.setVisible(false);
//                ButtonChangeOn();
//                loadBoolPage("DiscreteMathInput.fxml");
            }
            case "Output" -> {
                Output.setVisible(true);
                Input.setVisible(false);
                Expression.setVisible(false);
                Table.setVisible(false);
//                ButtonChangeOn();
//                loadBoolPage("DiscreteMathOutput.fxml");
            }
            case "Table" -> {
                Table.setVisible(true);
                Input.setVisible(false);
                Expression.setVisible(false);
                Output.setVisible(false);
//                ButtonChangeOff();
//                 loadBoolPage("DiscreteMathTable.fxml");
            }
            case "Expression" -> {
                Expression.setVisible(true);
                Input.setVisible(false);
                Table.setVisible(false);
                Output.setVisible(false);
//                ButtonChangeOff();
//                 loadBoolPage("DiscreteMathExpression.fxml");
            }
        }
    }
    private void ButtonChangeOn() {
        ButtonRemove.setVisible(true);
        ButtonRename.setVisible(true);
        ButtonAdd.setVisible(true);
    }
    private void ButtonChangeOff() {
        ButtonRemove.setVisible(false);
        ButtonRename.setVisible(false);
        ButtonAdd.setVisible(false);
    }

    public void setNewInputVariable(ActionEvent newInputVariable) {
        String newInputVar = inputName.getText();
        // 存储新的自变量，并将其添加到上述表格中
    }
    /**
     * @Description 存储用户输入的因变量名称
     * @param newOutputVariable
     * @author 郑悦
     * @date 2023/11/28 23:58
    **/
    public void setNewOutputVariable(ActionEvent newOutputVariable) {
        String newOutputVar = outputName.getText();
        // 存储新的自变量，并将其添加到上述表格中
        ObservableList<String> data = FXCollections.observableArrayList(
                newOutputVar
        );
        variableInCnt.setItems(data);
    }
    /**
     * @Description 存储用户输入的对应的输出的表达式
     * @param newInputExpression
     * @author 郑悦
     * @date 2023/11/29 0:02
    **/
    public void inputExpression(ActionEvent newInputExpression) {
        String newExpression = ExpressionInput.getText();
    }

    public void MinTreeShift(ActionEvent actionEvent) {
        MinTreePane.setVisible(true);
        ShortPathPane.setVisible(false);
    }

    public void ShortPathShift(ActionEvent actionEvent) {
        MinTreePane.setVisible(false);
        ShortPathPane.setVisible(true);
    }

    public void handleInputSource(ActionEvent actionEvent) {
    }

    public void handleInputNum(KeyEvent actionEvent) {
    }

    public void handleCostOutput(ActionEvent actionEvent) {
    }

    public void handleDestKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String input = destInputText.getText();
            int dest;
            // 获取数据到数组
            try {
                dest = getDest(input);
            } catch (NotInputDataException e) {
                // 展示javafx的即时提示词
                showAlert("错误提示", "未输入数据", 2000);
                return; // 不return的话catch完会继续执行方法剩下的语句
            }
            if (flagHasSPSolve) {
                // 显示从源点到destination需要的最小代价——不能到达显示-1
            }
        }
    }

    private int getDest(String input) throws NotInputDataException {
        if (input.isEmpty()) {
            throw new NotInputDataException("未输入数据");
        }
        if (isInteger(input)) {
            int dest = Integer.parseInt(input);
            if (dest > 0)
                return Integer.parseInt(input);
            else
                showAlert("错误提示", "请输入正整数", 2400);
        } else {
            showAlert("错误提示", "请输入正整数", 2400);
        }
        return -1;
    }

    public void handleMTPointNumKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String input = PointNumText.getText();
            int num;
            try {
                num = getDest(input);
            } catch (NotInputDataException e) {
                // 展示javafx的即时提示词
                showAlert("错误提示", "未输入数据", 2000);
                return; // 不return的话catch完会继续执行方法剩下的语句
            }
            pointNum = num;
            // 输入正确，初始化输入图矩阵的Table_注意先初始化点的名称再进行数据输入
            TableColumn<ObservableList<String>, String> columnName = new TableColumn<>("Point Name");
            columnName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
            columnName.setCellFactory(TextFieldTableCell.forTableColumn());
            GraphTable.getColumns().add(columnName);
            columnName.setEditable(false);
            // 创建列并设置列名
            for (int j = 1; j <= num; j++) {
                TableColumn<ObservableList<String>, String> column = new TableColumn<>("Point " + (char) ('a' + j -1));
                final int columnIndex = j;
                column.setCellValueFactory(data -> {
                    ObservableValue<String> cellValue = new ReadOnlyObjectWrapper<>(data.getValue().get(columnIndex));
                    cellValue.addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            int rowIndex = GraphTable.getItems().indexOf(data.getValue());
                            // 先判断输入是否合法
                            if (!isInteger(newValue)) {
                                showAlert("错误提示", "请输入整数", 2400);
                                return;
                            }
                            // 用函数记录改变值
                            MTGraphSolve.addEdgeUseIndex(rowIndex, columnIndex - 1, Integer.parseInt(newValue));
                            System.out.println("Cell [" + rowIndex + "," + columnIndex + "] changed from " + oldValue + " to " + newValue);
                            data.getValue().set(columnIndex, newValue); // 更新数据源中对应单元格的值
                            // de只能改一个表格，之后被javafx覆盖改变的bug
                        }
                    });
                    return cellValue;
                });
                column.setCellFactory(TextFieldTableCell.forTableColumn());
                GraphTable.getColumns().add(column);
            }
            // 创建自定义数据表格
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            for (int i = 0; i < num; i++) { // i代表行
                ObservableList<String> rowData = FXCollections.observableArrayList();
                for (int j = 0; j <= num; j++) {
                    rowData.add(""); // 初始化为空数据
                }
                data.add(rowData);
            }
            for (int i = 0; i < num; i++) {
                StringBuffer str = new StringBuffer();
                str.append("Point ");
                str.append((char) ('a'+i));
//                str.append('\0');
                data.get(i).set(0, str.toString());
            }
            for (int i = 0; i < num; i++) { // i代表行
                for(int j = 1; j <= num; j++) { // 第一列写点的名字了
                    data.get(i).set(j, "∞");
                }
            }
            GraphTable.setItems(data);
            // 图数据输入完之后要改成不可更改
            GraphTable.setEditable(true);
            columnName.setEditable(false);
            // 进行一个等数据量的图的建立以及相应的处理最小生成树模型的建立
            flagHasMTSolve = true;
            MTGraphSolve = new GraphOfMatrix(num, true); // bool判断是否是有向图
            char[] charArray = new char[num];
            for (int i = 0; i < num; i++) {
                charArray[i] = (char) ('a'+i);
            }
            MTGraphSolve.initArray(charArray);
        }
    }

    public void handleMTDataConfirm(ActionEvent keyEvent) {
        if (!flagHasMTSolve) {
            showAlert("错误提示", "请先完成图的构造");
            return;
        }
        flagConfirmData = true;
        int ans;
        GraphOfMatrix kminTree = new GraphOfMatrix(pointNum,true);
        try {
            ans = MTGraphSolve.kruskal(kminTree);
        } catch (RuntimeException e) {
            showAlert("错误提示", "没有最小生成树");
            minAns.setText("无最小生成树");
            return;
        }
        if (ans >= 2000000) {
            showAlert("错误提示", "没有最小生成树");
            minAns.setText("无最小生成树");
            return;
        }
        minAns.setText(String.valueOf(ans));
//        System.out.println(MTGraphSolve.kruskal(kminTree));
//        kminTree.printGraph();
    }


    public void handleShowMTree(ActionEvent actionEvent) {
        if (!flagHasMTSolve) {
            showAlert("错误提示", "请先完成图的构造");
            return;
        }
        if (!flagConfirmData) {
            showAlert("错误提示", "请先确认输入数据完毕");
            return;
        }
        // 显示最小生成树构造的动画
        Pane root = GraphPane;
//        Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);
//        primaryStage.setScene(scene);
//        primaryStage.show();

//        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
//        root.getChildren().add(CanvasShowGraph);

        double CANVAS_WIDTH = CanvasShowGraph.getWidth();
        double CANVAS_HEIGHT = CanvasShowGraph.getHeight();

        Label label = new Label();
        root.getChildren().add(label);

        VBox buttonBox = new VBox();
        buttonBox.setTranslateX(10);
        buttonBox.setTranslateY(10);
        root.getChildren().add(buttonBox);

        Button drawLineButton = new Button("Draw Line");
        drawLineButton.setOnAction(event -> drawLine());
        buttonBox.getChildren().add(drawLineButton);

        gc = CanvasShowGraph.getGraphicsContext2D();
        gc.setFill(CIRCLE_COLOR);
        gc.setStroke(CIRCLE_COLOR);

        // 第一步画点
        for (int i = 0; i < pointNum; i++) {
            double x = generateRandomX(CANVAS_WIDTH);
            double y = generateRandomY(CANVAS_HEIGHT);

            while (isOverlapping(x, y)) {
                x = generateRandomX(CANVAS_WIDTH);
                y = generateRandomY(CANVAS_HEIGHT);
            }

            Circle circle = new Circle(x, y, 10);
            circles.add(circle);
            gc.fillOval(x - circle.getRadius(), y - circle.getRadius(), circle.getRadius() * 2, circle.getRadius() * 2);

            String name = (char) ('a' + i) + "";
            Label nameLabel = new Label(name);
            nameLabel.setTextFill(LABEL_COLOR);
            nameLabel.setLayoutX(x - nameLabel.getWidth() / 2 + 10);
            nameLabel.setLayoutY(CanvasShowGraph.getLayoutY() + GraphPane.getLayoutY() + y - circle.getRadius() + 25);
//            nameLabel.setLayoutY(CanvasShowGraph.getLayoutY() + GraphPane.getLayoutY() + y - circle.getRadius() - nameLabel.getHeight());
            root.getChildren().add(nameLabel);
        }

        MTGraphSolve.getLineFromTo(circles);

        // 按点连线
        final int[] pCnt = {0};
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pCnt[0] < pointNum - 1) {
                    int cur = MTGraphSolve.From[pCnt[0]];
                    int next = MTGraphSolve.To[pCnt[0]];
                    Path path = new Path();
                    path.setStroke(Color.BLUE);
                    path.setStrokeWidth(5);
                    path.getElements().add(new MoveTo(
                            circles.get(next).getCenterX() + CanvasShowGraph.getLayoutX(),
                            circles.get(next).getCenterY() + CanvasShowGraph.getLayoutY() + GraphPane.getLayoutY() + 28));
                    path.getElements().add(new LineTo(
                            circles.get(cur).getCenterX() + CanvasShowGraph.getLayoutX() + GraphPane.getLayoutX(),
                            circles.get(cur).getCenterY() + CanvasShowGraph.getLayoutY() + GraphPane.getLayoutY() + 28));
                    GraphPane.getChildren().add(path);
                    pCnt[0]++;
                    System.out.println("draw");
                }
            }
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private double generateRandomX(double CANVAS_WIDTH) {
        return Math.random() * (CANVAS_WIDTH - 20) + 10; // 保持一定的边距
    }

    private double generateRandomY(double CANVAS_HEIGHT) {
        return Math.random() * (CANVAS_HEIGHT - 20) + 10; // 保持一定的边距
    }

    private boolean isOverlapping(double x, double y) {
        for (Circle circle : circles) {
            double distance = Math.sqrt(Math.pow(circle.getCenterX() - x, 2) + Math.pow(circle.getCenterY() - y, 2));
            if (distance < circle.getRadius() * 2) {
                return true;
            }
        }
        return false;
    }

    private static final Color CIRCLE_COLOR = Color.RED;
    private static final Color LABEL_COLOR = Color.BLACK;

    private List<Circle> circles = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();

//    private Canvas canvas;
    private GraphicsContext gc;

    private void drawLine() {
        if (lines.size() < 2) {
            lines.add(new Line(circles.get(0).getCenterX(), circles.get(0).getCenterY()));
            lines.add(new Line(circles.get(1).getCenterX(), circles.get(1).getCenterY()));
            drawLine(gc, lines.get(0), lines.get(1));
        }
    }

    private void drawLine(GraphicsContext gc, Line start, Line end) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
    }


    private static class Line {
        private double x;
        private double y;

        public Line(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}
