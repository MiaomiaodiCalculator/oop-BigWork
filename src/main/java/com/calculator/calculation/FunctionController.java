package com.calculator.calculation;

import NewFunction.UserFunction;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import scientific.ScientificSolve;

import java.net.URL;
import java.util.*;

/**
 * @author sxq
 * @Description:自定义函数的控制器
 * @date 2023/11/26 15:09
 */
public class FunctionController implements Initializable {
    public Button BUTTON_paraX;
    public Button BUTTON_paraY;
    public Button BUTTON_paraZ;
    public Button BUTTON_USERFUNCTION;
    public Button buttonPow;
    public TextField functionShow;
    public TextField functionName;
    public TableView FunctionList;
    public TableColumn nameList;
    public TableColumn paraNumList;
    public TableColumn formulaList;
    @FXML
    private StackPane cardContainer;
    @FXML
    private ChoiceBox<Integer> choiceBox;
    private Integer[] paraNum={1,2,3};
    /*自定义函数名*/
    protected String name="";
    /*展示给用户的表达式*/
     private String formula="";
     /*后台用于计算的表达式*/
    protected String exp="";
    /*储存formula的编辑过程*/
    private List<String> formulaProcess=new ArrayList<>();
    /*储存exp的编辑过程*/
    private List<String> expProcess =new ArrayList<>();
    public static ArrayList<UserFunction> functionList=new ArrayList<>();
    /*pow判断，以切换显示*/
    public static boolean atPow=false;
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
        if(atPow){
            buttonPow.setText(",");
        }else{
            buttonPow.setText("pow");
        }
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
        function.setParaNum(choiceBox.getValue());
        if(function.addFunction()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("创建了一个新函数！");
            alert.setHeaderText("函数名："+functionName.getText());
            alert.setContentText("表达式："+formula);
            alert.showAndWait();
            FunctionList.getItems().add(functionList.get(functionList.size()-1));
            formula="";
            exp="";
            formulaProcess.clear();
            expProcess.clear();
        }
    }
    /**
     * @Description 点击自定义函数按钮，获取已有的函数列表，并转换表达式和函数式
 * @param event        
     * @author sxq
     * @date 2023/11/27 20:41
    **/
    @FXML
    private void handleGetFunction(ActionEvent event){
        FunctionList.setVisible(!FunctionList.isVisible());//点击显示
    }
    /**
     * @Description 编辑自定义函数的文本
 * @param str
     * @author sxq
     * @date 2023/11/26 16:00
    **/
    public void editFormula(String str){
        switch(str){
            case "0": case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": case "(": case "+": case "-": case ".": case "!":
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
                formula=formula+"exp(";
                exp+="exp(";
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
                atPow=true;
                break;
            case ",":
                formula=formula+",";
                exp+=",";
                break;
            case ")":
                formula=formula+")";
                exp=exp+")";
                if(atPow){
                    atPow=checkPow();
                }
                break;
            case "←"://删除键
                if(!formula.isEmpty()&&!exp.isEmpty()){//表达式非空，回退到上一步
                    if(formula.contains("pow") && !formulaProcess.get(formulaProcess.size()-2).contains("pow")){
                       //上一步是pow
                        atPow=false;
                    }
                    formula=formulaProcess.get(formulaProcess.size()-2);
                    exp=expProcess.get(expProcess.size()-2);
                    formulaProcess.remove(formulaProcess.size()-1);
                    expProcess.remove(formulaProcess.size()-1);
                }
                return;
            default:
                System.out.println("按钮"+str+"未设置");
                return;
        }
        formulaProcess.add(formula);
        expProcess.add(exp);
    }
    /**
     * @Description 检查pow函数是否调用完成
 * @return boolean
     * @author sxq
     * @date 2023/11/28 17:12
    **/

    private boolean checkPow() {
        int index=formula.lastIndexOf("pow");
        if(index==-1)return false;
        int bracket=0;
        for(index=index+3;index<formula.length();index++){
            if(formula.charAt(index)=='(')bracket++;
            else if(formula.charAt(index)==')')bracket--;
        }
        if(bracket>0)return true;
        else return false;
    }

    /**
     * @Description 初始化参数选择框、函数列表
 * @param url
 * @param resourceBundle
     * @author sxq
     * @date 2023/11/27 22:49
    **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll(paraNum);
        choiceBox.setValue(1);
        choiceBox.setOnAction(this::getParaNum);
        FunctionList.setPlaceholder(new Label("脑瓜子空空的"));//占位文本
        nameList.setCellValueFactory(new PropertyValueFactory<>("name"));
        paraNumList.setCellValueFactory(new PropertyValueFactory<>("paraNum"));
        formulaList.setCellValueFactory(new PropertyValueFactory<>("formula"));
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

    /**
     * @Description 处理历史自定义函数的点击事件
 * @param mouseEvent
     * @author sxq
     * @date 2023/11/28 17:23
    **/
    public void handleRowClick(MouseEvent mouseEvent) {

    }
}
