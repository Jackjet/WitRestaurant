package cn.lsmya.kotlin.apiclient.network

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import cn.lsmya.kotlin.apiclient.network.HttpResponseHandler.Companion.BEFORE
import cn.lsmya.kotlin.apiclient.network.HttpResponseHandler.Companion.FAIL
import cn.lsmya.kotlin.apiclient.network.HttpResponseHandler.Companion.FINISH
import cn.lsmya.kotlin.apiclient.network.HttpResponseHandler.Companion.PROGRESS
import cn.lsmya.kotlin.apiclient.network.HttpResponseHandler.Companion.SUCCESS
import com.google.gson.Gson
import okhttp3.*
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference

class HttpRequest {

    private var type = 0//0:POST  1:GET  2:FILE   3:DOWN
    private lateinit var contextWeakReference: WeakReference<Context>
    private lateinit var url: String
    private var map: Map<String, Any>? = null
    private var headMap: Map<String, Any>? = null

    private var debug = false
    private lateinit var file: File
    private lateinit var fileKey: String
    private lateinit var handler: HttpResponseHandler<*>

    constructor(url: String, handler: HttpResponseHandler<*>) {
        this.url = url
        this.handler = handler
    }

    constructor(url: String, context: Context, handler: HttpResponseHandler<*>) {
        this.contextWeakReference = WeakReference(context)
        this.url = url
        this.handler = handler
    }

    fun getContext(): Context? = contextWeakReference.get()

    fun setPost(map: Map<String, Any>) {
        this.map = map
    }

    fun addHeader(headerMap: Map<String, Any>) {
        this.headMap = headerMap
    }

    fun setDebug(debug: Boolean) {
        this.debug = debug
    }

    fun getFile(): File {
        return file
    }

    fun isRoute(route: String) = url == route

    fun setFile(file: File) {
        this.file = file
    }


    private var callback = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            try {
                log("[请求错误] -- " + e.message)
                this@HttpRequest.handler.sendMessage(FAIL, this@HttpRequest)
            } catch (e: Exception) {
                log("[请求错误] -- " + e.message)
                this@HttpRequest.handler.sendMessage(FAIL, this@HttpRequest)
            } finally {
                this@HttpRequest.handler.sendMessage(FINISH, this@HttpRequest)
            }

        }

        override fun onResponse(call: Call, response: Response) {
            try {
                val resultString = response.body()!!.string()
                log("[请求状态] -- " + response.code())
                log("[请求结果] -- $resultString")
                when (response.code()) {
                    200 -> this@HttpRequest.handler.sendMessage(SUCCESS, this@HttpRequest, HttpResponse(url, resultString))
                    else -> this@HttpRequest.handler.sendMessage(FAIL, this@HttpRequest)
                }
            } catch (e: Exception) {
                log("[请求错误] -- 请求成功，服务器返回数据异常::" + e.message)
                this@HttpRequest.handler.sendMessage(FAIL, this@HttpRequest)
            } finally {
                this@HttpRequest.handler.sendMessage(FINISH, this@HttpRequest)
            }


        }
    }

    fun submit() {
        handler.sendMessage(BEFORE, this)
        log("[接口地址] -- $url")
        when (type) {
            0 -> postRequest()
            1 -> getRequest()
            2 -> fileRequest()
        }
    }

    /**
     * GET请求
     */
    fun get(): HttpRequest {
        type = 1
        return this
    }

    private fun getRequest() {
        if (map != null) {
            log("[GET_上传参数] -- " + Gson().toJson(map))
            var data = ""
            for ((key, value) in map!!) {
                data = if (TextUtils.isEmpty(data)) {
                    "?$key=$value"
                } else {
                    "$data&$key=$value"
                }
            }
            url += data
        } else {
            log("[GET_上传参数] -- 未上传参数")
        }
        val builder = Request.Builder()
                .url(url)
        if (headMap != null) {
            log("[GET_请求头部] -- " + Gson().toJson(headMap))
            for ((key, value) in headMap!!) {
                builder.addHeader(key, value.toString())
            }
        }
        val client = OkHttpClient()
                .newBuilder()
                .build()//创建OkHttpClient对象
        client.newCall(builder.build()).enqueue(callback)
    }

    /**
     * POST请求，默认POST请求
     */
    fun post(): HttpRequest {
        type = 0
        return this
    }

    private fun postRequest() {
        val formBody = FormBody.Builder()
        if (map != null) {
            log("[上传参数] -- " + Gson().toJson(map))
            for ((key, value) in map!!) {
                formBody.add(key, value.toString())
            }
        } else {
            log("[上传参数] -- 未上传参数")
        }
        val builder = Request.Builder()
                .url(url)
                .post(formBody.build())
        if (headMap != null) {
            log("[请求头部] -- " + Gson().toJson(headMap))
            for ((key, value) in headMap!!) {
                builder.addHeader(key, value.toString())
            }
        }
        val client = OkHttpClient()
                .newBuilder()
                .build()
        client.newCall(builder.build()).enqueue(callback)
    }

    /**
     * 上传文件
     */
    fun file(filekey: String): HttpRequest {
        type = 2
        this.fileKey = filekey
        return this
    }

    private fun fileRequest() {
        if (file.exists()) {
            val builder = MultipartBody.Builder()
            if (map != null) {
                log("[上传参数] -- " + Gson().toJson(map))
                for ((key, value) in map!!) {
                    builder.addFormDataPart(key, value.toString())
                }
            }
            val fileBody = RequestBody.create(MediaType.parse(getFileContentType(file)), file)
            val requestBody = builder
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(fileKey, file.name, fileBody)
                    .build()
            val isFirstDone = booleanArrayOf(true)
            val request = Request.Builder()
                    .url(url)
                    .post(object : CmlRequestBody(requestBody) {
                        override fun loading(current: Long, total: Long, done: Boolean) {
                            if (done) {
                                if (isFirstDone[0]) {
                                    isFirstDone[0] = false
                                }
                            } else {
                                val l = current / total * 100
                                handler.sendMessage(PROGRESS, this@HttpRequest, l.toInt())
                            }
                        }
                    })
            if (headMap != null) {
                log("[请求头部] -- " + Gson().toJson(headMap))
                for ((key, value) in headMap!!) {
                    request.addHeader(key, value.toString())
                }
            }
            val client = OkHttpClient()
                    .newBuilder()
                    .build()
            client.newCall(request.build()).enqueue(callback)
        }
    }

    /**
     * 下载文件
     */
    private fun down(): HttpRequest {
        type = 3
        return this
    }

    private fun downRequest() {
        val formBody = FormBody.Builder()
        if (map != null) {
            log("[上传参数] -- " + Gson().toJson(map))
            for ((key, value) in map!!) {
                formBody.add(key, value.toString())
            }
        }
        val builder = Request.Builder()
                .url(url)
                .post(formBody.build())
        if (headMap != null) {
            log("[请求头部] -- " + Gson().toJson(headMap))
            for ((key, value) in headMap!!) {
                builder.addHeader(key, value.toString())
            }
        }
        val client = OkHttpClient()
        client.newCall(builder.build()).enqueue(callback)
    }

    private fun log(tag: String) {
        if (debug) {
            Log.e("[ $url ]", tag)
        }
    }

    // 获取要上传文件的Content-Type
    private fun getFileContentType(file: File): String? {
        var contentType: String?
        try {
            contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(file.name))
        } catch (e: Exception) {
            contentType = ""
        }

        if (TextUtils.isEmpty(contentType)) {
            contentType = "application/octet-stream"
        }
        return contentType
    }


}