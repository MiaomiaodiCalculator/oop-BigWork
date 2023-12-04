package Probability;
import org.apache.commons.math3.distribution.PoissonDistribution;

/**
 * @author 郑悦
 * @Description:
 * @date 2023/12/2 0:43
 */
public class PoissonDistributionTest {
    public static void main(String[] args) {
        double lambda = 2.5; // 泊松分布的参数 lambda
        int k = 3; // 随机变量取值的上限

        PoissonDistribution poissonDistribution = new PoissonDistribution(lambda);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(k);

        System.out.println("P(X <= " + k + ") = " + cumulativeProbability);
    }
}
