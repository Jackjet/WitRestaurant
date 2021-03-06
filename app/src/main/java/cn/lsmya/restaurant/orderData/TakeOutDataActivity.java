package cn.lsmya.restaurant.orderData;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
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
import cn.lsmya.restaurant.app.App;
import cn.lsmya.restaurant.app.ROUTE;
import cn.lsmya.restaurant.model.AddressModel;
import cn.lsmya.restaurant.model.EatOrderModel;
import cn.lsmya.restaurant.model.GoodsModel;
import cn.lsmya.restaurant.model.OrderDataModel;
import cn.lsmya.restaurant.util.PrintQr;
import cn.lsmya.restaurant.util.ToastUtil;
import cn.lsmya.restaurant.view.ListViewNoSlide;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class TakeOutDataActivity extends BaseTitleActivity implements ApiClientResponseWithPullHandler.GetPullLayout {

    @BindView(R.id.takeOut_scrollView)
    PullScrollView scrollView;
    @BindView(R.id.takeOut_pullLayout)
    PullLayout pullLayout;
    @BindView(R.id.takeOut_orderNumber)
    TextView number;
    @BindView(R.id.takeOut_orderFoods)
    ListViewNoSlide foodsList;
    @BindView(R.id.takeOut_orderMoney)
    TextView money;
    @BindView(R.id.takeOut_orderOnSale)
    TextView onSale;
    @BindView(R.id.takeOut_orderPayMoney)
    TextView payMoney;
    @BindView(R.id.takeOut_orderPhone)
    TextView phone;
    @BindView(R.id.takeOut_orderPhoneClick)
    TextView phoneClick;
    @BindView(R.id.takeOut_orderName)
    TextView name;
    @BindView(R.id.takeOut_orderAddress)
    TextView address;
    @BindView(R.id.takeOut_orderPayData)
    TextView payData;
    @BindView(R.id.takeOut_orderMsg)
    TextView message;


    private ApiHandler apiHandler = new ApiHandler(this);
    private String storeId;
    private String orderNo;
    private FoodsAdapter adapter;
    private List<GoodsModel> list;
    private ApiClientRequest apiClientRequest;
    private OrderDataModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_take_out_data, null);
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
        apiClientRequest.setDebug(true);
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
        phoneClick.setOnClickListener(phoneListener);
        init();
    }

    @Nullable
    @Override
    public PullLayout getPullLayout() {
        return pullLayout;
    }

    private static class ApiHandler extends ApiClientResponseWithPullHandler<TakeOutDataActivity> {

        public ApiHandler(TakeOutDataActivity the) {
            super(the);
        }

        @Override
        public boolean onSubmitBefore(TakeOutDataActivity the, ApiClientRequest request) {
            if (request.isRoute(ROUTE.ORDER_DATA)) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("store_id", App.getStoreId());
                map.put("order_no", the.orderNo);
                request.setPost(map);
            }
            return super.onSubmitBefore(the, request);
        }

        @Override
        public int onSuccess(TakeOutDataActivity the, ApiClientRequest request, ApiClientResponse response, @Nullable PullLayout pullLayout) {
            if (request.isRoute(ROUTE.ORDER_DATA)) {
                EatOrderModel data = response.getData(EatOrderModel.class);
                if (data.getStatus() == OK) {
                    the.model = data.getData();
                    try {
                        the.number.setText(the.model.getOrder_no());
                        the.money.setText("¥" + the.model.getOrder_price());
                        the.onSale.setText("¥" + the.model.getOrder_subsidy_total());
                        the.payMoney.setText(the.model.getOrder_total());
                        the.phone.setText(the.model.getOrder_address_log().getMobile());
                        the.name.setText(the.model.getNickname() + " " + the.model.getOrder_address_log().getMobile());
                        the.address.setText(the.model.getOrder_address_log().getAddress());
                        the.message.setText(the.model.getOrder_exhort());
                        the.payData.setText(TimeUtils.todayYyyyMmDdHhMmSs(Long.valueOf(the.model.getCreate_time()) * 1000));
                        ArrayList<GoodsModel> order_goods_log = the.model.getOrder_goods_log();
                        the.list.clear();
                        the.list.addAll(order_goods_log);
                        the.adapter.notifyDataSetChanged();

                        the.downTopLogo(the.model.getDianpulogo());
                        the.downLogo(the.model.getLogo());
                        the.downErWeiMa(the.model.getErweima());
                    } catch (Exception e) {
                        Log.e("发生异常：：", e.getMessage());
                    }
                    return SUCCEED;
                } else {
                    return FAIL;
                }
            }
            return IGNORE;
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (topLogoBit != null && logoBit != null && erweimaBit != null) {
                print(model);
            }else {
                ToastUtil.showTextToast(TakeOutDataActivity.this,"店铺Logo或二维码加载失败，请重试！");
            }
        }
    };

    View.OnClickListener phoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String mobile = null;
            try {
                mobile = model.getOrder_address_log().getMobile();
            } catch (Exception e) {
                ToastUtil.showTextToast(TakeOutDataActivity.this, "该订单没有手机号");
            }
            try {
                if (!TextUtils.isEmpty(mobile)) {
                    diallPhone(mobile);
                } else {
                    ToastUtil.showTextToast(TakeOutDataActivity.this, "该订单没有手机号");
                }
            } catch (Exception e) {
                ToastUtil.showTextToast(TakeOutDataActivity.this, "该设备不支持拨打电话！");

            }
        }
    };

    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
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
            woyouService.lineWrap(4, callback);//打印条头部
            woyouService.setAlignment(1, callback);//设置居中
            woyouService.printBitmap(topLogoBit, callback);//顶部logo
            woyouService.lineWrap(1, callback);
            woyouService.sendRAWData(boldOn(), callback); // 添加字体加粗指令
            woyouService.printTextWithFont("外卖订单\n", "gh", 35f, callback);
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
            woyouService.printText("姓名：" + dataModel.getNickname() + "\n", callback);
            AddressModel order_address_log = dataModel.getOrder_address_log();
            if (order_address_log!= null){
            woyouService.printText("电话：" + order_address_log.getMobile() + "\n", callback);
            woyouService.printText("地址：" + order_address_log.getAddress() + "\n", callback);
            }
            woyouService.lineWrap(1, callback);

            woyouService.printerInit(callback);
            woyouService.setAlignment(1, callback);//设置居中
            woyouService.enterPrinterBuffer(true);
            woyouService.printBitmap(logoBit, callback);
            woyouService.printBitmap(erweimaBit, callback);
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

    private Bitmap topLogoBit;
    private Bitmap logoBit;
    private Bitmap erweimaBit;

    public void downTopLogo(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                topLogoBit = BitmapFactory.decodeStream(inputStream);
            }
        });
    }

    public void downLogo(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                logoBit = BitmapFactory.decodeStream(inputStream);
            }
        });
    }

    public void downErWeiMa(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                erweimaBit = BitmapFactory.decodeStream(inputStream);
            }
        });
    }

}

