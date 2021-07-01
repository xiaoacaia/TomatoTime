package com.zhang.discipline_app.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhang.discipline_app.adapter.HistoryRecordsAdapter
import com.zhang.discipline_app.databinding.ActivityToDoDescBinding
import com.zhang.discipline_app.db.DBManager

class ToDoDescActivity : AppCompatActivity() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var historyRecords_adapter: HistoryRecordsAdapter
        lateinit var time: String
    }

    private lateinit var binding_to_do_desc: ActivityToDoDescBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_to_do_desc = ActivityToDoDescBinding.inflate(layoutInflater)
        val view = binding_to_do_desc.root
        setContentView(view)

        time = intent.getStringExtra("time").toString()

        // 定义 layoutManger
        val layoutManger = LinearLayoutManager(this)

        // 设置布局的 layoutManger
        binding_to_do_desc.itemHistory.layoutManager = layoutManger

        DBManager.currentToDODescData = DBManager.getDescDataByDate(time)
        // 给适配器传入数据
        historyRecords_adapter = HistoryRecordsAdapter(this, DBManager.currentToDODescData)

        /// 设置 recycleView 布局的适配器
        binding_to_do_desc.itemHistory.adapter = historyRecords_adapter





    }
}