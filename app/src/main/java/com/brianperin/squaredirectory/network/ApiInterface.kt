package com.brianperin.squaredirectory.network

import com.brianperin.squaredirectory.model.response.Employees
import retrofit2.Response
import retrofit2.http.GET


/**
 * Interface with urls pointing to employee endpoints
 */
interface ApiInterface {

    @GET("sq-mobile-interview/employees.json")
    suspend fun getEmployees(): Response<Employees>

    //these would be removed once integration testing is available
    @GET("sq-mobile-interview/employees_malformed.json")
    suspend fun getMalformedEmployees(): Response<Employees>

    @GET("sq-mobile-interview/employees_empty.json")
    suspend fun getEmptyEmployees(): Response<Employees>
}