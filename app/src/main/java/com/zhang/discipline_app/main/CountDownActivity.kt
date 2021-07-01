package com.zhang.discipline_app.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zhang.discipline_app.R
import com.zhang.discipline_app.ToDoDesc
import com.zhang.discipline_app.service.PlayerMusicService.Companion.mediaPlayer
import com.zhang.discipline_app.databinding.TomatoActivityBinding
import com.zhang.discipline_app.db.DBHelper
import com.zhang.discipline_app.db.DBManager
import com.zhang.discipline_app.service.PlayerMusicService


class CountDownActivity : AppCompatActivity() {

    // 视图绑定
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: TomatoActivityBinding
        // 初始值 添加心得为0  表示未添加心得
        var add_desc_flag = 0
        lateinit var nowTime: String
    }

    lateinit var current_title: String



    lateinit var myBinder: PlayerMusicService.MyTimer

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            myBinder = service as PlayerMusicService.MyTimer
            myBinder.StartTimer()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }


    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("asd","create")
        binding = TomatoActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        nowTime = DBHelper.getDateTime().toString()

        // 计时器停止按钮
        binding.stopTomato.setOnClickListener {
            PlayerMusicService.myCountDownTimer?.cancel()
            PlayerMusicService.myCountDownTimer = null
        }


        // 计时器继续按钮
        binding.continueTomato.setOnClickListener {
            // onTick 结束后就不会为 leftTime 赋值了，所以 leftTime 要大于 1 秒才会继续创建 MyCountDownTimer
            // 如果剩余时间大于 1 秒并且计时器为空的情况下
            if (PlayerMusicService.leftTime > 1000 && PlayerMusicService.myCountDownTimer == null) {
                // 从剩余时间开始设定计时器
                PlayerMusicService.myCountDownTimer =
                    PlayerMusicService.MyCountDownTimer(PlayerMusicService.leftTime, 1000, this)
                PlayerMusicService.myCountDownTimer!!.start()
            }
        }

        // 重新计时
        binding.restartTomato.setOnClickListener {
            // 弹出确认对话框
            val builder = AlertDialog.Builder(this)
            builder.setMessage("你确定要重新开始吗")
                .setNegativeButton("确定",
                    DialogInterface.OnClickListener { _, _ ->
                        PlayerMusicService.myCountDownTimer?.cancel()
                        PlayerMusicService.myCountDownTimer = null
                        PlayerMusicService.myCountDownTimer =
                            PlayerMusicService.MyCountDownTimer(PlayerMusicService.all, 1000, this)
                        PlayerMusicService.myCountDownTimer!!.start()
                    })
                .setPositiveButton("取消", null)
            builder.create()
                .show()
        }

        // 点击弹出心得添加对话框
        binding.leaveTime.setOnClickListener {
            val inflator: View =
                LayoutInflater.from(this).inflate(R.layout.item_add_desc, null)
            // 弹出添加对话框
            AlertDialog.Builder(this).apply {
                setView(inflator)
                setPositiveButton("取消", null)
                setNegativeButton("确定") { _, _ ->
                    // 心得体会具体内容
                    val to_do_desc =
                        inflator.findViewById<EditText>(R.id.to_do_desc).text.toString()

                    val add_desc_dialog =
                        inflator.findViewById<TextView>(R.id.add_desc_dialog)
                    val addData =
                        nowTime.let { it1 ->
                            DBHelper.getDateTime()?.let { it2 ->
                                ToDoDesc(
                                    PlayerMusicService.current_title, to_do_desc, it1,
                                    it2
                                )
                            }
                        }

                    // 如果 add_desc_flag 为1 表示之前已经插入过数据了，实行更新操作
                    if (add_desc_flag == 1) {
                        // 更改标题
                        add_desc_dialog.setText("修改心得")
                        // 更新操作
                        val select_time: String = nowTime.split("").get(0)
                        // 当前所有数据
                        val allDate = DBManager.selectToDoDescDataByDate("$select_time%")
                        // 所有数据的最后一个一定为最新数据
                        val id = allDate[allDate.size - 1].id
                        DBHelper.getDateTime()?.let { it1 ->
                            DBManager.updateDescDateAndTimeById(
                                id, to_do_desc,
                                it1
                            )
                        }
                    } else { // 插入数据
//                        // 将对话框中的数据插入到数据库
                        if (addData != null) {
                            DBManager.insertToDoData(addData)
                        }
                        // 插入数据后 add_desc_flag 设置为1 表示已经插入过数据了
                        add_desc_flag = 1
                        Log.d("asd",add_desc_dialog.toString())
                    }

//                    if (addData != null) {
//                        DBManager.ToAllDoDescData.add(addData)
//                    }

                }
                show()
            }

        }

        // 从 intent 传来需要倒数的时间，单位：分钟
        val PrepareNeedTime = intent.getStringExtra("prepare_need_time")
        // 传过来的标题
        val prepareTitle = intent.getStringExtra("prepare_title")

        if (prepareTitle != null) {
            current_title = prepareTitle
        }

        val intentService = Intent(this, PlayerMusicService::class.java)
        intentService.putExtra("prepare_need_time", PrepareNeedTime)
        intentService.putExtra("current_title", current_title)
        bindService(intentService, connection, Context.BIND_AUTO_CREATE)
        startService(intentService)

    }


    override fun onBackPressed() {
//        super.onBackPressed() //注释掉这行,back键不退出activity
        if (PlayerMusicService.leftTime > 1000) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("确定要退出吗？")
                .setNegativeButton("确定",
                    DialogInterface.OnClickListener { _, _ -> finish() })
                .setPositiveButton("取消", null)
            builder.create().show()
        } else {
            super.onBackPressed()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("asd","destory")
        //停止 Service
        // 销毁界面时取消计时器
        PlayerMusicService.myCountDownTimer?.cancel()
        PlayerMusicService.myCountDownTimer = null
        // 没添加过数据
        add_desc_flag = 0
        // 停止播放音乐
        mediaPlayer.reset()
        unbindService(connection)
        val intent = Intent(this, PlayerMusicService::class.java)
        stopService(intent)
    }


}