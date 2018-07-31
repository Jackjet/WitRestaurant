package cn.lsmya.restaurant.model;

public class EatOrderModel {
    private int status;
    private OrderDataModel data;

    public EatOrderModel() {
    }

    public EatOrderModel(int status, OrderDataModel data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OrderDataModel getData() {
        return data;
    }

    public void setData(OrderDataModel data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EatOrderModel{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}
