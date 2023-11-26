package com.calculator.calculation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import scientific.ScientificSolve;
import scientific.Error;
/**
 * @author Bu Xinran
 * @Description 科学计算器处理点击事件
 * @date 2023/11/22 21:59
 */
public class ScientificController {
    public ImageView historyImg;
    public Button buttonPow;
    public TextField formulaShow;
    public TextField answerShow;
    private String formula="";
    private Error calFlag;
    private double answer=1;
    public static boolean atPow=false;
    @FXML
    private void handleHisImageClick(MouseEvent event) {
        // 跳转到历史记录界面

    }
    /***
     * @Description  处理点击了哪个按钮，实时更新式子
     * @param event 点击了哪个按钮
     * @author Bu Xinran
     * @date 2023/11/26 10:43
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
        formulaShow.setText(formula);
        checkIllegal();
        tackleError();
    }
    /***
     * @Description 产生错误后的后续处理
     * @author Bu Xinran
     * @date 2023/11/26 11:01
     **/
    private void tackleError() {
        if(calFlag==Error.bracket){
            answerShow.setText("括号不匹配！");
        }else if(calFlag==Error.symbolContinue){
            answerShow.setText("运算符连续！");
        }else if(calFlag==Error.pow){
            answerShow.setText("幂运算函数未完成");
        }else if(calFlag==Error.yes){
            setAnswer();
            answerShow.setText(String.valueOf(answer));
        }else if(calFlag==Error.dotRepeat){
            answerShow.setText("小数点重复，请删除！");
        }
    }
    /***
     * @Description 实时获得计算式的值
     * @author Bu Xinran
     * @date 2023/11/26 11:37
     **/
    private void setAnswer() {

    }
    /***
     * @Description  处理计算式生成正确的表达式
     * @param str 新输入的字符串
     * @author Bu Xinran
     * @date 2023/11/23 16:50
     **/
    public void editFormula(String str){
        switch(str){
            case "0": case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": case "(": case ")": case "+": case "-": case ".": case "!":
                formula=formula+str;
                break;
            case "×":
                formula=formula+"*";
                break;
            case "÷":
                formula=formula+"/";
                break;
            case "mod":
                formula=formula+"%";
                break;
            case "log10": case "log": case "log2":
                formula=formula+str+"(";
                break;
            case "10ˣ":
                formula=formula+"10^(";
                break;
            case "xⁿ":
                formula=formula+"^(";
                break;
            case "√":
                formula=formula+"sqrt(";
                break;
            case "³√":
                formula=formula+"^(1/3)";
                break;
            case "eˣ":
                formula=formula+"exp(";
                break;
            case "x³":
                formula=formula+"^3";
                break;
            case "π":
                formula=formula+"pi";
                break;
            case "e":
                formula=formula+"e";
                break;
            case "sin": case "cos": case "tan": case "sec": case "csc": case "cot":
                formula=formula+str+"(";
                break;
            case "x²":
                formula=formula+"^2";
                break;
            case "C":
                formula="";
                break;
            case "⌊x⌋":
                formula=formula+"floor(";
                break;
            case "⌈x⌉":
                formula=formula+"ceil(";
                break;
            case "|x|":
                formula=formula+"abs(";
                break;
            case "pow":
                formula=formula+"pow(";
                atPow=true;
                break;
            case "=":
                formula=formula+"=";
                ScientificSolve a=new ScientificSolve(formula);
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
    /***
     * @Description 判断算式合法性
     * @author Bu Xinran
     * @date 2023/11/26 10:24
     **/
    public void checkIllegal() {
        //判断括号的合法性
        int checkBrack=0;
        for(int i=0;i < formula.length();i++) {
            if(formula.charAt(i)=='('){
                checkBrack+=1;
            }else if(formula.charAt(i)==')'){
                checkBrack-=1;
            }
            if(checkBrack<0){
                calFlag=Error.bracket;
                return;
            }
        }
        //判断调用幂函数的合法性
        if(ScientificController.atPow){
            calFlag=Error.pow;
            return;
        }
        //判断符号是否连续使用
        int checkSymbol=0;
        for(int i=0;i<formula.length();i++){
            switch(formula.charAt(i)){
                case '+': case '-': case '*': case '/': case '%':
                    checkSymbol++;
                    break;
                case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    checkSymbol=0;
                    break;

            }
        }
        //判断小数点，如果是.1928默认为0.1928
        for(int i=0;i<formula.length();i++){
            if(formula.charAt(i)=='.'){
                if(formula.charAt(i-1)=='.'){
                    calFlag=Error.dotRepeat;
                }else if(!Character.isDigit(formula.charAt(i-1))){
                    String before=formula.substring(0,i);
                    String after=formula.substring(i+1);
                    formula=before+"0"+after;
                }
            }
        }
        calFlag=Error.yes;
    }
}