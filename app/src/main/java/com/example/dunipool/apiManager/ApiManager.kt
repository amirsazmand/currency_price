package com.example.dunipool.apiManager

import com.example.dunipool.models.ChartData
import com.example.dunipool.models.CoinsInfo
import com.example.dunipool.models.NewsData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiManager {

    private val apiService: ApiService


    init {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor {

                val oldRequest = it.request()
                val newRequest = oldRequest.newBuilder()

                newRequest.addHeader("authorization", API_KEY)

                it.proceed(newRequest.build())

            }

            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

    }

    fun getTopNews(apiCallback: ApiCallback<ArrayList<Pair<String, String>>>) {

        apiService.getTopNews().enqueue(object : Callback<NewsData> {
            override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {
                val data = response.body()!!

                val dataToSend: ArrayList<Pair<String, String>> = arrayListOf()

                data.data.forEach {

                    dataToSend.add(Pair(it.title, it.url))

                }

                apiCallback.onSuccess(dataToSend)

            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {
                apiCallback.onError(t.message!!)
            }


        })


    }

    fun getTopCoins(apiCallback: ApiCallback<List<CoinsInfo.Data>>) {

        apiService.getTopCoins().enqueue(object : Callback<CoinsInfo> {
            override fun onResponse(call: Call<CoinsInfo>, response: Response<CoinsInfo>) {
                val data = response.body()!!

                apiCallback.onSuccess(data.data)


            }

            override fun onFailure(call: Call<CoinsInfo>, t: Throwable) {
                apiCallback.onError(t.message!!)
            }


        })


    }

    fun getChartData(
        symbol: String,
        period: String,
        apiCallback: ApiCallback<Pair<List<ChartData.Data.Data>, ChartData.Data.Data?>>
    ) {

        var histoPeriod = ""
        var limit = 30
        var aggregate = 1
        when (period) {
            HOUR -> {
                histoPeriod = HISTO_MINUTE
                limit = 60
                aggregate = 12

            }

            HOURS24 -> {
                histoPeriod = HISTO_HOUR
                limit = 24

            }

            WEEK -> {
                histoPeriod = HISTO_DAY
                aggregate = 6
            }

            MONTH -> {
                histoPeriod = HISTO_DAY
                limit = 30
            }

            MONTH3 -> {
                histoPeriod = HISTO_DAY
                limit = 90
            }

            YEAR -> {
                histoPeriod = HISTO_DAY
                aggregate = 13
            }

            ALL -> {
                histoPeriod = HISTO_DAY
                aggregate = 30
                limit = 2000

            }


        }

        apiService.getchartData(histoPeriod, symbol, limit, aggregate)
            .enqueue(object : Callback<ChartData> {
                override fun onResponse(call: Call<ChartData>, response: Response<ChartData>) {
                    val fullData = response.body()!!

                    // list dots
                    val data1 = fullData.data.data

                    // list
                    val data2 = fullData.data.data.maxByOrNull { it.close.toFloat() }

                    val returningData = Pair(data1, data2)

                    apiCallback.onSuccess(returningData)

                }

                override fun onFailure(call: Call<ChartData>, t: Throwable) {
                    apiCallback.onError(t.message!!)
                }


            })
    }

    interface ApiCallback<T> {

        fun onSuccess(data: T)

        fun onError(messageError: String)


    }

}