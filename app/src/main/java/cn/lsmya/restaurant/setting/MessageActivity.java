package cn.lsmya.restaurant.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lsmya.library.NetWorkClient.ApiClientRequest;
import cn.lsmya.library.NetWorkClient.ApiClientResponse;
import cn.lsmya.library.NetWorkClient.ApiClientResponseWithPullHandler;
import cn.lsmya.library.PullLayout.PullLayout;
import cn.lsmya.library.PullLayout.PullRecyclerView;
import cn.lsmya.library.base.BaseTitleActivity;
import cn.lsmya.restaurant.R;
import cn.lsmya.restaurant.adapter.MessageAdapter;
import cn.lsmya.restaurant.app.ROUTE;
import cn.lsmya.library.base.BaseRecyclerViewAdapter;
import cn.lsmya.restaurant.model.MessageListModel;
import cn.lsmya.restaurant.model.MessageModel;

public class MessageActivity extends BaseTitleActivity implements ApiClientResponseWithPullHandler.GetPullLayout, BaseRecyclerViewAdapter.OnChildViewClickListener {
    @BindView(R.id.messageRecyclerView)
    PullRecyclerView recyclerView;
    @BindView(R.id.messagePullLayout)
    PullLayout pullLayout;
    @BindView(R.id.messageNull)
    ImageView nullData;

    private ApiHandler apiHandler = new ApiHandler(this);
    private ApiClientRequest apiClientRequest;

    private int p = 1;
    private int limit = 10;

    private ArrayList<MessageModel> list;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_message, null);
        ButterKnife.bind(this, inflate);
        setContentView(inflate);
        setTitle("消息中心");
        list = new ArrayList<>();
        apiClientRequest = new ApiClientRequest(this, ROUTE.MESSAGE_LIST, apiHandler);
        apiClientRequest.setDebug(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageAdapter(this, list, this);
        recyclerView.setAdapter(adapter);

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

    @Override
    public void onViewClick(int position, int id) {

    }

    private static class ApiHandler extends ApiClientResponseWithPullHandler<MessageActivity> {
        public ApiHandler(MessageActivity the) {
            super(the);
        }

        @Override
        public boolean onSubmitBefore(MessageActivity the, ApiClientRequest request) {
            if (request.isRoute(ROUTE.MESSAGE_LIST)) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("store_id", 2);
                map.put("is_read", 0);
                map.put("limit", the.limit);
                map.put("p", the.p);
                request.setPost(map);
            }
            return super.onSubmitBefore(the, request);
        }

        @Override
        public int onSuccess(MessageActivity the, ApiClientRequest request, ApiClientResponse response, @Nullable PullLayout pullLayout) {
            if (request.isRoute(ROUTE.MESSAGE_LIST)) {
                MessageListModel data = response.getData(MessageListModel.class);
                if (data.getStatus() == 1) {
                    if (the.p == OK) {
                        the.list.clear();
                    }
                    ArrayList<MessageModel> list = data.getData().get_list();
                    the.list.addAll(list);
                    the.adapter.notifyDataSetChanged();
                    if (the.list.size() > 0) {
                        the.pullLayout.setVisibility(View.VISIBLE);
                        the.nullData.setVisibility(View.GONE);
                    } else {
                        the.pullLayout.setVisibility(View.GONE);
                        the.nullData.setVisibility(View.VISIBLE);
                    }
                    the.p++;
                    if (the.limit > list.size()) {
                        return ALL;
                    } else {
                        return SUCCEED;
                    }
                } else {
                    return FAIL;
                }
            }
            return IGNORE;
        }
    }
}
