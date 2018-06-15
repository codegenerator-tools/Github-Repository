package com.github.repo.android.mainview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.Toast
import com.android.utils.Logger
import com.github.repo.android.R
import com.github.repo.android.datahelper.AndroidRepository
import com.github.repo.android.datahelper.LocateRepositoryHelper
import com.github.repo.android.viewhelper.RepositoryAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * This is the main launcher class which is used as the initial triggering point to receive
 * Android Repositories from Github and to display the results in the form of list view
 */
class RepositoryListActivity : AppCompatActivity() {

    /**
     * CompositeDisposable is used to dispose all the repository related dirty work
     * To be quick and safe, disposable is here to help
     */
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * It would hold the final github android repository results
     */
    private lateinit var repositoryList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repositoryList = findViewById(R.id.recipe_list_view) as ListView

        //To check whether network connectivity is available
        //uncomment below
        /*
        if( !(NetworkStateManager.getInstance().isNetworkAvailable) ) {

            Logger.w(Logger.MOD_NETWORK, "Internet Connection not available, please connect and try again")
            Toast.makeText(applicationContext, "Internet Connection not available, please connect and try again", Toast.LENGTH_SHORT).show()

            //return;
        }*/

        //GithubApi Service initialization to keep it ready with base url
        val repositoryHelper = LocateRepositoryHelper.provideRepository()

        compositeDisposable.add(
                //we are good to go to hit the request on github
                repositoryHelper.searchGitRepos("Android")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe ({
                            result ->

                            Logger.i(Logger.MOD_VIEW, "Received received repository result, processing -> " + result.toString())

                            if( !(result?.items != null && result.items.isNotEmpty()) ) {

                                Logger.w(Logger.MOD_VIEW, "No Data found, Please try again")
                                Toast.makeText(applicationContext, "No Data found, Please try again", Toast.LENGTH_SHORT).show()
                            }
                            else {

                                Logger.d(Logger.MOD_VIEW, "Data successfully received, repository count : " + result.items.size);

                                val repositoryList = result.items

                                val adapter = RepositoryAdapter(this, repositoryList as ArrayList<AndroidRepository>)
                                this.repositoryList.adapter = adapter

                                val context = this
                                this.repositoryList.setOnItemClickListener { _, _, position, _ ->

                                    val selectedRepository = repositoryList[position]

                                    Logger.d(Logger.MOD_VIEW, "Item click event received, displaying details for -> " + selectedRepository.name)
                                    val detailIntent = RepositoryDetailActivity.newIntent(context, selectedRepository)

                                    startActivity(detailIntent)
                                }
                            }
                        }, { error ->
                            error.printStackTrace()
                            Logger.dumpException(Logger.MOD_VIEW, error.toString())
                        })
        )
    }

    override fun onDestroy() {

        //The request is bound with this activity
        //we no longer require the data, as the activity has gone to background
        //just one line to take care of all
        compositeDisposable.clear()

        super.onDestroy()
    }

    override fun onResume() {

        //uncomment below line to register for network broadcast receiver
        //NetworkStateManager.getInstance().initialize(applicationContext)

        super.onResume()
    }

    override fun onPause() {

        //uncomment below line to un-register for network broadcast receiver
        //NetworkStateManager.getInstance().unRegister()

        super.onPause()
    }
}
