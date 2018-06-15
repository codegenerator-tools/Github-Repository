package com.github.repo.android.datahelper

import com.google.gson.annotations.SerializedName

/**
 * Data Structure to handle the Android Repository response
 * The same can be used to populate the Github API results
 */
data class AndroidRepository(

        @SerializedName("login") val login: String,
        @SerializedName("id") val id: Long,
        @SerializedName("url") val url: String,
        @SerializedName("description") val description: String,
        @SerializedName("full_name") val full_name: String,
        @SerializedName("name") val name: String,
        @SerializedName("created_at") val created_at: String,
        @SerializedName("updated_at") val updated_at: String,
        @SerializedName("html_url") val htmlUrl: String,
        @SerializedName("followers_url") val followersUrl: String,
        @SerializedName("following_url") val followingUrl: String,
        @SerializedName("starred_url") val starredUrl: String,
        @SerializedName("gists_url") val gistsUrl: String,
        @SerializedName("type") val type: String,
        @SerializedName("score") val score: Double,
        @SerializedName("clone_url") val clone_url: String
)

/**
 * The Final response structure
 * It is based on the data structure returned by Github repository api calls
 * e.g.
     {
    "total_count": 4806034,
    "incomplete_results": false,
    "items": [
    {
    "login": 22790488
    }, {} , {}
    }
 */
data class Response(
        val total_count: Int,
        val incomplete_results: Boolean,
        val items: List<AndroidRepository>
)