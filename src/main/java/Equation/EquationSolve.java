package Equation;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author Bu Xinran
 * @Description 创建方程式类已保存历史记录
 * @date 2023/11/29 22:27
 */
public class EquationSolve {
    private final EquationError calFlag;
    private final String showEquation;
    private final String equation;
    public EquationSolve(EquationError calFlag,String showEquation,String equation){
        this.calFlag=calFlag;
        this.showEquation=showEquation;
        this.equation=equation;
    }
    /***
     * @Description  计算方程式的结果
     * @param equation 方程式
     * @return double[]
     * @author Bu Xinran
     * @date 2023/11/29 23:00
    **/
    public static List<Double> getAnswer(String equation) {
        double[] d = shiftEquation(equation);
        UnivariateDifferentiableFunction function = new PolynomialFunction(d);
        System.out.println(function);
        UnivariateDifferentiableSolver solver = new NewtonRaphsonSolver();
        List<Double> res = new ArrayList<>();
        double initialGuess = 0;
        double epsilon = 1e-6;
        while (true) {
            double solution = solver.solve(1000, function, initialGuess);
            res.add(solution);
            initialGuess = solution + 1;
            if (Math.abs(function.value(solution)) < epsilon) {
                break;
            }
        }
        for (int i = 0; i < res.size(); i++) {
            BigDecimal bd = new BigDecimal(res.get(i)).setScale(4, RoundingMode.HALF_UP);
            res.set(i, bd.doubleValue());
        }
        return res;
    }
    /***
     * @Description  将多项式转换为数组形式
     * @param equation 多项式的表达式
     * @return double[]
     * @author Bu Xinran
     * @date 2023/11/30 10:37
    **/
    private static double[] shiftEquation(String equation) {
        String[] terms = equation.split("(?=[+-])");
        double[] d=new double[60];
        for(int i=0;i<60;i++){
            d[i]=0;
        }
        for(String term:terms){
            String[] result = new String[3];
            String[] parts = term.split("[x*]");
            //系数部分
            if (parts.length > 0) {
                if(parts[0].equals("+")|| parts[0].isEmpty())result[0]="1";
                else if(parts[0].equals("-1"))result[0]="-1";
                else result[0] = parts[0];
            }
            //是否有x
            if (term.contains("x")) {
                result[1] = "x";
            } else {
                result[1] = "";
            }
            //是否有幂次
            if (term.contains("^")) {
                result[2] = parts[1].substring(1); // 幂次部分
            } else {
                result[2] = "(1)"; // 如果没有^符号，则幂次为1
            }
            if(result[1].isEmpty())d[0]+=Double.parseDouble(result[0]);
            else{
                result[2]=result[2].substring(1,result[2].length()-1);
                d[Integer.parseInt(result[2])]+=Double.parseDouble(result[0]);
            }
        }
        return d;
    }
}
