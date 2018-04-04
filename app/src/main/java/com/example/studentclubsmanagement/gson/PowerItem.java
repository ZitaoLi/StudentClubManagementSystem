package com.example.studentclubsmanagement.gson;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by 李子韬 on 2018/4/4.
 */

public class PowerItem {

    @SerializedName("power_item")
    private int[] items;

    public int[] getItems() {
        return items;
    }

    public void setItems(int[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "PowerItem{" +
                "items=" + Arrays.toString(items) +
                '}';
    }
}
