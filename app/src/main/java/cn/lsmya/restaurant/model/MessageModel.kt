package cn.lsmya.restaurant.model

data class MessageModel(
        var id: String,
        var store_id: String,
        var uid: String,
        var from_uid: Any,
        var title: String,
        var content: String,
        var type: String,
        var is_read: String,
        var last_toast: Any,
        var url: String,
        var create_time: String
)