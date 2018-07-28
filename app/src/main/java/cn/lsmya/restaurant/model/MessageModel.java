package cn.lsmya.restaurant.model;

public class MessageModel {
    private String id;
    private String store_id;
    private String uid;
    private String from_uid;
    private String title;
    private String content;
    private String type;
    private String is_read;
    private String last_toast;
    private String url;
    private String create_time;

    public MessageModel() {
    }

    public MessageModel(String id, String store_id, String uid, String from_uid, String title, String content, String type, String is_read, String last_toast, String url, String create_time) {
        this.id = id;
        this.store_id = store_id;
        this.uid = uid;
        this.from_uid = from_uid;
        this.title = title;
        this.content = content;
        this.type = type;
        this.is_read = is_read;
        this.last_toast = last_toast;
        this.url = url;
        this.create_time = create_time;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getLast_toast() {
        return last_toast;
    }

    public void setLast_toast(String last_toast) {
        this.last_toast = last_toast;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "id='" + id + '\'' +
                ", store_id='" + store_id + '\'' +
                ", uid='" + uid + '\'' +
                ", from_uid='" + from_uid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", is_read='" + is_read + '\'' +
                ", last_toast='" + last_toast + '\'' +
                ", url='" + url + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
