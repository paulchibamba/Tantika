package com.paulchibamba.tantika.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.paulchibamba.tantika.R


class AddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Set Menu
        setHasOptionsMenu(true)


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mTitle = view.findViewById<EditText>(R.id.title_et).text.toString()
        val mPriority = view.findViewById<Spinner>(R.id.priorities_spinner).selectedItem.toString()
        val mDescription = view.findViewById<EditText>(R.id.description_et).text.toString()
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

    private fun insertDataToDb(view: View) {
        val mTitle = view.findViewById<EditText>(R.id.title_et).text.toString()
        val mDescription = view.findViewById<EditText>(R.id.description_et).text.toString()
        val validation = verifyDataFromUser(mTitle, mDescription)
    }

    private fun verifyDataFromUser(title: String, description: String): Boolean{
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            false
        }else !(title.isEmpty() || description.isEmpty())
    }
}