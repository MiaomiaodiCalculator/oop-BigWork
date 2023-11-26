package scientific;
import com.calculator.calculation.ScientificController;

/**
 * @author Bu Xinran
 * @Description 处理科学计算器得出的结果
 * @date 2023/11/23 16:12
 */
public class ScientificSolve {
    protected String formula="";
    protected Error calFlag=Error.yes;
    protected double answer;
    public ScientificSolve(String formula){
        this.formula=formula;
    }
}
