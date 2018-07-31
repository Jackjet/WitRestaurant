package cn.lsmya.restaurant.orderData;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import cn.lsmya.library.PullLayout.PullScrollView;
import cn.lsmya.library.base.BaseTitleActivity;
import cn.lsmya.library.util.TimeUtils;
import cn.lsmya.restaurant.R;
import cn.lsmya.restaurant.adapter.FoodsAdapter;
import cn.lsmya.restaurant.app.ROUTE;
import cn.lsmya.restaurant.model.EatOrderModel;
import cn.lsmya.restaurant.model.GoodsModel;
import cn.lsmya.restaurant.model.OrderDataModel;
import cn.lsmya.restaurant.util.PrintQr;
import cn.lsmya.restaurant.util.ToastUtil;
import cn.lsmya.restaurant.view.ListViewNoSlide;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class EatActivity extends BaseTitleActivity implements ApiClientResponseWithPullHandler.GetPullLayout {

    @BindView(R.id.eat_scrollView)
    PullScrollView scrollView;
    @BindView(R.id.eat_pullLayout)
    PullLayout pullLayout;
    @BindView(R.id.eat_orderNumber)
    TextView number;
    @BindView(R.id.eat_orderFoods)
    ListViewNoSlide foodsList;
    @BindView(R.id.eat_orderMoney)
    TextView money;
    @BindView(R.id.eat_orderOnSale)
    TextView onSale;
    @BindView(R.id.eat_orderPayMoney)
    TextView payMoney;
    @BindView(R.id.eat_orderAddressNum)
    TextView addressNum;
    @BindView(R.id.eat_orderPayData)
    TextView payData;

    private ApiHandler apiHandler = new ApiHandler(this);
    private ApiClientRequest apiClientRequest;
    private String storeId;
    private String orderNo;
    private FoodsAdapter adapter;
    private List<GoodsModel> list;
    private OrderDataModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_eat, null);
        ButterKnife.bind(this, inflate);
        setContentView(inflate);
        storeId = getIntent().getStringExtra("storeId");
        orderNo = getIntent().getStringExtra("orderNo");
        apiClientRequest = new ApiClientRequest(this, ROUTE.ORDER_DATA, apiHandler);
        setTitle("订单详情");
        tvRight.setText("打印");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(listener);
        pullLayout.setCanLoadMore(false);
        list = new ArrayList<>();
        pullLayout.addOnPullListener(new PullLayout.OnPullListener() {
            @Override
            public void onRefresh(PullLayout pullToRefreshLayout, boolean firstRefresh) {
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
        adapter = new FoodsAdapter(this, list);
        foodsList.setAdapter(adapter);
        pullLayout.refresh(true);
        init();
    }

    @Nullable
    @Override
    public PullLayout getPullLayout() {
        return pullLayout;
    }

    private static class ApiHandler extends ApiClientResponseWithPullHandler<EatActivity> {

        public ApiHandler(EatActivity the) {
            super(the);
        }

        @Override
        public boolean onSubmitBefore(EatActivity the, ApiClientRequest request) {
            if (request.isRoute(ROUTE.ORDER_DATA)) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("store_id", the.storeId);
                map.put("order_no", the.orderNo);
                request.setPost(map);
            }
            return super.onSubmitBefore(the, request);
        }

        @Override
        public int onSuccess(EatActivity the, ApiClientRequest request, ApiClientResponse response, @Nullable PullLayout pullLayout) {
            if (request.isRoute(ROUTE.ORDER_DATA)) {
                EatOrderModel data = response.getData(EatOrderModel.class);
                if (data.getStatus() == OK) {
                    the.model = data.getData();
                    the.number.setText(the.model.getOrder_no());
                    the.money.setText("¥" + the.model.getOrder_price());
                    the.onSale.setText("¥" + the.model.getOrder_subsidy_total());
                    the.payMoney.setText(the.model.getOrder_total());
                    the.addressNum.setText(the.model.getSeat_desk() + " " + the.model.getSeat_number());
                    the.payData.setText(TimeUtils.todayYyyyMmDdHhMmSs(Long.valueOf(the.model.getCreate_time()) * 1000));
                    ArrayList<GoodsModel> order_goods_log = the.model.getOrder_goods_log();
                    the.list.clear();
                    the.list.addAll(order_goods_log);
                    the.adapter.notifyDataSetChanged();
                    return SUCCEED;
                } else {
                    ToastUtil.showTextToast(the, "获取订单信息失败！");
                    the.finish();
                    return FAIL;
                }
            }
            return IGNORE;
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            print(model);
        }
    };

    private IWoyouService woyouService;

    public void init() {
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        startService(intent);//启动打印服务
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    public void print(OrderDataModel dataModel) {
        try {
            woyouService.lineWrap(2, callback);//打印条头部
            woyouService.lineWrap(2, callback);//打印条头部
            woyouService.setAlignment(1, callback);//设置居中
            woyouService.sendRAWData(boldOn(), callback); // 添加字体加粗指令
            woyouService.printTextWithFont("堂吃订单\n", "gh", 35f, callback);
            woyouService.printerInit(callback);
            woyouService.setAlignment(1, callback);//设置居中
            woyouService.sendRAWData(boldOn(), callback); // 添加字体加粗指令
            woyouService.printText("订单号：" + dataModel.getOrder_no() + "\n", callback);
            woyouService.printerInit(callback);
            woyouService.lineWrap(1, callback);
            woyouService.sendRAWData(boldOn(), callback); // 添加字体加粗指令
            String mmDdHhMm = TimeUtils.todayMmDdHhMm(Long.parseLong(model.getCreate_time()) * 1000);
            woyouService.printText(mmDdHhMm + "   " + dataModel.getBranch_name() + "\n", callback);
            woyouService.printText("备注：\n", callback);
            woyouService.printText("--------------------------------\n", callback);
            woyouService.printColumnsString(new String[]{"名称", "数量", "单价"}, new int[]{4, 4, 4}, new int[]{1, 1, 1}, callback);
            ArrayList<GoodsModel> order_goods_log = dataModel.getOrder_goods_log();
            if (order_goods_log != null) {
                for (GoodsModel goodsModel : order_goods_log) {
                    String name = goodsModel.getGoods_title();
                    String num = goodsModel.getBuy_num();
                    String price = goodsModel.getGoods_price();
                    woyouService.printColumnsString(
                            new String[]{name, num, "¥" + price},
                            new int[]{6, 1, 6},
                            new int[]{0, 1, 2}, callback);
                }
            }
            woyouService.printColumnsString(
                    new String[]{"优惠金额", "1", "¥" + dataModel.getOrder_subsidy_total()},
                    new int[]{6, 1, 6},
                    new int[]{0, 1, 2}, callback);
            woyouService.printColumnsString(
                    new String[]{"运费", "1", "¥" + dataModel.getOrder_ship_price()},
                    new int[]{6, 1, 6},
                    new int[]{0, 1, 2}, callback);
            woyouService.printerInit(callback);
            woyouService.setAlignment(2, callback);//设置居中
            woyouService.sendRAWData(boldOn(), callback); // 添加字体加粗指令
            woyouService.printTextWithFont("总计：" + dataModel.getOrder_total() + "\n", "gh", 28f, callback);
            woyouService.printerInit(callback);
            woyouService.sendRAWData(boldOn(), callback); // 添加字体加粗指令
            woyouService.printText("--------------------------------\n", callback);
            woyouService.printText("桌号：" + model.getSeat_desk() + "\n", callback);
            woyouService.lineWrap(1, callback);

            woyouService.printerInit(callback);
            woyouService.setAlignment(1, callback);//设置居中
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.text);
            byte[] printQRCode = PrintQr.getPrintQRCode("www.baidu.com", 4, 2);
            woyouService.enterPrinterBuffer(true);
            woyouService.printBitmap(bmp, callback);
            woyouService.sendRAWData(printQRCode, callback);
            woyouService.commitPrinterBuffer();
            woyouService.exitPrinterBuffer(true);
            woyouService.lineWrap(5, callback);//打印条尾部

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    ICallback callback = new ICallback.Stub() {

        @Override
        public void onRunResult(boolean success) throws RemoteException {
        }

        @Override
        public void onReturnString(final String value) throws RemoteException {
        }

        @Override
        public void onRaiseException(int code, final String msg)
                throws RemoteException {
        }
    };

    /**
     * 字体加粗
     */
    public static byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = 0x1B;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }
}
