package com.example.studentclubsmanagement.gson;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by 李子韬 on 2018/4/5.
 */

public class ClubIdList {

    @SerializedName("id_list")
    private int[] clubIdArray;

    public int[] getClubIdArray() {
        return clubIdArray;
    }

    public void setClubIdArray(int[] clubIdList) {
        this.clubIdArray = clubIdList;
    }

    @Override
    public String toString() {
        return "ClubIdList{" +
                "clubIdList=" + Arrays.toString(clubIdArray) +
                '}';
    }
}
