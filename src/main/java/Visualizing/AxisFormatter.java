package Visualizing;

import javafx.util.StringConverter;

/**
 * @author ZhouYH
 * @Description 坐标轴操作
 * @date 2023/11/29 22:23
 */
public class AxisFormatter extends StringConverter<Number> {
    public AxisFormatter() {
    }
    @Override
    public String toString(Number n) {
        if (n.intValue() != n.doubleValue()) {
            return "";
        } else {
            return "" + (n.intValue());
        }
    }
    @Override
    public Number fromString(String s) {
        Number val = Double.parseDouble(s);
        return val.intValue();
    }
}
