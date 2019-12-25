package com.example.administer.materialdisgnpullapp3.entity;

/**
 * Created by XHD on 2019/11/20
 */
public class TextData {
    private String text;

    public TextData(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public TextData setText(String text) {
        this.text = text;
        return this;
    }
}
