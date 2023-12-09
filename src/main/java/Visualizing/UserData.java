package Visualizing;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

/**
 * @author ZhouYH
 * @Description
 * @date 2023/12/9 14:50
 */
public class UserData implements Serializable {
    private String dataName;
    private String[] Items;

    public String getName() {
        return dataName;
    }
    public void setName(String name) {
        this.dataName = name;
    }
    public String[] getItem() { return Items; }
    public void setItem(String[] item) {
        this.Items = item;
    }
    public StringProperty dataNameProperty() {
        return new SimpleStringProperty(dataName);
    }
}
