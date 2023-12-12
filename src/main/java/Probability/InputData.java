package Probability;

/**
 * @author 郑悦
 * @Description: 用于表格数据展示
 * @date 2023/12/4 0:14
 */
public class InputData {
    // 展示basicSolve部分统计原始数据
    public double input1;
    public double input2;

    // 展示回归分析部分的回归参数
    public double residual;
    public double MSE;
    public double r;
    public double adjustedR;
    public InputData(double data1, double data2) {
        input1 = data1;
        input2 = data2;
        residual = 0;
        MSE = 0;
        r = 0;
        adjustedR = 0;
    }
    public InputData(double residual0, double MSE0, double r0) {
        residual = residual0;
        MSE = MSE0;
        r = r0;
//        adjustedR = adjustedR0;
    }
    /**
     * @Description 给表格显示提高read方法
     * @return double
     * @author 郑悦
     * @date 2023/12/4 0:14
    **/
    public double getInput1() {
        return input1;
    }

    public double getInput2() {
        return input2;
    }

    public double getMSE() {
        return MSE;
    }

    public double getR() {
        return r;
    }

    public double getAdjustedR() {
        return adjustedR;
    }

    public double getResidual() {
        return residual;
    }
}
