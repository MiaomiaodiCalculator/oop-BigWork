package com.calculator.calculation;
import Equation.EquationError;
import Equation.EquationSolve;
import com.singularsys.jep.EvaluationException;
import com.singularsys.jep.ParseException;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.apache.commons.math3.linear.*;

import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import static Equation.EquationSolve.getAnswer;
import static Scientific.ScientificSolve.checkText;

/**
 * @author Bu Xinran
 * @Description 解方程计算器控制器，处理点击事件
 * @date 2023/11/28 10:23
 */
public class EquationController implements Initializable {
    public Label showArea;
    public Label answerArea;
    public Button multiply;
    public Button subtract;
    public Button add;
    public Button para;
    public Button dot;
    public javafx.scene.layout.Pane Equations;
    public javafx.scene.layout.Pane e2;
    public javafx.scene.layout.Pane e3;
    public javafx.scene.layout.Pane e4;
    public Pane Equation;
    public TextField e2x1;
    public TextField e2y1;
    public TextField e2c1;
    public TextField e2c2;
    public TextField e2y2;
    public TextField e2x2;
    public Button e2answer;
    public Pane e2answerShow;
    public Label e2x;
    public Label e2y;
    public TextField e3x1;
    public TextField e3y1;
    public TextField e3z1;
    public TextField e3c1;
    public TextField e3x2;
    public TextField e3x3;
    public TextField e3z3;
    public TextField e3z2;
    public TextField e3y3;
    public TextField e3y2;
    public TextField e3c3;
    public TextField e3c2;
    public Button e3answer;
    public Label e3x;
    public Label e3y;
    public Label e3z;
    public Pane e3answerShow;
    public Pane e4answerShow;
    public TextField e4x1;
    public TextField e4y1;
    public TextField e4c1;
    public TextField e4z1;
    public TextField e4m1;
    public TextField e4x2;
    public TextField e4x3;
    public TextField e4x4;
    public TextField e4y2;
    public TextField e4y3;
    public TextField e4y4;
    public TextField e4z2;
    public TextField e4z3;
    public TextField e4z4;
    public TextField e4m2;
    public TextField e4m3;
    public TextField e4m4;
    public TextField e4c2;
    public TextField e4c3;
    public TextField e4c4;
    public Label e4x;
    public Label e4y;
    public Label e4z;
    public Label e4m;
    public Button equation1;
    public Button equation2;
    public Button d2;
    public Button d3;
    public Button d4;
    private String showEquation="";
    private int state=2;
    private String equation="";
    public static EquationError calFlag=EquationError.yes;
    public List<Double> answer;
    private boolean atMi=false;
    public String[] downSymbol =new String[]{"⁰","¹","²","³","⁴","⁵","⁶","⁷","⁸","⁹","˙"};
    public ArrayList<EquationSolve> historyEquation=new ArrayList<>();
    /*合法的方程组能否算出正确的值*/
    public static boolean get=true;
    /***
     * @Description 初始化界面
     * @param location 无意义，重载
     * @param resources 无意义，重载
     * @author Bu Xinran
     * @date 2023/11/30 15:20
     **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Equations.setVisible(true);
        Equation.setVisible(false);
        e3.setVisible(false);
        e4.setVisible(false);
        shift1();
    }
    /***
     * @Description 点击跳转到多元一次方程
     * @author Bu Xinran
     * @date 2023/11/30 15:21
     **/
    public void shift1(){
        Equations.setVisible(true);
        Equation.setVisible(false);
        equation1.getStyleClass().add("active");
        equation2.getStyleClass().remove("active");
        d2.getStyleClass().add("active");
        d3.getStyleClass().remove("active");
        d4.getStyleClass().remove("active");
    }
    /***
     * @Description  点击跳转到多次方程
     * @author Bu Xinran
     * @date 2023/11/30 15:21
     **/
    public void shift2(){
        Equations.setVisible(false);
        Equation.setVisible(true);
        equation2.getStyleClass().add("active");
        equation1.getStyleClass().remove("active");
    }
    /***
     * @Description  处理按钮点击事件，生成方程式
     * @param event 鼠标点击哪个按钮
     * @author Bu Xinran
     * @date 2023/11/29 18:30
     **/
    public void buttonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        editEquation(buttonText);
        showArea.setText(showEquation+"=0");
    }
    /***
     * @Description  编辑方程式并判断合法性
     * @param text 鼠标上的文本
     * @author Bu Xinran
     * @date 2023/11/29 18:32
     **/
    private void editEquation(String text) {
        switch (text) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                if (atMi) showEquation = showEquation + downSymbol[Integer.parseInt(text)];
                else showEquation = showEquation + text;
                equation = equation + text;
                break;
            case ".", "+", "-", "*":
                showEquation = showEquation + text;
                equation = equation + text;
                break;
            case "变量x":
                showEquation = showEquation + "x";
                equation = equation + "x";
                break;
            case "xʸ":
                atMi = true;
                equation = equation + "^(";
                break;
            case "↓":
                if(atMi)equation = equation + ")";
                atMi = false;
                break;
            case "x²":
                showEquation=showEquation+"x²";
                equation=equation+"x^(2)";
                break;
            case "C":
                atMi = false;
                equation = "";
                showEquation = "";
                calFlag=EquationError.yes;
                answerArea.setText("");
                break;
            default:
                //回退
                if (equation.charAt(equation.length() - 1) == ')') {
                    equation = equation.substring(0, equation.length() - 2);
                    showEquation = showEquation.substring(0, showEquation.length() - 1);
                    atMi = true;
                } else if (equation.charAt(equation.length() - 1) == '(') {
                    equation = equation.substring(0, equation.length() - 3);
                    showEquation = showEquation.substring(0, showEquation.length() - 1);
                    atMi = false;
                } else {
                    equation = equation.substring(0, equation.length() - 1);
                    showEquation = showEquation.substring(0, showEquation.length() - 1);
                }
        }
        if (atMi) {
            multiply.setDisable(true);
            subtract.setDisable(true);
            add.setDisable(true);
            para.setDisable(true);
            dot.setDisable(true);
        } else {
            multiply.setDisable(false);
            subtract.setDisable(false);
            add.setDisable(false);
            para.setDisable(false);
            dot.setDisable(false);
        }
        System.out.println(equation);
    }
    /***
     * @Description  计算多次一元方程
     * @author Bu Xinran
     * @date 2023/11/29 22:43
     **/
    public void calculateClick() throws ParseException, EvaluationException {
        judgeIllegal();
        if(calFlag==EquationError.yes){
            answer=getAnswer(equation);
            if(calFlag!=EquationError.notEqual&&calFlag!=EquationError.error){
                if(answer.isEmpty()){
                    answerArea.setText("无实根。");
                }else{
                    String show="";
                    for(int i=0;i<answer.size();i++){
                        show=show+ answer.get(i);
                        if(i!=answer.size()-1)show=show+",";
                    }
                    answerArea.setText(show);
                }
            }
        }else if(calFlag==EquationError.dotRepeat){
            answerArea.setText("小数点重复！");
        }else if(calFlag==EquationError.symbolRepeat){
            answerArea.setText("运算符重复！");
        }else if(calFlag==EquationError.xRepeat){
            answerArea.setText("请直接输入幂次，不要连续输入变量x！");
        }else if(calFlag==EquationError.cannotFindX){
            answerArea.setText("输入的方程中不包含x！");
        }
        if(calFlag==EquationError.notEqual){
            answerArea.setText("运算式不相等！");
        }else if(calFlag==EquationError.error){
            answerArea.setText("运算式错误！");
        }
        EquationSolve solve=new EquationSolve(calFlag,showEquation,equation);
        historyEquation.add(solve);
    }
    /***
     * @Description 判断方程式合法性
     * @author Bu Xinran
     * @date 2023/11/29 23:20
     **/
    protected void judgeIllegal() {
        //检查小数点连续问题。
        int len=equation.length();
        int checkDot=0;
        for(int i=0;i<len;i++){
            if(!Character.isDigit(equation.charAt(i))&&equation.charAt(i)!='.'){
                checkDot=0;
            }
            if(equation.charAt(i)=='.'){
                checkDot++;
                if(checkDot>1){
                    calFlag=EquationError.dotRepeat;
                    return;
                }
            }
        }
        //检查运算符连续问题。
        int checkSymbol=0;
        for(int i=0;i<len;i++){
            if(equation.charAt(i)=='+'||equation.charAt(i)=='-'||equation.charAt(i)=='*')checkSymbol++;
            else checkSymbol=0;
            if(checkSymbol>1){
                calFlag=EquationError.symbolRepeat;
                return;
            }
        }
        //检查x是否连续
        int checkX=0;
        for(int i=0;i<len;i++){
            if(equation.charAt(i)=='x'){
                checkX++;
            }else{
                checkX=0;
            }
            if(checkX>1){
                calFlag=EquationError.xRepeat;
                return;
            }
        }
        //检查是否有x
        int cntX=0;
        for(int i=0;i<len;i++){
            if(equation.charAt(i)=='x'){
                cntX++;
                break;
            }
        }
        if(cntX==0){
            calFlag=EquationError.cannotFindX;
            return;
        }
    }
    /***
     * @Description 通过点击按钮切换界面
     * @param event  鼠标点击了哪个按钮
     * @author Bu Xinran
     * @date 2023/12/1 11:13
     **/
    public void shift(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String text = clickedButton.getText();
        e2.setVisible(false);
        e2answerShow.setVisible(false);
        e3.setVisible(false);
        e3answerShow.setVisible(false);
        e4.setVisible(false);
        e4answerShow.setVisible(false);
        switch (text) {
            case "2×2":
                e2.setVisible(true);
                d2.getStyleClass().add("active");
                d3.getStyleClass().remove("active");
                d4.getStyleClass().remove("active");
                break;
            case "3×3":
                e3.setVisible(true);
                d3.getStyleClass().add("active");
                d2.getStyleClass().remove("active");
                d4.getStyleClass().remove("active");
                break;
            default:
                e4.setVisible(true);
                d4.getStyleClass().add("active");
                d3.getStyleClass().remove("active");
                d2.getStyleClass().remove("active");
                break;
        }
        state=Integer.parseInt(text.substring(0,1));
    }
    /***
     * @Description 计算得到方程组结果
     * @author Bu Xinran
     * @date 2023/12/1 11:25
     **/
    public void getAns() {
        if(state==2){
            tackle2();
        }else if(state==3){
            tackle3();
        }else if(state==4){
            tackle4();
        }
    }
    /***
     * @Description  计算二元一次方程组
     * @author Bu Xinran
     * @date 2023/12/1 11:27
     **/
    private void tackle2() {
        e2answerShow.setVisible(true);
        String e2X1=e2x1.getText();
        String e2Y1=e2y1.getText();
        String e2X2=e2x2.getText();
        String e2Y2=e2y2.getText();
        String e2C1=e2c1.getText();
        String e2C2=e2c2.getText();
        if(checkText(e2X1)&&checkText(e2Y1)&&checkText(e2X2)&&checkText(e2Y2)){
            double[][] a=new double[2][2];
            double[] b=new double[2];
            a[0][0]=Integer.parseInt(e2X1);
            a[0][1]=Integer.parseInt(e2Y1);
            a[1][0]=Integer.parseInt(e2X2);
            a[1][1]=Integer.parseInt(e2Y2);
            b[0]=Integer.parseInt(e2C1);
            b[1]=Integer.parseInt(e2C2);
            answer=getAnswers(a,b);
            if(get){
                e2x.setText("x="+ answer.get(0));
                e2y.setText("y="+answer.get(1));
                return;
            }
        }
        e2x.setText("算式错误");
    }
    /***
     * @Description 计算三元一次方程组
     * @author Bu Xinran
     * @date 2023/12/1 12:58
     **/
    private void tackle3() {
        e3answerShow.setVisible(true);
        String e3X1=e3x1.getText();
        String e3Y1=e3y1.getText();
        String e3Z1=e3z1.getText();
        String e3X2=e3x2.getText();
        String e3Y2=e3y2.getText();
        String e3Z2=e3z2.getText();
        String e3X3=e3x3.getText();
        String e3Y3=e3y3.getText();
        String e3Z3=e3z3.getText();
        String e3C1=e3c1.getText();
        String e3C2=e3c2.getText();
        String e3C3=e3c3.getText();
        if(checkText(e3X1)&&checkText(e3Y1)&&checkText(e3Z1)&&checkText(e3X2)&&checkText(e3Y2)&&checkText(e3Z2)&&checkText(e3X3)&&checkText(e3Y3)&&checkText(e3Z3)&&checkText(e3C1)&&checkText(e3C2)&&checkText(e3C3)){
            double[][] a=new double[3][3];
            double[] b=new double[3];
            a[0][0]=Integer.parseInt(e3X1);
            a[0][1]=Integer.parseInt(e3Y1);
            a[0][2]=Integer.parseInt(e3Z1);
            a[1][0]=Integer.parseInt(e3X2);
            a[1][1]=Integer.parseInt(e3Y2);
            a[1][2]=Integer.parseInt(e3Z2);
            a[2][0]=Integer.parseInt(e3X3);
            a[2][1]=Integer.parseInt(e3Y3);
            a[2][2]=Integer.parseInt(e3Z3);
            b[0]=Integer.parseInt(e3C1);
            b[1]=Integer.parseInt(e3C2);
            b[2]=Integer.parseInt(e3C3);
            answer=getAnswers(a,b);
            if(get){
                e3x.setText("x="+ answer.get(0));
                e3y.setText("y="+answer.get(1));
                e3z.setText("z="+answer.get(2));
                return;
            }
        }
        e3x.setText("算式错误");
    }
    /***
     * @Description 计算四元一次方程组
     * @author Bu Xinran
     * @date 2023/12/1 12:58
     **/
    private void tackle4() {
        e4answerShow.setVisible(true);
        String X1=e4x1.getText();
        String Y1=e4y1.getText();
        String Z1=e4z1.getText();
        String M1=e4m1.getText();
        String X2=e4x2.getText();
        String Y2=e4y2.getText();
        String Z2=e4z2.getText();
        String M2=e4m2.getText();
        String X3=e4x3.getText();
        String Y3=e4y3.getText();
        String Z3=e4z3.getText();
        String M3=e4m3.getText();
        String X4=e4x4.getText();
        String Y4=e4y4.getText();
        String Z4=e4z4.getText();
        String M4=e4m4.getText();
        String C1=e4c1.getText();
        String C2=e4c2.getText();
        String C3=e4c3.getText();
        String C4=e4c4.getText();
        if(checkText(X1)&&checkText(Y1)&&checkText(Z1)&&checkText(M1)&&checkText(X2)&&checkText(Y2)&&checkText(Z2)&&checkText(M2)&&checkText(X3)&&checkText(Y3)&&checkText(Z3)&&checkText(M3)&&checkText(X4)&&checkText(Y4)&&checkText(Z4)&&checkText(M4)&&checkText(C1)&&checkText(C2)&&checkText(C3)&&checkText(C4)){
            double[][] a=new double[4][4];
            double[] b=new double[4];
            a[0][0]=Integer.parseInt(X1);
            a[0][1]=Integer.parseInt(Y1);
            a[0][2]=Integer.parseInt(Z1);
            a[0][3]=Integer.parseInt(M1);
            a[1][0]=Integer.parseInt(X2);
            a[1][1]=Integer.parseInt(Y2);
            a[1][2]=Integer.parseInt(Z2);
            a[1][3]=Integer.parseInt(M2);
            a[2][0]=Integer.parseInt(X3);
            a[2][1]=Integer.parseInt(Y3);
            a[2][2]=Integer.parseInt(Z3);
            a[2][3]=Integer.parseInt(M3);
            a[3][0]=Integer.parseInt(X4);
            a[3][1]=Integer.parseInt(Y4);
            a[3][2]=Integer.parseInt(Z4);
            a[3][3]=Integer.parseInt(M4);
            b[0]=Integer.parseInt(C1);
            b[1]=Integer.parseInt(C2);
            b[2]=Integer.parseInt(C3);
            b[3]=Integer.parseInt(C4);
            answer=getAnswers(a,b);
            if(get){
                e4x.setText("x="+ answer.get(0));
                e4y.setText("y="+answer.get(1));
                e4z.setText("z="+answer.get(2));
                e4m.setText("z="+answer.get(3));
                return;
            }
        }
        e4x.setText("算式错误");
    }
    /***
     * @Description 求解多元一次方程组
     * @param a 二维矩阵
     * @param b 常数项
     * @return java.util.List<java.lang.Double>
     * @author Bu Xinran
     * @date 2023/11/30 16:11
     **/
    public static List<Double> getAnswers(double[][] a, double[] b){
        try{
            RealMatrix coefficientsMatrix = new Array2DRowRealMatrix(a);
            // 使用LU分解求解方程组
            DecompositionSolver solver = new LUDecomposition(coefficientsMatrix).getSolver();
            RealVector constantsVector = new ArrayRealVector(b);
            RealVector solutionVector = solver.solve(constantsVector);
            DecimalFormat df = new DecimalFormat("#.####");
            df.setRoundingMode(RoundingMode.HALF_UP);
            List<Double> solutionList = new ArrayList<>();
            for (double value : solutionVector.toArray()) {
                solutionList.add(Double.parseDouble(df.format(value)));
            }
            get=true;
            return solutionList;
        }catch(SingularMatrixException e){
            get=false;
            return null;
        }
    }
}