package com.example.dunipool.features.coinActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.dunipool.R
import com.example.dunipool.utils.ALL
import com.example.dunipool.utils.BASE_URL_TWITTER
import com.example.dunipool.utils.HOUR
import com.example.dunipool.utils.HOURS24
import com.example.dunipool.utils.MONTH
import com.example.dunipool.utils.MONTH3
import com.example.dunipool.utils.NetworkChecker
import com.example.dunipool.utils.WEEK
import com.example.dunipool.utils.YEAR
import com.example.dunipool.databinding.ActivityCoinBinding
import com.example.dunipool.features.marketActivity.COIN_ABOUT_DATA
import com.example.dunipool.features.marketActivity.COIN_DATA
import com.example.dunipool.model.MainRepository
import com.example.dunipool.model.models.ChartData
import com.example.dunipool.model.models.CoinAboutItem
import com.example.dunipool.model.models.CoinsInfo
import com.example.dunipool.utils.ApiServiceSingleton
import com.example.dunipool.utils.CoinViewModelFactory
import com.example.dunipool.viewModel.CoinViewModel
import com.google.android.material.snackbar.Snackbar

class CoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoinBinding
    private lateinit var dataThisCoin: CoinsInfo.Data
    private lateinit var coinAboutItem: CoinAboutItem
    private lateinit var coinViewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        coinViewModel = ViewModelProvider(
            this,
            CoinViewModelFactory(MainRepository(ApiServiceSingleton.apiService!!))
        )[CoinViewModel::class.java]
        initUi()
    }


    private fun initUi() {

        if (checkedInternet()) {
            checkDataFromIntent()
            initAboutUi()
            initStatisticUi()
            initChartUi()
        } else {
            binding.moduleToolBarCoin.toolBarLayoutMain.title = "Waiting for network..."
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

                initUi()

            })

            .setActionTextColor(ContextCompat.getColor(this, R.color.white))
            .setActionTextColor(ContextCompat.getColor(this, R.color.black))
            .setTextColor(ContextCompat.getColor(this, R.color.black))
            .show()
    }

    private fun checkedInternet(): Boolean {


        val networkChecker = NetworkChecker(this)

        return networkChecker.isInternetConnected


    }

    private fun checkDataFromIntent() {

        dataThisCoin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(COIN_DATA, CoinsInfo.Data::class.java)!!


        } else {

            intent.getParcelableExtra<CoinsInfo.Data>(COIN_DATA)!!

        }
        binding.moduleToolBarCoin.toolBarLayoutMain.title = dataThisCoin.coinInfo.fullName


        coinAboutItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(COIN_ABOUT_DATA, CoinAboutItem::class.java)!!


        } else {

            intent.getParcelableExtra<CoinAboutItem>(COIN_ABOUT_DATA)!!

        }


    }

    @SuppressLint("SetTextI18n")
    private fun initChartUi() {
        var period: String = HOUR
        requestAndShowChart(period)
        binding.moduleChartCoin.radioGroupMain.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.radio_12h -> {
                    period = HOUR

                }

                R.id.radio_1Day -> {
                    period = HOURS24

                }

                R.id.radio_1Y -> {
                    period = YEAR

                }

                R.id.radio_1M -> {
                    period = MONTH

                }

                R.id.radio_3M -> {
                    period = MONTH3

                }

                R.id.radio_1w -> {
                    period = WEEK

                }

                R.id.radio_all -> {
                    period = ALL

                }


            }

            if (checkedInternet()) {
                requestAndShowChart(period)

            } else {
                Toast.makeText(this, " Please Checked Internet !", Toast.LENGTH_SHORT).show()
            }

        }
        binding.moduleChartCoin.priceChartTxt.text = dataThisCoin.dISPLAY.uSD.pRICE
        binding.moduleChartCoin.chartChange1.text = " " + dataThisCoin.dISPLAY.uSD.cHANGE24HOUR
        val taghir = dataThisCoin.rAW.uSD.cHANGEPCT24HOUR
        if (taghir < 0) {

            binding.moduleChartCoin.chartChange2.text =
                dataThisCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 5) + "%"

            binding.moduleChartCoin.priceChartUpdown.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorLoss
                )
            )
            binding.moduleChartCoin.chartChange2.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorLoss
                )
            )
            binding.moduleChartCoin.priceChartUpdown.text = "▼"

            binding.moduleChartCoin.sparkView.lineColor =
                ContextCompat.getColor(this, R.color.colorLoss)

        } else if (taghir > 0) {
            binding.moduleChartCoin.chartChange2.text =
                dataThisCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 4) + "%"

            binding.moduleChartCoin.priceChartUpdown.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorGain
                )
            )
            binding.moduleChartCoin.chartChange2.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorGain
                )
            )
            binding.moduleChartCoin.priceChartUpdown.text = "▲"
            binding.moduleChartCoin.sparkView.lineColor =
                ContextCompat.getColor(this, R.color.colorGain)


        } else {

            binding.moduleChartCoin.chartChange1.text = "0%"
            binding.moduleChartCoin.priceChartUpdown.text = "▲"
            binding.moduleChartCoin.priceChartUpdown.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.tertiaryTextColor
                )
            )


        }

        binding.moduleChartCoin.sparkView.setScrubListener {
            if (it == null) {
                binding.moduleChartCoin.priceChartTxt.text = dataThisCoin.dISPLAY.uSD.pRICE

            } else {
                binding.moduleChartCoin.priceChartTxt.text =
                    "$" + (it as ChartData.Data.Data).close.toString()


            }
        }


    }
    private fun requestAndShowChart(period: String) {
        coinViewModel.getChartData(
            dataThisCoin.coinInfo.name, period).observe(this) {
            val chartAdapter = ChartAdapter(it.first, it.second?.open.toString())
            binding.moduleChartCoin.sparkView.adapter = chartAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initStatisticUi() {

        binding.moduleStatisticCoin.tvOpenAmount.text = dataThisCoin.dISPLAY.uSD.oPEN24HOUR
        binding.moduleStatisticCoin.tvTodaysHighAmount.text = dataThisCoin.dISPLAY.uSD.hIGH24HOUR
        binding.moduleStatisticCoin.tvTodayLowAmount.text = dataThisCoin.dISPLAY.uSD.lOW24HOUR
        binding.moduleStatisticCoin.tvChangeTodayAmount.text = dataThisCoin.dISPLAY.uSD.cHANGE24HOUR
        binding.moduleStatisticCoin.tvAlgorithm.text = dataThisCoin.coinInfo.algorithm
        binding.moduleStatisticCoin.tvTotalVolume.text = dataThisCoin.dISPLAY.uSD.tOTALVOLUME24H
        binding.moduleStatisticCoin.tvAvgMarketCapAmount.text = dataThisCoin.dISPLAY.uSD.mKTCAP
        binding.moduleStatisticCoin.tvSupplyNumber.text = dataThisCoin.dISPLAY.uSD.sUPPLY
    }

    @SuppressLint("SetTextI18n")
    private fun initAboutUi() {

        binding.moduleAboutCoin.txtReddit.text = coinAboutItem.coinReddit
        binding.moduleAboutCoin.txtGithub.text = coinAboutItem.coinGit
        binding.moduleAboutCoin.txtWebsite.text = coinAboutItem.coinWeb
        binding.moduleAboutCoin.txtAboutCoin.text = coinAboutItem.coinDesc

        if (binding.moduleAboutCoin.txtTwitter.text.isEmpty()) {
            binding.moduleAboutCoin.txtTwitter.text = "no-data"
            binding.moduleAboutCoin.txtTwitter.isClickable = false
        }
        if (binding.moduleAboutCoin.txtReddit.text.isEmpty()) {
            binding.moduleAboutCoin.txtReddit.text = "no-data"
            binding.moduleAboutCoin.txtReddit.isClickable = false
        }
        if (binding.moduleAboutCoin.txtGithub.text.isEmpty()) {
            binding.moduleAboutCoin.txtGithub.text = "no-data"

            binding.moduleAboutCoin.txtGithub.isClickable = false
        }
        if (binding.moduleAboutCoin.txtWebsite.text.isEmpty()) {
            binding.moduleAboutCoin.txtWebsite.text = "no-data"
            binding.moduleAboutCoin.txtWebsite.isClickable = false
        }

        if (coinAboutItem.coinTwitter == "no-data") {
            binding.moduleAboutCoin.txtTwitter.text = coinAboutItem.coinTwitter

        } else {
            binding.moduleAboutCoin.txtTwitter.text = "@" + coinAboutItem.coinTwitter

        }

        if (binding.moduleAboutCoin.txtGithub.text != "no-data") {
            binding.moduleAboutCoin.txtGithub.setOnClickListener {
                openWebSiteDataCoin(binding.moduleAboutCoin.txtGithub.text.toString())

            }

        } else {

            binding.moduleAboutCoin.txtGithub.isClickable = false
        }


        if (binding.moduleAboutCoin.txtWebsite.text != "no-data") {

            binding.moduleAboutCoin.txtWebsite.setOnClickListener {
                openWebSiteDataCoin(binding.moduleAboutCoin.txtWebsite.text.toString())

            }

        } else {

            binding.moduleAboutCoin.txtWebsite.isClickable = false
        }


        if (binding.moduleAboutCoin.txtReddit.text != "no-data") {


            binding.moduleAboutCoin.txtReddit.setOnClickListener {
                openWebSiteDataCoin(binding.moduleAboutCoin.txtReddit.text.toString())

            }

        } else {

            binding.moduleAboutCoin.txtReddit.isClickable = false
        }
        if (binding.moduleAboutCoin.txtTwitter.text != "no-data") {


            binding.moduleAboutCoin.txtTwitter.setOnClickListener {
                openWebSiteDataCoin(BASE_URL_TWITTER + coinAboutItem.coinTwitter)

            }

        } else {

            binding.moduleAboutCoin.txtTwitter.isClickable = false
        }


    }

    private fun openWebSiteDataCoin(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }
}