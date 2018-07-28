package cn.lsmya.library.NetWorkClient;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class LogUtil {
    private static final String TAG = "日志";

    private static boolean DEBUG_MODE = true;

    public static void init(boolean isDebugModel) {
        DEBUG_MODE = isDebugModel;
    }

    public static void setDebugMode(boolean isDebugMode) {
        DEBUG_MODE = isDebugMode;
    }

    public static void v(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.v(TAG, "[" + tag + "] " + msg);
        }
    }

    public static void v(@NonNull Class<?> clazz, String msg) {
        v(getClassSimpleName(clazz), msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.d(TAG, "[" + tag + "] " + msg);
        }
    }

    public static void d(@NonNull Class<?> clazz, String msg) {
        d(getClassSimpleName(clazz), msg);
    }

    public static void i(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.i(TAG, "[" + tag + "] " + msg);
        }
    }

    public static void i(String tag, Object msg) {
        if (DEBUG_MODE) {
            Log.i(TAG, "[" + tag + "] " + String.valueOf(msg));
        }
    }

    public static void i(@NonNull Class<?> clazz, String msg) {
        i(getClassSimpleName(clazz), msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.w(TAG, "[" + tag + "] " + msg);
        }
    }

    public static void w(@NonNull Class<?> clazz, String msg) {
        w(getClassSimpleName(clazz), msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.e(TAG, "[" + tag + "] " + msg);
        }
    }

    public static void e(String tag, String msg, @Nullable Throwable tr) {
        if (DEBUG_MODE) {
            Log.e(TAG, "[" + tag + "] " + msg, tr);
        }
        if (tr != null) {
        }
    }


    public static void e(String tag, @NonNull Throwable tr) {
        e(tag, tr.getMessage(), tr);
    }

    public static void e(@NonNull Class<?> clazz, @NonNull Throwable tr) {
        e(getClassSimpleName(clazz), tr.getMessage(), tr);
    }

    public static void e(@NonNull Class<?> clazz, String msg, Throwable tr) {
        e(getClassSimpleName(clazz), msg, tr);
    }

    public static void e(@NonNull Class<?> clazz, String msg) {
        e(getClassSimpleName(clazz), msg);
    }


    public static void e(@NonNull Object clazz, String s) {
        e(getClassSimpleName(clazz.getClass()), s);
    }


    @NonNull
    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    }

    private static String getClassSimpleName(@NonNull Class clazz) {
        if (clazz.isAnonymousClass()) {
            return clazz.getEnclosingClass().getSimpleName() + "/" + clazz.getSuperclass().getSimpleName();
        } else {
            Class<?> up = clazz.getEnclosingClass();
            if (up == null) {
                return clazz.getSimpleName();
            } else {
                return up.getSimpleName() + "/" + clazz.getSimpleName();
            }
        }
    }


    public static void debug(@NonNull Object... objs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objs.length; i++) {
            sb.append(objs[i]);
            if (i + 1 != objs.length) {
                sb.append(", ");
            }
        }
        Log.e(TAG, "[DEBUG] " + sb.toString());
    }

}
