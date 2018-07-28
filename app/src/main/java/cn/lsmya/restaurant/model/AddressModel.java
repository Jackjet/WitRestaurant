package cn.lsmya.restaurant.model;

public class AddressModel {
    private String address;
    private String mobile;
    private String truename;
    private String province;
    private String city;
    private String district;
    private String map;
    private String aid;

    public AddressModel() {
    }

    public AddressModel(String address, String mobile, String truename, String province, String city, String district, String map, String aid) {
        this.address = address;
        this.mobile = mobile;
        this.truename = truename;
        this.province = province;
        this.city = city;
        this.district = district;
        this.map = map;
        this.aid = aid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    @Override
    public String toString() {
        return "AddressModel{" +
                "address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", truename='" + truename + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", map='" + map + '\'' +
                ", aid='" + aid + '\'' +
                '}';
    }
}
