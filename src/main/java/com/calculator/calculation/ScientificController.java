package com.calculator.calculation;

import com.singularsys.jep.EvaluationException;
import com.singularsys.jep.ParseException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scientific.ScientificSolve;
import scientific.Error;
import com.singularsys.jep.Jep;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @author Bu Xinran
 * @Description 科学计算器处理点击事件
 * @date 2023/11/22 21:59
 */
public class ScientificController implements Initializable{
    public ImageView historyImg;
    public Button buttonPow;
    public TextField formulaShow;
    public Label answerShow;
    public ImageView returnImg;
    public Pane Scientific;
    public Pane History;
    public Button buttonFree;
    private ArrayList<ScientificSolve> list =new ArrayList<>();
    public TableView<ScientificSolve> tableView;
    public TableColumn<ScientificSolve, String> formulaList;
    public TableColumn<ScientificSolve, String> answerList;
    private String formula="";
    private Error calFlag;
    private String answer="";
    private LinkedHashMap<Integer,String> process=new LinkedHashMap<>();
    private static int cntProcess=0;
    public static boolean atPow=false;
    private boolean finish=false;
    /***
     * @Description  初始化科学计算器界面
     * @param location 所在位置
     * @param resources 事件
     * @author Bu Xinran
     * @date 2023/11/27 10:54
     **/
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化时，显示第一个卡片，隐藏第二个卡片
        Scientific.setVisible(true);
        History.setVisible(false);
    }
    /***
     * @Description 从文件中反序列化中存储历史记录的图
     * @author Bu Xinran
     * @date 2023/11/27 11:16
     **/
    private void getListFromFile() throws IOException, ClassNotFoundException {
        String path="./data/Scientific.out";
        File file=new File(path);
        if(file.exists()){
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list =(ArrayList<ScientificSolve>)ois.readObject();
            ois.close();
            fis.close();
        }
    }
    /***
     * @Description  跳转到历史记录界面
     * @param event 点击事件
     * @author Bu Xinran
     * @date 2023/11/27 10:09
     **/
    @FXML
    private void handleHisImageClick(MouseEvent event) throws IOException {
        try {
            getListFromFile();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        tableView.setPlaceholder(new Label("无历史记录"));//占位文本
        formulaList.setCellValueFactory(cellData -> cellData.getValue().formulaProperty());
        answerList.setCellValueFactory(cellData -> cellData.getValue().answerProperty());
        ObservableList<ScientificSolve> observableList = FXCollections.observableArrayList(list);
        tableView.setItems(observableList);
        Scientific.setVisible(false);
        History.setVisible(true);
    }
    /***
     * @Description  通过判断鼠标点击哪一行跳转回编辑界面
     * @param event 鼠标点击了tableview的哪一行
     * @author Bu Xinran
     * @date 2023/11/27 23:59
     **/

    public void handleRowClick(MouseEvent event) {
        // 判断是否双击行
        if (event.getClickCount() == 2) {
            ScientificSolve selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String toFormula=selectedItem.getFormula();
                String toAnswer=selectedItem.getResult();
                Scientific.setVisible(true);
                History.setVisible(false);
                formula=toFormula;
                formulaShow.setText(formula);
                answer=toAnswer;
                answerShow.setText(answer);
                process=selectedItem.getProcess();
                cntProcess= selectedItem.getCntProcess();
            }
        }
    }
    /***
     * @Description 返回到原界面
     * @param mouseEvent 鼠标点击返回图片
     * @author Bu Xinran
     * @date 2023/11/27 10:30
     **/
    public void handleReturnClick(MouseEvent mouseEvent) throws IOException {
        Scientific.setVisible(true);
        History.setVisible(false);
    }
    /***
     * @Description  处理点击了哪个按钮，实时更新式子
     * @param event 点击了哪个按钮
     * @author Bu Xinran
     * @date 2023/11/26 10:43
     **/
    @FXML
    private void handleButtonClick(ActionEvent event) throws ParseException, EvaluationException {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        editFormula(buttonText);
        if(atPow){
            buttonPow.setText(",");
        }else{
            buttonPow.setText("pow");
        }
        formulaShow.setText(formula);
        if(!finish)checkIllegal();
        tackleError();
    }
    /***
     * @Description  点击自定义函数，调用当前的自定义函数
     * @param actionEvent 点击了自定义函数按钮
     * @author Bu Xinran
     * @date 2023/11/28 10:49
     **/
    public void Freedom(ActionEvent actionEvent) {
//        Stage functionsStage = new Stage();
//        functionsStage.initModality(Modality.WINDOW_MODAL);
//        functionsStage.initOwner(buttonFree.getScene().getWindow());
//        TableView<Function> functionsTable = new TableView<>();
//        TableColumn<Function, String> nameColumn = new TableColumn<>("Name");
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//        TableColumn<Function, String> expressionColumn = new TableColumn<>("Expression");
//        expressionColumn.setCellValueFactory(new PropertyValueFactory<>("expression"));
//        functionsTable.getColumns().addAll(nameColumn, expressionColumn);
//        functionsTable.setItems(FXCollections.observableArrayList(
//                new Function("Function 1", "x^2 + 3"),
//                new Function("Function 2", "sin(x)"),
//                new Function("Function 3", "1 / (1 + e^(-x))"),
//                new Function("Function 4", "log(x)")
//        ));
//        VBox.setVgrow(functionsTable, Priority.ALWAYS);
//        VBox.setMargin(functionsTable, new Insets(10));
//        VBox.setMargin(functionsPanel, new Insets(10));
//        functionsPanel.getChildren().setAll(functionsTable);
//        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setPrefSize(400, 300);
//        scrollPane.setContent(functionsPanel);
//        Scene scene = new Scene(scrollPane);
//        functionsStage.setScene(scene);
//        functionsStage.show();
    }
    /***
     * @Description 产生错误后的后续处理
     * @author Bu Xinran
     * @date 2023/11/26 11:01
     **/
    private void tackleError() throws ParseException, EvaluationException {
        if(calFlag==Error.bracket){
            answerShow.setText("括号不匹配！");
        }else if(calFlag==Error.symbolContinue){
            answerShow.setText("运算符使用错误！");
        }else if(calFlag==Error.pow){
            answerShow.setText("幂运算函数未完成");
        }else if(calFlag==Error.yes){
            setAnswer();
            answerShow.setText(String.valueOf(answer));
        }else if(calFlag==Error.dotRepeat){
            answerShow.setText("小数点重复，请删除！");
        }else if(calFlag==Error.nothing){
            answerShow.setText("还未输入算式");
        }else if(calFlag== Error.divideZero){
            answerShow.setText("不能除以0！");
        }else if(calFlag==Error.Illegal){
            answerShow.setText("算式非法！");
        }
    }
    /***
     * @Description  将向量计算器的计算结果存入List，然后序列化储存在文件中
     * @author Bu Xinran
     * @date 2023/11/25 20:45
     **/
    private void pushListToHistory(){
        try{
            String path="./data/Scientific.out";
            File file=new File(path);
            if(!file.exists()){
                file.getParentFile().mkdirs();
            }else{
                file.delete();
                file.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.flush();
            oos.close();
            fos.flush();
            fos.close();
        }catch(SecurityException e){
            System.out.println("File operation failed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /***
     * @Description 实时获得计算式的值
     * @author Bu Xinran
     * @date 2023/11/26 11:37
     **/
    private void setAnswer() throws EvaluationException {
        Jep jep=new Jep();
        try{
            jep.parse(formula);
            answer=jep.evaluate().toString();
        }catch(ParseException e){
            System.out.print(" ");
        }
    }
    /***
     * @Description  处理计算式生成正确的表达式
     * @param str 新输入的字符串
     * @author Bu Xinran
     * @date 2023/11/23 16:50
     **/
    public void editFormula(String str){
        if(!str.contains("="))finish=false;
        switch(str){
            case "0": case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": case "(":  case "+": case "-": case ".": case "!":
                formula=formula+str;
                break;
            case ")":
                formula=formula+")";
                if(atPow){
                    atPow=checkPow();
                }
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
            case "log10": case "log": case "log2", "sin", "cos", "tan", "sec", "csc", "cot":
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
            case "x²":
                formula=formula+"^2";
                break;
            case "C":
                formula="";
                answer="";
                answerShow.setText("");
                break;
            case "rand":
                Random rand = new Random();
                formula=formula+String.valueOf(rand.nextInt(900) + 100);
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
            case ",":
                formula=formula+",";
                break;
            case "=":
                formula=formula+"=";
                cntProcess++;
                process.put(cntProcess,formula);
                if(calFlag!=Error.divideZero)checkIllegal();
                finish=true;
                ScientificSolve a=new ScientificSolve(formula,answer,calFlag,process,cntProcess);
                a.setAnswer(a.getResult());
                if(!list.contains(a)){
                    list.add(a);
                }
                pushListToHistory();
                break;
            default:
                if(formula.contains("pow") && !process.get(cntProcess - 1).contains("pow")){
                    atPow=false;
                }
                cntProcess--;
                if(cntProcess==0){
                    formula="";
                    return;
                }
                formula=process.get(cntProcess);
                process.remove(cntProcess);
                return;
        }
        cntProcess++;
        process.put(cntProcess,formula);
    }
    /***
     * @Description  检查当前pow函数是否调用完成
     * @return boolean
     * @author Bu Xinran
     * @date 2023/11/27 16:32
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
        //判断是否直接输入=
        if(formula.length()==1&&formula.charAt(0)=='='){
            calFlag=Error.nothing;
            return;
        }
        //判断调用幂函数的合法性
        if(ScientificController.atPow){
            calFlag=Error.pow;
            return;
        }
        //判断小数点，如果是.1928默认为0.1928
        int checkDot=0;
        for(int i=0;i<formula.length();i++){
            if(!Character.isDigit(formula.charAt(i))&&formula.charAt(i)!='.'){
                checkDot=0;
            }
            if(formula.charAt(i)=='.'){
                checkDot++;
                if(checkDot>1){
                    calFlag=Error.dotRepeat;
                    return;
                } else if(!Character.isDigit(formula.charAt(i-1))){
                    String before=formula.substring(0,i);
                    String after=formula.substring(i);
                    formula=before+"0"+after;
                    i++;
                }
            }
        }
        //判断符号是否使用错误
        int checkSymbol=0;
        if(!formula.isEmpty()){
            switch(formula.charAt(0)){
                case '+':case '-': case '*': case '/': case '%': case '^':
                    calFlag=Error.symbolContinue;
                    return;
            }
        }
        for(int i=0;i<formula.length();i++){
            switch(formula.charAt(i)){
                case '*': case '/': case '+': case '-': case '%':
                    checkSymbol++;
                    break;
                default:
                    checkSymbol=0;
                    break;

            }
            if(checkSymbol>1){
                calFlag=Error.symbolContinue;
                return;
            }
        }
        //除以0
        Jep jep=new Jep();
        String ans ="";
        try{
            jep.parse(formula);
            ans=jep.evaluate().toString();
        }catch(ParseException|EvaluationException e){
            System.out.print(" ");
        }
        if(ans.equals("Infinity")){
            calFlag=Error.divideZero;
            return;
        }else if(ans.length()>1&&ans.charAt(0)=='('){
            calFlag=Error.Illegal;
            return;
        }
        calFlag=Error.yes;
    }
}
