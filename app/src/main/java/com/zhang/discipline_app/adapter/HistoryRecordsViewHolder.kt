package com.zhang.discipline_app.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.zhang.discipline_app.R

/**
 *@ClassName HistoryRecordsViewHolder
 *@Description TODO
 *@Author zhang
 *@Date 2021/3/12 15:58
 *@Version 1.0
 **/
class HistoryRecordsViewHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view) {
    init {
        val mTimelineView: TimelineView = view.findViewById(R.id.timeline);
        mTimelineView.initLine(viewType)
    }
    val to_do_start_time: TextView = view.findViewById(R.id.to_do_start_time)
    val to_do_end_time: TextView = view.findViewById(R.id.to_do_end_time)
    val todo_title: TextView = view.findViewById(R.id.to_do_title)
    val to_do_desc: TextView = view.findViewById(R.id.to_do_desc)
    val to_do_duration: TextView = view.findViewById(R.id.to_do_duration)
}