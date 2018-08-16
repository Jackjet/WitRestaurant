package cn.lsmya.restaurant.model;

public class UserDataModel {
    private int status;
    private DataModel data;
    private String info;

    public UserDataModel() {
    }

    public UserDataModel(int status, DataModel data, String info) {
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

    public class DataModel {

        private String store_id;
        private String store_domain;
        private String password;
        private String mobile;
        private String email;
        private String store_name;
        private String store_keywords;
        private String store_manager;
        private String store_see_num;
        private String store_description;
        private String store_address;
        private String store_map;
        private String store_tel;
        private String store_close_info;
        private String store_sort;
        private String store_end_time;
        private String store_logo;
        private String store_wechat_qrcode;
        private String store_show;
        private String store_qq;
        private String store_ww;
        private String store_rang;
        private String store_ship_price;
        private String store_start_ship_price;
        private String store_score_scale;
        private String store_site_number;
        private String store_recommend;
        private String store_credit;
        private String praise_rate;
        private String store_desccredit;
        private String store_servicecredit;
        private String store_deliverycredit;
        private String store_collect;
        private String store_sales;
        private String store_kefu;
        private String store_workingtime;
        private String store_h5_img;
        private String store_h5_music;
        private String market_price_scale;
        private String goods_num_head;
        private String last_login_ip;
        private String login;
        private String last_login_time;
        private String status;
        private String create_time;
        private String logo_path;
        private String ewm_path;

        public DataModel() {
        }

        public DataModel(String store_id, String store_domain, String password, String mobile, String email, String store_name, String store_keywords, String store_manager, String store_see_num, String store_description, String store_address, String store_map, String store_tel, String store_close_info, String store_sort, String store_end_time, String store_logo, String store_wechat_qrcode, String store_show, String store_qq, String store_ww, String store_rang, String store_ship_price, String store_start_ship_price, String store_score_scale, String store_site_number, String store_recommend, String store_credit, String praise_rate, String store_desccredit, String store_servicecredit, String store_deliverycredit, String store_collect, String store_sales, String store_kefu, String store_workingtime, String store_h5_img, String store_h5_music, String market_price_scale, String goods_num_head, String last_login_ip, String login, String last_login_time, String status, String create_time, String logo_path, String ewm_path) {
            this.store_id = store_id;
            this.store_domain = store_domain;
            this.password = password;
            this.mobile = mobile;
            this.email = email;
            this.store_name = store_name;
            this.store_keywords = store_keywords;
            this.store_manager = store_manager;
            this.store_see_num = store_see_num;
            this.store_description = store_description;
            this.store_address = store_address;
            this.store_map = store_map;
            this.store_tel = store_tel;
            this.store_close_info = store_close_info;
            this.store_sort = store_sort;
            this.store_end_time = store_end_time;
            this.store_logo = store_logo;
            this.store_wechat_qrcode = store_wechat_qrcode;
            this.store_show = store_show;
            this.store_qq = store_qq;
            this.store_ww = store_ww;
            this.store_rang = store_rang;
            this.store_ship_price = store_ship_price;
            this.store_start_ship_price = store_start_ship_price;
            this.store_score_scale = store_score_scale;
            this.store_site_number = store_site_number;
            this.store_recommend = store_recommend;
            this.store_credit = store_credit;
            this.praise_rate = praise_rate;
            this.store_desccredit = store_desccredit;
            this.store_servicecredit = store_servicecredit;
            this.store_deliverycredit = store_deliverycredit;
            this.store_collect = store_collect;
            this.store_sales = store_sales;
            this.store_kefu = store_kefu;
            this.store_workingtime = store_workingtime;
            this.store_h5_img = store_h5_img;
            this.store_h5_music = store_h5_music;
            this.market_price_scale = market_price_scale;
            this.goods_num_head = goods_num_head;
            this.last_login_ip = last_login_ip;
            this.login = login;
            this.last_login_time = last_login_time;
            this.status = status;
            this.create_time = create_time;
            this.logo_path = logo_path;
            this.ewm_path = ewm_path;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getStore_domain() {
            return store_domain;
        }

        public void setStore_domain(String store_domain) {
            this.store_domain = store_domain;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getStore_keywords() {
            return store_keywords;
        }

        public void setStore_keywords(String store_keywords) {
            this.store_keywords = store_keywords;
        }

        public String getStore_manager() {
            return store_manager;
        }

        public void setStore_manager(String store_manager) {
            this.store_manager = store_manager;
        }

        public String getStore_see_num() {
            return store_see_num;
        }

        public void setStore_see_num(String store_see_num) {
            this.store_see_num = store_see_num;
        }

        public String getStore_description() {
            return store_description;
        }

        public void setStore_description(String store_description) {
            this.store_description = store_description;
        }

        public String getStore_address() {
            return store_address;
        }

        public void setStore_address(String store_address) {
            this.store_address = store_address;
        }

        public String getStore_map() {
            return store_map;
        }

        public void setStore_map(String store_map) {
            this.store_map = store_map;
        }

        public String getStore_tel() {
            return store_tel;
        }

        public void setStore_tel(String store_tel) {
            this.store_tel = store_tel;
        }

        public String getStore_close_info() {
            return store_close_info;
        }

        public void setStore_close_info(String store_close_info) {
            this.store_close_info = store_close_info;
        }

        public String getStore_sort() {
            return store_sort;
        }

        public void setStore_sort(String store_sort) {
            this.store_sort = store_sort;
        }

        public String getStore_end_time() {
            return store_end_time;
        }

        public void setStore_end_time(String store_end_time) {
            this.store_end_time = store_end_time;
        }

        public String getStore_logo() {
            return store_logo;
        }

        public void setStore_logo(String store_logo) {
            this.store_logo = store_logo;
        }

        public String getStore_wechat_qrcode() {
            return store_wechat_qrcode;
        }

        public void setStore_wechat_qrcode(String store_wechat_qrcode) {
            this.store_wechat_qrcode = store_wechat_qrcode;
        }

        public String getStore_show() {
            return store_show;
        }

        public void setStore_show(String store_show) {
            this.store_show = store_show;
        }

        public String getStore_qq() {
            return store_qq;
        }

        public void setStore_qq(String store_qq) {
            this.store_qq = store_qq;
        }

        public String getStore_ww() {
            return store_ww;
        }

        public void setStore_ww(String store_ww) {
            this.store_ww = store_ww;
        }

        public String getStore_rang() {
            return store_rang;
        }

        public void setStore_rang(String store_rang) {
            this.store_rang = store_rang;
        }

        public String getStore_ship_price() {
            return store_ship_price;
        }

        public void setStore_ship_price(String store_ship_price) {
            this.store_ship_price = store_ship_price;
        }

        public String getStore_start_ship_price() {
            return store_start_ship_price;
        }

        public void setStore_start_ship_price(String store_start_ship_price) {
            this.store_start_ship_price = store_start_ship_price;
        }

        public String getStore_score_scale() {
            return store_score_scale;
        }

        public void setStore_score_scale(String store_score_scale) {
            this.store_score_scale = store_score_scale;
        }

        public String getStore_site_number() {
            return store_site_number;
        }

        public void setStore_site_number(String store_site_number) {
            this.store_site_number = store_site_number;
        }

        public String getStore_recommend() {
            return store_recommend;
        }

        public void setStore_recommend(String store_recommend) {
            this.store_recommend = store_recommend;
        }

        public String getStore_credit() {
            return store_credit;
        }

        public void setStore_credit(String store_credit) {
            this.store_credit = store_credit;
        }

        public String getPraise_rate() {
            return praise_rate;
        }

        public void setPraise_rate(String praise_rate) {
            this.praise_rate = praise_rate;
        }

        public String getStore_desccredit() {
            return store_desccredit;
        }

        public void setStore_desccredit(String store_desccredit) {
            this.store_desccredit = store_desccredit;
        }

        public String getStore_servicecredit() {
            return store_servicecredit;
        }

        public void setStore_servicecredit(String store_servicecredit) {
            this.store_servicecredit = store_servicecredit;
        }

        public String getStore_deliverycredit() {
            return store_deliverycredit;
        }

        public void setStore_deliverycredit(String store_deliverycredit) {
            this.store_deliverycredit = store_deliverycredit;
        }

        public String getStore_collect() {
            return store_collect;
        }

        public void setStore_collect(String store_collect) {
            this.store_collect = store_collect;
        }

        public String getStore_sales() {
            return store_sales;
        }

        public void setStore_sales(String store_sales) {
            this.store_sales = store_sales;
        }

        public String getStore_kefu() {
            return store_kefu;
        }

        public void setStore_kefu(String store_kefu) {
            this.store_kefu = store_kefu;
        }

        public String getStore_workingtime() {
            return store_workingtime;
        }

        public void setStore_workingtime(String store_workingtime) {
            this.store_workingtime = store_workingtime;
        }

        public String getStore_h5_img() {
            return store_h5_img;
        }

        public void setStore_h5_img(String store_h5_img) {
            this.store_h5_img = store_h5_img;
        }

        public String getStore_h5_music() {
            return store_h5_music;
        }

        public void setStore_h5_music(String store_h5_music) {
            this.store_h5_music = store_h5_music;
        }

        public String getMarket_price_scale() {
            return market_price_scale;
        }

        public void setMarket_price_scale(String market_price_scale) {
            this.market_price_scale = market_price_scale;
        }

        public String getGoods_num_head() {
            return goods_num_head;
        }

        public void setGoods_num_head(String goods_num_head) {
            this.goods_num_head = goods_num_head;
        }

        public String getLast_login_ip() {
            return last_login_ip;
        }

        public void setLast_login_ip(String last_login_ip) {
            this.last_login_ip = last_login_ip;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
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

        public String getLogo_path() {
            return logo_path;
        }

        public void setLogo_path(String logo_path) {
            this.logo_path = logo_path;
        }

        public String getEwm_path() {
            return ewm_path;
        }

        public void setEwm_path(String ewm_path) {
            this.ewm_path = ewm_path;
        }
    }
}
