package com.paulchibamba.tantika.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.paulchibamba.tantika.R
import com.paulchibamba.tantika.data.models.ToDoData
import com.paulchibamba.tantika.data.viewmodel.ToDoViewModel
import com.paulchibamba.tantika.databinding.FragmentAddBinding
import com.paulchibamba.tantika.fragments.SharedViewModel
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
class AddFragment : Fragment() {
    @InternalCoroutinesApi
    private val mTodoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)

        //Set Menu
        setHasOptionsMenu(true)



//        titleEditText = binding.titleEt
//        prioritySpinner = binding.prioritiesSpinner
//        descriptionEditText = binding.descriptionEt

        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener
        return binding.root
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
        val mTitle = binding.titleEt.text.toString()
        val mPriority = binding.prioritiesSpinner.selectedItem.toString()
        val mDescription = binding.descriptionEt.text.toString()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}