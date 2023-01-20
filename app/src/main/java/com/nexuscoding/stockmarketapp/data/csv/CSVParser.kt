package com.nexuscoding.stockmarketapp.data.csv

import java.io.InputStream

/**
 * A Generic is created to convert a type of input stream into a list of that type
 */
interface CSVParser<T> {
    suspend fun parse(stream: InputStream): List<T>
}