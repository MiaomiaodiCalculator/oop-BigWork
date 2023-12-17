module com.calculator.calculation {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires jep.java;
    requires jlatexmath;
    requires commons.math3;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;

    opens com.calculator.calculation to javafx.fxml;
    exports com.calculator.calculation;
    exports Scientific;
    opens Scientific to javafx.fxml,javafx.base;
    opens NewFunction to javafx.base;
    opens Probability to javafx.base;
}