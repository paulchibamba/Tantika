package com.paulchibamba.tantika.data.repository

import androidx.lifecycle.LiveData
import com.paulchibamba.tantika.data.ToDoDao
import com.paulchibamba.tantika.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {
    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insertData(toDoData)
    }
}