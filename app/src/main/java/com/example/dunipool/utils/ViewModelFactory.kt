package com.example.dunipool.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dunipool.model.MainRepository
import com.example.dunipool.viewModel.CoinViewModel
import com.example.dunipool.viewModel.MarketViewModel

class MarketViewModelFactory(private val mainRepository: MainRepository , private val application: Application) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return MarketViewModel(application, mainRepository) as T
    }

}

class CoinViewModelFactory(private val mainRepository: MainRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CoinViewModel( mainRepository) as T
    }

}