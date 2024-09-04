package com.example.laboratory3

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleCustomSearchApi {
    @GET("customsearch/v1")
    fun searchImages(
        @Query("key") apiKey: String,
        @Query("cx") cseId: String ,
        @Query("q") query: String,
        @Query("searchType") searchType: String
    ): Call<SearchResponce>

    data class SearchResult (
        @SerializedName("link") val link: String
    )
    data class SearchResponce(
        @SerializedName("items") val items: List<SearchResult>
    )
}
