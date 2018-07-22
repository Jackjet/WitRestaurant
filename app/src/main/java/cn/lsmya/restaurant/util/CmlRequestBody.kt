package cn.lsmya.kotlin.apiclient.network

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Sink
import okio.Okio

abstract class CmlRequestBody(requestBody: RequestBody) : RequestBody() {

    private var requestBody: RequestBody? = null
    private var bufferedSink: BufferedSink? = null

    override fun contentType(): MediaType? = requestBody?.contentType()

    override fun writeTo(sink: BufferedSink?) {
        bufferedSink = Okio.buffer(sink(sink!!))
        requestBody?.writeTo(bufferedSink)
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink?.flush()
    }

    private fun sink(sink: Sink): Sink = object : ForwardingSink(sink) {
        private var current: Long = 0
        private var total: Long = 0
        private var last = 0

        override fun write(source: Buffer?, byteCount: Long) {
            super.write(source, byteCount)
            if (total == 0L) {
                total = contentLength()
            }
            current += byteCount
            val now = (current * 100 / total).toInt()
            if (last < now) {
                loading(last.toLong(), 100, total == current)
                last = now
            }
        }
    }

    abstract fun loading(current: Long, total: Long, done: Boolean)
}