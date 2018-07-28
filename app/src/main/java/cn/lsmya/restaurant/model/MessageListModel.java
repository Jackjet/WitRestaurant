package cn.lsmya.restaurant.model;

import java.util.ArrayList;

public class MessageListModel {
    private int status;
    private DataModel data;
    private String info;

    public MessageListModel() {
    }

    public MessageListModel(int status, DataModel data, String info) {
        this.status = status;
        this.data = data;
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "MessageListModel{" +
                "status=" + status +
                ", data=" + data +
                ", info='" + info + '\'' +
                '}';
    }

    public class DataModel {
        private String _total;
        private int _totalPages;
        private ArrayList<MessageModel> _list;

        public DataModel() {
        }

        public DataModel(String _total, int _totalPages, ArrayList<MessageModel> _list) {
            this._total = _total;
            this._totalPages = _totalPages;
            this._list = _list;
        }

        public String get_total() {
            return _total;
        }

        public void set_total(String _total) {
            this._total = _total;
        }

        public int get_totalPages() {
            return _totalPages;
        }

        public void set_totalPages(int _totalPages) {
            this._totalPages = _totalPages;
        }

        public ArrayList<MessageModel> get_list() {
            return _list;
        }

        public void set_list(ArrayList<MessageModel> _list) {
            this._list = _list;
        }

        @Override
        public String toString() {
            return "DataModel{" +
                    "_total='" + _total + '\'' +
                    ", _totalPages=" + _totalPages +
                    ", _list=" + _list +
                    '}';
        }
    }


}
