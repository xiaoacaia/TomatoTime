package com.zhang.discipline_app.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.zhang.discipline_app.db.DBManager
import com.zhang.discipline_app.R
import com.zhang.discipline_app.ToDoDesc
import com.zhang.discipline_app.main.ToDoDescActivity
import com.zhang.discipline_app.main.ToDoDescActivity.Companion.historyRecords_adapter


class HistoryRecordsAdapter(val context: Context, val toDoDesc: List<ToDoDesc>) :
    RecyclerView.Adapter<HistoryRecordsViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryRecordsViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_historic_records,
            parent,
            false
        )
        val holder = HistoryRecordsViewHolder(view, viewType)


        holder.itemView.findViewById<CardView>(R.id.history_cardview).setOnClickListener {

            val inflator: View =
                LayoutInflater.from(this.context).inflate(R.layout.dialog_edit_desc, null)
            // 弹出编辑对话框
            AlertDialog.Builder(this.context).apply {
                setView(inflator)
                setPositiveButton("取消", null)
                setNegativeButton("确定") { _, _ ->
                    // 更新的数据
                    val newDate =
                        inflator.findViewById<EditText>(R.id.change_desc).text.toString()
                    // 根据选中的 id 更新数据
                    val id =
                        DBManager.selectToDoDescDataByDate(ToDoDescActivity.time)[holder.adapterPosition].id
                    DBManager.updateDescDateById(id, newDate)
                    // 更新数据源
                    DBManager.currentToDODescData[holder.adapterPosition].to_do_desc = newDate
                    // 提示适配器更新数据
                    historyRecords_adapter.notifyDataSetChanged()
                }

                show()
            }



        }

        // 点击进入编辑界面
        holder.itemView.setOnClickListener {
            val inflator: View =
                LayoutInflater.from(this.context).inflate(R.layout.dialog_edit_desc, null)
            // 弹出编辑对话框
            AlertDialog.Builder(this.context).apply {
                setView(inflator)
                setPositiveButton("取消", null)
                setNegativeButton("确定") { _, _ ->
                    // 更新的数据
                    val newDate =
                        inflator.findViewById<EditText>(R.id.change_desc).text.toString()
                    // 根据选中的 id 更新数据
                    val id =
                        DBManager.selectToDoDescDataByDate(ToDoDescActivity.time)[holder.adapterPosition].id
                    DBManager.updateDescDateById(id, newDate)
                    // 更新数据源
                    DBManager.currentToDODescData[holder.adapterPosition].to_do_desc = newDate
                    // 提示适配器更新数据
                    historyRecords_adapter.notifyDataSetChanged()
                }
                show()
            }
        }

        // 长按删除编辑界面
        holder.itemView.setOnLongClickListener {
                // 弹出删除对话框
                val builder = AlertDialog.Builder(this.context)
                builder.setMessage("确定要删除这条心得吗？")
                    .setNegativeButton("确定",
                        DialogInterface.OnClickListener { _, _ ->
                            // 根据 id 删除数据库中的数据
                            val id =
                                DBManager.selectToDoDescDataByDate(ToDoDescActivity.time)[holder.adapterPosition].id
                            DBManager.deleteDescById(id)
                            // 删除待办数据源中的数据
                            DBManager.currentToDODescData.removeAt(holder.adapterPosition)
                            // 更新视图
                            historyRecords_adapter.notifyDataSetChanged()
                        })
                    .setPositiveButton("取消", null)
                builder.create().show()
                true
        }



        return holder
    }


    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryRecordsViewHolder, position: Int) {
        val singleData = toDoDesc[position]

        // 子数据赋值
        val start_time = singleData.start_time.toString().split(" ")[1].substring(0, 5)
        val end_time = singleData.end_time.toString().split(" ")[1].substring(0, 5)

        holder.to_do_start_time.text = start_time
        holder.to_do_end_time.text = end_time
        holder.todo_title.text = singleData.to_do_title
        holder.to_do_desc.text = singleData.to_do_desc

        val duration_minute =
            (end_time.split(":")[0].toInt() - start_time.split(":")[0].toInt()) * 60
        val duration_second = (end_time.split(":")[1].toInt() - start_time.split(":")[1].toInt())

        holder.to_do_duration.text = "${(duration_minute + duration_second).toString()} 分钟"
    }

    override fun getItemCount(): Int = toDoDesc.size
}