package Probability.Exception;

/**
 * @author 郑悦
 * @Description:
 * @date 2023/12/3 21:32
 */

public class NegativeProbabilityException extends ProbabilityException {
    /**
     * @Description 负概率异常
     * @param message
     * @return null
     * @author 郑悦
     * @date 2023/12/18 22:14
    **/
    public NegativeProbabilityException(String message) {
        super(message);
    }
}
