package com.calculator.calculation;
import NewFunction.UserFunction;
import infinitesimal.InfinitesimalSolve;
import Database.SqlInfinitesimal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sxq
 * @Description:微积分页面控制函数
 * @date 2023/12/4 9:58
 */
public class InfinitesimalController {

    public Button BUTTON_paraX;
    public Button BUTTON_USERFUNCTION;
    public Button buttonPow;
    /*函数表达式文本框*/
    public TextField functionShow;
    /*上限文本框*/
    public TextField upValue;
    /*下限文本框*/
    public TextField downValue;
    /*结果文本框*/
    public TextField result;
    /*自定义函数列表元素*/
    public TableView FunctionTableView;
    public TableColumn nameList;
    public TableColumn paraNumList;
    public TableColumn formulaList;
    public Pane infinitesimal;
    public Pane infinitesimalHistory;
    public ImageView historyImg;
    public ImageView returnImg;
    /*历史记录列表元素*/
    public TableView historyTableView;
    public TableColumn historyTimeList;
    public TableColumn historyFormulaList;
    public TableColumn historyResList;
    /*展示给用户的表达式*/
    private String formula = "";
    /*后台用于计算的表达式*/
    protected String exp = "";
    /*储存formula的编辑过程*/
    private List<String> formulaProcess = new ArrayList<>();
    /*储存exp的编辑过程*/
    private List<String> expProcess = new ArrayList<>();
    private static List<InfinitesimalSolve> historyList = new ArrayList<>();
    /*pow判断，以切换显示*/
    public static boolean atPow = false;

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
        if(formulaProcess.size()<=1||expProcess.size()<=1){
            clear();
        }
        else if(!formula.isEmpty()&&!exp.isEmpty()){//表达式非空，回退到上一步
            if (formula.contains("pow") && !formulaProcess.get(formulaProcess.size() - 2).contains("pow")) {
                //上一步是pow
                atPow = false;
            }
            exp = expProcess.get(expProcess.size() - 2);
            expProcess.remove(formulaProcess.size() - 1);
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("出错了");
        alert.setHeaderText(null);
        String up_s=upValue.getText();
        String down_s=downValue.getText();
        if(up_s.isEmpty()){
            alert.setContentText("请输入积分上限");
            alert.showAndWait();
        }
        else if(down_s.isEmpty()){
            alert.setContentText("请输入积分下限");
            alert.showAndWait();
        }
        else if(!up_s.matches("^(?!0\\d)\\d+(\\.\\d+)?$")){
            alert.setContentText("积分上限只能是合法数字~");
            alert.showAndWait();
        }
        else if(!down_s.matches("^(?!0\\d)\\d+(\\.\\d+)?$")){
            alert.setContentText("积分下限只能是合法数字~");
            alert.showAndWait();
        }
        else if (!UserFunction.judgeFunction(exp).equals("true")) {
            alert.setContentText(UserFunction.judgeFunction(exp));
        }//判断表达式合法性
        double up_d=Double.parseDouble(up_s);
        double down_d=Double.parseDouble(down_s);
        InfinitesimalSolve ift=new InfinitesimalSolve(up_d,down_d,formula,exp,formulaProcess,expProcess);
        double res=ift.getRes();
        result.setText(String.format("%.3f",res));
        SqlInfinitesimal.add(ift);
        formula = "";
        exp = "";
        formulaProcess.clear();
        expProcess.clear();
        functionShow.setText(formula);
    }
    /**
     * @Description 点击显示微积分计算历史记录
 * @param mouseEvent 点击事件
     * @author sxq
     * @date 2023/12/8 14:33
    **/
    @FXML
    public void handleHisImageClick(MouseEvent mouseEvent) {
        getHistoryList();
        infinitesimal.setVisible(false);
        infinitesimalHistory.setVisible(true);
    }
    /**
     * @Description 从历史记录页面返回
     * @param mouseEvent
     * @author sxq
     * @date 2023/12/8 15:29
     **/
    public void handleReturnClick(MouseEvent mouseEvent) {
        infinitesimal.setVisible(true);
        infinitesimalHistory.setVisible(false);
    }

    public void handleHistoryRowClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {//左键单击添加
            InfinitesimalSolve ift = (InfinitesimalSolve) (historyTableView.getSelectionModel().getSelectedItem());
            if(ift==null){
                System.out.println("点空了");
                return ;
            }
            if(SqlInfinitesimal.exists(ift.getSaveTime())){
               formula=ift.getFormula();
               exp=ift.getExp();
               formulaProcess=new ArrayList<>(ift.getFormulaProcess());
               expProcess=new ArrayList<>(ift.getExpProcess());
               upValue.setText(String.valueOf(ift.getUpValue()));
               downValue.setText(String.valueOf(ift.getDownValue()));
               result.setText("");
               functionShow.setText(formula);
               //返回编辑页面
                infinitesimalHistory.setVisible(false);
                infinitesimal.setVisible(true);
            }
        }
        else if(mouseEvent.getButton()==MouseButton.SECONDARY){//右键单击选择是否删除
            InfinitesimalSolve ift = (InfinitesimalSolve) (historyTableView.getSelectionModel().getSelectedItem());
            if(SqlInfinitesimal.exists(ift.getSaveTime())){
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setContentText("删除这条历史记录？");
                Optional<ButtonType> result = alert1.showAndWait();
                if (result.get() == ButtonType.OK){
                    SqlInfinitesimal.delete(ift);
                    getHistoryList();
                }
            }
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
        FunctionTableView.setVisible(!FunctionTableView.isVisible());//点击显示
    }

    /**
     * @param mouseEvent
     * @Description 处理历史自定义函数的行点击事件：加入历史自定义函数
     * @author sxq
     * @date 2023/11/28 17:23
     **/
    public void handleRowClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            UserFunction f = (UserFunction)(FunctionTableView.getSelectionModel().getSelectedItem());
            if(f.getParaNum()!=1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("出错了");
                alert.setHeaderText(null);
                alert.setContentText("寄蒜器只会算一重积分TvT");
                alert.showAndWait();
            }
            else{
                formula+=f.getName()+"(x)";
                exp+="("+f.getExp()+")";
                formulaProcess.add(formula);
                expProcess.add(exp);
                functionShow.setText(formula);
            }
        }
    }

    /**
     * @Description 编辑自定义函数的文本
     * @param str
     * @author sxq
     * @date 2023/12/4 16:00
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
                clear();
                break;
            case "x":
                formula=formula+"x";
                exp+="$x$";
                break;
            case "pow":
                formula=formula+"pow(";
                exp+="pow(";
                atPow=true;
                break;
            case ","://只针对pow按钮
                formula=formula+",";
               exp+=",";
                break;
            case ")":
                formula=formula+")";
                exp+=")";
                if(atPow){
                    atPow=checkPow();
                }
                break;
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
     * @date 2023/12/4 15:14
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
     * @Description 清空重置页面
     * @author sxq
     * @date 2023/12/11 11:20
    **/
    private void clear(){
        formula="";
        exp="";
        formulaProcess.clear();
        expProcess.clear();
        result.setText("");
        upValue.setText("");
        downValue.setText("");
        BUTTON_USERFUNCTION.setDisable(false);
    }
    public void initialize(){
        /*初始化自定义函数列表*/
        FunctionTableView.setPlaceholder(new Label("脑瓜子空空的"));//占位文本
        nameList.setCellValueFactory(new PropertyValueFactory<>("name"));
        paraNumList.setCellValueFactory(new PropertyValueFactory<>("paraNum"));
        formulaList.setCellValueFactory(new PropertyValueFactory<>("formula"));
        FunctionTableView.getItems().addAll(FunctionController.functionList);
        /*初始化历史记录列表*/
        historyTableView.setPlaceholder(new Label("还没有进行过微积分计算"));//占位文本
        historyFormulaList.setCellValueFactory(new PropertyValueFactory<>("formula"));
        historyResList.setCellValueFactory(new PropertyValueFactory<>("result"));
        historyTimeList.setCellValueFactory(new PropertyValueFactory<>("saveTime"));
    }
    /**
     * @Description 从数据库中获取历史记录列表         
     * @author sxq
     * @date 2023/12/8 15:10
    **/
    private void getHistoryList(){
        historyList=SqlInfinitesimal.getAllHis();
        historyTableView.getItems().setAll(historyList);
        historyTableView.refresh();
    }
    /**
     * @Description 获取从自定义函数页面跳转得到的参数
 * @param f 表达式
 * @param e 计算式
     * @author sxq
     * @date 2023/12/11 11:24
    **/
    public void getJumpFunction(String f,String e){
        this.formula=f;
        this.exp=e;
        this.formulaProcess.add(f);
        this.expProcess.add(e);
        functionShow.setText(formula);
    }
}
