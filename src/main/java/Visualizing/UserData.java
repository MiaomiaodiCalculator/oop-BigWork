package Visualizing;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author ZhouYH
 * @Description
 * @date 2023/12/9 14:50
 */
public class UserData implements Serializable {
    private Timestamp saveTime;
    private String[] Items;

    public String[] getItem() { return Items; }
    public void setItem(String[] item) {
        this.Items = item;
    }

    public Timestamp getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Timestamp saveTime) {
        this.saveTime = saveTime;
    }
}
