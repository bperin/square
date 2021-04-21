package com.brianperin.squaredirectory.viewmodel

import androidx.lifecycle.*
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
import java.util.*
import kotlin.collections.HashMap


class EmployeesViewModel : ViewModel() {

    private val employeesRepo = EmployeesRepo()
    private lateinit var originalList: List<Employee>

    var selected = MutableLiveData<Employee>()
    var employeesResult = MutableLiveData<Result<Employees>>()


    init {
        getEmployees()
    }

    fun getBy(sort: SortMethod) {

//        return sortedMap.get(sort)
    }

    private fun getEmployees() {

        Timber.tag(Constants.TIMBER).d("fetching employees")

        viewModelScope.launch(Dispatchers.IO) {

            employeesResult.postValue(Result.loading())

            val employeeResult = employeesRepo.getEmployees()
            val status = employeeResult.status
            val message: String? = null

            if (status == Result.Status.SUCCESS) {

                try { //validate 200 ok but data is ok
                    employeeResult.data?.let { employees ->
                        employees.employees.forEach {
                            it.validate()
                        }
                    }
                    val nameList: List<Employee>? = employeeResult.data?.employees?.sortedBy { it.fullName }

                    employeesResult.postValue(Result(status, employeeResult.data, null))

                } catch (e: ConstraintViolationException) {
                    Timber.tag(Constants.TIMBER).e(e)
                    employeesResult.postValue(Result(Result.Status.ERROR, null, e.constraintViolations.toString()))
                }

            } else if (status == Result.Status.ERROR) {
                employeesResult.postValue(Result(Result.Status.ERROR, null, message))
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