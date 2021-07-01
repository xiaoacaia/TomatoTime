package com.zhang.discipline_app

data class PrepareToDo(val prepareTitle: String, val prepareNeedTime: String, val imageId: Int)
class PrepareToDOWithId(val id: Int, val pre: PrepareToDo)

data class ToDoDesc(val to_do_title: String, var to_do_desc: String, val start_time: String, val end_time: String)
data class ToDoDescWithId(val id: Int, val toDoDesc: ToDoDesc)

