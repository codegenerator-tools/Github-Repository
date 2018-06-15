package com.github.repo.android.mainview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.github.repo.android.R
import com.github.repo.android.datahelper.AndroidRepository

/**
 * This class is used to display the repository details
 * To display the data, it expects input data object AndroidRepository
 */
class RepositoryDetailActivity : AppCompatActivity() {

    /**
     * To keep the keys for later use
     * and the input data
     */
    companion object {
        const val EXTRA_NAME = "name"
        const val EXTRA_FULL_NAME = "full_name"
        const val EXTRA_DESCRIPTION = "description"
        const val EXTRA_INITIAL_CREATE = "initial_create"
        const val EXTRA_LAST_UPDATED = "last_updated"
        const val EXTRA_URL = "url"
        const val EXTRA_CLONE_URL = "clone_url"

        /**
         * Companion method to store the intent data for later use
         * The same data shall be consumed based on the input key
         */
        fun newIntent(context: Context, repository: AndroidRepository): Intent {
            val detailIntent = Intent(context, RepositoryDetailActivity::class.java)

            detailIntent.putExtra(EXTRA_NAME, repository.name)
            detailIntent.putExtra(EXTRA_FULL_NAME, repository.full_name)
            detailIntent.putExtra(EXTRA_DESCRIPTION, repository.description)
            detailIntent.putExtra(EXTRA_INITIAL_CREATE, repository.created_at)
            detailIntent.putExtra(EXTRA_LAST_UPDATED, repository.updated_at)
            detailIntent.putExtra(EXTRA_URL, repository.url)
            detailIntent.putExtra(EXTRA_CLONE_URL, repository.clone_url)

            return detailIntent
        }
    }

    //only text view used to populate all the details of the repository
    //Data would be appended on this for final display
    private lateinit var details: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_detail)

        details = findViewById(R.id.detail_view) as TextView

        val name = intent.extras.getString(EXTRA_NAME)
        val fullName = intent.extras.getString(EXTRA_FULL_NAME)
        val description = intent.extras.getString(EXTRA_DESCRIPTION)
        val initialCreate = intent.extras.getString(EXTRA_INITIAL_CREATE)
        val lastUpdated = intent.extras.getString(EXTRA_LAST_UPDATED)
        val url = intent.extras.getString(EXTRA_URL)
        val cloneUrl = intent.extras.getString(EXTRA_CLONE_URL)

        title = name

        details.setText("Repository : " + name + " \n\n ")
        details.append("Full Name : " + fullName + " \n ")
        details.append("Description : " + description + " \n\n ")
        details.append("Created At : " + initialCreate + " \n ")
        details.append("Updated At : " + lastUpdated + " \n\n ")
        details.append("URL : " + url + " \n ")
        details.append("Clone URL : " + cloneUrl + " \n ")
    }
}
