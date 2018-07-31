package cn.lsmya.restaurant.model;

import java.util.ArrayList;

public class ListModel {
    private int status;
    private ArrayList<OrderDataModel> data;
    private MapModel map;
    private String info;

    public ListModel() {
    }

    public ListModel(int status, ArrayList<OrderDataModel> data, MapModel map, String info) {
        this.status = status;
        this.data = data;
        this.map = map;
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<OrderDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<OrderDataModel> data) {
        this.data = data;
    }

    public MapModel getMap() {
        return map;
    }

    public void setMap(MapModel map) {
        this.map = map;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ListModel{" +
                "status=" + status +
                ", data=" + data +
                ", map=" + map +
                ", info='" + info + '\'' +
                '}';
    }

    public class MapModel{
        private String store_id;
        private String order_type;
        private String status;

        public MapModel() {
        }

        public MapModel(String store_id, String order_type, String status) {
            this.store_id = store_id;
            this.order_type = order_type;
            this.status = status;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "MapModel{" +
                    "store_id='" + store_id + '\'' +
                    ", order_type='" + order_type + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
