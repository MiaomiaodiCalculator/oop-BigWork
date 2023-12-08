package com.calculator.calculation;

import Database.SqlFunction;
import NewFunction.UserFunction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.*;

/**
 * @author sxq
 * @Description 自定义函数的控制器
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
    private ChoiceBox<Integer> choiceBox;
    private final Integer[] paraNum = {1, 2, 3};
    String replacePara = "";//替换嵌套函数中的参数
    /*嵌套的子函数名*/
    private String sonFName = "";
    /*嵌套的子函数参数个数*/
    private int sonParaNum = 0;
    /*已经替代的子函数参数个数*/
    private int replacedNum = 0;
    /*自定义函数名*/
    protected String name = "";
    /*展示给用户的表达式*/
    private String formula = "";
    /*后台用于计算的表达式*/
    protected String exp = "";
    /*储存formula的编辑过程*/
    private final List<String> formulaProcess = new ArrayList<>();
    /*储存exp的编辑过程*/
    private final List<String> expProcess = new ArrayList<>();
    /*储存参数编辑过程*/
    private final ArrayList<String>[] replaceParaProcess = new ArrayList [3];
    public static ArrayList<UserFunction> functionList = SqlFunction.getAllFunction();
    /*pow判断，以切换显示*/
    public static boolean atPow = false;
    /*判断是否正在嵌套自定义函数*/
    public static boolean atFunc = false;

    /**
     * @param event
     * @Description 处理普通符号按钮点击事件
     * @author sxq
     * @date 2023/11/27 16:42
     **/
    @FXML
    private void handleButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        editFormula(buttonText);
        if (atPow) {
            buttonPow.setText(",");
        } else {
            buttonPow.setText("pow");
        }
        functionShow.setText(formula);
    }
    /**
     * @Description 处理点击删除键
     * @param event
     * @author sxq
     * @date 2023/12/4 10:09
     **/
    public void handleDelClick(ActionEvent event){
        if(!formula.isEmpty()&&!exp.isEmpty()){//表达式非空，回退到上一步
            if(!atFunc) {//不在嵌套自定义函数中
                if (formula.contains("pow") && !formulaProcess.get(formulaProcess.size() - 2).contains("pow")) {
                    //上一步是pow
                    atPow = false;
                }
                exp = expProcess.get(expProcess.size() - 2);
                expProcess.remove(formulaProcess.size() - 1);
            }
            else{//在嵌套自定义函数中
                if (formula.contains("pow") && !formulaProcess.get(formulaProcess.size() - 2).contains("pow")) {
                    //上一步是pow
                    atPow = false;
                }
                else if(replacedNum==0&&replacePara.isEmpty()){
                    //上一步是嵌套自定义函数
                    atFunc=false;
                    sonParaNum=0;
                    exp = expProcess.get(expProcess.size() - 2);
                    expProcess.remove(formulaProcess.size() - 1);
                }
                else if(replacedNum>0&&replacedNum<sonParaNum&&replacePara.isEmpty()){
                    //上一步输入逗号，不是右括号,一个参数完成输入
                    replacedNum--;
                    replacePara= replaceParaProcess[replacedNum].get(replaceParaProcess[replacedNum].size()-1);
                    //更改之前替换的表达式；exp无更改
                }
                else if(replacedNum!=sonParaNum){
                    replacePara= replaceParaProcess[replacedNum].get(replaceParaProcess[replacedNum].size()-2);
                    replaceParaProcess[replacedNum].remove(replaceParaProcess[replacedNum].size()-1);
                }
            }
            formula = formulaProcess.get(formulaProcess.size() - 2);
            formulaProcess.remove(formulaProcess.size() - 1);
        }
        functionShow.setText(formula);
    }

    /**
     * @param event
     * @Description 处理点击保存事件，包括判断名称合法性、函数表达式合法性等
     * @author sxq
     * @date 2023/11/27 16:44
     **/
    @FXML
    private void handleSaveClick(ActionEvent event) {
        //TODO 添加自动替换参数功能?
        String judgeName = UserFunction.judgeName(functionName.getText());
        System.out.println(functionName.getText());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("出错了");
        alert.setHeaderText(null);
        //判断名称合法性
        if (!judgeName.equals("true")) {
            alert.setContentText(judgeName);
            alert.showAndWait();
            return;
        }
        //判断是否重复
        if (UserFunction.judgeNameRepeat(functionName.getText())) {
            alert.setContentText("该函数名已存在");
            alert.showAndWait();
            return;
        }
        //判断表达式合法性
        if (!UserFunction.judgeFunction(exp).equals("true")) {
            alert.setContentText(UserFunction.judgeFunction(exp));
            alert.showAndWait();
            return;
        }
        UserFunction function = new UserFunction(functionName.getText(), exp, formula);
        function.setParaNum(choiceBox.getValue());
        boolean haveAdd;//是否成功添加
        //判断参数个数并选择
        if(function.judgeParaNum()!=0){
            int trueNum=function.getParaNum()+ function.judgeParaNum();
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setContentText("坚持保存吗？它将被设置为"+trueNum+"元函数");
            if(function.judgeParaNum()>0)
                alert1.setHeaderText("参数个数比想象中要多……");
            else
                alert1.setHeaderText("参数个数比想象中要少……");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.get() == ButtonType.OK){
                function.setParaNum(trueNum);
                haveAdd=function.addFunction();
            } else {
                System.out.println("选择了返回");
                return;
            }
        }
        else {
            haveAdd = function.addFunction();
        }
        if (haveAdd) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("创建了一个新函数！");
            alert1.setHeaderText("函数名：" + functionName.getText());
            alert1.setContentText("表达式：" + formula);
            alert1.showAndWait();
            //更新列表
            FunctionList.getItems().clear();
            functionList= SqlFunction.getAllFunction();
            FunctionList.getItems().addAll(functionList);
            formula = "";
            exp = "";
            formulaProcess.clear();
            expProcess.clear();
            functionShow.setText(formula);
        }
    }

    /**
     * @param event
     * @Description 点击自定义函数按钮，获取已有的函数列表，并转换表达式和函数式
     * @author sxq
     * @date 2023/11/27 20:41
     **/
    @FXML
    private void handleGetFunction(ActionEvent event) {
        if (!atFunc)//不处于嵌套状态
            FunctionList.setVisible(!FunctionList.isVisible());//点击显示
        else {
            //处于嵌套状态，点击逗号
            formula += ",";
            formulaProcess.add(formula);
            replacedNum++;
            replacePara = "";
            if(replacedNum==sonParaNum-1) {//不能再加参数
                BUTTON_USERFUNCTION.setDisable(false);
                BUTTON_USERFUNCTION.setText("自定义函数");
            }
            functionShow.setText(formula);
        }
    }

    /**
     * @param mouseEvent
     * @Description 处理历史自定义函数的行点击事件：加入历史自定义函数
     * @author sxq
     * @date 2023/11/28 17:23
     **/
    @FXML
    public void handleRowClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            TablePosition pos = (TablePosition) FunctionList.getSelectionModel().getSelectedCells().get(0);
            int rowNum = pos.getRow();
            UserFunction f = (UserFunction) FunctionList.getItems().get(rowNum);
            //获取到了本行函数，将其加载到当前表达式中
            sonFName = f.getName();
            formula += f.getName() + "(";
            //替换exp表达式，阻止嵌套故无需反复命名
            String renameExp = f.getExp().replace("$x$", "($x_$)");
            renameExp = renameExp.replace("$y$", "($y_$)");
            renameExp = renameExp.replace("$z$", "($z_$)");
            exp +="("+renameExp;

            atFunc = true;
            FunctionList.setVisible(false);
            //根据有几个参数，分别替换原表达式中的参数字符
            sonParaNum = f.getParaNum();
            if(sonParaNum>1)
                BUTTON_USERFUNCTION.setText(",");
            else//只能输入一个参数时禁用
                BUTTON_USERFUNCTION.setDisable(true);
        }
        functionShow.setText(formula);
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
                if(atFunc) replacePara+=str;
                else  exp+=str;
                break;
            case "×":
                formula=formula+"*";
                if(atFunc) replacePara+="*";
                else exp+="*";
                break;
            case "÷":
                formula=formula+"/";
                if(atFunc) replacePara+="/";
                else exp+="/";
                break;
            case "mod":
                formula=formula+"%";
                if(atFunc) replacePara+="%";
                else exp+="%";
                break;
            case "log10": case "log": case "log2", "sin", "cos", "tan", "sec", "csc", "cot":
                formula=formula+str+"(";
                if(atFunc)replacePara+=str+"(";
                else  exp+=str+"(";
                break;
            case "10ˣ":
                formula=formula+"10^(";
                if(atFunc)replacePara+="10^(";
                else  exp+="10^(";
                break;
            case "xⁿ":
                formula=formula+"^(";
                if(atFunc)replacePara+="^(";
                else exp+="^(";
                break;
            case "√":
                formula=formula+"sqrt(";
                if(atFunc)replacePara+="sqrt(";
                else exp+="sqrt(";
                break;
            case "³√":
                formula=formula+"^(1/3)";
                if(atFunc)replacePara+="^(1/3)";
                else exp+="^(1/3)";
                break;
            case "eˣ":
                formula=formula+"exp(";
                if(atFunc) replacePara+="exp(";
                else exp+="exp(";
                break;
            case "x³":
                formula=formula+"^3";
                if(atFunc)replacePara+="^3";
                else exp+="^3";
                break;
            case "π":
                formula=formula+"pi";
                if(atFunc)replacePara+="pi";
                else exp+="pi";
                break;
            case "e":
                formula=formula+"e";
                if(atFunc)replacePara+="e";
                else exp+="e";
                break;
            case "x²":
                formula=formula+"^2";
                if(atFunc)replacePara+="^2";
                else exp+="^2";
                break;
            case "C":
                formula="";
                exp="";
                atFunc=false;
                sonFName="";
                sonParaNum=0;
                replacePara="";
                replacedNum=0;
                BUTTON_USERFUNCTION.setDisable(false);
                break;
            case "x":
                formula=formula+"x";
                if(atFunc) replacePara+="$x$";
                else exp+="$x$";
                break;
            case "y":
                formula=formula+"y";
                if(atFunc) replacePara+="$y$";
                else exp+="$y$";
                break;
            case "z":
                formula=formula+"z";
                if(atFunc) replacePara+="$z$";
                else exp+="$z$";
                break;
            case "pow":
                formula=formula+"pow(";
                if(atFunc) replacePara+="pow(";
                else exp+="pow(";
                atPow=true;
                break;
            case ","://只针对pow按钮
                formula=formula+",";
                if(atFunc) replacePara+=",";
                else exp+=",";
                break;
            case ")":
                formula=formula+")";
                if(atFunc){
                    if(!(atFunc=checkAtFunc())){//嵌套结束，开始替换
                        if(replaceParaProcess[0].get(replaceParaProcess[0].size()-1).isEmpty()){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("出错了");
                            alert.setContentText("请输入函数"+sonFName+"的参数x");
                            alert.showAndWait();
                            return;
                        }
                        exp=exp.replace("$x_$", replaceParaProcess[0].get(replaceParaProcess[0].size()-1));
                        replaceParaProcess[0].clear();
                        if(sonParaNum>1) {
                            if(replaceParaProcess[1].get(replaceParaProcess[1].size()-1).isEmpty()){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("出错了");
                                alert.setContentText("请输入函数"+sonFName+"的参数y");
                                alert.showAndWait();
                                return;
                            }
                            exp = exp.replace("$y_$", replaceParaProcess[1].get(replaceParaProcess[1].size() - 1));
                            replaceParaProcess[1].clear();
                        }
                        if(sonParaNum>2) {
                            if(replaceParaProcess[2].get(replaceParaProcess[2].size()-1).isEmpty()){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("出错了");
                                alert.setContentText("请输入函数"+sonFName+"的参数z");
                                alert.showAndWait();
                                return;
                            }
                            exp = exp.replace("$z_$", replaceParaProcess[2].get(replaceParaProcess[2].size() - 1));
                            replaceParaProcess[2].clear();
                        }
                        //结束嵌套，重置
                        sonFName="";
                        sonParaNum=0;
                        replacePara="";
                        replacedNum=0;
                        BUTTON_USERFUNCTION.setDisable(false);
                    }
                }
                else exp=exp+")";
                if(atPow){
                    atPow=checkPow();
                }
                break;
            default:
                System.out.println("按钮"+str+"未设置");
                return;
        }
        formulaProcess.add(formula);
        if(atFunc)
            replaceParaProcess[replacedNum].add(replacePara);
        else
            expProcess.add(exp);
    }
/**
 * @Description 判断是否处于嵌套函数中
 * @return boolean
 * @author sxq
 * @date 2023/11/28 23:38
**/
    private boolean checkAtFunc() {
        if(sonFName.isEmpty())return false;
        int index=formula.lastIndexOf(sonFName);
        int bracket=0;
        for(index=index+sonFName.length();index<formula.length();index++){
            if(formula.charAt(index)=='(')bracket++;
            else if(formula.charAt(index)==')')bracket--;
        }
        return bracket > 0;
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
        return bracket > 0;
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
        FunctionList.getItems().addAll(functionList);
        replaceParaProcess[0]=new ArrayList<>();
        replaceParaProcess[1]=new ArrayList<>();
        replaceParaProcess[2]=new ArrayList<>();

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


