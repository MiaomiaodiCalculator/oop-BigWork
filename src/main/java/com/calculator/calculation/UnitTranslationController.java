package com.calculator.calculation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author ZhouYH
 * @Description 处理单位换算的点击事件
 * @date 2023/11/27 19:46
 */
public class UnitTranslationController implements Initializable  {
    public TextField input;
    public TextField output;
    public TextField unit1;
    public TextField unit2;
    public TextField TypeSet;
    public int type;
    public int label1; //取0时即暂未设置该单位
    public int label2; //取0时即暂未设置该单位
    public Pane Volume;
    public Pane Length;
    public Pane Weight;
    public Pane Temperature;
    public Pane Area;
    public Pane Times;
    public Pane Angle;
    BigDecimal in;
    BigDecimal out;
    
    /**
     * @Description 运算结果 
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 1:01
     **/
    public void Calculate(ActionEvent actionEvent) {
        if(label1==0||label2==0){
            output.setText("Set Units First!");
        }else {
            try {
                in=new BigDecimal(input.getText());
                if((type==1||type==3||type==5||type==6)&&in.toString().charAt(0)=='-'){
                    output.setText("Illegal Input!");
                }else if(type==4&&((label1==3&&in.toString().charAt(0)=='-')||(label1==1&& in.add(new BigDecimal("273.15")).toString().charAt(0)=='-')||(label1==2&&in.add(new BigDecimal("459.67")).toString().charAt(0)=='-'))){
                    output.setText("Illegal Input!");
                }else {
                    if (label1==label2){
                        output.setText(in.toString());
                    }else {
                        out = in;
                        BigDecimal Big1000=new BigDecimal("1000");
                        BigDecimal Big100=new BigDecimal("100");
                        switch (type){
                            case 1, 3:
                                if (label1<label2){
                                    for (int tmp=label1; tmp<label2; tmp++)
                                        out=out.divide(Big1000);
                                }else {
                                    for (int tmp=label1; tmp>label2; tmp--)
                                        out=out.multiply(Big1000);
                                }
                                break;
                            case 2:
                                BigDecimal Big12=new BigDecimal("12");  //1英尺=12英寸
                                BigDecimal Big254=new BigDecimal("2.54");  //1英寸=2.54厘米
                                if (label1<6&&label2<6){
                                    if (label1<label2){
                                        for (int tmp=label1; tmp<label2; tmp++)
                                            out=out.divide(Big1000);
                                    }else
                                        for (int tmp=label1; tmp>label2; tmp--)
                                            out=out.multiply(Big1000);
                                }else if (label1<6){
                                    if (label1<4){
                                        for (int tmp=label1; tmp<4; tmp++)
                                            out=out.divide(Big1000);
                                    }else {
                                        for (int tmp=label1; tmp>4; tmp--)
                                            out=out.multiply(Big1000);
                                    }  //这步换算成“米”（4）
                                    out=out.multiply(Big100);  //这步换算成“厘米”（6）
                                    int tmp=6;
                                    if (tmp<label2){
                                        try {
                                            out=out.divide(Big254);
                                        }catch (ArithmeticException e){
                                            out=out.divide(Big254,10, RoundingMode.HALF_UP);
                                        }
                                        tmp++;
                                    }  //这步换算成“英寸”（7）
                                    if (tmp<label2){
                                        try {
                                            out=out.divide(Big12);
                                        }catch (ArithmeticException e){
                                            out=out.divide(Big12,10, RoundingMode.HALF_UP);
                                        }
                                    }  //这步换算成“英尺”（8）
                                } else if (label2<6) {
                                    int tmp=label1;
                                    if (tmp>=8){
                                        out=out.multiply(Big12);
                                        tmp--;
                                    }if(tmp>=7){
                                        out=out.multiply(Big254);
                                    }
                                    out=out.multiply(Big100);  //这步换算成“米”（4）
                                    tmp=4;
                                    if (label2>4){
                                        for (; tmp<label2; tmp++)
                                            out=out.divide(Big1000);
                                    }else {
                                        for (; tmp>label2; tmp--)
                                            out=out.multiply(Big1000);
                                    }
                                }else {
                                    if(label2<label1){
                                        if (label1==8)
                                            out=out.multiply(Big12);
                                        if (label2==6)
                                            out=out.multiply(Big254);
                                    }else {
                                        if (label1==6)
                                            try {
                                                out=out.divide(Big254);
                                            }catch (ArithmeticException e){
                                                out=out.divide(Big254,10, RoundingMode.HALF_UP);
                                            }
                                        if (label2==8)
                                            try {
                                                out=out.divide(Big12);
                                            }catch (ArithmeticException e){
                                                out=out.divide(Big12,10, RoundingMode.HALF_UP);
                                            }
                                    }
                                }
                                break;
                            case 4:
                                if(label1==3){
                                    if(label2==1){
                                        out=out.subtract(new BigDecimal("273.15"));
                                    }else {
                                        out=out.multiply(new BigDecimal("1.8"));
                                        out=out.subtract(new BigDecimal("459.67"));
                                    }
                                }else if(label2==3){
                                    if(label1==1){
                                        out=out.add(new BigDecimal("273.15"));
                                    }else {
                                        try {
                                            out=out.divide(new BigDecimal("1.8"));
                                        }catch (ArithmeticException e){
                                            out=out.divide(new BigDecimal("1.8"),10, RoundingMode.HALF_UP);
                                        }
                                        out=out.add(new BigDecimal("459.67"));
                                    }
                                }else {
                                    if (label1==1){
                                        out=out.add(new BigDecimal("273.15"));
                                        out=out.multiply(new BigDecimal("1.8"));
                                        out=out.subtract(new BigDecimal("459.67"));
                                    }else {
                                        try {
                                            out=out.divide(new BigDecimal("1.8"));
                                        }catch (ArithmeticException e){
                                            out=out.divide(new BigDecimal("1.8"),10, RoundingMode.HALF_UP);
                                        }
                                        out=out.add(new BigDecimal("459.67"));
                                        out=out.multiply(new BigDecimal("1.8"));
                                        out=out.subtract(new BigDecimal("459.67"));
                                    }
                                }
                                break;
                            case 5:
                                if (label1<label2){
                                    for (int tmp=label1; tmp<label2; tmp++)
                                        out=out.divide(Big100);
                                }else {
                                    for (int tmp=label1; tmp>label2; tmp--)
                                        out=out.multiply(Big100);
                                }
                                break;
                            case 6:
                                BigDecimal[] nums=new BigDecimal[8];
                                nums[1]=new BigDecimal("1000");
                                nums[2]=nums[3]=new BigDecimal("60");
                                nums[4]=new BigDecimal("24");
                                nums[5]=new BigDecimal("7");
                                nums[6]=new BigDecimal("365").divide(nums[5],20,RoundingMode.HALF_UP);
                                if (label1<label2){
                                    for (int tmp=label1; tmp<label2; tmp++)
                                        try {
                                            out=out.divide(nums[tmp]);
                                        }catch (ArithmeticException e){
                                            out=out.divide(nums[tmp],15, RoundingMode.HALF_UP);
                                        }
                                }else {
                                    for (int tmp=label1; tmp>label2; tmp--)
                                        out=out.multiply(nums[tmp-1]);
                                }
                                break;
                            case 7:
                                BigDecimal num=new BigDecimal("57.2957795131");
                                if (label1==1){
                                    out=out.divide(num,10, RoundingMode.HALF_UP);
                                }else out=out.multiply(num);
                                break;
                            default:
                                out=new BigDecimal("000000");
                                break;
                        }
                        String Str=out.toString();
                        if (Str.matches("-?\\d+\\.\\d*0+"))
                            while (Str.charAt(Str.length()-1)=='0')
                                Str=Str.substring(0,Str.length()-1);
                        if (Str.matches("-?\\d+\\."))
                            Str=Str.substring(0,Str.length()-1);
                        output.setText(Str);
                    }
                }
            }catch (NumberFormatException e){
                output.setText("Illegal Input!");
            }catch (ArithmeticException e){
                output.setText("error!");
            }
        }
    }

    /**
     * @Description 刷新Texts
     * @author ZhouYH
     * @date 2023/11/28 23:08
     **/
    public void newText() {
        input.setVisible(true);
        output.setVisible(true);
        unit1.setVisible(true);
        unit2.setVisible(true);
        input.setText("");
        output.setText("");
        unit1.setText("");
        unit2.setText("");
        label1=label2=0;
    }
    /**
     * @Description  打开Volume的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void VolumeShift(ActionEvent actionEvent) {
        Volume.setVisible(true);
        Length.setVisible(false);
        Weight.setVisible(false);
        Temperature.setVisible(false);
        Area.setVisible(false);
        Times.setVisible(false);
        Angle.setVisible(false);
        newText();
        TypeSet.setText("体积");
        type=1;
    }

    /**
     * @Description  打开Length的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void LengthShift(ActionEvent actionEvent) {
        Volume.setVisible(false);
        Length.setVisible(true);
        Weight.setVisible(false);
        Temperature.setVisible(false);
        Area.setVisible(false);
        Times.setVisible(false);
        Angle.setVisible(false);
        newText();
        TypeSet.setText("长度");
        type=2;
    }

    /**
     * @Description  打开Weight的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void WeightShift(ActionEvent actionEvent) {
        Volume.setVisible(false);
        Length.setVisible(false);
        Weight.setVisible(true);
        Temperature.setVisible(false);
        Area.setVisible(false);
        Times.setVisible(false);
        Angle.setVisible(false);
        newText();
        TypeSet.setText("质量");
        type=3;
    }

    /**
     * @Description  打开Temperature的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void TemperatureShift(ActionEvent actionEvent) {
        Volume.setVisible(false);
        Length.setVisible(false);
        Weight.setVisible(false);
        Temperature.setVisible(true);
        Area.setVisible(false);
        Times.setVisible(false);
        Angle.setVisible(false);
        newText();
        TypeSet.setText("温度");
        type=4;
    }

    /**
     * @Description  打开Area的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void AreaShift(ActionEvent actionEvent) {
        Volume.setVisible(false);
        Length.setVisible(false);
        Weight.setVisible(false);
        Temperature.setVisible(false);
        Area.setVisible(true);
        Times.setVisible(false);
        Angle.setVisible(false);
        newText();
        TypeSet.setText("面积");
        type=5;
    }

    /**
     * @Description  打开Time的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void TimeShift(ActionEvent actionEvent) {
        Volume.setVisible(false);
        Length.setVisible(false);
        Weight.setVisible(false);
        Temperature.setVisible(false);
        Area.setVisible(false);
        Times.setVisible(true);
        Angle.setVisible(false);
        newText();
        TypeSet.setText("时间");
        type=6;
    }

    /**
     * @Description  打开Angle的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void AngleShift(ActionEvent actionEvent) {
        Volume.setVisible(false);
        Length.setVisible(false);
        Weight.setVisible(false);
        Temperature.setVisible(false);
        Area.setVisible(false);
        Times.setVisible(false);
        Angle.setVisible(true);
        newText();
        TypeSet.setText("角度");
        type=7;
    }

    /**
     * @Description 目录中选择Volume单位的初始化
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 13:31
     **/
    public void A1_1(ActionEvent actionEvent) { unit1.setText("毫升"); label1=1; }
    public void A1_2(ActionEvent actionEvent) { unit1.setText("立方厘米"); label1=1; }
    public void A1_3(ActionEvent actionEvent) { unit1.setText("升"); label1=2; }
    public void A1_4(ActionEvent actionEvent) { unit1.setText("立方米"); label1=3; }
    public void A2_1(ActionEvent actionEvent) { unit2.setText("毫升"); label2=1; }
    public void A2_2(ActionEvent actionEvent) { unit2.setText("立方厘米"); label2=1; }
    public void A2_3(ActionEvent actionEvent) { unit2.setText("升"); label2=2; }
    public void A2_4(ActionEvent actionEvent) { unit2.setText("立方米"); label2=3; }

    /**
     * @Description 目录中选择Length单位的初始化
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 13:31
     **/
    public void B1_1(ActionEvent actionEvent) { unit1.setText("纳米"); label1=1; }
    public void B1_2(ActionEvent actionEvent) { unit1.setText("微米"); label1=2; }
    public void B1_3(ActionEvent actionEvent) { unit1.setText("毫米"); label1=3; }
    public void B1_4(ActionEvent actionEvent) { unit1.setText("厘米"); label1=6; }
    public void B1_5(ActionEvent actionEvent) { unit1.setText("米"); label1=4; }
    public void B1_6(ActionEvent actionEvent) { unit1.setText("公里"); label1=5; }
    public void B1_7(ActionEvent actionEvent) { unit1.setText("英寸"); label1=7; }
    public void B1_8(ActionEvent actionEvent) { unit1.setText("英尺"); label1=8; }
    public void B2_1(ActionEvent actionEvent) { unit2.setText("纳米"); label2=1; }
    public void B2_2(ActionEvent actionEvent) { unit2.setText("微米"); label2=2; }
    public void B2_3(ActionEvent actionEvent) { unit2.setText("毫米"); label2=3; }
    public void B2_4(ActionEvent actionEvent) { unit2.setText("厘米"); label2=6; }
    public void B2_5(ActionEvent actionEvent) { unit2.setText("米"); label2=4; }
    public void B2_6(ActionEvent actionEvent) { unit2.setText("公里"); label2=5; }
    public void B2_7(ActionEvent actionEvent) { unit2.setText("英寸"); label2=7; }
    public void B2_8(ActionEvent actionEvent) { unit2.setText("英尺"); label2=8; }

    /**
     * @Description 目录中选择Weight单位的初始化
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 13:31
     **/
    public void C1_1(ActionEvent actionEvent) { unit1.setText("毫克"); label1=1; }
    public void C1_2(ActionEvent actionEvent) { unit1.setText("克"); label1=2; }
    public void C1_3(ActionEvent actionEvent) { unit1.setText("千克"); label1=3;  }
    public void C1_4(ActionEvent actionEvent) { unit1.setText("吨"); label1=4; }
    public void C2_1(ActionEvent actionEvent) { unit2.setText("毫克"); label2=1; }
    public void C2_2(ActionEvent actionEvent) { unit2.setText("克"); label2=2; }
    public void C2_3(ActionEvent actionEvent) { unit2.setText("千克"); label2=3; }
    public void C2_4(ActionEvent actionEvent) { unit2.setText("吨"); label2=4; }

    /**
     * @Description 目录中选择Temperature单位的初始化
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 22:41
     **/
    public void D1_1(ActionEvent actionEvent) { unit1.setText("摄氏度"); label1=1; }
    public void D1_2(ActionEvent actionEvent) { unit1.setText("华氏度"); label1=2; }
    public void D1_3(ActionEvent actionEvent) { unit1.setText("开尔文"); label1=3; }
    public void D2_1(ActionEvent actionEvent) { unit2.setText("摄氏度"); label2=1; }
    public void D2_2(ActionEvent actionEvent) { unit2.setText("华氏度"); label2=2; }
    public void D2_3(ActionEvent actionEvent) { unit2.setText("开尔文"); label2=3; }

    /**
     * @Description 目录中选择Area单位的初始化
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 22:42
     **/
    public void E1_1(ActionEvent actionEvent) { unit1.setText("平方厘米"); label1=1; }
    public void E1_2(ActionEvent actionEvent) { unit1.setText("平方米"); label1=2; }
    public void E1_3(ActionEvent actionEvent) { unit1.setText("平方公里"); label1=5; }
    public void E2_1(ActionEvent actionEvent) { unit2.setText("平方厘米"); label2=1; }
    public void E2_2(ActionEvent actionEvent) { unit2.setText("平方米"); label2=2; }
    public void E2_3(ActionEvent actionEvent) { unit2.setText("平方公里"); label2=5; }

    /**
     * @Description 目录中选择Time单位的初始化
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 22:42
     **/
    public void F1_1(ActionEvent actionEvent) { unit1.setText("毫秒"); label1=1; }
    public void F1_2(ActionEvent actionEvent) { unit1.setText("秒"); label1=2; }
    public void F1_3(ActionEvent actionEvent) { unit1.setText("分钟"); label1=3; }
    public void F1_4(ActionEvent actionEvent) { unit1.setText("小时"); label1=4; }
    public void F1_5(ActionEvent actionEvent) { unit1.setText("天"); label1=5; }
    public void F1_6(ActionEvent actionEvent) { unit1.setText("周"); label1=6; }
    public void F1_7(ActionEvent actionEvent) { unit1.setText("年"); label1=7; }
    public void F2_1(ActionEvent actionEvent) { unit2.setText("毫秒"); label2=1; }
    public void F2_2(ActionEvent actionEvent) { unit2.setText("秒"); label2=2; }
    public void F2_3(ActionEvent actionEvent) { unit2.setText("分钟"); label2=3; }
    public void F2_4(ActionEvent actionEvent) { unit2.setText("小时"); label2=4; }
    public void F2_5(ActionEvent actionEvent) { unit2.setText("天"); label2=5; }
    public void F2_6(ActionEvent actionEvent) { unit2.setText("周"); label2=6; }
    public void F2_7(ActionEvent actionEvent) { unit2.setText("年"); label2=7; }

    /**
     * @Description 目录中选择Angle单位的初始化
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 22:42
     **/
    public void G1_1(ActionEvent actionEvent) { unit1.setText("度"); label1=1; }
    public void G1_2(ActionEvent actionEvent) { unit1.setText("弧度"); label1=2; }
    public void G2_1(ActionEvent actionEvent) { unit2.setText("度"); label2=1; }
    public void G2_2(ActionEvent actionEvent) { unit2.setText("弧度"); label2=2; }

    /**
     * @Description 初始化设置在输入栏点击回车键的行为
     * @param url 无意义
     * @param resourceBundle 无意义
     * @author ZhouYH
     * @date 2023/12/17 13:28
     **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VolumeShift(new ActionEvent());
        input.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override //键入ENTER时进行单位换算
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER)
                    Calculate(new ActionEvent());
            }
        });
    }
}
