package com.zhang.discipline_app.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zhang.discipline_app.db.DBManager
import com.zhang.discipline_app.PrepareToDo
import com.zhang.discipline_app.R
import com.zhang.discipline_app.main.CountDownActivity
import com.zhang.discipline_app.main.fragment.TomatoFragment

class ToDoListAdapter(val context: Context, val prepareData: List<PrepareToDo>) :
    RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val prepareToDoImageId: ImageView = view.findViewById(R.id.prepareToDoImageId)
        val prepareTitle: TextView = view.findViewById(R.id.prepareTitle)
        val PrepareNeedTime: TextView = view.findViewById(R.id.PrepareNeedTime)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // 传入单个布局
        val view = LayoutInflater.from(context).inflate(R.layout.item_to_do_list, parent, false)
        val holder = ViewHolder(view)

        val prepare_start: Button = view.findViewById<Button>(R.id.prepare_start)

        // 点击开始按钮
        prepare_start.setOnClickListener {
            val position = holder.adapterPosition
            // 点击的单个项目
            val clickdata = prepareData[position]
            val intent = Intent(context, CountDownActivity::class.java)
            // 传入需要定时的时间
            intent.putExtra("prepare_need_time", clickdata.prepareNeedTime)
            intent.putExtra("prepare_title", clickdata.prepareTitle)
            context.startActivity(intent)
        }


        // 长点击整体事件
        holder.itemView.setOnLongClickListener(
            View.OnLongClickListener() {
                // 弹出删除对话框
                val builder = AlertDialog.Builder(this.context)
                builder.setMessage("确定要删除该条待办事项吗？")
                    .setNegativeButton("确定",
                        DialogInterface.OnClickListener { _, _ ->
                            // 根据删除数据库中的数据
                            DBManager.deletePrepareById(DBManager.selectAllPrepareData()[holder.adapterPosition].id)
                            // 删除待办数据源中的数据
                            DBManager.PrepareToDoData.removeAt(holder.adapterPosition)
                            // 更新视图
                            TomatoFragment.toDoList_adapter.notifyDataSetChanged()
                        })
                    .setPositiveButton("取消", null)
                // Create the AlertDialog object and return it
                builder.create().show()
                true
            }
        )
        return holder
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val singleData = prepareData[position]
        // 子数据赋值
        holder.prepareTitle.text = singleData.prepareTitle
        holder.PrepareNeedTime.text = "${singleData.prepareNeedTime} 分钟"
        Glide.with(context).load(singleData.imageId).into(holder.prepareToDoImageId)
    }
    override fun getItemCount(): Int = prepareData.size
}

