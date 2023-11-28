package scientific;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * @author Bu Xinran
 * @Description 处理科学计算器得出的结果
 * @date 2023/11/23 16:12
 */
public class ScientificSolve implements Serializable {
    protected String formula="";
    protected String exp="";
    protected Error calFlag=Error.yes;
    protected String answer;
    protected LinkedHashMap<Integer,String> process=new LinkedHashMap<>();
    protected int cntProcess=0;
    /***
     * @Description 构造方法
     * @param formula 计算公式
     * @param answer 计算结果
     * @param calFlag 计算式是否正确
     * @author Bu Xinran
     * @date 2023/11/27 14:15
     **/
    public ScientificSolve(String formula,String answer,Error calFlag,LinkedHashMap<Integer,String> process,int cntProcess,String exp){
        this.formula=formula;
        this.answer=answer;
        this.calFlag=calFlag;
        this.process=process;
        this.cntProcess=cntProcess;
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
        if(calFlag==Error.symbolContinue)return "运算符连续";
        else if(calFlag==Error.dotRepeat)return "小数点重复";
        else if(calFlag==Error.bracket)return "括号不匹配";
        else if(calFlag==Error.pow)return "幂运算函数未完成";
        else if(calFlag==Error.divideZero)return "除数等于0";
        else if(calFlag==Error.Illegal)return "算式非法";
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
    public LinkedHashMap<Integer,String> getProcess(){
        return process;
    }
    /***
     * @Description   获得历史记录中给的编辑步数
     * @return int
     * @author Bu Xinran
     * @date 2023/11/28 10:13
     **/
    public int getCntProcess(){
        return cntProcess;
    }
}
