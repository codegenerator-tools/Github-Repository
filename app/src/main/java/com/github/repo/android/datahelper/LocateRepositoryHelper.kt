package com.github.repo.android.datahelper

import com.android.utils.Logger
import com.github.repo.android.networkhelper.GithubApiService

/**
 * The helper class to be used for GithubApiService initialization
 */
object LocateRepositoryHelper {

    /*
     * It would use the retrofit2 to handle the network related calls
     *
     * @return : The object shall be provided with the Github base url setup
     * Github Base URL is : "https://api.github.com/"
     */
    fun provideRepository(): LocateRepository {

        Logger.i(Logger.MOD_NETWORK, "Github repository provider request initiated")
        return LocateRepository(GithubApiService.create())
    }
}