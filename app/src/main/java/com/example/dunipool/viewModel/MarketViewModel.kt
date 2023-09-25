package com.example.dunipool.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dunipool.model.models.CoinAboutData
import com.example.dunipool.model.models.CoinsInfo
import com.example.dunipool.model.MainRepository
import com.example.dunipool.model.models.NewsData
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MarketViewModel(
    private val application: Application,
    private val mainRepository: MainRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    fun getTopNews(): LiveData<ArrayList<Pair<String, String>>> {


        val mutableLiveData = MutableLiveData<ArrayList<Pair<String, String>>>()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->

            Log.e("errorTopNews", throwable.message!!)
        }

        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val data = mainRepository.getTopNews()
            val dataToSend: ArrayList<Pair<String, String>> = arrayListOf()
            data.data.forEach {

                dataToSend.add(Pair(it.title, it.url))

            }

            mutableLiveData.postValue(dataToSend)

        }
        return mutableLiveData


    }

    fun getTopCoins(): LiveData<List<CoinsInfo.Data>> {
        val mutableLiveData = MutableLiveData<List<CoinsInfo.Data>>()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->

            Log.e("errorTopCoins", throwable.message!!)
        }

        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val data = mainRepository.getTopCoins()
            mutableLiveData.postValue(data.data)
        }
        return mutableLiveData

    }

    fun getCoinsAboutDataFromAssets(): CoinAboutData {

        return mainRepository.getCoinsAboutDataFromAssets(application.applicationContext)

    }


    override fun onCleared() {
        viewModelScope.cancel()
        compositeDisposable.clear()
        super.onCleared()
    }


}