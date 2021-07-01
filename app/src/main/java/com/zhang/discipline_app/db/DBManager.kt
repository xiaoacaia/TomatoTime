package com.zhang.discipline_app.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.zhang.discipline_app.PrepareToDOWithId
import com.zhang.discipline_app.PrepareToDo
import com.zhang.discipline_app.ToDoDesc
import com.zhang.discipline_app.ToDoDescWithId
import kotlin.collections.ArrayList

object DBManager {

    private var db: SQLiteDatabase? = null

    // 待办数据源
    val PrepareToDoData = ArrayList<PrepareToDo>()
//    val ToAllDoDescData = ArrayList<ToDoDesc>()
    var currentToDODescData =  ArrayList<ToDoDesc>()
    var flag = 0

    // 查询数据库所有数据，并将数据传入适配器
    fun getAllData(){
        for (i in selectAllPrepareData()){
            PrepareToDoData.add(i.pre)
        }
//        for (i in selectAllToDoDescData()){
//            ToAllDoDescData.add(i.toDoDesc)
//        }

    }


    fun getDescDataByDate(date: String): ArrayList<ToDoDesc> {
        val ToDoDescData = ArrayList<ToDoDesc>()
        for (i in selectToDoDescDataByDate(date)){
            ToDoDescData.add(i.toDoDesc)
        }
        return ToDoDescData
    }


    /* 初始化数据库对象*/
    fun initDB(context: Context) {
        // 创建数据库
        val dbHelper = DBHelper(context, "DisciplineDataBase.db", 1)
        // 检查 dbHelper 中的数据库是否存在，不存在便创建数据库并调用 MyDatabaseHelper 的 OnCreate() 方法
        db = dbHelper.writableDatabase

        if (flag == 0){
            getAllData()
            flag = 1
        }
    }


    // 查询待办事项中所有的数据
    @SuppressLint("Recycle")
    fun selectAllPrepareData(): ArrayList<PrepareToDOWithId> {
        val list = ArrayList<PrepareToDOWithId>()
        val cursor = db?.rawQuery("select * from prepareToDo", null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex("id"))
                    val prepareTitle = cursor.getString(cursor.getColumnIndex("prepareTitle"))
                    val prepareNeedTime = cursor.getString(cursor.getColumnIndex("PrepareNeedTime"))
                    val imageId = cursor.getInt(cursor.getColumnIndex("imageId"))
                    list.add(
                        PrepareToDOWithId(
                            id,
                            PrepareToDo(prepareTitle, prepareNeedTime, imageId)
                        )
                    )
                } while (cursor.moveToNext())
            }
        }
        return list
    }


    // 插入待办事项的数据
    fun insertPrepareData(singData: PrepareToDo) {
        db?.insert("prepareToDo", null, ContentValues().apply {
            put("prepareTitle", singData.prepareTitle)
            put("PrepareNeedTime", singData.prepareNeedTime)
            put("imageId", singData.imageId)
        })
    }

    // 根据 id 删除待办事项中的数据
    fun deletePrepareById(id: Int) {
        db?.delete("prepareToDo", "id = ?", arrayOf(id.toString()))
    }


    /*  心得类代码 */


    // 查询心得中所有的数据
    /*@SuppressLint("Recycle")
    fun selectAllToDoDescData(): ArrayList<ToDoDescWithId> {
        val list = ArrayList<ToDoDescWithId>()
        val cursor = db?.rawQuery("select * from toDoDesc", null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex("id"))
                    val todo_title = cursor.getString(cursor.getColumnIndex("todo_title"))
                    val todo_desc = cursor.getString(cursor.getColumnIndex("todo_desc"))
                    val start_time = cursor.getString(cursor.getColumnIndex("start_time"))
                    val end_time = cursor.getString(cursor.getColumnIndex("end_time"))
                    list.add(
                        ToDoDescWithId(
                            id,
                            ToDoDesc(todo_title, todo_desc, start_time, end_time)
                        )
                    )
//                    Log.d("dataall",ToDoDescWithId(id,ToDoDesc(todo_title, todo_desc, start_time, end_time)).toString())
                } while (cursor.moveToNext())
            }
        }
        return list
    }*/

    // 按照日期查询心得中所有的数据
    @SuppressLint("Recycle")
    fun selectToDoDescDataByDate(date: String): ArrayList<ToDoDescWithId> {
        val list = ArrayList<ToDoDescWithId>()
        val cursor = db?.rawQuery("select * from toDoDesc where start_time like ? ", arrayOf(date))
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex("id"))
                    val todo_title = cursor.getString(cursor.getColumnIndex("todo_title"))
                    val todo_desc = cursor.getString(cursor.getColumnIndex("todo_desc"))
                    val start_time = cursor.getString(cursor.getColumnIndex("start_time"))
                    val end_time = cursor.getString(cursor.getColumnIndex("end_time"))
                    list.add(
                        ToDoDescWithId(
                            id,
                            ToDoDesc(todo_title, todo_desc, start_time, end_time)
                        )
                    )

//                    Log.d("bydate",ToDoDescWithId(id,ToDoDesc(todo_title, todo_desc, start_time, end_time)).toString())


                } while (cursor.moveToNext())
            }
        }
        return list
    }


    // 插入待办事项的数据
    fun insertToDoData(singData: ToDoDesc) {
        db?.insert("toDoDesc", null, ContentValues().apply {
            put("todo_title", singData.to_do_title)
            put("todo_desc", singData.to_do_desc)
            put("start_time", singData.start_time)
            put("end_time", singData.end_time)
        })
    }


    // 根据 id 删除心得中的数据
    fun deleteDescById(id: Int) {
        db?.delete("toDoDesc", "id = ?", arrayOf(id.toString()))
    }


    // 根据 id 更新心得中的数据
    fun updateDescDateById(id: Int, date: String){
        db?.execSQL("update toDoDesc set todo_desc = ? where id = ?", arrayOf(date, id))
    }

    fun updateDescDateAndTimeById(id: Int, date: String, endtime: String){
        db?.execSQL("update toDoDesc set todo_desc = ?, end_time = ?  where id = ?", arrayOf(date, endtime, id))
    }

    fun updateDescTimeById(id: Int, endtime: String){
        db?.execSQL("update toDoDesc set  end_time = ?  where id = ?", arrayOf( endtime, id))
    }


}