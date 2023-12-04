module com.calculator.calculation {
        requires javafx.controls;
        requires javafx.fxml;
        requires org.kordamp.ikonli.javafx;
        requires jep.java;
        requires jlatexmath;
        requires commons.math3;
    requires java.desktop;

    exports com.calculator.calculation;
        exports Probability.Exception;
        exports Probability;
        exports scientific;
        opens scientific to javafx.fxml,javafx.base;
        opens NewFunction to javafx.base;
    opens com.calculator.calculation to javafx.base, javafx.fxml;
}