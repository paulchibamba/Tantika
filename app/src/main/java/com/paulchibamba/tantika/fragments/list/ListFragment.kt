package com.paulchibamba.tantika.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.text.BoringLayout.make
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.paulchibamba.tantika.R
import com.paulchibamba.tantika.data.models.ToDoData
import com.paulchibamba.tantika.data.viewmodel.ToDoViewModel
import com.paulchibamba.tantika.databinding.FragmentListBinding
import com.paulchibamba.tantika.fragments.SharedViewModel
import com.paulchibamba.tantika.fragments.list.Adapter.ListAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.FieldPosition

@InternalCoroutinesApi
class ListFragment : Fragment(), View.OnClickListener, SearchView.OnQueryTextListener{
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    var navController: NavController? = null

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Data binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        //Recycler View Adapter
        setupRecyclerView()

        //Observes LiveData
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            mSharedViewModel.checkIfDatabaseIsEmpty(data)
            adapter.setData(data)
        })

        //Set Menu
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = LandingAnimator().apply {
            addDuration = 300
        }

        //Allows user to swipe left to delete an item
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                //Delete Item
                mToDoViewModel.deleteData(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                //Restore Delete Item
                restoreDeletedData(viewHolder.itemView, deletedItem, viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData, position: Int){
        val snackbar = Snackbar.make(
                view, "Deleted '${deletedItem.title}'",
                Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo"){
            mToDoViewModel.insertData(deletedItem)
        }
        snackbar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.listLayout -> navController!!.navigate(R.id.action_listFragment_to_updateFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, Observer { adapter.setData(it) })
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(this, Observer { adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            searchThroughDatabase(newText)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery: String = "%$query%"

        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer {list ->
            list?.let {
                adapter.setData(it)
            }
        })

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}