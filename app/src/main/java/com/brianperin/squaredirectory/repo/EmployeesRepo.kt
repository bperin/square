package com.brianperin.squaredirectory.repo

import com.brianperin.squaredirectory.model.response.Employees
import com.brianperin.squaredirectory.network.ApiClient
import com.brianperin.squaredirectory.network.BaseDataSource
import com.brianperin.squaredirectory.network.Result


/**
 * Repository layer for fetching list of employees
 * no caching network calls
 * here we could add a cache if we wanted
 */
class EmployeesRepo() : BaseDataSource() {

    private var employeesResult: Result<Employees>? = null

    suspend fun getEmployees(): Result<Employees> {
        return if (employeesResult == null) {
            employeesResult = getResult { ApiClient.apiService.getEmployees() }
            employeesResult as Result<Employees>
        } else {
            employeesResult as Result<Employees>
        }

    }
}