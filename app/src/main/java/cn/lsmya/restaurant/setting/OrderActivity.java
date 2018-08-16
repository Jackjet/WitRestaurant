package cn.lsmya.restaurant.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lsmya.library.NetWorkClient.ApiClientRequest;
import cn.lsmya.library.NetWorkClient.ApiClientResponse;
import cn.lsmya.library.NetWorkClient.ApiClientResponseWithPullHandler;
import cn.lsmya.library.PullLayout.PullLayout;
import cn.lsmya.library.PullLayout.PullRecyclerView;
import cn.lsmya.library.base.BaseTitleActivity;
import cn.lsmya.library.util.TimeUtils;
import cn.lsmya.restaurant.R;
import cn.lsmya.restaurant.app.App;
import cn.lsmya.restaurant.app.ROUTE;
import cn.lsmya.restaurant.model.DataListModel;
import cn.lsmya.restaurant.model.OrderListModel;
import cn.lsmya.restaurant.util.ToastUtil;
import cn.lsmya.restaurant.view.RecyclerViewAdapter.OnBindView;
import cn.lsmya.restaurant.view.RecyclerViewAdapter.RecyclerViewAdapter;
import cn.lsmya.restaurant.view.RecyclerViewAdapter.ViewClickListener;

public class OrderActivity extends BaseTitleActivity implements ApiClientResponseWithPullHandler.GetPullLayout {
    @BindView(R.id.order_recyclerView)
    PullRecyclerView recyclerView;
    @BindView(R.id.order_pullLayout)
    PullLayout pullLayout;
    private List<DataListModel> list;
    private RecyclerViewAdapter adapter;

    private ApiHandler apiHandler = new ApiHandler(this);
    private ApiClientRequest apiClientRequest;
    private int limit = 10;
    private int p = 1;

    private TextView headOrder_amount;
    private TextView headOrder_today;
    private TextView headOrder_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_order, null);
        ButterKnife.bind(this, inflate);
        setContentView(inflate);
        setTitle("有效订单");
        apiClientRequest = new ApiClientRequest(this, ROUTE.ORDER_MONEY, apiHandler);
        list = new ArrayList<>();
        pullLayout.setCanLoadMore(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(list, R.layout.item_order, new OnBindView() {
            @Override
            public void bindView(int position, Object data, View itemView, ViewClickListener.OnChildViewClickListener viewClickListener) {
                TextView number = adapter.getView(itemView, R.id.order_number);
                TextView time = adapter.getView(itemView, R.id.order_time);
                TextView status = adapter.getView(itemView, R.id.order_status);
                TextView money = adapter.getView(itemView, R.id.order_money);
                DataListModel model = (DataListModel) data;
                number.setText(model.getOrder_no());
                time.setText(TimeUtils.todayYyyyMmDdHhMm(Long.parseLong(model.getCreate_time())));
                if (model.getStatus().equals("success")) {
                    status.setText("已完成");
                }
                money.setText("¥" + model.getPrice());
            }
        });
        recyclerView.setAdapter(adapter);
        View view = LayoutInflater.from(this).inflate(R.layout.head_order, null);
        headOrder_amount = view.findViewById(R.id.headOrder_amount);
        headOrder_today = view.findViewById(R.id.headOrder_today);
        headOrder_number = view.findViewById(R.id.headOrder_number);

        adapter.addHeaderView(view);
        pullLayout.addOnPullListener(new PullLayout.OnPullListener() {
            @Override
            public void onRefresh(PullLayout pullToRefreshLayout, boolean firstRefresh) {
                p = 1;
                apiClientRequest.submit();
            }

            @Override
            public void onLoadMore(PullLayout pullToRefreshLayout) {
                apiClientRequest.submit();
            }

            @Override
            public void onErrorClickReload(PullLayout pullToRefreshLayout) {
                pullToRefreshLayout.refresh(true);
            }
        });
        pullLayout.refresh(true);
    }

    @Nullable
    @Override
    public PullLayout getPullLayout() {
        return pullLayout;
    }

    private static class ApiHandler extends ApiClientResponseWithPullHandler<OrderActivity> {
        public ApiHandler(OrderActivity the) {
            super(the);
        }

        @Override
        public boolean onSubmitBefore(OrderActivity the, ApiClientRequest request) {
            if (request.isRoute(ROUTE.ORDER_MONEY)) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("store_id", App.getStoreId());
                map.put("create_time", "today");
                map.put("status", "success");
                map.put("limit", the.limit);
                map.put("p", the.p);
                request.setPost(map);
            }
            return super.onSubmitBefore(the, request);
        }

        @Override
        public int onSuccess(OrderActivity the, ApiClientRequest request, ApiClientResponse response, @Nullable PullLayout pullLayout) {
            if (request.isRoute(ROUTE.ORDER_MONEY)) {
                OrderListModel data = response.getData(OrderListModel.class);
                if (data.getStatus() == OK) {
                    OrderListModel.DataModel model = data.getData();
                    the.headOrder_amount.setText(model.getZongJinE());
                    the.headOrder_today.setText(String.valueOf(model.getAmount()));
                    the.headOrder_number.setText(model.get_total());
                    List<DataListModel> list = model.get_list();
                    if (the.p == 1) {
                        the.list.clear();
                    }
                    the.list.addAll(list);
                    the.adapter.notifyDataSetChanged();
                    if (the.limit > list.size()) {
                        return ALL;
                    } else {
                        return SUCCEED;
                    }
                } else {
                    ToastUtil.showTextToast(the, data.getInfo());
                }
            }
            return IGNORE;
        }
    }
}
