package com.nexuscoding.stockmarketapp.data.remote.dto

import com.squareup.moshi.Json

data class IntradayInfoDto(
    val timeStamp: String,
    val close: Double
)
