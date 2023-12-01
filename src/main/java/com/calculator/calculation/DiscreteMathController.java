package com.calculator.calculation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import scientific.ScientificSolve;
import vector.VectorSolve;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author 郑悦
 * @Description: 处理离散数学计算器的点击事件
 * @date 2023/11/26 20:49
 */
public class DiscreteMathController implements Initializable {
    protected static LinkedHashMap<Integer, VectorSolve> historyVector = new LinkedHashMap<>();
    public static int cntHistory = 0;
    public ImageView historyImg;
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
    public Button ButtonMinimized;
    private static boolean flag = false;
    public MenuButton MinimizedOutputMenu;
    public MenuButton MinimizedFormatMenu;
    public TableView<String> MinimizedTable;
    public TextArea ExpressionInput;
    public Button ButtonClear;
    public Button ButtonEnter;
    public TableView<String> ValueTable;
    public TableColumn<String, String> inputNameColumn;
    public ImageView returnImg;
    public Pane History;
    public Pane Input;
    public Pane Table;
    public Pane Expression;
    public Pane Minimized;
    public MenuButton MinimizedOutputMenuMini;
    public Pane Output;
    public Button ButtonAdd0;
    public Button ButtonRename0;
    public Button ButtonRemove0;
    public TableColumn outputNameColumn;
    @FXML
    private StackPane DiscreteMathCardContainer;
    @FXML
    private StackPane DiscreteMathBooleanCardContainer;
    @FXML
    private StackPane DiscreteMathGraphCardContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        FunctionName.setText("Graph Theory");
    }

    public void CriticalPathShift() {
        loadPage("CriticalPath.fxml");
    }

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
        FunctionName.setText("Boolean Algebra");
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
     * @Description 加载化简表达式页面
     * @param actionEvent
     * @author 郑悦
     * @date 2023/11/28 10:31
    **/
    public void MinimizedShift(ActionEvent actionEvent) {
        loadPage("DiscreteMathMinimized.fxml");
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
                Minimized.setVisible(false);
//                ButtonChangeOn();
//                loadBoolPage("DiscreteMathInput.fxml");
            }
            case "Output" -> {
                Output.setVisible(true);
                Input.setVisible(false);
                Expression.setVisible(false);
                Table.setVisible(false);
                Minimized.setVisible(false);
//                ButtonChangeOn();
//                loadBoolPage("DiscreteMathOutput.fxml");
            }
            case "Table" -> {
                Table.setVisible(true);
                Input.setVisible(false);
                Expression.setVisible(false);
                Output.setVisible(false);
                Minimized.setVisible(false);
                ButtonChangeOff();
//                 loadBoolPage("DiscreteMathTable.fxml");
            }
            case "Expression" -> {
                Expression.setVisible(true);
                Input.setVisible(false);
                Table.setVisible(false);
                Output.setVisible(false);
                Minimized.setVisible(false);
                ButtonChangeOff();
//                 loadBoolPage("DiscreteMathExpression.fxml");
            }
            case "Minimized" -> {
                Minimized.setVisible(true);
                Input.setVisible(false);
                Table.setVisible(false);
                Output.setVisible(false);
                Expression.setVisible(false);
                ButtonChangeOff();
//                 loadBoolPage("DiscreteMathMinimized.fxml");
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

}
