package cn.lsmya.library.base;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 使用弱引用管理Activity
 */

public class ActivityManager {

    @NonNull
    private static Map<String, List<WeakReference<Activity>>> mMap = new LinkedHashMap<>();
    @NonNull
    private static List<Activity> mList = new LinkedList<>();
    private static WeakReference<Activity> sCurrentActivityWeakRef;

    private ActivityManager() {
    }

    public static Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public static void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

    //关闭每一个list内的activity
    static public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
            mList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    // 把当前activity加入到管理器
    static public void add(@NonNull Activity activity) {
        String name = activity.getClass().getName();
        synchronized (ActivityManager.class) {
            if (mMap.containsKey(name)) {
                mMap.get(name).add(new WeakReference<>(activity));
            } else {
                List<WeakReference<Activity>> lists = new LinkedList<>();
                lists.add(new WeakReference<>(activity));
                mMap.put(name, lists);
            }
        }
    }

    // 删除某类Activity
    static public void remove(@NonNull Class clazz) {
        synchronized (ActivityManager.class) {
            mMap.remove(clazz.getName());
        }
    }

    // 删除某个Activity
    static public void remove(@NonNull Activity the) {
        String name = the.getClass().getName();
        synchronized (ActivityManager.class) {
            if (!mMap.containsKey(name)) {
                return;
            }
            List<WeakReference<Activity>> lists = mMap.get(name);
            for (int i = lists.size() - 1; i >= 0; i--) {
                WeakReference<Activity> wr = lists.get(i);
                Activity activity = wr.get();
                if (activity == null || activity == the) {
                    lists.remove(wr);
                }
            }
            if (lists.isEmpty()) {
                mMap.remove(name);
            }
        }
    }

    // 关闭多个某类Activity
    static public void finish(@NonNull Class[] classes) {
        for (Class clazz : classes) {
            finish(clazz);
        }
    }

    // 关闭某类Activity
    static public <T extends Activity> void finish(@NonNull Class<T> clazz) {
        List<T> lists_activity;
        synchronized (ActivityManager.class) {
            lists_activity = getAll(clazz);
            mMap.remove(clazz.getName());
        }
        for (int i = lists_activity.size() - 1; i >= 0; i--) {
            lists_activity.get(i).finish();
        }
    }

    // 关闭所有Activity
    static public void finishAll() {
        List<Activity> lists_activity;
        synchronized (ActivityManager.class) {
            lists_activity = getAll();
            mMap.clear();
        }
        for (int i = lists_activity.size() - 1; i >= 0; i--) {
            lists_activity.get(i).finish();
        }
    }

    // 关闭某类 符合条件的 所有某Activity
    @SuppressWarnings("unchecked")
    static public <T> void finishAll(@NonNull Class<T> clazz, @NonNull OnIsChecker<T> checker) {
        String name = clazz.getName();
        List<Activity> lists_activity = new ArrayList<>();
        synchronized (ActivityManager.class) {
            if (!mMap.containsKey(name)) {
                return;
            }

            List<WeakReference<Activity>> lists = mMap.get(name);
            for (int i = lists.size() - 1; i >= 0; i--) {
                WeakReference<Activity> wr = lists.get(i);
                Activity activity = wr.get();
                if (activity == null) {
                    lists.remove(wr);
                } else if (checker.is((T) activity)) {
                    lists_activity.add(activity);
                    lists.remove(wr);
                }

            }
            if (lists.isEmpty()) {
                mMap.remove(name);
            }
        }
        for (int i = lists_activity.size() - 1; i >= 0; i--) {
            lists_activity.get(i).finish();
        }
    }

    // 判断是否符合条件
    public interface OnIsChecker<T> {
        boolean is(T the);
    }

    // 清空
    static public void clear() {
        synchronized (ActivityManager.class) {
            mMap.clear();
        }
    }

    // 获取所有Activity
    @NonNull
    static public List<Activity> getAll() {
        synchronized (ActivityManager.class) {
            List<Activity> lists_activity = new ArrayList<>();
            for (String name : mMap.keySet()) {
                List<WeakReference<Activity>> _lists = mMap.get(name);
                for (int i = _lists.size() - 1; i >= 0; i--) {
                    WeakReference<Activity> wr = _lists.get(i);
                    Activity activity = wr.get();
                    if (activity == null) {
                        _lists.remove(wr);
                    } else {
                        lists_activity.add(activity);
                    }
                }
                if (_lists.isEmpty()) {
                    mMap.remove(name);
                }
            }
            return lists_activity;
        }
    }

    // 获取所有某类Activity
    @NonNull
    @SuppressWarnings("unchecked")
    static public <T extends Activity> List<T> getAll(@NonNull Class<T> clazz) {
        List<T> lists_activity = new ArrayList<>();
        String name = clazz.getName();
        synchronized (ActivityManager.class) {
            if (!mMap.containsKey(name)) {
                return lists_activity;
            }
            List<WeakReference<Activity>> _lists = mMap.get(name);
            for (int i = _lists.size() - 1; i >= 0; i--) {
                WeakReference<Activity> wr = _lists.get(i);
                Activity activity = wr.get();
                if (activity == null) {
                    _lists.remove(wr);
                } else {
                    lists_activity.add((T) activity);
                }
            }
            if (_lists.isEmpty()) {
                mMap.remove(name);
            }
        }

        return lists_activity;
    }

    /* 【让Activity只保留一个】关闭除此以外的其它Activity*/
    static public void finishOther(Activity the) {
        List<Activity> lists_activity = new ArrayList<>();
        synchronized (ActivityManager.class) {
            if (mMap.isEmpty()) {
                return;
            }
            for (String name : mMap.keySet().toArray(new String[mMap.size()])) {
                List<WeakReference<Activity>> lists = mMap.get(name);
                for (int i = lists.size() - 1; i >= 0; i--) {
                    WeakReference<Activity> wr = lists.get(i);
                    Activity activity = wr.get();
                    if (activity == null) {
                        lists.remove(wr);
                    } else if (activity != the) {
                        lists_activity.add(activity);
                        lists.remove(wr);
                    }
                }
                if (lists.isEmpty()) {
                    mMap.remove(name);
                }
            }
        }

        for (int i = lists_activity.size() - 1; i >= 0; i--) {
            lists_activity.get(i).finish();
        }
    }

    /* 【让Activity只保留一个】关闭除此以外的其它Activity*/
    static public void finishOther(@NonNull Class clazz) {
        String class_name = clazz.getName();
        List<Activity> lists_activity = new ArrayList<>();
        synchronized (ActivityManager.class) {
            if (mMap.isEmpty()) {
                return;
            }
            for (String name : mMap.keySet()) {
                if (name.equals(class_name)) {
                    continue;
                }
                List<WeakReference<Activity>> lists = mMap.get(name);
                for (int i = lists.size() - 1; i >= 0; i--) {
                    WeakReference<Activity> wr = lists.get(i);
                    Activity activity = wr.get();
                    if (activity == null) {
                        lists.remove(wr);
                    } else {
                        lists_activity.add(activity);
                        lists.remove(wr);
                    }
                }
                if (lists.isEmpty()) {
                    mMap.remove(name);
                }
            }
        }

        for (int i = lists_activity.size() - 1; i >= 0; i--) {
            lists_activity.get(i).finish();
        }
    }

}
