package Equation;
/**
 * @author Bu Xinran
 * @Description 方程式的错误类型化
 * @date 2023/11/30 9:55
 */
public enum EquationError {
    //yes:无错误。dotRepeat：小数点重复。symbolRepeat:运算符重复。notEqual:一定不相等。cannotFindX:不能找到x。error:算式错误。xRepeat:变量x连续。
    yes,dotRepeat,symbolRepeat,notEqual,cannotFindX,error,xRepeat,
}
