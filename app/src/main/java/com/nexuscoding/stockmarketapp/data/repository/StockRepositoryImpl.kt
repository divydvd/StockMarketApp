package com.nexuscoding.stockmarketapp.data.repository

import com.nexuscoding.stockmarketapp.data.csv.CSVParser
import com.nexuscoding.stockmarketapp.data.csv.CompanyListingParser
import com.nexuscoding.stockmarketapp.data.local.StockDatabase
import com.nexuscoding.stockmarketapp.data.mapper.toCompanyListing
import com.nexuscoding.stockmarketapp.data.mapper.toCompanyListingEntity
import com.nexuscoding.stockmarketapp.data.remote.StockApi
import com.nexuscoding.stockmarketapp.domain.model.CompanyListing
import com.nexuscoding.stockmarketapp.domain.repository.StockRepository
import com.nexuscoding.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api: StockApi,
    val db: StockDatabase,
    val companyListingsParser: CSVParser<CompanyListing>
): StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            /**
             * we check that query.isBlank because if there is a search for say xyz,
             * then there might not be any company with that name,
             * however, it doesn't imply that the localListings is empty
             */
            val isDbEmpty = localListings.isEmpty() && query.isBlank()

            /**
             * we make the api call for the first time and after that we load from cache
             * until there is a request by swiping down to refresh the list
             */
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Could not load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Could not load data"))
                null
            }

            /**
             * Following the single source of truth principle (SST) we first make an API call
             * clear the database from the old values
             * insert the new/updated values and then emit the data to UI from the the database
             * so that there is only one single source of data (API)
             */

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }
}