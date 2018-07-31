package cn.lsmya.restaurant.model;

import java.util.ArrayList;

public class OrderDataModel {
    private String id;
    private String store_id;
    private String branch_id;
    private String order_no;
    private String uid;
    private String nickname;
    private String branch_name;
    private String order_type;
    private String order_buy_num;
    private AddressModel order_address_log;
    private SubsidyLogModel order_subsidy_log;
    private String order_score_pay_log;
    private ArrayList<GoodsModel> order_goods_log;
    private String order_goods_total;
    private String order_ship_price;
    private String order_subsidy_total;
    private String order_price;
    private String order_total;
    private String order_success_time;
    private String order_close_info;
    private String order_exhort;
    private String order_ship_reply;
    private String order_ship_time;
    private String order_ship_waiter_log;
    private String is_ping;
    private String remark;
    private String status;
    private String create_time;
    private String order_refund_status;
    private String order_refund_way;
    private String fail_info;
    private String transaction_no;
    private String seat_number;
    private String seat_desk;
    private String eating_num;
    private CapitalpoolModel Capitalpool;
    private boolean OrderGoods;

    public OrderDataModel() {
    }

    public OrderDataModel(String id, String store_id, String branch_id, String order_no, String uid, String nickname, String branch_name, String order_type, String order_buy_num, AddressModel order_address_log, SubsidyLogModel order_subsidy_log, String order_score_pay_log, ArrayList<GoodsModel> order_goods_log, String order_goods_total, String order_ship_price, String order_subsidy_total, String order_price, String order_total, String order_success_time, String order_close_info, String order_exhort, String order_ship_reply, String order_ship_time, String order_ship_waiter_log, String is_ping, String remark, String status, String create_time, String order_refund_status, String order_refund_way, String fail_info, String transaction_no, String seat_number, String seat_desk, String eating_num, CapitalpoolModel capitalpool, boolean orderGoods) {
        this.id = id;
        this.store_id = store_id;
        this.branch_id = branch_id;
        this.order_no = order_no;
        this.uid = uid;
        this.nickname = nickname;
        this.branch_name = branch_name;
        this.order_type = order_type;
        this.order_buy_num = order_buy_num;
        this.order_address_log = order_address_log;
        this.order_subsidy_log = order_subsidy_log;
        this.order_score_pay_log = order_score_pay_log;
        this.order_goods_log = order_goods_log;
        this.order_goods_total = order_goods_total;
        this.order_ship_price = order_ship_price;
        this.order_subsidy_total = order_subsidy_total;
        this.order_price = order_price;
        this.order_total = order_total;
        this.order_success_time = order_success_time;
        this.order_close_info = order_close_info;
        this.order_exhort = order_exhort;
        this.order_ship_reply = order_ship_reply;
        this.order_ship_time = order_ship_time;
        this.order_ship_waiter_log = order_ship_waiter_log;
        this.is_ping = is_ping;
        this.remark = remark;
        this.status = status;
        this.create_time = create_time;
        this.order_refund_status = order_refund_status;
        this.order_refund_way = order_refund_way;
        this.fail_info = fail_info;
        this.transaction_no = transaction_no;
        this.seat_number = seat_number;
        this.seat_desk = seat_desk;
        this.eating_num = eating_num;
        this.Capitalpool = capitalpool;
        this.OrderGoods = orderGoods;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_buy_num() {
        return order_buy_num;
    }

    public void setOrder_buy_num(String order_buy_num) {
        this.order_buy_num = order_buy_num;
    }

    public AddressModel getOrder_address_log() {
        return order_address_log;
    }

    public void setOrder_address_log(AddressModel order_address_log) {
        this.order_address_log = order_address_log;
    }

    public SubsidyLogModel getOrder_subsidy_log() {
        return order_subsidy_log;
    }

    public void setOrder_subsidy_log(SubsidyLogModel order_subsidy_log) {
        this.order_subsidy_log = order_subsidy_log;
    }

    public String getOrder_score_pay_log() {
        return order_score_pay_log;
    }

    public void setOrder_score_pay_log(String order_score_pay_log) {
        this.order_score_pay_log = order_score_pay_log;
    }

    public ArrayList<GoodsModel> getOrder_goods_log() {
        return order_goods_log;
    }

    public void setOrder_goods_log(ArrayList<GoodsModel> order_goods_log) {
        this.order_goods_log = order_goods_log;
    }

    public String getOrder_goods_total() {
        return order_goods_total;
    }

    public void setOrder_goods_total(String order_goods_total) {
        this.order_goods_total = order_goods_total;
    }

    public String getOrder_ship_price() {
        return order_ship_price;
    }

    public void setOrder_ship_price(String order_ship_price) {
        this.order_ship_price = order_ship_price;
    }

    public String getOrder_subsidy_total() {
        return order_subsidy_total;
    }

    public void setOrder_subsidy_total(String order_subsidy_total) {
        this.order_subsidy_total = order_subsidy_total;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getOrder_success_time() {
        return order_success_time;
    }

    public void setOrder_success_time(String order_success_time) {
        this.order_success_time = order_success_time;
    }

    public String getOrder_close_info() {
        return order_close_info;
    }

    public void setOrder_close_info(String order_close_info) {
        this.order_close_info = order_close_info;
    }

    public String getOrder_exhort() {
        return order_exhort;
    }

    public void setOrder_exhort(String order_exhort) {
        this.order_exhort = order_exhort;
    }

    public String getOrder_ship_reply() {
        return order_ship_reply;
    }

    public void setOrder_ship_reply(String order_ship_reply) {
        this.order_ship_reply = order_ship_reply;
    }

    public String getOrder_ship_time() {
        return order_ship_time;
    }

    public void setOrder_ship_time(String order_ship_time) {
        this.order_ship_time = order_ship_time;
    }

    public String getOrder_ship_waiter_log() {
        return order_ship_waiter_log;
    }

    public void setOrder_ship_waiter_log(String order_ship_waiter_log) {
        this.order_ship_waiter_log = order_ship_waiter_log;
    }

    public String getIs_ping() {
        return is_ping;
    }

    public void setIs_ping(String is_ping) {
        this.is_ping = is_ping;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOrder_refund_status() {
        return order_refund_status;
    }

    public void setOrder_refund_status(String order_refund_status) {
        this.order_refund_status = order_refund_status;
    }

    public String getOrder_refund_way() {
        return order_refund_way;
    }

    public void setOrder_refund_way(String order_refund_way) {
        this.order_refund_way = order_refund_way;
    }

    public String getFail_info() {
        return fail_info;
    }

    public void setFail_info(String fail_info) {
        this.fail_info = fail_info;
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
    }

    public String getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(String seat_number) {
        this.seat_number = seat_number;
    }

    public String getSeat_desk() {
        return seat_desk;
    }

    public void setSeat_desk(String seat_desk) {
        this.seat_desk = seat_desk;
    }

    public String getEating_num() {
        return eating_num;
    }

    public void setEating_num(String eating_num) {
        this.eating_num = eating_num;
    }

    public CapitalpoolModel getCapitalpool() {
        return Capitalpool;
    }

    public void setCapitalpool(CapitalpoolModel capitalpool) {
        Capitalpool = capitalpool;
    }

    public boolean isOrderGoods() {
        return OrderGoods;
    }

    public void setOrderGoods(boolean orderGoods) {
        OrderGoods = orderGoods;
    }

    @Override
    public String toString() {
        return "OrderDataModel{" +
                "id='" + id + '\'' +
                ", store_id='" + store_id + '\'' +
                ", branch_id='" + branch_id + '\'' +
                ", order_no='" + order_no + '\'' +
                ", uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", branch_name='" + branch_name + '\'' +
                ", order_type='" + order_type + '\'' +
                ", order_buy_num='" + order_buy_num + '\'' +
                ", order_address_log=" + order_address_log +
                ", order_subsidy_log='" + order_subsidy_log + '\'' +
                ", order_score_pay_log='" + order_score_pay_log + '\'' +
                ", order_goods_log=" + order_goods_log +
                ", order_goods_total='" + order_goods_total + '\'' +
                ", order_ship_price='" + order_ship_price + '\'' +
                ", order_subsidy_total='" + order_subsidy_total + '\'' +
                ", order_price='" + order_price + '\'' +
                ", order_total='" + order_total + '\'' +
                ", order_success_time='" + order_success_time + '\'' +
                ", order_close_info='" + order_close_info + '\'' +
                ", order_exhort='" + order_exhort + '\'' +
                ", order_ship_reply='" + order_ship_reply + '\'' +
                ", order_ship_time='" + order_ship_time + '\'' +
                ", order_ship_waiter_log='" + order_ship_waiter_log + '\'' +
                ", is_ping='" + is_ping + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", create_time='" + create_time + '\'' +
                ", order_refund_status='" + order_refund_status + '\'' +
                ", order_refund_way='" + order_refund_way + '\'' +
                ", fail_info='" + fail_info + '\'' +
                ", transaction_no='" + transaction_no + '\'' +
                ", seat_number='" + seat_number + '\'' +
                ", seat_desk='" + seat_desk + '\'' +
                ", eating_num='" + eating_num + '\'' +
                ", Capitalpool=" + Capitalpool +
                ", OrderGoods=" + OrderGoods +
                '}';
    }
}
