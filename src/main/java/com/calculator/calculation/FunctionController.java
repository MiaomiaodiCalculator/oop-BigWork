package com.calculator.calculation;

import NewFunction.UserFunction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import scientific.ScientificSolve;

import java.net.URL;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author sxq
 * @Description:自定义函数的控制器
 * @date 2023/11/26 15:09
 */
public class FunctionController implements Initializable {
    public Button BUTTON_paraX;
    public Button BUTTON_paraY;
    public Button BUTTON_paraZ;
    public TextField functionShow;
    public TextField functionName;
    @FXML
    private StackPane cardContainer;

    @FXML
    private ImageView historyImg;
    @FXML
    private ChoiceBox<Integer> choiceBox;
    private Integer[] paraNum={1,2,3};
    /*自定义函数名*/
    protected String name="";
    /*展示给用户的表达式*/
     private String formula="";
     /*后台用于计算的表达式*/
    protected String exp="";

    @FXML
        void handleHisImageClick(MouseEvent event) {

        }
        /**
         * @Description 处理普通符号按钮点击事件
 * @param event
         * @author sxq
         * @date 2023/11/27 16:42
        **/
    @FXML
    private void handleButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        editFormula(buttonText);
        functionShow.setText(formula);
    }
    /**
     * @Description 处理点击保存事件，包括判断名称合法性、函数表达式合法性等
 * @param event
     * @author sxq
     * @date 2023/11/27 16:44
    **/
    @FXML
    private void handleSaveClick(ActionEvent event){
        String judgeName=UserFunction.judgeName(functionName.getText());
        System.out.println(functionName.getText());
        if(!judgeName.equals("true")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("出错了");
            alert.setHeaderText(null);
            alert.setContentText(judgeName);
            alert.showAndWait();
            return;
        }
        //判断名称合法性
        if(UserFunction.judgeNameRepeat(functionName.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("出错了");
            alert.setHeaderText(null);
            alert.setContentText("该函数名已存在");
            alert.showAndWait();
            return ;
        }
        //判断是否重复
        if(!UserFunction.judgeFunction(exp).equals("true")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("出错了");
            alert.setHeaderText(null);
            alert.setContentText(UserFunction.judgeFunction(exp));
            alert.showAndWait();
            return ;
        }
        //判断表达式合法性
        UserFunction function=new UserFunction(functionName.getText(),exp,formula);
        if(function.addFunction()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("创建了一个新函数！");
            alert.setHeaderText("函数名："+functionName.getText());
            alert.setContentText("表达式："+formula);
            alert.showAndWait();
        }
    }
    /**
     * @Description 编辑自定义函数的文本
 * @param str
     * @author sxq
     * @date 2023/11/26 16:00
    **/
    public void editFormula(String str){
        switch(str){
            case "0": case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": case "(": case ")": case "+": case "-": case ".": case "!":
                formula=formula+str;
                exp+=str;
                break;
            case "×":
                formula=formula+"*";
                exp+="*";
                break;
            case "÷":
                formula=formula+"/";
                exp+="/";
                break;
            case "mod":
                formula=formula+"%";
                exp+="%";
                break;
            case "log10": case "log": case "log2", "sin", "cos", "tan", "sec", "csc", "cot":
                formula=formula+str+"(";
                exp+=str+"(";
                break;
            case "10ˣ":
                formula=formula+"10^(";
                exp+="10^(";
                break;
            case "xⁿ":
                formula=formula+"^(";
                exp+="^(";
                break;
            case "√":
                formula=formula+"sqrt(";
                exp+="sqrt(";
                break;
            case "³√":
                formula=formula+"^(1/3)";
                exp+="^(1/3)";
                break;
            case "eˣ":
                formula=formula+"e(";
                exp+="e(";
                break;
            case "x³":
                formula=formula+"^3";
                exp+="^3";
                break;
            case "π":
                formula=formula+"pi";
                exp+="pi";
                break;
            case "e":
                formula=formula+"e";
                exp+="e";
                break;
            case "x²":
                formula=formula+"^2";
                exp+="^2";
                break;
            case "C":
                formula="";
                exp="";
                break;
            case "x":
                formula=formula+"x";
                exp+="$x$";
                break;
            case "y":
                formula=formula+"y";
                exp+="$y$";
                break;
            case "z":
                formula=formula+"z";
                exp+="$z$";
                break;
            case "pow":
                formula=formula+"pow(";
                exp+="pow(";
                break;
            case "保存":
                //TODO 保存新的自定义函数
                break;
            default:
                if(Character.isLetter(formula.charAt(str.length() - 1))){
                    formula=formula.substring(0,formula.length()-3);
                }else{
                    formula=formula.substring(0,formula.length()-1);
                }
                break;
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll(paraNum);
        choiceBox.setValue(1);
        choiceBox.setOnAction(this::getParaNum);
    }
    /**
     * @Description 获取参数个数并禁用自变量按钮；清空输入区
     * @param e
     * @author sxq
     * @date 2023/11/26 16:21
     **/
    public Integer getParaNum(ActionEvent e){
        int num= choiceBox.getValue();
        formula="";
        switch (num) {
            case 1 -> {
                BUTTON_paraX.setDisable(false);
                BUTTON_paraY.setDisable(true);
                BUTTON_paraZ.setDisable(true);
            }
            case 2 -> {
                BUTTON_paraX.setDisable(false);
                BUTTON_paraY.setDisable(false);
                BUTTON_paraZ.setDisable(true);
            }
            case 3 -> {
                BUTTON_paraX.setDisable(false);
                BUTTON_paraY.setDisable(false);
                BUTTON_paraZ.setDisable(false);
            }
        }
        return num;
    }


}
