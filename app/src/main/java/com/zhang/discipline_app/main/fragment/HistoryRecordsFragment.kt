package com.zhang.discipline_app.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zhang.discipline_app.db.DBManager
import com.zhang.discipline_app.main.ToDoDescActivity
import com.zhang.discipline_app.databinding.HistoricRecordsActivityBinding
import com.zhang.discipline_app.databinding.ItemHistoricRecordsBinding

class HistoryRecordsFragment : Fragment() {

    private var _binding: HistoricRecordsActivityBinding? = null
    private val binding get() = _binding!!

    private var _binding_item_historic: ItemHistoricRecordsBinding? = null
    private val binding_item_historic get() = _binding_item_historic!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.historic_records_activity, container, false)

        _binding = HistoricRecordsActivityBinding.inflate(inflater, container, false)
        _binding_item_historic = ItemHistoricRecordsBinding.inflate(inflater, container, false)

        val view = binding.root



        // 日历点击事件 ， 点击进入 ToDoDescActivity
        binding.calendarview.setOnDateChangeListener { _, year, month_minus1, dayOfMonth ->


            val month: Int = month_minus1 + 1
            val time: String = if (month < 10){
                if (dayOfMonth < 10){
                    "$year-0$month-0$dayOfMonth%"
                }else{
                    "$year-0$month-$dayOfMonth%"
                }
            }else{
                if (dayOfMonth < 10){
                    "$year-$month-0$dayOfMonth%"
                }else{
                    "$year-$month-$dayOfMonth%"
                }
            }

            // 如果有数据就跳转
            if (DBManager.getDescDataByDate(time).size > 0){
                val intent = Intent(this.context, ToDoDescActivity::class.java)
                intent.putExtra("time",time)
                startActivity(intent)
            }

        }





        // 从 onCreateView() 方法返回根视图，使其成为屏幕上的活动视图
        return view
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _binding_item_historic = null
    }


}