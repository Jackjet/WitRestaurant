package cn.lsmya.restaurant.app

class ROUTE {
    companion object {
        //订单列表
        const val ORDER_LIST = "http://f.wangdao.co/api/apiorder/orderinfo"
        //消息中心
        const val MESSAGE_LIST = "http://f.wangdao.co/api/apimessage/message"
        //订单详情
        const val ORDER_DATA = "http://f.wangdao.co/api/apiorder/detailed"
//        const val ORDER_DATA = "http://f.wangdao.co/api/shangjia/chaxun"
        //修改订单状态
        const val CHANGE_ORDER = "http://f.wangdao.co/api/shangjia/upd"
        //登录
        const val LOGIN = "http://f.wangdao.co/api/shangjia/index"
        //订单统计
        const val ORDER_MONEY = "http://f.wangdao.co/api/apiorder/statorder"
        //商家信息
        const val USER_INFO = "http://f.wangdao.co/api/shangjia/xinxi"
    }
}