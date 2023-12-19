package com.calculator.calculation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import user.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static Database.SqlUser.add;
import static Database.SqlUser.exists;

public class MainController implements Initializable {
    public static MainController mainController;
    @FXML
    public ImageView person;
    @FXML
    public ImageView returnMain;
    @FXML
    private StackPane cardContainer;
    public static boolean flag=false;
    public static String present="Scientific";
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!flag) {
            flag = true;
            ScientificShift();
        }
        if(!LoginController.state){
            person.setVisible(false);
        }
    }
    /***
     * @Description  加载科学计算器卡片布局
     * @author Bu Xinran
     * @date 2023/11/26 11:18
     **/
    @FXML
    private void ScientificShift() {
        present="Scientific";
        loadPage("Scientific.fxml");
    }
    /***
     * @Description  加载向量计算器卡片布局
     * @author Bu Xinran
     * @date 2023/11/26 11:18
     **/
    @FXML
    private void VectorShift(){
        present="Vector";
        loadPage("Vector.fxml");
    }
    /**
     * @Description 加载自定义函数页面
     * @author sxq
     * @date 2023/11/26 15:18
     **/
    @FXML
    private void FunctionShift() {
        present="Function";
        loadPage("Function.fxml");}
    /**
     * @Description  加载离散数学卡片布局
     * @author 郑悦
     * @date 2023/11/26 21:22
     **/
    @FXML
    private void DiscreteMathShift() {
        present="DiscreteMath";
        loadPage("DiscreteMath.fxml");
        DiscreteMathController.flagDM = false;
    }
    /**
     * @Description 加载概率统计卡片布局
     * @author 郑悦
     * @date 2023/12/1 20:40
    **/
    @FXML
    private void ProbabilityShift() {
        present="Probability";
        loadPage("Probability.fxml");
    }
    /***
     * @Description 加载方程式计算器卡片布局
     * @author Bu Xinran
     * @date 2023/11/28 10:21
     **/
    @FXML
    public void EquationShift() {
        present="Equation";
        loadPage("Equation.fxml");}
    /**
     * @Description   加载单位换算页面
     * @author ZhouYH
     * @date 2023/11/27 19:37
     **/
    @FXML
    public void UnitTranslationShift() {
        present="UnitTranslation";
        loadPage("UnitTranslation.fxml");}
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
            returnMain.setVisible(false);
            person.setVisible(true);
            if(!LoginController.state)person.setVisible(false);
            if(!fxmlFileName.equals("Vector.fxml"))VectorController.flag=false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("loadPage:error");
        }
    }
    /**
     * @Description 加载微积分计算页面         
     * @author sxq
     * @date 2023/12/4 15:26
    **/
    @FXML
    public void InfinitesimalShift() {
        present="Infinitesimal";
        loadPage("Infinitesimal.fxml");
    }

    public static Stage PersonStage = new Stage();
    /**
     * @Description 布局美化+使用体验考虑改动个人中心的显示
     * @param mouseEvent
     * @author 郑悦
     * @date 2023/12/17 21:44
    **/
    @FXML
    public void goPersonalCenter(MouseEvent mouseEvent) throws IOException {
        Stage stage = PersonStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PersonCenter.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 355, 465);
        stage.setTitle("个人中心");
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
        Image cursorImage = new Image("cur.png");
        ImageCursor cursor = new ImageCursor(cursorImage);
        stage.setScene(scene);
        scene.setCursor(cursor);
        stage.show();
    }
    /***
     * @Description  返回到主界面
     * @param mouseEvent  点击事件
     * @author Bu Xinran
     * @date 2023/12/7 22:50
    **/
    @FXML
    public void goMain(MouseEvent mouseEvent) {
        person.setVisible(true);
        returnMain.setVisible(false);
        loadPage(present+".fxml");
    }
    /**
     * @Description   加载函数图像绘制页面
     * @author ZhouYH
     * @date 2023/11/27 19:37
     **/
    @FXML
    public void VisualizationShift() {
        present="Visualization";
        loadPage("Visualization.fxml");
    }

    public StackPane getCardContainer() {
        return cardContainer;
    }
}