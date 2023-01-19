package com.nexuscoding.stockmarketapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntities: List<CompanyListingEntity>
    )

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListings()

    /**
     * In the query || means to concatenate just like we use + in Kotlin or some other language
     * Let's say our search query is tEs then our query will frist convert it to tes and add % on both sides of the query, i.e. %tes%
     * LIKE operator is used to check if the name to lower case contains tes
     * or if we convert query to Upper case and we find any match to the name of the Company
     */
    @Query(
        """SELECT * FROM companylistingentity WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR UPPER(:query) == symbol"""
    )
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>
}