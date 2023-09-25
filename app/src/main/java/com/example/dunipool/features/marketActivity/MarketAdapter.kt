package com.example.dunipool.features.marketActivity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dunipool.R
import com.example.dunipool.utils.BASE_URL_IMAGE
import com.example.dunipool.databinding.CoinsItemBinding
import com.example.dunipool.model.models.CoinsInfo

class MarketAdapter (private val data: ArrayList<CoinsInfo.Data>, private val recyclerCallback: RecyclerCallback) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {


    inner class MarketViewHolder(private val binding: CoinsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData (position: Int) {
            val coinInfo = data[position].coinInfo
            val usd = data[position].rAW.uSD
            val display = data[position].dISPLAY

            binding.coinNameTxt.text = coinInfo.fullName


//            val dot = usd.pRICE.toString().indexOf('.')
//
//            // دو رقم بعد از اعشار داریم :
//
//            if (dot + 3 <= usd.pRICE.toString().length-1) {
//
//                binding.coinPriceTxt.text = "$" + usd.pRICE.toString().substring(0 , dot + 3)
//
//            } else if (dot + 2 <= usd.pRICE.toString().length-1) {
//
//                binding.coinPriceTxt.text = "$" + usd.pRICE.toString().substring(0 , dot + 2)
//
//            }

            binding.coinPriceTxt.text = display.uSD.pRICE

            val marketCap = usd.mKTCAP / 1000000000
            val indexDot = marketCap.toString().indexOf('.')


            if (marketCap > 0) {
                if (indexDot + 3 <= marketCap.toString().length - 1) {

                    binding.coinMarketCapTxt.text = "$" +  marketCap.toString().substring(0 , indexDot + 3) + " B"

                } else if (indexDot + 2 <= marketCap.toString().length - 1) {

                    binding.coinMarketCapTxt.text = "$" +  marketCap.toString().substring(0 , indexDot + 2) + " B"

                }


            } else {

                binding.coinMarketCapTxt.text = "$0"


            }







            val taghir = usd.cHANGEPCT24HOUR
            if (taghir < 0) {


                binding.coinTaghirTxt.setTextColor(ContextCompat.getColor(itemView.context , R.color.colorLoss))
                binding.coinTaghirTxt.text = usd.cHANGEPCT24HOUR.toString().substring(0,5) + "%"

            }else if (taghir > 0) {

                binding.coinTaghirTxt.setTextColor(ContextCompat.getColor(itemView.context , R.color.colorGain))
                binding.coinTaghirTxt.text =  usd.cHANGEPCT24HOUR.toString().substring(0,4) + "%"

            }else {

                binding.coinTaghirTxt.text = "0%"
            }


            Glide.with(itemView.context)
                .load(BASE_URL_IMAGE +coinInfo.imageUrl)
                .transform()
                .into(binding.coinImgItem)

            itemView.setOnClickListener {


                recyclerCallback.onCoinItemClicked(data[position])

            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val binding = CoinsItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return MarketViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bindData(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showData(newData : ArrayList<CoinsInfo.Data>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()


    }

    interface RecyclerCallback {
        fun onCoinItemClicked(dataCoin : CoinsInfo.Data )

    }
}