package Probability.Exception;

/**
 * @author 郑悦
 * @Description:
 * @date 2023/12/3 21:21
 */

public class NotInputDataException extends Exception {
    /**
     * @Description 无输入数据异常
     * @param message
     * @return null
     * @author 郑悦
     * @date 2023/12/18 22:15
    **/
    public NotInputDataException(String message) {
        super(message);
    }
}
