package com.zhang.discipline_app.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.zhang.discipline_app.db.DBHelper
import com.zhang.discipline_app.db.DBManager
import com.zhang.discipline_app.ToDoDesc
import com.zhang.discipline_app.main.CountDownActivity
import com.zhang.discipline_app.main.CountDownActivity.Companion.nowTime
import com.zhang.discipline_app.databinding.TomatoActivityBinding
import com.zhang.discipline_app.main.MainActivity

class PlayerMusicService : Service() {
    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    var _binding: TomatoActivityBinding? = null
    private val binding get() = _binding!!

    companion object {
        // 创建 MediaPlayer 对象
        val mediaPlayer = MediaPlayer()
        lateinit var prepare_need_time: String
        lateinit var current_title: String

        @SuppressLint("StaticFieldLeak")
        var myCountDownTimer: MyCountDownTimer? = null

        // 剩余时间
        var leftTime: Long = 15 * 60 * 1000

        var all: Long = 5 * 1000

        // 创建初始时间

    }


    private val mBinder = MyTimer(this)

    override fun onDestroy() {
        Log.d("asd","service destory")

        super.onDestroy()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            prepare_need_time = intent.getStringExtra("prepare_need_time").toString()
            current_title = intent.getStringExtra("current_title").toString()
        }


        return super.onStartCommand(intent, flags, startId)
    }


    class MyTimer(val service_this: Context) : Binder() {
        fun StartTimer() {
            // 总时间
            if (prepare_need_time == "999") {    // 测试时间 5 s
                all = 5 * 1000
            } else {
                all = prepare_need_time.toLong() * 60 * 1000
            }
            // 设置计时器
            myCountDownTimer = MyCountDownTimer(all, 1000, service_this)
            myCountDownTimer!!.start()
        }

    }

    class MyCountDownTimer(
        millisInFuture: Long,
        countDownInterval: Long,
        val counter_this: Context
    ) :
        CountDownTimer(millisInFuture, countDownInterval) {

        // 倒计时开始时要做的事情     millisUntilFinished 还剩多少时间
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            leftTime = millisUntilFinished

            Log.d("asdqweqwe", millisUntilFinished.toString())

            val minute = millisUntilFinished / 1000 / 60
            val second = millisUntilFinished / 1000 % 60
            // 如果秒数小于 10
            if (second < 10) {
                // 在秒数前添加一个0 如 10:5 -> 10:05
                CountDownActivity.binding.leaveTime.text = "$minute: 0$second"
                Log.d("asdqweqwe", "$minute: 0$second")
            } else {
                CountDownActivity.binding.leaveTime.text = "$minute: $second"
                Log.d("asdqweqwe", "$minute: $second")
            }
        }

        // 计时器结束的时候要做的事情
        override fun onFinish() {
            // 播放音乐
//                val intent = Intent(binding.root.context, PlayerMusicService::class.java)
//                startService(intent)
            // 播放音乐
            // getAssets() 方法用于读取 assets 目录下的任何资源
            val assetManager = counter_this.assets
            // 将音乐文件句柄打开
            val fd = assetManager.openFd("love_Story.mp3")
            // 设置音频文件的路径
            mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
            // 使 MediaPlayer 进入准备状态，不能省略 prepare 直接进入 start
            mediaPlayer.prepare()
            // 开始播放音乐
            mediaPlayer.start()
//            Log.d("asd","???????")
            // 记录做过的事
            if (CountDownActivity.add_desc_flag == 0) { //如果之前没插入过数据
                val addData =
                    nowTime.let { it1 ->
                        DBHelper.getDateTime()?.let { it2 ->
                            ToDoDesc(
                                current_title, "", it1,
                                it2
                            )
                        }
                    }
                if (addData != null) {
                    DBManager.insertToDoData(addData)
                }

            } else {  // 更新日期
                // 更新操作
                val select_time: String = nowTime.split("").get(0)
                // 当前所有数据
                val allDate = DBManager.selectToDoDescDataByDate("$select_time%")
                // 所有数据的最后一个一定为最新数据
                val id = allDate[allDate.size - 1].id
                DBHelper.getDateTime()?.let { it1 ->
                    DBManager.updateDescTimeById(
                        id,
                        it1
                    )
                }
            }


        }

    }

    @SuppressLint("InflateParams", "WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        Log.d("asd","servie create")

        super.onCreate()

        // NotificationManager 类对通知进行管理
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 使用 NotificationChannel 类构建一个通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("my_service", "音乐播放", NotificationManager.IMPORTANCE_MAX)
            manager.createNotificationChannel(channel)
        }

        // 表达我们想要启动某个页面的意图
        val intent = Intent(this, MainActivity::class.java)

        // PendingIntent 可以理解为延迟执行的 Intent
        val pi = PendingIntent.getActivity(this, 0, intent, 0)

        val notification = NotificationCompat.Builder(this, "my_service")
            .setContentTitle("playing music")
            .setContentText("time is up")
            .setContentIntent(pi)
            .build()
        // 将 Service 变成一个前台 Service
        startForeground(1, notification)

    }


}

