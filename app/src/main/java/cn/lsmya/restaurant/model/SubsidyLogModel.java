package cn.lsmya.restaurant.model;

import java.util.ArrayList;

public class SubsidyLogModel {
    private ArrayList<SubsidyListModel> subsidy_list;
    private String order_subsidy_price;
    private String order_subsidy_score;
    private String order_subsidy_score_price;
    private String order_subsidy_card_pirce;
    private String order_subsidy_total;
    private String order_ship_price;
    private String order_price;
    private String order_total;

    public SubsidyLogModel() {
    }

    public SubsidyLogModel(ArrayList<SubsidyListModel> subsidy_list, String order_subsidy_price, String order_subsidy_score, String order_subsidy_score_price, String order_subsidy_card_pirce, String order_subsidy_total, String order_ship_price, String order_price, String order_total) {
        this.subsidy_list = subsidy_list;
        this.order_subsidy_price = order_subsidy_price;
        this.order_subsidy_score = order_subsidy_score;
        this.order_subsidy_score_price = order_subsidy_score_price;
        this.order_subsidy_card_pirce = order_subsidy_card_pirce;
        this.order_subsidy_total = order_subsidy_total;
        this.order_ship_price = order_ship_price;
        this.order_price = order_price;
        this.order_total = order_total;
    }

    public ArrayList<SubsidyListModel> getSubsidy_list() {
        return subsidy_list;
    }

    public void setSubsidy_list(ArrayList<SubsidyListModel> subsidy_list) {
        this.subsidy_list = subsidy_list;
    }

    public String getOrder_subsidy_price() {
        return order_subsidy_price;
    }

    public void setOrder_subsidy_price(String order_subsidy_price) {
        this.order_subsidy_price = order_subsidy_price;
    }

    public String getOrder_subsidy_score() {
        return order_subsidy_score;
    }

    public void setOrder_subsidy_score(String order_subsidy_score) {
        this.order_subsidy_score = order_subsidy_score;
    }

    public String getOrder_subsidy_score_price() {
        return order_subsidy_score_price;
    }

    public void setOrder_subsidy_score_price(String order_subsidy_score_price) {
        this.order_subsidy_score_price = order_subsidy_score_price;
    }

    public String getOrder_subsidy_card_pirce() {
        return order_subsidy_card_pirce;
    }

    public void setOrder_subsidy_card_pirce(String order_subsidy_card_pirce) {
        this.order_subsidy_card_pirce = order_subsidy_card_pirce;
    }

    public String getOrder_subsidy_total() {
        return order_subsidy_total;
    }

    public void setOrder_subsidy_total(String order_subsidy_total) {
        this.order_subsidy_total = order_subsidy_total;
    }

    public String getOrder_ship_price() {
        return order_ship_price;
    }

    public void setOrder_ship_price(String order_ship_price) {
        this.order_ship_price = order_ship_price;
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

    public class SubsidyListModel {
        private String price;
        private String consume;
        private String title;
        private String type;

        public SubsidyListModel() {
        }

        public SubsidyListModel(String price, String consume, String title, String type) {
            this.price = price;
            this.consume = consume;
            this.title = title;
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getConsume() {
            return consume;
        }

        public void setConsume(String consume) {
            this.consume = consume;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
