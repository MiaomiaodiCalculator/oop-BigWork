package com.calculator.calculation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private StackPane cardContainer;
    private static boolean flag=false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!flag) {
            flag = true;
            ScientificShift();
        }
        System.out.println("1!!");
    }
    /***
     * @Description  加载科学计算器卡片布局
     * @author Bu Xinran
     * @date 2023/11/26 11:18
     **/
    @FXML
    private void ScientificShift() {
        loadPage("Scientific.fxml");
    }
    /***
     * @Description  加载向量计算器卡片布局
     * @author Bu Xinran
     * @date 2023/11/26 11:18
     **/
    @FXML
    private void VectorShift(){
        loadPage("Vector.fxml");
    }
    /**
     * @Description 加载自定义函数页面
     * @author sxq
     * @date 2023/11/26 15:18
     **/
    @FXML
    private void FunctionShift() {loadPage("Function.fxml");}
    /***
     * @Description 加载方程式计算器卡片布局
     * @author Bu Xinran
     * @date 2023/11/28 10:21
     **/
    @FXML
    public void EquationShift() {loadPage("Equation.fxml");}
    /**
     * @Description   加载单位换算页面
     * @author ZhouYH
     * @date 2023/11/27 19:37
     **/
    @FXML
    public void UnitTranslation() {loadPage("UnitTranslation.fxml");}
    /***
     * @Description  加载页面的函数
     * @param fxmlFileName 文件名称
     * @author Bu Xinran
     * @date 2023/11/26 11:18
     **/
    private void loadPage(String fxmlFileName) {
        try {
            Pane page = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFileName)));
            cardContainer.getChildren().setAll(page);
        } catch (Exception e) {
            System.out.println("error");
        }
    }

}