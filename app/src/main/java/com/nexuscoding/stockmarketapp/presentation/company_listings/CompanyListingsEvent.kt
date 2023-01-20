package com.nexuscoding.stockmarketapp.presentation.company_listings

/**
 * The purpose of this class is to have a list of all possible events that can occur on a single
 * screen. For example the CompanyListings Screen allows two things:
 * 1. To Swipe down to refresh the list of companies
 * 2. Put in a search query in the search bar to search any particular company
 */

sealed class CompanyListingsEvent {
    object Refresh: CompanyListingsEvent()
    data class OnSearchQueryChange(val query: String): CompanyListingsEvent()
}
