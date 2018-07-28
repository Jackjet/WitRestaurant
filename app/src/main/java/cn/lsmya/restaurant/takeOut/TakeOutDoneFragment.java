package cn.lsmya.restaurant.takeOut;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.lsmya.library.NetWorkClient.ApiClientRequest;
import cn.lsmya.library.NetWorkClient.ApiClientResponse;
import cn.lsmya.library.NetWorkClient.ApiClientResponseHandler;
import cn.lsmya.library.NetWorkClient.ApiClientResponseWithPullHandler;
import cn.lsmya.library.base.BaseFragment;
import cn.lsmya.library.base.BaseRecyclerViewAdapter;
import cn.lsmya.restaurant.R;
import cn.lsmya.restaurant.adapter.ListAdapter;
import cn.lsmya.restaurant.app.App;
import cn.lsmya.restaurant.app.ROUTE;
import cn.lsmya.restaurant.model.ListDataModel;
import cn.lsmya.restaurant.model.ListModel;

public class TakeOutDoneFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnChildViewClickListener {

    @BindView(R.id.fragmentList_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fragmentList_refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fragmentList_null)
    ImageView nullData;

    private ListAdapter adapter;
    private List<ListDataModel> list;
    private ApiHandler apiHandler = new ApiHandler(this);
    private ApiClientRequest apiClientRequest;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void onViewCreated(View view) {
        list = new ArrayList<>();
        apiClientRequest = new ApiClientRequest(getActivity(), ROUTE.ORDER_LIST, apiHandler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ListAdapter(getActivity(), list, this);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiClientRequest.submit();
            }
        });

    }

    @Override
    protected void onViewShow() {
        super.onViewShow();
        apiClientRequest.submit();

    }

    @Override
    public void onViewClick(int position, int id) {
        switch (id){
            case -1:
                break;
        }
    }

    private static class ApiHandler extends ApiClientResponseHandler<TakeOutDoneFragment> {
        public ApiHandler(TakeOutDoneFragment the) {
            super(the);
        }

        @Override
        public void onSuccess(TakeOutDoneFragment the, ApiClientRequest request, ApiClientResponse response) {
            if (request.isRoute(ROUTE.ORDER_LIST)) {
                ListModel data = response.getData(ListModel.class);
                if (data.getStatus() == OK) {
                    ArrayList<ListDataModel> list = data.getData();
                    the.list.clear();
                    the.list.addAll(list);
                    the.adapter.notifyDataSetChanged();
                    if (list.size() != 0) {
                        the.refreshLayout.setVisibility(View.VISIBLE);
                        the.nullData.setVisibility(View.GONE);
                    } else {
                        the.recyclerView.setVisibility(View.GONE);
                        the.nullData.setVisibility(View.VISIBLE);
                    }
                } else {

                }
            }
        }

        @Override
        public void onFinished(TakeOutDoneFragment the, ApiClientRequest request) {
            super.onFinished(the, request);
            if (request.isRoute(ROUTE.ORDER_LIST)) {
                if (the.refreshLayout.isRefreshing()) {
                    the.refreshLayout.setRefreshing(false);
                }

            }
        }

        @Override
        public boolean onSubmitBefore(TakeOutDoneFragment the, ApiClientRequest request) {
            if (request.isRoute(ROUTE.ORDER_LIST)) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("store_id", App.getStoreId());
                map.put("order_type", "ship");
                map.put("status", "success");
                request.setPost(map);
            }
            return super.onSubmitBefore(the, request);
        }
    }
}
