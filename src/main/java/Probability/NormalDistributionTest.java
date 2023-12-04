package Probability;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * @author 郑悦
 * @Description:
 * @date 2023/12/2 0:40
 */
public class NormalDistributionTest {
    public static void main(String[] args) {
        double mean = 0; // 高斯分布的均值
        double standardDeviation = 1; // 高斯分布的标准差
        double x = 1.5; // 随机变量取值的上限

        NormalDistribution normalDistribution = new NormalDistribution(mean, standardDeviation);
        double cumulativeProbability = normalDistribution.cumulativeProbability(x);

        System.out.println("P(X <= " + x + ") = " + cumulativeProbability);
    }
}
