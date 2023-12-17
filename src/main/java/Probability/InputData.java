package Probability;

import java.sql.Timestamp;

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
    /**用户输入的数字序列1*/
    private String data1;
    /**用户输入的数字序列2*/
    private String data2;
    private Timestamp saveTime;
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
    public InputData(){};
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

    public void setInput1(double input1) {
        this.input1 = input1;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public Timestamp getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Timestamp saveTime) {
        this.saveTime = saveTime;
    }
}
