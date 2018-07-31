package cn.lsmya.restaurant.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ToastUtil {
    @Nullable
    private static Toast toast = null;

    public static void showTextToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

}
