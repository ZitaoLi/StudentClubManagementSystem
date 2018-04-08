package com.example.studentclubsmanagement.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * Created by 李子韬 on 2018/4/5.
 */

public class TimeUtil {

    public static String timestamp2string(Timestamp timestamp) {
        String tsString = "";
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            tsString = sdf.format(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsString;
    }

    public static void sleep(int sleepTime, final Callable<Void> func) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    func.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, sleepTime);
    }
}
