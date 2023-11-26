package NewFunction;
import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;
import com.singularsys.jep.ParseException;
import com.singularsys.jep.functions.PostfixMathCommand;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author sxq
 * @Description:用户自定义函数，实现函数的自定义、存储、保存以及调用
 * @date 2023/11/25 16:07
 */
public class UserFunction {
    /*全局：所有自定义的函数<名称，函数>*/
    public static Map<String,UserFunction> userFunctionMap=new TreeMap<>();
    protected String name;
    /*在一次输入中被使用的次数，注意输入结束置零*/
    private int useTime=0;
    /*参数列表，是否有x y z*/
    protected boolean hasX=false;
    protected boolean hasY=false;
    protected boolean hasZ=false;
    public int x;
    public int y;
    public int z;
    /*传入的表达式*/
    protected String exp;
    public UserFunction(){};
    public UserFunction(String _name,String _exp){
        this.name=_name;
        this.exp=_exp;
    }
    /**
     * @Description  判断函数名是否合法。要求只由数字字母组成，开头为字母，长度<=5
     * @param _name
     * @return boolean
     * @author sxq
     * @date 2023/11/26 11:11
    **/
    public static boolean judgeName(String _name){
        if(_name.length()>5){
            System.out.println("函数名最多5个字符");
            return false;
        }
        if(!_name.matches("[a-zA-Z0-9]")){
            System.out.println("只能包含数字和英文字母");
            return false;
        }
        if(!_name.matches("^[a-zA-Z][a-zA-Z0-9]")){
            System.out.println("名称必须以字母开头");
            return false;
        }
        if(_name.matches("^[a-zA-Z][a-zA-Z0-9]{0,4}$")){
            return true;
        }
        System.out.println("名称不合法");
        return false;
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
    public static boolean judgeFunction(String _exp){
        //删除空格
        if(_exp==null){
            System.out.println("表达式为空");
            return false;
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
            System.out.println("表达式不合法，解析失败");
            return false;
        }
        //判断是否存在未定义的变量等不合法表达
        System.out.println("函数表达式合法");
        return true;
    }
    /**
     * @Description 新建自定义函数，加入函数map
     * @return boolean       true：成功 false：失败
     * @author sxq
     * @date 2023/11/25 22:19
     **/
    public boolean addFunction(){

        if(judgeFunction(this.name)){
            System.out.println("函数名重复");
            return false;
        }
        if(!judgeFunction(this.exp)){
            System.out.println("自定义函数不合法！");
            return false;
        }
        //添加到自定义函数
       userFunctionMap.put(this.name,this);

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
    /***
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
//    public double getRes(double x){
//
//    }

}


