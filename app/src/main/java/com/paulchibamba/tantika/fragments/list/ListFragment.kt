package com.paulchibamba.tantika.fragments.list

import android.os.Bundle
import android.text.Layout
import android.view.*
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.paulchibamba.tantika.R

class ListFragment : Fragment(), View.OnClickListener {
    private lateinit var floatingActionButtonBtn: FloatingActionButton
    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_list, container, false)

        //Set Menu
        setHasOptionsMenu(true)
       return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener(this)
        view.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.listLayout).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.floatingActionButton -> navController!!.navigate(R.id.action_listFragment_to_addFragment)
            R.id.listLayout -> navController!!.navigate(R.id.action_listFragment_to_updateFragment)
        }
    }


}