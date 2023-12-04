package com.calculator.calculation;
import NewFunction.UserFunction;
import infinitesimal.InfinitesimalSolve;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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

    public TableView FunctionList;
    public TableColumn nameList;
    public TableColumn paraNumList;
    public TableColumn formulaList;
    /*展示给用户的表达式*/
    private String formula = "";
    /*后台用于计算的表达式*/
    protected String exp = "";
    /*储存formula的编辑过程*/
    private List<String> formulaProcess = new ArrayList<>();
    /*储存exp的编辑过程*/
    private List<String> expProcess = new ArrayList<>();
    public static ArrayList<UserFunction> functionList = new ArrayList<>();
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
        if(!formula.isEmpty()&&!exp.isEmpty()){//表达式非空，回退到上一步
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
        else if(!up_s.matches("-?\\d+(\\.\\d+)?")){
            alert.setContentText("积分上限只能是合法数字~");
            alert.showAndWait();
        }
        else if(!down_s.matches("-?\\d+(\\.\\d+)?")){
            alert.setContentText("积分下限只能是合法数字~");
            alert.showAndWait();
        }
        else if (!UserFunction.judgeFunction(exp).equals("true")) {
            alert.setContentText(UserFunction.judgeFunction(exp));
        }//判断表达式合法性
        double up_d=Double.parseDouble(up_s);
        double down_d=Double.parseDouble(down_s);
        InfinitesimalSolve ift=new InfinitesimalSolve(up_d,down_d,exp);
        double res=ift.getRes();
        result.setText(String.format("%.3f",res));
        //TODO 保存计算结果

    }

    /**
     * @param event
     * @Description 点击自定义函数按钮，获取已有的函数列表，并转换表达式和函数式
     * @author sxq
     * @date 2023/11/27 20:41
     **/
    @FXML
    private void handleGetFunction(ActionEvent event) {
        FunctionList.setVisible(!FunctionList.isVisible());//点击显示
    }

    /**
     * @param mouseEvent
     * @Description 处理历史自定义函数的行点击事件：加入历史自定义函数
     * @author sxq
     * @date 2023/11/28 17:23
     **/
    public void handleRowClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            UserFunction f = (UserFunction)(FunctionList.getSelectionModel().getSelectedItem());
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
                formula="";
                exp="";
                result.setText(null);
                upValue.setText(null);
                downValue.setText(null);
                BUTTON_USERFUNCTION.setDisable(false);
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
    public void initialize(){
        FunctionList.setPlaceholder(new Label("脑瓜子空空的"));//占位文本
        nameList.setCellValueFactory(new PropertyValueFactory<>("name"));
        paraNumList.setCellValueFactory(new PropertyValueFactory<>("paraNum"));
        formulaList.setCellValueFactory(new PropertyValueFactory<>("formula"));
        FunctionList.getItems().addAll(FunctionController.functionList);
    }
}
