package cn.lsmya.library.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    private static final SimpleDateFormat YYYY_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());
    private static final SimpleDateFormat MMDD_HHMM_FORMAT = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
    private static final SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat YYYYMMDDHHMMSS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat YYYYMMDDHHMM_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    private static final SimpleDateFormat HHMMSS_FORMAT = new SimpleDateFormat("HHmmss", Locale.getDefault());
    private static final SimpleDateFormat YYYYMMDDHHMMSS_FORMAT_NUMBER = new SimpleDateFormat("〔yyyyMMdd〕HHmmss", Locale.getDefault());

    public static String todayYyyyMmDd() {
        return YYYYMMDD_FORMAT.format(new Date());
    }

    public static String todayYyyy() {
        return YYYY_FORMAT.format(new Date());
    }

    public static String todayYyyyMmDdHhMmSs() {
        return YYYYMMDDHHMMSS_FORMAT.format(new Date());
    }

    public static String todayYyyyMmDdHhMmSs(Date date) {
        return YYYYMMDDHHMMSS_FORMAT.format(date);
    }

    public static String todayYyyyMmDdHhMmSs(long time) {
        return YYYYMMDDHHMMSS_FORMAT.format(time);
    }

    public static String todayMmDdHhMm(long time) {
        return MMDD_HHMM_FORMAT.format(time);
    }

    public static String todayYyyyMmDdHhMm(long time) {
        return YYYYMMDDHHMM_FORMAT.format(time);
    }

}
