package NewFunction;
import com.calculator.calculation.FunctionController;
import com.singularsys.jep.EvaluationException;
import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;
import com.singularsys.jep.ParseException;
import java.util.*;

/**
 * @author sxq
 * @Description:用户自定义函数，实现函数的自定义、存储、保存以及调用
 * @date 2023/11/25 16:07
 */
public class UserFunction {
    /*全局：所有自定义的函数<名称，函数>*/
    public static Map<String,UserFunction> userFunctionMap=new TreeMap<>();
    private String name;
    private int paraNum;
    /*在一次输入中被使用的次数，注意输入结束置零*/
    private int useTime=0;
    protected Jep jep;
    /*参数列表，是否有x y z*/
    protected boolean hasX=false;
    protected boolean hasY=false;
    protected boolean hasZ=false;
    /*展示出的表达式，可能有嵌套存在*/
    private   String formula;
    /*传入的表达式，可直接计算*/
    private String exp;
    public UserFunction(){};
    public UserFunction(String _name,String _exp,String _formula){
        this.name=_name;
        this.exp=_exp;
        this.formula=_formula;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParaNum() {
        return paraNum;
    }

    public void setParaNum(int paraNum) {
        this.paraNum = paraNum;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    /**
     * @Description  判断函数名是否合法。要求只由数字字母组成，开头为字母，长度<=5
     * @param _name
     * @return boolean
     * @author sxq
     * @date 2023/11/26 11:11
    **/
    public static String judgeName(String _name){
        if(_name.length()>5){
           return "函数名最多5个字符";
        }
        if(_name.matches("^[a-zA-Z][a-zA-Z0-9]{0,4}$")){
            return "true";
        }
        if(!_name.matches("[a-zA-Z0-9]")){
           return ("函数名只能包含数字和英文字母");
        }
        if(!_name.matches("^[a-zA-Z][a-zA-Z0-9]")){
            return ("函数名必须以字母开头");
        }

       return ("名称不合法，未知错误");
    }
    /**
     * @Description 判断新自定义函数的名称是否重复
 * @param _name
 * @return boolean
     * @author sxq
     * @date 2023/11/26 10:29
    **/
    public static boolean judgeNameRepeat(String _name){
        return (UserFunction.userFunctionMap.containsKey(_name));
    }
    /**
     * @Description 判断输入的函数表达式是否合法：支持隐式乘法；支持除0；支持exp；不支持除x y z 外的参数;嵌套自定义已解开
     * @param _exp 函数
     * @return boolean 是否合法
     * @author sxq
     * @date 2023/11/25 21:42
     **/
    public static String judgeFunction(String _exp){
        //删除空格
        if(_exp==null){
            return ("表达式为空");
        }
        _exp=_exp.replaceAll(" ","");
        Jep jep =new Jep();
        jep.addVariable("$x$");
        jep.addVariable("$y$");
        jep.addVariable("$z$");
        jep.setAllowUndeclared(false);
        //限定变量为x y z，添加其他变量则报错
        jep.setAllowAssignment(false);
        //不允许赋值方程
        try{
            jep.parse(_exp);
        }catch(ParseException e){
            return ("表达式不合法，解析失败");
        }
        //判断是否存在未定义的变量等不合法表达
        return "true";
    }
    /**
     * @Description 新建自定义函数，加入函数map；已在controller中做合法判定
     * @return boolean       true：成功 false：失败
     * @author sxq
     * @date 2023/11/25 22:19
     **/
    public boolean addFunction(){
        //添加到自定义函数
       userFunctionMap.put(this.name,this);
       FunctionController.functionList.add(this);
       return true;
    }
    /***
     * @Description 获取参数列表
     * @author sxq
     * @date 2023/11/26 10:51
    **/
    public void getPara(){
        if(this.exp.contains("$x$")){
            hasX=true;
        }
        if(this.exp.contains("$y$")){
            hasY=true;
        }
        if(this.exp.contains("$z$")){
            hasZ=true;
        }
    }
    /**
     * @Description 用于自定义函数嵌套时替换参数
 * @return java.lang.String       返回替换后的表达式
     * @author sxq
     * @date 2023/11/26 10:36
    **/
    public String changePara(){
        String replace="$"+this.name+"_"+this.useTime+"_";
        this.exp=this.exp.replace("$x$",replace+"x");
        this.exp=this.exp.replace("$y$",replace+"y");
        this.exp=this.exp.replace("$z$",replace+"z");
        return this.exp;
    }
    /**
     * @Description 参数为1时计算 
 * @param x 
 * @return java.lang.Object       
     * @author sxq
     * @date 2023/11/28 12:41
    **/
    public Object getRes(double x) {
        Jep jep1=new Jep();
        try {
            jep1.addVariable("$x$",x);
            jep1.parse(exp);
        } catch (JepException e) {
            throw new RuntimeException(e);
        }
        try {
            return jep1.evaluate();
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * @Description 参数为2时计算结果 
 * @param x
 * @param y 
 * @return java.lang.Object       
     * @author sxq
     * @date 2023/11/28 12:43
    **/
    
    public Object getRes(double x,double y){
        Jep jep1=new Jep();
        try{
            jep1.addVariable("$x$",x);
            jep1.addVariable("$y$",y);
            jep1.parse(exp);
        }catch (JepException e){
            throw  new RuntimeException(e);
        }
        try{
            return jep1.evaluate();
        }catch (EvaluationException e){
            throw new RuntimeException(e);
        }
    }
    /**
     * @Description 参数为3时计算结果 
 * @param x
 * @param y
 * @param z 
 * @return java.lang.Object       
     * @author sxq
     * @date 2023/11/28 12:44
    **/
    public Object getRes(double x,double y,double z){
        Jep jep1=new Jep();
        try{
            jep1.addVariable("$x$",x);
            jep1.addVariable("$y$",y);
            jep1.addVariable("$z$",z);
            jep1.parse(exp);
        }catch (JepException e){
            throw  new RuntimeException(e);
        }
        try{
            return jep1.evaluate();
        }catch (EvaluationException e){
            throw new RuntimeException(e);
        }
    }
}