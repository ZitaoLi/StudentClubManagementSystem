package com.example.studentclubsmanagement.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 李子韬 on 2018/4/4.
 */

public class Power {

    @SerializedName("power")
    public List<PowerItem> power;

    public List<PowerItem> getPower() {
        return power;
    }

    public void setPower(List<PowerItem> power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "Power{" +
                "power=" + power +
                '}';
    }
}
