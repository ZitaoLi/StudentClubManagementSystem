package com.example.studentclubsmanagement.gson;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by 李子韬 on 2018/4/5.
 */

public class UserIdList {

    @SerializedName("id_list")
    private int[] userIdArray;

    public int[] getUserIdArray() {
        return userIdArray;
    }

    public void setClubIdArray(int[] userIdList) {
        this.userIdArray = userIdList;
    }

    @Override
    public String toString() {
        return "UserIdList{" +
                "UserIdList=" + Arrays.toString(userIdArray) +
                '}';
    }
}
