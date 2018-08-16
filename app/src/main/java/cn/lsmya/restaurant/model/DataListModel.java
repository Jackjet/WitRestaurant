package cn.lsmya.restaurant.model;

public class DataListModel {
    private String id;
    private String order_no;
    private String store_id;
    private String uid;
    private String nickname;
    private String type;
    private String category;
    private String pay_way;
    private String price;
    private String cause;
    private String remark;
    private String transaction_no;
    private String pingxx_code;
    private String error_info;
    private String over_time;
    private String md5;
    private String status;
    private String create_time;

    public DataListModel() {
    }

    public DataListModel(String id, String order_no, String store_id, String uid, String nickname, String type, String category, String pay_way, String price, String cause, String remark, String transaction_no, String pingxx_code, String error_info, String over_time, String md5, String status, String create_time) {
        this.id = id;
        this.order_no = order_no;
        this.store_id = store_id;
        this.uid = uid;
        this.nickname = nickname;
        this.type = type;
        this.category = category;
        this.pay_way = pay_way;
        this.price = price;
        this.cause = cause;
        this.remark = remark;
        this.transaction_no = transaction_no;
        this.pingxx_code = pingxx_code;
        this.error_info = error_info;
        this.over_time = over_time;
        this.md5 = md5;
        this.status = status;
        this.create_time = create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
    }

    public String getPingxx_code() {
        return pingxx_code;
    }

    public void setPingxx_code(String pingxx_code) {
        this.pingxx_code = pingxx_code;
    }

    public String getError_info() {
        return error_info;
    }

    public void setError_info(String error_info) {
        this.error_info = error_info;
    }

    public String getOver_time() {
        return over_time;
    }

    public void setOver_time(String over_time) {
        this.over_time = over_time;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
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
}
