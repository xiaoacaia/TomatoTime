package com.zhang.discipline_app.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.zhang.discipline_app.R

/**
 *@ClassName ActivityUtils
 *@Description TODO
 *@Author zhang
 *@Date 2021/3/12 14:51
 *@Version 1.0
 **/
object ActivityUtils {
    // 更改视图代码
    public fun replaceFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        // 待传入的容器，待添加的 Fragment 实例
        transaction.replace(R.id.main_window, fragment) // 后面的替代到 main_window 中
        transaction.commit()
    }
}