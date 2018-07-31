package cn.lsmya.restaurant.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class App extends Application {
    private static Context mContext;
    private static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        sp = getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
    }

    public static void setLogin(boolean isLogin) {
        sp.edit().putBoolean("isLogin", isLogin).apply();
    }

    public static boolean isLogin() {
        return sp.getBoolean("isLogin", false);
    }

    public static void setStoreId(String id) {
        sp.edit().putString("store_id", id).apply();
    }

    public static String getStoreId(){
        return sp.getString("store_id","");
    }

    public static Context getContext() {
        return mContext;
    }
}
