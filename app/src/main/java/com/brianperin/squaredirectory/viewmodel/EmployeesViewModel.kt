package com.brianperin.squaredirectory.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brianperin.squaredirectory.model.response.Employee
import com.brianperin.squaredirectory.model.response.Employees
import com.brianperin.squaredirectory.network.Result
import com.brianperin.squaredirectory.repo.EmployeesRepo
import com.brianperin.squaredirectory.util.Constants
import com.brianperin.squaredirectory.util.validate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.valiktor.ConstraintViolationException
import timber.log.Timber


class EmployeesViewModel : ViewModel() {

    private val employeesRepo = EmployeesRepo()
    private var employees: List<Employee> = emptyList()

    var selected = MutableLiveData<Employee>()
    var employeesResult = MutableLiveData<Result<Employees>>()

    fun sort(sort: SortMethod) {

    }

    /**
     * First fetch of employees repo will return whats in memory but its not persisted to
     * any type of cache
     */
     fun getEmployees() {

        Timber.tag(Constants.TIMBER).d("fetching employees")

        viewModelScope.launch(Dispatchers.IO) {

            employeesResult.postValue(Result.loading())
            val employeeResult = employeesRepo.getEmployees()
            val status = employeeResult.status

            if (status == Result.Status.SUCCESS) { //validate 200 ok but check if also valid

                try {
                    employeeResult.data?.validate()
                    employees = employeeResult.data?.employees!!
                    employeesResult.postValue(Result(status, employeeResult.data, null))

                } catch (e: ConstraintViolationException) {
                    Timber.tag(Constants.TIMBER).e(e)
                    employeesResult.postValue(Result(Result.Status.ERROR, null, e.constraintViolations.toString()))
                }

            } else if (status == Result.Status.ERROR) { //true http error
                Timber.tag(Constants.TIMBER).e(employeeResult.message)
                employeesResult.postValue(Result(Result.Status.ERROR, null, employeeResult.message))
            }
        }
    }

    enum class SortMethod {
        NAME, TEAM, TYPE
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(Constants.TIMBER).d("cleared")
    }
}