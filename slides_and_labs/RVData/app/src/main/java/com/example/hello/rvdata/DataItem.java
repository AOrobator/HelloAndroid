package com.example.hello.rvdata;

class DataItem {
    String itemName;
    String image;

    public DataItem(String itemName, String image) {
        this.itemName = itemName;
        this.image = image;
    }

    public String getItemName() {

        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
