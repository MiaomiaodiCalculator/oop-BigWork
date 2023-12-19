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
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

import static DiscreteMath.GraphOfMatrix.INF;
import static com.calculator.calculation.ProbabilityController.isInteger;
import static com.calculator.calculation.ProbabilityController.showAlert;

import DiscreteMath.Circle;

/**
 * @author 郑悦
 * @Description: 处理离散数学计算器的点击事件
 * @date 2023/11/26 20:49
 */
public class DiscreteMathController implements Initializable {
    public Label error;
    public static boolean flagDM = false;
    public AnchorPane DMPane;
    public Button ButtonGenerateGraph;
    @FXML
    public Canvas CanvasShowGraph;
    public TableView<ObservableList<String>> GraphTable;
    public Button ButtonGraphDataEnter;
    public TextField minAns;
    public TextField PointNumText;
    public AnchorPane GraphPane;
    public Button detail;
    public TextFlow textFlow;
    @FXML
    private StackPane DiscreteMathCardContainer;

    static int MAX = 1000;
    int pointNum = MAX; // 记录目前图论计算的点的个数
    private boolean flagConfirmData; // 记录是否确认输入数据

    // 最小生成树问题
    private boolean flagHasMTSolve = false;
    GraphOfMatrix MTGraphSolve = new GraphOfMatrix(MAX, true);
    /**
     * @Description 页面初始化
     * @param location
     * @param resources
     * @author 郑悦
     * @date 2023/12/18 21:51
    **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!flagDM) {
            flagDM = true;
            loadPage("DiscreteMathGraph.fxml");
        }
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
     * @Description 获取到达点
     * @param input
     * @return int
     * @author 郑悦
     * @date 2023/12/18 21:48
    **/
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
    /**
     * @Description 获取生成图的顶点个数
     * @param keyEvent
     * @author 郑悦
     * @date 2023/12/18 21:47
    **/
    public void handleMTPointNumKeyPressed(KeyEvent keyEvent) {
        // 清空原本页面
        GraphTable.getColumns().clear();
        minAns.clear();
        CanvasShowGraph.getGraphicsContext2D().clearRect(0, 0, CanvasShowGraph.getWidth(), CanvasShowGraph.getHeight());
        circles.clear();
        lines.clear();
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
//                            System.out.println("Cell [" + rowIndex + "," + columnIndex + "] changed from " + oldValue + " to " + newValue);
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
    /**
     * @Description 处理构造图
     * @param keyEvent
     * @author 郑悦
     * @date 2023/12/18 21:47
    **/
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

    private List<Path> pathes = new ArrayList<>();
    /**
     * @Description 展示最小生成树过程
     * @param actionEvent
     * @author 郑悦
     * @date 2023/12/18 21:47
    **/
    public void handleShowMTree(ActionEvent actionEvent) {
        if (!flagHasMTSolve) {
            showAlert("错误提示", "请先完成图的构造");
            return;
        }
        if (!flagConfirmData) {
            showAlert("错误提示", "请先确认输入数据完毕");
            return;
        }
        if (MTGraphSolve.getTreeWeight() >= INF) {
            showAlert("错误提示", "没有最小生成树");
            return;
        }
        // 显示最小生成树构造的动画
        Pane root = GraphPane;

        double CANVAS_WIDTH = CanvasShowGraph.getWidth();
        double CANVAS_HEIGHT = CanvasShowGraph.getHeight();

        Label label = new Label();
        root.getChildren().add(label);

        VBox buttonBox = new VBox();
        buttonBox.setTranslateX(10);
        buttonBox.setTranslateY(10);
        root.getChildren().add(buttonBox);

        gc = CanvasShowGraph.getGraphicsContext2D();
        // 清空
        circles.clear();
        pathes.clear();
        gc.clearRect(0, 0, CanvasShowGraph.getWidth(), CanvasShowGraph.getHeight());
        gc.setFill(CIRCLE_COLOR);
        gc.setStroke(CIRCLE_COLOR);

        // 第一步画点
        for (int i = 0; i < pointNum; i++) {
            double x = generateRandomX(CANVAS_WIDTH);
            double y = generateRandomY(CANVAS_HEIGHT);

            int count = 0;   // 防止找不到空白处陷入死循环
            while (isOverlapping(x, y)) {
                count++;
                if (count > 400) {
                    showAlert("错误提示", "点数太多啦，显示失败，再试其他数据吧");
                    return;
                }
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
            root.getChildren().add(nameLabel);
        }

        MTGraphSolve.getLineFromTo(circles);

        // 按点连线
        final int[] pCnt = {0};
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1200), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pCnt[0] < pointNum - 1) {
                    int cur = MTGraphSolve.From[pCnt[0]];
                    int next = MTGraphSolve.To[pCnt[0]];
                    Path path = new Path();
                    path.setStroke(Color.BLACK);
                    path.setStrokeWidth(2);
                    path.getElements().add(new MoveTo(
                            circles.get(next).getCenterX() + CanvasShowGraph.getLayoutX(),
                            circles.get(next).getCenterY() + CanvasShowGraph.getLayoutY() + GraphPane.getLayoutY()));
                    path.getElements().add(new LineTo(
                            circles.get(cur).getCenterX() + CanvasShowGraph.getLayoutX() + GraphPane.getLayoutX(),
                            circles.get(cur).getCenterY() + CanvasShowGraph.getLayoutY() + GraphPane.getLayoutY()));
                    GraphPane.getChildren().add(path);
                    pathes.add(path);
                    pCnt[0]++;
                }
            }
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
    /**
     * @Description 获取点将画的位置X
     * @param CANVAS_WIDTH
     * @return double
     * @author 郑悦
     * @date 2023/12/15 21:43
    **/
    private double generateRandomX(double CANVAS_WIDTH) {
        return Math.random() * (CANVAS_WIDTH - 20) + 10; // 保持一定的边距
    }
    /**
     * @Description 获取点将画的位置Y
     * @param CANVAS_HEIGHT
     * @return double
     * @author 郑悦
     * @date 2023/12/15 21:43
     **/
    private double generateRandomY(double CANVAS_HEIGHT) {
        return Math.random() * (CANVAS_HEIGHT - 30) + 5; // 保持一定的边距
    }
    /**
     * @Description 判断要画的位置点是否和已有点重合
     * @param x
     * @param y
     * @return boolean
     * @author 郑悦
     * @date 2023/12/15 21:44
    **/
    private boolean isOverlapping(double x, double y) {
        for (Circle circle : circles) {
            double distance = Math.sqrt(Math.pow(circle.getCenterX() - x, 2) + Math.pow(circle.getCenterY() - y, 2));
            if (distance < circle.getRadius() * 2) {
                return true;
            }
        }
        return false;
    }

    private static final Color CIRCLE_COLOR = Color.ORANGE;
    private static final Color LABEL_COLOR = Color.BLACK;

    private List<Circle> circles = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    private GraphicsContext gc;
    /**
     * @Description 构造传给实际画线函数的参数
     * @author 郑悦
     * @date 2023/12/15 21:46
    **/
    private void drawLine() {
        if (lines.size() < 2) {
            lines.add(new Line(circles.get(0).getCenterX(), circles.get(0).getCenterY()));
            lines.add(new Line(circles.get(1).getCenterX(), circles.get(1).getCenterY()));
            drawLine(gc, lines.get(0), lines.get(1));
        }
    }
    /**
     * @Description 画线
     * @param gc
     * @param start
     * @param end
     * @author 郑悦
     * @date 2023/12/15 21:45
    **/
    private void drawLine(GraphicsContext gc, Line start, Line end) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
    }
    /**
     * @Description 点击说明按钮
     * @param event
     * @author sxq
     * @date 2023/12/18 14:20
    **/
    public void clickDetail(ActionEvent event) {
        if(textFlow.isVisible()){
            textFlow.setVisible(false);
            detail.setText("说明");
            loadPage("DiscreteMathGraph.fxml");
        }
        else{
            textFlow.setVisible(true);
            detail.setText("收起");
        }
    }

    /**
     * @Description 创建线条内部类
     * @author 郑悦
     * @date 2023/12/18 21:45
    **/
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
