package cn.lsmya.restaurant.model;

import java.util.ArrayList;
import java.util.List;

public class OrderListModel {
    private int status;
    private String info;
    private DataModel data;

    public OrderListModel() {
    }

    public OrderListModel(int status, String info, DataModel data) {
        this.status = status;
        this.info = info;
        this.data = data;
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

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OrderListModel{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }

    public class DataModel {
        private String ZongJinE;
        private String amount;
        private String _total;
        private boolean _totalPages;
        private List<DataListModel> _list;

        public DataModel() {
        }

        public DataModel(String zongJinE, String amount, String _total, boolean _totalPages, List<DataListModel> _list) {
            ZongJinE = zongJinE;
            this.amount = amount;
            this._total = _total;
            this._totalPages = _totalPages;
            this._list = _list;
        }

        public String getZongJinE() {
            return ZongJinE;
        }

        public void setZongJinE(String zongJinE) {
            ZongJinE = zongJinE;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String get_total() {
            return _total;
        }

        public void set_total(String _total) {
            this._total = _total;
        }

        public boolean get_totalPages() {
            return _totalPages;
        }

        public void set_totalPages(boolean _totalPages) {
            this._totalPages = _totalPages;
        }

        public List<DataListModel> get_list() {
            return _list;
        }

        public void set_list(List<DataListModel> _list) {
            this._list = _list;
        }

        @Override
        public String toString() {
            return "DataModel{" +
                    "ZongJinE='" + ZongJinE + '\'' +
                    ", amount='" + amount + '\'' +
                    ", _total='" + _total + '\'' +
                    ", _totalPages='" + _totalPages + '\'' +
                    ", _list=" + _list +
                    '}';
        }
    }
}
