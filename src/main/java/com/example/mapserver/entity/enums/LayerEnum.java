package com.example.mapserver.entity.enums;

import lombok.AllArgsConstructor;

/**
 * @Description
 * @Author bin
 * @Date 2021/12/09
 */
@AllArgsConstructor
public enum LayerEnum {
    img_c(0, "img_c"),
    cva_c(1, "cva_c"),
    cia_c(2, "cia_c"),
    vec_c(3, "vec_c");

    private int number;
    private String text;

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }
}
