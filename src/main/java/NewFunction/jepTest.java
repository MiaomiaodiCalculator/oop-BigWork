package NewFunction;

import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;

public class jepTest {

    public static void main(String[] args) {

        try {
            Jep jep=new Jep();
            jep.addVariable("$x$");
            jep.addVariable("y");
            jep.addVariable("z");
            jep.setAllowUndeclared(false);
            //常规公式
            String formula1 = "($x$)";
            jep.parse(formula1);
           // System.out.println("测试公式1："+jep.evaluate().toString());
            //公式中分母为0
            String formula2 = "3/0";
            jep.parse(formula2);
            System.out.println("测试公式2："+jep.evaluate().toString());

            //布尔表达式
            String formula3 = "3>=0";
            jep.parse(formula3);
            System.out.println("测试公式3："+jep.evaluate().toString());

            //逻辑运算
//            String formula4 = "1&&0";
//            String formula4 = "1||0";
            String formula4 = "1!=1";
            jep.parse(formula4);
            System.out.println("测试公式4："+jep.evaluate().toString());

        } catch (JepException e) {
            e.printStackTrace();
        }


    }

}

