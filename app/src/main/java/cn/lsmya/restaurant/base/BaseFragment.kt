package cn.lsmya.restaurant.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

open abstract class BaseFragment : Fragment() {

    protected abstract val layoutId: Int

    private var visible = false// 是否可视
    private var resume = false// 是否恢复
    private var start = true//是否已经运行过第一次
    private var init = true//是否已初始化
    private var run = false//是否运行

    protected abstract fun onViewCreated(view: View)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(view)
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        visible = userVisibleHint
        if (visible && resume && !run) {
            if (start) {
                start = false
                onViewStart()
            }
            run = true
            onViewShow()
        } else if (!visible && run) {
            run = false
            onViewHide()
        }
    }

    override fun onPause() {
        super.onPause()
        resume = false
        if (run) {
            run = false
            onViewHide()
        }
    }

    override fun onResume() {
        super.onResume()
        resume = true
        if (visible) {
            if (!run) {
                if (start) {
                    start = false
                    onViewStart()
                }
                run = true
                onViewShow()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (init) {
            init = false
            onViewInit()
        }
    }

    // 初次加载Fragment时执行
    private fun onViewInit() {}

    // 初次加载Fragment并显示出来的时候运行
    private fun onViewStart() {}

    // Fragment被显示时运行
    private fun onViewShow() {}

    // Fragment被隐藏时运行
    private fun onViewHide() {}

}
