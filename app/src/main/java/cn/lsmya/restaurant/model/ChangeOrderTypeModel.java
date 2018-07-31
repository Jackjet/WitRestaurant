package cn.lsmya.restaurant.model;

public class ChangeOrderTypeModel {
    private int status;
    private String info;

    public ChangeOrderTypeModel() {
    }

    public ChangeOrderTypeModel(int status, String info) {
        this.status = status;
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ChangeOrderTypeModel{" +
                "status=" + status +
                ", info='" + info + '\'' +
                '}';
    }
}
