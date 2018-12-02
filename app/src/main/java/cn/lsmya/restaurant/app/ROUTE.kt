package cn.lsmya.restaurant.app

class ROUTE {
    companion object {
        //订单列表
        const val ORDER_LIST = "http://lxp.ijiaque.com/api/apiorder/orderinfo"
        //消息中心
        const val MESSAGE_LIST = "http://lxp.ijiaque.com/api/apimessage/message"
        //订单详情
        const val ORDER_DATA = "http://lxp.ijiaque.com/api/apiorder/detailed"
//        const val ORDER_DATA = "http://lxp.ijiaque.com/api/shangjia/chaxun"
        //修改订单状态
        const val CHANGE_ORDER = "http://lxp.ijiaque.com/api/shangjia/upd"
        //登录
        const val LOGIN = "http://lxp.ijiaque.com/api/shangjia/index"
        //订单统计
        const val ORDER_MONEY = "http://lxp.ijiaque.com/api/apiorder/statorder"
        //商家信息
        const val USER_INFO = "http://lxp.ijiaque.com/api/shangjia/xinxi"
    }
}