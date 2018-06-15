package com.github.repo.android.viewhelper;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.github.repo.android.R
import com.github.repo.android.datahelper.AndroidRepository

/**
 * Helper class which provides customized listview
 * which enables to handle multiple items in a single row
 *
 */
class RepositoryAdapter(private val context: Context,
                        private val dataSource: ArrayList<AndroidRepository>) : BaseAdapter() {

    //Real time layout inflation to handle the view
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    /**
     * To find the count of the repositories
     *
     * @return item count
     */
    override fun getCount(): Int {
        return dataSource.size
    }

    /**
     * To fetch the selected item object from the list
     *
     * @param The selected position of the item
     * @return AndroidRepository object basis input position
     */
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    /**
     * @return item id basis selected position
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * overridden method to handle each row of list view
     * It creates view for each row and populates the input data
     * This method needs to be triggered to retrieve the updates
     * or refresh the list
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View
        val holder: ViewHolder

        //to be initialized for the first time
        if (convertView == null) {

            view = inflater.inflate(R.layout.list_item_repository, parent, false)
            holder = ViewHolder()
            holder.titleTextView = view.findViewById(R.id.recipe_list_title) as TextView
            holder.subtitleTextView = view.findViewById(R.id.recipe_list_subtitle) as TextView
            holder.detailTextView = view.findViewById(R.id.recipe_list_detail) as TextView

            view.tag = holder
        }
        else {

            //if the view is already created, skip the initialization
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        //data update shall be called every time the getview has requested
        //to provide the latest data
        val titleTextView = holder.titleTextView
        val subtitleTextView = holder.subtitleTextView
        val detailTextView = holder.detailTextView
        val repository = getItem(position) as AndroidRepository

        titleTextView.text = repository.name
        subtitleTextView.text = repository.updated_at

        return view
    }

    private class ViewHolder {

        //repository title which would be the name of the Repository
        lateinit var titleTextView: TextView

        //The last updated timestamp of the repository
        lateinit var subtitleTextView: TextView

        //The description of the repository
        lateinit var detailTextView: TextView
    }
}
