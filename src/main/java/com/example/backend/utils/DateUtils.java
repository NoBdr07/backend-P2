package com.example.backend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	 private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	    public static String formatToMySQLDateTime(Date date) {
	        return sdf.format(date);
	    }
}
