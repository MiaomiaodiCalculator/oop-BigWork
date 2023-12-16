package com.calculator.calculation;
import Database.SqlScientific;
import NewFunction.UserFunction;
import com.singularsys.jep.*;
import com.singularsys.jep.ParseException;
import com.singularsys.jep.functions.Abs;
import com.singularsys.jep.functions.Ceil;
import com.singularsys.jep.functions.Floor;
import com.singularsys.jep.functions.PostfixMathCommand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import Scientific.ScientificSolve;
import Scientific.ErrorScientific;
import com.singularsys.jep.Jep;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import static com.calculator.calculation.FunctionController.functionList;

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
    public TableView FunctionList;
    public TableColumn nameList;
    public TableColumn paraNumList;
    public TableColumn formulaList1;
    public TextField searchField;
    public VBox FreeShow;
    public ListView listView;
    public StackPane searchResult;
    public StackPane stackPane;
    public TableColumn<ScientificSolve,String> timeList;
    private ArrayList<ScientificSolve> list =new ArrayList<>();
    public TableView<ScientificSolve> tableView;
    public TableColumn<ScientificSolve, String> formulaList;
    public TableColumn<ScientificSolve, String> answerList;
    /**formula是展示在前端界面上的计算式*/
    private String formula="";
    /**可直接计算的计算式*/
    private String exp="";
    private ErrorScientific calFlag= ErrorScientific.yes;
    private String answer="";
    private List<String> process=new ArrayList<>();
    private List<String> processExp=new ArrayList<>();
    public static boolean atPow=false;
    private boolean finish=false;
    public String[] downSymbol =new String[]{"⁰","¹","²","³","⁴","⁵","⁶","⁷","⁸","⁹","˙"};
    /***
     * @Description  初始化科学计算器界面
     * @param location 所在位置
     * @param resources 事件
     * @author Bu Xinran
     * @date 2023/11/27 10:54
     **/
    public void initialize(URL location, ResourceBundle resources) {
        for (UserFunction userFunction : functionList) {
            FunctionList.getItems().add(userFunction);
        }
        FunctionList.setPlaceholder(new Label("暂无自定义函数"));//占位文本
        nameList.setCellValueFactory(new PropertyValueFactory<>("name"));
        paraNumList.setCellValueFactory(new PropertyValueFactory<>("paraNum"));
        formulaList1.setCellValueFactory(new PropertyValueFactory<>("formula"));
        // 初始化时，显示第一个卡片，隐藏第二个卡片
        Scientific.setVisible(true);
        History.setVisible(false);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchListener();
        });
    }
    /***
     * @Description  跳转到历史记录界面
     * @param event 点击事件
     * @author Bu Xinran
     * @date 2023/11/27 10:09
     **/
    @FXML
    private void handleHisImageClick(MouseEvent event) throws IOException {
        list= SqlScientific.getAllHis();
        tableView.setPlaceholder(new Label("无历史记录"));//占位文本
        timeList.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        formulaList.setCellValueFactory(cellData -> cellData.getValue().formulaProperty());
        answerList.setCellValueFactory(cellData -> cellData.getValue().answerProperty());
        ObservableList<ScientificSolve> observableList = FXCollections.observableArrayList(list);
        tableView.setItems(observableList);
        Scientific.setVisible(false);
        History.setVisible(true);
    }
    /***
     * @Description  通过鼠标点击哪一个自定义函数来调用
     * @param event  鼠标点击了tableview的哪一行
     * @author Bu Xinran
     * @date 2023/11/28 14:45
     **/
    public void chooseFunction(MouseEvent event) throws ParseException, EvaluationException {
        if (event.getClickCount() == 2) {
            UserFunction selectedItem = (UserFunction)(FunctionList.getSelectionModel().getSelectedItem());
            selectFunction(selectedItem);
        }
    }
    /***
     * @Description  调用自定义函数
     * @param selectedItem  选中的自定义函数
     * @author Bu Xinran
     * @date 2023/12/9 11:44
     **/
    public void selectFunction(UserFunction selectedItem) throws ParseException, EvaluationException {
        if (selectedItem != null) {
            String title="正在调用自定义函数'"+selectedItem.getName()+"'";
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().getScene().getStylesheets().add(getClass().getResource("style/dialog.css").toExternalForm());
            dialog.getDialogPane().getStyleClass().add("alert");
            dialog.setTitle(title);
            dialog.setHeaderText("请输入各变量的值：");
            int num=selectedItem.getParaNum();
            GridPane gridPane = new GridPane();
            TextField textX = null;
            TextField textY=null;
            TextField textZ=null;
            if(num>=1){
                textX = new TextField();
                gridPane.add(textX, 1, 0);
                gridPane.add(new Label("X:"), 0, 0);
            }
            if(num>=2){
                textY= new TextField();
                gridPane.add(textY, 1, 1);
                gridPane.add(new Label("Y:"), 0, 1);
            }
            if(num>=3){
                textZ= new TextField();
                gridPane.add(textZ, 1, 2);
                gridPane.add(new Label("Z:"), 0, 2);
            }
            dialog.getDialogPane().setContent(gridPane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean checkError;
                String valueX;
                String valueY = null;
                String valueZ = null;
                if(num==1){
                    valueX=textX.getText();
                    checkError=ScientificSolve.checkText(valueX);
                }else if(num==2){
                    valueX=textX.getText();
                    valueY=textY.getText();
                    boolean a=ScientificSolve.checkText(valueX);
                    if(!a) checkError=false;
                    else checkError=ScientificSolve.checkText(valueY);
                }else{
                    valueX=textX.getText();
                    valueY=textY.getText();
                    valueZ=textY.getText();
                    boolean a=ScientificSolve.checkText(valueX);
                    boolean b=ScientificSolve.checkText(valueY);
                    if(!a) checkError=false;
                    else if(!b) checkError=false;
                    else checkError=ScientificSolve.checkText(valueZ);
                }
                if(!checkError){
                    Dialog alert = new Dialog();
                    alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("style/dialog.css").toExternalForm());
                    alert.getDialogPane().getStyleClass().add("alertFunction");
                    alert.setTitle("警告");
                    alert.setHeaderText(null);
                    alert.setContentText("存在参数值为空或不是数字！");
                    alert.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                    alert.showAndWait();
                }else{
                    //处理正确获得的表达式
                    if(num==1){
                        formula=formula+selectedItem.getName()+"("+valueX+")";
                        exp=exp+selectedItem.getRes(Double.parseDouble(valueX)).toString();
                    }else if(num==2){
                        formula=formula+selectedItem.getName()+"("+valueX+","+valueY+")";
                        exp=exp+selectedItem.getRes(Double.parseDouble(valueX),Double.parseDouble(valueY)).toString();
                    }else{
                        formula=formula+selectedItem.getName()+"("+valueX+","+valueY+","+valueZ+")";
                        exp=exp+selectedItem.getRes(Double.parseDouble(valueX),Double.parseDouble(valueY),Double.parseDouble(valueY)).toString();
                    }
                    formulaShow.setText(formula);
                    tackleError();
                    dialog.close();
                    FreeShow.setVisible(false);
                    listView.setVisible(false);
                }
            }
        }
    }
    /***
     * @Description  通过判断鼠标点击哪一行跳转回编辑界面
     * @param event 鼠标点击了tableview的哪一行
     * @author Bu Xinran
     * @date 2023/11/27 23:59
     **/
    public void handleRowClick(MouseEvent event) {
        // 判断是否双击行
        if (event.getClickCount() == 2&&event.getButton()== MouseButton.PRIMARY) {
            ScientificSolve selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String toFormula=selectedItem.getFormula();
                String toAnswer=selectedItem.getResult();
                exp=selectedItem.getExp();
                Scientific.setVisible(true);
                History.setVisible(false);
                formula=toFormula;
                formulaShow.setText(formula);
                answer=toAnswer;
                answerShow.setText(answer);
                process=new ArrayList<>(selectedItem.getProcess());
                processExp=new ArrayList<>(selectedItem.getProcessExp());
            }
        } else if(event.getButton()==MouseButton.SECONDARY){//右键单击选择是否删除
            ScientificSolve ift = tableView.getSelectionModel().getSelectedItem();
            if(SqlScientific.exists(ift.getSaveTime())){
                Dialog alert1 = new Dialog();
                alert1.getDialogPane().getScene().getStylesheets().add(getClass().getResource("style/dialog.css").toExternalForm());
                alert1.getDialogPane().getStyleClass().add("alertFunction");
                alert1.setContentText("删除这条历史记录？");
                alert1.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                Optional<ButtonType> result = alert1.showAndWait();
                if (result.get() == ButtonType.OK){
                    SqlScientific.delete(ift);
                    getHistoryList();
                }
            }
        }
    }
    private void getHistoryList(){
        list=SqlScientific.getAllHis();
        tableView.getItems().setAll(list);
        tableView.refresh();
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
        FreeShow.setVisible(!FreeShow.isVisible());
    }
    /***
     * @Description 产生错误后的后续处理
     * @author Bu Xinran
     * @date 2023/11/26 11:01
     **/
    private void tackleError() throws ParseException, EvaluationException {
        if(calFlag== ErrorScientific.bracket){
            answerShow.setText("括号不匹配！");
        }else if(calFlag== ErrorScientific.symbolContinue){
            answerShow.setText("运算符使用错误！");
        }else if(calFlag== ErrorScientific.yes){
            setAnswer();
            if(!answer.isEmpty()){
                double ans=Double.parseDouble(answer);
                DecimalFormat df = new DecimalFormat("0.#######");
                if (ans > 1e5) {
                    df.applyPattern("0.#######E0");
                }
                String show = df.format(ans);
                if(show.contains("E")){
                    String[] cmd=show.split("E");
                    System.out.println(Arrays.toString(cmd));
                    show=cmd[0]+"×10";
                    char[] charArray = cmd[1].toCharArray();
                    for (char c : charArray) {
                        show=show+downSymbol[c-'0'];
                    }
                }
                answerShow.setText(show);
            }else{
                answerShow.setText("");
            }
        }else if(calFlag== ErrorScientific.dotRepeat){
            answerShow.setText("小数点重复，请删除！");
        }else if(calFlag== ErrorScientific.nothing){
            answerShow.setText("还未输入算式");
        }else if(calFlag== ErrorScientific.divideZero){
            answerShow.setText("不能除以0！");
        }else if(calFlag== ErrorScientific.Illegal){
            answerShow.setText("算式非法！");
        }
    }
    /***
     * @Description 实时获得计算式的值
     * @author Bu Xinran
     * @date 2023/11/26 11:37
     **/
    private void setAnswer() throws EvaluationException {
        Jep jep=new Jep();
        jep.addFunction("abs", new Abs());
        jep.addFunction("floor",new Floor());
        jep.addFunction("ceil",new Ceil());
        try{
            jep.parse(exp);
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
                exp=exp+str;
                break;
            case ")":
                formula=formula+")";
                exp=exp+")";
                if(atPow){
                    atPow=checkPow();
                }
                break;
            case "×":
                formula=formula+"×";
                exp=exp+"*";
                break;
            case "÷":
                formula=formula+"÷";
                exp=exp+"/";
                break;
            case "mod":
                formula=formula+"%";
                exp=exp+"%";
                break;
            case "log₁₀":
                formula=formula+"log₁₀(";
                exp=exp+"log(";
                break;
            case "ln": case  "sin", "cos", "tan", "sec", "csc", "cot":
                formula=formula+str+"(";
                exp=exp+str+"(";
                break;
            case "10ˣ":
                formula=formula+"10^(";
                exp=exp+"10^(";
                break;
            case "xⁿ":
                formula=formula+"^(";
                exp=exp+"^(";
                break;
            case "√":
                formula=formula+"sqrt(";
                exp=exp+"sqrt(";
                break;
            case "³√":
                formula=formula+"^(1/3)";
                exp=exp+"^(1/3)";
                break;
            case "eˣ":
                formula=formula+"exp(";
                exp=exp+"exp(";
                break;
            case "x³":
                formula=formula+"³";
                exp=exp+"^3";
                break;
            case "π":
                formula=formula+"π";
                exp=exp+"pi";
                break;
            case "e":
                formula=formula+"e";
                exp=exp+"e";
                break;
            case "x²":
                formula=formula+"²";
                exp=exp+"^2";
                break;
            case "C":
                formula="";
                answer="";
                calFlag=ErrorScientific.yes;
                answerShow.setText("");
                process.clear();
                processExp.clear();
                break;
            case "⌊x⌋":
                formula=formula+"floor(";
                exp=exp+"floor(";
                break;
            case "⌈x⌉":
                formula=formula+"ceil(";
                exp=exp+"ceil(";
                break;
            case "|x|":
                formula=formula+"abs(";
                exp=exp+"abs(";
                break;
            case ",":
                formula=formula+",";
                exp=exp+",";
                break;
            case "=":
                formula=formula+"=";
                exp=exp+"=";
                process.add(formula);
                processExp.add(exp);
                if(calFlag!= ErrorScientific.divideZero)checkIllegal();
                finish=true;
                ScientificSolve a=new ScientificSolve(formula,answer,calFlag,process,processExp,exp);
                a.setAnswer(a.getResult());
                if(!list.contains(a)){
                    list.add(a);
                }
                SqlScientific.add(a);
                break;
            default:
                if(process.size()<=1){
                    formula="";
                    exp="";
                    answer="";
                    return;
                }
                formula=process.get(process.size() - 2);
                exp=processExp.get(processExp.size() - 2);
                process.remove(process.size() - 1);
                processExp.remove(processExp.size() - 1);
                return;
        }
        process.add(formula);
        processExp.add(exp);
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
                calFlag= ErrorScientific.bracket;
                return;
            }
        }
        //判断是否直接输入=
        if(formula.length()==1&&formula.charAt(0)=='='){
            calFlag= ErrorScientific.nothing;
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
                    calFlag= ErrorScientific.dotRepeat;
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
                    calFlag= ErrorScientific.symbolContinue;
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
                calFlag= ErrorScientific.symbolContinue;
                return;
            }
        }
        //除以0
        Jep jep=new Jep();
        String ans ="";
        try{
            jep.parse(exp);
            ans=jep.evaluate().toString();
        }catch(ParseException | EvaluationException e){
            System.out.print(" ");
        }
        if(ans.equals("Infinity")){
            calFlag= ErrorScientific.divideZero;
            return;
        }else if(ans.length()>1&&ans.charAt(0)=='('){
            calFlag= ErrorScientific.Illegal;
            return;
        }
        calFlag= ErrorScientific.yes;
    }
    /***
     * @Description  实时更新搜索框
     * @author Bu Xinran
     * @date 2023/11/29 0:23
     **/
    public void searchListener() {
        String search=searchField.getText();
        System.out.println(search);
        List<String> searchList=new ArrayList<>();
        for(UserFunction key:functionList){
            if(key.getName().contains(search)&&searchList.size()<=7){
                searchList.add(key.getName());
            }
        }
        Collections.sort(searchList);
        ObservableList<String> items = FXCollections.observableArrayList(searchList);
        listView.setItems(items);
        listView.setPrefHeight(searchList.size()* listView.getFixedCellSize());
        searchResult.setVisible(true);
        if(searchList.isEmpty()){
            searchResult.setVisible(false);
        }
        searchList.clear();
    }
    /***
     * @Description 监听搜索提示框中鼠标动作
     * @author Bu Xinran
     * @date 2023/12/9 11:40
     **/
    public void MouseChoose() {
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // 双击事件
                String selectedItem = (String)(listView.getSelectionModel().getSelectedItem());
                for(UserFunction key:functionList){
                    if(key.getName().equals(selectedItem)){
                        try {
                            selectFunction(key);
                            searchResult.setVisible(false);
                            searchField.setText("");
                        } catch (ParseException | EvaluationException e) {
                            System.out.println("Mouse:error");
                        }
                    }
                }
            }
        });
    }
    /***
     * @Description 监听搜索提示框中的键盘事件
     * @author Bu Xinran
     * @date 2023/12/9 11:41
     **/
    public void KeyChoose() {
        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                int nextIndex = listView.getSelectionModel().getSelectedIndex();
                if (nextIndex <=7) {
                    listView.getSelectionModel().select(nextIndex);
                }
            } else if (event.getCode() == KeyCode.UP) {
                int prevIndex = listView.getSelectionModel().getSelectedIndex();
                if (prevIndex >= 0) {
                    listView.getSelectionModel().select(prevIndex);
                }
            }else if(event.getCode()==KeyCode.ENTER){
                String selectedItem = (String)(listView.getSelectionModel().getSelectedItem());
                for(UserFunction key:functionList){
                    if(key.getName().equals(selectedItem)){
                        try {
                            selectFunction(key);
                            searchField.setText("");
                            searchResult.setVisible(false);
                        } catch (ParseException | EvaluationException e) {
                            System.out.println("Key:error");
                        }
                    }
                }
            }
        });
    }
    /***
     * @Description  使用键盘DOWN跳转到搜索提示框
     * @author Bu Xinran
     * @date 2023/12/9 13:56
     **/
    public void KeyDown() {
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                listView.requestFocus();
                listView.getSelectionModel().selectFirst();
                event.consume();
            }
        });
    }
}