package com.paulchibamba.tantika.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.text.Layout
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.paulchibamba.tantika.R
import com.paulchibamba.tantika.data.viewmodel.ToDoViewModel
import com.paulchibamba.tantika.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ListFragment : Fragment(), View.OnClickListener {
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    var navController: NavController? = null

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        //Recycler View Adapter
        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            mSharedViewModel.checkIfDatabaseIsEmpty(data)
            adapter.setData(data)
        })
        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseViews(it)
        })
        //Set Menu
        setHasOptionsMenu(true)
        return view
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if (emptyDatabase){
            view?.findViewById<ImageView>(R.id.no_data_imageView)?.visibility = View.VISIBLE
            view?.findViewById<TextView>(R.id.no_data_textView)?.visibility = View.VISIBLE
        }else{
            view?.findViewById<ImageView>(R.id.no_data_imageView)?.visibility = View.INVISIBLE
            view?.findViewById<TextView>(R.id.no_data_textView)?.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)


        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.floatingActionButton -> navController!!.navigate(R.id.action_listFragment_to_addFragment)
            R.id.listLayout -> navController!!.navigate(R.id.action_listFragment_to_updateFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_delete_all ->
                confirmRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    //Show an Alert Dialog Message to confirm the removal of all items
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes, Delete All") {_, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(), "Successfully Deleted Everything!", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") {_, _ ->}
        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure you want to delete all your ToDos?")
        builder.create().show()
    }
}