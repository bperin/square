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

/**
 * Main view model for fetching employees, validating json is well formatted
 * and returning an output to any observers, we want to make this call only once per
 * app lifecycle once the parent activity has been destroyed it will get GC
 * This is also set up to be shared among different views.
 * We also have a basic sorting method for how we want to display results
 */
class EmployeesViewModel : ViewModel() {

    private val employeesRepo = EmployeesRepo()
    var employeesResult = MutableLiveData<Result<Employees>>()

    /**
     * First fetch of employees repo will return whats in memory but its not persisted to
     * any type of cache
     */
    fun getEmployees(sortMethod: SortMethod?) {

        Timber.tag(Constants.TIMBER).d("fetching employees")

        viewModelScope.launch(Dispatchers.IO) {

            employeesResult.postValue(Result.loading())
            val employeeResult = employeesRepo.getEmployees()
            val status = employeeResult.status

            if (status == Result.Status.SUCCESS) { //validate 200 ok but check if data is valid

                try {
                    employeeResult.data?.validate()

                    sortMethod?.let {
                        when (it) {
                            SortMethod.NAME -> employeeResult.data?.sortByName()
                            SortMethod.TEAM -> employeeResult.data?.sortByTeam()
                            SortMethod.TYPE -> employeeResult.data?.sortByType()
                            else -> employeeResult.data?.sortByName()
                        }
                    }
                    if (employeeResult.data?.employees.isNullOrEmpty()) {
                        employeesResult.postValue(Result(Result.Status.EMPTY, null, null))
                    } else {
                        employeesResult.postValue(Result(status, employeeResult.data, null))
                    }
                } catch (e: ConstraintViolationException) {
                    Timber.tag(Constants.TIMBER).e(e)
                    employeesResult.postValue(Result(Result.Status.ERROR, Employees(emptyList()), e.constraintViolations.toString()))
                    //we'll pprobably override the message to the user its not helpful and log the real issue
                }

            } else if (status == Result.Status.ERROR) { //true http error
                Timber.tag(Constants.TIMBER).e(employeeResult.message)
                employeesResult.postValue(Result(Result.Status.ERROR, Employees(emptyList()), employeeResult.message))
                //same here in displaying the message
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