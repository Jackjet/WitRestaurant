package cn.lsmya.restaurant.takeOut;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.lsmya.library.NetWorkClient.ApiClientRequest;
import cn.lsmya.library.NetWorkClient.ApiClientResponse;
import cn.lsmya.library.NetWorkClient.ApiClientResponseWithPullHandler;
import cn.lsmya.library.PullLayout.PullLayout;
import cn.lsmya.library.PullLayout.PullRecyclerView;
import cn.lsmya.library.base.BaseFragment;
import cn.lsmya.library.base.BaseRecyclerViewAdapter;
import cn.lsmya.restaurant.R;
import cn.lsmya.restaurant.adapter.TakeOutListAdapter;
import cn.lsmya.restaurant.app.App;
import cn.lsmya.restaurant.app.ROUTE;
import cn.lsmya.restaurant.model.ChangeOrderTypeModel;
import cn.lsmya.restaurant.model.OrderDataModel;
import cn.lsmya.restaurant.model.ListModel;
import cn.lsmya.restaurant.orderData.EatActivity;
import cn.lsmya.restaurant.orderData.TakeOutDataActivity;
import cn.lsmya.restaurant.util.ToastUtil;

public class TakeOutToDoFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnChildViewClickListener, ApiClientResponseWithPullHandler.GetPullLayout {

    @BindView(R.id.fragmentList_recyclerView)
    PullRecyclerView recyclerView;
    @BindView(R.id.fragmentList_pullLayout)
    PullLayout pullLayout;
    @BindView(R.id.fragmentList_null)
    ImageView nullData;

    private TakeOutListAdapter adapter;
    private List<OrderDataModel> list;
    private ApiHandler apiHandler = new ApiHandler(this);
    private ApiClientRequest apiClientRequest;
    private ApiClientRequest apiClientRequest_change;

    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void onViewCreated(View view) {
        list = new ArrayList<>();
        apiClientRequest = new ApiClientRequest(getActivity(), ROUTE.ORDER_LIST, apiHandler);
        apiClientRequest_change = new ApiClientRequest(getActivity(), ROUTE.CHANGE_ORDER, apiHandler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TakeOutListAdapter(getActivity(), list, this);
        recyclerView.setAdapter(adapter);
        pullLayout.addOnPullListener(new PullLayout.OnPullListener() {
            @Override
            public void onRefresh(PullLayout pullToRefreshLayout, boolean firstRefresh) {
                page = 1;
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
    }

    @Override
    protected void onViewShow() {
        super.onViewShow();
        pullLayout.refresh(true);
    }

    @Override
    public void onViewClick(int position, int id) {
        OrderDataModel model = list.get(position);
        switch (id) {
            case -1:
                String storeId = model.getStore_id();
                String orderNo = model.getOrder_no();
                Intent intent = new Intent(getActivity(), TakeOutDataActivity.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("orderNo", orderNo);
                startActivity(intent);
                break;
            case 1:
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", model.getId());
                map.put("type", "ship");
                map.put("store_id", model.getStore_id());
                try {
                    map.put("modal", model.getOrder_address_log().getMobile());
                } catch (Exception e) {
                    map.put("modal", null);
                }
                apiClientRequest_change.setDebug(true);
                apiClientRequest_change.setPost(map);
                apiClientRequest_change.submit();
                break;
        }
    }

    @Nullable
    @Override
    public PullLayout getPullLayout() {
        return pullLayout;
    }

    private static class ApiHandler extends ApiClientResponseWithPullHandler<TakeOutToDoFragment> {
        public ApiHandler(TakeOutToDoFragment the) {
            super(the);
        }

        @Override
        public int onSuccess(TakeOutToDoFragment the, ApiClientRequest request, ApiClientResponse response, @Nullable PullLayout pullLayout) {
            if (request.isRoute(ROUTE.ORDER_LIST)) {
                ListModel data = response.getData(ListModel.class);
                if (data.getStatus() == OK) {
                    ArrayList<OrderDataModel> list = data.getData();
                    if (the.page == 1) {
                        the.list.clear();
                    }
                    the.list.addAll(list);
                    the.adapter.notifyDataSetChanged();
                    if (list.size() != 0) {
                        the.pullLayout.setVisibility(View.VISIBLE);
                        the.nullData.setVisibility(View.GONE);
                    } else {
                        the.pullLayout.setVisibility(View.GONE);
                        the.nullData.setVisibility(View.VISIBLE);
                    }
                    the.page++;
                    if (list.size() < 6) {
                        return ALL;
                    } else {
                        return SUCCEED;
                    }
                } else {
                    return FAIL;
                }
            } else if (request.isRoute(ROUTE.CHANGE_ORDER)) {
                ChangeOrderTypeModel data = response.getData(ChangeOrderTypeModel.class);
                if (data.getStatus() == OK) {
                    the.pullLayout.refresh(true);
                }
                ToastUtil.showTextToast(the.getActivity(), data.getInfo());
            }
            return IGNORE;
        }


        @Override
        public boolean onSubmitBefore(TakeOutToDoFragment the, ApiClientRequest request) {
            if (request.isRoute(ROUTE.ORDER_LIST)) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("store_id", App.getStoreId());
                map.put("order_type", "ship");
                map.put("status", "pay");
                map.put("page", the.page);
                request.setPost(map);
            }
            return super.onSubmitBefore(the, request);
        }
    }
}
