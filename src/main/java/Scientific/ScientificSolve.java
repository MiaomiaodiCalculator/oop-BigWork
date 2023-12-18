package Scientific;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Bu Xinran
 * @Description 处理科学计算器得出的结果
 * @date 2023/11/23 16:12
 */
public class ScientificSolve implements Serializable {
    protected String formula="";
    protected String exp="";
    protected ErrorScientific calFlag= ErrorScientific.yes;
    protected String answer;
    protected List<String> process=new ArrayList<>();
    protected List<String> processExp=new ArrayList<>();
    private Timestamp saveTime;
    /***
     * @Description 构造方法
     * @param formula 计算公式
     * @param answer 计算结果
     * @param calFlag 计算式是否正确
     * @author Bu Xinran
     * @date 2023/11/27 14:15
     **/
    public ScientificSolve(String formula, String answer, ErrorScientific calFlag, List<String> process,List<String> processExp,String exp){
        this.formula=formula;
        this.answer=answer;
        this.calFlag=calFlag;
        this.process=process;
        this.processExp=processExp;
        this.exp=exp;
    }
    /***
     * @Description getter函数，获得formula公式
     * @return java.lang.String
     * @author Bu Xinran
     * @date 2023/11/27 15:10
     **/
    public String getFormula() {
        return formula;
    }
    /***
     * @Description   getter函数，获得exp公式
     * @return java.lang.String
     * @author Bu Xinran
     * @date 2023/11/28 15:46
    **/
    public String getExp(){return exp;}
    /***
     * @Description getter函数，获得运算结果
     * @return String
     * @author Bu Xinran
     * @date 2023/11/27 15:11
     **/
    public String getResult() {
        if(calFlag== ErrorScientific.symbolContinue)return "运算符连续";
        else if(calFlag== ErrorScientific.dotRepeat)return "小数点重复";
        else if(calFlag== ErrorScientific.bracket)return "括号不匹配";
        else if(calFlag== ErrorScientific.pow)return "幂运算函数未完成";
        else if(calFlag== ErrorScientific.divideZero)return "除数等于0";
        else if(calFlag== ErrorScientific.Illegal)return "算式非法";
        else if(calFlag== ErrorScientific.nothing)return "还未输入算式";
        else if(calFlag==ErrorScientific.BrackNull)return "括号内为空";
        if(calFlag==ErrorScientific.error)return "运算式错误！";
        else return answer;
    }
    /***
     * @Description  获得计算式结果
     * @param answer 计算式结果（包含错误处理）
     * @author Bu Xinran
     * @date 2023/11/28 10:10
     **/
    public void setAnswer(String answer){
        this.answer=answer;
    }
    /***
     * @Description   获得计算过程式并将其转化为StringProperty形式
     * @return javafx.beans.property.StringProperty
     * @author Bu Xinran
     * @date 2023/11/28 10:11
     **/
    public StringProperty formulaProperty() {
        return new SimpleStringProperty(formula);
    }
    /**
     * @Description 获得计算结果并将其转化为StringProperty形式
     * @return null
     * @author Bu Xinran
     * @date 2023/11/28 10:11
     **/
    public ObservableValue<String> answerProperty() {
        return new SimpleStringProperty(answer);
    }
    /***
     * @Description   获得历史记录中的计算过程
     * @return java.util.LinkedHashMap<java.lang.Integer,java.lang.String>
     * @author Bu Xinran
     * @date 2023/11/28 10:13
     **/
    public List<String> getProcess(){
        return process;
    }
    /***
     * @Description  判断输入的字符是否合法
     * @param text 输入的字符
     * @return boolean
     * @author Bu Xinran
     * @date 2023/11/28 22:56
    **/
    public static boolean checkText(String text){
        return text != null && text.matches("\\d+(\\.\\d+)?");
    }
    /***
     * @Description   计算式过程
     * @return java.util.List<java.lang.String>  字符串list
     * @author Bu Xinran
     * @date 2023/12/18 12:46
    **/
    public List<String> getProcessExp() {return processExp;}
    /***
     * @Description 计算时间
     * @param time  时间
     * @author Bu Xinran
     * @date 2023/12/18 12:46
    **/
    public void setSaveTime(Timestamp time) {this.saveTime = time;}
    /***
     * @Description  返回时间戳
     * @return java.sql.Timestamp 时间戳
     * @author Bu Xinran
     * @date 2023/12/18 12:45
    **/
    public Timestamp getSaveTime() {return saveTime;}
    /***
     * @Description   返回时间戳
     * @return javafx.beans.value.ObservableValue<java.lang.String>     时间字符串
     * @author Bu Xinran
     * @date 2023/12/18 12:45
    **/
    public ObservableValue<String> timeProperty() {return new SimpleStringProperty(String.valueOf(saveTime));}
    /***
     * @Description 返回错误类型
     * @return Scientific.ErrorScientific   错误类型
     * @author Bu Xinran
     * @date 2023/12/18 12:45
    **/
    public ErrorScientific getCalFlag() {
        return calFlag;
    }
}
