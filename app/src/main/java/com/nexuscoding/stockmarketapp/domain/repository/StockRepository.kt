package com.nexuscoding.stockmarketapp.domain.repository

import com.nexuscoding.stockmarketapp.domain.model.CompanyListing
import com.nexuscoding.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}