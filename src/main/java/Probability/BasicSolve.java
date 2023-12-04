package Probability;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.correlation.Covariance;
import java.util.*;

/**
 * @author 郑悦
 * @Description: 适用于离散型随机变量的数字特征
 * @date 2023/12/2 9:49
 */
public class BasicSolve {
    double[] values; // 数据
    double[] valuesY; // 第二个随机变量的数据
    double[] probabilities; // 概率
    public double mean; // 均值
    public double meanY;
    public double median; // 中位数
    public double medianY;
    public List<Double> Mode; // 众数
    public List<Double> ModeY;
    public double Range; // 极差
    public double RangeY;
    public double Variance; // 方差
    public double VarianceY;
    public double StandardDeviation; // 标准差
    public double StandardDeviationY;
    public double Percentiles; // 百分位数
    public double PercentilesY;
    public double Cov; // 协方差
    public double correlationCoefficient; // 相关系数
    /**
     * @Description  类的无参构造
     * @return null
     * @author 郑悦
     * @date 2023/12/2 21:37
    **/
    public BasicSolve() {}
    /**
     * @Description 单变量的基本数字特征求解
     * @param statics
     * @return null       
     * @author 郑悦
     * @date 2023/12/2 20:48
    **/
    public BasicSolve(double[] statics) {
        values = statics;
        // 创建 DescriptiveStatistics 对象
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        // 添加值到 DescriptiveStatistics 对象
        for (double value : values) {
            descriptiveStatistics.addValue(value);
        }
        // 计算平均值_在所有数据记录时即数学期望（所有数据记录不是分别按概率记录）
        mean = descriptiveStatistics.getMean();
        median = descriptiveStatistics.getPercentile(50);
        Mode = getModeInStat(statics);
        Range = getRangeInStat(statics);
        Variance = descriptiveStatistics.getVariance();
        StandardDeviation = descriptiveStatistics.getStandardDeviation();
    }

    /**
     * @Description 不是输入所有数据点，而是输入数据和对应概率
     * @param statics
     * @param prob
     * @return null
     * @author 郑悦
     * @date 2023/12/2 20:54
    **/
    public BasicSolve(double[] statics, double[] prob) {
        values = statics;
        probabilities = prob;
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
//        mean = descriptiveStatistics.getMean(); // 引入概率，所以不能直接求均值当期望
        mean = getMeanInStat(statics, prob);
        median = descriptiveStatistics.getPercentile(50);
        Mode = getModeInStat(statics);
        Range = getRangeInStat(statics);
        Variance = descriptiveStatistics.getVariance();
        StandardDeviation = descriptiveStatistics.getStandardDeviation();
    }

    /**
     * @Description 双变量的基本数字特征求解 注意特别是增加了协方差的特征
     * @param statics
     * @param staticsY
     * @param isBivariate 注意这个变量仅是为了和上面有概率的两个参数进行区分
     * @return null
     * @author 郑悦
     * @date 2023/12/2 20:56
    **/
    public BasicSolve(double[] statics, double[] staticsY, boolean isBivariate) {
        values = statics;
        valuesY = staticsY;
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        DescriptiveStatistics descriptiveStatisticsY = new DescriptiveStatistics();
        for (double value : values) {
            descriptiveStatistics.addValue(value);
        }
        for (double value : valuesY) {
            descriptiveStatisticsY.addValue(value);
        }
        mean = descriptiveStatistics.getMean();
        meanY = descriptiveStatisticsY.getMean();
        median = descriptiveStatistics.getPercentile(50);
        medianY = descriptiveStatisticsY.getPercentile(50);
        Mode = getModeInStat(statics);
        ModeY = getModeInStat(staticsY);
        Range = getRangeInStat(statics);
        RangeY = getRangeInStat(staticsY);
        Variance = descriptiveStatistics.getVariance();
        VarianceY = descriptiveStatisticsY.getVariance();
        StandardDeviation = descriptiveStatistics.getStandardDeviation();
        StandardDeviationY = descriptiveStatisticsY.getStandardDeviation();
        Cov = getCovariance(statics, staticsY);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlationCoefficient = correlation.correlation(values, valuesY);
    }

    /**
     * @Description 获得给定数据的众数
     * @param values
     * @return java.util.List<java.lang.Double>
     * @author 郑悦
     * @date 2023/12/2 21:34
    **/
    public List<Double> getModeInStat(double[] values) {
        // 创建一个 Map 用于统计值和出现次数的映射关系
        Map<Double, Integer> valueCountMap = new HashMap<>();

        // 统计值和出现次数
        for (double value : values) {
            if (valueCountMap.containsKey(value)) {
                valueCountMap.put(value, valueCountMap.get(value) + 1);
            } else {
                valueCountMap.put(value, 1);
            }
        }

        // 找到出现次数最多的值
        int maxCount = 0;
        List<Double> modes = new ArrayList<>();
        for (Map.Entry<Double, Integer> entry : valueCountMap.entrySet()) {
            int count = entry.getValue();
            if (count > maxCount) {
                maxCount = count;
                modes.clear();
                modes.add(entry.getKey());
            } else if (count == maxCount) {
                modes.add(entry.getKey()); // 众数可能有多个
            }
        }

        return modes;
    }
    /**
     * @Description 获得给定数据的极差
     * @param values
     * @return double
     * @author 郑悦
     * @date 2023/12/2 21:33
    **/
    private double getRangeInStat(double[] values) {
        // 找到最大值和最小值
        double max = Arrays.stream(values).max().getAsDouble();
        double min = Arrays.stream(values).min().getAsDouble();

        // 计算极差
        return max - min;
    }
    /**
     * @Description 计算给定对应概率的数据的数学期望
     * @param statics
     * @param prob
     * @return double
     * @author 郑悦
     * @date 2023/12/2 21:42
    **/
    private double getMeanInStat(double[] statics, double[] prob) {
        int num = statics.length;
        double result = 0.0;
        for(int i = 0; i < num; i++) {
            result += statics[i] * prob[i];
        }
        return result;
    }
    /**
     * @Description 获取对应数据用户要求百分数
     * @param type
     * @param p
     * @return double
     * @author 郑悦
     * @date 2023/12/3 0:11
    **/
    public double getPercentilesInStat(int type, double p) {
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        switch (type) {
            case 1:
                for (double value : values) {
                    descriptiveStatistics.addValue(value);
                }
                break;
            case 2:
                for (double value : valuesY) {
                    descriptiveStatistics.addValue(value);
                }
                break;
        }
        return descriptiveStatistics.getPercentile(p);
    }
    public double getCovariance(double[] value1, double[] value2) {
        // 获取协方差
        // 创建 Covariance 对象
        Covariance covariance = new Covariance();
        // 计算协方差
        return covariance.covariance(value1, value2);
    }
}
