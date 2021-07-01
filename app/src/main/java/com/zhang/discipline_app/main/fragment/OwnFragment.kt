package com.zhang.discipline_app.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.zhang.discipline_app.R

class OwnFragment: Fragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inflate = inflater.inflate(R.layout.own, container, false)


        val explain: String = "人的大脑有两种思考模式,一种是专注模式,另一种是发散模式\n " +
                "专注模式就像开了强光的手电筒，亮度高但是照亮的范围小，并且更加的耗电\n " +
                "而发散模式就像开了弱光的手电筒，虽然亮度低但是照亮的范围大\n " +
                "人的精力就像给手电筒提供能量的电池，一直开专注模式电量很容易耗尽\n " +
                "通过休息可以给你的电池充电\n " +
                "所以要想学习效率高，就像进行状态的切换，从专注模式切换到发散模式\n " +
                "因为发散模式作用范围广，所以也许会有突然的灵感冒出来\n " +
                "与其劳累的学习，效率还不高，不如去休息一下\n " +
                "这也是为什么有番茄时间的原因"

        inflate.findViewById<TextView>(R.id.explain).text=explain

        return inflate
    }
}