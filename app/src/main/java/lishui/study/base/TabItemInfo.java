package lishui.study.base;

/**
 * Created by lishui.lin on 19-11-12
 */
public class TabItemInfo {

    public static final int NO_ID = -1;

    private int id = NO_ID;
    private int position = 0;
    private String title = "";
    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
