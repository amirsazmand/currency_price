package com.example.dunipool.model.api

import com.example.dunipool.utils.API_KEY
import com.example.dunipool.model.models.ChartData
import com.example.dunipool.model.models.CoinsInfo
import com.example.dunipool.model.models.NewsData
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @Headers(API_KEY)
    @GET("v2/news/")
    suspend fun getTopNews(
        // query params -> popular or latest
        @Query("sortOrder") sortOrder: String = "popular"
    ): NewsData


    @Headers(API_KEY)
    @GET("top/totalvolfull")
    suspend fun getTopCoins(
        @Query("tsym") symbol: String = "USD",
        @Query("limit") limitData: Int = 50

    ): CoinsInfo



    @Headers(API_KEY)
    @GET("v2/{period}")
   suspend fun getChartData (

        @Path("period") period: String ,
        @Query("fsym") fromSymbol : String ,
        @Query("limit") limit : Int ,
        @Query("aggregate") aggregate : Int ,
        @Query("tsym") toSymbol : String = "USD"

    ) : ChartData




}