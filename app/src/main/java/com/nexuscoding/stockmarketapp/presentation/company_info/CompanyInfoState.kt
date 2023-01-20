package com.nexuscoding.stockmarketapp.presentation.company_info

import com.nexuscoding.stockmarketapp.domain.model.CompanyInfo
import com.nexuscoding.stockmarketapp.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
