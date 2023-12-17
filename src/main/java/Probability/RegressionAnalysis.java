package Probability;

import com.singularsys.jep.functions.Str;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.fitting.SimpleCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.List;

/**
 * @author 郑悦
 * @Description:
 * @date 2023/12/3 0:27
 */
public class RegressionAnalysis {
    public String regressionExpression;
    double[] xRaw;
    double[] yRaw;
    public double sumOfResidual = 0;
    public double R;
    public double adjustedR;
    public double MSE;
    WeightedObservedPoints points = new WeightedObservedPoints();
    double[] residuals;
    int regressionType;
    // 线性回归
    SimpleRegression regression = new SimpleRegression();
    // 多项式模拟
    PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1);
    public double[] parameters;  // 记录拟合函数对应的参数
    public RegressionAnalysis() {
        sumOfResidual = 0;
        R = 0;
        adjustedR = 0;
        MSE = 0;
    }
    public RegressionAnalysis(double[] xInput, double[] yInput, int type, int PolyNum) {
        xRaw = xInput;
        yRaw = yInput;
        regressionType = type;
        for (int i = 0; i < xInput.length; i++) {
            points.add(xInput[i], yInput[i]);
        }
        residuals = new double[points.toList().size()];
        fitter = PolynomialCurveFitter.create(PolyNum);
        double[] coefficients = fitter.fit(points.toList());
        regressionExpression = "";
        String p0;
        for (int j = coefficients.length - 1; j > 0; j--) {
            p0 = String.format("%.3f", coefficients[j]);
            regressionExpression += p0 + " * x^" + j + " + ";
        }
        regressionExpression += coefficients[0];
        regression = new SimpleRegression();
        for (int j = 0; j < xInput.length; j++) {
            regression.addData(xInput[j], yInput[j]);
        }
        for (int i = 0; i < points.toList().size(); i++) {
            WeightedObservedPoint point = points.toList().get(i);
            double x = point.getX();
            double y = point.getY();
            double predictedY = 0;
            String p1, p2;
            switch (regressionType) {   // 根据不同回归类型确定不同函数表达式
                case 1: // 线性回归
                    predictedY = regression.predict(x);
                    parameters = new double[2];
                    parameters[0] = regression.getSlope();
                    parameters[1] = regression.getIntercept();
                    p1 = String.format("%.3f", parameters[0]);
                    p2 = String.format("%.3f", parameters[1]);
                    regressionExpression = "y = " + p1 + "x + " + p2;
                    break;
                case 2: // 多项式回归
                    parameters = coefficients;
                    predictedY = evaluatePolynomial(coefficients, x);
                    break;
                case 3: // 指数回归
                    getExpParameters();
                    p1 = String.format("%.3f", parameters[0]);
                    p2 = String.format("%.3f", parameters[1]);
                    regressionExpression = "y = " + p1 + "e^(" + p2 + "x)";
                    predictedY = parameters[0] * Math.exp(parameters[1] * x);
                    break;
                case 4: // 对数回归
                    getLogParameters();
                    p1 = String.format("%.3f", parameters[0]);
                    p2 = String.format("%.3f", parameters[1]);
                    regressionExpression = "y = " + p1 + "lnx + " + p2;
                    predictedY = parameters[0] * Math.log(x) + parameters[1];
                    break;
            }
            residuals[i] = y - predictedY;
            sumOfResidual += Math.abs(residuals[i]);
        }
        R = calculateRSquared(points.toList());
//        adjustedR = calculateAdjustedRSquared(R, xInput.length, parameters.length);
        MSE = calculateMSE(residuals);
    }
    public double evaluatePolynomial(double[] coefficients, double x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, i);
        }
        return result;
    }
    /**
     * @Description 计算均方误差（MSE）
     * @param residuals
     * @return double
     * @author 郑悦
     * @date 2023/12/11 17:22
    **/
    private static double calculateMSE(double[] residuals) {
        double sum = 0;
        for (double residual : residuals) {
            sum += Math.pow(residual, 2);
        }
        return sum / residuals.length;
    }
    /**
     * @Description 计算决定系数（R-squared）
     * @param points
     * @return double
     * @author 郑悦
     * @date 2023/12/11 17:22
    **/
    private double calculateRSquared(List<WeightedObservedPoint> points) {
        double sumOfSquares = 0;
        double sumOfResiduals = 0;
        double meanY = calculateMeanY(points);

        for (double residual : residuals) {
            sumOfSquares += Math.pow(residual, 2);
            sumOfResiduals += Math.pow(residual - meanY, 2);
        }

        return 1 - (sumOfSquares / sumOfResiduals);
    }
    /**
     * @Description 计算均值（meanY）
     * @param points
     * @return double
     * @author 郑悦
     * @date 2023/12/11 17:22
    **/
    private double calculateMeanY(List<WeightedObservedPoint> points) {
        double sum = 0;
        for (WeightedObservedPoint point : points) {
            sum += point.getY();
        }
        return sum / points.size();
    }
    /**
     * @Description 计算调整的决定系数（Adjusted R-squared）
     * @param rSquared
     * @param numDataPoints
     * @param numParameters
     * @return double
     * @author 郑悦
     * @date 2023/12/11 17:23
    **/
    private double calculateAdjustedRSquared(double rSquared, int numDataPoints, int numParameters) {
        return 1 - ((1 - rSquared) * (numDataPoints - 1) / (numDataPoints - numParameters - 1));
    }

    /**
     * @Description 获取指数拟合的参数
     * @author 郑悦
     * @date 2023/12/11 21:40
    **/
    public void getExpParameters() {
        SimpleCurveFitter fitter = SimpleCurveFitter.create(new ExponentialFunction(), new double[]{1, 1});
        parameters = fitter.fit(points.toList());
    }

    /**
     * @Description 用指数函数拟合
     * @author 郑悦
     * @date 2023/12/11 21:31
    **/
    static class ExponentialFunction implements ParametricUnivariateFunction {
        @Override
        public double value(double x, double... parameters) {
            double a = parameters[0]; // 指数函数的底数
            double b = parameters[1]; // 指数函数的指数
            return a * Math.exp(b * x);
        }

        @Override
        public double[] gradient(double x, double... parameters) {
            // 如果需要计算梯度，可以在这里实现
            // 指数函数的梯度计算公式
            double a = parameters[0];
            double b = parameters[1];
            double partialDerivativeA = Math.exp(b * x);
            double partialDerivativeB = a * x * Math.exp(b * x);
            return new double[]{partialDerivativeA, partialDerivativeB};
        }
    }

    /**
     * @Description 获取对数拟合的参数
     * @author 郑悦
     * @date 2023/12/11 21:40
     **/
    public void getLogParameters() {
        LogarithmicFunction logarithmicFunction = new LogarithmicFunction();
        SimpleCurveFitter fitter = SimpleCurveFitter.create(logarithmicFunction, new double[]{1, 1});
        parameters = fitter.fit(points.toList());
    }
    /**
     * @Description 用对数函数拟合
     * @author 郑悦
     * @date 2023/12/11 21:31
     **/
    static class LogarithmicFunction implements ParametricUnivariateFunction {
        @Override
        public double value(double x, double... parameters) {
            double a = parameters[0]; // 对数函数的系数
            double b = parameters[1]; // 对数函数的常数
            return a * Math.log(x) + b;
        }

        @Override
        public double[] gradient(double x, double... parameters) {
            double a = parameters[0];
            double b = parameters[1];
            double partialDerivativeA = Math.log(x);
            double partialDerivativeB = 1;
            return new double[]{partialDerivativeA, partialDerivativeB};
        }
    }
}
