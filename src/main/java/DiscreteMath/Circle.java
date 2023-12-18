package DiscreteMath;

/**
 * @author 郑悦
 * @Description:
 * @date 2023/12/16 22:12
 */
public class Circle {
    private double centerX;
    private double centerY;
    private double radius;
    /**
     * @Description 构造circle保存
     * @param centerX
     * @param centerY
     * @param radius
     * @return null
     * @author 郑悦
     * @date 2023/12/16 22:01
    **/
    public Circle(double centerX, double centerY, double radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }
    /**
     * @Description 获取X坐标
     * @return double
     * @author 郑悦
     * @date 2023/12/16 22:01
    **/
    public double getCenterX() {
        return centerX;
    }
    /**
     * @Description 获取Y坐标
     * @return double
     * @author 郑悦
     * @date 2023/12/16 22:01
     **/
    public double getCenterY() {
        return centerY;
    }
    /**
     * @Description 获取半径
     * @return double
     * @author 郑悦
     * @date 2023/12/16 22:01
     **/
    public double getRadius() {
        return radius;
    }
}
