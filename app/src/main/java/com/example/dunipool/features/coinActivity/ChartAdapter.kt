package com.example.dunipool.features.coinActivity

import com.example.dunipool.model.models.ChartData
import com.robinhood.spark.SparkAdapter

class ChartAdapter(

    private val historyCalData: List<ChartData.Data.Data>,
    private val baseLine: String?
) : SparkAdapter() {
    override fun getCount(): Int {
        return historyCalData.size
    }

    override fun getItem(index: Int): ChartData.Data.Data {
        return historyCalData[index]
    }

    override fun getY(index: Int): Float {

        return historyCalData[index].close.toFloat()

    }

    override fun hasBaseLine(): Boolean {
        return true
    }

    override fun getBaseLine(): Float {
        return baseLine?.toFloat() ?: super.getBaseLine()
    }
}