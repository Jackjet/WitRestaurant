package cn.lsmya.restaurant.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 不可滑动的ListView
 * 用于ListView、GridView、RecyclerView中嵌套ListView时使用
 * Created by lsm on 2018/1/4.
 */
public class ListViewNoSlide extends ListView {
    public ListViewNoSlide(Context context) {
        super(context);
    }

    public ListViewNoSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewNoSlide(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /***
     * 此方法用于重新测量尺寸
     * 就可以解决ScrollView、ListView嵌套问题
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}