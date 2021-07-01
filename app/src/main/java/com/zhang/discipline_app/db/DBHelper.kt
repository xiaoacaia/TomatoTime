package com.zhang.discipline_app.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(val context: Context, name: String, version: Int): SQLiteOpenHelper(
    context,
    name,
    null,
    version
) {


    companion object{
         fun getDateTime(): String? {

             val fm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
             fm.timeZone = TimeZone.getTimeZone("GMT+8")
            return fm.format(Date())
        }
    }

    private val createPrepareToDO = "create table prepareToDo(" +
            "id integer primary key autoincrement," +
            "prepareTitle text," +
            "PrepareNeedTime text," +
            "imageId integer)"

    private val createToDoDesc = "create table toDoDesc(" +
            "id integer primary key autoincrement," +
            "todo_title text," +
            "todo_desc text," +
            "start_time datetime," +
            "end_time datetime DEFAULT CURRENT_TIMESTAMP)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createPrepareToDO)
        db?.execSQL(createToDoDesc)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}
