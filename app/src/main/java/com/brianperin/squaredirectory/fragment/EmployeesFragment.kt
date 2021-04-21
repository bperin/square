package com.brianperin.squaredirectory.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.brianperin.squaredirectory.R
import com.brianperin.squaredirectory.`interface`.EmployeeClickListener
import com.brianperin.squaredirectory.adapter.EmployeesAdapter
import com.brianperin.squaredirectory.model.response.Employee
import com.brianperin.squaredirectory.network.Result
import com.brianperin.squaredirectory.util.Constants
import com.brianperin.squaredirectory.viewmodel.EmployeesViewModel
import kotlinx.android.synthetic.main.fragment_employees.*
import timber.log.Timber

/**
 * Fragment responsible for syncing our views
 * to view model / repository / domain for fetching employees
 * and updating the adapter used to display list of employees
 */
class EmployeesFragment() : BaseFragment() {

    private val employeesViewModel: EmployeesViewModel by activityViewModels()
    private val employeesAdapter: EmployeesAdapter = EmployeesAdapter()

    companion object {
        fun newInstance() = EmployeesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_employees, container, false)
    }

    /**
     * Fetch our employees once view exists
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerEmployees.layoutManager = LinearLayoutManager(context)
        recyclerEmployees.adapter = employeesAdapter
        employeesAdapter.setClickListener(employeeClickListener)

        employeesViewModel.employeesResult.observe(viewLifecycleOwner, { result ->
            if (result.status == Result.Status.SUCCESS) {
                result.data?.let { employeesAdapter.setEmployees(it.employees) }
            }
        })

    }

    private val employeeClickListener = object : EmployeeClickListener {
        override fun onClick(employee: Employee, view: Int, position: View) {
            Timber.tag(Constants.TIMBER).d(employee.id.toString())
        }
    }
}