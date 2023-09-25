package com.example.dunipool.apiManager

import com.example.dunipool.models.ChartData
import com.example.dunipool.models.CoinsInfo
import com.example.dunipool.models.NewsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @Headers(API_KEY)
    @GET("v2/news/")
    fun getTopNews(
        // query params -> popular or latest
        @Query("sortOrder") sortOrder: String = "popular"
    ): Call<NewsData>


    @Headers(API_KEY)
    @GET("top/totalvolfull")
    fun getTopCoins(
        @Query("tsym") symbol: String = "USD",
        @Query("limit") limitData: Int = 50

    ): Call<CoinsInfo>

    @Headers(API_KEY)
    @GET("v2/{period}")
    fun getchartData (

        @Path("period") period: String ,
        @Query("fsym") fromSymbol : String ,
        @Query("limit") limit : Int ,
        @Query("aggregate") aggregate : Int ,
        @Query("tsym") toSymbol : String = "USD"

    ) : Call<ChartData>


//    @GET("users")
//    fun getUsers(): Call<ArrayList<User>>
//
//    @GET("users/{id}")
//    fun getUser(@Path("id") idUsers: Int): Call<User>
//
//    @GET("users")
//    fun getUsersSorted(@Query("sort") queryParam: String): Call<List<User>>
//
//    @POST("users")
//    fun insertUser(@Body body: JsonObject)
//
//    @GET("users")
//    fun authUser(@Header("Authorization") auth: String)


}