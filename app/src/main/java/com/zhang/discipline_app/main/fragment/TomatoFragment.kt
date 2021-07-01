package com.zhang.discipline_app.main.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhang.discipline_app.db.DBManager
import com.zhang.discipline_app.PrepareToDo
import com.zhang.discipline_app.R
import com.zhang.discipline_app.adapter.ToDoListAdapter
import com.zhang.discipline_app.databinding.ItemAddDialogPrepareToDoBinding
import com.zhang.discipline_app.databinding.TomatoBinding


class TomatoFragment : Fragment() {

    private var _binding: TomatoBinding? = null
    private val binding get() = _binding!!

    private var _binding_dialog_data: ItemAddDialogPrepareToDoBinding? = null
    private val binding_dialog_data get() = _binding_dialog_data!!

//    private var _binding_prepare: PrepareToDoItemBinding? = null
//    private val binding_prepare get() = _binding!!

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var toDoList_adapter: ToDoListAdapter
    }

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);

        // 初始化数据库
        this.context?.let { DBManager.initDB(it) }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 创建该绑定类的实例以供 Fragment 使用
        _binding = TomatoBinding.inflate(inflater, container, false)
        _binding_dialog_data = ItemAddDialogPrepareToDoBinding.inflate(inflater, container, false)
        // 获取对根视图的引用
        val view = binding.root

        // 定义 layoutManger
        val layoutManger = LinearLayoutManager(activity)
        // 设置待办列表为线性布局显示
        binding.toDoList.layoutManager = layoutManger
        // 给适配器传入数据
        toDoList_adapter = activity?.let { ToDoListAdapter(it, DBManager.PrepareToDoData) }!!
        /// 设置 recycleView 的适配器
        binding.toDoList.adapter = toDoList_adapter

        // 从 onCreateView() 方法返回根视图，使其成为屏幕上的活动视图
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _binding_dialog_data = null
    }


    // 创建 menu  ！！！ onActivityCreated 中的代码一定不能省略
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 在 Fragment 添加 ToolBar  改代码不可省略，否则 menu 显示不出
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
    }


    @SuppressLint("InflateParams")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // 点击添加待办按钮
            R.id.add -> {

                val inflator: View =
                    LayoutInflater.from(this.context).inflate(R.layout.item_add_dialog_prepare_to_do, null)

                // 弹出添加对话框
                AlertDialog.Builder(this.context).apply {
                    setView(inflator)
                    setPositiveButton("取消", null)
                    setNegativeButton("确定") { _, _ ->
                        val s1 =
                            inflator.findViewById<EditText>(R.id.prepareTitleDialog).text.toString()
                        val s2 =
                            inflator.findViewById<EditText>(R.id.prepareNeedTime).text.toString()


                        val addData: PrepareToDo
                        val randoms = (1..6).random()
                        when (randoms){
                            1 -> addData = PrepareToDo(s1, s2, R.drawable.pre1)
                            2 -> addData = PrepareToDo(s1, s2, R.drawable.pre2)
                            3 -> addData = PrepareToDo(s1, s2, R.drawable.pre3)
                            4 -> addData = PrepareToDo(s1, s2, R.drawable.pre4)
                            5 -> addData = PrepareToDo(s1, s2, R.drawable.pre5)
                            else -> addData = PrepareToDo(s1, s2, R.drawable.pre6)
                        }
                        // 将对话框中的数据插入到数据库
                        DBManager.insertPrepareData(addData)
                        // 将对话框中的数据添加到待办数据源
                        DBManager.PrepareToDoData.add(addData)
                        // 提示适配器更新数据
                        toDoList_adapter.notifyDataSetChanged()

                    }
                    show()
                }
            }
        }
        return true
    }


}