module com.calculator.calculation {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires jep.java;
    requires jlatexmath;
    requires commons.math3;

    opens com.calculator.calculation to javafx.fxml;
    exports com.calculator.calculation;
    exports scientific;
    opens scientific to javafx.fxml,javafx.base;
    opens NewFunction to javafx.base;
}