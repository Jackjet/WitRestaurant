package cn.lsmya.restaurant.util.Glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GlideUtils {
    private static GlideUtils glideUtils;

    public static GlideUtils getInstence() {
        if (glideUtils == null) {
            glideUtils = new GlideUtils();
        }
        return glideUtils;
    }

    /**
     * 加载为圆形图片
     */
    public void loadCircle(Context context, String url, ImageView imageView) {
        try {
            Glide.with(context)
                    .load(url)
                    .transform(new GlideCircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade(1000)
                    .into(imageView);
        } catch (Exception e) {

        }
    }

}
