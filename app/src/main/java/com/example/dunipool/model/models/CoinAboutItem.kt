package com.example.dunipool.model.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoinAboutItem(
    val coinReddit : String? = "no-data" ,
    val coinWeb : String? = "no-data" ,
    val coinTwitter : String? = "no-data" ,
    val coinDesc : String? = "no-data" ,
    val coinGit : String? = "no-data"

) : Parcelable
