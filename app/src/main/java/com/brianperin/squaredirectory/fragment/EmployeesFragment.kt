package com.brianperin.squaredirectory.fragment

import android.os.Bundle
import android.view.View
import com.brianperin.squaredirectory.repo.EmployeesRepo
import com.brianperin.squaredirectory.viewmodel.EmployeesViewModel

/**
 * Fragment responsible for syncing our views
 * to view model / repository / domain for fetching employees
 * and updating the adapter used to display list of employees
 */
class EmployeesFragment() : BaseFragment() {

    private val employeesViewModel = EmployeesViewModel()

    companion object {
        fun newInstance() = EmployeesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Fetch our employees once view exists
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        employeesViewModel.getEmployees().observe(viewLifecycleOwner, {
            val employees = it.data
        })

    }
}