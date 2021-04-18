package com.brianperin.squaredirectory.repo

import com.brianperin.squaredirectory.network.ApiClient
import com.brianperin.squaredirectory.network.BaseDataSource

/**
 * Repository layer for fetching list of employees
 * no caching network calls
 */
class EmployeesRepo() : BaseDataSource() {

    suspend fun getEmployees() = getResult { ApiClient.apiService.getEmployees() }
}