package com.github.repo.android.networkhelper

import com.android.utils.Logger
import com.github.repo.android.datahelper.Response

/**
 * This interface is used to takes care of the Github api calls
 * It makes use of the Retrofit2 library to perform network transactions
 */
interface GithubApiService {


    /**
     * It is used to provide the repository results with the help of retrofit2 library
     *
     * @param query : It uses a string to search
     * e.g 'Android' would return the repositories for android topic
     * @param page : The number of pages requested at single execution
     * @param perPage : The number of items per page
     *
     * @return It returns the final data based on the input query
     */
    @retrofit2.http.GET("search/repositories")
    fun search(@retrofit2.http.Query("q") query: String,
               @retrofit2.http.Query("page") page: Int,
               @retrofit2.http.Query("per_page") perPage: Int): io.reactivex.Observable<Response>

    /**
     * Companion object for the factory is used
     * to deliver a ready-to-use object
     * Also, to hide the complexity of creating and selecting the right object
     */
    companion object Factory {

        private const val BASE_URL = "https://api.github.com/"

        /**
         * This factory method is used to initialize the API Service
         * It takes BASE_URL as the constant and registers the retrofit2
         * To keep it ready for use
         */
        fun create(): GithubApiService {

            Logger.i(Logger.MOD_NETWORK, "Initializing Github API Service")

            val retrofit = retrofit2.Retrofit.Builder()
                    .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                    .baseUrl("$BASE_URL")
                    .build()

            return retrofit.create(GithubApiService::class.java);
        }
    }
}