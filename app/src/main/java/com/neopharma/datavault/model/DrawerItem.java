package com.neopharma.datavault.model;

public class DrawerItem {

    public int icon;
    public String name;
    public boolean isSelected;

    public DrawerItem(int icon, String name, boolean isSelected) {
        this.icon = icon;
        this.name = name;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}