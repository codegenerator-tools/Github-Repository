package com.github.repo.android.datahelper

import com.android.utils.Logger
import com.github.repo.android.networkhelper.GithubApiService

/**
 * This class shall be used to search for the Github repositories
 */
class LocateRepository(private val apiService: GithubApiService) {

    /**
     * This method is used to fetch the Github repositories
     *
     * @param : The results shall be provided based on the input string
     * e.g 'Android' text string would return the repositories related to Android topic
     *
     * @return : Github Repositories should be retunred
     */
    fun searchGitRepos(topic: String): io.reactivex.Observable<Response> {

        Logger.i(Logger.MOD_DATA_HELPER, "Searching repository for topic : " + topic)
        return apiService.search(query = "topic:$topic", page = 1, perPage = 15)
    }

}