package Probability;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 郑悦
 * @Description: 用于表格数据展示
 * @date 2023/12/4 0:14
 */
public class InputData implements Serializable {
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
    /**
     * @Description 显示两列数据
     * @param data1
     * @param data2
     * @return null
     * @author 郑悦
     * @date 2023/12/18 22:11
    **/
    public InputData(double data1, double data2) {
        input1 = data1;
        input2 = data2;
        residual = 0;
        MSE = 0;
        r = 0;
        adjustedR = 0;
    }
    /**
     * @Description 显示拟合评估结果
     * @param residual0
     * @param MSE0
     * @param r0
     * @return null
     * @author 郑悦
     * @date 2023/12/18 22:12
    **/
    public InputData(double residual0, double MSE0, double r0) {
        residual = residual0;
        MSE = MSE0;
        r = r0;
//        adjustedR = adjustedR0;
    }
    /**
     * @Description 无参初始化
     * @return null
     * @author 郑悦
     * @date 2023/12/18 22:12
    **/
    public InputData(){};
    /**
     * @Description 历史记录表格初始化
     * @param d1
     * @param d2
     * @return null
     * @author 郑悦
     * @date 2023/12/18 22:12
    **/
    public InputData(String d1, String d2) { // 用于历史记录存储
        data1 = d1;
        data2 = d2;
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
