package infinitesimal;

import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;
import com.singularsys.jep.ParseException;

/**
 * @author sxq
 * @Description: 获取微积分计算结果
 * @date 2023/11/25 15:52
 */
public class InfinitesimalSolve {
    /*上限&下限*/
    private double upValue;
    private double downValue;
    /*被积函数式*/
    private String exp;
    public InfinitesimalSolve(double u,double d,String e){
        this.upValue=u;
        this.downValue=d;
        this.exp=e;
    }
    public double getRes(){
        Jep jep=new Jep();
        try {
            jep.addVariable("$x$");
            jep.parse(exp);
        } catch (ParseException e) {
            System.out.println("表达式不合法!");
            throw new RuntimeException(e);
        }
        double sum=0;
        double e=(upValue-downValue)/100000.0;
        for(int i=1;i<=100000;i++){
            double x=i*e+downValue;
            try {
                jep.addVariable("$x$",x);
                double r= jep.evaluateD();
                sum+=r;
            } catch (JepException ex) {
                throw new RuntimeException(ex);
            }
        }
        return sum*e;
    }

}
