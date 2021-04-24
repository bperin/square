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
import com.google.android.material.tabs.TabLayout
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_employees, container, false)
    }

    /**
     * Fetch our employees once view exists
     * update our ui as we observe the live data from the view model
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_sort_name)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_sort_team)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_sort_type)))

        tabLayout.addOnTabSelectedListener(tabClickListener)

        recyclerEmployees.layoutManager = LinearLayoutManager(context)
        recyclerEmployees.adapter = employeesAdapter
        employeesAdapter.setClickListener(employeeClickListener)


        employeesViewModel.employeesResult.observe(viewLifecycleOwner, { result ->
            employeesAdapter.clearList()
            when (result.status) {
                Result.Status.SUCCESS -> showSuccessState()
                Result.Status.ERROR -> showErrorState()
                Result.Status.LOADING -> showLoadingState()
                Result.Status.EMPTY -> showEmptyState()

            }
            if (result.status == Result.Status.SUCCESS) {
                result.data?.let { employeesAdapter.setEmployees(it.employees) }
            }

        })

        //click to retry could be timeout or data error
        btnTryAgain.setOnClickListener {
            employeesViewModel.getEmployees(EmployeesViewModel.SortMethod.NAME)
        }

    }

    private fun showLoadingState() {
        layoutLoading.visibility = View.VISIBLE
        progressLoading.visibility = View.VISIBLE
        btnTryAgain.visibility = View.GONE
        tvEmployeeMessage.text = getString(R.string.loading)
    }

    private fun showSuccessState() {
        layoutLoading.visibility = View.GONE
        progressLoading.visibility = View.GONE
    }

    private fun showErrorState() {
        layoutLoading.visibility = View.VISIBLE
        btnTryAgain.visibility = View.VISIBLE
        progressLoading.visibility = View.GONE
        tvEmployeeMessage.text = getString(R.string.an_error_has_occurred)
    }

    private fun showEmptyState() {
        layoutLoading.visibility = View.VISIBLE
        btnTryAgain.visibility = View.VISIBLE
        progressLoading.visibility = View.GONE
        tvEmployeeMessage.text = getString(R.string.empty)
    }

    private val employeeClickListener = object : EmployeeClickListener {
        override fun onClick(employee: Employee, view: Int, position: View) {
            Timber.tag(Constants.TIMBER).d(employee.id.toString())
        }
    }
    private val tabClickListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val position = tab?.position
            Timber.tag(Constants.TIMBER).d(position.toString())
            recyclerEmployees.smoothScrollToPosition(0)

            val method = when (position) {
                0 -> EmployeesViewModel.SortMethod.NAME
                1 -> EmployeesViewModel.SortMethod.TEAM
                2 -> EmployeesViewModel.SortMethod.TYPE
                else -> EmployeesViewModel.SortMethod.NAME
            }
            employeesViewModel.getEmployees(method)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    }
}