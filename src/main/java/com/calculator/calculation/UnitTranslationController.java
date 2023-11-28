package com.calculator.calculation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author ZhouYH
 * @Description 处理单位换算的点击事件
 * @date 2023/11/27 19:46
 */
public class UnitTranslationController {
    public TextField input;
    public TextField output;
    public TextField unit1;
    public TextField unit2;
    public TextField TypeSet;
    public int type;
    public int label1; //取0时即暂未设置该单位
    public int label2; //取0时即暂未设置该单位
    BigDecimal in;
    BigDecimal out;
    @FXML
    private StackPane UnitContainer;

    /**
     * @Description  加载卡片布局
     * @param fxmlFileName 要打开的fxml文件名称
     * @author ZhouYH
     * @date 2023/11/27 23:59
     **/
    private void loadPage(String fxmlFileName) {
        try {
            Pane page = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFileName)));
            UnitContainer.getChildren().setAll(page);
        } catch (Exception e) {
            System.out.println("error");
        }
    }
    
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
                if (label1==label2){
                    output.setText(in.toString());
                }else {
                    out = in;
                    BigDecimal Big1000=new BigDecimal("1000");
                    switch (type){
                        case 1:
                            if (label1<label2){
                                for (int tmp=label1; tmp<label2; tmp++)
                                    out=out.divide(Big1000);
                            }else {
                                for (int tmp=label1; tmp>label2; tmp--)
                                    out=out.multiply(Big1000);
                            }
                            break;
                        case 2:
                            BigDecimal Big100=new BigDecimal("100");
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
                        default:
                            out=new BigDecimal("000000");
                            break;
                    }
                    String Str=out.toString();
                    if (Str.matches("\\d+.\\d*0+"))
                        while (Str.charAt(Str.length()-1)=='0')
                            Str=Str.substring(0,Str.length()-1);
                    if (Str.matches("\\d+."))
                        Str=Str.substring(0,Str.length()-1);
                    output.setText(Str);
                }
            }catch (NumberFormatException e){
                output.setText("Illegal Input!");
            }catch (ArithmeticException e){
                output.setText("error!");
            }
        }
    }

    /**
     * @Description  打开Volume的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void VolumeShift(ActionEvent actionEvent) { loadPage("Volume.fxml"); TypeSet.setText("Volume"); }

    /**
     * @Description  打开Length的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void LengthShift(ActionEvent actionEvent) { loadPage("Length.fxml"); TypeSet.setText("Length"); }

    /**
     * @Description  打开Weight的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void WeightShift(ActionEvent actionEvent) { loadPage("Weight.fxml"); TypeSet.setText("Weight"); }

    /**
     * @Description  打开Temperature的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void TemperatureShift(ActionEvent actionEvent) { loadPage("Temperature.fxml"); TypeSet.setText("Temperature"); }

    /**
     * @Description  打开Area的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void AreaShift(ActionEvent actionEvent) { loadPage("Area.fxml"); TypeSet.setText("Area"); }

    /**
     * @Description  打开Time的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void TimeShift(ActionEvent actionEvent) { loadPage("Time.fxml"); TypeSet.setText("Time"); }

    /**
     * @Description  打开Angle的单位换算页面
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 0:00
     **/
    public void AngleShift(ActionEvent actionEvent) { loadPage("Angle.fxml"); TypeSet.setText("Angle"); }

    /**
     * @Description 目录中选择Volume单位的初始化
     * @param actionEvent 无意义
     * @author ZhouYH
     * @date 2023/11/28 13:31
     **/
    public void A1_1(ActionEvent actionEvent) { unit1.setText("毫升"); label1=1; type=1; }
    public void A1_2(ActionEvent actionEvent) { unit1.setText("立方厘米"); label1=1; type=1; }
    public void A1_3(ActionEvent actionEvent) { unit1.setText("升"); label1=2; type=1; }
    public void A1_4(ActionEvent actionEvent) { unit1.setText("立方米"); label1=3; type=1; }
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
    public void B1_1(ActionEvent actionEvent) { unit1.setText("纳米"); label1=1; type=2; }
    public void B1_2(ActionEvent actionEvent) { unit1.setText("微米"); label1=2; type=2; }
    public void B1_3(ActionEvent actionEvent) { unit1.setText("毫米"); label1=3; type=2; }
    public void B1_4(ActionEvent actionEvent) { unit1.setText("厘米"); label1=6; type=2; }
    public void B1_5(ActionEvent actionEvent) { unit1.setText("米"); label1=4; type=2; }
    public void B1_6(ActionEvent actionEvent) { unit1.setText("公里"); label1=5; type=2; }
    public void B1_7(ActionEvent actionEvent) { unit1.setText("英寸"); label1=7; type=2; }
    public void B1_8(ActionEvent actionEvent) { unit1.setText("英尺"); label1=8; type=2; }
    public void B2_1(ActionEvent actionEvent) { unit2.setText("纳米"); label2=1; }
    public void B2_2(ActionEvent actionEvent) { unit2.setText("微米"); label2=2; }
    public void B2_3(ActionEvent actionEvent) { unit2.setText("毫米"); label2=3; }
    public void B2_4(ActionEvent actionEvent) { unit2.setText("厘米"); label2=6; }
    public void B2_5(ActionEvent actionEvent) { unit2.setText("米"); label2=4; }
    public void B2_6(ActionEvent actionEvent) { unit2.setText("公里"); label2=5; }
    public void B2_7(ActionEvent actionEvent) { unit2.setText("英寸"); label2=7; }
    public void B2_8(ActionEvent actionEvent) { unit2.setText("英尺"); label2=8; }
}
