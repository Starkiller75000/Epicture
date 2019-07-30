package com.example.starkiller75000.epicture

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.starkiller75000.epicture.api.ImgurApi


class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val token: String = this.arguments!!.getString("token")
        val rv_gallery = view.findViewById<RecyclerView>(R.id.rv_gallery)
        val sortSpinner = view.findViewById<Spinner>(R.id.sortSpinner)
        val searchInput = view.findViewById<SearchView>(R.id.searchInput)
        val preferences = PreferenceManager.getDefaultSharedPreferences(activity!!.applicationContext)
        val (_, sortIndex, _) = PreferenceUtils.getGalleryParameters(preferences, resources)

        sortSpinner.setSelection(sortIndex, true)

        val galleryAdapter = GalleryAdapter(arrayOf(), arrayOf(), this.activity!!, token)
        rv_gallery.adapter = galleryAdapter

        sortSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // We shouldn't need to do anything here as user can't select an empty entry anyways
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                AsyncAction({ ImgurApi.getSearch(searchInput.query.toString(), position) },
                    { images -> galleryAdapter.items = images })
            }
        }

        searchInput.setOnClickListener { searchInput.isIconified = false }

        searchInput.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val sortIndex = sortSpinner.selectedItemPosition
                AsyncAction({ ImgurApi.getSearch(query, sortIndex) }, { images ->
                    galleryAdapter.items = images
                    rv_gallery.requestFocus()
                })
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return view
    }
}