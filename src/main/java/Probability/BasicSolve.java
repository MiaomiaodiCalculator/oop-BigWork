package Probability;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.correlation.Covariance;
import java.util.*;

/**
 * @author 郑悦
 * @Description: 适用于离散型随机变量的数字特征
 * @date 2023/12/2 9:49
 */
public class BasicSolve {
    public double[] values; // 数据
    public double[] valuesY; // 第二个随机变量的数据
    public double[] probabilities; // 概率
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
    public int dataNum;
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
        dataNum = statics.length;
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
        dataNum = statics.length;
//        mean = descriptiveStatistics.getMean(); // 引入概率，所以不能直接求均值当期望
        mean = getMeanInStat(statics, prob);
        median = getWeightedPercent(values, probabilities, 0.5);
        Mode = getWeightedMode(values, probabilities);
        Range = getRangeInStat(statics);
        Variance = getWeightedVariance(values, probabilities, mean);
        StandardDeviation = Math.sqrt(Variance);
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
        dataNum = statics.length;
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
     * @Description 处理带概率变量的方差
     * @param values
     * @param probabilities
     * @param weightedMean
     * @return double
     * @author 郑悦
     * @date 2023/12/9 16:38
     **/
    private static double getWeightedVariance(double[] values, double[] probabilities, double weightedMean) {
        double weightedSumSquaredDeviation = 0;
        double totalWeight = 0;

        for (int i = 0; i < values.length; i++) {
            double deviation = values[i] - weightedMean;
            weightedSumSquaredDeviation += probabilities[i] * deviation * deviation;
            totalWeight += probabilities[i];
        }

        return weightedSumSquaredDeviation / totalWeight;
    }
    /**
     * @Description 计算带概率的数据的百分位数
     * @param values
     * @param weights
     * @return double
     * @author 郑悦
     * @date 2023/12/9 16:49
     **/
    public static double getWeightedPercent(double[] values, double[] weights, double p) {
        // p传入0.5的时候就是中位数
        int n = values.length;

        // 计算总权重
        double totalWeight = 0;
        for (int i = 0; i < n; i++) {
            totalWeight += weights[i];
        }

        // 将数据按值进行排序
        sortData(values, weights);

        // 找到中位数位置
        double cumulativeWeight = 0;
        int index = 0;
        while (cumulativeWeight < p) {
            cumulativeWeight += weights[index];
            index++;
        }

        if (cumulativeWeight == p) {
            // 如果累计权重恰好等于总权重的一半，则返回当前观测值
            return values[index - 1];
        } else {
            // 使用插值法估计中位数所在的位置
            double prevWeight = cumulativeWeight - weights[index - 1];
            double interpolationFactor = (p - prevWeight) / weights[index - 1];
            return values[index - 1] + interpolationFactor * (values[index] - values[index - 1]);
        }
    }
    private static void sortData(double[] values, double[] weights) {
        int n = values.length;

        // 使用插入排序对数据进行排序
        for (int i = 1; i < n; i++) {
            double value = values[i];
            double weight = weights[i];
            int j = i - 1;
            while (j >= 0 && values[j] > value) {
                values[j + 1] = values[j];
                weights[j + 1] = weights[j];
                j--;
            }
            values[j + 1] = value;
            weights[j + 1] = weight;
        }
    }
    /**
     * @Description 在有概率的情况下得到众数
     * @param values
     * @param weights
     * @return java.util.List<java.lang.Double>
     * @author 郑悦
     * @date 2023/12/9 17:03
     **/
    private List<Double> getWeightedMode(double[] values, double[] weights) {
        int n = values.length;

        // 统计每个值的累计权重
        Map<Double, Double> cumulativeWeights = new HashMap<>();
        for (int i = 0; i < n; i++) {
            double value = values[i];
            double weight = weights[i];
            cumulativeWeights.put(value, cumulativeWeights.getOrDefault(value, 0.0) + weight);
        }

        // 找到具有最高累计权重的值或值集合
        double maxCumulativeWeight = 0;
        for (double cumulativeWeight : cumulativeWeights.values()) {
            maxCumulativeWeight = Math.max(maxCumulativeWeight, cumulativeWeight);
        }

        // 收集具有最高累计权重的值或值集合
        List<Double> mode = new ArrayList<>();
        for (Map.Entry<Double, Double> entry : cumulativeWeights.entrySet()) {
            if (entry.getValue() == maxCumulativeWeight) {
                mode.add(entry.getKey());
            }
        }

        return mode;
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
    /**
     * @Description 获得两个随机变量的相关性
     * @param value1
     * @param value2
     * @return double
     * @author 郑悦
     * @date 2023/12/9 11:23
     **/
    public double getCovariance(double[] value1, double[] value2) {
        // 获取协方差
        // 创建 Covariance 对象
        Covariance covariance = new Covariance();
        // 计算协方差
        return covariance.covariance(value1, value2);
    }
    /**
     * @Description 处理在两个输入文本框中有输入改变（比如enter之后又del的情况）
     * @param staticsRevise
     * @param statics
     * @param toWhich
     * @param ProcessMethod
     * @return Probability.BasicSolve
     * @author 郑悦
     * @date 2023/12/9 11:33
     **/
    public BasicSolve reEditForVarTwo(double[] staticsRevise, double[] statics, int toWhich, int ProcessMethod) {
        BasicSolve basic = new BasicSolve();
        switch (toWhich) {
            case 1:
                values = staticsRevise;
                valuesY = statics;
                break;
            case 2:
                values = statics;
                valuesY = staticsRevise;
                break;
        }
        switch (ProcessMethod) {
            case 1: // 处理两个随机变量
                basic = new BasicSolve(values, valuesY, true);
                break;
            case 2: // 处理有概率的随机变量
                basic = new BasicSolve(values, valuesY);
                break;
        }
        return basic;
    }
    /**
     * @Description 处理在一个输入文本框中有输入改变（比如enter之后又del的情况）
     * @param staticsRevise
     * @return Probability.BasicSolve
     * @author 郑悦
     * @date 2023/12/9 11:34
     **/
    public BasicSolve reEditForVarOne(double[] staticsRevise) {
        return new BasicSolve(staticsRevise);
    }
}