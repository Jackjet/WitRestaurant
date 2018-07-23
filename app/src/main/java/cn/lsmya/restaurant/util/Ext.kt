package cn.lsmya.restaurant.util

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.widget.Toast

class Ext {
    companion object {
        private var mToast: Toast? = null

        fun Activity.toast(tag: String, duration: Int = Toast.LENGTH_SHORT) {
            if (mToast == null) {
                mToast = Toast.makeText(this, tag, duration)
            } else {
                mToast?.setText(tag)
            }
            mToast?.show()
        }

        fun Context.toast(tag: String, duration: Int = Toast.LENGTH_SHORT) {
            if (mToast == null) {
                mToast = Toast.makeText(this, tag, duration)
            } else {
                mToast?.setText(tag)
            }
            mToast?.show()
        }

        fun android.support.v4.app.Fragment.toast(tag: String, duration: Int = Toast.LENGTH_SHORT) {
            if (mToast == null) {
                mToast = Toast.makeText(this.activity, tag, duration)
            } else {
                mToast?.setText(tag)
            }
            mToast?.show()
        }

        fun Fragment.toast(tag: String, duration: Int = Toast.LENGTH_SHORT) {
            if (mToast == null) {
                mToast = Toast.makeText(this.activity, tag, duration)
            } else {
                mToast?.setText(tag)
            }
            mToast?.show()
        }

    }
}