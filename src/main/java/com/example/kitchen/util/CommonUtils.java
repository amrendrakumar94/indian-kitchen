package com.example.kitchen.util;

import java.sql.Timestamp;
import java.util.Calendar;

public class CommonUtils {
    public static boolean isNotNullAndNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static Timestamp getCurrentTimestamp() {
        Calendar calendar = Calendar.getInstance();
        return new Timestamp(calendar.getTimeInMillis());
    }


}
