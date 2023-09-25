package com.example.dunipool.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dunipool.model.MainRepository
import com.example.dunipool.model.models.ChartData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.math.log

class CoinViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    fun getChartData(
        symbol: String,
        period: String
    ): LiveData<Pair<List<ChartData.Data.Data>, ChartData.Data.Data?>> {

        val mutableLiveData =
            MutableLiveData<Pair<List<ChartData.Data.Data>, ChartData.Data.Data?>>()

        val errorHandler = CoroutineExceptionHandler { _, throwable ->

            Log.e("ChartDataError", throwable.message!!)


        }

        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val data = mainRepository.getChartData(symbol, period)
            val data1 = data.data.data

            val data2 = data.data.data.maxByOrNull { it.close.toFloat() }

            val returningData = Pair(data1, data2)

            mutableLiveData.postValue(returningData)


        }

        return mutableLiveData


    }

    override fun onCleared() {
        viewModelScope.cancel()
        compositeDisposable.clear()
        super.onCleared()
    }


}