package Visualizing;

import java.util.HashMap;
import java.util.function.DoubleUnaryOperator;

/**
 * @author ZhouYH
 * @Description 将字符串转化为函数表达式以运算
 * @date 2023/12/1 16:45
 */
public class Parser{
    // instance variables
    private final HashMap<String, DoubleUnaryOperator> map;
    private HashMap<String, Double> vars;
    private int pos;    // keep track of our position in the string
    private int val;    // keep track of the last char we consumed
    private String input;

    /**
     * @Description 初始化并定义数学工具们
     * @author ZhouYH
     * @date 2023/12/1 21:39
     **/
    public Parser() {
        this.map = new HashMap<>();
        map.put("sin", Math::sin);
        map.put("cos", Math::cos);
        map.put("tan", Math::tan);
        map.put("asin", Math::asin);
        map.put("acos", Math::acos);
        map.put("atan", Math::atan);
        map.put("sqrt", Math::sqrt);
        map.put("log", Math::log);
        map.put("exp", Math::exp);
        map.put("sec", (val) -> (1.0 / Math.cos(val)));
        map.put("csc", (val) -> (1.0 / Math.sin(val)));
        map.put("cot", (val) -> (1.0 / Math.tan(val)));
        reset();
    }

    /**
     * @Description 读取下一位放入val（以int形式），没有下一位时置-1
     * @author ZhouYH
     * @date 2023/12/1 22:30
     **/
    private void next() {
        val = (++pos < input.length() ? input.charAt(pos) : -1);
    }

    /**
     * @Description 若匹配，从当前字符匹配处跳转到下一个字符
     * @param c 被匹配的字符
     * @return boolean
     * @author ZhouYH
     * @date 2023/12/1 23:29
     **/
    private boolean consume(char c) {
        if (val == c) {
            next();
            return true;
        }
        return false;
    }

    /**
     * @Description 整个数组到表达式转化的入口
     * @return Visualizing.Expression 最终表达式
     * @author ZhouYH
     * @date 2023/12/1 22:53
     **/
    private Expression parse() {
        next(); //consume the next character
        return parseTier1();
    }

    /**
     * @Description   最低优先级：+ -
     * @return Visualizing.Expression
     * @author ZhouYH
     * @date 2023/12/1 23:43
     **/
    private Expression parseTier1() {
        Expression x1 = parseTier2();
        while (true) {
            if (consume('+')) {
                Expression left = x1, right = parseTier2();
                x1 = () -> left.eval() + right.eval();
            } else if (consume('-')) {
                Expression left = x1, right = parseTier2();
                x1 = () -> left.eval() - right.eval();
            } else {
                return x1;
            }
        }
    }

    /**
     * @Description   第二优先级： * / %
     * @return Visualizing.Expression
     * @author ZhouYH
     * @date 2023/12/1 23:44
     **/
    private Expression parseTier2() {
        Expression x2 = parseTier3();
        while (true) {
            if (consume('*')) {
                Expression left = x2, right = parseTier3();
                x2 = () -> left.eval() * right.eval();
            } else if (consume('/')) {
                Expression left = x2, right = parseTier3();
                x2 = () -> left.eval() / right.eval();
            }else if (consume('%')) {
                Expression left = x2, right = parseTier3();
                x2 = () -> left.eval() % right.eval();
            } else {
                return x2;
            }
        }
    }

    /**
     * @Description   第三优先级：^ @(√x)
     * @return Visualizing.Expression
     * @author ZhouYH
     * @date 2023/12/1 23:44
     **/
    private Expression parseTier3() {
        Expression x3 = parseTier4();
        while (true) {
            // handle exponentiation & nth roots/fractional exponents
            if (consume('^')) {
                Expression left = x3, right = parseTier4();
                x3 = () -> Math.pow(left.eval(), right.eval());
            } else if (consume('@')) {
                Expression left = x3, right = parseTier4();
                x3 = () -> Math.pow(left.eval(), (1.0 / right.eval()));
            } else {
                return x3;
            }
        }
    }

    /**
     * @Description   最高优先级：括号结构、正负
     * @return Visualizing.Expression
     * @author ZhouYH
     * @date 2023/12/1 23:44
     **/
    private Expression parseTier4() {
        int start = this.pos;
        Expression x4;   // declare the Expression we're going to return
        if (consume('+')) {
            x4 = parseTier4();
            return x4;
        } else if (consume('-')) {
            Expression right = parseTier4();
            x4 = () -> (-1.0 * right.eval());
            return x4;
        }
        if (consume('(')) {
            x4 = parseTier1();     // branch our tree until we hit the ')'
            consume(')');
            return x4;
        } else if (isNumber()) {
            while(isNumber()) {
                next();     // advance our parser to the first non-digit or '.'
            }
            double d = Double.parseDouble(input.substring(start, this.pos));
            x4 = () -> d;
            return x4;
        } else if (isAlpha()) {     // handle unary functions, and variables
            while (isAlpha()) {
                next();     // advance our parser to the first non-alpha
            }
            String fn = input.substring(start, this.pos); // get the name of the function
            x4 = parseTier4();    // get the value the function will operate on
            if (map.containsKey(fn)) {
                Expression arg = x4;
                DoubleUnaryOperator func = map.get(fn);
                x4 = () -> func.applyAsDouble(arg.eval());
            } else {
                x4 = () -> vars.get(fn);
            }
            return x4;
        } else {
            return null;
        }
    }

    /**
     * @Description 判断在数字内，包括小数点
     * @return boolean
     * @author ZhouYH
     * @date 2023/12/1 23:38
     **/
    private boolean isNumber() {
        return Character.isDigit(val) || val == '.';
    }

    /**
     * @Description 判断是否是字母
     * @return boolean
     * @author ZhouYH
     * @date 2023/12/1 23:39
     **/
    private boolean isAlpha() {
        return Character.isAlphabetic(val) &&
                !(val == '(' || val == ')');
    }

    /**
     * @Description 为下次转换重置parser         
     * @author ZhouYH
     * @date 2023/12/1 21:39
     **/
    private void reset() {
        this.pos = -1;
        this.val = -1;
        this.input = "";
    }

    /**
     * @Description 标准化读取
     * @param in 原本读取的字符串
     * @return java.lang.String 经标准化表达的字符串
     * @author ZhouYH
     * @date 2023/12/1 21:31
     **/
    private String formatInput(String in) {
        return in.replace(" ", "")
                .replace("ⁿ√x", "@")
                .replace("√", "sqrt")
                .replace("×", "*")
                .replace("÷", "/")
                .replace("x+", "(x)+")
                .replace("x-", "(x)-");
    }

    /**
     * @Description 一次由原本读入的字符串到函数表达式的转换过程。读入字符串不是合法表达式时会抛出异常，但parser仍被重置
     * @param exp 读入的字符串
     * @param vars 传入的HashMap，因为Controller里也要用所以由外部传入
     * @return Visualizing.Expression
     * @author ZhouYH
     * @date 2023/12/1 21:37
     **/
    public Expression eval(String exp, HashMap<String, Double> vars) {
        this.vars = vars;
        try {
            this.input = formatInput(exp);
            int lefts=0,rights=0;
            for(int c=0; c<this.input.length();c++){
                if(this.input.charAt(c)=='(') lefts++;
                if(this.input.charAt(c)==')') rights++;
            }
            if(lefts!=rights) throw new NullPointerException();
            return this.parse();
        } finally {
            reset();
        }
    }
}
