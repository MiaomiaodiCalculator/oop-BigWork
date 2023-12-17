package vector;

import java.io.Serializable;

/**
 * @author Bu Xinran
 * @Description 处理向量计算器获得的数据
 * @date 2023/11/24 23:23
 */
public class VectorSolve implements Serializable {
    protected String x1;
    protected double X1;
    protected String y1;
    protected double Y1;
    protected String z1;
    protected double Z1;
    protected String x2;
    protected double X2;
    protected String y2;
    protected double Y2;
    protected String z2;
    protected double Z2;
    protected int state=2;
    protected boolean error=true;
    protected double[] add=new double[3];
    protected double dot;
    protected double cross;
    protected double angle;
    public VectorSolve(String x1,String y1,String z1,String x2,String y2,String z2,int state){
        this.x1=x1;
        this.y1=y1;
        this.z1=z1;
        this.x2=x2;
        this.y2=y2;
        this.z2=z2;
        this.state=state;
        try{
            X1=Double.parseDouble(x1);
            Y1=Double.parseDouble(y1);
            X2=Double.parseDouble(x2);
            Y2=Double.parseDouble(y2);
            if(state==3){
                Z1=Double.parseDouble(z1);
                Z2=Double.parseDouble(z2);
            }
        }catch(NumberFormatException e){
            error=false;
        }
    }
    /***
     * @Description 向量相加
     * @return double[]
     * @author Bu Xinran
     * @date 2023/11/28 13:39
    **/

    public double[] addVector(){
        add[0]=X1+X2;
        add[1]=Y1+Y2;
        if(state==3)
            add[2]=Z1+Z2;
        return add;
    }
    /***
     * @Description   向量点乘
     * @return double
     * @author Bu Xinran
     * @date 2023/11/28 13:39
    **/

    public double dotVector(){
        if(state==3){
            dot=X1*X2+Y1*Y2+Z1*Z2;
            return dot;
        }else{
            dot=X1*X2+Y1*Y2;
            return dot;
        }
    }
    /***
     * @Description   向量叉乘
     * @return double
     * @author Bu Xinran
     * @date 2023/11/28 13:39
     **/

    public double crossVector(){
        if(state==2){
            cross=X1*Y2 - X2*Y1;
            return cross;
        }
        else{
            cross=Y1*Z2-Y2*Z1+X2*Z1-X1*Z2+X1*Y2-X2*Y1;
            return cross;
        }
    }
    /***
     * @Description   求角度
     * @return double
     * @author Bu Xinran
     * @date 2023/11/28 13:40
     **/
    public double angleVector(){
        if(state==2){
            angle=X1*X2+Y1*Y2;
            angle/=Math.sqrt(X1*X1+Y1*Y1)*Math.sqrt(X2*X2+Y2*Y2);
        }else{
            angle=X1*X2+Y1*Y2+Z1*Z2;
            angle/=Math.sqrt(X1*X1+Y1*Y1+Z1*Z1)*Math.sqrt(X2*X2+Y2*Y2+Z2*Z2);
        }
        return angle;
    }
    public boolean getError(){
        return error;
    }
}
