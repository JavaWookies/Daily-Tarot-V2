package com.amcwustl.dailytarot.models;

public class Card {

    private String type;
    private String nameShort;
    private String name;
    private String value;
    private int valueInt;
    private String meaningUp;
    private String meaningRev;
    private String desc;

    public Card() {
    }

    public Card(String type, String nameShort, String name, String value, int valueInt, String meaningUp, String meaningRev, String desc) {
        this.type = type;
        this.nameShort = nameShort;
        this.name = name;
        this.value = value;
        this.valueInt = valueInt;
        this.meaningUp = meaningUp;
        this.meaningRev = meaningRev;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getNameShort() {
        return nameShort;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getValueInt() {
        return valueInt;
    }

    public String getMeaningUp() {
        return meaningUp;
    }

    public String getMeaningRev() {
        return meaningRev;
    }

    public String getDesc() {
        return desc;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValueInt(int valueInt) {
        this.valueInt = valueInt;
    }

    public void setMeaningUp(String meaningUp) {
        this.meaningUp = meaningUp;
    }

    public void setMeaningRev(String meaningRev) {
        this.meaningRev = meaningRev;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Card{" +
                "type='" + type + '\'' +
                ", nameShort='" + nameShort + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", valueInt=" + valueInt +
                ", meaningUp='" + meaningUp + '\'' +
                ", meaningRev='" + meaningRev + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
