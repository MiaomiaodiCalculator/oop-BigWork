package infinitesimal;

import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;
import com.singularsys.jep.ParseException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sxq
 * @Description 获取微积分计算结果
 * @date 2023/11/25 15:52
 */
public class InfinitesimalSolve {
    /*上限&下限*/
    private double upValue;
    private double downValue;
    /*被积函数式*/
    private String formula;
    private String exp;

    /*储存formula的编辑过程*/
    private  List<String> formulaProcess = new ArrayList<>();
    /*储存exp的编辑过程*/
    private  List<String> expProcess = new ArrayList<>();
    /*积分结果*/
    private double result;
    private Timestamp saveTime;
    public InfinitesimalSolve(double u,double d,String f,String e,List<String> fp,List<String> ep){
        this.upValue=u;
        this.downValue=d;
        this.formula=f;
        this.exp=e;
        this.formulaProcess=fp;
        this.expProcess=ep;
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
        result=sum*e;
        return result;
    }

    public double getUpValue() {
        return upValue;
    }

    public void setUpValue(double upValue) {
        this.upValue = upValue;
    }

    public double getDownValue() {
        return downValue;
    }

    public void setDownValue(double downValue) {
        this.downValue = downValue;
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

    public List<String> getFormulaProcess() {
        return formulaProcess;
    }

    public void setFormulaProcess(List<String> formulaProcess) {
        this.formulaProcess = formulaProcess;
    }

    public void setExpProcess(List<String> expProcess) {
        this.expProcess = expProcess;
    }

    public List<String> getExpProcess() {
        return expProcess;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public Timestamp getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Timestamp saveTime) {
        this.saveTime = saveTime;
    }
}
