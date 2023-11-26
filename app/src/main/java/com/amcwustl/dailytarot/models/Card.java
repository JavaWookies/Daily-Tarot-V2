package com.amcwustl.dailytarot.models;

public class Card {

    private Long id;
    private String type;
    private String nameShort;
    private String name;
    private String meaningUp;
    private String meaningRev;
    private String desc;
    private String intPast;
    private String intPresent;
    private String intFuture;
    private String associatedWords;
    private int orientation;

    public Card() {
    }

    public Card(Long id, String type, String nameShort, String name, String meaningUp, String meaningRev, String desc, int orientation, String intPast, String intPresent, String intFuture, String associatedWords) {
        this.id = id;
        this.type = type;
        this.nameShort = nameShort;
        this.name = name;
        this.meaningUp = meaningUp;
        this.meaningRev = meaningRev;
        this.desc = desc;
        this.orientation = orientation;
        this.intPast = intPast;
        this.intPresent = intPresent;
        this.intFuture = intFuture;
        this.associatedWords = associatedWords;
    }

    public Card(Long id, String type, String nameShort, String name, String meaningUp, String meaningRev, String desc, String intPast, String intPresent, String intFuture, String associatedWords) {
        this.id = id;
        this.type = type;
        this.nameShort = nameShort;
        this.name = name;
        this.meaningUp = meaningUp;
        this.meaningRev = meaningRev;
        this.desc = desc;
        this.orientation = 0;
        this.intPast = intPast;
        this.intPresent = intPresent;
        this.intFuture = intFuture;
        this.associatedWords = associatedWords;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public void setMeaningUp(String meaningUp) {
        this.meaningUp = meaningUp;
    }

    public void setMeaningRev(String meaningRev) {
        this.meaningRev = meaningRev;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getIntPast() {
        return intPast;
    }

    public void setIntPast(String intPast) {
        this.intPast = intPast;
    }

    public String getIntPresent() {
        return intPresent;
    }

    public void setIntPresent(String intPresent) {
        this.intPresent = intPresent;
    }

    public String getIntFuture() {
        return intFuture;
    }

    public void setIntFuture(String intFuture) {
        this.intFuture = intFuture;
    }

    public String getAssociatedWords() {
        return associatedWords;
    }

    public void setAssociatedWords(String associatedWords) {
        this.associatedWords = associatedWords;
    }



}
