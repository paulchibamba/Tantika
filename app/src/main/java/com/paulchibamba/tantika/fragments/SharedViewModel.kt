package com.paulchibamba.tantika.fragments

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.paulchibamba.tantika.R
import com.paulchibamba.tantika.data.models.Priority
import com.paulchibamba.tantika.data.models.ToDoData

class SharedViewModel(application: Application): AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)
    fun checkIfDatabaseIsEmpty(toDoData: List<ToDoData>){
        emptyDatabase.value = toDoData.isEmpty()

    }

    //Spinner Color Logic
    val listener: AdapterView.OnItemSelectedListener = object :
    AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position){
                0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.highPriority)) }
                1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.mediumPriority)) }
                2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.lowPriority)) }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }

    fun verifyDataFromUser(title: String, description: String): Boolean{
        return !(title.isEmpty() || description.isEmpty())
    }

    fun parsePriority(priority: String): Priority {
        return when(priority){
            "High Priority" -> {
                Priority.HIGH}
            "Medium Priority" -> {
                Priority.MEDIUM}
            "Low Priority" -> {
                Priority.LOW}
            else -> Priority.LOW
        }
    }


}