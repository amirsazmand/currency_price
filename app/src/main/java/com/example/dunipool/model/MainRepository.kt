package com.example.dunipool.model

import android.content.Context
import com.example.dunipool.model.api.ApiService
import com.example.dunipool.model.models.ChartData
import com.example.dunipool.model.models.CoinAboutData
import com.example.dunipool.model.models.CoinsInfo
import com.example.dunipool.model.models.NewsData
import com.example.dunipool.utils.ALL
import com.example.dunipool.utils.HISTO_DAY
import com.example.dunipool.utils.HISTO_HOUR
import com.example.dunipool.utils.HISTO_MINUTE
import com.example.dunipool.utils.HOUR
import com.example.dunipool.utils.HOURS24
import com.example.dunipool.utils.MONTH
import com.example.dunipool.utils.MONTH3
import com.example.dunipool.utils.WEEK
import com.example.dunipool.utils.YEAR
import com.google.gson.Gson

class MainRepository(private val apiService: ApiService) {


    suspend fun getTopNews(): NewsData {
        return apiService.getTopNews()

    }

    suspend fun getTopCoins(): CoinsInfo {

        return apiService.getTopCoins()

    }


    fun getCoinsAboutDataFromAssets(context: Context): CoinAboutData {
        val fileInString = context.applicationContext.assets
            .open("currencyinfo.json")
            .bufferedReader()
            .use { it.readText() }

        val gson = Gson()
        return gson.fromJson(fileInString, CoinAboutData::class.java)

    }

    suspend fun getChartData(   symbol: String  ,  period: String  ): ChartData {
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

        return apiService.getChartData(histoPeriod, symbol, limit, aggregate)

    }
}