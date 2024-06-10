package com.bdr.backend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// This method is used to format the date to a MySQL compatible format
	public static String formatToMySQLDateTime(Date date) {
		return sdf.format(date);
	}
}
