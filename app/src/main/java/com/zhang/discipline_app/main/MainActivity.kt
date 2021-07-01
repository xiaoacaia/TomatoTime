package com.zhang.discipline_app.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhang.discipline_app.main.fragment.HistoryRecordsFragment
import com.zhang.discipline_app.databinding.ActivityMainBinding
import com.zhang.discipline_app.main.fragment.OwnFragment
import com.zhang.discipline_app.main.fragment.TomatoFragment
import com.zhang.discipline_app.util.ActivityUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 创建绑定类的实例以供 Activity 使用
        binding = ActivityMainBinding.inflate(layoutInflater)
        // 获取对根视图的引用
        val view = binding.root
        setContentView(view)

        // 获取 FragmentManager
        val fragmentManager = supportFragmentManager

        ActivityUtils.replaceFragmentToActivity(fragmentManager, TomatoFragment())

        // 点击，更改视图
        binding.plan.setOnClickListener {
            ActivityUtils.replaceFragmentToActivity(fragmentManager, HistoryRecordsFragment())
        }
        binding.tomato.setOnClickListener {
            ActivityUtils.replaceFragmentToActivity(fragmentManager, TomatoFragment())
        }
        binding.own.setOnClickListener {
            ActivityUtils.replaceFragmentToActivity(fragmentManager, OwnFragment())
        }



    }

}