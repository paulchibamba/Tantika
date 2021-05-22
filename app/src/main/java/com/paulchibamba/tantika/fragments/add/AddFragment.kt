package com.paulchibamba.tantika.fragments.add

import android.os.Bundle
import android.renderscript.RenderScript
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.paulchibamba.tantika.R
import com.paulchibamba.tantika.data.ToDoDatabase
import com.paulchibamba.tantika.data.models.Priority
import com.paulchibamba.tantika.data.models.ToDoData
import com.paulchibamba.tantika.data.viewmodel.ToDoViewModel
import com.paulchibamba.tantika.fragments.SharedViewModel
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
class AddFragment : Fragment() {
    @InternalCoroutinesApi
    private val mTodoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private lateinit var titleEditText: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var descriptionEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Set Menu
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        titleEditText = view.findViewById(R.id.title_et) as EditText
        prioritySpinner = view.findViewById(R.id.priorities_spinner) as Spinner
        descriptionEditText = view.findViewById(R.id.description_et) as EditText

        prioritySpinner.onItemSelectedListener = mSharedViewModel.listener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add){
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    @InternalCoroutinesApi
    private fun insertDataToDb() {
        val mTitle = titleEditText.text.toString()
        val mPriority = prioritySpinner.selectedItem.toString()
        val mDescription = descriptionEditText.text.toString()
        val validation = mSharedViewModel.verifyDataFromUser(mTitle, mDescription)

        if (validation){
            //Insert Data into DB
            val newData = ToDoData(
                    0,
                    mTitle,
                    mSharedViewModel.parsePriority(mPriority),
                    mDescription
            )
            mTodoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_SHORT).show()

            //Navigate back to List fragment
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please Fill Out All Fields", Toast.LENGTH_SHORT).show()
        }
    }


}