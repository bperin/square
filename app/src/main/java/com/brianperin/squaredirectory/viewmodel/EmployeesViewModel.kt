package com.brianperin.squaredirectory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.brianperin.squaredirectory.model.response.Employee
import com.brianperin.squaredirectory.network.Result
import com.brianperin.squaredirectory.repo.EmployeesRepo
import com.brianperin.squaredirectory.util.validate
import kotlinx.coroutines.Dispatchers
import org.valiktor.ConstraintViolationException
import timber.log.Timber


class EmployeesViewModel : ViewModel() {

    private val employeesRepo = EmployeesRepo()
    private var employees: List<Employee> = emptyList()

    init {
        getEmployees()
    }


    fun getEmployees(): LiveData<Result<List<Employee>>> {

        return liveData(Dispatchers.IO) {

            emit(Result.loading())

            val response = employeesRepo.getEmployees()
            employees = response.data?.employees!!
            var status = response.status
            var message: String? = null

            //got success back need to validate objects if exception caught empty list
            if (status == Result.Status.SUCCESS) {

                try {
                    employees.forEach { employee ->
                        employee.validate()
                    }
                } catch (e: ConstraintViolationException) {
                    e.printStackTrace()
                    status = Result.Status.ERROR
                    employees = emptyList()
                    message = "malformed json"
                }
            }
            emit(Result(status, employees, message))
        }
    }

    fun sortBy(sort: SortMethod): List<Employee> {
        if (employees.isNotEmpty()) {
            return employees.sortedBy { it.fullName }.toList()
        }
        return employees.sortedBy { it.team }.toList()
        return emptyList()
    }

    enum class SortMethod {
        NAME, TEAM, TYPE
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag("view model").d("cleared")
    }
}