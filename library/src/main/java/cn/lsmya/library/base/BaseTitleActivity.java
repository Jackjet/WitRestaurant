package cn.lsmya.library.base;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.lsmya.library.R;

public abstract class BaseTitleActivity extends BaseActivity {

    public ImageView back;        //返回
    private TextView titleView;          //中间TextView
    public ImageView ivRight;       //右侧图片
    public TextView tvRight;        //右侧TextView
    private RelativeLayout contentView;    //内容布局
    public TextView tvLeft;         //左侧TextView
    public RelativeLayout TitleBar; //整个顶部Bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_title);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        TitleBar = (RelativeLayout) findViewById(R.id.base_TitleBar);
        back = (ImageView) findViewById(R.id.base_back);
        titleView = (TextView) findViewById(R.id.base_title);
        ivRight = (ImageView) findViewById(R.id.base_iv_right);
        tvRight = (TextView) findViewById(R.id.base_tv_right);
        contentView = (RelativeLayout) findViewById(R.id.base_content);
        tvLeft = (TextView) findViewById(R.id.base_tv_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setContentView(View view) {
        contentView.addView(view);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }
}