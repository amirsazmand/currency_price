package com.example.dunipool.features.marketActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dunipool.R
import com.example.dunipool.utils.NetworkChecker

import com.example.dunipool.databinding.ActivityMarketBinding
import com.example.dunipool.features.coinActivity.CoinActivity
import com.example.dunipool.model.models.CoinAboutItem
import com.example.dunipool.model.models.CoinsInfo
import com.example.dunipool.model.MainRepository
import com.example.dunipool.utils.ApiServiceSingleton
import com.example.dunipool.utils.MarketViewModelFactory
import com.example.dunipool.viewModel.MarketViewModel
import com.google.android.material.snackbar.Snackbar

const val COIN_DATA = "coin_data"
const val COIN_ABOUT_DATA = "coin_about_data"

class MarketActivity : AppCompatActivity(), MarketAdapter.RecyclerCallback {
    private lateinit var binding: ActivityMarketBinding
    private lateinit var dataNews: ArrayList<Pair<String, String>>
    private lateinit var adapter: MarketAdapter
    private lateinit var aboutDataMap: MutableMap<String, CoinAboutItem>
    private lateinit var marketViewModel: MarketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.moduleToolBarMain.toolBarLayoutMain.title = "DuniPool Market"
        initRecycler()
        marketViewModel = ViewModelProvider(
            this, MarketViewModelFactory(
                MainRepository(ApiServiceSingleton.apiService!!), application
            )
        )[MarketViewModel::class.java]

        dataNews = arrayListOf()
        initUI()
        getCoinsAboutDataFromAssets()
        binding.moduleWatchlistMain.moreBtn.setOnClickListener {

            val url = "https://www.livecoinwatch.com/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)

        }
        binding.swipeRefreshMain.setOnRefreshListener {
            if (checkedInternet()) {
                getNewsFromApi()
                getTopCoinsFromApi()
            } else {
                Toast.makeText(this, " Please Checked Internet !", Toast.LENGTH_SHORT).show()
            }

            Handler(Looper.getMainLooper()).postDelayed({

                binding.swipeRefreshMain.isRefreshing = false

            }, 1500)
        }

    }

    private fun initUI() {
        if (checkedInternet()) {
            getNewsFromApi()
            getTopCoinsFromApi()
            binding.moduleWatchlistMain.moreBtn.visibility = View.VISIBLE
            binding.moduleNewsMain.viewsImg.visibility = View.VISIBLE
        } else {
            binding.moduleWatchlistMain.moreBtn.visibility = View.INVISIBLE
            binding.moduleNewsMain.viewsImg.visibility = View.INVISIBLE
            showError()
        }
    }

    private fun showError() {
        Snackbar.make(
            this,
            binding.root,
            "Please check your connection status !",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Retry", View.OnClickListener {
                if (checkedInternet()) {
                    initUI()

                } else {

                    showError()
                }

            })
            .setBackgroundTint(ContextCompat.getColor(this, R.color.white))
            .setActionTextColor(ContextCompat.getColor(this, R.color.black))
            .setTextColor(ContextCompat.getColor(this, R.color.black))
            .show()
    }

    private fun checkedInternet(): Boolean {


        val networkChecker = NetworkChecker(this)

        return networkChecker.isInternetConnected


    }

    private fun getCoinsAboutDataFromAssets() {
       val dataAboutAll = marketViewModel.getCoinsAboutDataFromAssets()
        aboutDataMap = mutableMapOf()
        dataAboutAll.forEach {
            aboutDataMap[it.currencyName] = CoinAboutItem(
                it.info.reddit,
                it.info.web,
                it.info.twt,
                it.info.desc,
                it.info.github
            )


        }


    }

    private fun getTopCoinsFromApi() {


        marketViewModel.getTopCoins().observe(this) {
            adapter.showData(ArrayList(it))
        }

    }

    private fun initRecycler() {
        val data = arrayListOf<CoinsInfo.Data>()
        binding.moduleWatchlistMain.recyclerViewCoins.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MarketAdapter(data, this)
        binding.moduleWatchlistMain.recyclerViewCoins.adapter = adapter
    }

    private fun getNewsFromApi() {
        marketViewModel.getTopNews().observe(this) {

            dataNews = it
            refreshNews()

        }

    }

    private fun refreshNews() {

        val randomAccess = (0 until dataNews.size).random()
        binding.moduleNewsMain.viewsImg.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataNews[randomAccess].second))
            startActivity(intent)
        }

        binding.moduleNewsMain.newsTxt.text = dataNews[randomAccess].first
        binding.moduleNewsMain.newsTxt.setOnClickListener {

            refreshNews()

        }

    }


    override fun onCoinItemClicked(dataCoin: CoinsInfo.Data) {
        val intent = Intent(this, CoinActivity::class.java)
        val coinAboutItem = aboutDataMap[dataCoin.coinInfo.name]
        intent.putExtra(COIN_DATA, dataCoin)
        if (coinAboutItem != null) {
            intent.putExtra(COIN_ABOUT_DATA, coinAboutItem)

        } else {
            val coinAboutItem1 = CoinAboutItem()
            intent.putExtra(COIN_ABOUT_DATA, coinAboutItem1)

        }
        startActivity(intent)
    }
}