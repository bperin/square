package com.brianperin.squaredirectory.repo

import com.brianperin.squaredirectory.model.response.Employees
import com.brianperin.squaredirectory.network.ApiClient
import com.brianperin.squaredirectory.network.BaseDataSource
import com.brianperin.squaredirectory.network.Result


/**
 * Repository layer for fetching list of employees
 * no caching network calls
 * here we could add a cache if we wanted
 * Dont cache an error result
 */
class EmployeesRepo() : BaseDataSource() {

    private var employeesResult: Result<Employees>? = null

    suspend fun getEmployees(): Result<Employees> {
        return if (employeesResult == null) {
            val result = getResult { ApiClient.apiService.getEmployees() }
            if (result.status != Result.Status.ERROR) {
                employeesResult = result
            }
            return result
        } else {
            employeesResult as Result<Employees>
        }
    }
}