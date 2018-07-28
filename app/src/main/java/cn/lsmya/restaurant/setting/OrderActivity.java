package cn.lsmya.restaurant.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import cn.lsmya.library.base.BaseTitleActivity;
import cn.lsmya.restaurant.R;

public class OrderActivity extends BaseTitleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_order, null);
        ButterKnife.bind(this,inflate);
        setContentView(inflate);
        setTitle("有效订单");

    }
}
