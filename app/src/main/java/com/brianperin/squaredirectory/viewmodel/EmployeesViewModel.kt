package com.brianperin.squaredirectory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.brianperin.squaredirectory.model.response.Employees
import com.brianperin.squaredirectory.network.Result
import com.brianperin.squaredirectory.repo.EmployeesRepo
import kotlinx.coroutines.Dispatchers


class EmployeesViewModel : ViewModel() {

    private val employeesRepo = EmployeesRepo()
    private var unsortedEmployees: Employees? = null

    fun getEmployees(): LiveData<Result<Employees>> {

        return liveData(Dispatchers.IO) {

            emit(Result.loading())

            unsortedEmployees = employeesRepo.getEmployees().data

            val cachedResult = Result(Result.Status.SUCCESS, unsortedEmployees, null)

            emit(cachedResult)
        }
    }

    fun sortBy(sort: SortMethod) {

    }

    enum class SortMethod {
        NAME, TEAM, TYPE
    }
}