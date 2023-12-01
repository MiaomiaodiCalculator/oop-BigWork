package com.calculator.calculation.Visualizing;

/**
 * @author ZhouYH
 * @Description 函数表达式接口，在Parser.java里用到，生成的表达式用此存储、运算
 * @date 2023/12/1 16:46
 */
public interface Expression {
    double eval();
}