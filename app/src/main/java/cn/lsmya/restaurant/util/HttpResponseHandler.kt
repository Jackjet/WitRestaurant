package cn.lsmya.kotlin.apiclient.network

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

public open abstract class HttpResponseHandler<T> : Handler {

    private var mThe: WeakReference<T>? = null

    constructor(t: T) {
        mThe = WeakReference(t)
    }

    companion object {
        //请求前
        const val BEFORE = -1000
        //请求成功
        const val SUCCESS = -1001
        //请求失败
        const val FAIL = -1002
        //请求完成
        const val FINISH = -1003
        //请求取消
        const val CANCEL = -1004
        //请求进度条
        const val PROGRESS = -1005
        //上传完成
        const val DONE = -1006
    }

    private fun getThe(): T? {
        return if (mThe == null) {
            null
        } else mThe!!.get() ?: return null
    }

    override fun handleMessage(msg: Message?) {
        super.handleMessage(msg)
        var the = getThe()
        val obj = msg?.obj as MethodMessage
        if (msg?.what == -1010101) {
            when (obj.method) {
                BEFORE -> {
                    onBefore(the!!, obj.request)
                }
                SUCCESS -> {
                    onSuccess(the!!, obj.request, obj.response!!)
                }
                FAIL -> {
                    onFail(the!!, obj.request)
                }
                FINISH -> {
                    onFinish(the!!, obj.request)
                }
                CANCEL -> {
                    onCancel(the!!, obj.request)
                }
                PROGRESS -> {
                    onProgress(the!!, obj.request, obj.progress)
                }
                DONE -> {
                    uploadDone(the!!, obj.request)
                }
            }
        }
    }

    fun sendMessage(method: Int, request: HttpRequest, response: HttpResponse) {
        val msg = Message()
        msg.what = -1010101
        msg.obj = MethodMessage(method, request, response)
        sendMessage(msg)
    }


    fun sendMessage(method: Int, request: HttpRequest) {
        val msg = Message()
        msg.what = -1010101
        msg.obj = MethodMessage(method, request)
        sendMessage(msg)
    }

    fun sendMessage(method: Int, request: HttpRequest, progress: Int) {
        val msg = Message()
        msg.what = -1010101
        msg.obj = MethodMessage(method, request, progress)
        sendMessage(msg)
    }


    class MethodMessage {
        var method: Int = 0
        var response: HttpResponse? = null
        var request: HttpRequest
        var progress = 0


        constructor(method: Int, request: HttpRequest, response: HttpResponse, progress: Int) {
            this.method = method
            this.response = response
            this.request = request
            this.progress = progress
        }

        constructor(method: Int, request: HttpRequest, response: HttpResponse) {
            this.method = method
            this.response = response
            this.request = request
        }

        constructor(method: Int, request: HttpRequest, progress: Int) {
            this.method = method
            this.request = request
            this.progress = progress
        }

        constructor(method: Int, request: HttpRequest) {
            this.method = method
            this.request = request
        }

    }

    //网络请求前回调这里
    open fun onBefore(t: T, request: HttpRequest) {}

    //网络请求成功后请求这里
    abstract fun onSuccess(t: T, request: HttpRequest, response: HttpResponse)

    // 网络请求被取消
    open fun onCancel(the: T, request: HttpRequest) {}

    // 网络请求进度条改变
    open fun onProgress(the: T, request: HttpRequest, progress: Int) {}

    // 网络请求完毕
    open fun onFinish(the: T, request: HttpRequest) {}


    //网络错误、代码异常时走这里
    open fun onFail(the: T, request: HttpRequest) {}

    //上传文件完成
    open fun uploadDone(the: T, request: HttpRequest) {}

}